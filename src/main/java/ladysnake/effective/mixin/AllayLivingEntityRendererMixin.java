package ladysnake.effective.mixin;

import ladysnake.effective.client.Effective;
import ladysnake.effective.client.EffectiveConfig;
import ladysnake.effective.client.contracts.AllayParticleInitialData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AllayEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class AllayLivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> {
	protected AllayLivingEntityRendererMixin(EntityRendererFactory.Context ctx) {
		super(ctx);
	}

	// allay trail and twinkle
	@Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("TAIL"))
	public void render(T livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
		if (EffectiveConfig.enableAllayTrails && livingEntity instanceof AllayEntity allayEntity && allayEntity.getX() != allayEntity.prevX && allayEntity.getY() != allayEntity.prevY && allayEntity.getZ() != allayEntity.prevZ && !MinecraftClient.getInstance().isPaused()) {
			AllayParticleInitialData data = new AllayParticleInitialData(allayEntity.getUuid().hashCode() % 2 == 0 && EffectiveConfig.goldenAllays ? 0xFFC200 : 0x22CFFF);

			allayEntity.world.addParticle(Effective.ALLAY_TRAIL.setData(data),
					allayEntity.getClientCameraPosVec(MinecraftClient.getInstance().getTickDelta()).x,
					allayEntity.getClientCameraPosVec(MinecraftClient.getInstance().getTickDelta()).y - 0.2f,
					allayEntity.getClientCameraPosVec(MinecraftClient.getInstance().getTickDelta()).z,
					0, 0, 0);

			if ((allayEntity.getRandom().nextInt(100) + 1) <= EffectiveConfig.allayTwinkleDensity) {
				allayEntity.world.addParticle(Effective.ALLAY_TWINKLE.setData(data),
						allayEntity.getClientCameraPosVec(MinecraftClient.getInstance().getTickDelta()).x + allayEntity.getRandom().nextGaussian() / 2f,
						allayEntity.getClientCameraPosVec(MinecraftClient.getInstance().getTickDelta()).y - 0.2f + allayEntity.getRandom().nextGaussian() / 2f,
						allayEntity.getClientCameraPosVec(MinecraftClient.getInstance().getTickDelta()).z + allayEntity.getRandom().nextGaussian() / 2f,
						0, 0, 0);
			}
		}
	}

}
