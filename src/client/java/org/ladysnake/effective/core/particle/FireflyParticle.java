package org.ladysnake.effective.core.particle;

import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.client.render.light.PointLight;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.ladysnake.effective.core.Effective;
import org.ladysnake.effective.core.index.EffectiveLights;
import org.ladysnake.effective.core.settings.SpawnSettings;
import org.ladysnake.effective.core.settings.data.FireflySpawnSetting;
import org.ladysnake.effective.core.utils.EffectiveUtils;
import org.ladysnake.effective.cosmetics.EffectiveCosmetics;

import java.awt.*;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class FireflyParticle extends SpriteBillboardParticle {
	protected static final float BLINK_STEP = 0.05f;
	protected float nextAlphaGoal = 0f;
	protected double xTarget;
	protected double yTarget;
	protected double zTarget;
	protected int maxHeight;
	private BlockPos lightTarget;

	PointLight light;

	public FireflyParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
		super(world, x, y, z, velocityX, velocityY, velocityZ);

		this.setSprite(spriteProvider);

		this.maxAge = ThreadLocalRandom.current().nextInt(400, 1200); // live between 20 seconds and one minute
		this.maxHeight = 4;
		this.alpha = 0f;
		this.collidesWithWorld = false;
		this.scale = 0.02f + random.nextFloat() * 0.02f;

		Color color = Color.GREEN;
		RegistryEntry<Biome> biome = world.getBiome(BlockPos.ofFloored(x, y, z));
		if (biome.getKey().isPresent()) {
			FireflySpawnSetting fireflySpawnSetting = SpawnSettings.FIREFLIES.get(biome.getKey().get());
			if (fireflySpawnSetting != null) {
				color = fireflySpawnSetting.color();
			}
		}
		this.red = color.getRed() / 255f;
		this.green = color.getGreen() / 255f;
		this.blue = color.getBlue() / 255f;

		this.light = new PointLight();
		this.light.setBrightness(0f);
		this.light.setColor(color.getRGB());
		this.light.setRadius(25f * this.scale);
		this.light.setPosition(x, y, z);
		EffectiveLights.PARTICLE_LIGHTS.add(this.light);
		VeilRenderSystem.renderer().getLightRenderer().addLight(light);
	}

	@Override
	protected int getBrightness(float tint) {
		return LightmapTextureManager.MAX_LIGHT_COORDINATE;
	}

	@Override
	public void markDead() {
		super.markDead();
		VeilRenderSystem.renderer().getLightRenderer().removeLight(this.light);
	}

	public static boolean canFlyThroughBlock(World world, BlockPos blockPos, BlockState blockState) {
		return !blockState.shouldSuffocate(world, blockPos) && blockState.getFluidState().isEmpty();
	}

	public void tick() {
		this.prevPosX = this.x;
		this.prevPosY = this.y;
		this.prevPosZ = this.z;

		this.light.setPosition(this.x, this.y, this.z);

		// fade and die on daytime or if old enough unless fireflies can spawn any time of day
		if ((!world.getDimension().hasFixedTime() && !EffectiveCosmetics.isNightTime(world)) || this.age++ >= this.maxAge) {
			nextAlphaGoal = 0;
			if (this.alpha <= 0.01f) {
				this.markDead();
			}
		}

		// blinking
		if (this.alpha > nextAlphaGoal - BLINK_STEP && this.alpha < nextAlphaGoal + BLINK_STEP) {
			nextAlphaGoal = random.nextFloat();
		} else {
			if (nextAlphaGoal > this.alpha) {
				this.alpha = Math.min(this.alpha + BLINK_STEP, 1f);
			} else if (nextAlphaGoal < this.alpha) {
				this.alpha = Math.max(this.alpha - BLINK_STEP, 0f);
			}
		}
		this.light.setBrightness(this.alpha * 4f);

//		this.targetChangeCooldown -= (new Vec3d(x, y, z).squaredDistanceTo(prevPosX, prevPosY, prevPosZ) < 0.0125) ? 10 : 1;

		if (random.nextInt(20) == 0 || (xTarget == 0 && yTarget == 0 && zTarget == 0) || new Vec3d(x, y, z).squaredDistanceTo(xTarget, yTarget, zTarget) < 9) {
			selectBlockTarget();
		}

		Vec3d targetVector = new Vec3d(this.xTarget - this.x, this.yTarget - this.y, this.zTarget - this.z);
		double length = targetVector.length();
		targetVector = targetVector.multiply(0.1 / length);

		BlockPos blockPos = BlockPos.ofFloored(this.x, this.y - 0.1, this.z);
		if (!canFlyThroughBlock(this.world, blockPos, this.world.getBlockState(blockPos))) {
			velocityX = (0.9) * velocityX + (0.1) * targetVector.x;
			velocityY = 0.05;
			velocityZ = (0.9) * velocityZ + (0.1) * targetVector.z;
		} else {
			velocityX = (0.9) * velocityX + (0.1) * targetVector.x;
			velocityY = (0.9) * velocityY + (0.1) * targetVector.y;
			velocityZ = (0.9) * velocityZ + (0.1) * targetVector.z;
		}
		if (!BlockPos.ofFloored(x, y, z).equals(this.getTargetPosition())) {
			this.move(velocityX, velocityY, velocityZ);
		}
	}

	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
	}

	private void selectBlockTarget() {
		if (this.lightTarget == null) {
			// Behaviour
			double groundLevel = 0;
			for (int i = 0; i < 20; i++) {
				BlockPos checkedPos = BlockPos.ofFloored(this.x, this.y - i, this.z);
				BlockState checkedBlock = this.world.getBlockState(checkedPos);
				if (canFlyThroughBlock(this.world, checkedPos, checkedBlock)) {
					groundLevel = this.y - i;
				}
				if (groundLevel != 0) break;
			}

			this.xTarget = this.x + EffectiveUtils.getRandomFloatOrNegative(this.random) * 10;
			this.yTarget = Math.min(Math.max(this.y + EffectiveUtils.getRandomFloatOrNegative(this.random) * 2, groundLevel), groundLevel + maxHeight);
			this.zTarget = this.z + EffectiveUtils.getRandomFloatOrNegative(this.random) * 10;

			BlockPos targetPos = BlockPos.ofFloored(this.xTarget, this.yTarget, this.zTarget);
			if (!canFlyThroughBlock(this.world, targetPos, this.world.getBlockState(targetPos))) {
				this.yTarget += 1;
			}

			this.lightTarget = getMostLitBlockAround();
		} else {
			this.xTarget = this.lightTarget.getX() + EffectiveUtils.getRandomFloatOrNegative(this.random);
			this.yTarget = this.lightTarget.getY() + EffectiveUtils.getRandomFloatOrNegative(this.random);
			this.zTarget = this.lightTarget.getZ() + EffectiveUtils.getRandomFloatOrNegative(this.random);

			if (this.world.getLightLevel(LightType.BLOCK, BlockPos.ofFloored(x, y, z)) > 0 && !this.world.isDay()) {
				this.lightTarget = getMostLitBlockAround();
			} else {
				this.lightTarget = null;
			}
		}

//		targetChangeCooldown = random.nextInt() % 100;
	}

	public BlockPos getTargetPosition() {
		return BlockPos.ofFloored(this.xTarget, this.yTarget + 0.5, this.zTarget);
	}

	private BlockPos getMostLitBlockAround() {
		HashMap<BlockPos, Integer> randBlocks = new HashMap<>();

		// get blocks adjacent to the fly
		for (int x = -1; x <= 1; x++) {
			for (int y = -1; y <= 1; y++) {
				for (int z = -1; z <= 1; z++) {
					BlockPos bp = BlockPos.ofFloored(this.x + x, this.y + y, this.z + z);
					randBlocks.put(bp, this.world.getLightLevel(LightType.BLOCK, bp));
				}
			}
		}

		// get other random blocks to find a different light source
		for (int i = 0; i < 15; i++) {
			BlockPos randBP = BlockPos.ofFloored(this.x + EffectiveUtils.getRandomFloatOrNegative(this.random) * 10, this.y + EffectiveUtils.getRandomFloatOrNegative(this.random) * 10, this.z + EffectiveUtils.getRandomFloatOrNegative(this.random) * 10);
			randBlocks.put(randBP, this.world.getLightLevel(LightType.BLOCK, randBP));
		}

		return randBlocks.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey();
	}

	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleFactory<SimpleParticleType> {
		private final SpriteProvider spriteProvider;

		public Factory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		@Override
		public Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			return new FireflyParticle(world, x, y, z, velocityX, velocityY, velocityZ, this.spriteProvider);
		}
	}
}
