package org.ladysnake.effective.ambience.sound;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Nullable;

public record AmbientCondition(@Nullable RegistryKey<Biome> specificBiomeCondition, @Nullable TagKey<Biome> biomeTagCondition, TimeCondition timeCondition, SoundEvent sound) {
	public enum TimeCondition {
		DAY, NIGHT, NONE
	}

	public boolean shouldPlay(ClientPlayerEntity player) {
		World world = player.getWorld();
		BlockPos blockPos = player.getBlockPos();
		RegistryEntry<Biome> biome = world.getBiome(blockPos);

		boolean canPlay = false;
		if (this.specificBiomeCondition() != null) {
			canPlay = biome.matchesKey(this.specificBiomeCondition());
		} else if (this.biomeTagCondition() != null) {
			canPlay = biome.isIn(this.biomeTagCondition());
		}

		return canPlay &&
			(this.timeCondition() == AmbientCondition.TimeCondition.NONE
				|| world.isDay() && this.timeCondition() == AmbientCondition.TimeCondition.DAY
				|| world.isNight() && this.timeCondition() == AmbientCondition.TimeCondition.NIGHT);
	}
}
