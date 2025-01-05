package org.ladysnake.effective.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Blocks;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import org.ladysnake.effective.index.EffectiveParticles;

public class GlowDropletParticle extends DropletParticle {
	public float redAndGreen = random.nextFloat() / 5f;
	public float blue = 1.0f;

	private GlowDropletParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
		super(world, x, y, z, velocityX, velocityY, velocityZ, spriteProvider);

		float redAndGreenRender = Math.min(1, redAndGreen + world.getLightLevel(LightType.BLOCK, BlockPos.ofFloored(x, y, z)) / 15f);
		this.red = redAndGreenRender;
		this.green = redAndGreenRender;
	}

	@Override
	public void tick() {
		this.prevPosX = this.x;
		this.prevPosY = this.y;
		this.prevPosZ = this.z;

		if (this.age++ >= this.maxAge) {
			this.markDead();
		}

		if (this.onGround || (this.age > 5 && this.world.getBlockState(BlockPos.ofFloored(this.x, this.y + this.velocityY, this.z)).getBlock() == Blocks.WATER)) {
			this.markDead();
		}

		if (this.world.getBlockState(BlockPos.ofFloored(this.x, this.y + this.velocityY, this.z)).getBlock() == Blocks.WATER && this.world.getBlockState(BlockPos.ofFloored(this.x, this.y, this.z)).isAir()) {
			for (int i = 0; i > -10; i--) {
				BlockPos pos = BlockPos.ofFloored(this.x, Math.round(this.y) + i, this.z);
				if (this.world.getBlockState(pos).getBlock() == Blocks.WATER && this.world.getBlockState(BlockPos.ofFloored(this.x, Math.round(this.y) + i, this.z)).getFluidState().isStill() && this.world.getBlockState(BlockPos.ofFloored(this.x, Math.round(this.y) + i + 1, this.z)).isAir()) {
					this.world.addParticle(EffectiveParticles.GLOW_RIPPLE, this.x, Math.round(this.y) + i + 0.9f, this.z, 0, 0, 0);
					break;
				}
			}

			this.markDead();
		}

		this.velocityX *= 0.99f;
		this.velocityY -= 0.05f;
		this.velocityZ *= 0.99f;

		this.move(velocityX, velocityY, velocityZ);
	}

	@Override
	protected int getBrightness(float tint) {
		return LightmapTextureManager.MAX_LIGHT_COORDINATE;
	}

	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleFactory<SimpleParticleType> {
		private final SpriteProvider spriteProvider;

		public Factory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		@Override
		public Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			return new GlowDropletParticle(world, x, y, z, velocityX, velocityY, velocityZ, this.spriteProvider);
		}
	}
}
