package org.ladysnake.effective.ambience;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags;
import net.minecraft.block.BlockState;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeKeys;
import org.ladysnake.effective.ambience.sound.AmbientCondition;
import org.ladysnake.effective.core.Effective;
import org.ladysnake.effective.core.utils.EffectiveUtils;

import java.util.HashSet;
import java.util.Set;

public class EffectiveAmbience implements ClientModInitializer {
	public static final Set<AmbientCondition> AMBIENT_CONDITIONS = new HashSet<>();

	@Override
	public void onInitializeClient() {
		// bees in floral biomes during the day
		AMBIENT_CONDITIONS.add(new AmbientCondition(EffectiveAmbienceSounds.ANIMAL_BEES, AmbientCondition.Type.ANIMAL,
			(world, pos, player) -> EffectiveUtils.isInOverworld(world, pos) && !EffectiveUtils.isInCave(world, pos) && world.getBiome(pos).isIn(ConventionalBiomeTags.FLORAL) && !Effective.isNightTime(world)));

		// birds in forests during the day
		AMBIENT_CONDITIONS.add(new AmbientCondition(EffectiveAmbienceSounds.ANIMAL_BIRDS, AmbientCondition.Type.ANIMAL,
			(world, pos, player) -> EffectiveUtils.isInOverworld(world, pos) && !EffectiveUtils.isInCave(world, pos) && world.getBiome(pos).isIn(ConventionalBiomeTags.FOREST) && !Effective.isNightTime(world)));

		// cicadas in savannas during day
		AMBIENT_CONDITIONS.add(new AmbientCondition(EffectiveAmbienceSounds.ANIMAL_CICADAS, AmbientCondition.Type.ANIMAL,
			(world, pos, player) -> EffectiveUtils.isInOverworld(world, pos) && !EffectiveUtils.isInCave(world, pos) && world.getBiome(pos).isIn(ConventionalBiomeTags.SAVANNA) && !Effective.isNightTime(world)));

		// crickets in temperate (excluding swamps to use their dedicated cricket and frog ambience instead) and floral biomes at night
		AMBIENT_CONDITIONS.add(new AmbientCondition(EffectiveAmbienceSounds.ANIMAL_CRICKETS, AmbientCondition.Type.ANIMAL,
			(world, pos, player) -> EffectiveUtils.isInOverworld(world, pos) && !EffectiveUtils.isInCave(world, pos) && ((world.getBiome(pos).isIn(ConventionalBiomeTags.CLIMATE_TEMPERATE) && !world.getBiome(pos).isIn(ConventionalBiomeTags.SWAMP)) || world.getBiome(pos).isIn(ConventionalBiomeTags.FLORAL)) && Effective.isNightTime(world)));

		// frogs and crickets in swamps at night
		AMBIENT_CONDITIONS.add(new AmbientCondition(EffectiveAmbienceSounds.ANIMAL_FROGS_AND_CRICKETS, AmbientCondition.Type.ANIMAL,
			(world, pos, player) -> EffectiveUtils.isInOverworld(world, pos) && !EffectiveUtils.isInCave(world, pos) && (world.getBiome(pos).isIn(ConventionalBiomeTags.SWAMP)) && Effective.isNightTime(world)));

		// day jungle animals in jungles during the day
		AMBIENT_CONDITIONS.add(new AmbientCondition(EffectiveAmbienceSounds.ANIMAL_JUNGLE_DAY, AmbientCondition.Type.ANIMAL,
			(world, pos, player) -> EffectiveUtils.isInOverworld(world, pos) && !EffectiveUtils.isInCave(world, pos) && (world.getBiome(pos).isIn(ConventionalBiomeTags.JUNGLE)) && !Effective.isNightTime(world)));

		// night jungle animals in jungles at night
		AMBIENT_CONDITIONS.add(new AmbientCondition(EffectiveAmbienceSounds.ANIMAL_JUNGLE_NIGHT, AmbientCondition.Type.ANIMAL,
			(world, pos, player) -> EffectiveUtils.isInOverworld(world, pos) && !EffectiveUtils.isInCave(world, pos) && (world.getBiome(pos).isIn(ConventionalBiomeTags.JUNGLE)) && Effective.isNightTime(world)));

		// mangrove birds in mangroves during the day
		AMBIENT_CONDITIONS.add(new AmbientCondition(EffectiveAmbienceSounds.ANIMAL_MANGROVE_BIRDS, AmbientCondition.Type.ANIMAL,
			(world, pos, player) -> EffectiveUtils.isInOverworld(world, pos) && !EffectiveUtils.isInCave(world, pos) && world.getBiome(pos).matchesKey(BiomeKeys.MANGROVE_SWAMP) && !Effective.isNightTime(world)));

		// owls in forests at night
		AMBIENT_CONDITIONS.add(new AmbientCondition(EffectiveAmbienceSounds.ANIMAL_OWLS, AmbientCondition.Type.ANIMAL,
			(world, pos, player) -> EffectiveUtils.isInOverworld(world, pos) && !EffectiveUtils.isInCave(world, pos) && (world.getBiome(pos).isIn(ConventionalBiomeTags.FOREST)) && Effective.isNightTime(world)));

		// rustling foliage in forests, floral biomes, swamps, jungles, wooded badlands and lush caves
		AMBIENT_CONDITIONS.add(new AmbientCondition(EffectiveAmbienceSounds.FOLIAGE_LEAVES, AmbientCondition.Type.FOLIAGE,
			(world, pos, player) -> EffectiveUtils.isInOverworld(world, pos) && (!EffectiveUtils.isInCave(world, pos) || world.getBiome(pos).matchesKey(BiomeKeys.LUSH_CAVES)) && (world.getBiome(pos).isIn(ConventionalBiomeTags.FOREST) || world.getBiome(pos).isIn(ConventionalBiomeTags.FLORAL) || world.getBiome(pos).isIn(ConventionalBiomeTags.SWAMP) || world.getBiome(pos).isIn(ConventionalBiomeTags.JUNGLE) || world.getBiome(pos).matchesKey(BiomeKeys.WOODED_BADLANDS) || world.getBiome(pos).matchesKey(BiomeKeys.LUSH_CAVES))));

		// water dripping in dripstone caves
		AMBIENT_CONDITIONS.add(new AmbientCondition(EffectiveAmbienceSounds.WATER_DRIPSTONE_CAVES, AmbientCondition.Type.WATER,
			(world, pos, player) -> EffectiveUtils.isInOverworld(world, pos) && world.getBiome(pos).matchesKey(BiomeKeys.DRIPSTONE_CAVES)));

		// water streams in lush caves
		AMBIENT_CONDITIONS.add(new AmbientCondition(EffectiveAmbienceSounds.WATER_LUSH_CAVES, AmbientCondition.Type.WATER,
			(world, pos, player) -> EffectiveUtils.isInOverworld(world, pos) && world.getBiome(pos).matchesKey(BiomeKeys.LUSH_CAVES)));

		// water flowing in rivers
		AMBIENT_CONDITIONS.add(new AmbientCondition(EffectiveAmbienceSounds.WATER_RIVER, AmbientCondition.Type.WATER,
			(world, pos, player) -> EffectiveUtils.isInOverworld(world, pos) && !EffectiveUtils.isInCave(world, pos) && world.getBiome(pos).isIn(ConventionalBiomeTags.RIVER)));

		// water waves in beaches and oceans
		AMBIENT_CONDITIONS.add(new AmbientCondition(EffectiveAmbienceSounds.WATER_WAVES, AmbientCondition.Type.WATER,
			(world, pos, player) -> EffectiveUtils.isInOverworld(world, pos) && !EffectiveUtils.isInCave(world, pos) && world.getBiome(pos).isIn(ConventionalBiomeTags.BEACH) || world.getBiome(pos).isIn(ConventionalBiomeTags.OCEAN)));

		// arid wind in deserts and mesas
		AMBIENT_CONDITIONS.add(new AmbientCondition(EffectiveAmbienceSounds.WIND_ARID, AmbientCondition.Type.WIND,
			(world, pos, player) -> EffectiveUtils.isInOverworld(world, pos) && !EffectiveUtils.isInCave(world, pos) && (world.getBiome(pos).isIn(ConventionalBiomeTags.DESERT) || world.getBiome(pos).isIn(ConventionalBiomeTags.MESA))));

		// cave wind in caves (excluding the deep dark to use its dedicated ambience instead)
		AMBIENT_CONDITIONS.add(new AmbientCondition(EffectiveAmbienceSounds.WIND_CAVE, AmbientCondition.Type.WIND,
			(world, pos, player) -> EffectiveUtils.isInOverworld(world, pos) && EffectiveUtils.isInCave(world, pos) && !world.getBiome(pos).matchesKey(BiomeKeys.DEEP_DARK)));

		// cold wind in cold biomes (excluding peaks to use their dedicated wind instead) and mountain slopes
		AMBIENT_CONDITIONS.add(new AmbientCondition(EffectiveAmbienceSounds.WIND_COLD, AmbientCondition.Type.WIND,
			(world, pos, player) -> EffectiveUtils.isInOverworld(world, pos) && !EffectiveUtils.isInCave(world, pos) && (world.getBiome(pos).isIn(ConventionalBiomeTags.CLIMATE_COLD) || world.getBiome(pos).isIn(ConventionalBiomeTags.MOUNTAIN_SLOPE)) && !world.getBiome(pos).isIn(ConventionalBiomeTags.MOUNTAIN_PEAK)));

		// deep dark ambience (classified as wind)
		AMBIENT_CONDITIONS.add(new AmbientCondition(EffectiveAmbienceSounds.WIND_DEEP_DARK, AmbientCondition.Type.WIND,
			(world, pos, player) -> EffectiveUtils.isInOverworld(world, pos) && world.getBiome(pos).matchesKey(BiomeKeys.DEEP_DARK)));

		// end ambience (classified as wind)
		AMBIENT_CONDITIONS.add(new AmbientCondition(EffectiveAmbienceSounds.WIND_END, AmbientCondition.Type.WIND,
			(world, pos, player) -> world.getBiome(pos).isIn(ConventionalBiomeTags.IN_THE_END)));

		// mountain wind in peaks
		AMBIENT_CONDITIONS.add(new AmbientCondition(EffectiveAmbienceSounds.WIND_MOUNTAINS, AmbientCondition.Type.WIND,
			(world, pos, player) -> EffectiveUtils.isInOverworld(world, pos) && !EffectiveUtils.isInCave(world, pos) && world.getBiome(pos).isIn(ConventionalBiomeTags.MOUNTAIN_PEAK)));

		// soft wind in temperate, floral, savanna, jungle, swamp and mushroom field biomes
		AMBIENT_CONDITIONS.add(new AmbientCondition(EffectiveAmbienceSounds.WIND_TEMPERATE, AmbientCondition.Type.WIND,
			(world, pos, player) -> EffectiveUtils.isInOverworld(world, pos) && !EffectiveUtils.isInCave(world, pos) && (world.getBiome(pos).isIn(ConventionalBiomeTags.CLIMATE_TEMPERATE) || world.getBiome(pos).isIn(ConventionalBiomeTags.FLORAL) || world.getBiome(pos).isIn(ConventionalBiomeTags.SAVANNA) || world.getBiome(pos).isIn(ConventionalBiomeTags.JUNGLE) || world.getBiome(pos).isIn(ConventionalBiomeTags.SWAMP) || world.getBiome(pos).isIn(ConventionalBiomeTags.MUSHROOM))));
	}
}
