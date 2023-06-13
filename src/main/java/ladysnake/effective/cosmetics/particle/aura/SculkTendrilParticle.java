package ladysnake.effective.cosmetics.particle.aura;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.*;

import java.util.Random;

public class SculkTendrilParticle extends SpriteBillboardParticle {
	private static final Random RANDOM = new Random();
	private final SpriteProvider provider;
	private boolean wasOnGround = false;

	public SculkTendrilParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
		super(world, x + (RANDOM.nextFloat() * 2f - 1.0f), y, z + (RANDOM.nextFloat() * 2f - 1.0f), velocityX, velocityY, velocityZ);
		this.setSprite(spriteProvider.getSprite(0, 1));
		provider = spriteProvider;
		this.maxAge = 100;
		this.angle = RANDOM.nextFloat() * 180f;

		this.scale = 0f;
		this.collidesWithWorld = true;
	}

	@Override
	public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
		Vec3d vec3d = camera.getPos();
		float f = (float) (MathHelper.lerp(tickDelta, this.prevPosX, this.x) - vec3d.getX());
		float g = (float) (MathHelper.lerp(tickDelta, this.prevPosY, this.y) - vec3d.getY());
		float h = (float) (MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - vec3d.getZ());

		Vec3f[] Vec3fs = new Vec3f[]{new Vec3f(-1.0F, -1.0F, 0.0F), new Vec3f(-1.0F, 1.0F, 0.0F), new Vec3f(1.0F, 1.0F, 0.0F), new Vec3f(1.0F, -1.0F, 0.0F)};
		float j = this.getSize(tickDelta);

		for (int k = 0; k < 4; ++k) {
			Vec3f Vec3f2 = Vec3fs[k];
			Vec3f2.rotate(new Quaternion(0f, angle, 0f, 0));
			Vec3f2.scale(j);
			Vec3f2.add(f, g, h);
		}

		float minU = this.getMinU();
		float maxU = this.getMaxU();
		float minV = this.getMinV();
		float maxV = this.getMaxV();
		int l = 15728880;

		vertexConsumer.vertex(Vec3fs[0].getX(), Vec3fs[0].getY(), Vec3fs[0].getZ()).uv(maxU, maxV).color(colorRed, colorGreen, colorBlue, colorAlpha).light(l).next();
		vertexConsumer.vertex(Vec3fs[1].getX(), Vec3fs[1].getY(), Vec3fs[1].getZ()).uv(maxU, minV).color(colorRed, colorGreen, colorBlue, colorAlpha).light(l).next();
		vertexConsumer.vertex(Vec3fs[2].getX(), Vec3fs[2].getY(), Vec3fs[2].getZ()).uv(minU, minV).color(colorRed, colorGreen, colorBlue, colorAlpha).light(l).next();
		vertexConsumer.vertex(Vec3fs[3].getX(), Vec3fs[3].getY(), Vec3fs[3].getZ()).uv(minU, maxV).color(colorRed, colorGreen, colorBlue, colorAlpha).light(l).next();
	}

	@Override
	public void tick() {
		if (!this.onGround) {
			this.move(0, -1D, 0);
			wasOnGround = false;
			return;
		}
		if (!wasOnGround) {
			this.repositionFromBoundingBox();
			wasOnGround = true;
		}
		if (this.age++ < this.maxAge) {
			if (this.scale == 0f) {
				this.y -= 0.4f;
			}
			if (this.scale < 0.4f) {
				this.scale = Math.min(0.4f, this.scale + 0.05f);
				this.y += 0.05f;
			}
			if (this.age == 75) {
				this.setSprite(provider.getSprite(1, 1));
			}
		}
		this.setBoundingBox(this.getBoundingBox().expand(0, 1, 0));


		this.prevPosX = this.x;
		this.prevPosY = this.y;
		this.prevPosZ = this.z;
		this.prevAngle = this.angle;

		if (this.age >= this.maxAge) {
			this.scale = Math.max(0f, this.scale - 0.05f);
			this.y -= 0.05f;

			if (this.scale <= 0f) {
				this.markDead();
			}
		}
	}

	@Override
	protected void repositionFromBoundingBox() {
		Box box = this.getBoundingBox();
		this.x = (box.minX + box.maxX) / 2.0D;
		this.y = box.minY + 0.4D;
		this.z = (box.minZ + box.maxZ) / 2.0D;
	}

	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
	}


	public static class DefaultFactory implements ParticleFactory<DefaultParticleType> {
		private final SpriteProvider spriteProvider;

		public DefaultFactory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
			return new SculkTendrilParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
		}
	}
}
