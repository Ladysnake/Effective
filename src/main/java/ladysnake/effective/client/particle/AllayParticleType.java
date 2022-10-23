package ladysnake.effective.client.particle;

import ladysnake.effective.client.contracts.AllayParticleInitialData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleEffect;

@Environment(EnvType.CLIENT)
public class AllayParticleType extends DefaultParticleType {
	public AllayParticleInitialData initialData;

	public AllayParticleType(boolean alwaysShow) {
		super(alwaysShow);
	}

	public ParticleEffect setData(AllayParticleInitialData target) {
		this.initialData = target;
		return this;
	}
}
