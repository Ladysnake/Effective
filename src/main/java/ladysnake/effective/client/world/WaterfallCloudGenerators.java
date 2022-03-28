package ladysnake.effective.client.world;

import ladysnake.effective.client.Effective;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class WaterfallCloudGenerators {
    public static final List<WaterfallCloudGenerator> generators = new ArrayList<>();

    public static void addGenerator(World world, BlockPos blockPos) {
        if (generators.stream().noneMatch(generator -> generator.world == world && generator.blockPos.equals(blockPos))) {
            generators.add(new WaterfallCloudGenerator(world, blockPos.toImmutable()));
        }
    }

    private static boolean shouldRender(WaterfallCloudGenerator generator) {
        return generator.world == MinecraftClient.getInstance().player.world && Math.sqrt(generator.blockPos.getSquaredDistance(MinecraftClient.getInstance().player.getBlockPos())) <= MinecraftClient.getInstance().options.viewDistance * 16;
    }

    public static boolean shouldCauseWaterfall(BlockRenderView world, BlockPos pos) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        boolean foundWater = false;
        for (Direction direction : Direction.values()) {
            if (direction.getAxis() != Direction.Axis.Y) {
                FluidState fluidState = world.getFluidState(mutable.set(pos, direction));
                if (fluidState.getFluid() == Fluids.WATER) {
                    foundWater = true;
                }
            }
        }
        if (foundWater) {
            FluidState fluidState = world.getFluidState(mutable.set(pos.getX(), pos.getY() + 1, pos.getZ()));
            if (fluidState.getFluid() == Fluids.FLOWING_WATER) {
                int[] toCheck = {-1, 1};
                for (int x : toCheck) {
                    for (int z : toCheck) {
                        BlockState blockState = world.getBlockState(mutable.set(pos.getX() + x, pos.getY() + 1, pos.getZ() + z));
                        if (blockState.isAir()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static void tick() {
        for (int i = generators.size() - 1; i >= 0; i--) {
            WaterfallCloudGenerator generator = generators.get(i);
            if (!shouldRender(generator)) {
                generators.remove(i);
                continue;
            }
            World world = generator.world;
            BlockPos blockPos = generator.blockPos;

            if (shouldCauseWaterfall(world, blockPos)) {
                double offsetX = world.random.nextGaussian() / 5f;
                double offsetZ = world.random.nextGaussian() / 5f;

                if (world.random.nextInt(5) == 0) {
                    world.playSound(blockPos.getX(), blockPos.getY(), blockPos.getZ(), Effective.AMBIENCE_WATERFALL, SoundCategory.AMBIENT, 10f, 1.2f + world.random.nextFloat() / 10f, true);
                }

                if (world.random.nextInt(3) == 0) {
                    world.addParticle(Effective.WATERFALL_CLOUD, blockPos.getX() + .5 + offsetX, blockPos.getY() + 1 + world.random.nextFloat(), blockPos.getZ() + .5 + offsetZ, world.random.nextFloat() / 5f * Math.signum(offsetX), world.random.nextFloat() / 5f, world.random.nextFloat() / 5f * Math.signum(offsetZ));
                }
            }
            else {
                generators.remove(i);
            }
        }
    }

    public static class WaterfallCloudGenerator {
        public World world;
        public BlockPos blockPos;

        public WaterfallCloudGenerator(World world, BlockPos blockPos) {
            this.world = world;
            this.blockPos = blockPos;
        }
    }
}
