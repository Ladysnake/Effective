package ladysnake.illuminations.mixin;

import ladysnake.illuminations.client.Illuminations;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public abstract class BlockMixin {
	@Shadow
	public abstract BlockState getDefaultState();

	@Inject(method = "randomDisplayTick", at = @At("RETURN"))
	protected void illuminations$randomDisplayTick(BlockState state, World world, BlockPos pos, RandomGenerator random, CallbackInfo ci) {
		if (this.getDefaultState().getBlock() == Blocks.SEA_LANTERN) {
			for (int i = 0; i < 10; i++) {
				BlockPos blockPos = new BlockPos(pos.getX() + 0.5 + random.nextGaussian() * 15, pos.getY() + 0.5 + random.nextGaussian() * 15, pos.getZ() + 0.5 + random.nextGaussian() * 15);

				if (world.getBlockState(blockPos).getBlock() == Blocks.WATER && random.nextInt(1 + world.getLightLevel(blockPos)) == 0) {
					world.addParticle(Illuminations.PRISMARINE_CRYSTAL, true, blockPos.getX(), blockPos.getY(), blockPos.getZ(), 0f, 0f, 0f);
				}
			}
		}
	}
}
