package ladysnake.illuminations.client.enums;

public enum FireflySpawnRate {
	DISABLE(0f),
	LOW(0.00002f),
	MEDIUM(0.00010f),
	HIGH(0.00025f);

	public final float spawnRate;

	FireflySpawnRate(float spawnRate) {
		this.spawnRate = spawnRate;
	}
}
