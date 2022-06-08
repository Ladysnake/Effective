package ladysnake.effective.mixin;

import ladysnake.effective.client.Effective;
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

import java.util.Random;

@Mixin(WaterFluid.class)
public class WaterFluidMixin {
    @Inject(method = "randomDisplayTick", at = @At("HEAD"))
    protected void effective$splash(World world, BlockPos pos, FluidState state, net.minecraft.util.math.random.Random random, CallbackInfo ci) {
        if (shouldSplash(world, pos.up())) {
            Vec3d vec3d = state.getVelocity(world, pos);
            for (int i = 0; i < random.nextInt(50); i++) {
                world.addParticle(ParticleTypes.SPLASH, pos.getX() + .5 + random.nextGaussian() / 2f, pos.getY() + 1 + random.nextFloat(), pos.getZ() + .5 + random.nextGaussian() / 2f, vec3d.getX() * random.nextFloat(), random.nextFloat() / 10f, vec3d.getZ() * random.nextFloat());
            }
        }
    }

    @Unique
    private static boolean shouldSplash(World world, BlockPos pos) {
        if (Effective.config.generateCascades) {
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
}