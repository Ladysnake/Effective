package org.ladysnake.effective.mixin.fireballs;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.DragonFireballEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.projectile.DragonFireballEntity;
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

@Mixin(DragonFireballEntityRenderer.class)
public class DragonFireballRendererSwapper {
	@Inject(method = "render(Lnet/minecraft/entity/projectile/DragonFireballEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"), cancellable = true)
	public void render(DragonFireballEntity dragonFireballEntity, float f, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, CallbackInfo ci) {
		if (EffectiveConfig.improvedDragonFireballsAndBreath) {
			float x = (float) (MathHelper.lerp(tickDelta, dragonFireballEntity.lastRenderX, dragonFireballEntity.getX()));
			float y = (float) (MathHelper.lerp(tickDelta, dragonFireballEntity.lastRenderY, dragonFireballEntity.getY()));
			float z = (float) (MathHelper.lerp(tickDelta, dragonFireballEntity.lastRenderZ, dragonFireballEntity.getZ()));
			float scale = 1f;

			for (int i = 0; i < 2; i++) {
				WorldParticleBuilder.create(Effective.DRAGON_BREATH)
					.setSpinData(SpinParticleData.create((float) (dragonFireballEntity.getWorld().random.nextGaussian() / 5f)).build())
					.setScaleData(GenericParticleData.create(scale, 0f).setEasing(Easing.CIRC_OUT).build())
					.setTransparencyData(GenericParticleData.create(1f).build())
					.setColorData(
						ColorParticleData.create(new Color(0xD21EFF), new Color(0x7800FF))
							.setEasing(Easing.CIRC_OUT)
							.build()
					)
					.enableNoClip()
					.setLifetime(20)
					.spawn(dragonFireballEntity.getWorld(), x + dragonFireballEntity.getWorld().random.nextGaussian() / 20f, y + (dragonFireballEntity.getHeight() / 2f) + dragonFireballEntity.getWorld().random.nextGaussian() / 20f, z + dragonFireballEntity.getWorld().random.nextGaussian() / 20f);
			}

			ci.cancel();
		}
	}
}
