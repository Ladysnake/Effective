package org.ladysnake.effective.mixin;

import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Holder;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.dimension.DimensionType;
import org.ladysnake.effective.Effective;
import org.ladysnake.effective.EffectiveConfig;
import org.ladysnake.effective.particle.FireflyParticle;
import org.ladysnake.effective.settings.SpawnSettings;
import org.ladysnake.effective.settings.data.FireflySpawnSetting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.lodestar.lodestone.systems.rendering.particle.LodestoneWorldParticleTextureSheet;
import team.lodestar.lodestone.systems.rendering.particle.WorldParticleBuilder;
import team.lodestar.lodestone.systems.rendering.particle.data.ColorParticleData;
import team.lodestar.lodestone.systems.rendering.particle.data.GenericParticleData;

import java.time.LocalDate;
import java.time.Month;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

@Mixin(ClientWorld.class)
public abstract class ParticleSpawningClientWorldMixin extends World {
	@Shadow
	@Final
	private WorldRenderer worldRenderer;

	protected ParticleSpawningClientWorldMixin(MutableWorldProperties worldProperties, RegistryKey<World> registryKey, DynamicRegistryManager registryManager, Holder<DimensionType> dimension, Supplier<Profiler> profiler, boolean client, boolean debug, long seed, int maxChainedNeighborUpdates) {
		super(worldProperties, registryKey, registryManager, dimension, profiler, client, debug, seed, maxChainedNeighborUpdates);
	}

	@Inject(method = "randomBlockDisplayTick", slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/Biome;getParticleConfig()Ljava/util/Optional;")),
		at = @At(value = "INVOKE", target = "Ljava/util/Optional;ifPresent(Ljava/util/function/Consumer;)V", ordinal = 0, shift = At.Shift.AFTER))
	private void randomBlockDisplayTick(int centerX, int centerY, int centerZ, int radius, RandomGenerator random, @Coerce Object blockParticle, BlockPos.Mutable blockPos, CallbackInfo ci) {
		BlockPos.Mutable pos = blockPos.add(MathHelper.floor(this.random.nextGaussian() * 50), MathHelper.floor(this.random.nextGaussian() * 10), MathHelper.floor(this.random.nextGaussian() * 50)).mutableCopy();
		Holder<Biome> biome = this.getBiome(pos);

		// FIREFLIES
		if (EffectiveConfig.fireflyDensity > 0) {
			FireflySpawnSetting fireflySpawnSetting = SpawnSettings.FIREFLIES.get(biome.getKey().get());
			if (fireflySpawnSetting != null && FireflyParticle.canFlyThroughBlock(this, pos, this.getBlockState(pos))) {
				if (random.nextFloat() * 250f <= fireflySpawnSetting.spawnChance() * EffectiveConfig.fireflyDensity) {
					WorldParticleBuilder.create(Effective.FIREFLY)
						.setColorData(ColorParticleData.create(fireflySpawnSetting.color(), fireflySpawnSetting.color()).build())
						.setScaleData(GenericParticleData.create(0.05f + random.nextFloat() * 0.10f).build())
						.setLifetime(ThreadLocalRandom.current().nextInt(40, 120))
						.setRenderType(LodestoneWorldParticleTextureSheet.ADDITIVE)
						.spawn(this, pos.getX() + random.nextFloat(), pos.getY() + random.nextFloat(), pos.getZ() + random.nextFloat());
				}
			}
		}

		pos = blockPos.add(MathHelper.floor(this.random.nextGaussian() * 50), MathHelper.floor(this.random.nextGaussian() * 25), MathHelper.floor(this.random.nextGaussian() * 50)).mutableCopy();

		// WILL O' WISP
		if (EffectiveConfig.willOWispDensity > 0) {
			if (biome.isRegistryKey(Biomes.SOUL_SAND_VALLEY)) {
				if (random.nextFloat() * 100f <= 0.01f * EffectiveConfig.willOWispDensity) {
					if (this.getBlockState(pos).isIn(BlockTags.SOUL_FIRE_BASE_BLOCKS)) {
						this.addParticle(Effective.WILL_O_WISP, pos.getX(), pos.getY(), pos.getZ(), 0, 0, 0);
					}
				}
			}
		}

		// EYES IN THE DARK
		if ((EffectiveConfig.eyesInTheDark == EffectiveConfig.EyesInTheDarkOptions.ALWAYS || (EffectiveConfig.eyesInTheDark == EffectiveConfig.EyesInTheDarkOptions.HALLOWEEN && LocalDate.now().getMonth() == Month.OCTOBER))
			&& random.nextFloat() <= 0.00002f) {
			this.addParticle(Effective.EYES, (double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5, 0.0D, 0.0D, 0.0D);
		}
	}
}
