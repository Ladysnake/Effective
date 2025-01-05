package org.ladysnake.effective.mixin.screenshake;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WardenEntity.class)
public abstract class WardenRoarScreenshakeAdder extends HostileEntity {
	//	public ScreenshakeInstance roarScreenShake;
//	public int ticksSinceAnimationStart = 0;
//
	protected WardenRoarScreenshakeAdder(EntityType<? extends HostileEntity> entityType, World world) {
		super(entityType, world);
	}
//
//	@Inject(method = "tick", at = @At("HEAD"))
//	public void tick(CallbackInfo ci) {
//		if (EffectiveConfig.wardenScreenShake && this.getPose().equals(EntityPose.ROARING)) {
//			ticksSinceAnimationStart++;
//			if (roarScreenShake == null) {
//				if (ticksSinceAnimationStart >= 20) {
//					roarScreenShake = new PositionedScreenshakeInstance(70, this.getPos(), 20f, 25f, Easing.CIRC_IN_OUT).setIntensity(0.0f, EffectiveConfig.screenShakeIntensity, 0.0f);
//					ScreenshakeHandler.addScreenshake(roarScreenShake);
//				}
//			}
//		} else {
//			roarScreenShake = null;
//			ticksSinceAnimationStart = 0;
//		}
//	}


}
