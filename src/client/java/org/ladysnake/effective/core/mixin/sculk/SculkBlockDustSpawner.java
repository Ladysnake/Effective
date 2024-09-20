package org.ladysnake.effective.core.mixin.sculk;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.ladysnake.effective.core.Effective;
import org.ladysnake.effective.core.EffectiveConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.render_types.LodestoneWorldParticleRenderType;

import java.awt.*;

@Mixin(Block.class)
public class SculkBlockDustSpawner {
	@Inject(method = "randomDisplayTick", at = @At("HEAD"))
	protected void effective$spawnSculkParticles(BlockState state, World world, BlockPos pos, Random random, CallbackInfo ci) {
		if (random.nextFloat() <= (EffectiveConfig.sculkDustDensity / 100f) && state.getBlock() == Blocks.SCULK && (world.getBlockState(pos.offset(Direction.UP, 1)).isOf(Blocks.SCULK_VEIN) || world.getBlockState(pos.offset(Direction.UP, 1)).isAir())) {
			boolean bright = random.nextInt(50) == 0;
			Color color = bright ? new Color(0x29DFEB) : new Color(0x0D1217);
			WorldParticleBuilder.create(Effective.PIXEL)
				.enableForcedSpawn()
				.setLightLevel(bright ? LightmapTextureManager.MAX_LIGHT_COORDINATE : -1)
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
				.setRenderType(bright ? LodestoneWorldParticleRenderType.ADDITIVE : ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT)
				.spawn(world, pos.getX() + .5f + random.nextGaussian() / 3f, pos.getY() + 0.975f, pos.getZ() + .5f + random.nextGaussian() / 3f);
		}
	}
}
