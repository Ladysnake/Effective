package org.ladysnake.effective.cosmetics.data;

import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.function.BiPredicate;
import java.util.function.Supplier;

public record IlluminationData(SimpleParticleType illuminationType,
							   BiPredicate<World, BlockPos> locationSpawnPredicate,
							   Supplier<Float> chanceSupplier) {

	public boolean shouldAddParticle(Random random) {
		float chance = chanceSupplier.get();
		if (chance <= 0f) return false;
		float density = 1f;
		return random.nextFloat() <= chance * density;
	}
}
