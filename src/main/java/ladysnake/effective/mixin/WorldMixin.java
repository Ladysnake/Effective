package ladysnake.effective.mixin;

import ladysnake.effective.client.EffectiveConfig;
import ladysnake.effective.client.world.WaterfallCloudGenerators;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public abstract class WorldMixin {
    @Shadow
    public abstract FluidState getFluidState(BlockPos pos);

    @Inject(method = "setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;II)Z", at = @At("RETURN"))
    private void effective$flowingWaterCascade(BlockPos pos, BlockState state, int flags, int maxUpdateDepth, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValueZ() && EffectiveConfig.shouldFlowingWaterSpawnParticlesOnFirstTick && getFluidState(pos).getFluid() == Fluids.FLOWING_WATER) {
            WaterfallCloudGenerators.addWaterfallCloud(World.class.cast(this), pos);
        }
    }
}
