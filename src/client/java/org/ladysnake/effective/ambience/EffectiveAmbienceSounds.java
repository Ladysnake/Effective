package org.ladysnake.effective.ambience;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import org.ladysnake.effective.core.Effective;

import java.util.LinkedList;
import java.util.List;

public class EffectiveAmbienceSounds {
	protected static final List<SoundEvent> SOUND_EVENTS = new LinkedList<>();

	/* SURFACE */
	public static final SoundEvent ANIMAL_BEES = create("ambient.animal.bees");
	public static final SoundEvent ANIMAL_BIRDS = create("ambient.animal.birds");
	public static final SoundEvent ANIMAL_CICADAS = create("ambient.animal.cicadas");
	public static final SoundEvent ANIMAL_CRICKETS = create("ambient.animal.crickets");
	public static final SoundEvent ANIMAL_FROGS_AND_CRICKETS = create("ambient.animal.frogs_and_crickets");
	public static final SoundEvent ANIMAL_JUNGLE_DAY = create("ambient.animal.jungle_day");
	public static final SoundEvent ANIMAL_JUNGLE_NIGHT = create("ambient.animal.jungle_night");
	public static final SoundEvent ANIMAL_MANGROVE_BIRDS = create("ambient.animal.mangrove_birds");
	public static final SoundEvent ANIMAL_OWLS = create("ambient.animal.owls");
	public static final SoundEvent FOLIAGE_CAVE_LEAVES = create("ambient.foliage.cave_leaves");
	public static final SoundEvent FOLIAGE_LEAVES = create("ambient.foliage.leaves");
	public static final SoundEvent WATER_DRIPSTONE_CAVES = create("ambient.water.dripstone_caves");
	public static final SoundEvent WATER_LUSH_CAVES = create("ambient.water.lush_caves");
	public static final SoundEvent WATER_RIVER = create("ambient.water.river");
	public static final SoundEvent WATER_WAVES = create("ambient.water.waves");
	public static final SoundEvent WIND_ARID = create("ambient.wind.arid");
	public static final SoundEvent WIND_CAVE = create("ambient.wind.cave");
	public static final SoundEvent WIND_COLD = create("ambient.wind.cold");
	public static final SoundEvent WIND_DEEP_DARK = create("ambient.wind.deep_dark");
	public static final SoundEvent WIND_END = create("ambient.wind.end");
	public static final SoundEvent WIND_MOUNTAINS = create("ambient.wind.mountains");
	public static final SoundEvent WIND_TEMPERATE = create("ambient.wind.temperate");

	protected static SoundEvent create(String name) {
		SoundEvent soundEvent = SoundEvent.of(Effective.id(name));
		SOUND_EVENTS.add(soundEvent);
		return soundEvent;
	}

	public static void initialize() {
		SOUND_EVENTS.forEach(soundEvent -> Registry.register(Registries.SOUND_EVENT, soundEvent.getId(), soundEvent));
	}
}
