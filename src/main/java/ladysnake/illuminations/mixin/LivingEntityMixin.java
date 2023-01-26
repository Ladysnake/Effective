package ladysnake.illuminations.mixin;

import ladysnake.illuminations.client.Illuminations;
import ladysnake.illuminations.client.config.Config;
import ladysnake.illuminations.client.enums.HalloweenFeatures;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.time.LocalDate;
import java.time.Month;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Shadow
	public abstract boolean isUndead();

	@Inject(method = "onDeath", at = @At("RETURN"))
	public void onDeath(DamageSource source, CallbackInfo callbackInfo) {
		if (this.isUndead() && random.nextInt(5) == 0 && Illuminations.isNightTime(world) && ((Config.getHalloweenFeatures() == HalloweenFeatures.ENABLE && LocalDate.now().getMonth() == Month.OCTOBER) || Config.getHalloweenFeatures() == HalloweenFeatures.ALWAYS)) {
			this.world.playSound(null, this.getBlockPos(), SoundEvents.ENTITY_VEX_CHARGE, SoundCategory.AMBIENT, 1.0f, 0.8f);

			world.addParticle(Illuminations.POLTERGEIST, true, this.getX() + 0.5, this.getEyeY(), this.getZ(), 0f, 0f, 0f);
		}
	}
}
