package ladysnake.effective.client.settings;

import com.google.common.collect.ImmutableMap;
import ladysnake.effective.client.settings.data.FireflySpawnSetting;
import ladysnake.illuminations.client.data.AuraSettings;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biomes;

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
	public static final ImmutableMap<Identifier, FireflySpawnSetting> FIREFLIES = ImmutableMap.<Identifier, FireflySpawnSetting>builder()
			.put(Biomes.PLAINS.getValue(), new FireflySpawnSetting(LOW, 0x91BD59))
			.put(Biomes.SUNFLOWER_PLAINS.getValue(), new FireflySpawnSetting(LOW, 0x91BD59))
			.put(Biomes.OLD_GROWTH_PINE_TAIGA.getValue(), new FireflySpawnSetting(LOW, 0xBFFF00))
			.put(Biomes.OLD_GROWTH_SPRUCE_TAIGA.getValue(), new FireflySpawnSetting(LOW, 0xBFFF00))
			.put(Biomes.TAIGA.getValue(), new FireflySpawnSetting(LOW, 0xBFFF00))
			.put(Biomes.FOREST.getValue(), new FireflySpawnSetting(MEDIUM, 0xBFFF00))
			.put(Biomes.FLOWER_FOREST.getValue(), new FireflySpawnSetting(MEDIUM, 0xFF7FED))
			.put(Biomes.BIRCH_FOREST.getValue(), new FireflySpawnSetting(MEDIUM, 0xE4FF00))
			.put(Biomes.DARK_FOREST.getValue(), new FireflySpawnSetting(MEDIUM, 0x006900))
			.put(Biomes.OLD_GROWTH_BIRCH_FOREST.getValue(), new FireflySpawnSetting(MEDIUM, 0xE4FF00))
			.put(Biomes.JUNGLE.getValue(), new FireflySpawnSetting(MEDIUM, 0x00FF21))
			.put(Biomes.SPARSE_JUNGLE.getValue(), new FireflySpawnSetting(MEDIUM, 0x00FF21))
			.put(Biomes.BAMBOO_JUNGLE.getValue(), new FireflySpawnSetting(MEDIUM, 0x00FF21))
			.put(Biomes.LUSH_CAVES.getValue(), new FireflySpawnSetting(MEDIUM, 0xF2B646))
			.put(Biomes.SWAMP.getValue(), new FireflySpawnSetting(HIGH, 0xBFFF00))
			.put(Biomes.MANGROVE_SWAMP.getValue(), new FireflySpawnSetting(HIGH, 0xBFFF00))
			.build();
}
