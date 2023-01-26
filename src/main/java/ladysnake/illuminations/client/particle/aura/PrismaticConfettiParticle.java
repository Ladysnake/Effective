package ladysnake.illuminations.client.particle.aura;

import ladysnake.illuminations.client.Illuminations;
import ladysnake.illuminations.client.data.PlayerCosmeticData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.DefaultParticleType;

import java.util.Objects;

public class PrismaticConfettiParticle extends ConfettiParticle {
	public PrismaticConfettiParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
		super(world, x, y, z, velocityX, velocityY, velocityZ, spriteProvider);

		PlayerEntity owner = world.getClosestPlayer(TargetPredicate.createNonAttackable().setBaseMaxDistance(1D), this.x, this.y, this.z);

		if (owner != null && owner.getUuid() != null && Illuminations.getCosmeticData(owner) != null) {
			PlayerCosmeticData data = Objects.requireNonNull(Illuminations.getCosmeticData(owner));
			this.colorRed = data.getColorRed() / 255f;
			this.colorGreen = data.getColorGreen() / 255f;
			this.colorBlue = data.getColorBlue() / 255f;
		} else {
			this.markDead();
		}
	}

	@Environment(EnvType.CLIENT)
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
