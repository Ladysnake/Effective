package org.ladysnake.effective.mixin.glowsquids;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.GlowSquidEntity;
import net.minecraft.util.Identifier;
import org.ladysnake.effective.Effective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FeatureRenderer.class)
public abstract class RainbowShaderRenderLayerSwapper {
	@Unique
	private static boolean isRgb;

	@Inject(method = "renderModel", at = @At("HEAD"))
	private static <T extends LivingEntity> void captureEntity(EntityModel<T> model, Identifier texture, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float red, float green, float blue, CallbackInfo ci) {
		isRgb = entity instanceof GlowSquidEntity && entity.hasCustomName() && "jeb_".equals(entity.getName().getString());
	}

	@ModifyArg(method = "renderModel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexConsumerProvider;getBuffer(Lnet/minecraft/client/render/RenderLayer;)Lcom/mojang/blaze3d/vertex/VertexConsumer;"))
	private static RenderLayer replaceRenderLayer(RenderLayer base) {
		if (isRgb) {
			return Effective.RAINBOW_SHADER.getRenderLayer(base);
		}

		return base;
	}
}
