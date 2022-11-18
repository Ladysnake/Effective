package ladysnake.effective.client.particle.types;

import ladysnake.effective.client.contracts.ColoredParticleInitialData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleEffect;

@Environment(EnvType.CLIENT)
public class ColoredParticleType extends DefaultParticleType {
	public ColoredParticleInitialData initialData;

	public ColoredParticleType(boolean alwaysShow) {
		super(alwaysShow);
	}

	public ParticleEffect setData(ColoredParticleInitialData target) {
		this.initialData = target;
		return this;
	}
}
