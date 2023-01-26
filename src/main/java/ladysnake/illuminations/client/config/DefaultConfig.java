package ladysnake.illuminations.client.config;

import com.google.common.collect.ImmutableMap;
import ladysnake.illuminations.client.data.AuraSettings;
import ladysnake.illuminations.client.data.BiomeSettings;
import ladysnake.illuminations.client.enums.*;
import net.minecraft.registry.Holder;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

public final class DefaultConfig {

	public static final HalloweenFeatures HALLOWEEN_FEATURES = HalloweenFeatures.ENABLE;
	public static final EyesInTheDarkSpawnRate EYES_IN_THE_DARK_SPAWN_RATE = EyesInTheDarkSpawnRate.MEDIUM;
	public static final WillOWispsSpawnRate WILL_O_WISPS_SPAWN_RATE = WillOWispsSpawnRate.MEDIUM;
	public static final int CHORUS_PETALS_SPAWN_MULTIPLIER = 1;
	public static final int DENSITY = 100;
	public static final boolean FIREFLY_SPAWN_ALWAYS = false;
	public static final boolean FIREFLY_SPAWN_UNDERGROUND = false;
	public static final int FIREFLY_WHITE_ALPHA = 100;
	public static final boolean FIREFLY_RAINBOW = false;
	public static final boolean DISPLAY_COSMETICS = true;
	public static final boolean DEBUG_MODE = false;
	public static final boolean VIEW_AURAS_FP = false;
	public static final boolean DISPLAY_DONATION_TOAST = true;

	public static final ImmutableMap<Identifier, BiomeSettings> BIOME_SETTINGS = ImmutableMap.<Identifier, BiomeSettings>builder()
			.put(BiomeKeys.THE_VOID.getValue(), new BiomeSettings(FireflySpawnRate.DISABLE, 0xFFFFFF, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.PLAINS.getValue(), new BiomeSettings(FireflySpawnRate.LOW, 0x91BD59, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.SUNFLOWER_PLAINS.getValue(), new BiomeSettings(FireflySpawnRate.LOW, 0x91BD59, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.SNOWY_PLAINS.getValue(), new BiomeSettings(FireflySpawnRate.DISABLE, 0x00BFFF, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.ICE_SPIKES.getValue(), new BiomeSettings(FireflySpawnRate.DISABLE, 0x00BFFF, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.DESERT.getValue(), new BiomeSettings(FireflySpawnRate.MEDIUM, 0xFFA755, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.SWAMP.getValue(), new BiomeSettings(FireflySpawnRate.HIGH, 0xBFFF00, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.MANGROVE_SWAMP.getValue(), new BiomeSettings(FireflySpawnRate.HIGH, 0xBFFF00, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.FOREST.getValue(), new BiomeSettings(FireflySpawnRate.MEDIUM, 0xBFFF00, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.FLOWER_FOREST.getValue(), new BiomeSettings(FireflySpawnRate.MEDIUM, 0xFF7FED, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.BIRCH_FOREST.getValue(), new BiomeSettings(FireflySpawnRate.MEDIUM, 0xE4FF00, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.DARK_FOREST.getValue(), new BiomeSettings(FireflySpawnRate.MEDIUM, 0x006900, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.OLD_GROWTH_BIRCH_FOREST.getValue(), new BiomeSettings(FireflySpawnRate.MEDIUM, 0xE4FF00, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.OLD_GROWTH_PINE_TAIGA.getValue(), new BiomeSettings(FireflySpawnRate.LOW, 0xBFFF00, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA.getValue(), new BiomeSettings(FireflySpawnRate.LOW, 0xBFFF00, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.TAIGA.getValue(), new BiomeSettings(FireflySpawnRate.LOW, 0xBFFF00, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.SNOWY_TAIGA.getValue(), new BiomeSettings(FireflySpawnRate.LOW, 0xBFFF00, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.SAVANNA.getValue(), new BiomeSettings(FireflySpawnRate.LOW, 0x889300, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.SAVANNA_PLATEAU.getValue(), new BiomeSettings(FireflySpawnRate.DISABLE, 0x889300, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.WINDSWEPT_HILLS.getValue(), new BiomeSettings(FireflySpawnRate.LOW, 0xBFFF00, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.WINDSWEPT_GRAVELLY_HILLS.getValue(), new BiomeSettings(FireflySpawnRate.DISABLE, 0xBFFF00, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.WINDSWEPT_FOREST.getValue(), new BiomeSettings(FireflySpawnRate.LOW, 0xBFFF00, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.WINDSWEPT_SAVANNA.getValue(), new BiomeSettings(FireflySpawnRate.LOW, 0x889300, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.JUNGLE.getValue(), new BiomeSettings(FireflySpawnRate.MEDIUM, 0x00FF21, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.SPARSE_JUNGLE.getValue(), new BiomeSettings(FireflySpawnRate.MEDIUM, 0x00FF21, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.BAMBOO_JUNGLE.getValue(), new BiomeSettings(FireflySpawnRate.MEDIUM, 0x00FF21, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.BADLANDS.getValue(), new BiomeSettings(FireflySpawnRate.DISABLE, 0x889300, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.ERODED_BADLANDS.getValue(), new BiomeSettings(FireflySpawnRate.DISABLE, 0x889300, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.WOODED_BADLANDS.getValue(), new BiomeSettings(FireflySpawnRate.DISABLE, 0x889300, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.MEADOW.getValue(), new BiomeSettings(FireflySpawnRate.LOW, 0x4CAE88, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.GROVE.getValue(), new BiomeSettings(FireflySpawnRate.DISABLE, 0x00BFFF, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.SNOWY_SLOPES.getValue(), new BiomeSettings(FireflySpawnRate.DISABLE, 0x00BFFF, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.FROZEN_PEAKS.getValue(), new BiomeSettings(FireflySpawnRate.DISABLE, 0x00BFFF, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.JAGGED_PEAKS.getValue(), new BiomeSettings(FireflySpawnRate.DISABLE, 0x00BFFF, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.STONY_PEAKS.getValue(), new BiomeSettings(FireflySpawnRate.DISABLE, 0xBFFF00, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.RIVER.getValue(), new BiomeSettings(FireflySpawnRate.MEDIUM, 0xBFFF00, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.FROZEN_RIVER.getValue(), new BiomeSettings(FireflySpawnRate.DISABLE, 0x00BFFF, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.BEACH.getValue(), new BiomeSettings(FireflySpawnRate.DISABLE, 0xBFFF00, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.SNOWY_BEACH.getValue(), new BiomeSettings(FireflySpawnRate.DISABLE, 0x00BFFF, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.STONY_SHORE.getValue(), new BiomeSettings(FireflySpawnRate.DISABLE, 0xBFFF00, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.WARM_OCEAN.getValue(), new BiomeSettings(FireflySpawnRate.DISABLE, 0xBFFF00, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.LUKEWARM_OCEAN.getValue(), new BiomeSettings(FireflySpawnRate.DISABLE, 0xBFFF00, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.DEEP_LUKEWARM_OCEAN.getValue(), new BiomeSettings(FireflySpawnRate.DISABLE, 0xBFFF00, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.OCEAN.getValue(), new BiomeSettings(FireflySpawnRate.DISABLE, 0xBFFF00, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.DEEP_OCEAN.getValue(), new BiomeSettings(FireflySpawnRate.DISABLE, 0xBFFF00, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.COLD_OCEAN.getValue(), new BiomeSettings(FireflySpawnRate.DISABLE, 0xBFFF00, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.DEEP_COLD_OCEAN.getValue(), new BiomeSettings(FireflySpawnRate.DISABLE, 0xBFFF00, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.FROZEN_OCEAN.getValue(), new BiomeSettings(FireflySpawnRate.DISABLE, 0xBFFF00, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.DEEP_FROZEN_OCEAN.getValue(), new BiomeSettings(FireflySpawnRate.DISABLE, 0xBFFF00, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.MUSHROOM_FIELDS.getValue(), new BiomeSettings(FireflySpawnRate.LOW, 0xFF7F8F, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.DRIPSTONE_CAVES.getValue(), new BiomeSettings(FireflySpawnRate.DISABLE, 0xBFFF00, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.LUSH_CAVES.getValue(), new BiomeSettings(FireflySpawnRate.MEDIUM, 0xF2B646, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.DEEP_DARK.getValue(), new BiomeSettings(FireflySpawnRate.LOW, 0x29DDEB, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.NETHER_WASTES.getValue(), new BiomeSettings(FireflySpawnRate.DISABLE, 0xFF8000, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.WARPED_FOREST.getValue(), new BiomeSettings(FireflySpawnRate.LOW, 0x008080, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.CRIMSON_FOREST.getValue(), new BiomeSettings(FireflySpawnRate.LOW, 0xFF8000, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.SOUL_SAND_VALLEY.getValue(), new BiomeSettings(FireflySpawnRate.DISABLE, 0x00FFFF, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.BASALT_DELTAS.getValue(), new BiomeSettings(FireflySpawnRate.DISABLE, 0xFF8000, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.THE_END.getValue(), new BiomeSettings(FireflySpawnRate.DISABLE, 0x8000FF, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.END_HIGHLANDS.getValue(), new BiomeSettings(FireflySpawnRate.DISABLE, 0x8000FF, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.END_MIDLANDS.getValue(), new BiomeSettings(FireflySpawnRate.DISABLE, 0x8000FF, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.SMALL_END_ISLANDS.getValue(), new BiomeSettings(FireflySpawnRate.DISABLE, 0x8000FF, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.put(BiomeKeys.END_BARRENS.getValue(), new BiomeSettings(FireflySpawnRate.DISABLE, 0x8000FF, GlowwormSpawnRate.DISABLE, PlanktonSpawnRate.DISABLE))
			.build();

	public static final ImmutableMap<String, AuraSettings> AURA_SETTINGS = ImmutableMap.<String, AuraSettings>builder()
			.put("twilight", new AuraSettings(0.1f, 1))
			.put("ghostly", new AuraSettings(0.1f, 1))
			.put("chorus", new AuraSettings(0.1f, 1))
			.put("autumn_leaves", new AuraSettings(0.3f, 1))
			.put("sculk_tendrils", new AuraSettings(0.1f, 1))
			.put("shadowbringer_soul", new AuraSettings(0.1f, 1))
			.put("goldenrod", new AuraSettings(0.4f, 1))
			.put("confetti", new AuraSettings(0.1f, 1))
			.put("prismatic_confetti", new AuraSettings(0.1f, 1))
			.put("prismarine", new AuraSettings(0.1f, 1))
			.build();

	private DefaultConfig() {
		throw new UnsupportedOperationException();
	}

	public static BiomeSettings getBiomeSettings(Holder<Biome> biome) {
		return BIOME_SETTINGS.get(biome);
	}

	public static AuraSettings getAuraSettings(String aura) {
		return AURA_SETTINGS.get(aura);
	}
}
