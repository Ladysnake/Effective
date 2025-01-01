package org.ladysnake.effective.core.render.particle;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.texture.TextureManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class SoftParticleRenderType implements ParticleTextureSheet {
	@NotNull
	public static final SoftParticleRenderType SOFT_PARTICLE = new SoftParticleRenderType();
	private static ShaderProgram softParticle;

	@Override
	public @Nullable BufferBuilder begin(Tessellator tessellator, TextureManager textureManager) {
		final MinecraftClient minecraft = MinecraftClient.getInstance();

		RenderSystem.setShader(() -> softParticle);

		// Disallow soft particles from writing to the depth buffer
		RenderSystem.depthMask(false);

		// Set `Sampler0` to the particle atlas
		// noinspection deprecation
		RenderSystem.setShaderTexture(0, SpriteAtlasTexture.PARTICLE_ATLAS_TEXTURE);

		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();

		// Set the sampler for the depth texture
		softParticle.addSampler("DiffuseDepthSampler", minecraft.getFramebuffer().getDepthAttachment());

		return tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR_LIGHT);
	}

	public static void loadShader(@NotNull final ShaderProgram shader) {
		softParticle = Objects.requireNonNull(shader);
	}
}
