package org.ladysnake.effective.core.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public abstract class BlockMixin {
	@Inject(method = "randomDisplayTick", at = @At("TAIL"))
	protected void effective$injectIntoRandomDisplayTick(BlockState state, World world, BlockPos pos, Random random, CallbackInfo ci) {
	}
}
