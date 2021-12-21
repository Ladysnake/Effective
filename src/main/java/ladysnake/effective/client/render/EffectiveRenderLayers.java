package ladysnake.effective.client.render;

import java.util.function.Function;

import ladysnake.effective.mixin.RenderLayerAccessor;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

public class EffectiveRenderLayers extends RenderLayer {
    private static final Function<Identifier, RenderLayer> GLOWWY = Util.memoize(texture -> {
        return RenderLayerAccessor.invokeOf("glowwy", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 256, false, true, MultiPhaseParameters.builder()
                .texture(new Texture(texture, false, false))
                .transparency(Transparency.TRANSLUCENT_TRANSPARENCY)
                .cull(RenderPhase.DISABLE_CULLING)
                .lightmap(DISABLE_LIGHTMAP)
                .overlay(DISABLE_OVERLAY_COLOR)
                .layering(VIEW_OFFSET_Z_LAYERING)
                .shader(RenderPhase.ENERGY_SWIRL_SHADER)
                .build(true));
    });
    private static final Function<Identifier, RenderLayer> NO_SHADING = Util.memoize(texture -> {
        return RenderLayerAccessor.invokeOf("noshading", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 256, false, true, RenderLayer.MultiPhaseParameters.builder()
                .texture(new RenderPhase.Texture(texture, false, false))
                .transparency(Transparency.TRANSLUCENT_TRANSPARENCY)
                .cull(RenderPhase.DISABLE_CULLING)
                .lightmap(DISABLE_LIGHTMAP)
                .overlay(DISABLE_OVERLAY_COLOR)
                .layering(VIEW_OFFSET_Z_LAYERING)
                .shader(RenderPhase.ENTITY_TRANSLUCENT_SHADER)
                .build(true));
    });

    private EffectiveRenderLayers() {
        super(null, null, null, 0, false, false, null, null);
    }

    public static RenderLayer glowwy(Identifier texture) {
        return GLOWWY.apply(texture);
    }

    public static RenderLayer noShading(Identifier texture) {
        return NO_SHADING.apply(texture);
    }
}
