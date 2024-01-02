package org.ladysnake.effective.cosmetics.render;

import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormats;
import ladysnake.satin.mixin.client.render.RenderLayerAccessor;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public class GlowyRenderLayer extends RenderLayer {
	public GlowyRenderLayer(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction) {
		super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
	}

	public static RenderLayer get(Identifier texture) {
		MultiPhaseParameters multiPhaseParameters = MultiPhaseParameters.builder().texture(new Texture(texture, false, false)).transparency(Transparency.TRANSLUCENT_TRANSPARENCY).cull(DISABLE_CULLING).lightmap(ENABLE_LIGHTMAP).overlay(DISABLE_OVERLAY_COLOR).layering(VIEW_OFFSET_Z_LAYERING).shader(ENERGY_SWIRL_SHADER).build(true);
		return RenderLayerAccessor.satin$of("crown", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 256, false, false, multiPhaseParameters);
	}
}
