package org.ladysnake.effective.mixin.fireballs;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import org.ladysnake.effective.EffectiveConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class FireballVisualFireDisabler {
	@Inject(method = "doesRenderOnFire", at = @At("HEAD"), cancellable = true)
	protected void doesRenderOnFire(CallbackInfoReturnable<Boolean> cir) {
		if (EffectiveConfig.improvedFireballs && (Object) this instanceof AbstractFireballEntity) {
			cir.setReturnValue(false);
		}
	}
}
