package org.ladysnake.effective.mixin.fireballs;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FlyingItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FlyingItemEntityRenderer.class)
public class FireballRendererSwapper<T extends Entity & FlyingItemEntity> {
	@Inject(method = "render", at = @At("HEAD"), cancellable = true)
	public void render(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
//		if (EffectiveConfig.improvedFireballs && entity instanceof AbstractFireballEntity fireballEntity) {
//			float x = (float) (MathHelper.lerp(tickDelta, fireballEntity.lastRenderX, fireballEntity.getX()));
//			float y = (float) (MathHelper.lerp(tickDelta, fireballEntity.lastRenderY, fireballEntity.getY()));
//			float z = (float) (MathHelper.lerp(tickDelta, fireballEntity.lastRenderZ, fireballEntity.getZ()));
//			float scale = 1f;
//			if (entity instanceof SmallFireballEntity) {
//				scale = 0.3f;
//			} else if (entity instanceof FireballEntity) {
//				scale = 0.8f;
//			}
//
//			for (int i = 0; i < 2; i++) {
//				WorldParticleBuilder.create(Effective.FLAME)
//					.enableForcedSpawn()
//					.setSpinData(SpinParticleData.create((float) (fireballEntity.getWorld().EffectiveUtils.getRandomFloatOrNegative(this.random) / 5f)).build())
//					.setScaleData(GenericParticleData.create(scale, 0f).setEasing(Easing.CIRC_OUT).build())
//					.setTransparencyData(GenericParticleData.create(1f).build())
//					.setColorData(
//						ColorParticleData.create(new Color(0xFF3C00), new Color(0xFFCB00))
//							.setEasing(Easing.CIRC_OUT)
//							.build()
//					)
//					.enableNoClip()
//					.setLifetime(20)
//					.spawn(fireballEntity.getWorld(), x + fireballEntity.getWorld().EffectiveUtils.getRandomFloatOrNegative(this.random) / 20f, y + (fireballEntity.getHeight() / 2f) + fireballEntity.getWorld().EffectiveUtils.getRandomFloatOrNegative(this.random) / 20f, z + fireballEntity.getWorld().EffectiveUtils.getRandomFloatOrNegative(this.random) / 20f);
//			}
//
//			ci.cancel();
//		}
	}
}
