package ladysnake.effective.client.world;

import ladysnake.effective.client.Config;
import ladysnake.effective.client.Effective;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WaterfallCloudGenerators {
    public static final List<WaterfallCloudGenerator> generators = new ArrayList<>();

    public static void tryAddGenerator(BlockRenderView world, BlockPos pos) {
        if (!Config.enableWaterfallParticles) return;

        final BlockPos abovePos = pos.up();

        if (world.getBlockState(pos).getBlock() == Blocks.WATER && world.getBlockState(pos).getFluidState().isStill() && world.getBlockState(abovePos).getBlock() == Blocks.WATER && !world.getBlockState(abovePos).getFluidState().isStill() && world.getBlockState(abovePos).getFluidState().getHeight() >= 0.77f) {
            addGenerator(MinecraftClient.getInstance().world, new BlockPos(pos.getX() + .5f, pos.getY() + .5f, pos.getZ() + .5f));
        }
    }

    public static void addGenerator(World world, BlockPos blockPos) {
        if (!generators.contains(new WaterfallCloudGenerator(world, blockPos))) {
            generators.add(new WaterfallCloudGenerator(world, blockPos));
        }
    }

    public static void removeGenerator(World world, BlockPos blockPos) {
        generators.removeIf(waterfallCloudGenerator -> waterfallCloudGenerator.world == world && waterfallCloudGenerator.blockPos == blockPos);
    }

    public static void tick() {
        if (generators.isEmpty()) return; //If the generators list is empty there is no need to do anything

        List<WaterfallCloudGenerator> generatorsToRemove = new ArrayList<>();

        List<WaterfallCloudGenerator> generatorsInDistance = generators.stream().filter(waterfallCloudGenerator -> waterfallCloudGenerator.world == MinecraftClient.getInstance().player.world && Math.sqrt(waterfallCloudGenerator.blockPos.getSquaredDistance(MinecraftClient.getInstance().player.getBlockPos())) <= MinecraftClient.getInstance().options.viewDistance * 8f).collect(Collectors.toUnmodifiableList());

        for (WaterfallCloudGenerator waterfallCloudGenerator : generatorsInDistance) {
            World world = waterfallCloudGenerator.world;
            BlockPos blockPos = waterfallCloudGenerator.blockPos;

            if (!(world.getBlockState(blockPos).getBlock() == Blocks.WATER && world.getBlockState(blockPos).getFluidState().isStill() && world.getBlockState(blockPos.add(0, 1, 0)).getBlock() == Blocks.WATER && !world.getBlockState(blockPos.add(0, 1, 0)).getFluidState().isStill() && world.getBlockState(blockPos.add(0, 1, 0)).getFluidState().getHeight() >= 0.77f)) {
                generatorsToRemove.add(waterfallCloudGenerator);
            }

            if (world.random.nextInt(1) == 0 && world.getBlockState(blockPos).getBlock() == Blocks.WATER && world.getBlockState(blockPos).getFluidState().isStill() && world.getBlockState(blockPos.add(0, 1, 0)).getBlock() == Blocks.WATER && !world.getBlockState(blockPos.add(0, 1, 0)).getFluidState().isStill() && world.getBlockState(blockPos.add(0, 1, 0)).getFluidState().getHeight() >= 0.77f) {
                double offsetX = world.random.nextGaussian() / 5f;
                double offsetZ = world.random.nextGaussian() / 5f;

                world.playSound(blockPos.getX(), blockPos.getY(), blockPos.getZ(), Effective.AMBIENCE_WATERFALL, SoundCategory.AMBIENT, 2.5f, 1.2f + world.random.nextFloat() / 10f, true);
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
