package ladysnake.illuminations.client.particle.aura;

import ladysnake.effective.client.particle.ChorusPetalParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;

public class ChorusAuraParticle extends ChorusPetalParticle {
	public ChorusAuraParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
		super(world, x, y, z, velocityX, velocityY, velocityZ, spriteProvider);

		this.velocityY = -0.01 - random.nextFloat() / 10;
		this.velocityX = random.nextGaussian() / 50;
		this.velocityZ = random.nextGaussian() / 50;

		this.setPos(this.x + TwilightFireflyParticle.getWanderingDistance(this.random), this.y + random.nextFloat() * 2d, this.z + TwilightFireflyParticle.getWanderingDistance(this.random));
	}

	public void tick() {
		if (this.age++ < this.maxAge) {
			this.colorAlpha = Math.min(1f, this.colorAlpha + 0.1f);
		}

		this.prevPosX = this.x;
		this.prevPosY = this.y;
		this.prevPosZ = this.z;

		this.move(this.velocityX, this.velocityY, this.velocityZ);
		this.velocityX *= 0.99D;
		this.velocityY *= 0.99D;
		this.velocityZ *= 0.99D;

		this.colorRed *= 0.99;
		this.colorGreen *= 0.98;

		if (this.age >= this.maxAge) {
//            this.colorRed *= 0.9;
//            this.colorGreen *= 0.8;

			this.colorAlpha = Math.max(0f, this.colorAlpha - 0.1f);

			if (this.colorAlpha <= 0f) {
				this.markDead();
			}
		}

		this.prevAngle = this.angle;
		if (this.onGround || this.world.getFluidState(BlockPos.create(this.x, this.y, this.z)).isIn(FluidTags.WATER)) {
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
			return new ChorusAuraParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
		}
	}

}
