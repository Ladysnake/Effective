package ladysnake.effective.client.particle;

import ladysnake.effective.client.contracts.SplashParticleInitialData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleEffect;

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
