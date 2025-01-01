package org.ladysnake.effective.core.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import org.ladysnake.effective.core.Effective;
import org.ladysnake.effective.core.render.particle.SoftParticleRenderType;

public class MistParticle extends SpriteBillboardParticle {
	public MistParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
		super(world, x, y, z, velocityX, velocityY, velocityZ);

		this.velocityX = velocityX;
		this.velocityY = velocityY;
		this.velocityZ = velocityZ;

		this.setSpriteForAge(spriteProvider);

		this.scale = 10f + world.random.nextFloat() * 5f;
		this.maxAge = 100;
		this.alpha = 0.1f;

//		WorldParticleBuilder.create(Effective.MIST)
//			.enableForcedSpawn()
//			.setSpinData(SpinParticleData.create((world.random.nextFloat() - world.random.nextFloat()) / 20f).build())
//			.setScaleData(GenericParticleData.create(10f + world.random.nextFloat() * 5f).build())
//			.setTransparencyData(
//				GenericParticleData.create(0.001f, 0.1f, 0f)
//					.setEasing(Easing.EXPO_OUT, Easing.SINE_OUT)
//					.build()
//			)
//			.setLifetime(300)
//			.enableNoClip()
//			.setNaturalLighting()
//			.setRenderType(LodestoneWorldParticleRenderType.TRANSPARENT.withDepthFade())
//			.setColorData(ColorParticleData.create(waterfall.mistColor(), waterfall.mistColor()).build())
//			.setMotion(world.getRandom().nextFloat() / 15f * Math.signum(offsetX), world.getRandom().nextGaussian() / 25f, world.getRandom().nextFloat() / 15f * Math.signum(offsetZ))
//			.spawn(world, blockPos.getX() + .5f, blockPos.getY() + .5f, blockPos.getZ() + .5f);
	}

	@Override
	public ParticleTextureSheet getType() {
		return SoftParticleRenderType.SOFT_PARTICLE;
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
