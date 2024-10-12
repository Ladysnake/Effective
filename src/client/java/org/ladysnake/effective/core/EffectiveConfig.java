package org.ladysnake.effective.core;

import eu.midnightdust.lib.config.MidnightConfig;

public class EffectiveConfig extends MidnightConfig {
	/* VISUALS CATEGORY */
	public static final String visuals = "visuals";

	@Comment(category = visuals, centered = true) public static Comment waterEffects;
	@Entry(category = visuals)
	public static boolean splashes = true;
	@Entry(category = visuals, min = 0, max = 5, isSlider = true)
	public static float splashThreshold = 0.3f;
	@Entry(category = visuals, min = 0, max = 100, isSlider = true)
	public static int flowingWaterSplashingDensity = 50;
	@Entry(category = visuals)
	public static boolean cascades = true;
	@Entry(category = visuals, min = 0, max = 5, isSlider = true)
	public static float cascadeCloudDensity = 2.5f;
	@Entry(category = visuals, min = 0, max = 5, isSlider = true)
	public static float cascadeMistDensity = 1;
	@Entry(category = visuals)
	public static boolean shouldFlowingWaterSpawnParticlesOnFirstTick = true;
	@Entry(category = visuals, min = 0, max = 10, isSlider = true)
	public static float lapisBlockUpdateParticleChance = 1;
	@Entry(category = visuals, min = 0, max = 10, isSlider = true)
	public static int rainRippleDensity = 5;
	@Entry(category = visuals)
	public static boolean glowingPlankton = true;
	@Entry(category = visuals)
	public static boolean underwaterOpenChestBubbles = true;
	@Entry(category = visuals)
	public static ChestsOpenOptions underwaterChestsOpenRandomly = ChestsOpenOptions.ON_SOUL_SAND;

	@Comment(category = visuals, centered = true) public static Comment entityEffects;
	@Entry(category = visuals)
	public static GlowSquidHypnoOptions glowSquidHypnotize = GlowSquidHypnoOptions.ATTRACT;
	@Entry(category = visuals)
	public static TrailOptions allayTrails = TrailOptions.BOTH;
	@Entry(category = visuals)
	public static boolean goldenAllays = true;

	@Comment(category = visuals, centered = true) public static Comment screenShakeEffects;
	@Entry(category = visuals, min = 0, max = 5, isSlider = true)
	public static float screenShakeIntensity = 1;
	@Entry(category = visuals)
	public static boolean wardenScreenShake = true;
	@Entry(category = visuals)
	public static boolean sonicBoomScreenShake = true;
	@Entry(category = visuals)
	public static boolean ravagerScreenShake = true;
	@Entry(category = visuals)
	public static boolean dragonScreenShake = true;

	@Comment(category = visuals, centered = true) public static Comment illuminatedEffects;
	@Entry(category = visuals, min = 0, max = 10, isSlider = true)
	public static float fireflyDensity = 1;
	@Entry(category = visuals, min = 0, max = 10, isSlider = true)
	public static float chorusPetalDensity = 1;
	@Entry(category = visuals, min = 0, max = 10, isSlider = true)
	public static float willOWispDensity = 1;
	@Entry(category = visuals)
	public static EyesInTheDarkOptions eyesInTheDark = EyesInTheDarkOptions.HALLOWEEN;

	@Comment(category = visuals, centered = true) public static Comment improvedEffects;
	@Entry(category = visuals)
	public static boolean improvedFireballs = true;
	@Entry(category = visuals)
	public static boolean improvedDragonFireballsAndBreath = true;
	@Entry(category = visuals)
	public static boolean improvedGlowSquidParticles = true;
	@Entry(category = visuals)
	public static TrailOptions spectralArrowTrails = TrailOptions.BOTH;

	@Comment(category = visuals, centered = true) public static Comment miscellaneous;
	@Entry(category = visuals, min = 0, max = 100, isSlider = true)
	public static float sculkDustDensity = 100;
	@Entry(category = visuals)
	public static CosmeticsOptions cosmetics = CosmeticsOptions.ENABLE;
	@Entry(category = visuals)
	public static boolean ultrakill = false;

	/* AUDIO CATEGORY */
	public static final String audio = "audio";

	@Comment(category = audio, centered = true) public static Comment cascadeAudio;
	@Entry(category = audio, min = 0, max = 100, isSlider = true)
	public static int cascadeSoundsVolume = 30;
	@Entry(category = audio, min = 0, max = 400, isSlider = true)
	public static int cascadeSoundDistanceBlocks = 100;
	@Comment(category = audio, centered = true) public static Comment biomeAmbience;
	@Entry(category = audio, min = 0, max = 100, isSlider = true)
	public static int windAmbienceVolume = 100;
	@Entry(category = audio, min = 0, max = 100, isSlider = true)
	public static int waterAmbienceVolume = 100;
	@Entry(category = audio, min = 0, max = 100, isSlider = true)
	public static int foliageAmbienceVolume = 100;
	@Entry(category = audio, min = 0, max = 100, isSlider = true)
	public static int animalAmbienceVolume = 100;

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
