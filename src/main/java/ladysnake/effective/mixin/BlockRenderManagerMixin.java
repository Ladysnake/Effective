package ladysnake.effective.mixin;

import ladysnake.effective.client.Config;
import ladysnake.effective.client.world.WaterfallCloudGenerators;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockRenderManager.class)
public class BlockRenderManagerMixin {
    @Inject(method = "renderFluid", at = @At("TAIL"))
    public void renderFluid(BlockPos pos, BlockRenderView world, VertexConsumer vertexConsumer, FluidState state, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (Config.enableWaterfallParticles && world.getBlockState(pos).getBlock() == Blocks.WATER && world.getBlockState(pos).getFluidState().isStill() && world.getBlockState(pos.add(0, 1, 0)).getBlock() == Blocks.WATER && !world.getBlockState(pos.add(0, 1, 0)).getFluidState().isStill() && world.getBlockState(pos.add(0, 1, 0)).getFluidState().getHeight() >= 0.77f) {
            WaterfallCloudGenerators.addGenerator(MinecraftClient.getInstance().world, new BlockPos(pos.getX() + .5f, pos.getY() + .5f, pos.getZ() + .5f));
        }
    }
}