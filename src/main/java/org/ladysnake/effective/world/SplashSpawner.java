package org.ladysnake.effective.world;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.effective.Effective;
import org.ladysnake.effective.EffectiveConfig;
import org.ladysnake.effective.EffectiveUtils;
import org.ladysnake.effective.particle.contracts.SplashParticleInitialData;
import org.ladysnake.effective.particle.types.SplashParticleType;

public final class SplashSpawner {
	public static void trySpawnSplash(Entity entity) {
		Entity topMostEntity = entity.hasPassengers() && entity.getPrimaryPassenger() != null ? entity.getPrimaryPassenger() : entity;
		if (!(topMostEntity instanceof FishingBobberEntity)) {
			float amplifier = topMostEntity == entity ? 0.2f : 0.9f;
			Vec3d impactVelocity = topMostEntity.getVelocity();

			if (impactVelocity.length() < EffectiveConfig.splashThreshold) return;

			float splashIntensity = Math.min(1.0f, computeSplashIntensity(impactVelocity, amplifier));

			for (int y = -10; y < 10; y++) {
				if (isValidSplashPosition(entity, y)) {
					float splashY = Math.round(entity.getY()) + y + 0.9f;
					entity.getWorld().playSound(
						entity.getX(),
						splashY,
						entity.getZ(),
						topMostEntity instanceof PlayerEntity ? SoundEvents.ENTITY_PLAYER_SPLASH : SoundEvents.ENTITY_GENERIC_SPLASH,
						SoundCategory.AMBIENT,
						splashIntensity * 10f,
						0.8f,
						true
					);
					SplashParticleInitialData data = new SplashParticleInitialData(topMostEntity.getWidth(), impactVelocity.getY());
					spawnSplash(
						entity.getWorld(),
						entity.getX(),
						splashY,
						entity.getZ(),
						data
					);
					break;
				}
			}

			spawnWaterEffects(entity);
		}
	}

	private static float computeSplashIntensity(Vec3d impactVelocity, float f) {
		return (float) Math.sqrt(
			impactVelocity.x * impactVelocity.x * (double) 0.2f +
				impactVelocity.y * impactVelocity.y +
				impactVelocity.z * impactVelocity.z * (double) 0.2f
		) * f;
	}

	private static void spawnWaterEffects(Entity entity) {
		RandomGenerator random = entity.getWorld().getRandom();

		for (int j = 0; j < entity.getWidth() * 25f; j++) {
			EffectiveUtils.spawnWaterEffect(entity.getWorld(), new Vec3d(entity.getX() + random.nextGaussian() * entity.getWidth() / 5f, entity.getY(), entity.getZ() + random.nextGaussian() * entity.getWidth()), random.nextGaussian() / 15f, random.nextFloat() / 2.5f, random.nextGaussian() / 15f, EffectiveUtils.WaterEffectType.DROPLET);
		}
	}

	private static boolean isValidSplashPosition(Entity entity, int yOffset) {
		BlockPos pos = BlockPos.create(entity.getX(), Math.round(entity.getY()) + yOffset, entity.getZ());
		BlockState blockState = entity.getWorld().getBlockState(pos);

		if (blockState.getFluidState().getFluid() == Fluids.WATER) {
			if (blockState.getFluidState().isSource()) {
				return entity.getWorld().getBlockState(pos.up()).isAir();
			}
		}
		return false;
	}

	/**
	 * Chooses between spawning a normal splash or glow splash depending on biome
	 */
	private static void spawnSplash(World world, double x, double y, double z, @Nullable SplashParticleInitialData data) {
		SplashParticleType splash = EffectiveUtils.isGlowingWater(world, BlockPos.create(x, y, z)) ? Effective.GLOW_SPLASH : Effective.SPLASH;
        world.addParticle(splash.setData(data), x, y, z, 0, 0, 0);
	}
}
