package ladysnake.effective.client.world;

import ladysnake.effective.client.Effective;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WaterfallCloudGenerators {
    public static final List<WaterfallCloudGenerator> generators = new ArrayList<>();

    public static void addGenerator(World world, BlockPos blockPos) {
        if (!generators.contains(new WaterfallCloudGenerator(world, blockPos))) {
            generators.add(new WaterfallCloudGenerator(world, blockPos));
        }
    }

    public static void removeGenerator(World world, BlockPos blockPos) {
        generators.removeIf(waterfallCloudGenerator -> waterfallCloudGenerator.world == world && waterfallCloudGenerator.blockPos == blockPos);
    }

    public static void tick() {
        List<WaterfallCloudGenerator> generatorsToRemove = new ArrayList<>();

        List<WaterfallCloudGenerator> generatorsInDistance = generators.stream().filter(waterfallCloudGenerator -> waterfallCloudGenerator.world == MinecraftClient.getInstance().player.world && Math.sqrt(waterfallCloudGenerator.blockPos.getSquaredDistance(MinecraftClient.getInstance().player.getBlockPos())) <= MinecraftClient.getInstance().options.viewDistance * 8f).collect(Collectors.toUnmodifiableList());

        for (WaterfallCloudGenerator waterfallCloudGenerator : generatorsInDistance) {
            World world = waterfallCloudGenerator.world;
            BlockPos blockPos = waterfallCloudGenerator.blockPos;

            if (!(world.getBlockState(blockPos).getBlock() == Blocks.WATER && world.getBlockState(blockPos).getFluidState().isStill() && world.getBlockState(blockPos.add(0, 1, 0)).getBlock() == Blocks.WATER && !world.getBlockState(blockPos.add(0, 1, 0)).getFluidState().isStill() && world.getBlockState(blockPos.add(0, 1, 0)).getFluidState().getHeight() >= 0.77f)) {
                generatorsToRemove.add(waterfallCloudGenerator);
            }

            if (world.getBlockState(blockPos).getBlock() == Blocks.WATER && world.getBlockState(blockPos).getFluidState().isStill() && world.getBlockState(blockPos.add(0, 1, 0)).getBlock() == Blocks.WATER && !world.getBlockState(blockPos.add(0, 1, 0)).getFluidState().isStill() && world.getBlockState(blockPos.add(0, 1, 0)).getFluidState().getHeight() >= 0.77f) {
                double offsetX = world.random.nextGaussian() / 5f;
                double offsetZ = world.random.nextGaussian() / 5f;

                if (world.random.nextInt(5) == 0) {
                    Entity camera = MinecraftClient.getInstance().getCameraEntity();
                    double distance = Math.sqrt(camera.getBlockPos().getSquaredDistance(blockPos));
                    if (distance <= 80) {
                        world.playSound(blockPos.getX(), blockPos.getY(), blockPos.getZ(), Effective.AMBIENCE_WATERFALL, SoundCategory.AMBIENT, (float) MathHelper.lerp(distance / 80F, .05, 6), 1.2f + world.random.nextFloat() / 10f, true);
                    }
                }

                world.addParticle(Effective.WATERFALL_CLOUD, blockPos.getX() + .5 + offsetX, blockPos.getY() + 1 + world.random.nextFloat(), blockPos.getZ() + .5 + offsetZ, world.random.nextFloat() / 5f * Math.signum(offsetX), world.random.nextFloat() / 5f, world.random.nextFloat() / 5f * Math.signum(offsetZ));
            }
        }

        generators.removeAll(generatorsToRemove);
        generators.removeIf(waterfallCloudGenerator -> waterfallCloudGenerator.world != MinecraftClient.getInstance().player.world || Math.sqrt(waterfallCloudGenerator.blockPos.getSquaredDistance(MinecraftClient.getInstance().player.getBlockPos())) >= MinecraftClient.getInstance().options.viewDistance * 16f);
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
