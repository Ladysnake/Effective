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
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
				if (world.random.nextFloat() < openFactor) {
					if (doubleChest) {
						if (chestType == ChestType.LEFT) {
							float xOffset = 0f;
							float zOffset = 0f;

							if (facing == Direction.NORTH) {
								xOffset = 1f + (world.random.nextFloat() - world.random.nextFloat()) * .8f;
								zOffset = .5f + (world.random.nextFloat() - world.random.nextFloat()) * .3f;
							} else if (facing == Direction.SOUTH) {
								xOffset = (world.random.nextFloat() - world.random.nextFloat()) * .8f;
								zOffset = .5f + (world.random.nextFloat() - world.random.nextFloat()) * .3f;
							} else if (facing == Direction.EAST) {
								xOffset = .5f + (world.random.nextFloat() - world.random.nextFloat()) * .3f;
								zOffset = 1f + (world.random.nextFloat() - world.random.nextFloat()) * .8f;
							} else if (facing == Direction.WEST) {
								xOffset = .5f + (world.random.nextFloat() - world.random.nextFloat()) * .3f;
								zOffset = (world.random.nextFloat() - world.random.nextFloat()) * .8f;
							}

							spawnBubble(world, entity.getPos().getX() + xOffset, entity.getPos().getY() + .5f, entity.getPos().getZ() + zOffset);
						}
					} else {
						spawnBubble(world, entity.getPos().getX() + .5f + (world.random.nextFloat() - world.random.nextFloat()) * .3f, entity.getPos().getY() + .5f, entity.getPos().getZ() + .5f + (world.random.nextFloat() - world.random.nextFloat()) * .3f);
					}
				}
			}
		}
	}

	private void spawnBubble(World world, float x, float y, float z) {
		ParticleBuilders.create(Effective.BUBBLE)
			.setScale(.05f + world.random.nextFloat() * .05f)
			.setAlpha(1f)
			.enableNoClip()
			.setLifetime(60 + world.random.nextInt(60))
			.setMotion(0f, .1f, 0f)
			.overrideRenderType(ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT)
			.spawn(world, x, y, z);
	}
}
