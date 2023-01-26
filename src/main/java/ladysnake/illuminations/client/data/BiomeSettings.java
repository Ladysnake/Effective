package ladysnake.illuminations.client.data;

import ladysnake.illuminations.client.enums.FireflySpawnRate;
import ladysnake.illuminations.client.enums.GlowwormSpawnRate;
import ladysnake.illuminations.client.enums.PlanktonSpawnRate;

public record BiomeSettings(FireflySpawnRate fireflySpawnRate,
							int fireflyColor,
							GlowwormSpawnRate glowwormSpawnRate,
							PlanktonSpawnRate planktonSpawnRate) {

	public BiomeSettings withFireflySpawnRate(FireflySpawnRate value) {
		return new BiomeSettings(value, fireflyColor, glowwormSpawnRate, planktonSpawnRate);
	}

	public BiomeSettings withFireflyColor(int value) {
		return new BiomeSettings(fireflySpawnRate, value, glowwormSpawnRate, planktonSpawnRate);
	}

	public BiomeSettings withGlowwormSpawnRate(GlowwormSpawnRate value) {
		return new BiomeSettings(fireflySpawnRate, fireflyColor, value, planktonSpawnRate);
	}

	public BiomeSettings withPlanktonSpawnRate(PlanktonSpawnRate value) {
		return new BiomeSettings(fireflySpawnRate, fireflyColor, glowwormSpawnRate, value);
	}
}
