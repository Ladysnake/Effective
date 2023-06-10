package ladysnake.illuminations.client.particle.aura;

import com.mojang.blaze3d.vertex.VertexConsumer;
import ladysnake.effective.client.Effective;
import ladysnake.effective.client.particle.FireflyParticle;
import ladysnake.illuminations.client.Illuminations;
import net.minecraft.block.BlockState;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.render.Camera;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.random.RandomGenerator;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Optional;
import java.util.Random;

public class TwilightFireflyParticle extends FireflyParticle {
	private final PlayerEntity owner;

	public TwilightFireflyParticle(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider) {
		super(world, x, y, z, spriteProvider);

		this.maxAge = 20;
		this.owner = world.getClosestPlayer(TargetPredicate.createNonAttackable().setBaseMaxDistance(1D), this.x, this.y, this.z);
		this.maxHeight = 2;

		Optional.ofNullable(owner).map(Illuminations::getCosmeticData).ifPresentOrElse(
				data -> {
					this.colorRed = data.getColorRed() / 255f;
					this.colorGreen = data.getColorGreen() / 255f;
					this.colorBlue = data.getColorBlue() / 255f;
					this.nextAlphaGoal = 1f;
				},
				this::markDead
		);

		this.setPos(this.x + TwilightFireflyParticle.getWanderingDistance(this.random), this.y + random.nextFloat() * 2d, this.z + TwilightFireflyParticle.getWanderingDistance(this.random));
	}

	public static double getWanderingDistance(RandomGenerator random) {
		return random.nextGaussian() / 5d;
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


	@Override
	public void tick() {
		if (owner != null) {
			this.prevPosX = this.x;
			this.prevPosY = this.y;
			this.prevPosZ = this.z;

			// fade and die on daytime or if old enough
			if (this.age++ >= this.maxAge) {
				nextAlphaGoal = -BLINK_STEP;
				if (this.colorAlpha < 0f) {
					this.markDead();
				}
			}

			// blinking
			if (this.colorAlpha > nextAlphaGoal - BLINK_STEP && this.colorAlpha < nextAlphaGoal + BLINK_STEP) {
				nextAlphaGoal = new Random().nextFloat();
			} else {
				if (nextAlphaGoal > this.colorAlpha) {
					this.colorAlpha = Math.min(this.colorAlpha + BLINK_STEP, 1f);
				} else if (nextAlphaGoal < this.colorAlpha) {
					this.colorAlpha = Math.max(this.colorAlpha - BLINK_STEP, 0f);
				}
			}

			this.targetChangeCooldown -= (new Vec3d(x, y, z).squaredDistanceTo(prevPosX, prevPosY, prevPosZ) < 0.0125) ? 10 : 1;

			if ((this.world.getTime() % 20 == 0) && ((xTarget == 0 && yTarget == 0 && zTarget == 0) || new Vec3d(x, y, z).squaredDistanceTo(xTarget, yTarget, zTarget) < 9 || targetChangeCooldown <= 0)) {
				selectBlockTarget();
			}

			Vec3d targetVector = new Vec3d(this.xTarget - this.x, this.yTarget - this.y, this.zTarget - this.z);
			double length = targetVector.length();
			targetVector = targetVector.multiply(0.025 / length);


			if (!this.world.getBlockState(BlockPos.create(this.x, this.y - 0.1, this.z)).getBlock().canMobSpawnInside()) {
				velocityX = (0.9) * velocityX + (0.1) * targetVector.x;
				velocityY = 0.05;
				velocityZ = (0.9) * velocityZ + (0.1) * targetVector.z;
			} else {
				velocityX = (0.9) * velocityX + (0.1) * targetVector.x;
				velocityY = (0.2) * velocityY + (0.1) * targetVector.y;
				velocityZ = (0.9) * velocityZ + (0.1) * targetVector.z;
			}

			if (!BlockPos.create(x, y, z).equals(this.getTargetPosition())) {
				this.move(velocityX, velocityY, velocityZ);
			}
		} else {
			this.markDead();
		}
	}

	private void selectBlockTarget() {
		// Behaviour
		double groundLevel = 0;
		for (int i = 0; i < 20; i++) {
			BlockState checkedBlock = this.world.getBlockState(BlockPos.create(this.x, this.y - i, this.z));
			if (!checkedBlock.getBlock().canMobSpawnInside()) {
				groundLevel = this.y - i;
			}
			if (groundLevel != 0) break;
		}

		this.xTarget = owner.getX() + random.nextGaussian();
		this.yTarget = Math.min(Math.max(owner.getY() + random.nextGaussian(), groundLevel), groundLevel + maxHeight);
		this.zTarget = owner.getZ() + random.nextGaussian();

		BlockPos targetPos = BlockPos.create(this.xTarget, this.yTarget, this.zTarget);
		if (this.world.getBlockState(targetPos).isFullCube(world, targetPos)
				&& this.world.getBlockState(targetPos).isSolidBlock(world, targetPos)) {
			this.yTarget += 1;
		}

		targetChangeCooldown = random.nextInt() % 100;
	}


	public static class DefaultFactory implements ParticleFactory<DefaultParticleType> {
		private final SpriteProvider spriteProvider;

		public DefaultFactory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
			return new TwilightFireflyParticle(clientWorld, d, e, f, this.spriteProvider);
		}
	}

}
