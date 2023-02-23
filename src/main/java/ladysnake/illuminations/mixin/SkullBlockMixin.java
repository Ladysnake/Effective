package ladysnake.illuminations.mixin;

import ladysnake.illuminations.client.Illuminations;
import ladysnake.illuminations.client.config.Config;
import ladysnake.illuminations.client.enums.HalloweenFeatures;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SkullBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.time.LocalDate;
import java.time.Month;

@Mixin(SkullBlock.class)
public abstract class SkullBlockMixin extends SeaLanternBlockMixin {
	@Override
	protected void illuminations$randomDisplayTick(BlockState state, World world, BlockPos pos, RandomGenerator random, CallbackInfo ci) {
		if ((state.getBlock() == Blocks.SKELETON_SKULL || state.getBlock() == Blocks.SKELETON_WALL_SKULL) && Illuminations.isNightTime(world) && random.nextInt(100) == 0 && ((Config.getHalloweenFeatures() == HalloweenFeatures.ENABLE && LocalDate.now().getMonth() == Month.OCTOBER) || Config.getHalloweenFeatures() == HalloweenFeatures.ALWAYS)) {
			world.addParticle(Illuminations.POLTERGEIST, true, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0f, 0f, 0f);
		}
	}
}
