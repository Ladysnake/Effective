package org.ladysnake.effective.particle.types;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.SimpleParticleType;
import org.ladysnake.effective.particle.contracts.SplashParticleInitialData;

@Environment(EnvType.CLIENT)
public class SplashParticleType extends SimpleParticleType {
	public SplashParticleInitialData initialData;

	public SplashParticleType(boolean alwaysShow) {
		super(alwaysShow);
	}

	public ParticleEffect setData(SplashParticleInitialData target) {
		this.initialData = target;
		return this;
	}
}
