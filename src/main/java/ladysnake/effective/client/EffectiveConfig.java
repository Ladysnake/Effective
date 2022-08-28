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
    public static long lapisBlockUpdateParticleChance = 10;
}