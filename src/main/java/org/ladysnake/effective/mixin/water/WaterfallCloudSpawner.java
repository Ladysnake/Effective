package org.ladysnake.effective.mixin.water;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.ladysnake.effective.EffectiveConfig;
import org.ladysnake.effective.world.Waterfall;
import org.ladysnake.effective.world.WaterfallCloudGenerators;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;

@Mixin(World.class)
public abstract class WaterfallCloudSpawner {
	@Shadow
	public abstract FluidState getFluidState(BlockPos pos);

	@Inject(method = "setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;II)Z", at = @At("RETURN"))
	private void effective$flowingWaterCascade(BlockPos pos, BlockState state, int flags, int maxUpdateDepth, CallbackInfoReturnable<Boolean> cir) {
		if (cir.getReturnValueZ() && EffectiveConfig.shouldFlowingWaterSpawnParticlesOnFirstTick && getFluidState(pos).getFluid() == Fluids.FLOWING_WATER) {
			WaterfallCloudGenerators.addWaterfallCloud(World.class.cast(this), new Waterfall(pos, getFluidState(pos).getHeight() / 2f, true, new Color(0xFFFFFF)));
		}
	}
}
