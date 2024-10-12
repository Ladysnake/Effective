package org.ladysnake.effective.ambience.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.TickableSoundInstance;
import net.minecraft.client.world.ClientWorld;
import org.ladysnake.effective.ambience.EffectiveAmbience;
import org.ladysnake.effective.ambience.sound.AmbientCondition;
import org.ladysnake.effective.ambience.sound.BiomeAmbientLoop;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public class AmbienceClientWorldMixin {
	@Shadow
	@Final
	private MinecraftClient client;

	@Inject(method = "tick", at = @At(value = "HEAD"))
	private void effective$playAmbience(CallbackInfo ci) {
		ClientPlayerEntity clientPlayerEntity = client.player;
		if (clientPlayerEntity != null) {
			for (AmbientCondition ambientCondition : EffectiveAmbience.AMBIENT_CONDITIONS) {
				if (ambientCondition.predicate().shouldPlay(client.world, client.player.getBlockPos(), client.player)) {
					boolean allow = true;
					for (TickableSoundInstance tickingSound : client.getSoundManager().soundSystem.tickingSounds) {
						if (tickingSound.getId().equals(ambientCondition.event().getId())) {
							allow = false;
							break;
						}
					}
					if (allow) {
						client.getSoundManager().play(new BiomeAmbientLoop(clientPlayerEntity, ambientCondition.event(), ambientCondition));
					}
				}
			}
		}
	}
}
