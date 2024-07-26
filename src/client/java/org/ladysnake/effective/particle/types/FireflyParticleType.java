package org.ladysnake.effective.particle.types;

import com.mojang.serialization.Codec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.impl.client.particle.FabricSpriteProviderImpl;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleType;
import org.ladysnake.effective.particle.FireflyParticle;
import team.lodestar.lodestone.systems.rendering.particle.world.WorldParticleEffect;

@Environment(EnvType.CLIENT)
public class FireflyParticleType extends ParticleType<WorldParticleEffect> {

	public FireflyParticleType() {
		super(false, WorldParticleEffect.DESERIALIZER);
	}

	@Override
	public boolean shouldAlwaysSpawn() {
		return true;
	}

	@Override
	public Codec<WorldParticleEffect> getCodec() {
		return WorldParticleEffect.codecFor(this);
	}

	public record Factory(SpriteProvider sprite) implements ParticleFactory<WorldParticleEffect> {
		@Override
		public Particle createParticle(WorldParticleEffect data, ClientWorld world, double x, double y, double z, double mx, double my, double mz) {
			return new FireflyParticle(world, data, (FabricSpriteProviderImpl) sprite, x, y, z, mx, my, mz);
		}
	}
}
