package org.ladysnake.effective.cosmetics.particle.type;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleEffect;
import org.ladysnake.effective.cosmetics.particle.contracts.FireflyParticleInitialData;

@Environment(EnvType.CLIENT)
public class LegacyFireflyParticleType extends DefaultParticleType {
	public FireflyParticleInitialData initialData;

	public LegacyFireflyParticleType(boolean alwaysShow) {
		super(alwaysShow);
	}

	public ParticleEffect setData(FireflyParticleInitialData target) {
		this.initialData = target;
		return this;
	}
}
