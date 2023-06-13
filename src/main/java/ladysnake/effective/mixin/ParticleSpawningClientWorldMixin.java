package ladysnake.effective.mixin;

import ladysnake.effective.Effective;
import ladysnake.effective.EffectiveConfig;
import ladysnake.effective.particle.contracts.FireflyParticleInitialData;
import ladysnake.effective.settings.SpawnSettings;
import ladysnake.effective.settings.data.FireflySpawnSetting;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Holder;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.time.LocalDate;
import java.time.Month;
import java.util.function.Supplier;

@Mixin(ClientWorld.class)
public abstract class ParticleSpawningClientWorldMixin extends World {
	protected ParticleSpawningClientWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, Holder<DimensionType> dimension, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long seed, int maxChainedNeighborUpdates) {
		super(properties, registryRef, dimension, profiler, isClient, debugWorld, seed, maxChainedNeighborUpdates);
	}

	@Inject(method = "randomBlockDisplayTick", slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/Biome;getParticleConfig()Ljava/util/Optional;")),
		at = @At(value = "INVOKE", target = "Ljava/util/Optional;ifPresent(Ljava/util/function/Consumer;)V", ordinal = 0, shift = At.Shift.AFTER))
	private void randomBlockDisplayTick(int centerX, int centerY, int centerZ, int radius, RandomGenerator random, @Coerce Object blockParticle, BlockPos.Mutable blockPos, CallbackInfo ci) {
		BlockPos.Mutable pos = blockPos.add(this.random.nextGaussian() * 50, this.random.nextGaussian() * 25, this.random.nextGaussian() * 50).mutableCopy();

		Holder<Biome> biome = this.getBiome(pos);

		// FIREFLIES
		FireflySpawnSetting fireflySpawnSetting = SpawnSettings.FIREFLIES.get(biome);
		if (SpawnSettings.FIREFLIES.containsKey(biome)) {
			if (random.nextFloat() * 100 <= fireflySpawnSetting.spawnChance() * EffectiveConfig.fireflyDensity) {
				FireflyParticleInitialData data = new FireflyParticleInitialData(fireflySpawnSetting.color());
				this.addParticle(Effective.FIREFLY.setData(data), pos.getX(), pos.getY(), pos.getZ(), 0, 0, 0);
			}
		}

		// WILL O WISP
		if (biome.equals(BiomeKeys.SOUL_SAND_VALLEY.getValue())) {
			if (random.nextFloat() * 100 <= 0.01f * EffectiveConfig.willOWispDensity) {
				if (this.getBlockState(pos).isIn(BlockTags.SOUL_FIRE_BASE_BLOCKS)) {
					this.addParticle(Effective.WILL_O_WISP, pos.getX(), pos.getY(), pos.getZ(), 0, 0, 0);
				}
			}
		}

		// EYES IN THE DARK
		if ((EffectiveConfig.enableEyesInTheDark == EffectiveConfig.EyesInTheDarkOptions.ALWAYS || (EffectiveConfig.enableEyesInTheDark == EffectiveConfig.EyesInTheDarkOptions.HALLOWEEN && LocalDate.now().getMonth() == Month.OCTOBER))
			&& random.nextFloat() <= 0.00002f) {
			this.addParticle(Effective.EYES, (double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5, 0.0D, 0.0D, 0.0D);
		}
	}
}
