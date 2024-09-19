package org.ladysnake.effective.core.mixin.spectral_arrows;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.SpectralArrowEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.ladysnake.effective.core.Effective;
import org.ladysnake.effective.core.EffectiveConfig;
import org.ladysnake.effective.core.utils.PositionTrackedEntity;
import org.ladysnake.effective.core.particle.contracts.ColoredParticleInitialData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.lodestar.lodestone.registry.client.LodestoneRenderTypeRegistry;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.rendering.VFXBuilders;
import team.lodestar.lodestone.systems.rendering.rendeertype.RenderTypeToken;
import team.lodestar.lodestone.systems.rendering.trail.TrailPoint;

import java.awt.*;
import java.util.List;

@Mixin(ProjectileEntityRenderer.class)
public abstract class SpectralArrowTrailRenderer<T extends PersistentProjectileEntity> extends EntityRenderer<T> {
	private static final RenderLayer TRAIL_TYPE = LodestoneRenderTypeRegistry.ADDITIVE_TEXTURE_TRIANGLE.apply(RenderTypeToken.createCachedToken(Identifier.of(Effective.MODID, "textures/vfx/light_trail.png")));

	protected SpectralArrowTrailRenderer(EntityRendererFactory.Context ctx) {
		super(ctx);
	}

	public RenderLayer getTrailRenderType() {
		return TRAIL_TYPE;
	}

	// spectral arrow trail and twinkle
	@Inject(method = "render(Lnet/minecraft/entity/projectile/PersistentProjectileEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("TAIL"))
	public void render(T entity, float entityYaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, CallbackInfo ci) {
		// new render
		if (EffectiveConfig.spectralArrowTrails != EffectiveConfig.TrailOptions.NONE && entity instanceof SpectralArrowEntity spectralArrowEntity && !spectralArrowEntity.isInvisible()) {
			ColoredParticleInitialData data = new ColoredParticleInitialData(0xFFFF77);

			// trail
			if (EffectiveConfig.spectralArrowTrails == EffectiveConfig.TrailOptions.BOTH || EffectiveConfig.spectralArrowTrails == EffectiveConfig.TrailOptions.TWINKLE) {
				matrixStack.push();
				List<TrailPoint> positions = ((PositionTrackedEntity) (Object) spectralArrowEntity).getPastPositions();
				VFXBuilders.WorldVFXBuilder builder = VFXBuilders.createWorld().setRenderType(getTrailRenderType());

				float size = 0.15f;
				float alpha = 1f;

				float x = (float) MathHelper.lerp(tickDelta, spectralArrowEntity.prevX, spectralArrowEntity.getX());
				float y = (float) MathHelper.lerp(tickDelta, spectralArrowEntity.prevY, spectralArrowEntity.getY());
				float z = (float) MathHelper.lerp(tickDelta, spectralArrowEntity.prevZ, spectralArrowEntity.getZ());

				matrixStack.translate(-x, -y, -z);
				builder.setColor(new Color(data.color))
					.setAlpha(alpha)
					.renderTrail(matrixStack,
						positions,
						f -> MathHelper.sqrt(f) * size,
						f -> builder.setAlpha((float) Math.cbrt(Math.max(0, (alpha * f) - 0.1f)))
					)
					.renderTrail(matrixStack,
						positions,
						f -> (MathHelper.sqrt(f) * size) / 1.5f,
						f -> builder.setAlpha((float) Math.cbrt(Math.max(0, (((alpha * f) / 1.5f) - 0.1f))))
					);

				matrixStack.pop();
			}

			// twinkles
			if (EffectiveConfig.spectralArrowTrails == EffectiveConfig.TrailOptions.BOTH || EffectiveConfig.spectralArrowTrails == EffectiveConfig.TrailOptions.TWINKLE) {
				if ((spectralArrowEntity.getWorld().getRandom().nextInt(100) + 1) <= 5 && !MinecraftClient.getInstance().isPaused()) {
					float spreadDivider = 4f;
					WorldParticleBuilder.create(Effective.ALLAY_TWINKLE)
						.enableForcedSpawn()
						.setColorData(ColorParticleData.create(new Color(data.color), new Color(data.color)).build())
						.setTransparencyData(GenericParticleData.create(0.9f).build())
						.setScaleData(GenericParticleData.create(0.06f).build())
						.setLifetime(15)
						.setMotion(0, 0.05f, 0)
						.spawn(spectralArrowEntity.getWorld(), spectralArrowEntity.getClientCameraPosVec(MinecraftClient.getInstance().getTickDelta()).x + spectralArrowEntity.getWorld().getRandom().nextGaussian() / spreadDivider, spectralArrowEntity.getClientCameraPosVec(MinecraftClient.getInstance().getTickDelta()).y - 0.2f + spectralArrowEntity.getWorld().getRandom().nextGaussian() / spreadDivider, spectralArrowEntity.getClientCameraPosVec(MinecraftClient.getInstance().getTickDelta()).z + spectralArrowEntity.getWorld().getRandom().nextGaussian() / spreadDivider);
				}
			}
		}
	}

}
