package org.ladysnake.effective.mixin.screenshake;

import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.phase.AbstractSittingPhase;
import net.minecraft.entity.boss.dragon.phase.SittingAttackingPhase;
import org.ladysnake.effective.EffectiveConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.lodestar.lodestone.handlers.ScreenshakeHandler;
import team.lodestar.lodestone.systems.rendering.particle.Easing;
import team.lodestar.lodestone.systems.screenshake.PositionedScreenshakeInstance;
import team.lodestar.lodestone.systems.screenshake.ScreenshakeInstance;

@Mixin(SittingAttackingPhase.class)
public abstract class EnderDragonRoarScreenshakeAdder extends AbstractSittingPhase {
	public EnderDragonRoarScreenshakeAdder(EnderDragonEntity enderDragonEntity) {
		super(enderDragonEntity);
	}

	@Inject(method = "clientTick", at = @At("HEAD"))
	public void clientTick(CallbackInfo ci) {
		if (EffectiveConfig.dragonScreenShake) {
			ScreenshakeInstance roarScreenShake = new PositionedScreenshakeInstance(60, this.dragon.getPos(), 20f, 0f, 25f, Easing.CIRC_IN_OUT).setIntensity(0.0f, EffectiveConfig.screenShakeIntensity, 0.0f);
			ScreenshakeHandler.addScreenshake(roarScreenShake);
		}
	}
}
