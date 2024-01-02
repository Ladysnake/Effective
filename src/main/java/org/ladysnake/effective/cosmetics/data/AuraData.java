package org.ladysnake.effective.cosmetics.data;

import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.random.RandomGenerator;

import java.util.function.Supplier;

public record AuraData(DefaultParticleType particle, Supplier<AuraSettings> auraSettingsSupplier) {

	public boolean shouldAddParticle(RandomGenerator random, int age) {

		AuraSettings settings = auraSettingsSupplier().get();
		if (settings.spawnRate() == 0) return false;
		float rand = random.nextFloat();
		return rand <= settings.spawnRate() && (age % settings.delay() == 0);
	}
}
