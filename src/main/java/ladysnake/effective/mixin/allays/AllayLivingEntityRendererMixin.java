package ladysnake.effective.mixin.allays;

import com.sammy.lodestone.setup.LodestoneRenderLayers;
import com.sammy.lodestone.systems.rendering.PositionTrackedEntity;
import com.sammy.lodestone.systems.rendering.VFXBuilders;
import com.sammy.lodestone.systems.rendering.particle.ScreenParticleBuilder;
import com.sammy.lodestone.systems.rendering.particle.SimpleParticleEffect;
import com.sammy.lodestone.systems.rendering.particle.WorldParticleBuilder;
import com.sammy.lodestone.systems.rendering.particle.data.ColorParticleData;
import com.sammy.lodestone.systems.rendering.particle.data.GenericParticleData;
import ladysnake.effective.client.Effective;
import ladysnake.effective.client.EffectiveConfig;
import ladysnake.effective.client.EffectiveUtils;
import ladysnake.effective.client.particle.contracts.ColoredParticleInitialData;
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
import net.minecraft.util.math.Vec3d;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.util.ArrayList;

import static com.sammy.lodestone.handlers.RenderHandler.DELAYED_RENDER;

@Mixin(LivingEntityRenderer.class)
public abstract class AllayLivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> {
	private static final Identifier LIGHT_TRAIL = new Identifier(Effective.MODID, "textures/vfx/light_trail.png");
	private static final RenderLayer LIGHT_TYPE = LodestoneRenderLayers.ADDITIVE_TEXTURE.apply(LIGHT_TRAIL);

	protected AllayLivingEntityRendererMixin(EntityRendererFactory.Context ctx) {
		super(ctx);
	}

	// allay trail and twinkle
	@Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("TAIL"))
	public void render(T livingEntity, float entityYaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, CallbackInfo ci) {
		// new render
		if (EffectiveConfig.enableAllayTrails && livingEntity instanceof AllayEntity allayEntity && !allayEntity.isInvisible()) {
			ColoredParticleInitialData data = new ColoredParticleInitialData(allayEntity.getUuid().hashCode() % 2 == 0 && EffectiveConfig.goldenAllays ? 0xFFC200 : 0x22CFFF);

			// trail
			matrixStack.push();
			ArrayList<Vec3d> positions = new ArrayList<>(((PositionTrackedEntity) allayEntity).getPastPositions());
			VFXBuilders.WorldVFXBuilder builder = VFXBuilders.createWorld().setPosColorTexLightmapDefaultFormat();

			float size = 0.2f;
			float alpha = 1f;

			float x = (float) MathHelper.lerp(tickDelta, allayEntity.prevX, allayEntity.getX());
			float y = (float) MathHelper.lerp(tickDelta, allayEntity.prevY, allayEntity.getY());
			float z = (float) MathHelper.lerp(tickDelta, allayEntity.prevZ, allayEntity.getZ());

			builder.setColor(new Color(data.color)).setOffset(-x, -y, -z)
					.setAlpha(alpha)
					.renderTrail(
							DELAYED_RENDER.getBuffer(LIGHT_TYPE),
							matrixStack,
							positions.stream()
									.map(p -> new Vector4f((float) p.x, (float) p.y, (float) p.z, 1))
									.toList(),
							f -> MathHelper.sqrt(f) * size,
							f -> builder.setAlpha((float) Math.cbrt(Math.max(0, (alpha * f) - 0.1f)))
					)
					.renderTrail(
							DELAYED_RENDER.getBuffer(LIGHT_TYPE),
							matrixStack,
							positions.stream()
									.map(p -> new Vector4f((float) p.x, (float) p.y, (float) p.z, 1))
									.toList(),
							f -> (MathHelper.sqrt(f) * size) / 1.5f,
							f -> builder.setAlpha((float) Math.cbrt(Math.max(0, (((alpha * f) / 1.5f) - 0.1f))))
					);

			matrixStack.pop();

			// twinkles
			if ((allayEntity.getRandom().nextInt(100) + 1) <= EffectiveConfig.allayTwinkleDensity && EffectiveUtils.isGoingFast(allayEntity) && !MinecraftClient.getInstance().isPaused()) {
				float spreadDivider = 4f;
				WorldParticleBuilder.create(Effective.ALLAY_TWINKLE)
						.setColorData(ColorParticleData.create(new Color(data.color), new Color(data.color)).build())
						.setTransparencyData(GenericParticleData.create(0.9f).build())
						.setScaleData(GenericParticleData.create(0.06f).build())
						.setLifetime(15)
						.setMotion(0, 0.05f, 0)
						.spawn(allayEntity.world, allayEntity.getClientCameraPosVec(MinecraftClient.getInstance().getTickDelta()).x + allayEntity.getRandom().nextGaussian() / spreadDivider, allayEntity.getClientCameraPosVec(MinecraftClient.getInstance().getTickDelta()).y - 0.2f + allayEntity.getRandom().nextGaussian() / spreadDivider, allayEntity.getClientCameraPosVec(MinecraftClient.getInstance().getTickDelta()).z + allayEntity.getRandom().nextGaussian() / spreadDivider);
			}
		}
	}

}
