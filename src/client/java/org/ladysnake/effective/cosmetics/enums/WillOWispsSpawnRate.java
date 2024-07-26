package org.ladysnake.effective.cosmetics.enums;

public enum WillOWispsSpawnRate {
	DISABLE(0f), LOW(0.00002f), MEDIUM(0.00010f), HIGH(0.00025f);

	public final float spawnRate;

	WillOWispsSpawnRate(float spawnRate) {
		this.spawnRate = spawnRate;
	}
}
