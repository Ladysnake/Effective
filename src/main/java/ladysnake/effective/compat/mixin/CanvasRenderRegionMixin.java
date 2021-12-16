package ladysnake.effective.compat.mixin;

import grondag.canvas.terrain.region.RenderRegion;
import ladysnake.effective.client.world.WaterfallCloudGenerators;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RenderRegion.class)
public class CanvasRenderRegionMixin {
    @Redirect(method = "Lgrondag/canvas/terrain/region/RenderRegion;buildTerrain(Lgrondag/canvas/apiimpl/rendercontext/CanvasTerrainRenderContext;Lgrondag/canvas/terrain/region/RegionBuildState;)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/util/math/BlockPos$Mutable;set(III)Lnet/minecraft/util/math/BlockPos$Mutable;"))
    private BlockPos.Mutable fluidRenderHook(BlockPos.Mutable mutable, int x, int y, int z) {
        final BlockPos.Mutable pos = mutable.set(x, y, z);
        final ClientWorld world = MinecraftClient.getInstance().world;
        final BlockPos above = pos.add(0, 1, 0);
        final BlockState current = world.getBlockState(pos);

        if (!current.getFluidState().isEmpty() && world.getBlockState(pos).getBlock() == Blocks.WATER && world.getBlockState(pos).getFluidState().isStill() && world.getBlockState(above).getBlock() == Blocks.WATER && !world.getBlockState(above).getFluidState().isStill() && world.getBlockState(above).getFluidState().getHeight() >= 0.77f) {
            WaterfallCloudGenerators.addGenerator(world, new BlockPos(pos.getX() + .5f, pos.getY() + .5f, pos.getZ() + .5f));
        }

        return pos;
    }
}
