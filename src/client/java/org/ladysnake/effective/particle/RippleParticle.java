package org.ladysnake.effective.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import org.joml.Quaternionf;

import java.util.Random;

public class RippleParticle extends SpriteBillboardParticle {
	public final SpriteProvider spriteProvider;

	public RippleParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
		super(world, x, y, z, velocityX, velocityY, velocityZ);

		this.spriteProvider = spriteProvider;
		this.setSpriteForAge(spriteProvider);

		this.velocityX = 0;
		this.velocityY = 0;
		this.velocityZ = 0;

		int scaleAgeModifier = 1 + new Random().nextInt(10);
		this.scale *= 2f + random.nextFloat() / 10f * scaleAgeModifier;
		this.maxAge = 10 + new Random().nextInt(scaleAgeModifier);
	}

	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
	}

	@Override
	public void tick() {
		this.prevPosX = this.x;
		this.prevPosY = this.y;
		this.prevPosZ = this.z;

		if (this.age++ >= this.maxAge) {
			this.markDead();
		}

		this.setSpriteForAge(spriteProvider);
	}

	@Override
	public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
		Quaternionf quaternionf = new Quaternionf();
		quaternionf.rotateX((float) Math.toRadians(-90f));
		this.method_60373(vertexConsumer, camera, quaternionf, tickDelta);
	}

	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleFactory<SimpleParticleType> {
		private final SpriteProvider spriteProvider;

		public Factory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		@Override
		public Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			return new RippleParticle(world, x, y, z, velocityX, velocityY, velocityZ, this.spriteProvider);
		}
	}
}
