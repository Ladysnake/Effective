package org.ladysnake.effective.core.particle.types;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.impl.client.particle.FabricSpriteProviderImpl;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import org.ladysnake.effective.core.particle.WaterfallCloudParticle;
import team.lodestar.lodestone.systems.particle.world.options.WorldParticleOptions;
import team.lodestar.lodestone.systems.particle.world.type.LodestoneWorldParticleType;

@Environment(EnvType.CLIENT)
public class WaterfallCloudParticleType extends LodestoneWorldParticleType {
	public record Factory(SpriteProvider sprite) implements ParticleFactory<WorldParticleOptions> {
		@Override
		public Particle createParticle(WorldParticleOptions data, ClientWorld world, double x, double y, double z, double mx, double my, double mz) {
			return new WaterfallCloudParticle(world, data, (FabricSpriteProviderImpl) sprite, x, y, z, mx, my, mz);
		}
	}
}
