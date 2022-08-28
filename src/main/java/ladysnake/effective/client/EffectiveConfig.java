package ladysnake.effective.client;

import eu.midnightdust.lib.config.MidnightConfig;

public class EffectiveConfig extends MidnightConfig {
    @Entry
    public static boolean generateCascades = true;

    @Entry
    public static boolean generateSplashes = true;

    @Entry
    public static boolean shouldFlowingWaterSpawnParticlesOnFirstTick = true;

    @Entry(min = 0, max = 100)
    public static int lapisBlockUpdateParticleChance = 10;

    @Entry(max = 150)
    public static int waterfallSoundDistanceBlocks = 36;
    @Entry(max = 100)
    public static int waterfallSoundVolume = 100;
}