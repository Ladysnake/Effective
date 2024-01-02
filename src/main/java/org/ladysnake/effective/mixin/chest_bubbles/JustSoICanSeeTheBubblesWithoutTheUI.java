package org.ladysnake.effective.mixin.chest_bubbles;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.item.Items;
import org.quiltmc.loader.api.QuiltLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GenericContainerScreen.class)
public class JustSoICanSeeTheBubblesWithoutTheUI {
	@Inject(method = "render", at = @At("HEAD"), cancellable = true)
	public void onUse(GuiGraphics graphics, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		if (QuiltLoader.isDevelopmentEnvironment() && MinecraftClient.getInstance().player.getMainHandStack().isOf(Items.HEART_OF_THE_SEA)) {
			ci.cancel();
		}
	}
}
