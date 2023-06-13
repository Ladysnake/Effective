package ladysnake.effective.cosmetics.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import ladysnake.effective.Effective;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Random;

public class WispTrailParticle extends SpriteBillboardParticle {
	private final SpriteProvider spriteProvider;

	private final float redEvolution;
	private final float greenEvolution;
	private final float blueEvolution;

	private WispTrailParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, WispTrailParticleEffect wispTrailParticleEffect, SpriteProvider spriteProvider) {
		super(world, x, y, z, velocityX, velocityY, velocityZ);
		this.spriteProvider = spriteProvider;
		this.colorRed = wispTrailParticleEffect.getRed();
		this.colorGreen = wispTrailParticleEffect.getGreen();
		this.colorBlue = wispTrailParticleEffect.getBlue();
		this.redEvolution = wispTrailParticleEffect.getRedEvolution();
		this.greenEvolution = wispTrailParticleEffect.getGreenEvolution();
		this.blueEvolution = wispTrailParticleEffect.getBlueEvolution();
		this.maxAge = 10 + this.random.nextInt(10);
		this.scale *= 0.25f + new Random().nextFloat() * 0.50f;
		this.setSpriteForAge(spriteProvider);
		this.velocityY = 0.1;
	}

	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
	}

	@Override
	public void tick() {
		this.prevPosX = this.x;
		this.prevPosY = this.y;
		this.prevPosZ = this.z;

		// fade and die
		if (this.age++ >= this.maxAge) {
			this.colorAlpha -= 0.05f;
		}
		if (this.colorAlpha < 0f || this.scale <= 0f) {
			this.markDead();
		}

//        float redEv = -0.03f;
//        float greenEv = 0.0f;
//        float blueEv = -0.01f;
//        red = MathHelper.clamp(red+redEv, 0, 1);
//        green = MathHelper.clamp(green+greenEv, 0, 1);
//        blue = MathHelper.clamp(blue+blueEv, 0, 1);

		this.colorRed = MathHelper.clamp(this.colorRed + redEvolution, 0, 1);
		this.colorGreen = MathHelper.clamp(this.colorGreen + greenEvolution, 0, 1);
		this.colorBlue = MathHelper.clamp(this.colorBlue + blueEvolution, 0, 1);

		this.velocityY -= 0.001;
		this.velocityX = 0;
		this.velocityZ = 0;
		this.scale = Math.max(0, this.scale - 0.005f);
		this.move(velocityX, velocityY, velocityZ);
	}

	@Override
	public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
		Vec3d vec3d = camera.getPos();
		float f = (float) (MathHelper.lerp(tickDelta, this.prevPosX, this.x) - vec3d.getX());
		float g = (float) (MathHelper.lerp(tickDelta, this.prevPosY, this.y) - vec3d.getY());
		float h = (float) (MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - vec3d.getZ());
		Quaternionf quaternion2;
		if (this.angle == 0.0F) {
			quaternion2 = camera.getRotation();
		} else {
			quaternion2 = new Quaternionf(camera.getRotation());
			float i = MathHelper.lerp(tickDelta, this.prevAngle, this.angle);
			Effective.wheresTheHamiltonProductMojangski(quaternion2, new Quaternionf().rotateZ(i));
		}

		Vector3f Vector3f = new Vector3f(-1.0F, -1.0F, 0.0F);
		Vector3f.rotate(quaternion2);
		Vector3f[] Vector3fs = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
		float j = this.getSize(tickDelta);

		for (int k = 0; k < 4; ++k) {
			Vector3f Vector3f2 = Vector3fs[k];
			Vector3f2.rotate(quaternion2);
			Vector3f2.mul(j);
			Vector3f2.add(f, g, h);
		}

		float minU = this.getMinU();
		float maxU = this.getMaxU();
		float minV = this.getMinV();
		float maxV = this.getMaxV();
		int l = 15728880;

		vertexConsumer.vertex(Vector3fs[0].x, Vector3fs[0].y, Vector3fs[0].z).uv(maxU, maxV).color(colorRed, colorGreen, colorBlue, colorAlpha).light(l).next();
		vertexConsumer.vertex(Vector3fs[1].x, Vector3fs[1].y, Vector3fs[1].z).uv(maxU, minV).color(colorRed, colorGreen, colorBlue, colorAlpha).light(l).next();
		vertexConsumer.vertex(Vector3fs[2].x, Vector3fs[2].y, Vector3fs[2].z).uv(minU, minV).color(colorRed, colorGreen, colorBlue, colorAlpha).light(l).next();
		vertexConsumer.vertex(Vector3fs[3].x, Vector3fs[3].y, Vector3fs[3].z).uv(minU, maxV).color(colorRed, colorGreen, colorBlue, colorAlpha).light(l).next();
	}


	public static class Factory implements ParticleFactory<WispTrailParticleEffect> {
		private final SpriteProvider spriteProvider;

		public Factory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		public Particle createParticle(WispTrailParticleEffect wispTrailParticleEffect, ClientWorld clientWorld, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			return new WispTrailParticle(clientWorld, x, y, z, velocityX, velocityY, velocityZ, wispTrailParticleEffect, this.spriteProvider);
		}
	}
}
