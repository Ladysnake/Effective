package org.ladysnake.effective.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.sound.TickableSoundInstance;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.dimension.DimensionType;
import org.ladysnake.effective.index.EffectiveAmbientConditions;
import org.ladysnake.effective.sound.AmbientCondition;
import org.ladysnake.effective.sound.BiomeAmbientLoop;
import org.ladysnake.effective.Effective;
import org.ladysnake.effective.EffectiveConfig;
import org.ladysnake.effective.index.EffectiveParticles;
import org.ladysnake.effective.particle.FireflyParticle;
import org.ladysnake.effective.settings.SpawnSettings;
import org.ladysnake.effective.settings.data.FireflySpawnSetting;
import org.ladysnake.effective.utils.EffectiveUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.time.LocalDate;
import java.time.Month;
import java.util.function.Supplier;

@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin extends World {
	@Shadow
	@Final
	private MinecraftClient client;

	@Shadow
	@Final
	private WorldRenderer worldRenderer;

	protected ClientWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates) {
		super(properties, registryRef, registryManager, dimensionEntry, profiler, isClient, debugWorld, biomeAccess, maxChainedNeighborUpdates);
	}

	@ModifyConstant(method = "doRandomBlockDisplayTicks", constant = @Constant(intValue = 667))
	public int effective$multiplyRandomBlockDisplayTicksFrequency(int constant) {
		return Math.round(667 * EffectiveConfig.randomBlockDisplayTicksFrequencyMultiplier);
	}

	@ModifyConstant(method = "doRandomBlockDisplayTicks", constant = @Constant(intValue = 16))
	public int effective$overwriteRandomBlockDisplayTicksDistanceClose(int constant) {
		return EffectiveConfig.randomBlockDisplayTicksDistanceClose;
	}

	@ModifyConstant(method = "doRandomBlockDisplayTicks", constant = @Constant(intValue = 32))
	public int effective$overwriteRandomBlockDisplayTicksDistanceFar(int constant) {
		return EffectiveConfig.randomBlockDisplayTicksDistanceFar;
	}

	@WrapWithCondition(method = "doRandomBlockDisplayTicks", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;randomBlockDisplayTick(IIIILnet/minecraft/util/math/random/Random;Lnet/minecraft/block/Block;Lnet/minecraft/util/math/BlockPos$Mutable;)V", ordinal = 0))
	public boolean effective$cancelRandomBlockDisplayTicksClose(ClientWorld instance, int centerX, int centerY, int centerZ, int radius, Random random, Block block, BlockPos.Mutable pos) {
		return EffectiveConfig.randomBlockDisplayTicksDistanceClose > 0;
	}

	@WrapWithCondition(method = "doRandomBlockDisplayTicks", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;randomBlockDisplayTick(IIIILnet/minecraft/util/math/random/Random;Lnet/minecraft/block/Block;Lnet/minecraft/util/math/BlockPos$Mutable;)V", ordinal = 1))
	public boolean effective$cancelRandomBlockDisplayTicksFar(ClientWorld instance, int centerX, int centerY, int centerZ, int radius, Random random, Block block, BlockPos.Mutable pos) {
		return EffectiveConfig.randomBlockDisplayTicksDistanceFar > 0;
	}

	@Inject(method = "randomBlockDisplayTick", slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/Biome;getParticleConfig()Ljava/util/Optional;")),
		at = @At(value = "INVOKE", target = "Ljava/util/Optional;ifPresent(Ljava/util/function/Consumer;)V", ordinal = 0, shift = At.Shift.AFTER))
	private void effective$spawnEffectsFromRandomBlockDisplayTicks(int centerX, int centerY, int centerZ, int radius, Random random, Block block, BlockPos.Mutable blockPos, CallbackInfo ci) {
		BlockPos.Mutable pos = blockPos.add(MathHelper.floor(EffectiveUtils.getRandomFloatOrNegative(this.random) * 50), MathHelper.floor(EffectiveUtils.getRandomFloatOrNegative(this.random) * 10), MathHelper.floor(EffectiveUtils.getRandomFloatOrNegative(this.random) * 50)).mutableCopy();
		BlockPos.Mutable pos2 = pos.mutableCopy();
		RegistryEntry<Biome> biome = this.getBiome(pos);

		// FIREFLIES
		if (EffectiveConfig.fireflyDensity > 0 && EffectiveUtils.isNightTime((ClientWorld) (Object) this)) {
			FireflySpawnSetting fireflySpawnSetting = SpawnSettings.FIREFLIES.get(biome.getKey().get());
			if (fireflySpawnSetting != null) {
				if (random.nextFloat() * 250f <= fireflySpawnSetting.spawnChance() * EffectiveConfig.fireflyDensity && pos.getY() > this.getSeaLevel()) {
					for (int y = this.getSeaLevel(); y <= this.getSeaLevel() * 2; y++) {
						pos.setY(y);
						pos2.setY(y - 1);
						boolean canSpawnFirefly = FireflyParticle.canFlyThroughBlock(this, pos, this.getBlockState(pos)) && !FireflyParticle.canFlyThroughBlock(this, pos2, this.getBlockState(pos2));

						if (canSpawnFirefly) {
							this.addParticle(EffectiveParticles.FIREFLY, pos.getX() + random.nextFloat(), pos.getY() + random.nextFloat() * 5f, pos.getZ() + random.nextFloat(), 0, 0, 0);
							break;
						}
					}
				}
			}
		}

		pos = blockPos.add(MathHelper.floor(EffectiveUtils.getRandomFloatOrNegative(this.random) * 50), MathHelper.floor(EffectiveUtils.getRandomFloatOrNegative(this.random) * 25), MathHelper.floor(EffectiveUtils.getRandomFloatOrNegative(this.random) * 50)).mutableCopy();

		// WILL O' WISP
		if (EffectiveConfig.willOWispDensity > 0) {
			if (biome.matchesKey(BiomeKeys.SOUL_SAND_VALLEY)) {
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

	@Inject(method = "tick", at = @At(value = "HEAD"))
	private void effective$playAmbience(CallbackInfo ci) {
		ClientPlayerEntity clientPlayerEntity = client.player;
		if (clientPlayerEntity != null) {
			for (AmbientCondition ambientCondition : EffectiveAmbientConditions.INSTANCE) {
				if (ambientCondition.predicate().shouldPlay(client.world, client.player.getBlockPos(), client.player)) {
					boolean allow = true;
					for (TickableSoundInstance tickingSound : client.getSoundManager().soundSystem.tickingSounds) {
						if (tickingSound != null && tickingSound.getId().equals(ambientCondition.event().getId())) {
							allow = false;
							break;
						}
					}
					if (allow) {
						client.getSoundManager().play(new BiomeAmbientLoop(clientPlayerEntity, ambientCondition.event(), ambientCondition));
					}
				}
			}
		}
	}
}
