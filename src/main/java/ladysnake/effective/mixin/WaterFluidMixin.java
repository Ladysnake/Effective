package ladysnake.effective.mixin;

import ladysnake.effective.client.Effective;
import ladysnake.effective.client.EffectiveConfig;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WaterFluid.class)
public class WaterFluidMixin {
    @Unique
    private static boolean shouldSplash(World world, BlockPos pos) {
        if (EffectiveConfig.flowingWaterSplashingDesity > 0) {
            FluidState fluidState = world.getFluidState(pos);
            if (!fluidState.isStill() & fluidState.getHeight() >= 0.77) {
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
            return fluidState.isStill() && world.isRaining() && world.getBlockState(pos.add(0, 1, 0)).isAir();
        }
        return false;
    }

    @Inject(method = "randomDisplayTick", at = @At("HEAD"))
    protected void effective$splashAndRainRipples(World world, BlockPos pos, FluidState state, net.minecraft.util.math.random.Random random, CallbackInfo ci) {
        // flowing water splashes
        if (shouldSplash(world, pos.up())) {
            Vec3d vec3d = state.getVelocity(world, pos);
            for (int i = 0; i <= random.nextInt(EffectiveConfig.flowingWaterSplashingDesity); i++) {
                world.addParticle(ParticleTypes.SPLASH, pos.getX() + .5 + random.nextGaussian() / 2f, pos.getY() + 1 + random.nextFloat(), pos.getZ() + .5 + random.nextGaussian() / 2f, vec3d.getX() * random.nextFloat(), random.nextFloat() / 10f, vec3d.getZ() * random.nextFloat());
            }
        }

        // still water rain ripples
        if (shouldRipple(world, pos)) {
            for (int i = 0; i <= random.nextInt(EffectiveConfig.rainRippleDensity); i++) {
                world.addParticle(Effective.RIPPLE, pos.getX() + .5 + random.nextGaussian() / 2f, pos.getY() + 0.9f, pos.getZ() + .5 + random.nextGaussian() / 2f, 0f, 0f, 0f);
            }
        }
    }
}