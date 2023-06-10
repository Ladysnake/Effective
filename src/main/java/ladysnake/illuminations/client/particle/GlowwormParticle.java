package ladysnake.illuminations.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import ladysnake.effective.client.Effective;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class GlowwormParticle extends SpriteBillboardParticle {
	private static final float BLINK_STEP = 0.01f;
	private static final Random RANDOM = new Random();
	private final SpriteProvider spriteProvider;
	protected float nextAlphaGoal = 0f;
	boolean onCeiling;
	private BlockPos lightTarget;
	private double xTarget;
	private double yTarget;
	private double zTarget;
	private int targetChangeCooldown = 0;
	private boolean isAttractedByLight = false;
	private int maxHeight;

	private GlowwormParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
		super(world, x, y, z, velocityX, velocityY, velocityZ);
		this.spriteProvider = spriteProvider;

		this.scale *= 0.25f + new Random().nextFloat() * 0.50f;
		this.maxAge = ThreadLocalRandom.current().nextInt(1200, 3601); // live between one and three minutes
		this.maxHeight = 255;
		this.collidesWithWorld = true;
		this.setSpriteForAge(spriteProvider);

		this.colorRed = 0f;
		this.colorGreen = 0.75f + new Random().nextFloat() * 0.25f;
		this.colorBlue = 1f;
		this.colorAlpha = 0f;

		this.velocityX = 0;
		this.velocityY = 0;
		this.velocityZ = 0;
		this.initOnCeiling();
	}

	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
	}

	@Override
	public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
		Vec3d vec3d = camera.getPos();
		float f = (float) (MathHelper.lerp((double) tickDelta, this.prevPosX, this.x) - vec3d.getX());
		float g = (float) (MathHelper.lerp((double) tickDelta, this.prevPosY, this.y) - vec3d.getY());
		float h = (float) (MathHelper.lerp((double) tickDelta, this.prevPosZ, this.z) - vec3d.getZ());
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
		float a = Math.min(1f, Math.max(0f, this.colorAlpha));

		// colored layer
		vertexConsumer.vertex(Vector3fs[0].x, Vector3fs[0].y, Vector3fs[0].z).uv(maxU, minV + (maxV - minV) / 2.0F).color(this.colorRed, this.colorGreen, this.colorBlue, a).light(l).next();
		vertexConsumer.vertex(Vector3fs[1].x, Vector3fs[1].y, Vector3fs[1].z).uv(maxU, minV).color(this.colorRed, this.colorGreen, this.colorBlue, a).light(l).next();
		vertexConsumer.vertex(Vector3fs[2].x, Vector3fs[2].y, Vector3fs[2].z).uv(minU, minV).color(this.colorRed, this.colorGreen, this.colorBlue, a).light(l).next();
		vertexConsumer.vertex(Vector3fs[3].x, Vector3fs[3].y, Vector3fs[3].z).uv(minU, minV + (maxV - minV) / 2.0F).color(this.colorRed, this.colorGreen, this.colorBlue, a).light(l).next();

		// white center
		vertexConsumer.vertex(Vector3fs[0].x, Vector3fs[0].y, Vector3fs[0].z).uv(maxU, maxV).color(1f, 1f, 1f, 0.5f / 100f).light(l).next();
		vertexConsumer.vertex(Vector3fs[1].x, Vector3fs[1].y, Vector3fs[1].z).uv(maxU, minV + (maxV - minV) / 2.0F).color(1f, 1f, 1f, 0.5f / 100f).light(l).next();
		vertexConsumer.vertex(Vector3fs[2].x, Vector3fs[2].y, Vector3fs[2].z).uv(minU, minV + (maxV - minV) / 2.0F).color(1f, 1f, 1f, 0.5f / 100f).light(l).next();
		vertexConsumer.vertex(Vector3fs[3].x, Vector3fs[3].y, Vector3fs[3].z).uv(minU, maxV).color(1f, 1f, 1f, 0.5f / 100f).light(l).next();
	}

	public void tick() {
		this.prevPosX = this.x;
		this.prevPosY = this.y;
		this.prevPosZ = this.z;

		// if old enough, fade and die
		if (this.age++ >= this.maxAge) {
			nextAlphaGoal = -BLINK_STEP;
			if (this.colorAlpha < 0f) {
				this.markDead();
			}
		}

		// if above block is no longer here, tag no longer on ceiling
		if (this.world.getBlockState(BlockPos.create(this.x, this.y + 0.5, this.z)).isAir()) {
			this.onCeiling = false;
		}

		// if no longer on ceiling and no block under, fall, fade and die
		if (!this.onCeiling) {
			this.velocityY -= 0.1;
			this.maxAge = 0;
		}

		// blinking
		if (this.colorAlpha > nextAlphaGoal - BLINK_STEP && this.colorAlpha < nextAlphaGoal + BLINK_STEP) {
			nextAlphaGoal = new Random().nextFloat();
		} else {
			if (nextAlphaGoal > this.colorAlpha) {
				this.colorAlpha += BLINK_STEP;
			} else if (nextAlphaGoal < this.colorAlpha) {
				this.colorAlpha -= BLINK_STEP;
			}
		}

		this.targetChangeCooldown -= (new Vec3d(x, y, z).squaredDistanceTo(prevPosX, prevPosY, prevPosZ) < 0.0125) ? 10 : 1;

		if ((this.world.getTime() % 20 == 0) && ((xTarget == 0 && yTarget == 0 && zTarget == 0) || new Vec3d(x, y, z).squaredDistanceTo(xTarget, yTarget, zTarget) < 9 || targetChangeCooldown <= 0)) {
			selectBlockTarget();
		}

		Vec3d targetVector = new Vec3d(this.xTarget - this.x, this.yTarget - this.y, this.zTarget - this.z);
		double length = targetVector.length();
		targetVector = targetVector.multiply(0.1 / length);


		if (!this.world.getBlockState(BlockPos.create(this.x, this.y - 0.1, this.z)).getBlock().canMobSpawnInside()) {
			velocityX = (0.9) * velocityX + (0.1) * targetVector.x;
			velocityZ = (0.9) * velocityZ + (0.1) * targetVector.z;
		} else {
			velocityX = (0.9) * velocityX + (0.1) * targetVector.x;
			velocityZ = (0.9) * velocityZ + (0.1) * targetVector.z;
		}

		if (!BlockPos.create(x, y, z).equals(this.getTargetPosition())) {
			this.move(velocityX, velocityY, velocityZ);
		}
	}

	private void selectBlockTarget() {
		// Behaviour
		this.xTarget = this.x + random.nextGaussian();
		this.zTarget = this.z + random.nextGaussian();

		BlockPos targetPos = BlockPos.create(this.xTarget, this.y, this.zTarget);

		targetChangeCooldown = random.nextInt() % 100;
	}

	private void initOnCeiling() {
		this.onCeiling = true;
		this.y = (float) Math.ceil(this.y) - 0.025;
		this.colorAlpha = 0f;

		while (this.world.getBlockState(BlockPos.create(this.x, this.y + 1, this.z)).isAir()) {
			if (this.y++ > 255) {
				this.markDead();
				break;
			}
		}

		this.setPos(this.x, this.y, this.z);
	}

	public BlockPos getTargetPosition() {
		return BlockPos.create(this.xTarget, this.yTarget + 0.95, this.zTarget);
	}


	public static class DefaultFactory implements ParticleFactory<DefaultParticleType> {
		private final SpriteProvider spriteProvider;

		public DefaultFactory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
			return new GlowwormParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
		}
	}
}
