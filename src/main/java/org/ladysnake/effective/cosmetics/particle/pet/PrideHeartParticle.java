package org.ladysnake.effective.cosmetics.particle.pet;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.effective.cosmetics.render.entity.model.pet.PrideHeartModel;

public class PrideHeartParticle extends PlayerLanternParticle {
	protected PrideHeartParticle(ClientWorld world, double x, double y, double z, Identifier texture, float red, float green, float blue) {
		super(world, x, y, z, texture, red, green, blue);

		this.model = new PrideHeartModel(MinecraftClient.getInstance().getEntityModelLoader().getModelPart(PrideHeartModel.MODEL_LAYER));
	}

	@Override
	public void tick() {
		super.tick();
	}


	public static class DefaultFactory implements ParticleFactory<DefaultParticleType> {
		private final Identifier texture;
		private final float red;
		private final float green;
		private final float blue;

		public DefaultFactory(SpriteProvider spriteProvider, Identifier texture, float red, float green, float blue) {
			this.texture = texture;
			this.red = red;
			this.green = green;
			this.blue = blue;
		}

		@Nullable
		@Override
		public Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			return new PrideHeartParticle(world, x, y, z, this.texture, this.red, this.green, this.blue);
		}
	}
}
