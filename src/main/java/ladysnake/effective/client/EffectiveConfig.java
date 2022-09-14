package ladysnake.effective.client;

import eu.midnightdust.lib.config.MidnightConfig;

public class EffectiveConfig extends MidnightConfig {
    @Entry
    public static boolean generateSplashes = true;

    @Entry(min = 0f, max = 1.0f)
    public static float splashRimAlpha = 0.8f;

    @Entry
    public static boolean generateCascades = true;

    @Entry(min = 0, max = 500)
    public static int cascadeSoundsVolumeMultiplier = 100;

    @Entry(min = 0, max = 400)
    public static int waterfallSoundDistanceBlocks = 150;

    @Entry
    public static boolean shouldFlowingWaterSpawnParticlesOnFirstTick = true;

    @Entry(min = 0, max = 100)
    public static int lapisBlockUpdateParticleChance = 10;
}