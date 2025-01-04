package org.ladysnake.effective.core.mixin;

import net.minecraft.client.gl.GlDebug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GlDebug.class)
public class GlDebugMixin {
	@Inject(method = "info", at = @At(value = "HEAD"), cancellable = true)
	private static void effective$dudeStopFuckingSpammingMyConsoleWithDebugShitICantFixItsReallyNotMyFault(int source, int type, int id, int severity, int messageLength, long message, long l, CallbackInfo ci) {
		ci.cancel();
	}
}
