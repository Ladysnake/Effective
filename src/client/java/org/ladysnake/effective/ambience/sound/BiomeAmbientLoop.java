package org.ladysnake.effective.ambience.sound;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.ladysnake.effective.ambience.EffectiveAmbience;

public class BiomeAmbientLoop extends MovingSoundInstance {
	private static final int TRANSITION_TIME = 40;
	private final ClientPlayerEntity player;
	private int transitionTimer;
	private AmbientCondition ambientConditions;

	public BiomeAmbientLoop(ClientPlayerEntity player, SoundEvent ambientSound, AmbientCondition ambientConditions) {
		super(ambientSound, SoundCategory.AMBIENT, SoundInstance.createRandom());
		this.player = player;
		this.repeat = true;
		this.repeatDelay = 0;
		this.volume = 1.0F;
		this.relative = true;
		this.ambientConditions = ambientConditions;
	}

	@Override
	public void tick() {
		ClientWorld world = MinecraftClient.getInstance().world;
		if (world != null && !this.player.isRemoved() && this.transitionTimer >= 0) {
			this.transitionTimer = Math.min(this.transitionTimer + (this.ambientConditions.shouldPlay(this.player) ? 1 : -1), TRANSITION_TIME);
			this.volume = Math.max(0.0F, Math.min((float) this.transitionTimer / (float) TRANSITION_TIME, 1.0F));
		} else {
			this.setDone();
			EffectiveAmbience.AMBIENCES.remove(this.ambientConditions);
		}
	}
}
