package org.ladysnake.effective.core.settings;

import com.google.common.collect.ImmutableMap;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import org.ladysnake.effective.core.settings.data.FireflySpawnSetting;
import org.ladysnake.effective.cosmetics.data.AuraSettings;

import java.awt.*;

public class SpawnSettings {
	public static final ImmutableMap<String, AuraSettings> AURAS = ImmutableMap.<String, AuraSettings>builder()
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
	public static float LOW = 0.01f;
	public static float MEDIUM = 0.05f;
	public static float HIGH = 0.1f;
	public static final ImmutableMap<RegistryKey<Biome>, FireflySpawnSetting> FIREFLIES = ImmutableMap.<RegistryKey<Biome>, FireflySpawnSetting>builder()
		.put(BiomeKeys.PLAINS, new FireflySpawnSetting(LOW, new Color(0x91BD59)))
		.put(BiomeKeys.SUNFLOWER_PLAINS, new FireflySpawnSetting(LOW, new Color(0x91BD59)))
		.put(BiomeKeys.OLD_GROWTH_PINE_TAIGA, new FireflySpawnSetting(LOW, new Color(0xBFFF00)))
		.put(BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA, new FireflySpawnSetting(LOW, new Color(0xBFFF00)))
		.put(BiomeKeys.TAIGA, new FireflySpawnSetting(LOW, new Color(0xBFFF00)))
		.put(BiomeKeys.FOREST, new FireflySpawnSetting(MEDIUM, new Color(0xBFFF00)))
		.put(BiomeKeys.FLOWER_FOREST, new FireflySpawnSetting(MEDIUM, new Color(0xFF7FED)))
		.put(BiomeKeys.BIRCH_FOREST, new FireflySpawnSetting(MEDIUM, new Color(0xE4FF00)))
		.put(BiomeKeys.DARK_FOREST, new FireflySpawnSetting(MEDIUM, new Color(0x006900)))
		.put(BiomeKeys.OLD_GROWTH_BIRCH_FOREST, new FireflySpawnSetting(MEDIUM, new Color(0xE4FF00)))
		.put(BiomeKeys.JUNGLE, new FireflySpawnSetting(MEDIUM, new Color(0x00FF21)))
		.put(BiomeKeys.SPARSE_JUNGLE, new FireflySpawnSetting(MEDIUM, new Color(0x00FF21)))
		.put(BiomeKeys.BAMBOO_JUNGLE, new FireflySpawnSetting(MEDIUM, new Color(0x00FF21)))
		.put(BiomeKeys.LUSH_CAVES, new FireflySpawnSetting(MEDIUM, new Color(0xF2B646)))
		.put(BiomeKeys.SWAMP, new FireflySpawnSetting(HIGH, new Color(0xBFFF00)))
		.put(BiomeKeys.MANGROVE_SWAMP, new FireflySpawnSetting(HIGH, new Color(0xBFFF00)))
		.build();
}
