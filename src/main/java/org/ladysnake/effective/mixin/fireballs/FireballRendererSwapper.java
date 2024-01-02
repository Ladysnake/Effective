package org.ladysnake.effective.mixin.fireballs;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.util.math.MathHelper;
import org.ladysnake.effective.Effective;
import org.ladysnake.effective.EffectiveConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.lodestar.lodestone.systems.rendering.particle.Easing;
import team.lodestar.lodestone.systems.rendering.particle.WorldParticleBuilder;
import team.lodestar.lodestone.systems.rendering.particle.data.ColorParticleData;
import team.lodestar.lodestone.systems.rendering.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.rendering.particle.data.SpinParticleData;

import java.awt.*;

@Mixin(FlyingItemEntityRenderer.class)
public class FireballRendererSwapper<T extends Entity & FlyingItemEntity> {
	@Inject(method = "render", at = @At("HEAD"), cancellable = true)
	public void render(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
		if (EffectiveConfig.improvedFireballs && entity instanceof AbstractFireballEntity fireballEntity) {
			float x = (float) (MathHelper.lerp(tickDelta, fireballEntity.lastRenderX, fireballEntity.getX()));
			float y = (float) (MathHelper.lerp(tickDelta, fireballEntity.lastRenderY, fireballEntity.getY()));
			float z = (float) (MathHelper.lerp(tickDelta, fireballEntity.lastRenderZ, fireballEntity.getZ()));
			float scale = 1f;
			if (entity instanceof SmallFireballEntity) {
				scale = 0.3f;
			} else if (entity instanceof FireballEntity) {
				scale = 0.8f;
			}

			for (int i = 0; i < 2; i++) {
				WorldParticleBuilder.create(Effective.FLAME)
					.setSpinData(SpinParticleData.create((float) (fireballEntity.getWorld().random.nextGaussian() / 5f)).build())
					.setScaleData(GenericParticleData.create(scale, 0f).setEasing(Easing.CIRC_OUT).build())
					.setTransparencyData(GenericParticleData.create(1f).build())
					.setColorData(
						ColorParticleData.create(new Color(0xFF3C00), new Color(0xFFCB00))
							.setEasing(Easing.CIRC_OUT)
							.build()
					)
					.enableNoClip()
					.setLifetime(20)
					.spawn(fireballEntity.getWorld(), x + fireballEntity.getWorld().random.nextGaussian() / 20f, y + (fireballEntity.getHeight() / 2f) + fireballEntity.getWorld().random.nextGaussian() / 20f, z + fireballEntity.getWorld().random.nextGaussian() / 20f);
			}

			ci.cancel();
		}
	}
}
