package org.ladysnake.effective.index;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import org.ladysnake.effective.Effective;

import java.util.LinkedList;
import java.util.List;

public interface EffectiveSounds {

	List<SoundEvent> SOUND_EVENTS = new LinkedList<>();

	SoundEvent ENTITY_PARRY = create("entity.parry");
	SoundEvent AMBIENT_WATERFALL = create("ambient.waterfall");
	SoundEvent AMBIENT_ANIMAL_BEES = create("ambient.animal.bees");
	SoundEvent AMBIENT_ANIMAL_BIRDS = create("ambient.animal.birds");
	SoundEvent AMBIENT_ANIMAL_CICADAS = create("ambient.animal.cicadas");
	SoundEvent AMBIENT_ANIMAL_CRICKETS = create("ambient.animal.crickets");
	SoundEvent AMBIENT_ANIMAL_FROGS_AND_CRICKETS = create("ambient.animal.frogs_and_crickets");
	SoundEvent AMBIENT_ANIMAL_JUNGLE_DAY = create("ambient.animal.jungle_day");
	SoundEvent AMBIENT_ANIMAL_JUNGLE_NIGHT = create("ambient.animal.jungle_night");
	SoundEvent AMBIENT_ANIMAL_MANGROVE_BIRDS = create("ambient.animal.mangrove_birds");
	SoundEvent AMBIENT_ANIMAL_OWLS = create("ambient.animal.owls");
	SoundEvent AMBIENT_FOLIAGE_CAVE_LEAVES = create("ambient.foliage.cave_leaves");
	SoundEvent AMBIENT_FOLIAGE_LEAVES = create("ambient.foliage.leaves");
	SoundEvent AMBIENT_WATER_DRIPSTONE_CAVES = create("ambient.water.dripstone_caves");
	SoundEvent AMBIENT_WATER_LUSH_CAVES = create("ambient.water.lush_caves");
	SoundEvent AMBIENT_WATER_RIVER = create("ambient.water.river");
	SoundEvent AMBIENT_WATER_WAVES = create("ambient.water.waves");
	SoundEvent AMBIENT_WIND_ARID = create("ambient.wind.arid");
	SoundEvent AMBIENT_WIND_CAVE = create("ambient.wind.cave");
	SoundEvent AMBIENT_WIND_COLD = create("ambient.wind.cold");
	SoundEvent AMBIENT_WIND_DEEP_DARK = create("ambient.wind.deep_dark");
	SoundEvent AMBIENT_WIND_END = create("ambient.wind.end");
	SoundEvent AMBIENT_WIND_MOUNTAINS = create("ambient.wind.mountains");
	SoundEvent AMBIENT_WIND_TEMPERATE = create("ambient.wind.temperate");

	static SoundEvent create(String name) {
		SoundEvent soundEvent = SoundEvent.of(Effective.id(name));
		SOUND_EVENTS.add(soundEvent);
		return soundEvent;
	}

	static BlockSoundGroup createBlockSoundGroup(String name, float volume, float pitch) {
		return new BlockSoundGroup(volume, pitch,
			create("block." + name + ".break"),
			create("block." + name + ".step"),
			create("block." + name + ".place"),
			create("block." + name + ".hit"),
			create("block." + name + ".fall"));
	}

	static BlockSoundGroup copyBlockSoundGroup(BlockSoundGroup blockSoundGroup, float volume, float pitch) {
		return new BlockSoundGroup(volume, pitch,
			blockSoundGroup.getBreakSound(),
			blockSoundGroup.getStepSound(),
			blockSoundGroup.getPlaceSound(),
			blockSoundGroup.getHitSound(),
			blockSoundGroup.getFallSound());
	}

	static void initialize() {
		SOUND_EVENTS.forEach(soundEvent -> Registry.register(Registries.SOUND_EVENT, soundEvent.getId(), soundEvent));
	}

}
