package org.ladysnake.effective.core.mixin.allays;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.ladysnake.effective.core.Effective;
import org.ladysnake.effective.core.EffectiveConfig;
import org.ladysnake.effective.core.utils.EffectiveUtils;
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

@Mixin(LivingEntityRenderer.class)
public abstract class AllayTrailRenderer<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> {
	private static final RenderLayer TRAIL_TYPE = LodestoneRenderTypeRegistry.ADDITIVE_TEXTURE_TRIANGLE.apply(RenderTypeToken.createCachedToken(Identifier.of(Effective.MODID, "textures/vfx/light_trail.png")));

	protected AllayTrailRenderer(EntityRendererFactory.Context ctx) {
		super(ctx);
	}

	public RenderLayer getTrailRenderType() {
		return TRAIL_TYPE;
	}

	// allay trail and twinkle
	@Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("TAIL"))
	public void render(T livingEntity, float entityYaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, CallbackInfo ci) {
		// new render
		if (EffectiveConfig.allayTrails != EffectiveConfig.TrailOptions.NONE && livingEntity instanceof AllayEntity allayEntity && !allayEntity.isInvisible()) {
			ColoredParticleInitialData data = new ColoredParticleInitialData(allayEntity.getUuid().hashCode() % 2 == 0 && EffectiveConfig.goldenAllays ? 0xFFC200 : 0x22CFFF);

			// trail
			if (EffectiveConfig.allayTrails == EffectiveConfig.TrailOptions.BOTH || EffectiveConfig.allayTrails == EffectiveConfig.TrailOptions.TRAIL) {
				matrixStack.push();
				List<TrailPoint> positions = ((PositionTrackedEntity) allayEntity).getPastPositions();
				VFXBuilders.WorldVFXBuilder builder = VFXBuilders.createWorld().setRenderType(getTrailRenderType());

				float size = 0.2f;
				float alpha = 1f;

				float x = (float) MathHelper.lerp(tickDelta, allayEntity.prevX, allayEntity.getX());
				float y = (float) MathHelper.lerp(tickDelta, allayEntity.prevY, allayEntity.getY());
				float z = (float) MathHelper.lerp(tickDelta, allayEntity.prevZ, allayEntity.getZ());

				matrixStack.translate(-x, -y, -z);
				builder.setColor(new Color(data.color))
					.setAlpha(alpha)
					.renderTrail(matrixStack,
						positions,
						f -> MathHelper.sqrt(f) * size,
						f -> builder.setAlpha((float) Math.cbrt(Math.max(0, (alpha * f) - 0.1f)))
					)
					.setAlpha(alpha)
					.renderTrail(matrixStack,
						positions,
						f -> (MathHelper.sqrt(f) * size) / 1.5f,
						f -> builder.setAlpha((float) Math.cbrt(Math.max(0, (((alpha * f) / 1.5f) - 0.1f))))
					);

				matrixStack.pop();
			}

			// twinkles
			if (EffectiveConfig.allayTrails == EffectiveConfig.TrailOptions.BOTH || EffectiveConfig.allayTrails == EffectiveConfig.TrailOptions.TWINKLE) {
				if ((allayEntity.getRandom().nextInt(100) + 1) <= 5 && EffectiveUtils.isGoingFast(allayEntity) && !MinecraftClient.getInstance().isPaused()) {
					float spreadDivider = 4f;
					WorldParticleBuilder.create(Effective.ALLAY_TWINKLE)
						.enableForcedSpawn()
						.setColorData(ColorParticleData.create(new Color(data.color), new Color(data.color)).build())
						.setTransparencyData(GenericParticleData.create(0.9f).build())
						.setScaleData(GenericParticleData.create(0.12f).build())
						.setLifetime(15)
						.setMotion(0, 0.05f, 0)
						.spawn(allayEntity.getWorld(), allayEntity.getClientCameraPosVec(MinecraftClient.getInstance().getTickDelta()).x + allayEntity.getRandom().nextGaussian() / spreadDivider, allayEntity.getClientCameraPosVec(MinecraftClient.getInstance().getTickDelta()).y - 0.2f + allayEntity.getRandom().nextGaussian() / spreadDivider, allayEntity.getClientCameraPosVec(MinecraftClient.getInstance().getTickDelta()).z + allayEntity.getRandom().nextGaussian() / spreadDivider);
				}
			}
		}
	}

}
