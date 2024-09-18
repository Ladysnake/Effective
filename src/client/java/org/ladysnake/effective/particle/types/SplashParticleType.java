package org.ladysnake.effective.particle.types;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleEffect;
import org.ladysnake.effective.particle.contracts.SplashParticleInitialData;

@Environment(EnvType.CLIENT)
public class SplashParticleType extends DefaultParticleType {
	public SplashParticleInitialData initialData;

	public SplashParticleType(boolean alwaysShow) {
		super(alwaysShow);
	}

	public ParticleEffect setData(SplashParticleInitialData target) {
		this.initialData = target;
		return this;
	}
}
