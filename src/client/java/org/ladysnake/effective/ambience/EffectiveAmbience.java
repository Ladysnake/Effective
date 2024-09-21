package org.ladysnake.effective.ambience;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeKeys;
import org.ladysnake.effective.ambience.sound.AmbientCondition;
import org.ladysnake.effective.core.Effective;

import java.util.HashSet;
import java.util.Set;

public class EffectiveAmbience implements ClientModInitializer {
	public static final Set<AmbientCondition> AMBIENT_CONDITIONS = new HashSet<>();

	@Override
	public void onInitializeClient() {
		/* SURFACE */
		AMBIENT_CONDITIONS.add(new AmbientCondition(EffectiveAmbienceSounds.AMBIENT_PLAINS_DAY, (world, pos, player) -> !isInCave(world, pos) && world.getBiome(pos).isIn(ConventionalBiomeTags.CLIMATE_TEMPERATE) && !Effective.isNightTime(world))); // plains day
		AMBIENT_CONDITIONS.add(new AmbientCondition(EffectiveAmbienceSounds.AMBIENT_BEACH, (world, pos, player) -> !isInCave(world, pos) && world.getBiome(pos).isIn(ConventionalBiomeTags.BEACH))); // beach
		AMBIENT_CONDITIONS.add(new AmbientCondition(EffectiveAmbienceSounds.CRICKETS, (world, pos, player) -> !isInCave(world, pos) && world.getBiome(pos).isIn(ConventionalBiomeTags.CLIMATE_TEMPERATE) && Effective.isNightTime(world))); // crickets for night time in temperate biomes

		/* CAVES */
		AMBIENT_CONDITIONS.add(new AmbientCondition(EffectiveAmbienceSounds.AMBIENT_CAVE, (world, pos, player) -> isInCave(world, pos) && !world.isSkyVisible(pos))); // cave
		AMBIENT_CONDITIONS.add(new AmbientCondition(EffectiveAmbienceSounds.AMBIENT_DEEP_DARK, (world, pos, player) -> world.getBiome(pos).matchesKey(BiomeKeys.DEEP_DARK))); // deep dark
		AMBIENT_CONDITIONS.add(new AmbientCondition(EffectiveAmbienceSounds.AMBIENT_LUSH_CAVE, (world, pos, player) -> world.getBiome(pos).matchesKey(BiomeKeys.LUSH_CAVES))); // lush caves
		AMBIENT_CONDITIONS.add(new AmbientCondition(EffectiveAmbienceSounds.AMBIENT_DRIPSTONE_CAVE, (world, pos, player) -> world.getBiome(pos).matchesKey(BiomeKeys.DRIPSTONE_CAVES))); // dripstone caves
	}

	public static final boolean isInCave(World world, BlockPos pos) {
		return pos.getY() <= world.getSeaLevel();
	}
}
