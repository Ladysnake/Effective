package ladysnake.effective.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import ladysnake.effective.client.Effective;
import ladysnake.effective.client.render.entity.model.SplashBottomModel;
import ladysnake.effective.client.render.entity.model.SplashModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Blocks;
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
import net.minecraft.entity.Entity;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public class LavaSplashParticle extends Particle {
	public Identifier texture1;
	public Identifier texture2;
	public float widthMultiplier;
	public float heightMultiplier;
	public int wave1End;
	public int wave2Start;
	public int wave2End;
	Model waveModel;
	Model waveBottomModel;
	RenderLayer layer1;
	RenderLayer layer2;

	protected LavaSplashParticle(ClientWorld world, double x, double y, double z, Identifier texture) {
		super(world, x, y, z);
		this.texture1 = texture;
		this.texture2 = texture;
		this.waveModel = new SplashModel<>(MinecraftClient.getInstance().getEntityModelLoader().getModelPart(SplashModel.MODEL_LAYER));
		this.waveBottomModel = new SplashBottomModel<>(MinecraftClient.getInstance().getEntityModelLoader().getModelPart(SplashBottomModel.MODEL_LAYER));
		this.layer1 = RenderLayer.getEntityTranslucent(texture);
		this.layer2 = RenderLayer.getEntityTranslucent(texture);
		this.gravityStrength = 0.0F;
		this.widthMultiplier = 0f;
		this.heightMultiplier = 0f;

		this.wave1End = 12;
		this.wave2Start = 7;
		this.wave2End = 24;
	}

	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.CUSTOM;
	}

	@Override
	public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
		// first splash
		if (age <= this.wave1End) {
			int frame1 = Math.round(((float) this.age / (float) this.wave1End) * 12);

			this.texture1 = new Identifier(Effective.MODID, "textures/entity/splash/lava_splash_" + frame1 + ".png");
			this.layer1 = RenderLayer.getEntityTranslucent(texture1);

			Vec3d vec3d = camera.getPos();
			float f = (float) (MathHelper.lerp(tickDelta, this.prevPosX, this.x) - vec3d.getX());
			float g = (float) (MathHelper.lerp(tickDelta, this.prevPosY, this.y) - vec3d.getY());
			float h = (float) (MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - vec3d.getZ());

			MatrixStack matrixStack = new MatrixStack();
			matrixStack.translate(f, g, h);
			matrixStack.scale(widthMultiplier, -heightMultiplier, widthMultiplier);
			matrixStack.translate(0, -1, 0);
			VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
			VertexConsumer vertexConsumer2 = immediate.getBuffer(layer1);

			int light = this.getBrightness(tickDelta);
			this.waveModel.render(matrixStack, vertexConsumer2, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0f);

			immediate.draw();
		}
		if (age <= this.wave1End) {
			int frame1 = Math.round(((float) this.age / (float) this.wave1End) * 12);

			this.texture1 = new Identifier(Effective.MODID, "textures/entity/splash/lava_splash_" + frame1 + ".png");
			this.layer1 = RenderLayer.getEntityTranslucent(texture1);

			Vec3d vec3d = camera.getPos();
			float f = (float) (MathHelper.lerp(tickDelta, this.prevPosX, this.x) - vec3d.getX());
			float g = (float) (MathHelper.lerp(tickDelta, this.prevPosY, this.y) - vec3d.getY());
			float h = (float) (MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - vec3d.getZ());

			MatrixStack matrixStack = new MatrixStack();
			matrixStack.translate(f, g, h);
			matrixStack.scale(widthMultiplier, heightMultiplier, widthMultiplier);
			matrixStack.translate(0, 0.001, 0);
			VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
			VertexConsumer vertexConsumer2 = immediate.getBuffer(layer1);

			int light = this.getBrightness(tickDelta);
			this.waveBottomModel.render(matrixStack, vertexConsumer2, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0f);

			immediate.draw();
		}

		// second splash
		if (age >= this.wave2Start) {
			int frame2 = Math.round(((float) (this.age - wave2Start) / (float) (this.wave2End - this.wave2Start)) * 12);

			this.texture2 = new Identifier(Effective.MODID, "textures/entity/splash/lava_splash_" + frame2 + ".png");
			this.layer2 = RenderLayer.getEntityTranslucent(texture2);

			Vec3d vec3d = camera.getPos();
			float f = (float) (MathHelper.lerp(tickDelta, this.prevPosX, this.x) - vec3d.getX());
			float g = (float) (MathHelper.lerp(tickDelta, this.prevPosY, this.y) - vec3d.getY());
			float h = (float) (MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - vec3d.getZ());

			MatrixStack matrixStack = new MatrixStack();
			matrixStack.translate(f, g, h);
			matrixStack.scale(widthMultiplier * 0.5f, -heightMultiplier * 2, widthMultiplier * 0.5f);
			matrixStack.translate(0, -1, 0);
			VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
			VertexConsumer vertexConsumer2 = immediate.getBuffer(layer2);

			int light = this.getBrightness(tickDelta);
			this.waveModel.render(matrixStack, vertexConsumer2, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0f);

			immediate.draw();
		}
		if (age >= this.wave2Start) {
			int frame2 = Math.round(((float) (this.age - wave2Start) / (float) (this.wave2End - this.wave2Start)) * 12);

			this.texture2 = new Identifier(Effective.MODID, "textures/entity/splash/lava_splash_" + frame2 + ".png");
			this.layer2 = RenderLayer.getEntityTranslucent(texture2);

			Vec3d vec3d = camera.getPos();
			float f = (float) (MathHelper.lerp(tickDelta, this.prevPosX, this.x) - vec3d.getX());
			float g = (float) (MathHelper.lerp(tickDelta, this.prevPosY, this.y) - vec3d.getY());
			float h = (float) (MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - vec3d.getZ());

			MatrixStack matrixStack = new MatrixStack();
			matrixStack.translate(f, g, h);
			matrixStack.scale(widthMultiplier * 0.5f, heightMultiplier * 2, widthMultiplier * 0.5f);
			matrixStack.translate(0, 0.001, 0);
			VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
			VertexConsumer vertexConsumer2 = immediate.getBuffer(layer2);

			int light = this.getBrightness(tickDelta);
			this.waveBottomModel.render(matrixStack, vertexConsumer2, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0f);

			immediate.draw();
		}
	}

	@Override
	public void tick() {
		if (this.widthMultiplier == 0f) {
			List<Entity> closeEntities = world.getOtherEntities(null, this.getBoundingBox().expand(5.0f)).stream().filter(entity -> world.getBlockState(entity.getBlockPos().add(MathHelper.floor(entity.getVelocity().x), MathHelper.floor(entity.getVelocity().y), MathHelper.floor(entity.getVelocity().z))).getBlock() == Blocks.LAVA).collect(Collectors.toList());
			closeEntities.sort((o1, o2) -> (int) (o1.getPos().squaredDistanceTo(new Vec3d(this.x, this.y, this.z)) - o2.getPos().squaredDistanceTo(new Vec3d(this.x, this.y, this.z))));

			if (!closeEntities.isEmpty()) {
				this.widthMultiplier = closeEntities.get(0).getWidth() * 2f;
				this.heightMultiplier = (float) Math.max(-closeEntities.get(0).getVelocity().getY() * this.widthMultiplier, 0f);

				this.wave1End = 10 + Math.round(widthMultiplier * 1.2f);
				this.wave2Start = 6 + Math.round(widthMultiplier * 0.7f);
				this.wave2End = 20 + Math.round(widthMultiplier * 2.4f);
			} else {
				this.markDead();
			}
		}

		this.prevPosX = this.x;
		this.prevPosY = this.y;
		this.prevPosZ = this.z;

		this.widthMultiplier *= 1.03f;

		if (this.age++ >= this.wave2End) {
			this.markDead();
		}

		if (this.age == 1) {
			for (int i = 0; i < this.widthMultiplier * 10f; i++) {
				this.world.addParticle(ParticleTypes.LAVA, this.x + (this.random.nextGaussian() * this.widthMultiplier / 10f), this.y, this.z + (this.random.nextGaussian() * this.widthMultiplier / 10f), random.nextGaussian() / 10f * this.widthMultiplier / 2.5f, random.nextFloat() / 10f + this.heightMultiplier / 2.8f, random.nextGaussian() / 10f * this.widthMultiplier / 2.5f);
			}
		} else if (this.age == wave2Start) {
			for (int i = 0; i < this.widthMultiplier * 5f; i++) {
				this.world.addParticle(ParticleTypes.LAVA, this.x + (this.random.nextGaussian() * this.widthMultiplier / 10f * .5f), this.y, this.z + (this.random.nextGaussian() * this.widthMultiplier / 10f * .5f), random.nextGaussian() / 10f * this.widthMultiplier / 5f, random.nextFloat() / 10f + this.heightMultiplier / 2.2f, random.nextGaussian() / 10f * this.widthMultiplier / 5f);
			}
		}
	}

	@Environment(EnvType.CLIENT)
	public static class DefaultFactory implements ParticleFactory<DefaultParticleType> {
		private final Identifier texture;

		public DefaultFactory(SpriteProvider spriteProvider, Identifier texture) {
			this.texture = texture;
		}

		@Nullable
		@Override
		public Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			return new LavaSplashParticle(world, x, y, z, this.texture);
		}
	}
}
