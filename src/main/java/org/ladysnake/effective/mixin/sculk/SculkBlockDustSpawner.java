package org.ladysnake.effective.mixin.sculk;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.World;
import org.ladysnake.effective.Effective;
import org.ladysnake.effective.EffectiveConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.lodestar.lodestone.systems.rendering.particle.Easing;
import team.lodestar.lodestone.systems.rendering.particle.LodestoneWorldParticleTextureSheet;
import team.lodestar.lodestone.systems.rendering.particle.WorldParticleBuilder;
import team.lodestar.lodestone.systems.rendering.particle.data.ColorParticleData;
import team.lodestar.lodestone.systems.rendering.particle.data.GenericParticleData;

import java.awt.*;

@Mixin(Block.class)
public class SculkBlockDustSpawner {
	@Inject(method = "randomDisplayTick", at = @At("HEAD"))
	protected void effective$spawnSculkParticles(BlockState state, World world, BlockPos pos, RandomGenerator random, CallbackInfo ci) {
		if (random.nextFloat() <= (EffectiveConfig.sculkDustDensity / 100f) && state.getBlock() == Blocks.SCULK && (world.getBlockState(pos.offset(Direction.UP, 1)).isOf(Blocks.SCULK_VEIN) || world.getBlockState(pos.offset(Direction.UP, 1)).isAir())) {
			boolean bright = random.nextInt(50) == 0;
			Color color = bright ? new Color(0x29DFEB) : new Color(0x0D1217);
			WorldParticleBuilder.create(Effective.PIXEL)
				.setScaleData(GenericParticleData.create(0.02f).build())
				.setTransparencyData(
					GenericParticleData.create(1f, 1f, 0f)
						.setEasing(Easing.SINE_OUT)
						.build()
				)
				.setColorData(ColorParticleData.create(color, color).build())
				.enableNoClip()
				.setLifetime(100 + random.nextInt(50))
				.setMotion(0f, 0.01f + random.nextFloat() * .01f, 0f)
				.setRenderType(bright ? LodestoneWorldParticleTextureSheet.ADDITIVE : ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT)
				.spawn(world, pos.getX() + .5f + random.nextGaussian() / 3f, pos.getY() + 0.975f, pos.getZ() + .5f + random.nextGaussian() / 3f);
		}
	}
}
