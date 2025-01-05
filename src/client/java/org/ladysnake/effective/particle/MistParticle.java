package org.ladysnake.effective.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.MathHelper;
import org.ladysnake.effective.render.particle.SoftParticleRenderType;

public class MistParticle extends SpriteBillboardParticle {
	public MistParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
		super(world, x, y, z, velocityX, velocityY, velocityZ);

		this.velocityX = velocityX;
		this.velocityY = velocityY;
		this.velocityZ = velocityZ;

		this.setSpriteForAge(spriteProvider);

		this.scale = 10f + world.random.nextFloat() * 5f;
		this.maxAge = 300;
		this.alpha = 0.0001f;

		this.velocityMultiplier = 0.999f;
	}

	@Override
	public ParticleTextureSheet getType() {
		return SoftParticleRenderType.SOFT_PARTICLE;
	}

	@Override
	public void tick() {
		super.tick();

		if (this.age <= 20) {
			this.alpha = MathHelper.lerp(this.age / 20f, 0.0f, 0.2f);
		} else {
			this.alpha = MathHelper.lerp((this.age - 20f) / this.maxAge, 0.2f, 0.0f);
		}
	}

	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleFactory<SimpleParticleType> {
		private final SpriteProvider spriteProvider;

		public Factory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		@Override
		public Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			return new MistParticle(world, x, y, z, velocityX, velocityY, velocityZ, this.spriteProvider);
		}
	}
}
