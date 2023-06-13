package ladysnake.illuminations.mixin;

import ladysnake.illuminations.client.Illuminations;
import ladysnake.illuminations.client.data.AuraData;
import ladysnake.illuminations.client.data.PlayerCosmeticData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "tick", at = @At("RETURN"))
	public void tick(CallbackInfo callbackInfo) {
		PlayerCosmeticData cosmeticData = Illuminations.getCosmeticData((PlayerEntity) (Object) this);
		// if player has cosmetics
		if (cosmeticData != null) {
			// player aura
			String playerAura = cosmeticData.getAura();
			if (Config.shouldDisplayCosmetics() && playerAura != null && Illuminations.AURAS_DATA.containsKey(playerAura)) {
				// do not render in first person or if the player is invisible
				// noinspection ConstantConditions
				if (((Config.getViewAurasFP() || MinecraftClient.getInstance().gameRenderer.getCamera().isThirdPerson()) || MinecraftClient.getInstance().player != (Object) this) && !this.isInvisible()) {
					if (Illuminations.AURAS_DATA.containsKey(playerAura)) {
						AuraData aura = Illuminations.AURAS_DATA.get(playerAura);
						if (Illuminations.AURAS_DATA.get(playerAura).shouldAddParticle(this.random, this.age)) {
							world.addParticle(aura.particle(), this.getX(), this.getY(), this.getZ(), 0, 0, 0);
						}
					}
				}
			}

			// player pet
			String playerPet = cosmeticData.getPet();
			if (Config.shouldDisplayCosmetics() && playerPet != null && Illuminations.PETS_DATA.containsKey(playerPet)) {
				// do not render in first person or if the player is invisible
				//noinspection ConstantConditions
				if (((Config.getViewAurasFP() || MinecraftClient.getInstance().gameRenderer.getCamera().isThirdPerson()) || MinecraftClient.getInstance().player != (Object) this) && !this.isInvisible()) {
					if (Illuminations.PETS_DATA.containsKey(playerPet)) {
						DefaultParticleType overhead = Illuminations.PETS_DATA.get(playerPet);
						if (this.age % 20 == 0) {
							world.addParticle(overhead, this.getX() + Math.cos(this.bodyYaw / 50) * 0.5, this.getY() + this.getHeight() + 0.5f + Math.sin(this.age / 12f) / 12f, this.getZ() - Math.cos(this.bodyYaw / 50) * 0.5, 0, 0, 0);
						}
					}
				}
			}
		}
	}
}
