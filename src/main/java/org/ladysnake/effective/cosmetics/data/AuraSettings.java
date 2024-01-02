package org.ladysnake.effective.cosmetics.data;

public record AuraSettings(float spawnRate, int delay) {

	public AuraSettings withSpawnRate(float value) {
		return new AuraSettings(value, delay);
	}

	public AuraSettings withDelay(int value) {
		return new AuraSettings(spawnRate, value);
	}
}
