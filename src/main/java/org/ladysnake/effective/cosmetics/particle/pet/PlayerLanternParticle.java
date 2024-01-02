package org.ladysnake.effective.cosmetics.particle.pet;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.Model;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Axis;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.effective.cosmetics.render.GlowyRenderLayer;
import org.ladysnake.effective.cosmetics.render.entity.model.pet.LanternModel;

public class PlayerLanternParticle extends Particle {
	public final Identifier texture;
	final RenderLayer layer;
	public float yaw;
	public float pitch;
	public float prevYaw;
	public float prevPitch;
	protected PlayerEntity owner;
	Model model;

	protected PlayerLanternParticle(ClientWorld world, double x, double y, double z, Identifier texture, float red, float green, float blue) {
		super(world, x, y, z);
		this.texture = texture;
		this.model = new LanternModel(MinecraftClient.getInstance().getEntityModelLoader().getModelPart(LanternModel.MODEL_LAYER));
		this.layer = RenderLayer.getEntityTranslucent(texture);
		this.gravityStrength = 0.0F;

		this.maxAge = 35;
		this.owner = world.getClosestPlayer((TargetPredicate.createNonAttackable()).setBaseMaxDistance(1D), this.x, this.y, this.z);

		if (this.owner == null) {
			this.markDead();
		}

		this.colorRed = red;
		this.colorGreen = green;
		this.colorBlue = blue;
		this.colorAlpha = 0;
	}

	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.CUSTOM;
	}

	@Override
	public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
		Vec3d vec3d = camera.getPos();
		float f = (float) (MathHelper.lerp(tickDelta, this.prevPosX, this.x) - vec3d.getX());
		float g = (float) (MathHelper.lerp(tickDelta, this.prevPosY, this.y) - vec3d.getY());
		float h = (float) (MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - vec3d.getZ());

		MatrixStack matrixStack = new MatrixStack();
		matrixStack.translate(f, g, h);
		matrixStack.multiply(Axis.Y_POSITIVE.rotationDegrees(MathHelper.lerp(g, this.prevYaw, this.yaw) - 180));
		matrixStack.multiply(Axis.X_POSITIVE.rotationDegrees(MathHelper.lerp(g, this.prevPitch, this.pitch)));
		matrixStack.scale(0.5F, -0.5F, 0.5F);
		matrixStack.translate(0, -1, 0);
		VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
		VertexConsumer vertexConsumer2 = immediate.getBuffer(GlowyRenderLayer.get(texture));
		if (this.colorAlpha > 0) {
			this.model.render(matrixStack, vertexConsumer2, 15728880, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0f);
		}
		immediate.draw();
	}

	@Override
	public void tick() {
		if (this.age > 10) {
			this.colorAlpha = 1f;
		} else {
			this.colorAlpha = 0;
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

			this.prevYaw = this.yaw;
			this.yaw = owner.age * 2;
		} else {
			this.markDead();
		}
	}


	public static class DefaultFactory implements ParticleFactory<DefaultParticleType> {
		private final Identifier texture;
		private final float red;
		private final float green;
		private final float blue;

		public DefaultFactory(SpriteProvider spriteProvider, Identifier texture, float red, float green, float blue) {
			this.texture = texture;
			this.red = red;
			this.green = green;
			this.blue = blue;
		}

		@Nullable
		@Override
		public Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			return new PlayerLanternParticle(world, x, y, z, this.texture, this.red, this.green, this.blue);
		}
	}
}
