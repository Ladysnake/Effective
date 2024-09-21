package org.ladysnake.effective.ambience.mixin;

import com.mojang.authlib.GameProfile;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.Sound;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.world.ClientWorld;
import org.ladysnake.effective.ambience.EffectiveAmbience;
import org.ladysnake.effective.ambience.EffectiveAmbienceSounds;
import org.ladysnake.effective.ambience.sound.AmbientCondition;
import org.ladysnake.effective.ambience.sound.BiomeAmbientLoop;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class AmbienceClientPlayerEntityMixin extends AbstractClientPlayerEntity {
	@Shadow
	@Final
	protected MinecraftClient client;

	public AmbienceClientPlayerEntityMixin(ClientWorld world, GameProfile gameProfile) {
		super(world, gameProfile);
	}

	@Shadow
	public abstract boolean isSubmergedInWater();

	@Inject(method = "tick", at = @At(value = "HEAD"))
	private void effective$playAmbience(CallbackInfo ci) {
		if ((Object) this instanceof ClientPlayerEntity clientPlayerEntity) {
			for (AmbientCondition ambientCondition : EffectiveAmbience.AMBIENT_CONDITIONS) {
				if (ambientCondition.shouldPlay(clientPlayerEntity)) {
					SoundInstance ambience = EffectiveAmbience.AMBIENCES.get(ambientCondition);
					if (ambience == null || !this.client.getSoundManager().isPlaying(ambience)) {
						ambience = new BiomeAmbientLoop(clientPlayerEntity, ambientCondition.sound(), ambientCondition);
						this.client.getSoundManager().play(ambience);
						EffectiveAmbience.AMBIENCES.put(ambientCondition, ambience);
					}
				}
			}
		}
	}
}
