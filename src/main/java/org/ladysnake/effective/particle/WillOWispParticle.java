package org.ladysnake.effective.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.Model;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.ladysnake.effective.Effective;
import org.ladysnake.effective.LinearForcedMotionImpl;
import org.ladysnake.effective.cosmetics.particle.pet.PlayerWispParticle;
import org.ladysnake.effective.cosmetics.render.entity.model.pet.WillOWispModel;
import team.lodestar.lodestone.systems.rendering.particle.Easing;
import team.lodestar.lodestone.systems.rendering.particle.WorldParticleBuilder;
import team.lodestar.lodestone.systems.rendering.particle.data.ColorParticleData;
import team.lodestar.lodestone.systems.rendering.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.rendering.particle.data.SpinParticleData;

import java.awt.*;
import java.util.List;

public class WillOWispParticle extends Particle {
	public final Identifier texture;
	final Model model;
	final RenderLayer layer;
	public float yaw;
	public float pitch;
	public float prevYaw;
	public float prevPitch;
	public float speedModifier;
	protected float gotoRed;
	protected float gotoGreen;
	protected float gotoBlue;
	protected double xTarget;
	protected double yTarget;
	protected double zTarget;
	protected int targetChangeCooldown = 0;
	protected int timeInSolid = -1;

	protected WillOWispParticle(ClientWorld world, double x, double y, double z, Identifier texture, float red, float green, float blue, float gotoRed, float gotoGreen, float gotoBlue) {
		super(world, x, y, z);
		this.texture = texture;
		this.model = new WillOWispModel(MinecraftClient.getInstance().getEntityModelLoader().getModelPart(WillOWispModel.MODEL_LAYER));
		this.layer = RenderLayer.getEntityTranslucent(texture);
		this.gravityStrength = 0.0F;
		this.maxAge = 600 + random.nextInt(600);
		speedModifier = 0.1f + Math.max(0, random.nextFloat() - 0.1f);

		this.colorRed = red;
		this.colorGreen = green;
		this.colorBlue = blue;

		this.gotoRed = gotoRed;
		this.gotoBlue = gotoBlue;
		this.gotoGreen = gotoGreen;
	}

	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.CUSTOM;
	}

	@Override
	public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
		if (this.world.getBlockState(BlockPos.create(this.x, this.y, this.z)).isIn(BlockTags.SOUL_FIRE_BASE_BLOCKS)) {
			this.world.addParticle(ParticleTypes.SOUL, this.x + random.nextGaussian() / 10, this.y + random.nextGaussian() / 10, this.z + random.nextGaussian() / 10, random.nextGaussian() / 20, random.nextGaussian() / 20, random.nextGaussian() / 20);
		} else {
			float x = (float) (MathHelper.lerp(tickDelta, this.prevPosX, this.x));
			float y = (float) (MathHelper.lerp(tickDelta, this.prevPosY, this.y));
			float z = (float) (MathHelper.lerp(tickDelta, this.prevPosZ, this.z));

			for (int i = 0; i < 2; i++) {
				WorldParticleBuilder.create(Effective.WISP)
					.setSpinData(SpinParticleData.create((float) (this.world.random.nextGaussian() / 5f)).build())
					.setScaleData(
						GenericParticleData.create(this instanceof PlayerWispParticle ? 0.16f : 0.25f, 0f)
							.setEasing(Easing.CIRC_OUT)
							.build()
					)
					.setTransparencyData(GenericParticleData.create(1f).build())
					.setColorData(
						ColorParticleData.create(new Color(this.colorRed, this.colorGreen, this.colorBlue), new Color(this.gotoRed, this.gotoGreen, this.gotoBlue))
							.setEasing(Easing.CIRC_OUT)
							.build()
					)
					.setMotion(0, 0.066f, 0)
					.enableNoClip()
					.setLifetime(40)
					.spawn(this.world, x + random.nextGaussian() / 20f, y + random.nextGaussian() / 20f, z + random.nextGaussian() / 20f);
			}

			WorldParticleBuilder.create(Effective.WISP)
				.setSpinData(SpinParticleData.create((float) (this.world.random.nextGaussian() / 5f)).build())
				.setScaleData(GenericParticleData.create(this instanceof PlayerWispParticle ? 0.10f : 0.15f).build())
				.setTransparencyData(GenericParticleData.create(0.2f, 0f).build())
				.setColorData(ColorParticleData.create(new Color(0xFFFFFF), new Color(0xFFFFFF)).build())
				.setMotion(0, 0.066f, 0)
				.enableNoClip()
				.setLifetime(3)
				.spawn(this.world, x, y, z);
		}
	}

	@Override
	public void tick() {
		if (this.prevPosX == this.x && this.prevPosY == this.y && this.prevPosZ == this.z) {
			this.selectBlockTarget();
		}

		this.prevPosX = this.x;
		this.prevPosY = this.y;
		this.prevPosZ = this.z;

		if (this.age++ >= this.maxAge) {
			for (int i = 0; i < 50; i++) {
				WorldParticleBuilder.create(Effective.WISP)
					.setSpinData(SpinParticleData.create((float) (this.world.random.nextGaussian() / 5f)).build())
					.setScaleData(GenericParticleData.create(0.25f, 0f).setEasing(Easing.CIRC_OUT).build())
					.setTransparencyData(GenericParticleData.create(1f).build())
					.setColorData(
						ColorParticleData.create(new Color(this.colorRed, this.colorGreen, this.colorBlue), new Color(this.gotoRed, this.gotoGreen, this.gotoBlue))
							.setEasing(Easing.CIRC_OUT)
							.build()
					)
					.addActor(new LinearForcedMotionImpl(
						new Vector3f((float) (random.nextGaussian() / 10f), (float) (random.nextGaussian() / 10f), (float) (random.nextGaussian() / 10f)),
						new Vector3f(),
						1f
					))
					.enableNoClip()
					.setLifetime(20)
					.repeat(this.world, x, y, z, 3);
				this.world.addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.SOUL_SAND.getDefaultState()), this.x + random.nextGaussian() / 10, this.y + random.nextGaussian() / 10, this.z + random.nextGaussian() / 10, random.nextGaussian() / 20, random.nextGaussian() / 20, random.nextGaussian() / 20);
			}
			this.world.playSound(BlockPos.create(this.x, this.y, this.z), SoundEvents.PARTICLE_SOUL_ESCAPE, SoundCategory.AMBIENT, 1.0f, 1.5f, true);
			this.world.playSound(BlockPos.create(this.x, this.y, this.z), SoundEvents.BLOCK_SOUL_SAND_BREAK, SoundCategory.AMBIENT, 1.0f, 1.0f, true);
			this.markDead();
		}

		this.targetChangeCooldown -= (new Vec3d(x, y, z).squaredDistanceTo(prevPosX, prevPosY, prevPosZ) < 0.0125) ? 10 : 1;

		if ((this.world.getTime() % 20 == 0) && ((xTarget == 0 && yTarget == 0 && zTarget == 0) || new Vec3d(x, y, z).squaredDistanceTo(xTarget, yTarget, zTarget) < 9 || targetChangeCooldown <= 0)) {
			selectBlockTarget();
		}

		Vec3d targetVector = new Vec3d(this.xTarget - this.x, this.yTarget - this.y, this.zTarget - this.z);
		double length = targetVector.length();
		targetVector = targetVector.multiply(speedModifier / length);

		velocityX = (0.9) * velocityX + (0.1) * targetVector.x;
		velocityY = (0.9) * velocityY + (0.1) * targetVector.y;
		velocityZ = (0.9) * velocityZ + (0.1) * targetVector.z;

		this.prevYaw = this.yaw;
		this.prevPitch = this.pitch;
		Vec3d vec3d = new Vec3d(velocityX, velocityY, velocityZ);
		float f = (float) Math.sqrt(vec3d.x * vec3d.x + vec3d.z * vec3d.z);
		this.yaw = (float) (MathHelper.atan2(vec3d.x, vec3d.z) * 57.2957763671875D);
		this.pitch = (float) (MathHelper.atan2(vec3d.y, f) * 57.2957763671875D);

		if (!BlockPos.create(x, y, z).equals(this.getTargetPosition())) {
			this.move(velocityX, velocityY, velocityZ);
		}

		if (random.nextInt(20) == 0) {
			this.world.playSound(BlockPos.create(this.x, this.y, this.z), SoundEvents.PARTICLE_SOUL_ESCAPE, SoundCategory.AMBIENT, 1.0f, 1.5f, true);
		}

		BlockPos pos = BlockPos.create(this.x, this.y, this.z);
		if (!this.world.getBlockState(pos).isAir()) {
			if (timeInSolid > -1) {
				timeInSolid += 1;
			}
		} else {
			timeInSolid = 0;
		}

		if (timeInSolid > 25) {
			this.markDead();
		}
	}

	@Override
	public void move(double dx, double dy, double dz) {
		double d = dx;
		double e = dy;
		if (this.collidesWithWorld && !this.world.getBlockState(BlockPos.create(this.x + dx, this.y + dy, this.z + dz)).isIn(BlockTags.SOUL_FIRE_BASE_BLOCKS) && (dx != 0.0D || dy != 0.0D || dz != 0.0D)) {
			Vec3d vec3d = Entity.adjustSingleAxisMovementForCollisions(null, new Vec3d(dx, dy, dz), this.getBoundingBox(), this.world, List.of());

			dx = vec3d.x;
			dy = vec3d.y;
			dz = vec3d.z;
		}

		if (dx != 0.0D || dy != 0.0D || dz != 0.0D) {
			this.setBoundingBox(this.getBoundingBox().offset(dx, dy, dz));
			this.repositionFromBoundingBox();
		}

		this.onGround = dy != dy && e < 0.0D && !this.world.getBlockState(BlockPos.create(this.x, this.y, this.z)).isIn(BlockTags.SOUL_FIRE_BASE_BLOCKS);
		if (d != dx) {
			this.velocityX = 0.0D;
		}

		if (dz != dz) {
			this.velocityZ = 0.0D;
		}
	}

	public BlockPos getTargetPosition() {
		return BlockPos.create(this.xTarget, this.yTarget + 0.5, this.zTarget);
	}

	private void selectBlockTarget() {
		// Behaviour
		this.xTarget = this.x + random.nextGaussian() * 10;
		this.yTarget = this.y + random.nextGaussian() * 10;
		this.zTarget = this.z + random.nextGaussian() * 10;

		BlockPos targetPos = BlockPos.create(this.xTarget, this.yTarget, this.zTarget);
		if (this.world.getBlockState(targetPos).isFullCube(world, targetPos) && !this.world.getBlockState(targetPos).isIn(BlockTags.SOUL_FIRE_BASE_BLOCKS)) {
			targetChangeCooldown = 0;
			return;
		}

		speedModifier = 0.1f + Math.max(0, random.nextFloat() - 0.1f);
		targetChangeCooldown = random.nextInt() % (int) (100 / this.speedModifier);
	}


	public static class DefaultFactory implements ParticleFactory<DefaultParticleType> {
		private final Identifier texture;
		private final float red;
		private final float green;
		private final float blue;
		private final float toRed;
		private final float toGreen;
		private final float toBlue;

		public DefaultFactory(SpriteProvider spriteProvider, Identifier texture, float red, float green, float blue, float toRed, float toGreen, float toBlue) {
			this.texture = texture;
			this.red = red;
			this.green = green;
			this.blue = blue;
			this.toRed = toRed;
			this.toGreen = toGreen;
			this.toBlue = toBlue;
		}

		@Nullable
		@Override
		public Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			return new WillOWispParticle(world, x, y, z, this.texture, this.red, this.green, this.blue, this.toRed, this.toGreen, this.toBlue);
		}
	}
}
