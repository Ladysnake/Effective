package ladysnake.illuminations.client.enums;

public enum EyesInTheDarkSpawnRate {
	LOW(0.00002f), MEDIUM(0.00010f), HIGH(0.00025f);

	public final float spawnRate;

	EyesInTheDarkSpawnRate(float spawnRate) {
		this.spawnRate = spawnRate;
	}
}
