package ladysnake.effective.mixin;

import ladysnake.effective.client.Config;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import ladysnake.effective.client.world.WaterfallCloudGenerators;

import java.util.Random;

@Mixin(WaterFluid.class)
abstract class WaterFluidMixin {
    @Inject(method = "randomDisplayTick", at = @At("RETURN"))
    private void illuminations$randomDisplayTick(World world, BlockPos pos, FluidState state, Random random, CallbackInfo ci) {
        if (!Config.enableWaterfallParticles) return;

        if (!state.isStill() && WaterfallCloudGenerators.isFlowingAndFalling(world.getBlockState(pos.up()).getFluidState())) {
            Vec3d vec3d = state.getVelocity(world, pos);
            for (int i = 0; i < random.nextInt(50); i++) {
                world.addParticle(ParticleTypes.SPLASH,
                        pos.getX() + 0.5 + random.nextGaussian() / 2f,
                        pos.getY() + 1 + random.nextFloat(),
                        pos.getZ() + 0.5 + random.nextGaussian() / 2f,
                        vec3d.getX() * random.nextFloat(), random.nextFloat() / 10f,
                        vec3d.getZ() * random.nextFloat()
                );
            }
        }
        WaterfallCloudGenerators.tryAddGenerator(pos, world);
    }
}