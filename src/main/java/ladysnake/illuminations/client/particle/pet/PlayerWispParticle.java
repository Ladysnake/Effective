package ladysnake.illuminations.client.particle.pet;

import ladysnake.illuminations.client.particle.WillOWispParticle;
import ladysnake.illuminations.client.particle.WispTrailParticleEffect;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class PlayerWispParticle extends WillOWispParticle {
	protected PlayerEntity owner;

	protected PlayerWispParticle(ClientWorld world, double x, double y, double z, Identifier texture, float red, float green, float blue, float redEvolution, float greenEvolution, float blueEvolution) {
		super(world, x, y, z, texture, red, green, blue, redEvolution, greenEvolution, blueEvolution);

		this.maxAge = 35;
		this.owner = world.getClosestPlayer((TargetPredicate.createNonAttackable()).setBaseMaxDistance(1D), this.x, this.y, this.z);

		if (this.owner == null) {
			this.markDead();
		}

		this.colorRed = red;
		this.colorGreen = green;
		this.colorBlue = blue;
		this.colorAlpha = 0;
	}

	@Override
	public void tick() {
		if (this.age > 10) {
			this.colorAlpha = 1f;

			for (int i = 0; i < 1; i++) {
				this.world.addParticle(new WispTrailParticleEffect(this.colorRed, this.colorGreen, this.colorBlue, this.redEvolution, this.greenEvolution, this.blueEvolution), this.x + random.nextGaussian() / 15, this.y + random.nextGaussian() / 15, this.z + random.nextGaussian() / 15, 0, 0, 0);
			}
		} else {
			this.colorAlpha = 0;
		}

		if (owner != null) {
			this.prevPosX = this.x;
			this.prevPosY = this.y;
			this.prevPosZ = this.z;

			// die if old enough
			if (this.age++ >= this.maxAge) {
				this.markDead();
			}

			this.setPos(owner.getX() + Math.cos(owner.bodyYaw / 50) * 0.5, owner.getY() + owner.getHeight() + 0.5f + Math.sin(owner.age / 12f) / 12f, owner.getZ() - Math.cos(owner.bodyYaw / 50) * 0.5);

			this.pitch = -owner.getPitch();
			this.prevPitch = -owner.prevPitch;
			this.yaw = -owner.getYaw();
			this.prevYaw = -owner.prevYaw;
		} else {
			this.markDead();
		}
	}

	@Environment(EnvType.CLIENT)
	public static class DefaultFactory implements ParticleFactory<DefaultParticleType> {
		private final Identifier texture;
		private final float red;
		private final float green;
		private final float blue;
		private final float redEvolution;
		private final float greenEvolution;
		private final float blueEvolution;

		public DefaultFactory(SpriteProvider spriteProvider, Identifier texture, float red, float green, float blue, float redEvolution, float greenEvolution, float blueEvolution) {
			this.texture = texture;
			this.red = red;
			this.green = green;
			this.blue = blue;
			this.redEvolution = redEvolution;
			this.greenEvolution = greenEvolution;
			this.blueEvolution = blueEvolution;
		}

		@Nullable
		@Override
		public Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			return new PlayerWispParticle(world, x, y, z, this.texture, this.red, this.green, this.blue, this.redEvolution, this.greenEvolution, this.blueEvolution);
		}
	}
}
