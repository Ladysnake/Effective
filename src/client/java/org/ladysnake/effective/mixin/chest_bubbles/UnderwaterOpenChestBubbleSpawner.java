package org.ladysnake.effective.mixin.chest_bubbles;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.LidOpenable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.ladysnake.effective.EffectiveConfig;
import org.ladysnake.effective.utils.EffectiveUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChestBlockEntity.class)
public class UnderwaterOpenChestBubbleSpawner<T extends BlockEntity & LidOpenable> {
	@Inject(method = "clientTick", at = @At("TAIL"))
	private static void clientTick(World world, BlockPos pos, BlockState state, ChestBlockEntity blockEntity, CallbackInfo ci) {
		boolean bl = world != null;

		if (EffectiveConfig.underwaterOpenChestBubbles && bl && world.random.nextInt(2) == 0) {
			EffectiveUtils.doUnderwaterChestLogic(world, blockEntity);
		}
	}
}
