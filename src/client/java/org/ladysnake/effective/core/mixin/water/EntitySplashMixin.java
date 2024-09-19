package org.ladysnake.effective.core.mixin.water;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.ladysnake.effective.core.EffectiveConfig;
import org.ladysnake.effective.core.world.SplashSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntitySplashMixin {
	@Shadow
	private World world;

	@Inject(method = "onSwimmingStart", at = @At("TAIL"))
	protected void onSwimmingStart(CallbackInfo callbackInfo) {
		if (this.world.isClient && EffectiveConfig.splashes) {
			SplashSpawner.trySpawnSplash((Entity) (Object) this);
		}
	}
}
