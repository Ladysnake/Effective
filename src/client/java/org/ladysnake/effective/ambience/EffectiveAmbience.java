package org.ladysnake.effective.ambience;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags;
import net.minecraft.world.biome.BiomeKeys;
import org.ladysnake.effective.ambience.sound.AmbientCondition;
import org.ladysnake.effective.core.Effective;

import java.util.HashSet;
import java.util.Set;

public class EffectiveAmbience implements ClientModInitializer {
	public static final Set<AmbientCondition> AMBIENT_CONDITIONS = new HashSet<>();

	@Override
	public void onInitializeClient() {
		AMBIENT_CONDITIONS.add(new AmbientCondition(EffectiveAmbienceSounds.AMBIENT_PLAINS_DAY, (world, pos, player) -> world.getBiome(pos).isIn(ConventionalBiomeTags.PLAINS) && !Effective.isNightTime(world))); // plains day
		AMBIENT_CONDITIONS.add(new AmbientCondition(EffectiveAmbienceSounds.AMBIENT_BEACH, (world, pos, player) -> world.getBiome(pos).isIn(ConventionalBiomeTags.BEACH))); // beach
		AMBIENT_CONDITIONS.add(new AmbientCondition(EffectiveAmbienceSounds.AMBIENT_DEEP_DARK, (world, pos, player) -> world.getBiome(pos).matchesKey(BiomeKeys.DEEP_DARK))); // deep dark
		AMBIENT_CONDITIONS.add(new AmbientCondition(EffectiveAmbienceSounds.CRICKETS, (world, pos, player) -> world.getBiome(pos).isIn(ConventionalBiomeTags.CLIMATE_TEMPERATE) && Effective.isNightTime(world))); // crickets for night time in temperate biomes
	}
}
