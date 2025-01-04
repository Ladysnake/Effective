package org.ladysnake.effective.core.index;

import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.client.render.light.Light;
import foundry.veil.api.client.render.light.PointLight;
import foundry.veil.platform.VeilEventPlatform;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.GlowSquidEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.ladysnake.effective.core.EffectiveConfig;

import java.util.ArrayList;
import java.util.Map;

public class EffectiveLights {
	public static final ArrayList<Light> PARTICLE_LIGHTS = new ArrayList<>();
	public static final Map<Integer, PointLight> GLOW_SQUID_LIGHTS = new Object2ObjectOpenHashMap<>();

	public static void initialize() {
		VeilEventPlatform.INSTANCE.onVeilRenderLevelStage((stage, levelRenderer, bufferSource, matrixStack, frustumMatrix, projectionMatrix, renderTick, deltaTracker, camera, frustum) -> {
			// load glow squid lights
			for (Entity entity : MinecraftClient.getInstance().world.getEntities()) {
				if (entity instanceof GlowSquidEntity glowSquidEntity && EffectiveConfig.glowSquidDynamicLights) {
					PointLight light;

					Vec3d renderPosition = glowSquidEntity.getLerpedPos(deltaTracker.getTickDelta(false));
					if (!GLOW_SQUID_LIGHTS.containsKey(glowSquidEntity.getId())) {
						light = new PointLight();
						light.setBrightness(0f);
						light.setColor(0x69E2D0);
						light.setRadius(5f);
						light.setPosition(renderPosition.getX(), renderPosition.getY(), renderPosition.getZ());

						GLOW_SQUID_LIGHTS.put(glowSquidEntity.getId(), light);
						VeilRenderSystem.renderer().getLightRenderer().addLight(light);
					}

					light = GLOW_SQUID_LIGHTS.get(glowSquidEntity.getId());
					light.setPosition(renderPosition.getX(), renderPosition.getY(), renderPosition.getZ());
					light.setBrightness(MathHelper.clampedLerp(0.0F, 2.0F, 1.0F - (float) glowSquidEntity.getDarkTicksRemaining() / 10.0F));
				}
			}

			// clean glow squid light list of missing glow squids
			ArrayList<Integer> lightsToRemove = new ArrayList<>();
			for (Integer uuid : GLOW_SQUID_LIGHTS.keySet()) {
				Entity entity = MinecraftClient.getInstance().world.getEntityById(uuid);
				if (entity == null) {
					lightsToRemove.add(uuid);
				}
			}
			for (Integer uuid : lightsToRemove) {
				VeilRenderSystem.renderer().getLightRenderer().removeLight(GLOW_SQUID_LIGHTS.get(uuid));
				GLOW_SQUID_LIGHTS.remove(uuid);
			}
		});

		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
			GLOW_SQUID_LIGHTS.forEach((integer, pointLight) -> VeilRenderSystem.renderer().getLightRenderer().removeLight(pointLight));
			client.execute(GLOW_SQUID_LIGHTS::clear);
		});
	}
}
