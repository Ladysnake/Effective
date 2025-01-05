package org.ladysnake.effective.mixin.glow_squids;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.SquidEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.GlowSquidEntity;
import org.ladysnake.effective.world.RenderedHypnotizingEntities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SquidEntityRenderer.class)
public class HypnotizingGlowSquidsAdder {
	// add glow squid to entities hypnotizing the client
	@Inject(method = "setupTransforms(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/util/math/MatrixStack;FFFF)V", at = @At("TAIL"))
	protected void setupTransforms(LivingEntity entity, MatrixStack matrices, float animationProgress, float bodyYaw, float tickDelta, float scale, CallbackInfo ci) {
		if (entity instanceof GlowSquidEntity glowSquid && glowSquid.getDarkTicksRemaining() <= 0f && Math.sqrt(MinecraftClient.getInstance().player.getPos().squaredDistanceTo(entity.getPos())) < 20.0) {
			RenderedHypnotizingEntities.GLOWSQUIDS.add(glowSquid);
		}
	}
}
