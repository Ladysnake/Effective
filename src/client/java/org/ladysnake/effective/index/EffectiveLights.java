package org.ladysnake.effective.index;

import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.client.render.light.data.LightData;
import foundry.veil.api.client.render.light.data.PointLightData;
import foundry.veil.api.client.render.light.renderer.LightRenderHandle;
import foundry.veil.platform.VeilEventPlatform;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.entity.passive.GlowSquidEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.ladysnake.effective.EffectiveConfig;
import org.ladysnake.effective.utils.EffectiveUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

public class EffectiveLights {
	public static final ArrayList<LightData> PARTICLE_LIGHTS = new ArrayList<>();
	public static final Map<Integer, PointLightData> ENTITY_LIGHTS = new Object2ObjectOpenHashMap<>();
	public static final Map<Integer, LightRenderHandle<PointLightData>> ENTITY_LIGHT_HANDLES = new Object2ObjectOpenHashMap<>();

	public static void initialize() {
		VeilEventPlatform.INSTANCE.onVeilRenderLevelStage((stage, levelRenderer, bufferSource, matrixStack, frustumMatrix, projectionMatrix, renderTick, deltaTracker, camera, frustum) -> {
			for (Entity entity : MinecraftClient.getInstance().world.getEntities()) {
				if (entity instanceof GlowSquidEntity glowSquidEntity && EffectiveConfig.glowSquidDynamicLights) {
					tickGlowSquidLight(glowSquidEntity, deltaTracker);
				} else if (entity instanceof AllayEntity allayEntity && EffectiveConfig.allayDynamicLights) {
					tickAllayLight(allayEntity, deltaTracker);
				}
			}

			// clean glow squid light list of missing glow squids
			ArrayList<Integer> lightsToRemove = new ArrayList<>();
			for (Integer uuid : ENTITY_LIGHTS.keySet()) {
				Entity entity = MinecraftClient.getInstance().world.getEntityById(uuid);
				if (entity == null) {
					lightsToRemove.add(uuid);
				}
			}
			for (Integer uuid : lightsToRemove) {
				ENTITY_LIGHT_HANDLES.get(uuid).free();
				ENTITY_LIGHTS.remove(uuid);
			}
		});

		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
			ENTITY_LIGHT_HANDLES.forEach((integer, handle) -> handle.free());
			client.execute(ENTITY_LIGHTS::clear);
		});
	}

	private static void tickGlowSquidLight(GlowSquidEntity glowSquidEntity, RenderTickCounter deltaTracker) {
		PointLightData light;
		Vec3d renderPosition = glowSquidEntity.getLerpedPos(deltaTracker.getTickDelta(false));

		if (ENTITY_LIGHTS.containsKey(glowSquidEntity.getId())) {
			light = ENTITY_LIGHTS.get(glowSquidEntity.getId());
		} else {
			light = new PointLightData();
			light.setBrightness(0f);
			light.setColor(0x69E2D0);
			light.setRadius(10f);
			light.setPosition(renderPosition.getX(), renderPosition.getY(), renderPosition.getZ());

			ENTITY_LIGHTS.put(glowSquidEntity.getId(), light);
			ENTITY_LIGHT_HANDLES.put(glowSquidEntity.getId(), VeilRenderSystem.renderer().getLightRenderer().addLight(light));
		}

		light.setPosition(renderPosition.getX(), renderPosition.getY(), renderPosition.getZ());
		light.setBrightness(MathHelper.clampedLerp(0.0F, 1.0F, 1.0F - (float) glowSquidEntity.getDarkTicksRemaining() / 10.0F));
	}

	private static void tickAllayLight(AllayEntity allayEntity, RenderTickCounter deltaTracker) {
		PointLightData light;
		Vec3d renderPosition = allayEntity.getLerpedPos(deltaTracker.getTickDelta(false));

		if (ENTITY_LIGHTS.containsKey(allayEntity.getId())) {
			light = ENTITY_LIGHTS.get(allayEntity.getId());
		} else {
			light = new PointLightData();
			light.setBrightness(0.5f);
			light.setColor(EffectiveUtils.getAllayColor(allayEntity));
			light.setRadius(3f);
			light.setPosition(renderPosition.getX(), renderPosition.getY(), renderPosition.getZ());

			ENTITY_LIGHTS.put(allayEntity.getId(), light);
			ENTITY_LIGHT_HANDLES.put(allayEntity.getId(), VeilRenderSystem.renderer().getLightRenderer().addLight(light));
		}

		light.setPosition(renderPosition.getX(), renderPosition.getY(), renderPosition.getZ());
	}
}
