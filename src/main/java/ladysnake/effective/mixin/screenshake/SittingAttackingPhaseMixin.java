package ladysnake.effective.mixin.screenshake;

import com.sammy.ortus.handlers.ScreenshakeHandler;
import com.sammy.ortus.systems.rendering.particle.Easing;
import com.sammy.ortus.systems.screenshake.PositionedScreenshakeInstance;
import com.sammy.ortus.systems.screenshake.ScreenshakeInstance;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.phase.AbstractSittingPhase;
import net.minecraft.entity.boss.dragon.phase.SittingAttackingPhase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SittingAttackingPhase.class)
public abstract class SittingAttackingPhaseMixin extends AbstractSittingPhase {
	public SittingAttackingPhaseMixin(EnderDragonEntity enderDragonEntity) {
		super(enderDragonEntity);
	}

	@Inject(method = "clientTick", at = @At("HEAD"))
	public void clientTick(CallbackInfo ci) {
		ScreenshakeInstance roarScreenShake = new PositionedScreenshakeInstance(60, this.dragon.getPos(), 20f, 0f, 25f, Easing.CIRC_IN_OUT).setIntensity(0.0f, 1.0f, 0.0f);
		ScreenshakeHandler.addScreenshake(roarScreenShake);
	}
}
