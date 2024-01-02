package org.ladysnake.effective.mixin.choruspetals;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChorusFlowerBlock;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.random.RandomGenerator;
import org.ladysnake.effective.Effective;
import org.ladysnake.effective.EffectiveConfig;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticleManager.class)
public abstract class BrokenChorusFlowerPetalSpawner {
	@Shadow
	protected ClientWorld world;
	@Shadow
	@Final
	private RandomGenerator random;

	@Shadow
	public abstract Particle addParticle(ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ);

	@Inject(method = "addBlockBreakParticles", at = @At(value = "RETURN"))
	public void addBlockBreakParticles(BlockPos pos, BlockState state, CallbackInfo ci) {
		if (state.getBlock() == Blocks.CHORUS_FLOWER) {
			for (int i = 0; i < (6 - state.get(ChorusFlowerBlock.AGE)) * (EffectiveConfig.chorusPetalDensity*10f); i++) {
				this.addParticle(Effective.CHORUS_PETAL, (double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5, random.nextGaussian() / 10f, random.nextGaussian() / 10f, random.nextGaussian() / 10f);
			}
		}
	}
}
