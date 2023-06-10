package ladysnake.illuminations.client.particle.aura;

import ladysnake.effective.client.particle.ChorusPetalParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;

public class GoldenrodAuraParticle extends ChorusPetalParticle {
	private int elevation = 0;

	public GoldenrodAuraParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
		super(world, x, y, z, velocityX, velocityY, velocityZ, spriteProvider);

		this.velocityY = 0;
		this.velocityX = 0;
		this.velocityZ = 0;
		this.scale = 0.9f;


		this.setPos(this.x + get_rando(), this.y + random.nextFloat() + 0.5 * 1.5d, this.z + get_rando());
	}

	public double get_rando() {
		double rando = (random.nextFloat() - 0.5) * 1.4;
		if (rando < 0.3 && rando > 0) {
			rando += 0.3;
		} else if (rando < 0 && rando > -0.3) {
			rando -= 0.3;
		}
		return rando;
	}

	public void tick() {
		this.age += 2;
		if (this.age < this.maxAge) {
			this.colorAlpha = Math.min(1f, this.colorAlpha + 0.1f);
		}
		this.scale *= 0.9;

		this.prevPosX = this.x;
		this.prevPosY = this.y;
		this.prevPosZ = this.z;

		this.move(this.velocityX, this.velocityY, this.velocityZ);
		if (this.velocityY == 0) {
			int temp_rand = random.nextInt(15);
			if (temp_rand == 0 && elevation < 1) {
				this.velocityY = 0.3;
				elevation += 1;
			} else if (temp_rand == 1 && elevation > -2) {
				this.velocityY = -0.3;
				elevation -= 1;
			}
		} else if (Math.abs(this.velocityY) > 0.08) {
			this.velocityY *= 0.5;
		} else {
			this.velocityY = 0;
		}

		this.colorBlue *= 0.96;
		this.colorGreen *= 0.98;

		if (this.age >= this.maxAge) {
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

		this.angle = 0;

	}


	public static class DefaultFactory implements ParticleFactory<DefaultParticleType> {
		private final SpriteProvider spriteProvider;

		public DefaultFactory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
			return new GoldenrodAuraParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
		}
	}

}
