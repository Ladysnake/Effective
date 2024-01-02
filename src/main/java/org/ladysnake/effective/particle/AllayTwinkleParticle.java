package org.ladysnake.effective.particle;

import team.lodestar.lodestone.systems.rendering.particle.world.FrameSetParticle;
import team.lodestar.lodestone.systems.rendering.particle.world.WorldParticleEffect;
import net.fabricmc.fabric.impl.client.particle.FabricSpriteProviderImpl;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;

public class AllayTwinkleParticle extends FrameSetParticle {
	public AllayTwinkleParticle(ClientWorld world, WorldParticleEffect data, FabricSpriteProviderImpl spriteSet, double x, double y, double z, double xd, double yd, double zd) {
		super(world, data, spriteSet, x, y, z, xd, yd, zd);
		addFrames(0, 5);
		setMaxAge(frameSet.size() * 3);
	}

	@Override
	public void tick() {
		super.tick();
		if (age < frameSet.size() * 3) {
			setSprite(frameSet.get(MathHelper.floor(age / 3f)));
		}
	}
}
