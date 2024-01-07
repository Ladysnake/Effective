package org.ladysnake.effective;

import eu.midnightdust.lib.config.MidnightConfig;

public class EffectiveConfig extends MidnightConfig {
	public static final String waterEffects = "waterEffects";

	@Entry(category = waterEffects)
	public static boolean splashes = true;

	@Entry(category = waterEffects, min = 0, max = 5, isSlider = true)
	public static float splashThreshold = 0.3f;

	@Entry(category = waterEffects, min = 0, max = 100, isSlider = true)
	public static int flowingWaterSplashingDensity = 50;

	@Entry(category = waterEffects)
	public static boolean cascades = true;

	@Entry(category = waterEffects, min = 0, max = 5, isSlider = true)
	public static float cascadeCloudDensity = 1;

	@Entry(category = waterEffects, min = 0, max = 5, isSlider = true)
	public static float cascadeMistDensity = 1;

	@Entry(category = waterEffects, min = 0, max = 100, isSlider = true)
	public static int cascadeSoundsVolumeMultiplier = 30;

	@Entry(category = waterEffects, min = 0, max = 400, isSlider = true)
	public static int cascadeSoundDistanceBlocks = 150;

	@Entry(category = waterEffects)
	public static boolean shouldFlowingWaterSpawnParticlesOnFirstTick = true;

	@Entry(category = waterEffects, min = 0, max = 10, isSlider = true)
	public static float lapisBlockUpdateParticleChance = 1;

	@Entry(category = waterEffects, min = 0, max = 10, isSlider = true)
	public static int rainRippleDensity = 1;

	@Entry(category = waterEffects)
	public static boolean glowingPlankton = true;

	@Entry(category = waterEffects)
	public static boolean underwaterOpenChestBubbles = true;

	@Entry(category = waterEffects)
	public static ChestsOpenOptions underwaterChestsOpenRandomly = ChestsOpenOptions.ON_SOUL_SAND;

	public static final String entityEffects = "entityEffects";

	@Entry(category = entityEffects)
	public static GlowSquidHypnoOptions glowSquidHypnotize = GlowSquidHypnoOptions.ATTRACT;

	@Entry(category = entityEffects)
	public static TrailOptions allayTrails = TrailOptions.BOTH;

	@Entry(category = entityEffects)
	public static boolean goldenAllays = true;

	public static final String screenShakeEffects = "screenShakeEffects";

	@Entry(category = screenShakeEffects, min = 0, max = 5, isSlider = true)
	public static float screenShakeIntensity = 1;

	@Entry(category = screenShakeEffects)
	public static boolean wardenScreenShake = true;

	@Entry(category = screenShakeEffects)
	public static boolean sonicBoomScreenShake = true;

	@Entry(category = screenShakeEffects)
	public static boolean ravagerScreenShake = true;

	@Entry(category = screenShakeEffects)
	public static boolean dragonScreenShake = true;


	public static final String illuminatedEffects = "illuminatedEffects";

	@Entry(category = illuminatedEffects, min = 0, max = 10, isSlider = true)
	public static float fireflyDensity = 1;

	@Entry(category = illuminatedEffects, min = 0, max = 10, isSlider = true)
	public static float chorusPetalDensity = 1;

	@Entry(category = illuminatedEffects, min = 0, max = 10, isSlider = true)
	public static float willOWispDensity = 1;

	@Entry(category = illuminatedEffects)
	public static EyesInTheDarkOptions eyesInTheDark = EyesInTheDarkOptions.HALLOWEEN;

	public static final String improvedEffects = "improvedEffects";

	@Entry(category = improvedEffects)
	public static boolean improvedFireballs = true;

	@Entry(category = improvedEffects)
	public static boolean improvedDragonFireballsAndBreath = true;

	@Entry(category = improvedEffects)
	public static boolean improvedGlowSquidParticles = true;

	@Entry(category = improvedEffects)
	public static TrailOptions spectralArrowTrails = TrailOptions.BOTH;

	public static final String miscellaneous = "miscellaneous";

	@Entry(category = miscellaneous, min = 0, max = 100, isSlider = true)
	public static float sculkDustDensity = 100;

	@Entry(category = miscellaneous)
	public static CosmeticsOptions cosmetics = CosmeticsOptions.ENABLE;

	@Entry(category = miscellaneous)
	public static boolean ultrakill = false;

	@Entry(category = miscellaneous, min = 1, max = 10, isSlider = true)
	public static int freezeFrames = 5;

	public static boolean shouldGlowSquidsHypnotize() {
		return glowSquidHypnotize == GlowSquidHypnoOptions.ATTRACT || glowSquidHypnotize == GlowSquidHypnoOptions.VISUAL;
	}

	public static boolean shouldDisplayCosmetics() {
		return cosmetics == CosmeticsOptions.ENABLE || cosmetics == CosmeticsOptions.FIRST_PERSON;
	}

	public enum TrailOptions {
		BOTH, TRAIL, TWINKLE, NONE
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
