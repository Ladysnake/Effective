package org.ladysnake.effective.lights;

import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.client.render.deferred.light.PointLight;
import foundry.veil.platform.VeilEventPlatform;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class EffectiveLights implements ClientModInitializer {
	public static final Map<Item, PointLight> LIGHT_SOURCES = new Object2ObjectOpenHashMap<>();

	public static Map<Integer, PointLight> heldLights = new Object2ObjectOpenHashMap<>();

	public static PointLight getHeldLight(LivingEntity livingEntity) {
		if (livingEntity != null) {
			for (Map.Entry<Item, PointLight> entry : LIGHT_SOURCES.entrySet()) {
				Item item = entry.getKey();
				PointLight pointLight = entry.getValue();
				if (livingEntity.isHolding(item)) {
					return pointLight;
				}
			}
		}

		return null;
	}

	@Override
	public void onInitializeClient() {
		// initialize light source list
		LIGHT_SOURCES.put(Blocks.SEA_LANTERN.asItem(), (PointLight) (new PointLight()).setBrightness(1f).setRadius(15f).setColor(0xD5E4DF));
		LIGHT_SOURCES.put(Blocks.TORCH.asItem(), (PointLight) (new PointLight()).setBrightness(1f).setRadius(15f).setColor(0xF9C966));
		LIGHT_SOURCES.put(Blocks.LANTERN.asItem(), (PointLight) (new PointLight()).setBrightness(1f).setRadius(15f).setColor(0xF9C966));
		LIGHT_SOURCES.put(Blocks.SOUL_TORCH.asItem(), (PointLight) (new PointLight()).setBrightness(0.7f).setRadius(10f).setColor(0x74F1F5));
		LIGHT_SOURCES.put(Blocks.SOUL_LANTERN.asItem(), (PointLight) (new PointLight()).setBrightness(0.7f).setRadius(10f).setColor(0x74F1F5));

		// remove lights upon livingEntity disconnection
		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> client.execute(() -> {
			heldLights.clear();
		}));

		// render the client's held light
		VeilEventPlatform.INSTANCE.onVeilRenderTypeStageRender((stage, levelRenderer, bufferSource, poseStack, projectionMatrix, renderTick, partialTicks, camera, frustum) -> {

			// load living entity held lights
			for (Entity entity : MinecraftClient.getInstance().world.getEntities()) {
				if (entity instanceof LivingEntity livingEntity) {
					PointLight playerHeldLight = EffectiveLights.getHeldLight(livingEntity);
					if (playerHeldLight != null) {
						Vec3d cameraPosVec = livingEntity.getCameraPosVec(partialTicks).subtract(livingEntity.getRotationVec(partialTicks).multiply(.3f));

						if (!heldLights.containsKey(livingEntity.getId())) {
							PointLight playerLight = playerHeldLight.clone();
							playerLight.setPosition(cameraPosVec.getX(), cameraPosVec.getY(), cameraPosVec.getZ());
							heldLights.put(livingEntity.getId(), playerLight);
							VeilRenderSystem.renderer().getDeferredRenderer().getLightRenderer().addLight(playerLight);
						}

						heldLights.get(livingEntity.getId()).setPosition(cameraPosVec.getX(), cameraPosVec.getY(), cameraPosVec.getZ());
					}
				}
			}

			// clean held lights list of missing living entities
			ArrayList<Integer> heldLightsToRemove = new ArrayList<>();
			for (int id : heldLights.keySet()) {
				Entity entityById = MinecraftClient.getInstance().world.getEntityById(id);
				if (entityById instanceof LivingEntity livingEntity) {
					PointLight lightThatShouldBe = EffectiveLights.getHeldLight(livingEntity);
					PointLight lightThatCurrentlyIs = heldLights.get(id);
					if (lightThatShouldBe == null) {
						heldLightsToRemove.add(id);
						continue;
					}
					if (!(lightThatCurrentlyIs.getBrightness() == lightThatShouldBe.getBrightness() && lightThatCurrentlyIs.getColor().equals(lightThatShouldBe.getColor()))) {
						heldLightsToRemove.add(id);
					}
				}
			}
			for (int id : heldLightsToRemove) {
				if (heldLights.get(id) != null) {
					VeilRenderSystem.renderer().getDeferredRenderer().getLightRenderer().removeLight(heldLights.get(id));
					heldLights.remove(id);
				}
			}
		});
	}
}
