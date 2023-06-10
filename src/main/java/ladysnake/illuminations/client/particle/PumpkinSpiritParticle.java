package ladysnake.illuminations.client.particle;

import net.minecraft.block.Blocks;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PumpkinSpiritParticle extends WillOWispParticle {
	protected PumpkinSpiritParticle(ClientWorld world, double x, double y, double z, Identifier texture, float red, float green, float blue, float redEvolution, float greenEvolution, float blueEvolution) {
		super(world, x, y, z, texture, red, green, blue, redEvolution, greenEvolution, blueEvolution);
	}


	@Override
	public void tick() {
		if (this.prevPosX == this.x && this.prevPosY == this.y && this.prevPosZ == this.z) {
			this.selectBlockTarget();
		}

		if (this.age < 5) {
			for (int i = 0; i < 25; i++) {
				this.world.addParticle(new WispTrailParticleEffect(this.colorRed, this.colorGreen, this.colorBlue, this.redEvolution, this.greenEvolution, this.blueEvolution), this.x + random.nextGaussian() / 15, this.y + random.nextGaussian() / 15, this.z + random.nextGaussian() / 15, 0, 0, 0);
			}
		}

		this.prevPosX = this.x;
		this.prevPosY = this.y;
		this.prevPosZ = this.z;

		if (this.age++ >= this.maxAge) {
			for (int i = 0; i < 25; i++) {
				this.world.addParticle(new WispTrailParticleEffect(this.colorRed, this.colorGreen, this.colorBlue, this.redEvolution, this.greenEvolution, this.blueEvolution), this.x + random.nextGaussian() / 15, this.y + random.nextGaussian() / 15, this.z + random.nextGaussian() / 15, 0, 0, 0);
				this.world.addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.JACK_O_LANTERN.getDefaultState()), this.x + random.nextGaussian() / 10, this.y + random.nextGaussian() / 10, this.z + random.nextGaussian() / 10, random.nextGaussian() / 20, random.nextGaussian() / 20, random.nextGaussian() / 20);
			}
			this.world.playSound(BlockPos.create(this.x, this.y, this.z), SoundEvents.ENTITY_VEX_DEATH, SoundCategory.AMBIENT, 1.0f, 0.8f, true);
			this.world.playSound(BlockPos.create(this.x, this.y, this.z), SoundEvents.BLOCK_WOOD_BREAK, SoundCategory.AMBIENT, 1.0f, 1.0f, true);
			this.markDead();
		}

		this.targetChangeCooldown -= (new Vec3d(x, y, z).squaredDistanceTo(prevPosX, prevPosY, prevPosZ) < 0.0125) ? 10 : 1;

		if ((this.world.getTime() % 100 == 0) && ((xTarget == 0 && yTarget == 0 && zTarget == 0) || new Vec3d(x, y, z).squaredDistanceTo(xTarget, yTarget, zTarget) < 9 || targetChangeCooldown <= 0)) {
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

		for (int i = 0; i < 10 * this.speedModifier; i++) {
			this.world.addParticle(new WispTrailParticleEffect(this.colorRed, this.colorGreen, this.colorBlue, this.redEvolution, this.greenEvolution, this.blueEvolution), this.x + random.nextGaussian() / 15, this.y + random.nextGaussian() / 15, this.z + random.nextGaussian() / 15, 0, 0, 0);
		}

		if (!BlockPos.create(x, y, z).equals(this.getTargetPosition())) {
			this.move(velocityX, velocityY, velocityZ);
		}

		if (random.nextInt(100) == 0) {
			this.world.playSound(BlockPos.create(this.x, this.y, this.z), SoundEvents.ENTITY_VEX_AMBIENT, SoundCategory.AMBIENT, 1.0f, 0.8f, true);
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
		if (this.collidesWithWorld && (dx != 0.0D || dy != 0.0D || dz != 0.0D)) {
			Vec3d vec3d = Entity.adjustSingleAxisMovementForCollisions(null, new Vec3d(dx, dy, dz), this.getBoundingBox(), this.world, List.of());

			dx = vec3d.x;
			dy = vec3d.y;
			dz = vec3d.z;
		}

		if (dx != 0.0D || dy != 0.0D || dz != 0.0D) {
			this.setBoundingBox(this.getBoundingBox().offset(dx, dy, dz));
			this.repositionFromBoundingBox();
		}

		this.onGround = dy != dy && e < 0.0D;
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
		if (this.world.getBlockState(targetPos).isFullCube(world, targetPos)) {
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
		private final float redEvolution;
		private final float greenEvolution;
		private final float blueEvolution;

		public DefaultFactory(SpriteProvider spriteProvider, Identifier texture, float red, float green, float blue, float redEvolution, float greenEvolution, float blueEvolution) {
			this.texture = texture;
			this.red = red;
			this.green = green;
			this.blue = blue;
			this.redEvolution = redEvolution;
			this.greenEvolution = greenEvolution;
			this.blueEvolution = blueEvolution;
		}

		@Nullable
		@Override
		public Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			return new PumpkinSpiritParticle(world, x, y, z, this.texture, this.red, this.green, this.blue, this.redEvolution, this.greenEvolution, this.blueEvolution);
		}
	}
}
