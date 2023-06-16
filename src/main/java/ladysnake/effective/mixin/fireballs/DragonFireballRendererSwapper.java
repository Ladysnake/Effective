package ladysnake.effective.mixin.fireballs;

import com.sammy.lodestone.systems.rendering.particle.Easing;
import com.sammy.lodestone.systems.rendering.particle.ParticleBuilders;
import ladysnake.effective.Effective;
import ladysnake.effective.EffectiveConfig;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.DragonFireballEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.projectile.DragonFireballEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
				ParticleBuilders.create(Effective.DRAGON_BREATH)
					.setSpin((float) (dragonFireballEntity.world.random.nextGaussian() / 5f))
					.setScale(scale, 0f)
					.setScaleEasing(Easing.CIRC_OUT)
					.setAlpha(1f)
					.setColor(new Color(0xD21EFF), new Color(0x7800FF))
					.setColorEasing(Easing.CIRC_OUT)
					.enableNoClip()
					.setLifetime(20)
					.spawn(dragonFireballEntity.world, x + dragonFireballEntity.world.random.nextGaussian() / 20f, y + (dragonFireballEntity.getHeight() / 2f) + dragonFireballEntity.world.random.nextGaussian() / 20f, z + dragonFireballEntity.world.random.nextGaussian() / 20f);
			}

			ci.cancel();
		}
	}
}
