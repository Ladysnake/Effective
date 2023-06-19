package ladysnake.effective.mixin.sculk;

import com.sammy.lodestone.systems.rendering.particle.Easing;
import com.sammy.lodestone.systems.rendering.particle.ParticleBuilders;
import com.sammy.lodestone.systems.rendering.particle.ParticleTextureSheets;
import ladysnake.effective.Effective;
import ladysnake.effective.EffectiveConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(Block.class)
public class SculkBlockDustSpawner {
	@Inject(method = "randomDisplayTick", at = @At("HEAD"))
	protected void effective$spawnSculkParticles(BlockState state, World world, BlockPos pos, RandomGenerator random, CallbackInfo ci) {
		if (random.nextFloat() <= (EffectiveConfig.sculkDustDensity / 100f) && state.getBlock() == Blocks.SCULK && (world.getBlockState(pos.offset(Direction.UP, 1)).isOf(Blocks.SCULK_VEIN) || world.getBlockState(pos.offset(Direction.UP, 1)).isAir())) {
			boolean bright = random.nextInt(50) == 0;
			Color color = bright ? new Color(0x29DFEB) : new Color(0x0D1217);
			ParticleBuilders.create(Effective.PIXEL)
				.setScale(0.02f)
				.setAlpha(1f, 1f, 0f)
				.setAlphaEasing(Easing.SINE_OUT)
				.setColor(color, color)
				.enableNoClip()
				.setLifetime(100 + random.nextInt(50))
				.setMotion(0f, 0.01f + random.nextFloat() * .01f, 0f)
				.overrideRenderType(bright ? ParticleTextureSheets.ADDITIVE : ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT)
				.spawn(world, pos.getX() + .5f + random.nextGaussian() / 3f, pos.getY() + 0.975f, pos.getZ() + .5f + random.nextGaussian() / 3f);
		}
	}
}
