package ladysnake.illuminations.client.particle.aura;

import ladysnake.effective.client.particle.ChorusPetalParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.fluid.Fluids;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.BlockPos;

public class ShadowbringerParticle extends ChorusPetalParticle {

	private final SpriteProvider spriteProvider;
	private final float randEffect = random.nextFloat() + 0.5F;
	boolean negateX = random.nextBoolean(), negateZ = random.nextBoolean();

	public ShadowbringerParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
		super(world, x, y, z, velocityX, velocityY, velocityZ, spriteProvider);

		this.maxAge = 40 + random.nextInt(40);
		this.velocityY = (0.2 + random.nextFloat()) / 10;
		this.velocityX = negateX ? -random.nextGaussian() / 50 : random.nextGaussian() / 50;
		this.velocityZ = negateZ ? -random.nextGaussian() / 50 : random.nextGaussian() / 50;
		this.scale = (float) (scale + (random.nextGaussian() / 12.0));
		this.spriteProvider = spriteProvider;

		this.setSprite(spriteProvider.getSprite(0, 3));
		this.colorAlpha = 0;


		this.setPos(this.x + TwilightFireflyParticle.getWanderingDistance(this.random), this.y + random.nextFloat() * 1.5, this.z + TwilightFireflyParticle.getWanderingDistance(this.random));
	}

	public void tick() {
		if (this.age++ < this.maxAge) {
			this.colorAlpha = Math.min(1f, this.colorAlpha + 0.045f);
		}

		this.prevPosX = this.x;
		this.prevPosY = this.y;
		this.prevPosZ = this.z;

		velocityX = velocityX * 0.85 + (negateX ? -(Math.sin(age / (2.0 + randEffect)) / 20.0) : Math.sin(age / 3.0) / 20.0);
		velocityZ = velocityZ * 0.85 + (negateZ ? -(Math.sin(age / (2.0 + randEffect)) / 20.0) : Math.sin(age / 3.0) / 20.0);

		this.move(this.velocityX, this.velocityY, this.velocityZ);

		if (age > 0 && maxAge > 0) {
			float agePercent = (float) ((float) age / maxAge * 1.5);
			this.setSprite(spriteProvider.getSprite(Math.min(3, (int) (agePercent * 4)), 3));
		}

		if (this.age >= this.maxAge) {
			this.colorAlpha = Math.max(0f, this.colorAlpha - 0.015f);

			if (this.colorAlpha <= 0f) {
				this.markDead();
			}
		}

		this.prevAngle = this.angle;
		if (this.onGround || this.world.getFluidState(BlockPos.create(this.x, this.y, this.z)).getFluid() != Fluids.EMPTY) {
			this.velocityY *= 0.95;
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
			return new ShadowbringerParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
		}
	}
}
