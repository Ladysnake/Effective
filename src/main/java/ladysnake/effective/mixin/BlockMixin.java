package ladysnake.effective.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(Block.class)
public abstract class BlockMixin {
    @Shadow
    public abstract BlockState getDefaultState();

    @Inject(method = "randomDisplayTick", at = @At("RETURN"))
    protected void illuminations$randomDisplayTick(BlockState state, World world, BlockPos pos, Random random, CallbackInfo ci) {
        if (state.getBlock() == Blocks.WATER && !world.getBlockState(pos.add(0, 1, 0)).getFluidState().isStill() && world.getBlockState(pos.add(0, 1, 0)).getBlock() == Blocks.WATER && !world.getBlockState(pos.add(0, 1, 0)).getFluidState().isStill() && world.getBlockState(pos.add(0, 1, 0)).getFluidState().getHeight() >= 0.77f) {
            Vec3d vec3d = state.getFluidState().getVelocity(world, pos);
            for (int i = 0; i < random.nextInt(50); i++) {
                world.addParticle(ParticleTypes.SPLASH, pos.getX() + .5 + random.nextGaussian() / 2f, pos.getY() + 1 + random.nextFloat(), pos.getZ() + .5 + random.nextGaussian() / 2f, vec3d.getX() * random.nextFloat(), random.nextFloat() / 10f, vec3d.getZ() * random.nextFloat());
            }
        }
    }
}