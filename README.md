# Sleepy Eyes (Fabric mod, Minecraft Java 1.21.4)

When you lie down in a bed, black eyelids slowly close over your screen
(about 8 seconds, with a little drowsy flutter and a soft blurry edge).
When you wake up, they open quickly (about 0.6 seconds).

It is client-side only, so it works in singleplayer and on any server.

## Build it (option A: on your computer)

1. Install **JDK 21**. You do NOT need to install Gradle; the included
   wrapper downloads it for you.
2. Open a terminal in this folder and run:
   - Windows: `gradlew build`
   - Mac/Linux: `./gradlew build`
3. Your mod jar will be in `build/libs/sleepy-eyes-1.0.0.jar`
   (use the one without `-sources`).

## Build it (option B: online, nothing to install)

1. Create a free GitHub account and a new repository.
2. Upload all the files from this folder (keep the `.github` folder).
3. Open the **Actions** tab, wait for "Build mod" to finish (a few minutes),
   click the run, and download **sleepy-eyes-jar** under Artifacts.
4. Unzip it and use `sleepy-eyes-1.0.0.jar` (not the `-sources` one).

## Install it

1. Install the Fabric Loader for 1.21.4 from https://fabricmc.net/use/
2. Download Fabric API for 1.21.4 and put it in your `mods` folder.
3. Put `sleepy-eyes-1.0.0.jar` in the same `mods` folder.
4. Launch the game, place a bed, and sleep.

## Compatibility with animation mods (Punchy, Fluid-style first-person mods, etc.)

Sleepy Eyes is built to stay out of the way of first-person and combat
animation mods:

- It uses **no mixins** and replaces no renderers. It does not touch the
  hand, held item, camera, or player model, which is where animation mods
  do their work.
- It only draws a black overlay on the HUD layer, and only while the game
  reports the player as sleeping, so punch, swing, and movement animations
  are unaffected.
- It does not depend on any other mod except Fabric API.
- The vanilla "Leave Bed" button still appears on top of the eyelids.

If an animation mod you use changes how the sleeping camera looks, the eyelids
will simply draw over that view. If you find a mod that clashes, tell me its
exact name and version and I can adjust it.

## Tweak it

Open `src/client/java/com/example/sleepyeyes/SleepyEyesClient.java`:

- `CLOSE_SECONDS`: how long the eyes take to close.
- `OPEN_SECONDS`: how fast they open after waking.
- The `flutter` line controls the drowsy twitching (set it to `0f` to remove).
