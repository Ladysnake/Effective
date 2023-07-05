package ladysnake.effective.cosmetics.particle.type;

import ladysnake.effective.cosmetics.particle.contracts.FireflyParticleInitialData;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleEffect;
import org.quiltmc.loader.api.minecraft.ClientOnly;

@ClientOnly
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
