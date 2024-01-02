package org.ladysnake.effective.mixin.chest_bubbles;

import net.minecraft.block.AbstractChestBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.DoubleBlockProperties;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.EnderChestBlockEntity;
import net.minecraft.block.enums.ChestType;
import net.minecraft.client.block.ChestAnimationProgress;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.joml.Vector3f;
import org.ladysnake.effective.Effective;
import org.ladysnake.effective.EffectiveConfig;
import org.ladysnake.effective.LinearForcedMotionImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.lodestar.lodestone.systems.rendering.particle.WorldParticleBuilder;
import team.lodestar.lodestone.systems.rendering.particle.data.ColorParticleData;
import team.lodestar.lodestone.systems.rendering.particle.data.GenericParticleData;

import java.awt.*;

@Mixin(EnderChestBlockEntity.class)
public class UnderwaterOpenEnderChestBubbleSpawner<T extends BlockEntity & ChestAnimationProgress> {
	public boolean justClosed = false;

	@Inject(method = "clientTick", at = @At("TAIL"))
	private static void clientTick(World world, BlockPos pos, BlockState state, EnderChestBlockEntity blockEntity, CallbackInfo ci) {
		boolean bl = world != null;

		if (EffectiveConfig.underwaterOpenChestBubbles && bl && world.random.nextInt(2) == 0) {
			BlockState blockState = blockEntity.getCachedState();
			ChestType chestType = blockState.contains(ChestBlock.CHEST_TYPE) ? blockState.get(ChestBlock.CHEST_TYPE) : ChestType.SINGLE;
			Direction facing = blockState.contains(ChestBlock.FACING) ? blockState.get(ChestBlock.FACING) : Direction.NORTH;
			Block block = blockState.getBlock();
			if (block instanceof AbstractChestBlock && world.isWater(blockEntity.getPos()) && world.isWater(blockEntity.getPos().offset(Direction.UP, 1))) {
				AbstractChestBlock<?> abstractChestBlock = (AbstractChestBlock) block;
				boolean doubleChest = chestType != ChestType.SINGLE;

				DoubleBlockProperties.PropertySource<? extends ChestBlockEntity> propertySource;
				propertySource = abstractChestBlock.getBlockEntitySource(blockState, world, blockEntity.getPos(), true);

				float openFactor = propertySource.apply(ChestBlock.getAnimationProgressRetriever(blockEntity)).get(1.0f);

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

	private static void spawnBubble(World world, float x, float y, float z, boolean endChest) {
		float bubbleSize= .05f + world.random.nextFloat() * .05f;
		WorldParticleBuilder.create(Effective.BUBBLE)
			.setScaleData(GenericParticleData.create(bubbleSize).build())
			.setTransparencyData(GenericParticleData.create(1f).build())
			.enableNoClip()
			.setLifetime(60 + world.random.nextInt(60))
			.setMotion(0f, bubbleSize, 0f)
			.setRenderType(ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT)
			.setColorData(ColorParticleData.create(new Color(endChest ? 0x00FF90 : 0xFFFFFF), new Color(endChest ? 0x00FF90 : 0xFFFFFF)).build())
			.spawn(world, x, y, z);
	}

	private static void spawnClosingBubble(World world, float x, float y, float z, Direction direction, boolean doubleChest, boolean endChest) {
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
			WorldParticleBuilder.create(Effective.BUBBLE)
				.setScaleData(GenericParticleData.create(.05f + world.random.nextFloat() * .05f).build())
				.setTransparencyData(GenericParticleData.create(1f).build())
				.enableNoClip()
				.setLifetime(60 + world.random.nextInt(60))
				.addActor(new LinearForcedMotionImpl(
					new Vector3f(velX, .1f - (world.random.nextFloat() * .1f), velZ),
					new Vector3f(0f, .1f, 0f),
					10f
				))
				.setRenderType(ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT)
				.setColorData(ColorParticleData.create(new Color(endChest ? 0x00FF90 : 0xFFFFFF), new Color(endChest ? 0x00FF90 : 0xFFFFFF)).build())
				.spawn(world, x, y, z);
		}
	}
}
