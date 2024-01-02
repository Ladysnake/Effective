package org.ladysnake.effective.cosmetics.enums;

public enum GlowwormSpawnRate {
	DISABLE(0f), LOW(0.00004f), MEDIUM(0.00020f), HIGH(0.00050f);

	public final float spawnRate;

	GlowwormSpawnRate(float spawnRate) {
		this.spawnRate = spawnRate;
	}
}
