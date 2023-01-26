package ladysnake.illuminations.client.enums;

public enum PlanktonSpawnRate {
	DISABLE(0f), LOW(0.00020f), MEDIUM(0.00100f), HIGH(0.00250f);

	public final float spawnRate;

	PlanktonSpawnRate(float spawnRate) {
		this.spawnRate = spawnRate;
	}
}
