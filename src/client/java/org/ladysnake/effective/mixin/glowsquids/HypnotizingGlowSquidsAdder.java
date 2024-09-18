package org.ladysnake.effective.mixin.glowsquids;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.SquidEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.GlowSquidEntity;
import net.minecraft.entity.passive.SquidEntity;
import org.ladysnake.effective.world.RenderedHypnotizingEntities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SquidEntityRenderer.class)
public class HypnotizingGlowSquidsAdder {
	// add glow squid to entities hypnotizing the client
	@Inject(method = "setupTransforms(Lnet/minecraft/entity/passive/SquidEntity;Lnet/minecraft/client/util/math/MatrixStack;FFF)V", at = @At("TAIL"))
	protected void setupTransforms(SquidEntity squidEntity, MatrixStack matrixStack, float f, float g, float h, CallbackInfo callbackInfo) {
		if (squidEntity instanceof GlowSquidEntity glowSquid && glowSquid.getDarkTicksRemaining() <= 0f && Math.sqrt(MinecraftClient.getInstance().player.getPos().squaredDistanceTo(squidEntity.getPos())) < 20.0) {
			RenderedHypnotizingEntities.GLOWSQUIDS.add(glowSquid);
		}
	}
}
