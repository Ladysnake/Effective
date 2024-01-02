package org.ladysnake.effective.mixin.glowsquids;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.GlowSquidEntity;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.effective.Effective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public class JebGlowSquidRainbowRenderer {
	@Nullable
	@Inject(method = "getRenderLayer", at = @At("RETURN"), cancellable = true)
	protected void getRenderLayer(LivingEntity entity, boolean showBody, boolean translucent, boolean showOutline, CallbackInfoReturnable<RenderLayer> cir) {
		if (entity instanceof GlowSquidEntity) {
			RenderLayer baseLayer = cir.getReturnValue();
			if (entity.hasCustomName() && "jeb_".equals(entity.getName().getString())) {
				cir.setReturnValue(baseLayer == null ? null : Effective.RAINBOW_SHADER.getRenderLayer(baseLayer));
			}
		}
	}
}
