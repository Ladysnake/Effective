package org.ladysnake.effective.core.particle;

import net.fabricmc.fabric.impl.client.particle.FabricSpriteProviderImpl;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import team.lodestar.lodestone.systems.particle.world.FrameSetParticle;
import team.lodestar.lodestone.systems.particle.world.options.WorldParticleOptions;

public class AllayTwinkleParticle extends FrameSetParticle {
	public AllayTwinkleParticle(ClientWorld world, WorldParticleOptions data, FabricSpriteProviderImpl spriteSet, double x, double y, double z, double xd, double yd, double zd) {
		super(world, data, spriteSet, x, y, z, xd, yd, zd);
		addFrames(0, 5);
		setMaxAge(frameSet.size() * 3);
	}

	@Override
	public void tick() {
		super.tick();
		if (age < frameSet.size() * 3) {
			this.setSprite(this.spriteSet.getSprites().get(frameSet.get(MathHelper.floor(age / 3f))));
		}
	}
}
