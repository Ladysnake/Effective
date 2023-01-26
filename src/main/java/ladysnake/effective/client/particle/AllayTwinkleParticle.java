package ladysnake.effective.client.particle;

import com.sammy.lodestone.systems.rendering.particle.SimpleParticleEffect;
import com.sammy.lodestone.systems.rendering.particle.world.FrameSetParticle;
import com.sammy.lodestone.systems.rendering.particle.world.WorldParticleEffect;
import net.fabricmc.fabric.impl.client.particle.FabricSpriteProviderImpl;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;

public class AllayTwinkleParticle extends FrameSetParticle {
	public AllayTwinkleParticle(ClientWorld world, WorldParticleEffect data, FabricSpriteProviderImpl spriteSet, double x, double y, double z, double xd, double yd, double zd) {
		super(world, data, spriteSet, x, y, z, xd, yd, zd);
		addFrames(0, 5);
		setMaxAge(frameSet.size() * 3);
		this.scale = 0.12f;
	}

	@Override
	public void tick() {
		super.tick();
		this.scale = 0.12f;
		if (age < frameSet.size() * 3) {
			setSprite(frameSet.get(MathHelper.floor(age / 3f)));
		}
	}

	@Override
	public SimpleParticleEffect.Animator getAnimator() {
		return SimpleParticleEffect.Animator.WITH_AGE;
	}
}
