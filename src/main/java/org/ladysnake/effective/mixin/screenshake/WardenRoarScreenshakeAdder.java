package org.ladysnake.effective.mixin.screenshake;

import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.warden.WardenEntity;
import net.minecraft.world.World;
import org.ladysnake.effective.EffectiveConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.lodestar.lodestone.handlers.ScreenshakeHandler;
import team.lodestar.lodestone.systems.rendering.particle.Easing;
import team.lodestar.lodestone.systems.screenshake.PositionedScreenshakeInstance;
import team.lodestar.lodestone.systems.screenshake.ScreenshakeInstance;

@Mixin(WardenEntity.class)
public class WardenRoarScreenshakeAdder extends HostileEntity {
	public ScreenshakeInstance roarScreenShake;
	public int ticksSinceAnimationStart = 0;

	protected WardenRoarScreenshakeAdder(EntityType<? extends HostileEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "tick", at = @At("HEAD"))
	public void tick(CallbackInfo ci) {
		if (EffectiveConfig.wardenScreenShake && this.getPose().equals(EntityPose.ROARING)) {
			ticksSinceAnimationStart++;
			if (roarScreenShake == null) {
				if (ticksSinceAnimationStart >= 20) {
					roarScreenShake = new PositionedScreenshakeInstance(70, this.getPos(), 20f, 0f, 25f, Easing.CIRC_IN_OUT).setIntensity(0.0f, EffectiveConfig.screenShakeIntensity, 0.0f);
					ScreenshakeHandler.addScreenshake(roarScreenShake);
				}
			}
		} else {
			roarScreenShake = null;
			ticksSinceAnimationStart = 0;
		}
	}


}
