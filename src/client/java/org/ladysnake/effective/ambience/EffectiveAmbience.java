package org.ladysnake.effective.ambience;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.world.biome.BiomeKeys;
import org.ladysnake.effective.ambience.sound.AmbientCondition;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EffectiveAmbience implements ClientModInitializer {
	public static final Set<AmbientCondition> AMBIENT_CONDITIONS = new HashSet<>();
	public static final Map<AmbientCondition, SoundInstance> AMBIENCES = new HashMap<>();

	@Override
	public void onInitializeClient() {
		AMBIENT_CONDITIONS.add(new AmbientCondition(null, ConventionalBiomeTags.PLAINS, AmbientCondition.TimeCondition.DAY, EffectiveAmbienceSounds.AMBIENT_PLAINS_DAY)); // plains day
		AMBIENT_CONDITIONS.add(new AmbientCondition(BiomeKeys.DEEP_DARK, null, AmbientCondition.TimeCondition.NONE, EffectiveAmbienceSounds.AMBIENT_DEEP_DARK)); // deep dark
	}
}
