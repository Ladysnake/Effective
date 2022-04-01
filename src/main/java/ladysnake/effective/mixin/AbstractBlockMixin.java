package ladysnake.effective.mixin;

import ladysnake.effective.client.Effective;
import ladysnake.effective.client.world.WaterfallCloudGenerators;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashSet;
import java.util.Set;

@Mixin(AbstractBlock.class)
public class AbstractBlockMixin {
    @Inject(method = "getStateForNeighborUpdate", at = @At("RETURN"))
    protected void effective$forceParticles(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos, CallbackInfoReturnable<BlockState> cir) {
        if (neighborState.getBlock() == Blocks.LAPIS_BLOCK && state.getBlock() == Blocks.WATER && Effective.config.canLapisBlocksForceWaterfallClouds) {
            gatherWater(new HashSet<>(), world, new BlockPos.Mutable().set(pos)).forEach(waterPos -> WaterfallCloudGenerators.addWaterfallCloud(world, waterPos));
        }
    }

    @Unique
    private Set<BlockPos> gatherWater(Set<BlockPos> flowingWater, WorldAccess world, BlockPos.Mutable pos) {
        if (flowingWater.size() < 256) {
            int originalX = pos.getX(), originalY = pos.getY(), originalZ = pos.getZ();
            for (Direction direction : Direction.values()) {
                FluidState state = world.getFluidState(pos.set(originalX + direction.getOffsetX(), originalY + direction.getOffsetY(), originalZ + direction.getOffsetZ()));
                if (!flowingWater.contains(pos) && state.getFluid() == Fluids.FLOWING_WATER) {
                    flowingWater.add(pos.toImmutable());
                    gatherWater(flowingWater, world, pos);
                }
            }
        }
        return flowingWater;
    }
}