package ca.rttv.malum.util;

import ca.rttv.malum.client.init.MalumShaderRegistry;
import ca.rttv.malum.util.handler.RenderHandler;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.datafixers.util.Pair;
import ladysnake.effective.client.Effective;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.QuiltLoader;

import java.util.HashMap;
import java.util.function.Function;

import static com.mojang.blaze3d.vertex.VertexFormats.POSITION_COLOR_TEXTURE_LIGHT;
import static com.mojang.blaze3d.vertex.VertexFormats.POSITION_TEXTURE_COLOR_LIGHT;

public class RenderLayers extends RenderPhase {
    public RenderLayers(String string, Runnable runnable, Runnable runnable2) {
        super(string, runnable, runnable2);
    }
    /**
     * Stores many copies of render layers, a copy is a new instance of a render layer with the same properties.
     * It's useful when we want to apply different uniform changes with each separate use of our render layer.
     * Use the {@link #copy(int, RenderLayer)} method to create copies.
     */
    public static final HashMap<Pair<Integer, RenderLayer>, RenderLayer> COPIES = new HashMap<>();

    public static final RenderLayer ADDITIVE_PARTICLE = createGenericRenderLayer("additive_particle", POSITION_TEXTURE_COLOR_LIGHT, VertexFormat.DrawMode.QUADS, MalumShaderRegistry.ADDITIVE_PARTICLE.phase, StateShards.ADDITIVE_TRANSPARENCY, SpriteAtlasTexture.PARTICLE_ATLAS_TEXTURE);
    public static final RenderLayer ADDITIVE_BLOCK_PARTICLE = createGenericRenderLayer("additive_block_particle", POSITION_TEXTURE_COLOR_LIGHT, VertexFormat.DrawMode.QUADS, MalumShaderRegistry.ADDITIVE_PARTICLE.phase, StateShards.ADDITIVE_TRANSPARENCY, SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);
    public static final RenderLayer ADDITIVE_BLOCK = createGenericRenderLayer("block", POSITION_COLOR_TEXTURE_LIGHT, VertexFormat.DrawMode.QUADS, MalumShaderRegistry.ADDITIVE_TEXTURE.phase, StateShards.ADDITIVE_TRANSPARENCY, SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);

    /**
     * Render Functions. You can create Render Layers by statically applying these to your texture. Alternatively, use {@link #GENERIC} if none of the presets suit your needs.
     */
    public static final Function<Identifier, RenderLayer> ADDITIVE_TEXTURE = (texture) -> createGenericRenderLayer("additive_texture", POSITION_COLOR_TEXTURE_LIGHT, VertexFormat.DrawMode.QUADS, MalumShaderRegistry.ADDITIVE_TEXTURE.phase, StateShards.ADDITIVE_TRANSPARENCY, texture);
    public static final Function<Identifier, RenderLayer> RADIAL_NOISE = (texture) -> createGenericRenderLayer("radial_noise", POSITION_COLOR_TEXTURE_LIGHT, VertexFormat.DrawMode.QUADS, MalumShaderRegistry.RADIAL_NOISE.phase, StateShards.ADDITIVE_TRANSPARENCY, texture);
    public static final Function<Identifier, RenderLayer> RADIAL_SCATTER_NOISE = (texture) -> createGenericRenderLayer("radial_scatter_noise", POSITION_COLOR_TEXTURE_LIGHT, VertexFormat.DrawMode.QUADS, MalumShaderRegistry.RADIAL_SCATTER_NOISE.phase, StateShards.ADDITIVE_TRANSPARENCY, texture);
    public static final Function<Identifier, RenderLayer> TEXTURE_TRIANGLE = (texture) -> createGenericRenderLayer("texture_triangle", POSITION_COLOR_TEXTURE_LIGHT, VertexFormat.DrawMode.QUADS, MalumShaderRegistry.TRIANGLE_TEXTURE.phase, StateShards.ADDITIVE_TRANSPARENCY, texture);
    public static final Function<Identifier, RenderLayer> SCROLLING_TEXTURE_TRIANGLE = (texture) -> createGenericRenderLayer("scrolling_texture_triangle", POSITION_COLOR_TEXTURE_LIGHT, VertexFormat.DrawMode.QUADS, MalumShaderRegistry.SCROLLING_TRIANGLE_TEXTURE.phase, StateShards.ADDITIVE_TRANSPARENCY, texture);

    public static final Function<RenderLayerData, RenderLayer> GENERIC = (data) -> createGenericRenderLayer(data.name, data.format, data.mode, data.shader, data.transparency, data.texture);

    /**
     * Creates a custom render layer and creates a buffer builder for it.
     */
    public static RenderLayer createGenericRenderLayer(String name, VertexFormat format, VertexFormat.DrawMode mode, Shader shader, Transparency transparency, Identifier texture) {
        RenderLayer type = RenderLayer.of(
                Effective.MODID + ":" + name, format, mode, /*https://youtu.be/A3eHgip6DQM*/ QuiltLoader.isModLoaded("sodium") ? 262144 : 256, false, false, RenderLayer.MultiPhaseParameters.builder()
                        .shader(shader)
                        .writeMaskState(new WriteMaskState(true, true))
                        .lightmap(new Lightmap(false))
                        .transparency(transparency)
                        .texture(new Texture(texture, false, false))
                        .cull(new Cull(true))
                        .build(true)
        );
        RenderHandler.BUFFERS.put(type, new BufferBuilder(type.getExpectedBufferSize()));
        return type;
    }

    /**
     * Queues shader uniform changes for a render layer. When we end batches in {@link RenderHandler#renderLast(net.minecraft.client.util.math.MatrixStack)}, we do so one render layer at a time.
     * Prior to ending a batch, we run {@link ShaderUniformHandler#updateShaderData(net.minecraft.client.render.ShaderProgram)} if one is present for a given render layer.
     */
    public static RenderLayer queueUniformChanges(RenderLayer type, ShaderUniformHandler handler) {
        RenderHandler.HANDLERS.put(type, handler);
        return type;
    }

    /**
     * Creates a copy of a render layer. These are stored in the {@link #COPIES} hashmap, with the key being a pair of original render layer and copy index.
     */
    public static RenderLayer copy(int index, RenderLayer layer) {
        return COPIES.computeIfAbsent(Pair.of(index, layer), (p) -> GENERIC.apply(new RenderLayerData((RenderLayer.MultiPhase) layer)));
    }

    /**
     * Stores all relevant data from a RenderLayer.
     */
    public static class RenderLayerData {
        public final String name;
        public final VertexFormat format;
        public final VertexFormat.DrawMode mode;
        public final Shader shader;
        public Transparency transparency = StateShards.ADDITIVE_TRANSPARENCY;
        public final Identifier texture;

        public RenderLayerData(String name, VertexFormat format, VertexFormat.DrawMode mode, Shader shader, Identifier texture) {
            this.name = name;
            this.format = format;
            this.mode = mode;
            this.shader = shader;
            this.texture = texture;
        }

        public RenderLayerData(String name, VertexFormat format, VertexFormat.DrawMode mode, Shader shader, Transparency transparency, Identifier texture) {
            this(name, format, mode, shader, texture);
            this.transparency = transparency;
        }

        public RenderLayerData(RenderLayer.MultiPhase type) {
            this(type.toString(), type.getVertexFormat(), type.getDrawMode(), type.phases.shader, type.phases.transparency, type.phases.texture.getId().orElseThrow());
        }
    }
}
