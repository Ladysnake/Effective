package org.ladysnake.effective.mixin.choruspetals;

import net.minecraft.block.BlockState;
import net.minecraft.block.ChorusFlowerBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.World;
import org.ladysnake.effective.Effective;
import org.ladysnake.effective.EffectiveConfig;
import org.ladysnake.effective.mixin.RandomDisplayTickBlockMixin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChorusFlowerBlock.class)
public abstract class ChorusPetalSpawner extends RandomDisplayTickBlockMixin {
	@Override
	protected void effective$randomDisplayTick(BlockState state, World world, BlockPos pos, RandomGenerator random, CallbackInfo ci) {
		for (int i = 0; i < (6 - state.get(ChorusFlowerBlock.AGE)) * EffectiveConfig.chorusPetalDensity; i++) {
			world.addParticle(Effective.CHORUS_PETAL, true, pos.getX() + 0.5 + random.nextGaussian() * 5, pos.getY() + 0.5 + random.nextGaussian() * 5, pos.getZ() + 0.5 + random.nextGaussian() * 5, 0f, 0f, 0f);
		}
	}
}
