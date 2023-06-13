package ladysnake.effective.particle.types;

import ladysnake.effective.particle.contracts.FireflyParticleInitialData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleEffect;

@Environment(EnvType.CLIENT)
public class FireflyParticleType extends DefaultParticleType {
	public FireflyParticleInitialData initialData;

	public FireflyParticleType(boolean alwaysShow) {
		super(alwaysShow);
	}

	public ParticleEffect setData(FireflyParticleInitialData target) {
		this.initialData = target;
		return this;
	}
}
