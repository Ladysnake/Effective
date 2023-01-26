package ladysnake.illuminations.mixin;

import com.google.common.collect.ImmutableSet;
import ladysnake.illuminations.client.Illuminations;
import ladysnake.illuminations.client.config.Config;
import ladysnake.illuminations.client.data.IlluminationData;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.Holder;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin extends World {
	protected ClientWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, Holder<DimensionType> dimension, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long seed, int maxChainedNeighborUpdates) {
		super(properties, registryRef, dimension, profiler, isClient, debugWorld, seed, maxChainedNeighborUpdates);
	}

	@Inject(method = "randomBlockDisplayTick", slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/Biome;getParticleConfig()Ljava/util/Optional;")),
			at = @At(value = "INVOKE", target = "Ljava/util/Optional;ifPresent(Ljava/util/function/Consumer;)V", ordinal = 0, shift = At.Shift.AFTER))
	private void randomBlockDisplayTick(int centerX, int centerY, int centerZ, int radius, RandomGenerator random, @Coerce Object blockParticle, BlockPos.Mutable blockPos, CallbackInfo ci) {
		BlockPos.Mutable pos = blockPos.add(this.random.nextGaussian() * 50, this.random.nextGaussian() * 25, this.random.nextGaussian() * 50).mutableCopy();

		Holder<Biome> b = this.getBiome(pos);
		Identifier biome = this.getRegistryManager().get(RegistryKeys.BIOME).getId(b.value());
		spawnParticles(pos, Illuminations.ILLUMINATIONS_BIOME_CATEGORIES.get(biome));

		// Other miscellaneous biome settings
		if (Illuminations.ILLUMINATIONS_BIOMES.containsKey(biome)) {
			ImmutableSet<IlluminationData> illuminationDataSet = Illuminations.ILLUMINATIONS_BIOMES.get(biome);
			spawnParticles(pos, illuminationDataSet);
		}

		// spooky eyes
		if (Illuminations.EYES_LOCATION_PREDICATE.test(this, pos)
				&& random.nextFloat() <= Config.getEyesInTheDarkSpawnRate().spawnRate) {
			this.addParticle(Illuminations.EYES, (double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5, 0.0D, 0.0D, 0.0D);
		}

		// soul lanterns on soul fire blocks
		if (this.getBlockState(pos).getBlock() == Blocks.SOUL_LANTERN && this.getBlockState(pos.add(0, -1, 0)).isIn(BlockTags.SOUL_FIRE_BASE_BLOCKS) && random.nextInt(50) == 0) {
			this.addParticle(Illuminations.WILL_O_WISP, true, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0f, 0f, 0f);
		}
	}

	private void spawnParticles(BlockPos.Mutable pos, ImmutableSet<IlluminationData> illuminationDataSet) {
		if (illuminationDataSet != null) {
			for (IlluminationData illuminationData : illuminationDataSet) {
				if (illuminationData.locationSpawnPredicate().test(this, pos)
						&& illuminationData.shouldAddParticle(this.random)) {
					this.addParticle(illuminationData.illuminationType(), pos.getX(), pos.getY(), pos.getZ(), 0.0D, 0.0D, 0.0D);
				}
			}
		}
	}

	@Inject(method = "addPlayer", at = @At(value = "RETURN"))
	public void addPlayer(int id, AbstractClientPlayerEntity player, CallbackInfo ci) {
		Illuminations.loadPlayerCosmetics();
	}

}
