package org.ladysnake.effective.core.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Blocks;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.BlockPos;
import org.ladysnake.effective.core.Effective;

public class CascadeParticle extends SpriteBillboardParticle {
	private final SpriteProvider spriteProvider;

	public CascadeParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
		super(world, x, y, z, velocityX, velocityY, velocityZ);

		this.velocityX = velocityX;
		this.velocityY = velocityY;
		this.velocityZ = velocityZ;

		this.spriteProvider = spriteProvider;
		this.maxAge = 10;
		this.scale = .5f;

		this.setSpriteForAge(spriteProvider);

//		WorldParticleBuilder.create(Effective.WATERFALL_CLOUD)
//			.enableForcedSpawn()
//			.enableNoClip()
//			.setMotion((world.getRandom().nextFloat() * waterfall.strength()) / 10f * Math.signum(offsetX), (world.getRandom().nextFloat() * waterfall.strength()) / 10f, (world.getRandom().nextFloat() * waterfall.strength()) / 10f * Math.signum(offsetZ))
//			.spawn(world, blockPos.getX() + .5 + offsetX, blockPos.getY() + world.getRandom().nextFloat(), blockPos.getZ() + .5 + offsetZ);
	}

	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
	}

	@Override
	public void tick() {
		super.tick();

		this.setSpriteForAge(this.spriteProvider);

		this.prevPosX = this.x;
		this.prevPosY = this.y;
		this.prevPosZ = this.z;

		if (this.onGround || (this.age > 10 && this.world.getBlockState(BlockPos.ofFloored(this.x, this.y + this.velocityY, this.z)).getBlock() == Blocks.WATER)) {
			this.velocityX *= 0.5f;
			this.velocityY *= 0.5f;
			this.velocityZ *= 0.5f;
		}

		if (this.world.getBlockState(BlockPos.ofFloored(this.x, this.y + this.velocityY, this.z)).getBlock() == Blocks.WATER && this.world.getBlockState(BlockPos.ofFloored(this.x, this.y, this.z)).isAir()) {
			this.velocityX *= 0.9;
			this.velocityY *= 0.9;
			this.velocityZ *= 0.9;
		}

		this.velocityX *= 0.95f;
		this.velocityY -= 0.02f;
		this.velocityZ *= 0.95f;

		this.move(velocityX, velocityY, velocityZ);
	}

	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleFactory<SimpleParticleType> {
		private final SpriteProvider spriteProvider;

		public Factory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		@Override
		public Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			return new CascadeParticle(world, x, y, z, velocityX, velocityY, velocityZ, this.spriteProvider);
		}
	}
}
