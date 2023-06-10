package ladysnake.effective.client;

import ladysnake.effective.client.particle.contracts.SplashParticleInitialData;
import ladysnake.effective.client.particle.types.SplashParticleType;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biomes;
import org.jetbrains.annotations.Nullable;

public class EffectiveUtils {
	public static boolean isGoingFast(AllayEntity allayEntity) {
		Vec3d velocity = allayEntity.getVelocity();
		float speedRequired = 0.1f;

		return (velocity.getX() >= speedRequired || velocity.getX() <= -speedRequired)
				|| (velocity.getY() >= speedRequired || velocity.getY() <= -speedRequired)
				|| (velocity.getZ() >= speedRequired || velocity.getZ() <= -speedRequired);
	}

	// chooses between spawning a normal droplet / ripple / waterfall cloud or glow one depending on biome
	public static void spawnWaterEffect(World world, BlockPos pos, double velocityX, double velocityY, double velocityZ, WaterEffectType waterEffect) {
		DefaultParticleType particle = switch (waterEffect) {
			case DROPLET -> Effective.DROPLET;
			case RIPPLE -> Effective.RIPPLE;
			case WATERFALL_CLOUD -> Effective.WATERFALL_CLOUD;
		};
		if (EffectiveConfig.enableGlowingPlankton && Effective.isNightTime(world) && world.getBiome(pos).isRegistryKey(Biomes.WARM_OCEAN)) {
			particle = switch (waterEffect) {
				case DROPLET -> Effective.GLOW_DROPLET;
				case RIPPLE -> Effective.GLOW_RIPPLE;
				case WATERFALL_CLOUD -> Effective.GLOW_WATERFALL_CLOUD;
			};
		}

		world.addParticle(particle, pos.getX(), pos.getY(), pos.getZ(), velocityX, velocityY, velocityZ);
	}
	// chooses between spawning a normal splash or glow splash depending on biome
	public static void spawnSplash(World world, BlockPos pos, double velocityX, double velocityY, double velocityZ, @Nullable SplashParticleInitialData data) {
		SplashParticleType splash = Effective.SPLASH;
		if (EffectiveConfig.enableGlowingPlankton && Effective.isNightTime(world) && world.getBiome(pos).isRegistryKey(Biomes.WARM_OCEAN)) {
			splash = Effective.GLOW_SPLASH;
		}

		world.addParticle(splash.setData(data), pos.getX(), pos.getY(), pos.getZ(), velocityX, velocityY, velocityZ);
	}

	public static enum WaterEffectType {
		DROPLET,
		RIPPLE,
		WATERFALL_CLOUD
	}
}
