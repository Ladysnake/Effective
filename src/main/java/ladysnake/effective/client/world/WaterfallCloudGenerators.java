package ladysnake.effective.client.world;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import ladysnake.effective.client.Effective;
import ladysnake.effective.client.EffectiveConfig;
import ladysnake.effective.client.sound.WaterfallSoundInstance;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeKeys;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WaterfallCloudGenerators {
	public static final List<BlockPos> generators = new ArrayList<>();
	public static final Object2IntMap<BlockPos> particlesToSpawn = new Object2IntOpenHashMap<>();
	private static World lastWorld = null;

	public static void addGenerator(FluidState state, BlockPos pos) {
		if (pos == null || !EffectiveConfig.enableCascades || state.getFluid() != Fluids.FLOWING_WATER || generators.contains(pos)) {
			return;
		}
		if (shouldCauseWaterfall(MinecraftClient.getInstance().world, pos, state)) {
			synchronized (generators) {
				generators.add(new BlockPos(pos));
			}
		}
	}

	public static void tick() {
		MinecraftClient client = MinecraftClient.getInstance();
		World world = client.world;
		if (client.isPaused() || world == null) {
			return;
		}
		synchronized (generators) {
			if (world != lastWorld) {
				generators.clear();
				particlesToSpawn.clear();
				lastWorld = world;
			}
			tickParticles(world);
			if (world.getTime() % 3 != 0) {
				return;
			}
			generators.forEach(blockPos -> {
				if (blockPos == null) {
					return;
				}
				scheduleParticleTick(blockPos, 6);
				float distance = MathHelper.sqrt((float) client.player.getBlockPos().getSquaredDistance(blockPos));
				if (distance > EffectiveConfig.cascadeSoundDistanceBlocks || EffectiveConfig.cascadeSoundsVolumeMultiplier == 0 || EffectiveConfig.cascadeSoundDistanceBlocks == 0) {
					return;
				}
				if (world.random.nextInt(200) == 0) { // check for player visibility to avoid underground cascades being heard on the surface, but that shit don't work: && canSeeWaterfall(world, blockPos, MinecraftClient.getInstance().player)) {
					client.getSoundManager().play(WaterfallSoundInstance.ambient(Effective.AMBIENCE_WATERFALL, 1.2f + world.random.nextFloat() / 10f, blockPos, EffectiveConfig.cascadeSoundDistanceBlocks), (int) (distance / 2));
				}
			});
			generators.removeIf(blockPos -> blockPos == null || !shouldCauseWaterfall(world, blockPos, world.getFluidState(blockPos)));
		}
	}

	private static void tickParticles(World world) {
		for (BlockPos pos : particlesToSpawn.keySet()) {
			if (pos != null) {
				particlesToSpawn.computeInt(pos, (blockPos, integer) -> integer - 1);
				addWaterfallCloud(world, pos);
			}
		}
		particlesToSpawn.values().removeIf(integer -> integer < 0);
	}

	private static boolean shouldCauseWaterfall(BlockView world, BlockPos pos, FluidState fluidState) {
		if (!EffectiveConfig.enableCascades || fluidState.getFluid() != Fluids.FLOWING_WATER || world == null) {
			return false;
		}
		MinecraftClient client = MinecraftClient.getInstance();
		if (Math.sqrt(pos.getSquaredDistance(client.player.getBlockPos())) > client.options.getViewDistance().get() * 32) {
			return false;
		}
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		if (world.getFluidState(mutable.set(pos, Direction.DOWN)).getFluid() != Fluids.WATER) {
			return false;
		}
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
		if (!foundAir) {
			return false;
		}
		return Arrays.stream(Direction.values()).anyMatch(
				direction ->
						direction.getAxis() != Direction.Axis.Y &&
								world.getFluidState(mutable.set(pos.getX() + direction.getOffsetX(), pos.getY() - 1, pos.getZ() + direction.getOffsetZ())).getFluid() == Fluids.WATER
		);
	}

	public static void addWaterfallCloud(World world, BlockPos pos) {
		if (pos != null) {
			double offsetX = world.getRandom().nextGaussian() / 5f;
			double offsetZ = world.getRandom().nextGaussian() / 5f;

			DefaultParticleType waterfallCloud = Effective.WATERFALL_CLOUD;
			if (EffectiveConfig.enableGlowingPlankton && Effective.isNightTime(world) && world.getBiome(pos).isRegistryKey(BiomeKeys.WARM_OCEAN)) {
				waterfallCloud = Effective.GLOW_WATERFALL_CLOUD;
			}

			world.addParticle(waterfallCloud, pos.getX() + .5 + offsetX, pos.getY() + world.getRandom().nextFloat(), pos.getZ() + .5 + offsetZ, world.getRandom().nextFloat() / 5f * Math.signum(offsetX), world.getRandom().nextFloat() / 5f, world.getRandom().nextFloat() / 5f * Math.signum(offsetZ));
		}
	}

	public static void scheduleParticleTick(BlockPos pos, int ticks) {
		if (pos != null) {
			particlesToSpawn.put(pos, ticks);
		}
	}

	public static boolean canSeeWaterfall(World world, BlockPos waterfallPos, PlayerEntity player) {
		return world.raycast(new RaycastContext(new Vec3d(waterfallPos.getX(), waterfallPos.getY(), waterfallPos.getZ()), player.getCameraPosVec(MinecraftClient.getInstance().getTickDelta()), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, player)).getBlockPos().equals(player.getBlockPos());
	}
}
