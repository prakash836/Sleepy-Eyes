package com.example.sleepyeyes;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

/**
 * Draws two black "eyelids" that slowly close from the top and bottom of the
 * screen while the player is sleeping, and open again quickly on waking.
 */
public class SleepyEyesClient implements ClientModInitializer {

    /** Seconds the eyes take to fully close while sleeping. */
    private static final float CLOSE_SECONDS = 8.0f;
    /** Seconds the eyes take to open after waking up. */
    private static final float OPEN_SECONDS = 0.6f;

    private static float progress = 0f; // 0 = open, 1 = fully closed
    private static long lastNanos = System.nanoTime();

    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register(SleepyEyesClient::render);
    }

    private static void render(DrawContext ctx, RenderTickCounter tickCounter) {
        MinecraftClient mc = MinecraftClient.getInstance();

        long now = System.nanoTime();
        float dt = Math.min((now - lastNanos) / 1_000_000_000f, 0.1f);
        lastNanos = now;

        boolean sleeping = mc.player != null && mc.player.isSleeping();
        if (sleeping) {
            progress = Math.min(1f, progress + dt / CLOSE_SECONDS);
        } else {
            progress = Math.max(0f, progress - dt / OPEN_SECONDS);
        }
        if (progress <= 0.001f) return;

        // Smoothstep easing so the lids start slow, drift, then settle shut.
        float eased = progress * progress * (3f - 2f * progress);

        // Drowsy flutter: lids briefly twitch back open early on.
        float flutter = 0f;
        if (sleeping && progress < 0.8f) {
            flutter = (float) Math.sin(progress * 28f) * 0.03f * (1f - progress);
        }
        float lidAmount = Math.max(0f, Math.min(1f, eased + flutter));

        int w = ctx.getScaledWindowWidth();
        int h = ctx.getScaledWindowHeight();

        int lid = (int) (lidAmount * 0.5f * h);

        // Solid part of each lid.
        ctx.fill(0, 0, w, lid, 0xFF000000);
        ctx.fill(0, h - lid, w, h, 0xFF000000);

        // Soft, blurry edge that tightens as the eyes shut completely.
        int soft = (int) (h / 10f * (1f - lidAmount));
        for (int i = 0; i < soft; i += 2) {
            int alpha = (int) (255f * (1f - (float) i / soft));
            int color = (alpha << 24);
            ctx.fill(0, lid + i, w, lid + i + 2, color);
            ctx.fill(0, h - lid - i - 2, w, h - lid - i, color);
        }
    }
}
