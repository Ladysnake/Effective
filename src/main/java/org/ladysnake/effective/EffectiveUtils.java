package org.ladysnake.effective;

import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biomes;

import java.awt.*;

public class EffectiveUtils {
	public static boolean isGoingFast(AllayEntity allayEntity) {
		Vec3d velocity = allayEntity.getVelocity();
		float speedRequired = 0.1f;

		return (velocity.getX() >= speedRequired || velocity.getX() <= -speedRequired)
			|| (velocity.getY() >= speedRequired || velocity.getY() <= -speedRequired)
			|| (velocity.getZ() >= speedRequired || velocity.getZ() <= -speedRequired);
	}

	/**
     * chooses between spawning a normal droplet / ripple / waterfall cloud or glow one depending on biome
     */
	public static void spawnWaterEffect(World world, Vec3d pos, double velocityX, double velocityY, double velocityZ, WaterEffectType waterEffect) {
		DefaultParticleType particle = switch (waterEffect) {
			case DROPLET -> Effective.DROPLET;
			case RIPPLE -> Effective.RIPPLE;
		};
		if (isGlowingWater(world, pos)) {
			particle = switch (waterEffect) {
				case DROPLET -> Effective.GLOW_DROPLET;
				case RIPPLE -> Effective.GLOW_RIPPLE;
			};
		}

		world.addParticle(particle, pos.getX(), pos.getY(), pos.getZ(), velocityX, velocityY, velocityZ);
	}

	public static boolean isGlowingWater(World world, Vec3d pos) {
		return isGlowingWater(world, BlockPos.fromPosition(pos));
	}

	public static boolean isGlowingWater(World world, BlockPos pos) {
		return EffectiveConfig.glowingPlankton && Effective.isNightTime(world) && world.getBiome(pos).isRegistryKey(Biomes.WARM_OCEAN);
	}

	public static Color getGlowingWaterColor(World world, BlockPos pos) {
		return new Color(Math.min(1, world.random.nextFloat() / 5f + world.getLightLevel(LightType.BLOCK, pos) / 15f), Math.min(1, world.random.nextFloat() / 5f + world.getLightLevel(LightType.BLOCK, pos) / 15f), 1f);
	}

	public enum WaterEffectType {
		DROPLET,
		RIPPLE,
	}
}
