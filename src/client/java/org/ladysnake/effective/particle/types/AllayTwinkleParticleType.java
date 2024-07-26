package org.ladysnake.effective.particle.types;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.impl.client.particle.FabricSpriteProviderImpl;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleType;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.effective.particle.AllayTwinkleParticle;
import team.lodestar.lodestone.systems.rendering.particle.world.WorldParticleEffect;

public class AllayTwinkleParticleType extends ParticleType<WorldParticleEffect> {
	public AllayTwinkleParticleType() {
		super(false, WorldParticleEffect.DESERIALIZER);
	}

	@Override
	public Codec<WorldParticleEffect> getCodec() {
		return WorldParticleEffect.codecFor(this);
	}

	public static class Factory implements ParticleFactory<WorldParticleEffect> {
		private final SpriteProvider sprite;

		public Factory(SpriteProvider sprite) {
			this.sprite = sprite;
		}


		@Nullable
		@Override
		public Particle createParticle(WorldParticleEffect data, ClientWorld world, double x, double y, double z, double mx, double my, double mz) {
			return new AllayTwinkleParticle(world, data, (FabricSpriteProviderImpl) sprite, x, y, z, mx, my, mz);
		}
	}
}
