package ladysnake.effective.client.world;

import ladysnake.effective.client.Config;
import ladysnake.effective.client.Effective;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.World;
import org.spongepowered.include.com.google.common.base.Objects;

import java.util.HashSet;
import java.util.Set;

public class WaterfallCloudGenerators {
    public static final Set<WaterfallCloudGenerator> generators = new HashSet<>();

    public static void tryAddGenerator(BlockRenderView world, BlockPos pos) {
        if (!Config.enableWaterfallParticles) return;

        if (isInRange(pos) && isPositionValid(world, pos)) {
            generators.add(new WaterfallCloudGenerator(MinecraftClient.getInstance().world, pos));
        }
    }

    public static void removeGenerator(World world, BlockPos blockPos) {
        generators.removeIf(waterfallCloudGenerator -> waterfallCloudGenerator.world == world && waterfallCloudGenerator.blockPos == blockPos);
    }

    public static void tick() {
        for (WaterfallCloudGenerator generator : generators) {
            generator.tick();
        }

        generators.removeIf(waterfallCloudGenerator -> waterfallCloudGenerator.isOutofRange() || !isPositionValid(waterfallCloudGenerator.world, waterfallCloudGenerator.blockPos));
    }

    private static boolean isInRange(BlockPos pos) {
        MinecraftClient client = MinecraftClient.getInstance();
        return client.player != null && Math.sqrt(pos.getSquaredDistance(client.player.getBlockPos())) < client.options.viewDistance * 16f;
    }

    private static boolean isPositionValid(BlockRenderView world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        BlockState above = world.getBlockState(pos.up());

        return state.isOf(Blocks.WATER) && state.getFluidState().isStill()
                && above.isOf(Blocks.WATER) && !above.getFluidState().isStill()
                && above.getFluidState().contains(FlowableFluid.FALLING)
                && above.getFluidState().get(FlowableFluid.FALLING)
                && above.getFluidState().getHeight() >= 0.77f;
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

        public void tick() {
            if (world.isPlayerInRange(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 300f)) {
                if (world.getTime() % 11 == 0) {
                    world.playSound(blockPos.getX(), blockPos.getY(), blockPos.getZ(),
                            Effective.AMBIENCE_WATERFALL, SoundCategory.AMBIENT,
                            300f,
                            1f + world.random.nextFloat() / 10f, false);
                }

                for (int i = 0; i < 1; i++) {
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
            }
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(blockPos, world.getDimension());
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof WaterfallCloudGenerator
                    && Objects.equal(((WaterfallCloudGenerator) other).blockPos, blockPos)
                    && Objects.equal(((WaterfallCloudGenerator) other).world, world);
        }
    }
}
