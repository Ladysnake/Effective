package org.ladysnake.effective.mixin.screenshake;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.RavagerEntity;
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

@Mixin(RavagerEntity.class)
public class RavagerRoarScreenshakeAdder extends HostileEntity {
	protected RavagerRoarScreenshakeAdder(EntityType<? extends HostileEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "roar", at = @At("HEAD"))
	public void roar(CallbackInfo ci) {
		if (EffectiveConfig.ravagerScreenShake) {
			ScreenshakeInstance roarScreenShake = new PositionedScreenshakeInstance(10, this.getPos(), 20f, 0f, 25f, Easing.CIRC_IN_OUT).setIntensity(0.0f, EffectiveConfig.screenShakeIntensity, 0.0f);
			ScreenshakeHandler.addScreenshake(roarScreenShake);
		}
	}
}
