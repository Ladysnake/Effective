package org.ladysnake.effective.mixin.screenshake;

import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.SonicBoomTask;
import net.minecraft.entity.mob.warden.WardenEntity;
import net.minecraft.server.world.ServerWorld;
import org.ladysnake.effective.EffectiveConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.lodestar.lodestone.handlers.ScreenshakeHandler;
import team.lodestar.lodestone.systems.rendering.particle.Easing;
import team.lodestar.lodestone.systems.screenshake.PositionedScreenshakeInstance;
import team.lodestar.lodestone.systems.screenshake.ScreenshakeInstance;

@Mixin(SonicBoomTask.class)
public class SonicBoomScreenshakeAdder {
	@Inject(method = "keepRunning(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/mob/warden/WardenEntity;J)V", at = @At("HEAD"))
	protected void keepRunning(ServerWorld serverWorld, WardenEntity wardenEntity, long l, CallbackInfo ci) {
		if (EffectiveConfig.sonicBoomScreenShake && !wardenEntity.getBrain().hasMemoryModule(MemoryModuleType.SONIC_BOOM_SOUND_DELAY)
			&& !wardenEntity.getBrain().hasMemoryModule(MemoryModuleType.SONIC_BOOM_SOUND_COOLDOWN)) {
			wardenEntity.getBrain()
				.getOptionalMemory(MemoryModuleType.ATTACK_TARGET)
				.filter(wardenEntity::isEnemy)
				.filter(livingEntity -> wardenEntity.isInRange(livingEntity, 15.0, 20.0))
				.ifPresent(livingEntity -> {
					ScreenshakeInstance boomScreenShake = new PositionedScreenshakeInstance(20, wardenEntity.getPos(), 20f, 0f, 25f, Easing.CIRC_IN_OUT).setIntensity(EffectiveConfig.screenShakeIntensity, 0.0f, 0.0f);
					ScreenshakeHandler.addScreenshake(boomScreenShake);
				});
		}
	}
}
