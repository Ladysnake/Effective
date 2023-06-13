package ladysnake.effective;

import eu.midnightdust.lib.config.MidnightConfig;

public class EffectiveConfig extends MidnightConfig {
	@Entry
	public static boolean enableSplashes = true;

	@Entry(min = 0f, max = 1.0f)
	public static float splashRimAlpha = 0.7f;

	@Entry
	public static boolean enableCascades = true;

	@Entry(min = 0, max = 500)
	public static int cascadeSoundsVolumeMultiplier = 100;

	@Entry(min = 0, max = 400)
	public static int cascadeSoundDistanceBlocks = 150;

	@Entry
	public static boolean shouldFlowingWaterSpawnParticlesOnFirstTick = true;

	@Entry(min = 0, max = 100)
	public static int lapisBlockUpdateParticleChance = 10;

	@Entry(min = 0, max = 200)
	public static int flowingWaterSplashingDensity = 50;

	@Entry(min = 0, max = 10)
	public static int rainRippleDensity = 1;

	@Entry
	public static boolean enableGlowingPlankton = true;

	@Entry
	public static boolean glowsquidHypnotize = true;

	@Entry
	public static boolean glowsquidHypnotizeAttractCursor = true;

	@Entry
	public static boolean enableAllayTrails = true;

	@Entry(min = 0, max = 100)
	public static int allayTwinkleDensity = 5;

	@Entry
	public static boolean goldenAllays = true;

	@Entry
	public static boolean wardenScreenShake = true;

	@Entry
	public static boolean sonicBoomScreenShake = true;

	@Entry
	public static boolean ravagerScreenShake = true;

	@Entry
	public static boolean dragonScreenShake = true;

	@Entry(min = 0, max = 100)
	public static float fireflyDensity = 1;

	@Entry(min = 0, max = 100)
	public static float chorusPetalDensity = 1;

	@Entry(min = 0, max = 100)
	public static float willOWispDensity = 1;

	@Entry
	public static EyesInTheDarkOptions enableEyesInTheDark = EyesInTheDarkOptions.HALLOWEEN;
	public enum EyesInTheDarkOptions {
		HALLOWEEN, ALWAYS, DISABLED
	}

	@Entry
	public static CosmeticsOptions enableCosmetics = CosmeticsOptions.FALSE;
	public enum CosmeticsOptions {
		FALSE, TRUE, FIRST_PERSON
	}
	public static boolean shouldDisplayCosmetics() {
		return enableCosmetics == CosmeticsOptions.TRUE || enableCosmetics == CosmeticsOptions.FIRST_PERSON;
	}

}
