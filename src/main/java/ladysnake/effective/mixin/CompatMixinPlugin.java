package ladysnake.effective.mixin;

import ladysnake.effective.client.Effective;
import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class CompatMixinPlugin implements IMixinConfigPlugin {
    @Override
    public void onLoad(String mixinPackage) {
        // NOOP
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName.equals("ladysnake.effective.mixin.compat.CanvasTerrainRenderContextMixin")) {
            final boolean canvasIsLoaded = FabricLoader.getInstance().isModLoaded("canvas");

            if (canvasIsLoaded) {
                Effective.logger.info("Canvas found. Applying compatibility mixin...");
            }

            return canvasIsLoaded;
        }

        if (mixinClassName.equals("ladysnake.effective.mixin.compat.SodiumFluidRendererMixin")) {
            final boolean sodiumIsLoaded = FabricLoader.getInstance().isModLoaded("sodium");

            if (sodiumIsLoaded) {
                Effective.logger.info("Sodium found. Applying compatibility mixin...");
            }

            return sodiumIsLoaded;
        }

        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        // NOOP
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        // NOOP
    }
}
