package ladysnake.effective.mixin.chest_bubbles;

import com.sammy.lodestone.systems.rendering.particle.ParticleBuilders;
import ladysnake.effective.Effective;
import ladysnake.effective.EffectiveConfig;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.enums.ChestType;
import net.minecraft.client.block.ChestAnimationProgress;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.ChestBlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(ChestBlockEntityRenderer.class)
public class UnderwaterOpenChestBubbleSpawner<T extends BlockEntity & ChestAnimationProgress> {
	@Inject(method = "render(Lnet/minecraft/block/entity/BlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V", at = @At("TAIL"))
	private void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, CallbackInfo ci) {
		World world = entity.getWorld();
		boolean bl = world != null;

		if (EffectiveConfig.underwaterOpenChestBubbles && bl && world.random.nextInt(2) == 0) {
			BlockState blockState = entity.getCachedState();
			ChestType chestType = blockState.contains(ChestBlock.CHEST_TYPE) ? blockState.get(ChestBlock.CHEST_TYPE) : ChestType.SINGLE;
			Direction facing = blockState.contains(ChestBlock.FACING) ? blockState.get(ChestBlock.FACING) : Direction.NORTH;
			Block block = blockState.getBlock();
			if (block instanceof AbstractChestBlock && world.isWater(entity.getPos()) && world.isWater(entity.getPos().offset(Direction.UP, 1))) {
				AbstractChestBlock<?> abstractChestBlock = (AbstractChestBlock) block;
				boolean doubleChest = chestType != ChestType.SINGLE;

				DoubleBlockProperties.PropertySource<? extends ChestBlockEntity> propertySource;
				propertySource = abstractChestBlock.getBlockEntitySource(blockState, world, entity.getPos(), true);

				float openFactor = propertySource.apply(ChestBlock.getAnimationProgressRetriever(entity)).get(tickDelta);
				openFactor = 1.0F - openFactor;
				openFactor = 1.0F - openFactor * openFactor * openFactor;
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

							spawnBubble(world, entity.getPos().getX() + xOffset + xOffsetRand, entity.getPos().getY() + .5f, entity.getPos().getZ() + zOffset + zOffsetRand, block == Blocks.ENDER_CHEST);

							if (openFactor <= .6f) {
								spawnClosingBubble(world, entity.getPos().getX() + xOffset, entity.getPos().getY() + .5f, entity.getPos().getZ() + zOffset, facing, block == Blocks.ENDER_CHEST);
							}
						}
					} else {
						if (world.random.nextInt(2) == 0) {
							spawnBubble(world, entity.getPos().getX() + .5f + (world.random.nextFloat() - world.random.nextFloat()) * .3f, entity.getPos().getY() + .5f, entity.getPos().getZ() + .5f + (world.random.nextFloat() - world.random.nextFloat()) * .3f, block == Blocks.ENDER_CHEST);
						}
						if (openFactor <= .6f) {
							spawnClosingBubble(world, entity.getPos().getX() + .5f, entity.getPos().getY() + .5f, entity.getPos().getZ() + .5f, facing, block == Blocks.ENDER_CHEST);
						}
					}
				}
			}
		}
	}

	private void spawnBubble(World world, float x, float y, float z, boolean endChest) {
		ParticleBuilders.create(Effective.BUBBLE)
			.setScale(.05f + world.random.nextFloat() * .05f)
			.setAlpha(1f)
			.enableNoClip()
			.setLifetime(60 + world.random.nextInt(60))
			.setMotion(0f, .1f, 0f)
			.overrideRenderType(ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT)
			.setColor(new Color(endChest ? 0x00FF90 : 0xFFFFFF), new Color(endChest ? 0x00FF90 : 0xFFFFFF))
			.spawn(world, x, y, z);
	}

	private void spawnClosingBubble(World world, float x, float y, float z, Direction direction, boolean endChest) {
		for (int i = 0; i < 5; i++) {
			float velX = .5f;
			float velZ = .5f;
			if (direction == Direction.NORTH) {
				velX = (world.random.nextFloat() - world.random.nextFloat()) / 5f;
				velZ = -.05f - (world.random.nextFloat() / 5f);
			} else if (direction == Direction.SOUTH) {
				velX = (world.random.nextFloat() - world.random.nextFloat()) / 5f;
				velZ = .05f + (world.random.nextFloat() / 5f);
			} else if (direction == Direction.EAST) {
				velX = .05f + (world.random.nextFloat() / 5f);
				velZ = (world.random.nextFloat() - world.random.nextFloat()) / 5f;
			} else if (direction == Direction.WEST) {
				velX = -.05f - (world.random.nextFloat() / 5f);
				velZ = (world.random.nextFloat() - world.random.nextFloat()) / 5f;
			}

			ParticleBuilders.create(Effective.BUBBLE)
				.setScale(.05f + world.random.nextFloat() * .05f)
				.setAlpha(1f)
				.enableNoClip()
				.setLifetime(60 + world.random.nextInt(60))
				.setForcedMotion(new Vec3f(velX, .1f - (world.random.nextFloat() * .1f), velZ), new Vec3f(0f, .1f, 0f))
				.setMotionCoefficient(10f)
				.overrideRenderType(ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT)
				.setColor(new Color(endChest ? 0x00FF90 : 0xFFFFFF), new Color(endChest ? 0x00FF90 : 0xFFFFFF))
				.spawn(world, x, y, z);
		}
	}
}
