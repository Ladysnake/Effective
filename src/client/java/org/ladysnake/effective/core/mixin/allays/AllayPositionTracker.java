package org.ladysnake.effective.core.mixin.allays;


import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.world.World;
import org.ladysnake.effective.core.EffectiveConfig;
import org.ladysnake.effective.core.utils.PositionTrackedEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.lodestar.lodestone.systems.rendering.trail.TrailPoint;
import team.lodestar.lodestone.systems.rendering.trail.TrailPointBuilder;

import java.util.List;

@Mixin(AllayEntity.class)
public class AllayPositionTracker extends PathAwareEntity implements PositionTrackedEntity {
	@Unique
	public final TrailPointBuilder trailPointBuilder = TrailPointBuilder.create(16);

	protected AllayPositionTracker(EntityType<? extends PathAwareEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "tick", at = @At("HEAD"))
	public void tick(CallbackInfo ci) {
		if (EffectiveConfig.allayTrails != EffectiveConfig.TrailOptions.NONE) {
			trailPointBuilder.addTrailPoint(this.getPos().add(0, .2, 0));
			trailPointBuilder.tickTrailPoints();
		}
	}

	@Override
	public List<TrailPoint> getPastPositions() {
		return trailPointBuilder.getTrailPoints();
	}
}
