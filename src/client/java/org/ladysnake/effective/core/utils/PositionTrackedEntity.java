package org.ladysnake.effective.core.utils;

import team.lodestar.lodestone.systems.rendering.trail.TrailPoint;

import java.util.List;

public interface PositionTrackedEntity {
	List<TrailPoint> getPastPositions();
}
