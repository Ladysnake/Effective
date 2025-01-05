package org.ladysnake.effective.mixin.soft_particles;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.LightmapTextureManager;
import org.ladysnake.effective.mixinterface.soft_particles.ParticleManagerExtension;
import org.ladysnake.effective.render.particle.SoftParticleRenderType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

/**
 * Provides a method to render all {@link SoftParticleRenderType#SOFT_PARTICLE} particles by wrapping {@link ParticleManager#renderParticles(LightmapTextureManager, Camera, float)} and temporarily changing the {@link ParticleTextureSheet} list.
 *
 * @author Ryan
 */
@Mixin(ParticleManager.class)
public abstract class ParticleManagerMixin implements ParticleManagerExtension {

	@Shadow
	@Final
	@Mutable
	private static List<ParticleTextureSheet> PARTICLE_TEXTURE_SHEETS;

	@Shadow
	public abstract void renderParticles(LightmapTextureManager lightmapTextureManager, Camera camera, float tickDelta);

	@Override
	public void effective$renderSoftParticles(LightmapTextureManager lightmapTextureManager, Camera camera, float partialTick) {
		final List<ParticleTextureSheet> backup = PARTICLE_TEXTURE_SHEETS;

		PARTICLE_TEXTURE_SHEETS = ImmutableList.of(SoftParticleRenderType.SOFT_PARTICLE);
		this.renderParticles(lightmapTextureManager, camera, partialTick);

		PARTICLE_TEXTURE_SHEETS = backup;
	}
}
