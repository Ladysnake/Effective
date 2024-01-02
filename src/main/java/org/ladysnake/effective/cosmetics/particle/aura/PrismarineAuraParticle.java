package org.ladysnake.effective.cosmetics.particle.aura;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

import java.util.concurrent.ThreadLocalRandom;

public class PrismarineAuraParticle extends PrismarineCrystalParticle {
	public PrismarineAuraParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
		super(world, x, y, z, velocityX, velocityY, velocityZ, spriteProvider);

		this.setPos(this.x + TwilightLegacyFireflyParticle.getWanderingDistance(this.random), this.y + random.nextFloat() * 2d, this.z + TwilightLegacyFireflyParticle.getWanderingDistance(this.random));

		this.maxAge = ThreadLocalRandom.current().nextInt(100, 400);
	}

	public void tick() {
		if (this.age++ < this.maxAge) {
			this.colorAlpha = Math.min(1f, this.colorAlpha + 0.1f);
		}

		this.prevPosX = this.x;
		this.prevPosY = this.y;
		this.prevPosZ = this.z;

		this.move(this.velocityX, this.velocityY, this.velocityZ);

		if (this.age >= this.maxAge) {
			this.colorAlpha = Math.max(0f, this.colorAlpha - 0.1f);

			if (this.colorAlpha <= 0f) {
				this.markDead();
			}
		}

		this.colorRed = 0.8f + (float) Math.sin(this.age / 10f) * 0.2f;
//        this.colorBlue = 0.9f + (float) Math.cos(this.age/10f) * 0.1f;

		this.prevAngle = this.angle;
		if (this.onGround) {
			this.velocityX = 0;
			this.velocityY = 0;
			this.velocityZ = 0;
		}

		if (this.velocityY != 0) {
			this.angle += Math.PI * Math.sin(rotationFactor * this.age) / 2;
		}
	}


	public static class DefaultFactory implements ParticleFactory<DefaultParticleType> {
		private final SpriteProvider spriteProvider;

		public DefaultFactory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
			return new PrismarineAuraParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
		}
	}

}
