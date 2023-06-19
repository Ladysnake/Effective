package ladysnake.effective.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.sammy.lodestone.config.ClientConfig;
import com.sammy.lodestone.handlers.RenderHandler;
import com.sammy.lodestone.setup.LodestoneRenderLayers;
import com.sammy.lodestone.systems.rendering.particle.ParticleTextureSheets;
import com.sammy.lodestone.systems.rendering.particle.world.GenericParticle;
import com.sammy.lodestone.systems.rendering.particle.world.WorldParticleEffect;
import net.fabricmc.fabric.impl.client.particle.FabricSpriteProviderImpl;
import net.minecraft.client.render.Camera;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.*;

public class MistParticle extends GenericParticle {
	public MistParticle(ClientWorld world, WorldParticleEffect data, FabricSpriteProviderImpl spriteSet, double x, double y, double z, double xd, double yd, double zd) {
		super(world, data, spriteSet, x, y, z, xd, yd, zd);
	}

	@Override
	public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
		VertexConsumer consumer = vertexConsumer;
		if (ClientConfig.DELAYED_RENDERING) {
			if (getType().equals(ParticleTextureSheets.ADDITIVE)) {
				consumer = RenderHandler.DELAYED_RENDER.getBuffer(LodestoneRenderLayers.ADDITIVE_PARTICLE);
			}
			if (getType().equals(ParticleTextureSheets.TRANSPARENT)) {
				consumer = RenderHandler.DELAYED_RENDER.getBuffer(LodestoneRenderLayers.TRANSPARENT_PARTICLE);
			}
		}

		Vec3d vec3d = camera.getPos();
		float f = (float)(MathHelper.lerp(tickDelta, this.prevPosX, this.x) - vec3d.getX());
		float g = (float)(MathHelper.lerp(tickDelta, this.prevPosY, this.y) - vec3d.getY());
		float h = (float)(MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - vec3d.getZ());
		Quaternion quaternion;
		if (this.angle == 0.0F) {
			quaternion = camera.getRotation();
		} else {
			quaternion = new Quaternion(camera.getRotation());
			float i = MathHelper.lerp(tickDelta, this.prevAngle, this.angle);
			quaternion.hamiltonProduct(Vec3f.POSITIVE_Z.getRadialQuaternion(i));
		}

		Vec3f vec3f = new Vec3f(-1.0F, -1.0F, 0.0F);
		vec3f.rotate(quaternion);
		Vec3f[] vec3fs = new Vec3f[]{new Vec3f(-1.0F, -1.0F, 0.0F), new Vec3f(-1.0F, 1.0F, 0.0F), new Vec3f(1.0F, 1.0F, 0.0F), new Vec3f(1.0F, -1.0F, 0.0F)};
		float j = this.getSize(tickDelta);

		for(int k = 0; k < 4; ++k) {
			Vec3f vec3f2 = vec3fs[k];
			vec3f2.rotate(quaternion);
			vec3f2.scale(j);
			vec3f2.add(f, g, h);
		}

		float l = this.getMinU();
		float m = this.getMaxU();
		float n = this.getMinV();
		float o = this.getMaxV();
		int p = this.getBrightness(tickDelta);
		consumer.vertex(vec3fs[0].getX(), vec3fs[0].getY(), vec3fs[0].getZ())
			.uv(m, o)
			.color(this.colorRed, this.colorGreen, this.colorBlue, this.colorAlpha)
			.light(p)
			.next();
		consumer.vertex(vec3fs[1].getX(), vec3fs[1].getY(), vec3fs[1].getZ())
			.uv(m, n)
			.color(this.colorRed, this.colorGreen, this.colorBlue, this.colorAlpha)
			.light(p)
			.next();
		consumer.vertex(vec3fs[2].getX(), vec3fs[2].getY(), vec3fs[2].getZ())
			.uv(l, n)
			.color(this.colorRed, this.colorGreen, this.colorBlue, this.colorAlpha)
			.light(p)
			.next();
		consumer.vertex(vec3fs[3].getX(), vec3fs[3].getY(), vec3fs[3].getZ())
			.uv(l, o)
			.color(this.colorRed, this.colorGreen, this.colorBlue, this.colorAlpha)
			.light(p)
			.next();
	}
}
