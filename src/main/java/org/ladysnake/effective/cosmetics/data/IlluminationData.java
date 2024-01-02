package org.ladysnake.effective.cosmetics.data;

import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.World;

import java.util.function.BiPredicate;
import java.util.function.Supplier;

public record IlluminationData(DefaultParticleType illuminationType,
							   BiPredicate<World, BlockPos> locationSpawnPredicate,
							   Supplier<Float> chanceSupplier) {

	public boolean shouldAddParticle(RandomGenerator random) {
		float chance = chanceSupplier.get();
		if (chance <= 0f) return false;
		float density = 1f;
		return random.nextFloat() <= chance * density;
	}
}
