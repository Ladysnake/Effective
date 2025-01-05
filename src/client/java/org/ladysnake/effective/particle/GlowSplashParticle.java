package org.ladysnake.effective.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.effective.index.EffectiveParticles;
import org.ladysnake.effective.particle.types.SplashParticleType;

import java.awt.*;

public class GlowSplashParticle extends SplashParticle {
	protected GlowSplashParticle(ClientWorld world, double x, double y, double z) {
		super(world, x, y, z);
	}

	@Override
	public int getRimBrightness(float tickDelta) {
		return LightmapTextureManager.MAX_LIGHT_COORDINATE;
	}

	@Override
	public int getRimColor(BlockPos pos) {
		float redAndGreen = Math.min(1, world.getLightLevel(LightType.BLOCK, pos) / 15f);
		return new Color(redAndGreen, redAndGreen, blue, 1.0f).getRGB();
	}

	@Override
	public ParticleEffect getDropletParticle() {
		return EffectiveParticles.GLOW_DROPLET;
	}

	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleFactory<SimpleParticleType> {
		public Factory(SpriteProvider ignored) {
		}

		@Nullable
		@Override
		public Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			GlowSplashParticle instance = new GlowSplashParticle(world, x, y, z);
			if (parameters instanceof SplashParticleType splashParameters && splashParameters.initialData != null) {
				final float width = (float) splashParameters.initialData.width() * 2;
				instance.widthMultiplier = width;
				instance.heightMultiplier = (float) splashParameters.initialData.velocityY() * width;
				instance.wave1End = 10 + Math.round(width * 1.2f);
				instance.wave2Start = 6 + Math.round(width * 0.7f);
				instance.wave2End = 20 + Math.round(width * 2.4f);
			}
			return instance;
		}
	}
}
