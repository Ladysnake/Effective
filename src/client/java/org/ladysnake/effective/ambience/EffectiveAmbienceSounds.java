package org.ladysnake.effective.ambience;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import org.ladysnake.effective.core.Effective;

import java.util.LinkedList;
import java.util.List;

public class EffectiveAmbienceSounds {
	protected static final List<SoundEvent> SOUND_EVENTS = new LinkedList<>();

	public static final SoundEvent AMBIENT_PLAINS_DAY = create("ambient.plains.day");
	public static final SoundEvent AMBIENT_BEACH = create("ambient.beach");
	public static final SoundEvent AMBIENT_DEEP_DARK = create("ambient.deep_dark");
	public static final SoundEvent CRICKETS = create("ambient.crickets");

	protected static SoundEvent create(String name) {
		SoundEvent soundEvent = SoundEvent.of(Effective.id(name));
		SOUND_EVENTS.add(soundEvent);
		return soundEvent;
	}

	public static void initialize() {
		SOUND_EVENTS.forEach(soundEvent -> Registry.register(Registries.SOUND_EVENT, soundEvent.getId(), soundEvent));
	}
}
