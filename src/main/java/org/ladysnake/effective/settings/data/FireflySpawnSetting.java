package org.ladysnake.effective.settings.data;

import java.awt.*;

public record FireflySpawnSetting(float spawnChance,
								  Color color) { // spawn chance being the chance percent of a firefly spawning per tick
}
