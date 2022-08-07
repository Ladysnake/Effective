package ladysnake.effective.client.render;

import ladysnake.effective.mixin.RenderLayerAccessor;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;

public class GlowyRenderLayer extends RenderLayer {
    public GlowyRenderLayer(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction) {
        super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
    }

    public static RenderLayer get(Identifier texture) {
        MultiPhaseParameters multiPhaseParameters = MultiPhaseParameters.builder().texture(new Texture(texture, false, false)).transparency(Transparency.TRANSLUCENT_TRANSPARENCY).cull(RenderPhase.DISABLE_CULLING).lightmap(DISABLE_LIGHTMAP).overlay(DISABLE_OVERLAY_COLOR).layering(VIEW_OFFSET_Z_LAYERING).shader(RenderPhase.ENERGY_SWIRL_SHADER).build(true);
        return RenderLayerAccessor.invokeOf("glowwy", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 256, false, true, multiPhaseParameters);
    }
}
