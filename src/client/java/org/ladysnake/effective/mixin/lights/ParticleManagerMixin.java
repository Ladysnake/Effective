package org.ladysnake.effective.mixin.lights;

import foundry.veil.api.client.render.VeilRenderSystem;
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
		EffectiveLights.PARTICLE_LIGHTS.forEach(light -> VeilRenderSystem.renderer().getLightRenderer().removeLight(light));
		EffectiveLights.PARTICLE_LIGHTS.clear();
	}
}
