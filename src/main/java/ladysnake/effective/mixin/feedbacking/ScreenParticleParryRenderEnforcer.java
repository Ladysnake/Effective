package ladysnake.effective.mixin.feedbacking;

import com.sammy.lodestone.handlers.ScreenParticleHandler;
import com.sammy.lodestone.systems.rendering.particle.screen.base.ScreenParticle;
import ladysnake.effective.gui.ParryScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

import static com.sammy.lodestone.systems.rendering.particle.screen.base.ScreenParticle.RenderOrder.AFTER_EVERYTHING;
import static com.sammy.lodestone.systems.rendering.particle.screen.base.ScreenParticle.RenderOrder.BEFORE_UI;

@Mixin(ScreenParticleHandler.class)
public abstract class ScreenParticleParryRenderEnforcer {
	@Shadow(remap = false)
	public static ArrayList<ScreenParticleHandler.StackTracker> RENDERED_STACKS;
	@Shadow(remap = false)
	public static boolean canSpawnParticles;

	@Shadow(remap = false)
	public static void renderParticles(ScreenParticle.RenderOrder... renderOrders) {
	}

	@Inject(method = "renderParticles()V", at = @At("TAIL"), remap = false)
	private static void renderParticles(CallbackInfo ci) {
		final MinecraftClient client = MinecraftClient.getInstance();
		Screen screen = client.currentScreen;
		if (screen instanceof ParryScreen) {
			renderParticles(AFTER_EVERYTHING, BEFORE_UI);
		}
		RENDERED_STACKS.clear();
		canSpawnParticles = false;
	}

}
