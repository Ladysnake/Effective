package org.ladysnake.effective.mixin.fireballs;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.entity.projectile.DragonFireballEntity;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.world.World;
import org.ladysnake.effective.EffectiveConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ExplosiveProjectileEntity.class)
public class FireballSmokeDisabler {
	@WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V", ordinal = 1))
	private void effective$disableSmokeParticles(World world, ParticleEffect particleEffect, double x, double y, double z, double velX, double velY, double velZ, Operation<Void> voidOperation) {
		if ((!EffectiveConfig.improvedFireballs && (Object) this instanceof AbstractFireballEntity) || (!EffectiveConfig.improvedDragonFireballsAndBreath && (Object) this instanceof DragonFireballEntity)) {
			voidOperation.call(world, particleEffect, x, y, z, velX, velY, velZ);
		}
	}
}
