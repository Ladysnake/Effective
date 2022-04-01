package ladysnake.effective.client.world;

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import ladysnake.effective.client.Effective;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.ArrayList;
import java.util.List;

public class WaterfallCloudGenerators {
    public static List<BlockPos> generators = new ArrayList<>();
    public static final Object2IntMap<BlockPos> particlesToSpawn = new Object2IntArrayMap<>();
    private static volatile boolean adding = false;
    private static World lastWorld = null;

    public static void addGenerator(FluidState state, BlockPos pos) {
        if (pos != null && Effective.config.generateCascades && state.getFluid() == Fluids.FLOWING_WATER && !generators.contains(pos)) {
            adding = true;
            generators.add(new BlockPos(pos));
        }
    }

    public static void tick() {
        MinecraftClient client = MinecraftClient.getInstance();
        World world = client.world;
        if (client.isPaused() || world == null || adding) {
            if (adding) {
                adding = false;
            }
            return;
        }
        if (lastWorld != null && world != lastWorld) {
            generators.clear();
            particlesToSpawn.clear();
        }
        tickParticles(world);
        if (world.getTime() % 3 == 0) {
            for (int i = generators.size() - 1; i >= 0; i--) {
                BlockPos pos = generators.get(i);
                if (shouldCauseWaterfall(world, pos, world.getFluidState(pos))) {
                    if (world.random.nextInt(500) == 0) {
                        world.playSound(pos.getX(), pos.getY(), pos.getZ(), Effective.AMBIENCE_WATERFALL, SoundCategory.AMBIENT, 10f, 1.2f + world.random.nextFloat() / 10f, true);
                    }
                    particlesToSpawn.put(pos, 6);
                }
                else {
                    generators.remove(i);
                }
            }
        }
        lastWorld = world;
    }

    private static void tickParticles(World world) {
        for (BlockPos pos : particlesToSpawn.keySet()) {
            if (particlesToSpawn.put(pos, particlesToSpawn.getInt(pos) - 1) <= 0) {
                particlesToSpawn.removeInt(pos);
            }
            addWaterfallCloud(world, pos);
        }
    }

    private static boolean shouldCauseWaterfall(BlockView world, BlockPos pos, FluidState fluidState) {
        if (Effective.config.generateCascades && fluidState.getFluid() == Fluids.FLOWING_WATER && Math.sqrt(pos.getSquaredDistance(MinecraftClient.getInstance().player.getBlockPos())) <= MinecraftClient.getInstance().options.viewDistance * 16) {
            BlockPos.Mutable mutable = new BlockPos.Mutable();
            if (world.getFluidState(mutable.set(pos, Direction.DOWN)).getFluid() == Fluids.WATER) {
                boolean foundAir = false;
                for (int x = -1; x <= 1; x++) {
                    for (int z = -1; z <= 1; z++) {
                        if (x != 0 || z != 0) {
                            BlockState blockState = world.getBlockState(mutable.set(pos.getX() + x, pos.getY(), pos.getZ() + z));
                            if (blockState.isAir()) {
                                foundAir = true;
                                break;
                            }
                        }
                    }
                }
                if (foundAir) {
                    for (Direction direction : Direction.values()) {
                        if (direction.getAxis() != Direction.Axis.Y) {
                            if (world.getFluidState(mutable.set(pos.getX() + direction.getOffsetX(), pos.getY() - 1, pos.getZ() + direction.getOffsetZ())).getFluid() == Fluids.WATER) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public static void addWaterfallCloud(WorldAccess world, BlockPos pos) {
        double offsetX = world.getRandom().nextGaussian() / 5f;
        double offsetZ = world.getRandom().nextGaussian() / 5f;
        world.addParticle(Effective.WATERFALL_CLOUD, pos.getX() + .5 + offsetX, pos.getY() + world.getRandom().nextFloat(), pos.getZ() + .5 + offsetZ, world.getRandom().nextFloat() / 5f * Math.signum(offsetX), world.getRandom().nextFloat() / 5f, world.getRandom().nextFloat() / 5f * Math.signum(offsetZ));
    }
}
