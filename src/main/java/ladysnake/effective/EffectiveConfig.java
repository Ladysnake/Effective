package ladysnake.effective;

import eu.midnightdust.lib.config.MidnightConfig;

public class EffectiveConfig extends MidnightConfig {
	@Comment(centered = true)
	public static Comment waterEffects;

	@Entry
	public static boolean splashes = true;

	@Entry(min = 0, max = 100, isSlider = true)
	public static int flowingWaterSplashingDensity = 50;

	@Entry
	public static boolean cascades = true;

	@Entry(min = 0, max = 5, isSlider = true)
	public static float cascadeCloudDensity = 1;

	@Entry(min = 0, max = 5, isSlider = true)
	public static float cascadeMistDensity = 1;

	@Entry(min = 0, max = 500, isSlider = true)
	public static int cascadeSoundsVolumeMultiplier = 100;

	@Entry(min = 0, max = 400, isSlider = true)
	public static int cascadeSoundDistanceBlocks = 150;

	@Entry
	public static boolean shouldFlowingWaterSpawnParticlesOnFirstTick = true;

	@Entry(min = 0, max = 10, isSlider = true)
	public static float lapisBlockUpdateParticleChance = 1;

	@Entry(min = 0, max = 10, isSlider = true)
	public static int rainRippleDensity = 1;

	@Entry
	public static boolean glowingPlankton = true;

	@Entry
	public static boolean underwaterOpenChestBubbles = true;

	@Entry
	public static ChestsOpenOptions underwaterChestsOpenRandomly = ChestsOpenOptions.ON_SOUL_SAND;

	@Comment(centered = true)
	public static Comment entityEffects;

	@Entry
	public static GlowSquidHypnoOptions glowSquidHypnotize = GlowSquidHypnoOptions.ATTRACT;

	@Entry
	public static boolean allayTrails = true;

	@Entry
	public static boolean goldenAllays = true;

	@Comment(centered = true)
	public static Comment screenShakeEffects;

	@Entry
	public static boolean wardenScreenShake = true;

	@Entry
	public static boolean sonicBoomScreenShake = true;

	@Entry
	public static boolean ravagerScreenShake = true;

	@Entry
	public static boolean dragonScreenShake = true;


	@Comment(centered = true)
	public static Comment illuminatedEffects;

	@Entry(min = 0, max = 10, isSlider = true)
	public static float fireflyDensity = 1;

	@Entry(min = 0, max = 10, isSlider = true)
	public static float chorusPetalDensity = 1;

	@Entry(min = 0, max = 10, isSlider = true)
	public static float willOWispDensity = 1;

	@Entry
	public static EyesInTheDarkOptions eyesInTheDark = EyesInTheDarkOptions.HALLOWEEN;

	@Comment(centered = true)
	public static Comment improvedEffects;

	@Entry
	public static boolean improvedFireballs = true;

	@Entry
	public static boolean improvedDragonFireballsAndBreath = true;

	@Entry
	public static boolean improvedSpectralArrows = true;

	@Entry
	public static boolean improvedGlowSquidParticles = true;

	@Comment(centered = true)
	public static Comment miscellaneous;

	@Entry(min = 0, max = 100, isSlider = true)
	public static float sculkDustDensity = 100;

	@Entry
	public static CosmeticsOptions cosmetics = CosmeticsOptions.ENABLE;

	@Entry
	public static boolean feedbacking = false;

	public static boolean shouldGlowSquidsHypnotize() {
		return glowSquidHypnotize == GlowSquidHypnoOptions.ATTRACT || glowSquidHypnotize == GlowSquidHypnoOptions.VISUAL;
	}

	public static boolean shouldDisplayCosmetics() {
		return cosmetics == CosmeticsOptions.ENABLE || cosmetics == CosmeticsOptions.FIRST_PERSON;
	}

	public enum ChestsOpenOptions {
		ON_SOUL_SAND, RANDOMLY, NEVER
	}

	public enum GlowSquidHypnoOptions {
		ATTRACT, VISUAL, NEVER
	}

	public enum EyesInTheDarkOptions {
		HALLOWEEN, ALWAYS, NEVER
	}

	public enum CosmeticsOptions {
		ENABLE, FIRST_PERSON, DISABLE
	}
}
