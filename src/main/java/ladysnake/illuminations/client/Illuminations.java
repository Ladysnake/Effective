package ladysnake.illuminations.client;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.mojang.serialization.Codec;
import ladysnake.illuminations.client.config.Config;
import ladysnake.illuminations.client.config.DefaultConfig;
import ladysnake.illuminations.client.data.AuraData;
import ladysnake.illuminations.client.data.IlluminationData;
import ladysnake.illuminations.client.data.OverheadData;
import ladysnake.illuminations.client.data.PlayerCosmeticData;
import ladysnake.illuminations.client.enums.HalloweenFeatures;
import ladysnake.illuminations.client.particle.*;
import ladysnake.illuminations.client.particle.aura.*;
import ladysnake.illuminations.client.particle.pet.*;
import ladysnake.illuminations.client.render.entity.feature.OverheadFeatureRenderer;
import ladysnake.illuminations.client.render.entity.model.hat.*;
import ladysnake.illuminations.client.render.entity.model.pet.LanternModel;
import ladysnake.illuminations.client.render.entity.model.pet.PrideHeartModel;
import ladysnake.illuminations.client.render.entity.model.pet.WillOWispModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.resource.loader.api.ResourceLoader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiPredicate;

@Environment(EnvType.CLIENT)
public class Illuminations implements ClientModInitializer {
	public static final String MODID = "illuminations";
	public static final Logger logger = LogManager.getLogger("Illuminations");

	// illuminations constants
	public static final int EYES_VANISHING_DISTANCE = 5;
	public static final Gson COSMETICS_GSON = new GsonBuilder().registerTypeAdapter(PlayerCosmeticData.class, new PlayerCosmeticDataParser()).create();

	// spawn predicates
	public static final BiPredicate<World, BlockPos> FIREFLY_LOCATION_PREDICATE = (world, blockPos) -> {
		Block block = world.getBlockState(blockPos).getBlock();
		return world.getDimension().hasFixedTime()
				? (block == Blocks.AIR || block == Blocks.VOID_AIR)
				: block == Blocks.AIR
				&& (Config.doesFireflySpawnAlways() || Illuminations.isNightTime(world))
				&& (Config.doesFireflySpawnUnderground() || world.getBlockState(blockPos).isOf(Blocks.AIR));
	};
	public static final BiPredicate<World, BlockPos> GLOWWORM_LOCATION_PREDICATE = (world, blockPos) -> world.getBlockState(blockPos).getBlock() == Blocks.CAVE_AIR;
	public static final BiPredicate<World, BlockPos> PLANKTON_LOCATION_PREDICATE = (world, blockPos) -> world.getBlockState(blockPos).getFluidState().isIn(FluidTags.WATER) && world.getLightLevel(blockPos) < 2;
	public static final BiPredicate<World, BlockPos> EYES_LOCATION_PREDICATE = (world, blockPos) -> ((Config.getHalloweenFeatures() == HalloweenFeatures.ENABLE && LocalDate.now().getMonth() == Month.OCTOBER) || Config.getHalloweenFeatures() == HalloweenFeatures.ALWAYS) && (world.getBlockState(blockPos).getBlock() == Blocks.AIR || world.getBlockState(blockPos).getBlock() == Blocks.CAVE_AIR) && world.getLightLevel(blockPos) <= 0 && world.getClosestPlayer(blockPos.getX(), blockPos.getY(), blockPos.getZ(), EYES_VANISHING_DISTANCE, false) == null;
	public static final BiPredicate<World, BlockPos> WISP_LOCATION_PREDICATE = (world, blockPos) -> world.getBlockState(blockPos).isIn(BlockTags.SOUL_FIRE_BASE_BLOCKS);

	// register overhead models
	public static final EntityModelLayer CROWN = new EntityModelLayer(new Identifier(Illuminations.MODID, "crown"), "main");
	static final Type COSMETIC_SELECT_TYPE = new TypeToken<Map<UUID, PlayerCosmeticData>>() {
	}.getType();

	// illuminations cosmetics
	private static final String COSMETICS_URL = "https://doctor4t.uuid.gg/illuminations-data";
	public static ImmutableMap<String, AuraData> AURAS_DATA;
	public static ImmutableMap<String, DefaultParticleType> PETS_DATA;
	public static ImmutableMap<String, OverheadData> OVERHEADS_DATA;

	// particle types
	public static DefaultParticleType FIREFLY;
	public static DefaultParticleType GLOWWORM;
	public static DefaultParticleType PLANKTON;
	public static DefaultParticleType EYES;
	public static DefaultParticleType CHORUS_PETAL;
	public static DefaultParticleType WILL_O_WISP;
	public static ParticleType<WispTrailParticleEffect> WISP_TRAIL;
	public static DefaultParticleType PUMPKIN_SPIRIT;
	public static DefaultParticleType POLTERGEIST;
	public static DefaultParticleType PRISMARINE_CRYSTAL;

	// auras
	public static DefaultParticleType TWILIGHT_AURA;
	public static DefaultParticleType GHOSTLY_AURA;
	public static DefaultParticleType CHORUS_AURA;
	public static DefaultParticleType AUTUMN_LEAVES_AURA;
	public static DefaultParticleType SCULK_TENDRIL_AURA;
	public static DefaultParticleType SHADOWBRINGER_AURA;
	public static DefaultParticleType GOLDENROD_AURA;
	public static DefaultParticleType CONFETTI_AURA;
	public static DefaultParticleType PRISMATIC_CONFETTI_AURA;
	public static DefaultParticleType PRISMARINE_AURA;
	// pets
	public static DefaultParticleType PRIDE_PET;
	public static DefaultParticleType GAY_PRIDE_PET;
	public static DefaultParticleType TRANS_PRIDE_PET;
	public static DefaultParticleType JACKO_PET;
	public static DefaultParticleType LESBIAN_PRIDE_PET;
	public static DefaultParticleType BI_PRIDE_PET;
	public static DefaultParticleType ACE_PRIDE_PET;
	public static DefaultParticleType NB_PRIDE_PET;
	public static DefaultParticleType INTERSEX_PRIDE_PET;
	public static DefaultParticleType ARO_PRIDE_PET;
	public static DefaultParticleType PAN_PRIDE_PET;
	public static DefaultParticleType AGENDER_PRIDE_PET;
	public static DefaultParticleType WILL_O_WISP_PET;
	public static DefaultParticleType GOLDEN_WILL_PET;
	public static DefaultParticleType FOUNDING_SKULL_PET;
	public static DefaultParticleType DISSOLUTION_WISP_PET;
	public static DefaultParticleType PUMPKIN_SPIRIT_PET;
	public static DefaultParticleType POLTERGEIST_PET;
	public static DefaultParticleType LANTERN_PET;
	public static DefaultParticleType SOUL_LANTERN_PET;
	public static DefaultParticleType CRYING_LANTERN_PET;
	public static DefaultParticleType SOOTHING_LANTERN_PET;
	public static DefaultParticleType GENDERFLUID_PRIDE_PET;
	// spawn biome categories and biomes
	public static ImmutableMap<Identifier, ImmutableSet<IlluminationData>> ILLUMINATIONS_BIOME_CATEGORIES;
	public static ImmutableMap<Identifier, ImmutableSet<IlluminationData>> ILLUMINATIONS_BIOMES;
	private static Map<UUID, PlayerCosmeticData> PLAYER_COSMETICS = Collections.emptyMap();

	public static @Nullable PlayerCosmeticData getCosmeticData(PlayerEntity player) {
		return PLAYER_COSMETICS.get(player.getUuid());
	}

	public static @Nullable PlayerCosmeticData getCosmeticData(UUID uuid) {
		return PLAYER_COSMETICS.get(uuid);
	}

	public static void loadPlayerCosmetics() {
		// get illuminations player cosmetics
		CompletableFuture.supplyAsync(() -> {
			try (Reader reader = new InputStreamReader(new URL(COSMETICS_URL).openStream())) {
				if (Config.isDebugMode())
					logger.log(Level.INFO, "Retrieving Illuminations cosmetics from the dashboard...");
				return COSMETICS_GSON.<Map<UUID, PlayerCosmeticData>>fromJson(reader, COSMETIC_SELECT_TYPE);
			} catch (MalformedURLException e) {
				if (Config.isDebugMode())
					logger.log(Level.ERROR, "Could not get player cosmetics because of malformed URL: " + e.getMessage());
			} catch (IOException e) {
				if (Config.isDebugMode())
					logger.log(Level.ERROR, "Could not get player cosmetics because of I/O Error: " + e.getMessage());
			}

			return null;
		}).exceptionally(throwable -> {
			if (Config.isDebugMode())
				logger.log(Level.ERROR, "Could not get player cosmetics because wtf is happening", throwable);
			return null;
		}).thenAcceptAsync(playerData -> {
			if (playerData != null) {
				PLAYER_COSMETICS = playerData;
				if (Config.isDebugMode()) logger.log(Level.INFO, "Player cosmetics successfully registered");
			} else {
				PLAYER_COSMETICS = Collections.emptyMap();
				if (Config.isDebugMode())
					logger.log(Level.WARN, "Player cosmetics could not registered, cosmetics will be ignored");
			}
		}, MinecraftClient.getInstance());
	}

	public static boolean isNightTime(World world) {
		return world.getSkyAngle(world.getTimeOfDay()) >= 0.25965086 && world.getSkyAngle(world.getTimeOfDay()) <= 0.7403491;
	}

	@Override
	public void onInitializeClient(ModContainer mod) {
		// load config
		Config.load();

		// get illuminations player cosmetics
		loadPlayerCosmetics();

		// load jeb shader
		if (FabricLoader.getInstance().isModLoaded("satin")) {
			Rainbowlluminations.init();
		}

		// register resource packs
		ResourceLoader.registerBuiltinResourcePack(new Identifier(Illuminations.MODID, "lowerres"), mod, org.quiltmc.qsl.resource.loader.api.ResourcePackActivationType.NORMAL, Text.translatable(""));
		ResourceLoader.registerBuiltinResourcePack(new Identifier(Illuminations.MODID, "pixelaccurate"), mod, org.quiltmc.qsl.resource.loader.api.ResourcePackActivationType.NORMAL);

		// register model layers
		EntityModelLayerRegistry.registerModelLayer(CrownModel.MODEL_LAYER, CrownModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(HornsModel.MODEL_LAYER, HornsModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(HaloModel.MODEL_LAYER, HaloModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(TiaraModel.MODEL_LAYER, TiaraModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(VoidheartTiaraModel.MODEL_LAYER, VoidheartTiaraModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(WreathModel.MODEL_LAYER, WreathModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(WillOWispModel.MODEL_LAYER, WillOWispModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(LanternModel.MODEL_LAYER, LanternModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(PrideHeartModel.MODEL_LAYER, PrideHeartModel::getTexturedModelData);

		// particles
		FIREFLY = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Illuminations.MODID, "firefly"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Illuminations.FIREFLY, FireflyParticle.DefaultFactory::new);
		GLOWWORM = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Illuminations.MODID, "glowworm"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Illuminations.GLOWWORM, GlowwormParticle.DefaultFactory::new);
		PLANKTON = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Illuminations.MODID, "plankton"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Illuminations.PLANKTON, PlanktonParticle.DefaultFactory::new);
		EYES = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Illuminations.MODID, "eyes"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Illuminations.EYES, EyesParticle.DefaultFactory::new);
		CHORUS_PETAL = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Illuminations.MODID, "chorus_petal"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Illuminations.CHORUS_PETAL, ChorusPetalParticle.DefaultFactory::new);
		WILL_O_WISP = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Illuminations.MODID, "will_o_wisp"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Illuminations.WILL_O_WISP, fabricSpriteProvider -> new WillOWispParticle.DefaultFactory(fabricSpriteProvider, new Identifier(Illuminations.MODID, "textures/entity/will_o_wisp.png"), 1.0f, 1.0f, 1.0f, -0.1f, -0.01f, 0.0f));
		WISP_TRAIL = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Illuminations.MODID, "wisp_trail"), new ParticleType<WispTrailParticleEffect>(true, WispTrailParticleEffect.PARAMETERS_FACTORY) {
			@Override
			public Codec<WispTrailParticleEffect> getCodec() {
				return WispTrailParticleEffect.CODEC;
			}
		});
		ParticleFactoryRegistry.getInstance().register(Illuminations.WISP_TRAIL, WispTrailParticle.Factory::new);
		PUMPKIN_SPIRIT = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Illuminations.MODID, "pumpkin_spirit"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Illuminations.PUMPKIN_SPIRIT, fabricSpriteProvider -> new PumpkinSpiritParticle.DefaultFactory(fabricSpriteProvider, new Identifier(Illuminations.MODID, "textures/entity/pumpkin_spirit.png"), 1.0f, 0.95f, 0.0f, 0.0f, -0.03f, 0.0f));
		POLTERGEIST = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Illuminations.MODID, "poltergeist"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Illuminations.POLTERGEIST, fabricSpriteProvider -> new PoltergeistParticle.DefaultFactory(fabricSpriteProvider, new Identifier(Illuminations.MODID, "textures/entity/poltergeist.png"), 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f));
		PRISMARINE_CRYSTAL = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Illuminations.MODID, "prismarine_crystal"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Illuminations.PRISMARINE_CRYSTAL, PrismarineCrystalParticle.DefaultFactory::new);

		// aura particles
		TWILIGHT_AURA = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Illuminations.MODID, "twilight_aura"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Illuminations.TWILIGHT_AURA, TwilightFireflyParticle.DefaultFactory::new);
		GHOSTLY_AURA = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Illuminations.MODID, "ghostly_aura"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Illuminations.GHOSTLY_AURA, GhostlyAuraParticle.DefaultFactory::new);
		CHORUS_AURA = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Illuminations.MODID, "chorus_aura"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Illuminations.CHORUS_AURA, ChorusAuraParticle.DefaultFactory::new);
		AUTUMN_LEAVES_AURA = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Illuminations.MODID, "autumn_leaves"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Illuminations.AUTUMN_LEAVES_AURA, AutumnLeavesParticle.DefaultFactory::new);
		SCULK_TENDRIL_AURA = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Illuminations.MODID, "sculk_tendril"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Illuminations.SCULK_TENDRIL_AURA, SculkTendrilParticle.DefaultFactory::new);
		SHADOWBRINGER_AURA = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Illuminations.MODID, "shadowbringer_aura"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Illuminations.SHADOWBRINGER_AURA, ShadowbringerParticle.DefaultFactory::new);
		GOLDENROD_AURA = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Illuminations.MODID, "goldenrod_aura"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Illuminations.GOLDENROD_AURA, GoldenrodAuraParticle.DefaultFactory::new);
		CONFETTI_AURA = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Illuminations.MODID, "confetti"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Illuminations.CONFETTI_AURA, ConfettiParticle.DefaultFactory::new);
		PRISMATIC_CONFETTI_AURA = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Illuminations.MODID, "prismatic_confetti"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Illuminations.PRISMATIC_CONFETTI_AURA, PrismaticConfettiParticle.DefaultFactory::new);
		PRISMARINE_AURA = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Illuminations.MODID, "prismarine_aura"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Illuminations.PRISMARINE_AURA, PrismarineAuraParticle.DefaultFactory::new);

        /*
                PRIDE PETS
         */
		PRIDE_PET = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Illuminations.MODID, "pride_pet"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Illuminations.PRIDE_PET, fabricSpriteProvider -> new PrideHeartParticle.DefaultFactory(fabricSpriteProvider, new Identifier(Illuminations.MODID, "textures/entity/pride_heart.png"), 1.0f, 1.0f, 1.0f));
		GAY_PRIDE_PET = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Illuminations.MODID, "gay_pride_pet"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Illuminations.GAY_PRIDE_PET, fabricSpriteProvider -> new PrideHeartParticle.DefaultFactory(fabricSpriteProvider, new Identifier(Illuminations.MODID, "textures/entity/gay_pride_heart.png"), 1.0f, 1.0f, 1.0f));
		TRANS_PRIDE_PET = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Illuminations.MODID, "trans_pride_pet"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Illuminations.TRANS_PRIDE_PET, fabricSpriteProvider -> new PrideHeartParticle.DefaultFactory(fabricSpriteProvider, new Identifier(Illuminations.MODID, "textures/entity/trans_pride_heart.png"), 1.0f, 1.0f, 1.0f));
		LESBIAN_PRIDE_PET = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Illuminations.MODID, "lesbian_pride_pet"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Illuminations.LESBIAN_PRIDE_PET, fabricSpriteProvider -> new PrideHeartParticle.DefaultFactory(fabricSpriteProvider, new Identifier(Illuminations.MODID, "textures/entity/lesbian_pride_heart.png"), 1.0f, 1.0f, 1.0f));
		BI_PRIDE_PET = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Illuminations.MODID, "bi_pride_pet"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Illuminations.BI_PRIDE_PET, fabricSpriteProvider -> new PrideHeartParticle.DefaultFactory(fabricSpriteProvider, new Identifier(Illuminations.MODID, "textures/entity/bi_pride_heart.png"), 1.0f, 1.0f, 1.0f));
		ACE_PRIDE_PET = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Illuminations.MODID, "ace_pride_pet"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Illuminations.ACE_PRIDE_PET, fabricSpriteProvider -> new PrideHeartParticle.DefaultFactory(fabricSpriteProvider, new Identifier(Illuminations.MODID, "textures/entity/ace_pride_heart.png"), 1.0f, 1.0f, 1.0f));
		NB_PRIDE_PET = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Illuminations.MODID, "nb_pride_pet"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Illuminations.NB_PRIDE_PET, fabricSpriteProvider -> new PrideHeartParticle.DefaultFactory(fabricSpriteProvider, new Identifier(Illuminations.MODID, "textures/entity/nb_pride_heart.png"), 1.0f, 1.0f, 1.0f));
		INTERSEX_PRIDE_PET = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Illuminations.MODID, "intersex_pride_pet"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Illuminations.INTERSEX_PRIDE_PET, fabricSpriteProvider -> new PrideHeartParticle.DefaultFactory(fabricSpriteProvider, new Identifier(Illuminations.MODID, "textures/entity/intersex_pride_heart.png"), 1.0f, 1.0f, 1.0f));
		ARO_PRIDE_PET = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Illuminations.MODID, "aro_pride_pet"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Illuminations.ARO_PRIDE_PET, fabricSpriteProvider -> new PrideHeartParticle.DefaultFactory(fabricSpriteProvider, new Identifier(Illuminations.MODID, "textures/entity/aro_pride_heart.png"), 1.0f, 1.0f, 1.0f));
		PAN_PRIDE_PET = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Illuminations.MODID, "pan_pride_pet"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Illuminations.PAN_PRIDE_PET, fabricSpriteProvider -> new PrideHeartParticle.DefaultFactory(fabricSpriteProvider, new Identifier(Illuminations.MODID, "textures/entity/pan_pride_heart.png"), 1.0f, 1.0f, 1.0f));
		AGENDER_PRIDE_PET = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Illuminations.MODID, "agender_pride_pet"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Illuminations.AGENDER_PRIDE_PET, fabricSpriteProvider -> new PrideHeartParticle.DefaultFactory(fabricSpriteProvider, new Identifier(Illuminations.MODID, "textures/entity/agender_pride_heart.png"), 1.0f, 1.0f, 1.0f));
		GENDERFLUID_PRIDE_PET = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Illuminations.MODID, "genderfluid_pride_pet"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Illuminations.GENDERFLUID_PRIDE_PET, fabricSpriteProvider -> new PrideHeartParticle.DefaultFactory(fabricSpriteProvider, new Identifier(Illuminations.MODID, "textures/entity/genderfluid_pride_heart.png"), 1.0f, 1.0f, 1.0f));
        /*
                WILL O' WISP PETS
         */
		WILL_O_WISP_PET = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Illuminations.MODID, "will_o_wisp_pet"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Illuminations.WILL_O_WISP_PET, fabricSpriteProvider -> new PlayerWispParticle.DefaultFactory(fabricSpriteProvider, new Identifier(Illuminations.MODID, "textures/entity/will_o_wisp.png"), 1.0f, 1.0f, 1.0f, -0.1f, -0.01f, 0.0f));
		GOLDEN_WILL_PET = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Illuminations.MODID, "golden_will_pet"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Illuminations.GOLDEN_WILL_PET, fabricSpriteProvider -> new PlayerWispParticle.DefaultFactory(fabricSpriteProvider, new Identifier(Illuminations.MODID, "textures/entity/golden_will.png"), 1.0f, 0.3f, 1.0f, -0.05f, -0.01f, 0.0f));
		FOUNDING_SKULL_PET = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Illuminations.MODID, "founding_skull_pet"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Illuminations.FOUNDING_SKULL_PET, fabricSpriteProvider -> new PlayerWispParticle.DefaultFactory(fabricSpriteProvider, new Identifier(Illuminations.MODID, "textures/entity/founding_skull.png"), 1.0f, 0.0f, 0.25f, -0.03f, 0.0f, -0.01f));
		DISSOLUTION_WISP_PET = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Illuminations.MODID, "dissolution_wisp_pet"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Illuminations.DISSOLUTION_WISP_PET, PetParticle.DefaultFactory::new);

        /*
                SPOOKY PETS
         */
		JACKO_PET = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Illuminations.MODID, "jacko_pet"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Illuminations.JACKO_PET, JackoParticle.DefaultFactory::new);
		PUMPKIN_SPIRIT_PET = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Illuminations.MODID, "pumpkin_spirit_pet"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Illuminations.PUMPKIN_SPIRIT_PET, fabricSpriteProvider -> new PlayerWispParticle.DefaultFactory(fabricSpriteProvider, new Identifier(Illuminations.MODID, "textures/entity/pumpkin_spirit.png"), 1.0f, 0.95f, 0.0f, 0.0f, -0.03f, 0.0f));
		POLTERGEIST_PET = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Illuminations.MODID, "poltergeist_pet"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Illuminations.POLTERGEIST_PET, fabricSpriteProvider -> new PlayerWispParticle.DefaultFactory(fabricSpriteProvider, new Identifier(Illuminations.MODID, "textures/entity/poltergeist.png"), 1.0f, 1.0f, 1.0f, 0f, 0f, 0f));
		LANTERN_PET = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Illuminations.MODID, "lantern_pet"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Illuminations.LANTERN_PET, fabricSpriteProvider -> new PlayerLanternParticle.DefaultFactory(fabricSpriteProvider, new Identifier(Illuminations.MODID, "textures/entity/lantern.png"), 1.0f, 1.0f, 1.0f));
		SOUL_LANTERN_PET = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Illuminations.MODID, "soul_lantern_pet"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Illuminations.SOUL_LANTERN_PET, fabricSpriteProvider -> new PlayerLanternParticle.DefaultFactory(fabricSpriteProvider, new Identifier(Illuminations.MODID, "textures/entity/soul_lantern.png"), 1.0f, 1.0f, 1.0f));
		CRYING_LANTERN_PET = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Illuminations.MODID, "crying_lantern_pet"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Illuminations.CRYING_LANTERN_PET, fabricSpriteProvider -> new PlayerLanternParticle.DefaultFactory(fabricSpriteProvider, new Identifier(Illuminations.MODID, "textures/entity/crying_lantern.png"), 1.0f, 1.0f, 1.0f));
		SOOTHING_LANTERN_PET = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Illuminations.MODID, "soothing_lantern_pet"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Illuminations.SOOTHING_LANTERN_PET, fabricSpriteProvider -> new PlayerLanternParticle.DefaultFactory(fabricSpriteProvider, new Identifier(Illuminations.MODID, "textures/entity/soothing_lantern.png"), 1.0f, 1.0f, 1.0f));

        /*
                CROWNS FEATURE RENDERER REGISTRATION
         */
		LivingEntityFeatureRendererRegistrationCallback.EVENT.register((entityType, entityRenderer, registrationHelper, context) -> {
			if (entityType == EntityType.PLAYER) {
				@SuppressWarnings("unchecked") var playerRenderer = (FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>>) entityRenderer;
				registrationHelper.register(new OverheadFeatureRenderer(playerRenderer, context));
			}
		});

        /*
                ADDING FIRFLY, GLOWWORM AND PLANKTON BIOMES SPAWN RATES
         */
		ImmutableMap.Builder<Identifier, ImmutableSet<IlluminationData>> biomeBuilder = ImmutableMap.builder();
		Config.getBiomeSettings().forEach((biome, settings) -> {
			ImmutableSet.Builder<IlluminationData> illuminationDataBuilder = ImmutableSet.builder();

			illuminationDataBuilder.add(new IlluminationData(FIREFLY, FIREFLY_LOCATION_PREDICATE, () -> Config.getBiomeSettings(biome).fireflySpawnRate().spawnRate));
			if (settings.glowwormSpawnRate() != null)
				illuminationDataBuilder.add(new IlluminationData(GLOWWORM, GLOWWORM_LOCATION_PREDICATE, () -> Config.getBiomeSettings(biome).glowwormSpawnRate().spawnRate));
			if (settings.planktonSpawnRate() != null)
				illuminationDataBuilder.add(new IlluminationData(PLANKTON, PLANKTON_LOCATION_PREDICATE, () -> Config.getBiomeSettings(biome).planktonSpawnRate().spawnRate));

			biomeBuilder.put(biome, illuminationDataBuilder.build());
		});
		ILLUMINATIONS_BIOME_CATEGORIES = biomeBuilder.build();

        /*
                WILL O' WISP BIOME SPAWN
         */
		ILLUMINATIONS_BIOMES = ImmutableMap.<Identifier, ImmutableSet<IlluminationData>>builder()
				.put(new Identifier("minecraft:soul_sand_valley"), ImmutableSet.of(
						new IlluminationData(WILL_O_WISP, WISP_LOCATION_PREDICATE, () -> Config.getWillOWispsSpawnRate().spawnRate)))
				.build();

        /*
             Aura matching and spawn chances + overhead matching + crown matching
             Currently set to default aura settings.
             Uncomment settings related to auras in Config.java and change getDefaultAuraSettings to getAuraSettings to restore.
         */
		AURAS_DATA = ImmutableMap.<String, AuraData>builder()
				.put("twilight", new AuraData(TWILIGHT_AURA, () -> DefaultConfig.getAuraSettings("twilight")))
				.put("ghostly", new AuraData(GHOSTLY_AURA, () -> DefaultConfig.getAuraSettings("ghostly")))
				.put("chorus", new AuraData(CHORUS_AURA, () -> DefaultConfig.getAuraSettings("chorus")))
				.put("autumn_leaves", new AuraData(AUTUMN_LEAVES_AURA, () -> DefaultConfig.getAuraSettings("autumn_leaves")))
				.put("sculk_tendrils", new AuraData(SCULK_TENDRIL_AURA, () -> DefaultConfig.getAuraSettings("sculk_tendrils")))
				.put("shadowbringer_soul", new AuraData(SHADOWBRINGER_AURA, () -> DefaultConfig.getAuraSettings("shadowbringer_soul")))
				.put("goldenrod", new AuraData(GOLDENROD_AURA, () -> DefaultConfig.getAuraSettings("goldenrod")))
				.put("confetti", new AuraData(CONFETTI_AURA, () -> DefaultConfig.getAuraSettings("confetti")))
				.put("prismatic_confetti", new AuraData(PRISMATIC_CONFETTI_AURA, () -> DefaultConfig.getAuraSettings("prismatic_confetti")))
				.put("prismarine", new AuraData(PRISMARINE_AURA, () -> DefaultConfig.getAuraSettings("prismarine")))
				.build();

		OVERHEADS_DATA = ImmutableMap.<String, OverheadData>builder()
				.put("solar_crown", new OverheadData(CrownModel::new, "solar_crown"))
				.put("frost_crown", new OverheadData(CrownModel::new, "frost_crown"))
				.put("pyro_crown", new OverheadData(CrownModel::new, "pyro_crown"))
				.put("chorus_crown", new OverheadData(CrownModel::new, "chorus_crown"))
				.put("bloodfiend_crown", new OverheadData(CrownModel::new, "bloodfiend_crown"))
				.put("dreadlich_crown", new OverheadData(CrownModel::new, "dreadlich_crown"))
				.put("mooncult_crown", new OverheadData(CrownModel::new, "mooncult_crown"))
				.put("deepsculk_horns", new OverheadData(HornsModel::new, "deepsculk_horns"))
				.put("springfae_horns", new OverheadData(HornsModel::new, "springfae_horns"))
				.put("voidheart_tiara", new OverheadData(VoidheartTiaraModel::new, "voidheart_tiara"))
				.put("worldweaver_halo", new OverheadData(HaloModel::new, "worldweaver_halo"))
				.put("summerbreeze_wreath", new OverheadData(WreathModel::new, "summerbreeze_wreath"))
				.put("glowsquid_cult_crown", new OverheadData(TiaraModel::new, "glowsquid_cult_crown"))
				.put("timeaspect_cult_crown", new OverheadData(TiaraModel::new, "timeaspect_cult_crown"))
				.put("prismarine_crown", new OverheadData(CrownModel::new, "prismarine_crown"))
				.build();

		PETS_DATA = ImmutableMap.<String, DefaultParticleType>builder()
				.put("pride", PRIDE_PET)
				.put("gay_pride", GAY_PRIDE_PET)
				.put("trans_pride", TRANS_PRIDE_PET)
				.put("lesbian_pride", LESBIAN_PRIDE_PET)
				.put("bi_pride", BI_PRIDE_PET)
				.put("ace_pride", ACE_PRIDE_PET)
				.put("nb_pride", NB_PRIDE_PET)
				.put("intersex_pride", INTERSEX_PRIDE_PET)
				.put("aro_pride", ARO_PRIDE_PET)
				.put("pan_pride", PAN_PRIDE_PET)
				.put("agender_pride", AGENDER_PRIDE_PET)
				.put("genderfluid_pride", GENDERFLUID_PRIDE_PET)
				.put("jacko", JACKO_PET)
				.put("will_o_wisp", WILL_O_WISP_PET)
				.put("golden_will", GOLDEN_WILL_PET)
				.put("founding_skull", FOUNDING_SKULL_PET)
				.put("dissolution_wisp", DISSOLUTION_WISP_PET)
				.put("pumpkin_spirit", PUMPKIN_SPIRIT_PET)
				.put("poltergeist", POLTERGEIST_PET)
				.put("lantern", LANTERN_PET)
				.put("soul_lantern", SOUL_LANTERN_PET)
				.put("crying_lantern", CRYING_LANTERN_PET)
				.put("soothing_lantern", SOOTHING_LANTERN_PET)
				.build();
	}

	private static class PlayerCosmeticDataParser implements JsonDeserializer<PlayerCosmeticData> {
		@Override
		public PlayerCosmeticData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			JsonObject jsonObject = json.getAsJsonObject();
			return new PlayerCosmeticData(jsonObject.get("aura")
					, jsonObject.get("color")
					, jsonObject.get("overhead")
					, jsonObject.get("pet"));
		}
	}
}
