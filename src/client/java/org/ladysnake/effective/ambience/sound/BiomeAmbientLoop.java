package org.ladysnake.effective.ambience.sound;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.MathHelper;
import org.ladysnake.effective.core.EffectiveConfig;

public class BiomeAmbientLoop extends MovingSoundInstance {
	private static final int TRANSITION_TIME = 100;
	private final ClientPlayerEntity player;
	private int transitionTimer;
	private final AmbientCondition ambientConditions;

	public BiomeAmbientLoop(ClientPlayerEntity player, SoundEvent ambientSound, AmbientCondition ambientConditions) {
		super(ambientSound, SoundCategory.AMBIENT, SoundInstance.createRandom());
		this.player = player;
		this.repeat = true;
		this.repeatDelay = 0;
		this.volume = 0.001F;
		this.relative = true;
		this.ambientConditions = ambientConditions;
	}

	@Override
	public void tick() {
		final float windVolume = EffectiveConfig.windAmbienceVolume / 100f;
		final float waterVolume = EffectiveConfig.waterAmbienceVolume / 100f;
		final float foliageVolume = EffectiveConfig.foliageAmbienceVolume / 100f;
		final float animalVolume = EffectiveConfig.animalAmbienceVolume / 100f;

		final float volumeAdjustor = switch (this.ambientConditions.type()) {
			case WIND -> windVolume;
			case ANIMAL -> animalVolume;
			case FOLIAGE -> foliageVolume;
			case WATER -> waterVolume;
		};

		ClientWorld world = MinecraftClient.getInstance().world;
		if (world != null && !this.player.isRemoved() && !this.player.isSubmergedInWater() && this.transitionTimer >= 0 && volumeAdjustor > 0) {
			this.transitionTimer = Math.min(this.transitionTimer + (this.ambientConditions.predicate().shouldPlay(this.player.getWorld(), this.player.getBlockPos(), this.player) ? 1 : -1), TRANSITION_TIME);
			this.volume = MathHelper.clamp((float) this.transitionTimer / (float) TRANSITION_TIME, 0.0F, volumeAdjustor);
		} else {
			this.setDone();
		}
	}
}
