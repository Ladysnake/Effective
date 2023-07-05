package ladysnake.effective.particle.types;

import ladysnake.effective.particle.contracts.SplashParticleInitialData;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleEffect;
import org.quiltmc.loader.api.minecraft.ClientOnly;

@ClientOnly
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
