package org.ladysnake.effective.cosmetics.data;

import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.random.Random;

import java.util.function.Supplier;

public record AuraData(SimpleParticleType particle, Supplier<AuraSettings> auraSettingsSupplier) {

	public boolean shouldAddParticle(Random random, int age) {

		AuraSettings settings = auraSettingsSupplier().get();
		if (settings.spawnRate() == 0) return false;
		float rand = random.nextFloat();
		return rand <= settings.spawnRate() && (age % settings.delay() == 0);
	}
}
