package org.ladysnake.effective.mixin.fireballs;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AreaEffectCloudEntity.class)
public abstract class DragonBreathRendererSwapper extends Entity {
	public DragonBreathRendererSwapper(EntityType<?> type, World world) {
		super(type, world);
	}

	@WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addImportantParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V"))
	private void effective$swapDragonBreathParticles(World world, ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ, Operation<Void> voidOperation) {
//		if (EffectiveConfig.improvedDragonFireballsAndBreath && parameters == ParticleTypes.DRAGON_BREATH) {
//			WorldParticleBuilder.create(Effective.DRAGON_BREATH)
//				.enableForcedSpawn()
//				.setSpinData(SpinParticleData.create((float) (world.EffectiveUtils.getRandomFloatOrNegative(this.random) / 5f)).build())
//				.setScaleData(GenericParticleData.create(1f, 0f).setEasing(Easing.CIRC_OUT).build())
//				.setTransparencyData(GenericParticleData.create(0.2f).build())
//				.setColorData(
//					ColorParticleData.create(new Color(0xD21EFF), new Color(0x7800FF))
//						.setEasing(Easing.CIRC_OUT)
//						.build()
//				)
//				.enableNoClip()
//				.setLifetime(40)
//				.setMotion((float) velocityX, (float) velocityY + 0.05f, (float) velocityZ)
//				.spawn(world, x, y, z);
//		} else {
//			voidOperation.call(world, parameters, x, y, z, velocityX, velocityY, velocityZ);
//		}
	}
}
