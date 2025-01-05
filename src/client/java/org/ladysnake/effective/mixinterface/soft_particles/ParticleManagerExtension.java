package org.ladysnake.effective.mixinterface.soft_particles;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.LightmapTextureManager;

public interface ParticleManagerExtension {
	void effective$renderSoftParticles(LightmapTextureManager lightmapTextureManager, Camera camera, float partialTick);
}
