package org.ladysnake.effective.ambience.sound;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public record AmbientCondition(SoundEvent event, Type type, AmbiencePredicate predicate) {
	public interface AmbiencePredicate {
		boolean shouldPlay(World world, BlockPos pos, PlayerEntity player);
	}

	public static enum Type {
		WIND, ANIMAL, FOLIAGE, WATER;
	}
}
