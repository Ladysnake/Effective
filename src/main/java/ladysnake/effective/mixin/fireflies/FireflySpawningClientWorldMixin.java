package ladysnake.effective.mixin.fireflies;

import ladysnake.effective.client.Effective;
import ladysnake.effective.client.particle.contracts.FireflyParticleInitialData;
import ladysnake.effective.client.settings.SpawnSettings;
import ladysnake.effective.client.settings.data.FireflySpawnSetting;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Holder;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;
import org.joml.Vector3i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(ClientWorld.class)
public abstract class FireflySpawningClientWorldMixin extends World {
	protected FireflySpawningClientWorldMixin(MutableWorldProperties worldProperties, RegistryKey<World> registryKey, DynamicRegistryManager registryManager, Holder<DimensionType> dimension, Supplier<Profiler> profiler, boolean client, boolean debug, long seed, int maxChainedNeighborUpdates) {
		super(worldProperties, registryKey, registryManager, dimension, profiler, client, debug, seed, maxChainedNeighborUpdates);
	}

	@Inject(method = "randomBlockDisplayTick", slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/Biome;getParticleConfig()Ljava/util/Optional;")),
			at = @At(value = "INVOKE", target = "Ljava/util/Optional;ifPresent(Ljava/util/function/Consumer;)V", ordinal = 0, shift = At.Shift.AFTER))
	private void randomBlockDisplayTick(int centerX, int centerY, int centerZ, int radius, RandomGenerator random, @Coerce Object blockParticle, BlockPos.Mutable blockPos, CallbackInfo ci) {
		BlockPos.Mutable pos = blockPos.add(MathHelper.floor(this.random.nextGaussian() * 50), MathHelper.floor(this.random.nextGaussian() * 25), MathHelper.floor(this.random.nextGaussian() * 50)).mutableCopy();

		Holder<Biome> b = this.getBiome(pos);
		Identifier biome = this.getRegistryManager().get(RegistryKeys.BIOME).getId(b.value());

		FireflySpawnSetting fireflySpawnSetting = SpawnSettings.FIREFLIES.get(biome);

		if (SpawnSettings.FIREFLIES.containsKey(biome)) {
			if (random.nextFloat() * 100 <= fireflySpawnSetting.spawnChance()) {
				FireflyParticleInitialData data = new FireflyParticleInitialData(fireflySpawnSetting.color());
				this.addParticle(Effective.FIREFLY.setData(data), pos.getX(), pos.getY(), pos.getZ(), 0, 0, 0);
			}
		}
	}
}
