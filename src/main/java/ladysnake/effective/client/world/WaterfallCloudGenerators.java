package ladysnake.effective.client.world;

import ladysnake.effective.client.Config;
import ladysnake.effective.client.Effective;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.sound.SoundCategory;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;

import org.spongepowered.include.com.google.common.base.Objects;

public class WaterfallCloudGenerators {
    public static final Set<WaterfallCloudGenerator> generators = new HashSet<>();

    public static void tryAddGenerator(BlockPos pos, BlockRenderView world) {
        if (isInRange(pos) && isPositionValid(world, pos)) {
            generators.add(new WaterfallCloudGenerator(MinecraftClient.getInstance().world, pos));
        }
    }

    public static void removeGenerator(World world, BlockPos blockPos) {
        generators.removeIf(waterfallCloudGenerator -> waterfallCloudGenerator.world == world && waterfallCloudGenerator.blockPos == blockPos);
    }

    public static void tick() {
        generators.removeIf(WaterfallCloudGenerator::tick);
    }

    private static boolean isInRange(BlockPos pos) {
        MinecraftClient client =  MinecraftClient.getInstance();
        return client.player != null && Math.sqrt(pos.getSquaredDistance(client.player.getBlockPos())) < client.options.viewDistance * 16f;
    }

    private static boolean isPositionValid(BlockRenderView world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        BlockState above = world.getBlockState(pos.up());

        return state.isOf(Blocks.WATER)
            && state.getFluidState().isStill()
            && isFlowingAndFalling(above.getFluidState());
    }

    public static boolean isFlowingAndFalling(FluidState state) {
        return state.isIn(FluidTags.WATER)
                && !state.isStill()
                && state.getHeight() >= 0.77f
                && state.contains(FlowableFluid.FALLING)
                && state.get(FlowableFluid.FALLING);
    }

    public static final class WaterfallCloudGenerator {
        private final World world;
        private final BlockPos blockPos;

        public WaterfallCloudGenerator(World world, BlockPos blockPos) {
            this.world = world;
            this.blockPos = new BlockPos(blockPos);
        }

        public boolean isOutofRange() {
            return !isInRange(blockPos) || world != MinecraftClient.getInstance().player.world;
        }

        public boolean tick() {
            if (Config.enableWaterfallParticles) {
                return true;
            }

            if (isOutofRange() || !isPositionValid(world, blockPos)) {
                return true;
            }

            if (world.random.nextInt(1) != 0) {
                return false;
            }

            if (world.getTime() % 11 == 0) {
                world.playSound(blockPos.getX(), blockPos.getY(), blockPos.getZ(),
                        Effective.AMBIENCE_WATERFALL, SoundCategory.AMBIENT,
                        1.8f,
                        1f + world.random.nextFloat() / 10f, true);
            }

            for (int i = 0; i < 5; i++) {
                double offsetX = world.random.nextGaussian() / 5f;
                double offsetZ = world.random.nextGaussian() / 5f;

                world.addParticle(Effective.WATERFALL_CLOUD,
                        blockPos.getX() + .5 + offsetX,
                        blockPos.getY() + 1 + world.random.nextFloat(),
                        blockPos.getZ() + .5 + offsetZ,
                        world.random.nextFloat() / 5f * Math.signum(offsetX),
                        world.random.nextFloat() / 5f,
                        world.random.nextFloat() / 5f * Math.signum(offsetZ)
                );
            }

            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(blockPos, world.getDimension());
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof WaterfallCloudGenerator
                    && Objects.equal(((WaterfallCloudGenerator)other).blockPos, blockPos)
                    && Objects.equal(((WaterfallCloudGenerator)other).world, world);
        }
    }
}
