package org.ladysnake.effective.core.utils;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.LidOpenable;
import net.minecraft.block.enums.ChestType;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeKeys;
import org.ladysnake.effective.core.Effective;
import org.ladysnake.effective.core.EffectiveConfig;
import org.ladysnake.effective.core.index.EffectiveParticles;

import java.awt.*;

public class EffectiveUtils {
	public static boolean isGoingFast(AllayEntity allayEntity) {
		Vec3d velocity = allayEntity.getVelocity();
		float speedRequired = 0.1f;

		return (velocity.getX() >= speedRequired || velocity.getX() <= -speedRequired)
			|| (velocity.getY() >= speedRequired || velocity.getY() <= -speedRequired)
			|| (velocity.getZ() >= speedRequired || velocity.getZ() <= -speedRequired);
	}

	/**
	 * chooses between spawning a normal droplet / ripple / waterfall cloud or glow one depending on biome
	 */
	public static void spawnWaterEffect(World world, Vec3d pos, double velocityX, double velocityY, double velocityZ, WaterEffectType waterEffect) {
		SimpleParticleType particle = switch (waterEffect) {
			case DROPLET -> EffectiveParticles.DROPLET;
			case RIPPLE -> EffectiveParticles.RIPPLE;
		};
		if (isGlowingWater(world, pos)) {
			particle = switch (waterEffect) {
				case DROPLET -> EffectiveParticles.GLOW_DROPLET;
				case RIPPLE -> EffectiveParticles.GLOW_RIPPLE;
			};
		}

		world.addParticle(particle, pos.getX(), pos.getY(), pos.getZ(), velocityX, velocityY, velocityZ);
	}

	public static boolean isGlowingWater(World world, Vec3d pos) {
		return isGlowingWater(world, BlockPos.ofFloored(pos));
	}

	public static boolean isGlowingWater(World world, BlockPos pos) {
		return EffectiveConfig.glowingPlankton && Effective.isNightTime(world) && world.getBiome(pos).matchesKey(BiomeKeys.WARM_OCEAN);
	}

	public static Color getGlowingWaterColor(World world, BlockPos pos) {
		return new Color(Math.min(1, world.random.nextFloat() / 5f + world.getLightLevel(LightType.BLOCK, pos) / 15f), Math.min(1, world.random.nextFloat() / 5f + world.getLightLevel(LightType.BLOCK, pos) / 15f), 1f);
	}

	public enum WaterEffectType {
		DROPLET,
		RIPPLE,
	}

	public static boolean isInCave(World world, BlockPos pos) {
		return pos.getY() < world.getSeaLevel() && EffectiveUtils.hasStoneAbove(world, pos);
	}

	public static boolean isInOverworld(World world, BlockPos pos) {
		return world.getBiome(pos).isIn(ConventionalBiomeTags.IS_OVERWORLD);
	}

	// method to check if the player has a stone material type block above them, more reliable to detect caves compared to isSkyVisible
	// (okay nvm they removed materials we're using pickaxe mineable instead lmao oh god this is gonna be so unreliable)
	public static boolean hasStoneAbove(World world, BlockPos pos) {
		BlockPos.Mutable mutable = pos.mutableCopy();
		int startY = mutable.getY();
		for (int y = startY; y <= startY + 100; y++) {
			mutable.setY(y);
			if (world.getBlockState(mutable).isSolidBlock(world, pos) && world.getBlockState(mutable).isIn(BlockTags.PICKAXE_MINEABLE)) {
				return true;
			}
		}
		return false;
	}

	public static float getRandomFloatOrNegative(Random random) {
		return random.nextFloat() * 2f - 1f;
	}


	public static void doUnderwaterChestLogic(World world, BlockEntity blockEntity) {
		if (blockEntity instanceof LidOpenable lidOpenable) {
			BlockState blockState = blockEntity.getCachedState();
			ChestType chestType = blockState.contains(ChestBlock.CHEST_TYPE) ? blockState.get(ChestBlock.CHEST_TYPE) : ChestType.SINGLE;
			Direction facing = blockState.contains(ChestBlock.FACING) ? blockState.get(ChestBlock.FACING) : Direction.NORTH;
			Block block = blockState.getBlock();
			if (block instanceof AbstractChestBlock && world.isWater(blockEntity.getPos()) && world.isWater(blockEntity.getPos().offset(Direction.UP, 1))) {
				AbstractChestBlock<?> abstractChestBlock = (AbstractChestBlock) block;
				boolean doubleChest = chestType != ChestType.SINGLE;

				DoubleBlockProperties.PropertySource<? extends ChestBlockEntity> propertySource;
				propertySource = abstractChestBlock.getBlockEntitySource(blockState, world, blockEntity.getPos(), true);

				float openFactor = propertySource.apply(ChestBlock.getAnimationProgressRetriever(lidOpenable)).get(1.0f);

				if (openFactor > 0) {
					if (doubleChest) {
						if (chestType == ChestType.LEFT) {
							float xOffset = 0f;
							float zOffset = 0f;
							float xOffsetRand = 0f;
							float zOffsetRand = 0f;

							if (facing == Direction.NORTH) {
								xOffset = 1f;
								zOffset = .5f;
								xOffsetRand = (world.random.nextFloat() - world.random.nextFloat()) * .8f;
								zOffsetRand = (world.random.nextFloat() - world.random.nextFloat()) * .3f;
							} else if (facing == Direction.SOUTH) {
								xOffset = 0f;
								zOffset = .5f;
								xOffsetRand = (world.random.nextFloat() - world.random.nextFloat()) * .8f;
								zOffsetRand = (world.random.nextFloat() - world.random.nextFloat()) * .3f;
							} else if (facing == Direction.EAST) {
								xOffset = .5f;
								zOffset = 1f;
								xOffsetRand = (world.random.nextFloat() - world.random.nextFloat()) * .3f;
								zOffsetRand = (world.random.nextFloat() - world.random.nextFloat()) * .8f;
							} else if (facing == Direction.WEST) {
								xOffset = .5f;
								zOffset = 0f;
								xOffsetRand = (world.random.nextFloat() - world.random.nextFloat()) * .3f;
								zOffsetRand = (world.random.nextFloat() - world.random.nextFloat()) * .8f;
							}

							for (int i = 0; i < 1 + world.random.nextInt(3); i++) {
								spawnBubble(world, blockEntity.getPos().getX() + xOffset + xOffsetRand, blockEntity.getPos().getY() + .5f, blockEntity.getPos().getZ() + zOffset + zOffsetRand, block == Blocks.ENDER_CHEST);
							}

							if (openFactor <= .6f) {
								spawnClosingBubble(world, blockEntity.getPos().getX() + xOffset, blockEntity.getPos().getY() + .5f, blockEntity.getPos().getZ() + zOffset, facing, true, block == Blocks.ENDER_CHEST);
							}
						}
					} else {
						for (int i = 0; i < 1 + world.random.nextInt(3); i++) {
							spawnBubble(world, blockEntity.getPos().getX() + .5f + (world.random.nextFloat() - world.random.nextFloat()) * .3f, blockEntity.getPos().getY() + .5f, blockEntity.getPos().getZ() + .5f + (world.random.nextFloat() - world.random.nextFloat()) * .3f, block == Blocks.ENDER_CHEST);
						}

						if (openFactor <= .6f) {
							spawnClosingBubble(world, blockEntity.getPos().getX() + .5f, blockEntity.getPos().getY() + .5f, blockEntity.getPos().getZ() + .5f, facing, false, block == Blocks.ENDER_CHEST);
						}
					}
				}
			}
		}
	}

	public static void spawnBubble(World world, float x, float y, float z, boolean endChest) {
		world.addParticle(endChest ? EffectiveParticles.END_BUBBLE : EffectiveParticles.BUBBLE, x, y, z, 0f, .05f + world.random.nextFloat() * .05f, 0f);
	}

	public static void spawnClosingBubble(World world, float x, float y, float z, Direction direction, boolean doubleChest, boolean endChest) {
		for (int i = 0; i < (doubleChest ? 10 : 5); i++) {
			float velX = .5f;
			float velZ = .5f;
			if (direction == Direction.NORTH) {
				velX = (world.random.nextFloat() - world.random.nextFloat()) / (doubleChest ? 2.5f : 5f);
				velZ = -.05f - (world.random.nextFloat() / 5f);
			} else if (direction == Direction.SOUTH) {
				velX = (world.random.nextFloat() - world.random.nextFloat()) / (doubleChest ? 2.5f : 5f);
				velZ = .05f + (world.random.nextFloat() / 5f);
			} else if (direction == Direction.EAST) {
				velX = .05f + (world.random.nextFloat() / 5f);
				velZ = (world.random.nextFloat() - world.random.nextFloat()) / (doubleChest ? 2.5f : 5f);
			} else if (direction == Direction.WEST) {
				velX = -.05f - (world.random.nextFloat() / 5f);
				velZ = (world.random.nextFloat() - world.random.nextFloat()) / (doubleChest ? 2.5f : 5f);
			}
			world.addParticle(endChest ? EffectiveParticles.END_BUBBLE : EffectiveParticles.BUBBLE, x, y, z, velX, .1f - (world.random.nextFloat() * .1f), velZ);
		}
	}
}
