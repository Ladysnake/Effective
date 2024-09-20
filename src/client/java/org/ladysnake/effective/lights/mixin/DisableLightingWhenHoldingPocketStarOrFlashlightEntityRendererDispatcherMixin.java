package org.ladysnake.effective.lights.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.ladysnake.effective.lights.EffectiveLights;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityRenderDispatcher.class)
public class DisableLightingWhenHoldingPocketStarOrFlashlightEntityRendererDispatcherMixin {
	@ModifyReturnValue(method = "getLight", at = @At("RETURN"))
	public <E extends Entity> int getLight(int original, E entity, float tickDelta) {
		if (entity instanceof PlayerEntity player) {
			if (EffectiveLights.getHeldLight(player) != null) {
				return LightmapTextureManager.MAX_LIGHT_COORDINATE;
			}
		}

		return original;
	}

}
