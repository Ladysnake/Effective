package org.ladysnake.effective.mixin.water;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.block.FluidRenderer;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.ladysnake.effective.world.WaterfallCloudGenerators;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FluidRenderer.class)
public class WaterfallGeneratorAdder {
	@Inject(method = "render", at = @At("HEAD"))
	public void effective$generateWaterfall(BlockRenderView world, BlockPos pos, VertexConsumer vertexConsumer, BlockState blockState, FluidState fluidState, CallbackInfo ci) {
		WaterfallCloudGenerators.addGenerator(fluidState, pos.toImmutable());
	}
}
