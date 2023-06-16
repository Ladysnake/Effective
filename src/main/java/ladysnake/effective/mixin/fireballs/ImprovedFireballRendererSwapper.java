package ladysnake.effective.mixin.fireballs;

import com.sammy.lodestone.setup.LodestoneRenderLayers;
import com.sammy.lodestone.systems.rendering.particle.Easing;
import com.sammy.lodestone.systems.rendering.particle.ParticleBuilders;
import ladysnake.effective.Effective;
import ladysnake.effective.EffectiveConfig;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(FlyingItemEntityRenderer.class)
public class ImprovedFireballRendererSwapper<T extends Entity & FlyingItemEntity> {
	@Shadow
	@Final
	private float scale;

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
				ParticleBuilders.create(Effective.FLAME)
					.setSpin((float) (fireballEntity.world.random.nextGaussian() / 5f))
					.setScale(scale, 0f)
					.setScaleEasing(Easing.CIRC_OUT)
					.setAlpha(1f)
					.setColor(new Color(0xFF3C00), new Color(0xFFCB00))
					.setColorEasing(Easing.CIRC_OUT)
					.enableNoClip()
					.setLifetime(20)
					.spawn(fireballEntity.world, x + fireballEntity.world.random.nextGaussian() / 20f, y + (fireballEntity.getHeight() / 2f) + fireballEntity.world.random.nextGaussian() / 20f, z + fireballEntity.world.random.nextGaussian() / 20f);
			}

			ci.cancel();
		}
	}
}
