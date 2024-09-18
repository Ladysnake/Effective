package org.ladysnake.effective.particle;

import net.fabricmc.fabric.impl.client.particle.FabricSpriteProviderImpl;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import team.lodestar.lodestone.systems.particle.world.FrameSetParticle;
import team.lodestar.lodestone.systems.particle.world.options.WorldParticleOptions;

public class BubbleParticle extends FrameSetParticle {
	public BubbleParticle(ClientWorld world, WorldParticleOptions data, FabricSpriteProviderImpl spriteSet, double x, double y, double z, double xd, double yd, double zd) {
		super(world, data, spriteSet, x, y, z, xd, yd, zd);
	}

	@Override
	public void tick() {
		super.tick();

		if (!world.isWater(BlockPos.ofFloored(this.x, this.y, this.z))) {
			this.markDead();
		}
	}
}
