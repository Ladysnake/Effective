package org.ladysnake.effective.core.mixin.choruspetals;

import net.minecraft.block.BlockState;
import net.minecraft.block.ChorusFlowerBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.ladysnake.effective.core.Effective;
import org.ladysnake.effective.core.EffectiveConfig;
import org.ladysnake.effective.core.index.EffectiveParticles;
import org.ladysnake.effective.core.mixin.BlockMixin;
import org.ladysnake.effective.core.utils.EffectiveUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChorusFlowerBlock.class)
public abstract class ChorusPetalSpawner extends BlockMixin {
	@Override
	protected void effective$randomDisplayTick(BlockState state, World world, BlockPos pos, Random random, CallbackInfo ci) {
		for (int i = 0; i < (6 - state.get(ChorusFlowerBlock.AGE)) * EffectiveConfig.chorusPetalDensity; i++) {
			world.addParticle(EffectiveParticles.CHORUS_PETAL, true, pos.getX() + 0.5 + EffectiveUtils.getRandomFloatOrNegative(random) * 5, pos.getY() + 0.5 + EffectiveUtils.getRandomFloatOrNegative(random) * 5, pos.getZ() + 0.5 + EffectiveUtils.getRandomFloatOrNegative(random) * 5, 0f, 0f, 0f);
		}
	}
}
