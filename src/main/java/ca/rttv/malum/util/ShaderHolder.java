package ca.rttv.malum.util;

import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.ShaderProgram;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public final class ShaderHolder {
    public ShaderProgram instance;
    public final RenderPhase.Shader phase = new RenderPhase.Shader(getInstance());
    public final ArrayList<String> uniforms;
    public final ArrayList<UniformData> defaultUniformData = new ArrayList<>();

    public ShaderHolder(String... uniforms) {
        this.uniforms = new ArrayList<>(List.of(uniforms));
    }

    public Supplier<ShaderProgram> getInstance() {
        return () -> instance;
    }

    public void setInstance(ShaderProgram instance) {
        this.instance = instance;
    }
}