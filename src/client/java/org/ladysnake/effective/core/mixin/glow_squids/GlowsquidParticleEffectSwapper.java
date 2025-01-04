package org.ladysnake.effective.core.mixin.glow_squids;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.GlowSquidEntity;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.world.World;
import org.ladysnake.effective.core.EffectiveConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GlowSquidEntity.class)
public class GlowsquidParticleEffectSwapper extends SquidEntity {
	public GlowsquidParticleEffectSwapper(EntityType<? extends SquidEntity> entityType, World world) {
		super(entityType, world);
	}

	@WrapOperation(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V"))
	private void effective$swapGlowsquidParticles(World world, ParticleEffect particleEffect, double x, double y, double z, double velX, double velY, double velZ, Operation<Void> voidOperation) {
		if (EffectiveConfig.improvedGlowSquidParticles) {
			if (random.nextInt(5) == 0) {
				float spreadDivider = 1.2f;

//				WorldParticleBuilder.create(Effective.ALLAY_TWINKLE)
//					.enableForcedSpawn()
//					.setColorData(ColorParticleData.create(new Color(0x00FFAA), new Color(0x51FFFF)).build())
//					.setScaleData(GenericParticleData.create(0.01f, .2f + random.nextFloat() / 10f).setEasing(Easing.SINE_OUT).build())
//					.setLifetime(40)
//					.spawn(this.getWorld(), this.getClientCameraPosVec(MinecraftClient.getInstance().getRenderTime()).x + this.getRandom().nextGaussian() / spreadDivider, this.getClientCameraPosVec(MinecraftClient.getInstance().getRenderTime()).y - 0.2f + this.getRandom().nextGaussian() / spreadDivider, this.getClientCameraPosVec(MinecraftClient.getInstance().getRenderTime()).z + this.getRandom().nextGaussian() / spreadDivider);
			}
		} else {
			voidOperation.call(world, particleEffect, x, y, z, velX, velY, velZ);
		}
	}
}
