package org.ladysnake.effective.cosmetics.particle.aura;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.DefaultParticleType;
import org.ladysnake.effective.cosmetics.EffectiveCosmetics;
import org.ladysnake.effective.cosmetics.data.PlayerCosmeticData;

import java.util.Objects;

public class PrismaticConfettiParticle extends ConfettiParticle {
	public PrismaticConfettiParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
		super(world, x, y, z, velocityX, velocityY, velocityZ, spriteProvider);

		PlayerEntity owner = world.getClosestPlayer(TargetPredicate.createNonAttackable().setBaseMaxDistance(1D), this.x, this.y, this.z);

		if (owner != null && owner.getUuid() != null && EffectiveCosmetics.getCosmeticData(owner) != null) {
			PlayerCosmeticData data = Objects.requireNonNull(EffectiveCosmetics.getCosmeticData(owner));
			this.colorRed = data.getColor1Red() / 255f;
			this.colorGreen = data.getColor1Green() / 255f;
			this.colorBlue = data.getColor1Blue() / 255f;
		} else {
			this.markDead();
		}
	}


	public static class DefaultFactory implements ParticleFactory<DefaultParticleType> {
		private final SpriteProvider spriteProvider;

		public DefaultFactory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
			return new PrismaticConfettiParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
		}
	}

}
