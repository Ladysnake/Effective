package ca.rttv.malum.client.init;

import ca.rttv.malum.util.ExtendedShader;
import ca.rttv.malum.util.ShaderHolder;
import ca.rttv.malum.util.helper.DataHelper;
import com.mojang.blaze3d.vertex.VertexFormats;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.render.ShaderProgram;
import net.minecraft.resource.ResourceManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MalumShaderRegistry {
    public static List<Pair<ShaderProgram, Consumer<ShaderProgram>>> shaderList;
    public static final ShaderHolder ADDITIVE_TEXTURE = new ShaderHolder();
    public static final ShaderHolder ADDITIVE_PARTICLE = new ShaderHolder();

    public static final ShaderHolder DISTORTED_TEXTURE = new ShaderHolder("Speed", "TimeOffset", "Intensity", "XFrequency", "YFrequency", "UVCoordinates");
    public static final ShaderHolder METALLIC_NOISE = new ShaderHolder("Intensity", "Size", "Speed", "Brightness");
    public static final ShaderHolder RADIAL_NOISE = new ShaderHolder("Speed", "XFrequency", "YFrequency", "Intensity", "ScatterPower", "ScatterFrequency", "DistanceFalloff");
    public static final ShaderHolder RADIAL_SCATTER_NOISE = new ShaderHolder("Speed", "XFrequency", "YFrequency", "Intensity", "ScatterPower", "ScatterFrequency", "DistanceFalloff");

    public static final ShaderHolder SCROLLING_TEXTURE = new ShaderHolder("Speed");
    public static final ShaderHolder TRIANGLE_TEXTURE = new ShaderHolder();
    public static final ShaderHolder SCROLLING_TRIANGLE_TEXTURE = new ShaderHolder("Speed");

    public static void init(ResourceManager manager) throws IOException {
        shaderList = new ArrayList<>();
        registerShader(ExtendedShader.createShaderInstance(ADDITIVE_TEXTURE, manager, DataHelper.prefix("additive_texture"), VertexFormats.POSITION_COLOR_TEXTURE_LIGHT));
        registerShader(ExtendedShader.createShaderInstance(ADDITIVE_PARTICLE, manager, DataHelper.prefix("additive_particle"), VertexFormats.POSITION_TEXTURE_COLOR_LIGHT));

        registerShader(ExtendedShader.createShaderInstance(DISTORTED_TEXTURE, manager, DataHelper.prefix("noise/distorted_texture"), VertexFormats.POSITION_COLOR_TEXTURE_LIGHT));
        registerShader(ExtendedShader.createShaderInstance(METALLIC_NOISE, manager, DataHelper.prefix("noise/metallic"), VertexFormats.POSITION_COLOR_TEXTURE_LIGHT));
        registerShader(ExtendedShader.createShaderInstance(RADIAL_NOISE, manager, DataHelper.prefix("noise/radial_noise"), VertexFormats.POSITION_COLOR_TEXTURE_LIGHT));
        registerShader(ExtendedShader.createShaderInstance(RADIAL_SCATTER_NOISE, manager, DataHelper.prefix("noise/radial_scatter_noise"), VertexFormats.POSITION_COLOR_TEXTURE_LIGHT));

        registerShader(ExtendedShader.createShaderInstance(SCROLLING_TEXTURE, manager, DataHelper.prefix("vfx/scrolling_texture"), VertexFormats.POSITION_COLOR_TEXTURE_LIGHT));
        registerShader(ExtendedShader.createShaderInstance(TRIANGLE_TEXTURE, manager, DataHelper.prefix("vfx/triangle_texture"), VertexFormats.POSITION_COLOR_TEXTURE_LIGHT));
        registerShader(ExtendedShader.createShaderInstance(SCROLLING_TRIANGLE_TEXTURE, manager, DataHelper.prefix("vfx/scrolling_triangle_texture"), VertexFormats.POSITION_COLOR_TEXTURE_LIGHT));
    }
    public static void registerShader(ExtendedShader extendedShaderInstance) {
        registerShader(extendedShaderInstance, (shader) -> ((ExtendedShader) shader).getHolder().setInstance(shader));
    }
    public static void registerShader(ShaderProgram shader, Consumer<ShaderProgram> onLoaded) {
        shaderList.add(Pair.of(shader, onLoaded));
    }

}
