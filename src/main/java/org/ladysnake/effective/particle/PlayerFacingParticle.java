package org.ladysnake.effective.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.fabric.impl.client.particle.FabricSpriteProviderImpl;
import net.minecraft.client.render.Camera;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import team.lodestar.lodestone.config.ClientConfig;
import team.lodestar.lodestone.handlers.RenderHandler;
import team.lodestar.lodestone.setup.LodestoneRenderLayers;
import team.lodestar.lodestone.systems.rendering.particle.LodestoneWorldParticleTextureSheet;
import team.lodestar.lodestone.systems.rendering.particle.world.GenericParticle;
import team.lodestar.lodestone.systems.rendering.particle.world.WorldParticleEffect;

public class PlayerFacingParticle extends GenericParticle {
	public PlayerFacingParticle(ClientWorld world, WorldParticleEffect data, FabricSpriteProviderImpl spriteSet, double x, double y, double z, double xd, double yd, double zd) {
		super(world, data, spriteSet, x, y, z, xd, yd, zd);
	}

	@Override
	public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
		VertexConsumer consumer = vertexConsumer;
		if (ClientConfig.DELAYED_RENDERING) {
			if (getType().equals(LodestoneWorldParticleTextureSheet.ADDITIVE)) {
				consumer = RenderHandler.DELAYED_RENDER.getBuffer(LodestoneRenderLayers.ADDITIVE_PARTICLE);
			}
			if (getType().equals(LodestoneWorldParticleTextureSheet.TRANSPARENT)) {
				consumer = RenderHandler.DELAYED_RENDER.getBuffer(LodestoneRenderLayers.TRANSPARENT_PARTICLE);
			}
		}

		Vec3d vec3d = camera.getPos();
		float f = (float) (MathHelper.lerp(tickDelta, this.prevPosX, this.x) - vec3d.getX());
		float g = (float) (MathHelper.lerp(tickDelta, this.prevPosY, this.y) - vec3d.getY());
		float h = (float) (MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - vec3d.getZ());
		Quaternionf quaternion;
		if (this.angle == 0.0F) {
			quaternion = camera.getRotation();
		} else {
			quaternion = new Quaternionf(camera.getRotation());
			float i = MathHelper.lerp(tickDelta, this.prevAngle, this.angle);
			quaternion.rotateZ(i);
		}

		Vector3f[] vec3fs = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
		float j = this.getSize(tickDelta);

		Vec3d v1 = new Vec3d(0, 0, 1);
		Vec3d v2 = new Vec3d(this.x, this.y, this.z).subtract(camera.getPos()).normalize();
		Vec3d a = v1.crossProduct(v2);
		Quaternionf q = new Quaternionf(0f, (float) a.getY(), (float) a.getZ(), (float) (MathHelper.sqrt((float) ((v1.length() * v1.length()) * (v2.length() * v2.length()))) + v1.dotProduct(v2)));
		q.normalize();

		for (int k = 0; k < 4; ++k) {
			Vector3f vec3f2 = vec3fs[k];
			vec3f2.rotate(q);
			vec3f2.mul(j);
			vec3f2.add(f, g, h);
		}

		float minU = this.getMinU();
		float maxU = this.getMaxU();
		float minV = this.getMinV();
		float maxV = this.getMaxV();
		int l = this.getBrightness(tickDelta);

		consumer.vertex(vec3fs[0].x(), vec3fs[0].y(), vec3fs[0].z()).uv(maxU, maxV).color(colorRed, colorGreen, colorBlue, colorAlpha).light(l).next();
		consumer.vertex(vec3fs[1].x(), vec3fs[1].y(), vec3fs[1].z()).uv(maxU, minV).color(colorRed, colorGreen, colorBlue, colorAlpha).light(l).next();
		consumer.vertex(vec3fs[2].x(), vec3fs[2].y(), vec3fs[2].z()).uv(minU, minV).color(colorRed, colorGreen, colorBlue, colorAlpha).light(l).next();
		consumer.vertex(vec3fs[3].x(), vec3fs[3].y(), vec3fs[3].z()).uv(minU, maxV).color(colorRed, colorGreen, colorBlue, colorAlpha).light(l).next();
	}
}
