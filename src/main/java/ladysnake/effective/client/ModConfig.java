package ladysnake.effective.client;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = Effective.MODID)
public class ModConfig implements ConfigData {
	public boolean generateCascades = true;
	public boolean generateSplashes = true;
	public boolean shouldFlowingWaterSpawnParticlesOnFirstTick = true;
	public boolean canLapisBlocksForceWaterfallClouds = true;
}
