package ladysnake.effective.client.world;

import ladysnake.effective.client.Config;
import ladysnake.effective.client.Effective;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.fluid.FlowableFluid;
//import net.minecraft.fluid.Fluids;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.World;
import org.spongepowered.include.com.google.common.base.Objects;

//import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import static ladysnake.effective.client.Config.*;

public class WaterfallCloudGenerators {
    public static final Set<WaterfallCloudGenerator> generators = new CopyOnWriteArraySet<>();

    public static void tryAddGenerator(BlockRenderView world, BlockPos pos) {
        if (!Config.enableWaterfallParticles) return;

        if (isInRange(pos) && (isPositionValid(world, pos) == true)) {
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

        generators.removeIf(waterfallCloudGenerator -> waterfallCloudGenerator.isOutofRange() || isPositionValid(waterfallCloudGenerator.world, waterfallCloudGenerator.blockPos) == false);
    }

    private static boolean isInRange(BlockPos pos) {
        MinecraftClient client = MinecraftClient.getInstance();
        return client.player != null && Math.sqrt(pos.getSquaredDistance(client.player.getBlockPos())) < client.options.viewDistance * 16f;
    }

    public static boolean isPositionValid(BlockRenderView world, BlockPos pos) {
        final boolean hasAir = WaterfallCloudGenerators.hasAirNeighbors(world, pos);
        final int tallWater = (int) waterfallHeight;
        BlockState state = world.getBlockState(pos);
        BlockState above = world.getBlockState(pos.up());
        final FluidState aboveFluidState = above.getFluidState();

        if (state.getFluidState().isEqualAndStill(Fluids.WATER) && hasAir) {
            if (above.isOf(Blocks.WATER) && !(aboveFluidState.isStill())) { 
                if (aboveFluidState.get(FlowableFluid.FALLING) && aboveFluidState.getHeight() >= 0.77F) {
                    for (int i = -5; i <= 5; i++) {
                        if (world.getBlockState(pos.add(i, tallWater, i)).isOf(Blocks.WATER) && world.getBlockState(pos.add(i, tallWater, i)).getFluidState().get(FlowableFluid.FALLING)) {
                            return true;
                        }
                    }
                }
            }
        }
        if (state.isOpaqueFullCube(world, pos) && above.isOf(Blocks.WATER) && !(aboveFluidState.isStill()) && hasAir) {
            if (aboveFluidState.get(FlowableFluid.FALLING) && aboveFluidState.getHeight() >= 0.77F) { 
                for (int i = -5; i <= 5; i++) {
                    if (world.getBlockState(pos.add(i, tallWater, i)).isOf(Blocks.WATER) && world.getBlockState(pos.add(i, tallWater, i)).getFluidState().get(FlowableFluid.FALLING)) {
                        return true;
                    }
                }
            }
        }
        if (state.getFluidState().isEqualAndStill(Fluids.WATER) && hasAir){
            for (int i = -1; i <= 1 ; i++){
                if (i ==0){continue;}
                for (int j = 1; j <=10; j++){
                    for (int k = -2; k <= 2 ; k++){
                    if (world.getBlockState(pos.add(i,j,k)).getFluidState().isEqualAndStill(Fluids.WATER) && isPositionValid(world, pos.add(i,j,i))){
                        return true;
                        }
                    if (world.getBlockState(pos.add(i,j,k)).isOf(Blocks.WATER) && !world.getBlockState(pos.add(i,j,i)).getFluidState().isStill()){
                        return true;
                        }
                    }
                }
            }
        }
        return false;
    }  

    private static boolean hasAirNeighbors(BlockRenderView world, BlockPos origin) {
        for (int i = -1; i <= 1; i++) {
            if (i == 0){continue;}
            if (world.getBlockState(origin.add(i, 1, i)).isAir()) {
                return true;
            }
        }
        return false;
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
            if (world.isPlayerInRange(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 300f) && isPositionValid(world, blockPos) == true) {
                if (world.getTime() % 11 == 0) {
                    world.playSound(blockPos.getX(), blockPos.getY(), blockPos.getZ(),
                            Effective.AMBIENCE_WATERFALL, SoundCategory.AMBIENT,
                            waterfallVolume,
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
