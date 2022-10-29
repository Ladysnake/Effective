package ladysnake.effective.mixin.allays;


import com.sammy.ortus.systems.rendering.PositionTrackedEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

@Mixin(AllayEntity.class)
public abstract class AllayEntityMixin extends PathAwareEntity implements PositionTrackedEntity {
	public final ArrayList<Vec3d> pastPositions = new ArrayList<>();

	protected AllayEntityMixin(EntityType<? extends PathAwareEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "tick", at = @At("HEAD"))
	public void tick(CallbackInfo ci) {
		trackPastPositions();
	}

	public void trackPastPositions() {
		Vec3d position = this.getCameraPosVec(MinecraftClient.getInstance().getTickDelta()).add(0f, -.2f, 0f);
		if (!pastPositions.isEmpty()) {
			Vec3d latest = pastPositions.get(pastPositions.size() - 1);
			float distance = (float) latest.distanceTo(position);
			if (distance > 0.1f) {
				pastPositions.add(position);
			}
			int excess = pastPositions.size() - 1;
			ArrayList<Vec3d> toRemove = new ArrayList<>();
			float efficiency = (float) (excess * 0.12f + Math.exp((Math.max(0, excess - 20)) * 0.2f));
			float ratio = 0.1f;
			if (efficiency > 0f) {
				for (int i = 0; i < excess; i++) {
					Vec3d excessPosition = pastPositions.get(i);
					Vec3d nextExcessPosition = pastPositions.get(i + 1);
					pastPositions.set(i, excessPosition.lerp(nextExcessPosition, Math.min(1, ratio * (excess - i) * (ratio + efficiency))));
					float excessDistance = (float) excessPosition.distanceTo(nextExcessPosition);
					if (excessDistance < 0.05f) {
						toRemove.add(pastPositions.get(i));
					}
				}
				pastPositions.removeAll(toRemove);
			}
		} else {
			pastPositions.add(position);
		}
	}

	@Override
	public ArrayList<Vec3d> getPastPositions() {
		return pastPositions;
	}
}
