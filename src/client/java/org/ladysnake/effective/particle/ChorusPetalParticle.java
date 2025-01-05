package org.ladysnake.effective.particle;

import net.minecraft.block.Blocks;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.ladysnake.effective.index.EffectiveParticles;
import org.ladysnake.effective.utils.EffectiveUtils;

import java.util.Random;

public class ChorusPetalParticle extends SpriteBillboardParticle {
	private static final Random RANDOM = new Random();
	protected final float rotationFactor;
	private final float groundOffset;
	private boolean isInAir = true;

	public ChorusPetalParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
		super(world, x, y, z, velocityX, velocityY, velocityZ);

		this.scale *= 1f + RANDOM.nextFloat();
		this.maxAge = 300 + random.nextInt(60);
		this.collidesWithWorld = true;
		int variant = RANDOM.nextInt(3);
		this.setSprite(spriteProvider.getSprite(variant, 2));

		if (velocityY == 0f && velocityX == 0f && velocityZ == 0f) {
			this.alpha = 0f;
		}

		this.velocityY = velocityY - 0.15D - random.nextFloat() / 10;
		this.velocityX = velocityX - 0.05D - random.nextFloat() / 10;
		this.velocityZ = velocityZ - 0.05D - random.nextFloat() / 10;

		this.rotationFactor = EffectiveUtils.getRandomFloatOrNegative(world.random) * 0.1F;
		this.angle = random.nextFloat() * 360f;

		this.groundOffset = RANDOM.nextFloat() / 100f + 0.01f;
	}

	@Override
	public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
		if (this.isInAir) {
			super.buildGeometry(vertexConsumer, camera, tickDelta);
		} else {
			Quaternionf quaternionf = new Quaternionf();
			quaternionf.rotationYXZ(0f, (float) Math.toRadians(-90f), this.angle);
			this.method_60373(vertexConsumer, camera, quaternionf, tickDelta);
		}
	}

	@Override
	protected void method_60373(VertexConsumer vertexConsumer, Camera camera, Quaternionf quaternionf, float f) {
		Vec3d vec3d = camera.getPos();
		float g = (float) (MathHelper.lerp(f, this.prevPosX, this.x) - vec3d.getX());
		float h = (float) (MathHelper.lerp(f, this.prevPosY, this.y) - vec3d.getY()) + groundOffset;
		float i = (float) (MathHelper.lerp(f, this.prevPosZ, this.z) - vec3d.getZ());
		this.method_60374(vertexConsumer, quaternionf, g, h, i, f);
	}

	@Override
	protected int getBrightness(float tint) {
		return LightmapTextureManager.MAX_LIGHT_COORDINATE;
	}

	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
	}

	public void tick() {
		if (this.age++ < this.maxAge) {
			this.alpha = Math.min(1f, this.alpha + 0.1f);
		}

		this.prevPosX = this.x;
		this.prevPosY = this.y;
		this.prevPosZ = this.z;

		this.move(this.velocityX, this.velocityY, this.velocityZ);
		this.velocityX *= 0.99D;
		this.velocityY *= 0.99D;
		this.velocityZ *= 0.99D;

		this.green /= 1.002f;
		this.red = Math.max(this.green / 1.001f, 0.3f);

		if (this.age >= this.maxAge) {
			this.alpha = Math.max(0f, this.alpha - 0.1f);

			if (this.alpha <= 0f) {
				this.markDead();
			}
		}

		this.prevAngle = this.angle;
		if (this.onGround || this.world.getFluidState(BlockPos.ofFloored(this.x, this.y, this.z)).isIn(FluidTags.WATER)) {
			if (this.isInAir) {
				if (this.world.getBlockState(BlockPos.ofFloored(this.x, this.y, this.z)).getBlock() == Blocks.WATER) {
					for (int i = 0; i > -10; i--) {
						BlockPos pos = BlockPos.ofFloored(this.x, Math.round(this.y) + i, this.z);
						if (this.world.getBlockState(pos).getBlock() == Blocks.WATER && this.world.getBlockState(BlockPos.ofFloored(this.x, Math.round(this.y) + i, this.z)).getFluidState().isStill() && this.world.getBlockState(BlockPos.ofFloored(this.x, Math.round(this.y) + i + 1, this.z)).isAir()) {
							this.world.addParticle(EffectiveParticles.RIPPLE, this.x, Math.round(this.y) + i + 0.9f, this.z, 0, 0, 0);
							break;
						}
					}
				}

				this.velocityX = 0;
				this.velocityY = 0;
				this.velocityZ = 0;
				this.isInAir = false;
			}
		}

		if (this.velocityY != 0) {
			this.angle = (float) (Math.PI * rotationFactor * this.age);
		}
	}

	public static class Factory implements ParticleFactory<SimpleParticleType> {
		private final SpriteProvider spriteProvider;

		public Factory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		public Particle createParticle(SimpleParticleType SimpleParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
			return new ChorusPetalParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
		}
	}

}
