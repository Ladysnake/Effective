package org.ladysnake.effective.mixin.water;

import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.ladysnake.effective.EffectiveConfig;
import org.ladysnake.effective.EffectiveUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WaterFluid.class)
public class RippleAndFlowingWaterSplashesSpawner {
	@Unique
	private static boolean shouldSplash(World world, BlockPos pos) {
		if (EffectiveConfig.flowingWaterSplashingDensity > 0) {
			FluidState fluidState = world.getFluidState(pos);
			if (!fluidState.isSource() & fluidState.getHeight() >= 0.77) {
				BlockPos.Mutable mutable = new BlockPos.Mutable();
				for (Direction direction : Direction.values()) {
					if (direction != Direction.DOWN && world.getBlockState(mutable.set(pos, direction)).isAir()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Unique
	private static boolean shouldRipple(World world, BlockPos pos) {
		if (EffectiveConfig.rainRippleDensity > 0) {
			FluidState fluidState = world.getFluidState(pos);
			return fluidState.isSource() && world.isRaining() && world.getBlockState(pos.add(0, 1, 0)).isAir();
		}
		return false;
	}

	@Inject(method = "randomDisplayTick", at = @At("HEAD"))
	protected void effective$splashAndRainRipples(World world, BlockPos pos, FluidState state, RandomGenerator random, CallbackInfo ci) {
		// flowing water splashes
		if (shouldSplash(world, pos.up())) {
			Vec3d vec3d = state.getVelocity(world, pos);
			for (int i = 0; i <= random.nextInt(EffectiveConfig.flowingWaterSplashingDensity); i++) {
				world.addParticle(ParticleTypes.WATER_SPLASH, pos.getX() + .5 + random.nextGaussian() / 2f, pos.getY() + 1 + random.nextFloat(), pos.getZ() + .5 + random.nextGaussian() / 2f, vec3d.getX() * random.nextFloat(), random.nextFloat() / 10f, vec3d.getZ() * random.nextFloat());
			}
		}

		// still water rain ripples
		if (shouldRipple(world, pos)) {
			for (int i = 0; i <= random.nextInt(EffectiveConfig.rainRippleDensity); i++) {
				if (world.getBiome(pos).value().getPrecipitationAt(pos) == Biome.Precipitation.RAIN && world.isSkyVisibleAllowingSea(pos)) {
					EffectiveUtils.spawnWaterEffect(world, Vec3d.ofCenter(pos).add(random.nextFloat() - random.nextFloat(), .39f, random.nextFloat() - random.nextFloat()), 0f, 0f, 0f, EffectiveUtils.WaterEffectType.RIPPLE);
				}
			}
		}
	}
}
