package ladysnake.effective.client;

import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.util.math.Vec3d;

public class EffectiveUtils {
	public static boolean isGoingFast(AllayEntity allayEntity) {
		Vec3d velocity = allayEntity.getVelocity();
		float speedRequired = 0.1f;

		return (velocity.getX() >= speedRequired || velocity.getX() <= -speedRequired)
				|| (velocity.getY() >= speedRequired || velocity.getY() <= -speedRequired)
				|| (velocity.getZ() >= speedRequired || velocity.getZ() <= -speedRequired);
	}
}
