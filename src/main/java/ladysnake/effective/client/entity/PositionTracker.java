package ladysnake.effective.client.entity;

import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;

public interface PositionTracker {
	public void trackPastPositions();
	public ArrayList<Vec3d> getPastPositions();
}
