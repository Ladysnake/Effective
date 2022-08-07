package ladysnake.effective.client;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = Effective.MODID)
public class ModConfig implements ConfigData {
	public boolean generateCascades = true;
	public boolean generateSplashes = true;
	public boolean shouldFlowingWaterSpawnParticlesOnFirstTick = true;
	public boolean muteWaterfalls = false;
	@ConfigEntry.BoundedDiscrete(max = 100)
	public long lapisBlockUpdateParticleChance = 10;
}
