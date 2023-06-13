package ladysnake.effective.cosmetics.particle.pet;

import com.mojang.blaze3d.vertex.VertexConsumer;
import ladysnake.effective.Effective;
import ladysnake.effective.particle.FireflyParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.render.Camera;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Random;

public class PetParticle extends FireflyParticle {
	private static final Random RANDOM = new Random();
	protected final PlayerEntity owner;
	private final SpriteProvider spriteProvider;
	protected float alpha = 0f;

	public PetParticle(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider) {
		super(world, x, y, z, spriteProvider);
		this.spriteProvider = spriteProvider;
		this.setSpriteForAge(spriteProvider);

		this.colorAlpha = 0;
		this.maxAge = 40;
		this.owner = world.getClosestPlayer(TargetPredicate.createNonAttackable().setBaseMaxDistance(1D), this.x, this.y, this.z);

		this.scale = 0.2f;

		if (this.owner == null) {
			this.markDead();
		}
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

		vertexConsumer.vertex(Vector3fs[0].x, Vector3fs[0].y, Vector3fs[0].z).uv(maxU, maxV).color(1f, 1f, 1f, alpha).light(l).next();
		vertexConsumer.vertex(Vector3fs[1].x, Vector3fs[1].y, Vector3fs[1].z).uv(maxU, minV).color(1f, 1f, 1f, alpha).light(l).next();
		vertexConsumer.vertex(Vector3fs[2].x, Vector3fs[2].y, Vector3fs[2].z).uv(minU, minV).color(1f, 1f, 1f, alpha).light(l).next();
		vertexConsumer.vertex(Vector3fs[3].x, Vector3fs[3].y, Vector3fs[3].z).uv(minU, maxV).color(1f, 1f, 1f, alpha).light(l).next();
	}

	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
	}

	@Override
	public void tick() {
		if (this.age > 10) {
			alpha = 1;
		} else {
			alpha = 0;
		}

		if (owner != null) {
			this.prevPosX = this.x;
			this.prevPosY = this.y;
			this.prevPosZ = this.z;

			// die if old enough
			if (this.age++ >= this.maxAge) {
				this.markDead();
			}

			this.setPos(owner.getX() + Math.cos(owner.bodyYaw / 50) * 0.5, owner.getY() + owner.getHeight() + 0.5f + Math.sin(owner.age / 12f) / 12f, owner.getZ() - Math.cos(owner.bodyYaw / 50) * 0.5);
		} else {
			this.markDead();
		}
	}


	public static class DefaultFactory implements ParticleFactory<DefaultParticleType> {
		private final SpriteProvider spriteProvider;

		public DefaultFactory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
			return new PetParticle(clientWorld, d, e, f, this.spriteProvider);
		}
	}

}
