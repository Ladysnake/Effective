package ladysnake.effective;

import ladysnake.effective.particle.contracts.SplashParticleInitialData;
import ladysnake.effective.particle.types.SplashParticleType;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biomes;
import org.jetbrains.annotations.Nullable;
import org.joml.Math;
import org.joml.Quaternionf;

import java.awt.*;

public class EffectiveUtils {
	public static boolean isGoingFast(AllayEntity allayEntity) {
		Vec3d velocity = allayEntity.getVelocity();
		float speedRequired = 0.1f;

		return (velocity.getX() >= speedRequired || velocity.getX() <= -speedRequired)
			|| (velocity.getY() >= speedRequired || velocity.getY() <= -speedRequired)
			|| (velocity.getZ() >= speedRequired || velocity.getZ() <= -speedRequired);
	}

	public static Quaternionf creatQuat(float x, float y, float z, boolean degrees) {
		if (degrees) {
			x *= (float) (java.lang.Math.PI / 180.0);
			y *= (float) (java.lang.Math.PI / 180.0);
			z *= (float) (java.lang.Math.PI / 180.0);
		}
		float f = Math.sin(0.5F * x);
		float g = Math.cos(0.5F * x);
		float h = Math.sin(0.5F * y);
		float i = Math.cos(0.5F * y);
		float j = Math.sin(0.5F * z);
		float k = Math.cos(0.5F * z);
		x = f * i * k + g * h * j;
		y = g * h * k - f * i * j;
		z = f * h * k + g * i * j;
		float w = g * i * k - f * h * j;
		return new Quaternionf(x, y, z, w);
	}

	public static Quaternionf hamiltonProduct(Quaternionf a, Quaternionf b) {
		float quatf = a.x();
		float quatg = a.y();
		float quath = a.z();
		float quati = a.w();
		float quatj = b.x();
		float quatk = b.y();
		float quatl = b.z();
		float quatm = b.w();
		float quatx = quati * quatj + quatf * quatm + quatg * quatl - quath * quatk;
		float quaty = quati * quatk - quatf * quatl + quatg * quatm + quath * quatj;
		float quatz = quati * quatl + quatf * quatk - quatg * quatj + quath * quatm;
		float quatw = quati * quatm - quatf * quatj - quatg * quatk - quath * quatl;
		return new Quaternionf(quatx, quaty, quatz, quatw);
	}

	// chooses between spawning a normal droplet / ripple / waterfall cloud or glow one depending on biome
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

	private static Vec3i toVec3i(Vec3d vec) {
		return new Vec3i((int) vec.getX(), (int) vec.getY(), (int) vec.getZ());
	}

	public static boolean isGlowingWater(World world, Vec3d pos) {
		return EffectiveConfig.glowingPlankton && Effective.isNightTime(world) && world.getBiome(new BlockPos(toVec3i(pos))).isRegistryKey(Biomes.WARM_OCEAN);
	}

	public static Color getGlowingWaterColor(World world, BlockPos pos) {
		return new Color(Math.min(1, world.random.nextFloat() / 5f + world.getLightLevel(LightType.BLOCK, pos) / 15f), Math.min(1, world.random.nextFloat() / 5f + world.getLightLevel(LightType.BLOCK, pos) / 15f), 1f);
	}

	// chooses between spawning a normal splash or glow splash depending on biome
	public static void spawnSplash(World world, BlockPos pos, double velocityX, double velocityY, double velocityZ, @Nullable SplashParticleInitialData data) {
		SplashParticleType splash = Effective.SPLASH;
		if (EffectiveConfig.glowingPlankton && Effective.isNightTime(world) && world.getBiome(pos).isRegistryKey(Biomes.WARM_OCEAN)) {
			splash = Effective.GLOW_SPLASH;
		}

		world.addParticle(splash.setData(data), pos.getX() + .5f, pos.getY() + .9f, pos.getZ() + .5f, velocityX, velocityY, velocityZ);
	}

	public enum WaterEffectType {
		DROPLET,
		RIPPLE
	}
}
