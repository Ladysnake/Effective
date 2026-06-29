package org.ladysnake.effective.mixin.lights;

import net.minecraft.client.particle.ParticleManager;
import org.ladysnake.effective.index.EffectiveLights;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticleManager.class)
public class ParticleManagerMixin {
	@Inject(method = "clearParticles", at = @At("HEAD"))
	private void effective$clearLightsOnClearParticles(CallbackInfo ci) {
		EffectiveLights.ENTITY_LIGHT_HANDLES.forEach((light, handle) -> handle.free());
		EffectiveLights.PARTICLE_LIGHTS.clear();
		EffectiveLights.ENTITY_LIGHT_HANDLES.clear();
	}
}
