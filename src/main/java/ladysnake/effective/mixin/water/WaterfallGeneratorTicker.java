package ladysnake.effective.mixin.water;

import ladysnake.effective.EffectiveConfig;
import ladysnake.effective.world.WaterfallCloudGenerators;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashSet;
import java.util.Set;

@Mixin(AbstractBlock.class)
public class WaterfallGeneratorTicker {
	@Inject(method = "getStateForNeighborUpdate", at = @At("HEAD"))
	protected void effective$forceParticles(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos, CallbackInfoReturnable<BlockState> cir) {
		if (neighborState.getBlock() == Blocks.LAPIS_BLOCK && state.getBlock() == Blocks.WATER) {
			long chance = EffectiveConfig.lapisBlockUpdateParticleChance;
			if (chance > 0) {
				gatherWater(new HashSet<>(), world, new BlockPos.Mutable().set(pos)).forEach(waterPos -> {
					if (world.getRandom().nextInt(100) < chance) {
						WaterfallCloudGenerators.scheduleParticleTick(waterPos, 1);
					}
				});
			}
		}
	}

	@Unique
	private Set<BlockPos> gatherWater(Set<BlockPos> flowingWater, WorldAccess world, BlockPos.Mutable pos) {
		if (flowingWater.size() < 1024) {
			int originalX = pos.getX(), originalY = pos.getY(), originalZ = pos.getZ();
			for (Direction direction : Direction.values()) {
				FluidState state = world.getFluidState(pos.set(originalX + direction.getOffsetX(), originalY + direction.getOffsetY(), originalZ + direction.getOffsetZ()));
				if (!flowingWater.contains(pos) && state.getFluid() == Fluids.FLOWING_WATER) {
					flowingWater.add(pos.toImmutable());
					gatherWater(flowingWater, world, pos);
				}
			}
		}
		return flowingWater;
	}
}
