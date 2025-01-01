//package org.ladysnake.effective.core.render.particle;
//
//import com.mojang.blaze3d.systems.RenderSystem;
//import net.minecraft.client.MinecraftClient;
//import net.minecraft.client.particle.ParticleTextureSheet;
//import net.minecraft.client.render.BufferBuilder;
//import net.minecraft.client.render.Tessellator;
//import net.minecraft.client.texture.SpriteAtlasTexture;
//import net.minecraft.client.texture.TextureManager;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//
//public class SoftParticleRenderType implements ParticleTextureSheet {
//	@NotNull
//	public static final SoftParticleRenderType SOFT_PARTICLE = new SoftParticleRenderType();
//
//	@Override
//	public @Nullable BufferBuilder begin(Tessellator tessellator, TextureManager textureManager) {
//		final MinecraftClient minecraft = MinecraftClient.getInstance();
//
//		RenderSystem.setShader(() -> softParticle);
//
//		// Disallow soft particles from writing to the depth buffer
//		RenderSystem.depthMask(false);
//
//		// Set `Sampler0` to the particle atlas
//		// noinspection deprecation
//		RenderSystem.setShaderTexture(0, SpriteAtlasTexture.PARTICLE_ATLAS_TEXTURE);
//
//		RenderSystem.enableBlend();
//		RenderSystem.defaultBlendFunc();
//
//		// Set the sampler for the depth texture
//		softParticle.setSampler("DiffuseDepthSampler", minecraft.getMainRenderTarget().getDepthTextureId());
//
//		return tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
//	}
//}
