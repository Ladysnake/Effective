package org.ladysnake.effective.core.index;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import org.ladysnake.effective.core.Effective;

import java.util.LinkedList;
import java.util.List;

public interface EffectiveSounds {

	List<SoundEvent> SOUND_EVENTS = new LinkedList<>();

	SoundEvent AMBIENCE_WATERFALL = create("ambience.waterfall");
	SoundEvent PARRY = create("entity.parry");

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
