package ladysnake.effective.mixin;

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
        WaterfallCloudGenerators.tryAddGenerator(world, pos);
    }
}