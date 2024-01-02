package org.ladysnake.effective.cosmetics.particle.pet;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.effective.cosmetics.EffectiveCosmetics;
import org.ladysnake.effective.cosmetics.data.PlayerCosmeticData;
import org.ladysnake.effective.particle.WillOWispParticle;

import java.util.Objects;

public class PlayerWispParticle extends WillOWispParticle {
	protected PlayerEntity owner;

	protected PlayerWispParticle(ClientWorld world, double x, double y, double z, Identifier texture, float red, float green, float blue, float redEvolution, float greenEvolution, float blueEvolution) {
		super(world, x, y, z, texture, red, green, blue, redEvolution, greenEvolution, blueEvolution);

		this.prevPosX = this.x;
		this.prevPosY = this.y;
		this.prevPosZ = this.z;

		this.maxAge = 35;
		this.owner = world.getClosestPlayer((TargetPredicate.createNonAttackable()).setBaseMaxDistance(1D), this.x, this.y, this.z);

		if (this.owner == null) {
			this.markDead();
		}

		if (owner != null && owner.getUuid() != null && EffectiveCosmetics.getCosmeticData(owner) != null) {
			PlayerCosmeticData data = Objects.requireNonNull(EffectiveCosmetics.getCosmeticData(owner));
			this.colorRed = data.getColor1Red() / 255f;
			this.colorGreen = data.getColor1Green() / 255f;
			this.colorBlue = data.getColor1Blue() / 255f;
			this.gotoRed = data.getColor2Red() / 255f;
			this.gotoGreen = data.getColor2Green() / 255f;
			this.gotoBlue = data.getColor2Blue() / 255f;
		} else {
			this.markDead();
		}
		this.colorAlpha = 0;
	}

	@Override
	public void tick() {
		if (this.age > 10) {
			this.colorAlpha = 1f;
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
