package org.ladysnake.effective.mixin.allays;

import net.minecraft.client.render.entity.AllayRenderer;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.util.Identifier;
import org.ladysnake.effective.Effective;
import org.ladysnake.effective.EffectiveConfig;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AllayRenderer.class)
public class GoldenAllayTextureSwapper {
	private static final Identifier GOLDEN_TEXTURE = new Identifier(Effective.MODID, "textures/entity/golden_allay.png");

	@Shadow
	@Final
	private static Identifier ALLAY_TEXTURE;

	@Inject(method = "getTexture(Lnet/minecraft/entity/passive/AllayEntity;)Lnet/minecraft/util/Identifier;", at = @At("RETURN"), cancellable = true)
	public void getTexture(AllayEntity allayEntity, CallbackInfoReturnable<Identifier> cir) {
		cir.setReturnValue(allayEntity.getUuid().hashCode() % 2 == 0 && EffectiveConfig.goldenAllays ? GOLDEN_TEXTURE : ALLAY_TEXTURE);
	}
}
