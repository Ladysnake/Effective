package org.ladysnake.effective.mixin.spectral_arrows;


import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.SpectralArrowEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.ladysnake.effective.EffectiveConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.lodestar.lodestone.systems.rendering.PositionTrackedEntity;

import java.util.ArrayList;

@Mixin(SpectralArrowEntity.class)
public abstract class SpectralArrowPositionTrackerAndParticleRemover extends PersistentProjectileEntity implements PositionTrackedEntity {
	public final ArrayList<Vec3d> pastPositions = new ArrayList<>();

	protected SpectralArrowPositionTrackerAndParticleRemover(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "tick", at = @At("HEAD"))
	public void tick(CallbackInfo ci) {
		if (EffectiveConfig.spectralArrowTrails != EffectiveConfig.TrailOptions.NONE) {
			trackPastPositions();
		}
	}

	public void trackPastPositions() {
		Vec3d position = this.getCameraPosVec(MinecraftClient.getInstance().getTickDelta()).add(0, -.1f, 0f);
		if (!pastPositions.isEmpty()) {
			Vec3d latest = pastPositions.get(pastPositions.size() - 1);
			float distance = (float) latest.distanceTo(position);
			if (distance > 0.1f) {
				pastPositions.add(position);
			}
			int excess = pastPositions.size() - 1;
			ArrayList<Vec3d> toRemove = new ArrayList<>();
			float efficiency = (float) (excess * 0.12f + Math.exp((Math.max(0, excess - 20)) * 0.2f));
			float ratio = 0.03f;
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

	@WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V"))
	public void tick(World world, ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ, Operation<Void> voidOperation) {
		if (EffectiveConfig.spectralArrowTrails == EffectiveConfig.TrailOptions.NONE) {
			voidOperation.call(world, parameters, x, y, z, velocityX, velocityY, velocityZ);
		}
	}
}
