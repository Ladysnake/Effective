package org.ladysnake.effective.settings;

import com.google.common.collect.ImmutableMap;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import org.ladysnake.effective.cosmetics.data.AuraSettings;
import org.ladysnake.effective.settings.data.FireflySpawnSetting;

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
		.put(Biomes.PLAINS, new FireflySpawnSetting(LOW, new Color(0x91BD59)))
		.put(Biomes.SUNFLOWER_PLAINS, new FireflySpawnSetting(LOW, new Color(0x91BD59)))
		.put(Biomes.OLD_GROWTH_PINE_TAIGA, new FireflySpawnSetting(LOW, new Color(0xBFFF00)))
		.put(Biomes.OLD_GROWTH_SPRUCE_TAIGA, new FireflySpawnSetting(LOW, new Color(0xBFFF00)))
		.put(Biomes.TAIGA, new FireflySpawnSetting(LOW, new Color(0xBFFF00)))
		.put(Biomes.FOREST, new FireflySpawnSetting(MEDIUM, new Color(0xBFFF00)))
		.put(Biomes.FLOWER_FOREST, new FireflySpawnSetting(MEDIUM, new Color(0xFF7FED)))
		.put(Biomes.BIRCH_FOREST, new FireflySpawnSetting(MEDIUM, new Color(0xE4FF00)))
		.put(Biomes.DARK_FOREST, new FireflySpawnSetting(MEDIUM, new Color(0x006900)))
		.put(Biomes.OLD_GROWTH_BIRCH_FOREST, new FireflySpawnSetting(MEDIUM, new Color(0xE4FF00)))
		.put(Biomes.JUNGLE, new FireflySpawnSetting(MEDIUM, new Color(0x00FF21)))
		.put(Biomes.SPARSE_JUNGLE, new FireflySpawnSetting(MEDIUM, new Color(0x00FF21)))
		.put(Biomes.BAMBOO_JUNGLE, new FireflySpawnSetting(MEDIUM, new Color(0x00FF21)))
		.put(Biomes.LUSH_CAVES, new FireflySpawnSetting(MEDIUM, new Color(0xF2B646)))
		.put(Biomes.SWAMP, new FireflySpawnSetting(HIGH, new Color(0xBFFF00)))
		.put(Biomes.MANGROVE_SWAMP, new FireflySpawnSetting(HIGH, new Color(0xBFFF00)))
		.build();
}
