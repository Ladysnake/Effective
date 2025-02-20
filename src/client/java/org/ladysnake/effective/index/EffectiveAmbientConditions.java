package org.ladysnake.effective.index;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.BiomeKeys;
import org.ladysnake.effective.sound.AmbientCondition;
import org.ladysnake.effective.Effective;
import org.ladysnake.effective.utils.EffectiveUtils;

import java.util.HashSet;
import java.util.Set;

public class EffectiveAmbientConditions {
	public static final Set<AmbientCondition> INSTANCE = new HashSet<>();

	public static void initialize() {
		// bees in floral biomes during the day
		INSTANCE.add(new AmbientCondition(EffectiveSounds.AMBIENT_ANIMAL_BEES, AmbientCondition.Type.ANIMAL,
			(world, pos, player) -> EffectiveUtils.isInOverworld(world, pos) && !EffectiveUtils.isInCave(world, pos) && world.getBiome(pos).isIn(ConventionalBiomeTags.IS_FLORAL) && !Effective.isNightTime(world)));

		// birds in forests during the day
		INSTANCE.add(new AmbientCondition(EffectiveSounds.AMBIENT_ANIMAL_BIRDS, AmbientCondition.Type.ANIMAL,
			(world, pos, player) -> EffectiveUtils.isInOverworld(world, pos) && !EffectiveUtils.isInCave(world, pos) && world.getBiome(pos).isIn(ConventionalBiomeTags.IS_FOREST) && !Effective.isNightTime(world)));

		// cicadas in savannas during day
		INSTANCE.add(new AmbientCondition(EffectiveSounds.AMBIENT_ANIMAL_CICADAS, AmbientCondition.Type.ANIMAL,
			(world, pos, player) -> EffectiveUtils.isInOverworld(world, pos) && !EffectiveUtils.isInCave(world, pos) && world.getBiome(pos).isIn(ConventionalBiomeTags.IS_SAVANNA) && !Effective.isNightTime(world)));

		// crickets in temperate (excluding swamps to use their dedicated cricket and frog ambience instead) and floral biomes at night
		INSTANCE.add(new AmbientCondition(EffectiveSounds.AMBIENT_ANIMAL_CRICKETS, AmbientCondition.Type.ANIMAL,
			(world, pos, player) -> EffectiveUtils.isInOverworld(world, pos) && !EffectiveUtils.isInCave(world, pos) && ((world.getBiome(pos).isIn(ConventionalBiomeTags.IS_TEMPERATE) && !world.getBiome(pos).isIn(ConventionalBiomeTags.IS_SWAMP)) || world.getBiome(pos).isIn(ConventionalBiomeTags.IS_FLORAL)) && Effective.isNightTime(world)));

		// frogs and crickets in swamps at night
		INSTANCE.add(new AmbientCondition(EffectiveSounds.AMBIENT_ANIMAL_FROGS_AND_CRICKETS, AmbientCondition.Type.ANIMAL,
			(world, pos, player) -> EffectiveUtils.isInOverworld(world, pos) && !EffectiveUtils.isInCave(world, pos) && (world.getBiome(pos).isIn(ConventionalBiomeTags.IS_SWAMP)) && Effective.isNightTime(world)));

		// day jungle animals in jungles during the day
		INSTANCE.add(new AmbientCondition(EffectiveSounds.AMBIENT_ANIMAL_JUNGLE_DAY, AmbientCondition.Type.ANIMAL,
			(world, pos, player) -> EffectiveUtils.isInOverworld(world, pos) && !EffectiveUtils.isInCave(world, pos) && (world.getBiome(pos).isIn(ConventionalBiomeTags.IS_JUNGLE)) && !Effective.isNightTime(world)));

		// night jungle animals in jungles at night
		INSTANCE.add(new AmbientCondition(EffectiveSounds.AMBIENT_ANIMAL_JUNGLE_NIGHT, AmbientCondition.Type.ANIMAL,
			(world, pos, player) -> EffectiveUtils.isInOverworld(world, pos) && !EffectiveUtils.isInCave(world, pos) && (world.getBiome(pos).isIn(ConventionalBiomeTags.IS_JUNGLE)) && Effective.isNightTime(world)));

		// mangrove birds in mangroves during the day
		INSTANCE.add(new AmbientCondition(EffectiveSounds.AMBIENT_ANIMAL_MANGROVE_BIRDS, AmbientCondition.Type.ANIMAL,
			(world, pos, player) -> EffectiveUtils.isInOverworld(world, pos) && !EffectiveUtils.isInCave(world, pos) && world.getBiome(pos).matchesKey(BiomeKeys.MANGROVE_SWAMP) && !Effective.isNightTime(world)));

		// owls in forests at night
		INSTANCE.add(new AmbientCondition(EffectiveSounds.AMBIENT_ANIMAL_OWLS, AmbientCondition.Type.ANIMAL,
			(world, pos, player) -> EffectiveUtils.isInOverworld(world, pos) && !EffectiveUtils.isInCave(world, pos) && (world.getBiome(pos).isIn(ConventionalBiomeTags.IS_FOREST)) && Effective.isNightTime(world)));

		// rustling reverbed foliage in lush caves
		INSTANCE.add(new AmbientCondition(EffectiveSounds.AMBIENT_FOLIAGE_CAVE_LEAVES, AmbientCondition.Type.FOLIAGE,
			(world, pos, player) -> EffectiveUtils.isInOverworld(world, pos) && world.getBiome(pos).matchesKey(BiomeKeys.LUSH_CAVES)));

		// rustling foliage in forests, floral biomes, swamps, jungles, wooded badlands and lush caves
		INSTANCE.add(new AmbientCondition(EffectiveSounds.AMBIENT_FOLIAGE_LEAVES, AmbientCondition.Type.FOLIAGE,
			(world, pos, player) -> EffectiveUtils.isInOverworld(world, pos) && !EffectiveUtils.isInCave(world, pos) && (world.getBiome(pos).isIn(ConventionalBiomeTags.IS_FOREST) || world.getBiome(pos).isIn(ConventionalBiomeTags.IS_FLORAL) || world.getBiome(pos).isIn(ConventionalBiomeTags.IS_SWAMP) || world.getBiome(pos).isIn(ConventionalBiomeTags.IS_JUNGLE) || world.getBiome(pos).matchesKey(BiomeKeys.WOODED_BADLANDS))));

		// water dripping in dripstone caves
		INSTANCE.add(new AmbientCondition(EffectiveSounds.AMBIENT_WATER_DRIPSTONE_CAVES, AmbientCondition.Type.WATER,
			(world, pos, player) -> {
				if (EffectiveUtils.isInOverworld(world, pos)) {
					if (EffectiveUtils.isInCave(world, pos)) {
						BlockPos.Mutable mutable = pos.mutableCopy();
						int startY = mutable.getY();
						for (int y = startY; y <= startY + 20; y += 5) {
							mutable.setY(y);
							if (world.getBiome(mutable).matchesKey(BiomeKeys.DRIPSTONE_CAVES)) {
								return true;
							}
						}
						return false;
					} else return world.getBiome(pos).matchesKey(BiomeKeys.DRIPSTONE_CAVES);
				} else return false;
			}));

		// water streams in lush caves
		INSTANCE.add(new AmbientCondition(EffectiveSounds.AMBIENT_WATER_LUSH_CAVES, AmbientCondition.Type.WATER,
			(world, pos, player) -> EffectiveUtils.isInOverworld(world, pos) && world.getBiome(pos).matchesKey(BiomeKeys.LUSH_CAVES)));

		// water flowing in rivers
		INSTANCE.add(new AmbientCondition(EffectiveSounds.AMBIENT_WATER_RIVER, AmbientCondition.Type.WATER,
			(world, pos, player) -> EffectiveUtils.isInOverworld(world, pos) && !EffectiveUtils.isInCave(world, pos) && world.getBiome(pos).isIn(ConventionalBiomeTags.IS_RIVER)));

		// water waves in beaches and oceans
		INSTANCE.add(new AmbientCondition(EffectiveSounds.AMBIENT_WATER_WAVES, AmbientCondition.Type.WATER,
			(world, pos, player) -> EffectiveUtils.isInOverworld(world, pos) && !EffectiveUtils.isInCave(world, pos) && world.getBiome(pos).isIn(ConventionalBiomeTags.IS_BEACH) || world.getBiome(pos).isIn(ConventionalBiomeTags.IS_OCEAN)));

		// arid wind in deserts and mesas
		INSTANCE.add(new AmbientCondition(EffectiveSounds.AMBIENT_WIND_ARID, AmbientCondition.Type.WIND,
			(world, pos, player) -> EffectiveUtils.isInOverworld(world, pos) && !EffectiveUtils.isInCave(world, pos) && (world.getBiome(pos).isIn(ConventionalBiomeTags.IS_DESERT) || world.getBiome(pos).isIn(ConventionalBiomeTags.IS_BADLANDS))));

		// cave wind in caves (excluding the deep dark to use its dedicated ambience instead)
		INSTANCE.add(new AmbientCondition(EffectiveSounds.AMBIENT_WIND_CAVE, AmbientCondition.Type.WIND,
			(world, pos, player) -> EffectiveUtils.isInOverworld(world, pos) && EffectiveUtils.isInCave(world, pos) && !world.getBiome(pos).matchesKey(BiomeKeys.DEEP_DARK)));

		// cold wind in cold biomes (excluding peaks to use their dedicated wind instead) and mountain slopes
		INSTANCE.add(new AmbientCondition(EffectiveSounds.AMBIENT_WIND_COLD, AmbientCondition.Type.WIND,
			(world, pos, player) -> EffectiveUtils.isInOverworld(world, pos) && !EffectiveUtils.isInCave(world, pos) && (world.getBiome(pos).isIn(ConventionalBiomeTags.IS_COLD) || world.getBiome(pos).isIn(ConventionalBiomeTags.IS_MOUNTAIN_SLOPE)) && !world.getBiome(pos).isIn(ConventionalBiomeTags.IS_MOUNTAIN_PEAK)));

		// deep dark ambience (classified as wind)
		INSTANCE.add(new AmbientCondition(EffectiveSounds.AMBIENT_WIND_DEEP_DARK, AmbientCondition.Type.WIND,
			(world, pos, player) -> EffectiveUtils.isInOverworld(world, pos) && world.getBiome(pos).matchesKey(BiomeKeys.DEEP_DARK)));

		// end ambience (classified as wind)
		INSTANCE.add(new AmbientCondition(EffectiveSounds.AMBIENT_WIND_END, AmbientCondition.Type.WIND,
			(world, pos, player) -> world.getBiome(pos).isIn(ConventionalBiomeTags.IS_END)));

		// mountain wind in peaks
		INSTANCE.add(new AmbientCondition(EffectiveSounds.AMBIENT_WIND_MOUNTAINS, AmbientCondition.Type.WIND,
			(world, pos, player) -> EffectiveUtils.isInOverworld(world, pos) && !EffectiveUtils.isInCave(world, pos) && world.getBiome(pos).isIn(ConventionalBiomeTags.IS_MOUNTAIN_PEAK)));

		// soft wind in temperate, floral, savanna, jungle, swamp and mushroom field biomes
		INSTANCE.add(new AmbientCondition(EffectiveSounds.AMBIENT_WIND_TEMPERATE, AmbientCondition.Type.WIND,
			(world, pos, player) -> EffectiveUtils.isInOverworld(world, pos) && !EffectiveUtils.isInCave(world, pos) && (world.getBiome(pos).isIn(ConventionalBiomeTags.IS_TEMPERATE) || world.getBiome(pos).isIn(ConventionalBiomeTags.IS_FLORAL) || world.getBiome(pos).isIn(ConventionalBiomeTags.IS_SAVANNA) || world.getBiome(pos).isIn(ConventionalBiomeTags.IS_JUNGLE) || world.getBiome(pos).isIn(ConventionalBiomeTags.IS_SWAMP) || world.getBiome(pos).isIn(ConventionalBiomeTags.IS_MUSHROOM))));
	}
}
