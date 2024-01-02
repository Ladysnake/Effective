package org.ladysnake.effective.world;

import net.minecraft.entity.passive.GlowSquidEntity;

import java.util.HashSet;
import java.util.Set;

public class RenderedHypnotizingEntities {
	public static Set<GlowSquidEntity> GLOWSQUIDS = new HashSet<>();
	public static double lookIntensity = 0f;
	public static double lookIntensityGoal = 0f;
	public static int lockedIntensityTimer = 0;
}
