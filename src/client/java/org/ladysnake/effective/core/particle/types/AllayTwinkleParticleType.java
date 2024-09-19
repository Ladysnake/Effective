package org.ladysnake.effective.core.particle.types;

import net.fabricmc.fabric.impl.client.particle.FabricSpriteProviderImpl;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.effective.core.particle.AllayTwinkleParticle;
import team.lodestar.lodestone.systems.particle.world.options.WorldParticleOptions;
import team.lodestar.lodestone.systems.particle.world.type.LodestoneWorldParticleType;

public class AllayTwinkleParticleType extends LodestoneWorldParticleType {
	public static class Factory implements ParticleFactory<WorldParticleOptions> {
		private final SpriteProvider sprite;

		public Factory(SpriteProvider sprite) {
			this.sprite = sprite;
		}


		@Nullable
		@Override
		public Particle createParticle(WorldParticleOptions data, ClientWorld world, double x, double y, double z, double mx, double my, double mz) {
			return new AllayTwinkleParticle(world, data, (FabricSpriteProviderImpl) sprite, x, y, z, mx, my, mz);
		}
	}
}
