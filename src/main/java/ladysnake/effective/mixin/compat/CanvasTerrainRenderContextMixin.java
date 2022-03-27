package ladysnake.effective.mixin.compat;

import grondag.canvas.apiimpl.rendercontext.CanvasTerrainRenderContext;
import io.vram.frex.api.model.BlockModel;
import ladysnake.effective.client.world.WaterfallCloudGenerators;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CanvasTerrainRenderContext.class)
public class CanvasTerrainRenderContextMixin {
    @Inject(method = "renderFluid", at = @At("TAIL"))
    public void renderFluid(BlockState blockState, BlockPos blockPos, boolean defaultAo, BlockModel model, CallbackInfo ci) {
        WaterfallCloudGenerators.tryAddGenerator(MinecraftClient.getInstance().world, blockPos);
    }
}
