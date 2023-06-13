package ladysnake.effective.cosmetics;

import com.google.common.collect.ImmutableMap;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import ladysnake.effective.settings.SpawnSettings;
import ladysnake.effective.cosmetics.data.AuraData;
import ladysnake.effective.cosmetics.data.OverheadData;
import ladysnake.effective.cosmetics.data.PlayerCosmeticData;
import ladysnake.effective.cosmetics.particle.aura.*;
import ladysnake.effective.cosmetics.particle.pet.*;
import ladysnake.effective.cosmetics.render.entity.feature.OverheadFeatureRenderer;
import ladysnake.effective.cosmetics.render.entity.model.hat.*;
import ladysnake.effective.cosmetics.render.entity.model.pet.LanternModel;
import ladysnake.effective.cosmetics.render.entity.model.pet.PrideHeartModel;
import ladysnake.effective.cosmetics.render.entity.model.pet.WillOWispModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.resource.loader.api.ResourceLoader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Environment(EnvType.CLIENT)
public class Cosmetics implements ClientModInitializer {
	public static final String MODID = "effective_cosmetics";

	// illuminations constants
	public static final int EYES_VANISHING_DISTANCE = 5;
	public static final Gson COSMETICS_GSON = new GsonBuilder().registerTypeAdapter(PlayerCosmeticData.class, new PlayerCosmeticDataParser()).create();

	// register overhead models
	public static final EntityModelLayer CROWN = new EntityModelLayer(new Identifier(Cosmetics.MODID, "crown"), "main");
	static final Type COSMETIC_SELECT_TYPE = new TypeToken<Map<UUID, PlayerCosmeticData>>() {
	}.getType();

	// illuminations cosmetics
	private static final String COSMETICS_URL = "https://doctor4t.uuid.gg/illuminations-data";
	public static ImmutableMap<String, AuraData> AURAS_DATA;
	public static ImmutableMap<String, DefaultParticleType> PETS_DATA;
	public static ImmutableMap<String, OverheadData> OVERHEADS_DATA;

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
	public static DefaultParticleType GENDERFLUID_PRIDE_PET;
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

	private static Map<UUID, PlayerCosmeticData> PLAYER_COSMETICS = Collections.emptyMap();

	public static @Nullable PlayerCosmeticData getCosmeticData(PlayerEntity player) {
		return PLAYER_COSMETICS.get(player.getUuid());
	}

	public static void loadPlayerCosmetics() {
		// get illuminations player cosmetics
		CompletableFuture.supplyAsync(() -> {
			try (Reader reader = new InputStreamReader(new URL(COSMETICS_URL).openStream())) {
				return COSMETICS_GSON.<Map<UUID, PlayerCosmeticData>>fromJson(reader, COSMETIC_SELECT_TYPE);
			} catch (IOException ignored) {
			}

			return null;
		}).exceptionally(throwable -> null).thenAcceptAsync(playerData -> {
			if (playerData != null) {
				PLAYER_COSMETICS = playerData;
			} else {
				PLAYER_COSMETICS = Collections.emptyMap();
			}
		}, MinecraftClient.getInstance());
	}

	public static boolean isNightTime(World world) {
		return world.getSkyAngle(world.getTimeOfDay()) >= 0.25965086 && world.getSkyAngle(world.getTimeOfDay()) <= 0.7403491;
	}

	@Override
	public void onInitializeClient(ModContainer mod) {
		// get illuminations player cosmetics
		loadPlayerCosmetics();

		// register resource packs
		ResourceLoader.registerBuiltinResourcePack(new Identifier(Cosmetics.MODID, "lowerres"), mod, org.quiltmc.qsl.resource.loader.api.ResourcePackActivationType.NORMAL, Text.translatable(""));
		ResourceLoader.registerBuiltinResourcePack(new Identifier(Cosmetics.MODID, "pixelaccurate"), mod, org.quiltmc.qsl.resource.loader.api.ResourcePackActivationType.NORMAL);

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

		// aura particles
		TWILIGHT_AURA = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Cosmetics.MODID, "twilight_aura"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Cosmetics.TWILIGHT_AURA, TwilightFireflyParticle.DefaultFactory::new);
		GHOSTLY_AURA = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Cosmetics.MODID, "ghostly_aura"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Cosmetics.GHOSTLY_AURA, GhostlyAuraParticle.DefaultFactory::new);
		CHORUS_AURA = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Cosmetics.MODID, "chorus_aura"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Cosmetics.CHORUS_AURA, ChorusAuraParticle.DefaultFactory::new);
		AUTUMN_LEAVES_AURA = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Cosmetics.MODID, "autumn_leaves"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Cosmetics.AUTUMN_LEAVES_AURA, AutumnLeavesParticle.DefaultFactory::new);
		SCULK_TENDRIL_AURA = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Cosmetics.MODID, "sculk_tendril"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Cosmetics.SCULK_TENDRIL_AURA, SculkTendrilParticle.DefaultFactory::new);
		SHADOWBRINGER_AURA = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Cosmetics.MODID, "shadowbringer_aura"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Cosmetics.SHADOWBRINGER_AURA, ShadowbringerParticle.DefaultFactory::new);
		GOLDENROD_AURA = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Cosmetics.MODID, "goldenrod_aura"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Cosmetics.GOLDENROD_AURA, GoldenrodAuraParticle.DefaultFactory::new);
		CONFETTI_AURA = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Cosmetics.MODID, "confetti"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Cosmetics.CONFETTI_AURA, ConfettiParticle.DefaultFactory::new);
		PRISMATIC_CONFETTI_AURA = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Cosmetics.MODID, "prismatic_confetti"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Cosmetics.PRISMATIC_CONFETTI_AURA, PrismaticConfettiParticle.DefaultFactory::new);
		PRISMARINE_AURA = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Cosmetics.MODID, "prismarine_aura"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Cosmetics.PRISMARINE_AURA, PrismarineAuraParticle.DefaultFactory::new);

        /*
                PRIDE PETS
         */
		PRIDE_PET = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Cosmetics.MODID, "pride_pet"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Cosmetics.PRIDE_PET, fabricSpriteProvider -> new PrideHeartParticle.DefaultFactory(fabricSpriteProvider, new Identifier(Cosmetics.MODID, "textures/entity/pride_heart.png"), 1.0f, 1.0f, 1.0f));
		GAY_PRIDE_PET = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Cosmetics.MODID, "gay_pride_pet"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Cosmetics.GAY_PRIDE_PET, fabricSpriteProvider -> new PrideHeartParticle.DefaultFactory(fabricSpriteProvider, new Identifier(Cosmetics.MODID, "textures/entity/gay_pride_heart.png"), 1.0f, 1.0f, 1.0f));
		TRANS_PRIDE_PET = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Cosmetics.MODID, "trans_pride_pet"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Cosmetics.TRANS_PRIDE_PET, fabricSpriteProvider -> new PrideHeartParticle.DefaultFactory(fabricSpriteProvider, new Identifier(Cosmetics.MODID, "textures/entity/trans_pride_heart.png"), 1.0f, 1.0f, 1.0f));
		LESBIAN_PRIDE_PET = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Cosmetics.MODID, "lesbian_pride_pet"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Cosmetics.LESBIAN_PRIDE_PET, fabricSpriteProvider -> new PrideHeartParticle.DefaultFactory(fabricSpriteProvider, new Identifier(Cosmetics.MODID, "textures/entity/lesbian_pride_heart.png"), 1.0f, 1.0f, 1.0f));
		BI_PRIDE_PET = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Cosmetics.MODID, "bi_pride_pet"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Cosmetics.BI_PRIDE_PET, fabricSpriteProvider -> new PrideHeartParticle.DefaultFactory(fabricSpriteProvider, new Identifier(Cosmetics.MODID, "textures/entity/bi_pride_heart.png"), 1.0f, 1.0f, 1.0f));
		ACE_PRIDE_PET = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Cosmetics.MODID, "ace_pride_pet"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Cosmetics.ACE_PRIDE_PET, fabricSpriteProvider -> new PrideHeartParticle.DefaultFactory(fabricSpriteProvider, new Identifier(Cosmetics.MODID, "textures/entity/ace_pride_heart.png"), 1.0f, 1.0f, 1.0f));
		NB_PRIDE_PET = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Cosmetics.MODID, "nb_pride_pet"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Cosmetics.NB_PRIDE_PET, fabricSpriteProvider -> new PrideHeartParticle.DefaultFactory(fabricSpriteProvider, new Identifier(Cosmetics.MODID, "textures/entity/nb_pride_heart.png"), 1.0f, 1.0f, 1.0f));
		INTERSEX_PRIDE_PET = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Cosmetics.MODID, "intersex_pride_pet"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Cosmetics.INTERSEX_PRIDE_PET, fabricSpriteProvider -> new PrideHeartParticle.DefaultFactory(fabricSpriteProvider, new Identifier(Cosmetics.MODID, "textures/entity/intersex_pride_heart.png"), 1.0f, 1.0f, 1.0f));
		ARO_PRIDE_PET = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Cosmetics.MODID, "aro_pride_pet"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Cosmetics.ARO_PRIDE_PET, fabricSpriteProvider -> new PrideHeartParticle.DefaultFactory(fabricSpriteProvider, new Identifier(Cosmetics.MODID, "textures/entity/aro_pride_heart.png"), 1.0f, 1.0f, 1.0f));
		PAN_PRIDE_PET = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Cosmetics.MODID, "pan_pride_pet"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Cosmetics.PAN_PRIDE_PET, fabricSpriteProvider -> new PrideHeartParticle.DefaultFactory(fabricSpriteProvider, new Identifier(Cosmetics.MODID, "textures/entity/pan_pride_heart.png"), 1.0f, 1.0f, 1.0f));
		AGENDER_PRIDE_PET = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Cosmetics.MODID, "agender_pride_pet"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Cosmetics.AGENDER_PRIDE_PET, fabricSpriteProvider -> new PrideHeartParticle.DefaultFactory(fabricSpriteProvider, new Identifier(Cosmetics.MODID, "textures/entity/agender_pride_heart.png"), 1.0f, 1.0f, 1.0f));
		GENDERFLUID_PRIDE_PET = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Cosmetics.MODID, "genderfluid_pride_pet"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Cosmetics.GENDERFLUID_PRIDE_PET, fabricSpriteProvider -> new PrideHeartParticle.DefaultFactory(fabricSpriteProvider, new Identifier(Cosmetics.MODID, "textures/entity/genderfluid_pride_heart.png"), 1.0f, 1.0f, 1.0f));
        /*
                WILL O' WISP PETS
         */
		WILL_O_WISP_PET = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Cosmetics.MODID, "will_o_wisp_pet"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Cosmetics.WILL_O_WISP_PET, fabricSpriteProvider -> new PlayerWispParticle.DefaultFactory(fabricSpriteProvider, new Identifier(Cosmetics.MODID, "textures/entity/will_o_wisp.png"), 1.0f, 1.0f, 1.0f, -0.1f, -0.01f, 0.0f));
		GOLDEN_WILL_PET = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Cosmetics.MODID, "golden_will_pet"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Cosmetics.GOLDEN_WILL_PET, fabricSpriteProvider -> new PlayerWispParticle.DefaultFactory(fabricSpriteProvider, new Identifier(Cosmetics.MODID, "textures/entity/golden_will.png"), 1.0f, 0.3f, 1.0f, -0.05f, -0.01f, 0.0f));
		FOUNDING_SKULL_PET = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Cosmetics.MODID, "founding_skull_pet"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Cosmetics.FOUNDING_SKULL_PET, fabricSpriteProvider -> new PlayerWispParticle.DefaultFactory(fabricSpriteProvider, new Identifier(Cosmetics.MODID, "textures/entity/founding_skull.png"), 1.0f, 0.0f, 0.25f, -0.03f, 0.0f, -0.01f));
		DISSOLUTION_WISP_PET = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Cosmetics.MODID, "dissolution_wisp_pet"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Cosmetics.DISSOLUTION_WISP_PET, PetParticle.DefaultFactory::new);

        /*
                SPOOKY PETS
         */
		JACKO_PET = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Cosmetics.MODID, "jacko_pet"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Cosmetics.JACKO_PET, JackoParticle.DefaultFactory::new);
		PUMPKIN_SPIRIT_PET = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Cosmetics.MODID, "pumpkin_spirit_pet"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Cosmetics.PUMPKIN_SPIRIT_PET, fabricSpriteProvider -> new PlayerWispParticle.DefaultFactory(fabricSpriteProvider, new Identifier(Cosmetics.MODID, "textures/entity/pumpkin_spirit.png"), 1.0f, 0.95f, 0.0f, 0.0f, -0.03f, 0.0f));
		POLTERGEIST_PET = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Cosmetics.MODID, "poltergeist_pet"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Cosmetics.POLTERGEIST_PET, fabricSpriteProvider -> new PlayerWispParticle.DefaultFactory(fabricSpriteProvider, new Identifier(Cosmetics.MODID, "textures/entity/poltergeist.png"), 1.0f, 1.0f, 1.0f, 0f, 0f, 0f));
		LANTERN_PET = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Cosmetics.MODID, "lantern_pet"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Cosmetics.LANTERN_PET, fabricSpriteProvider -> new PlayerLanternParticle.DefaultFactory(fabricSpriteProvider, new Identifier(Cosmetics.MODID, "textures/entity/lantern.png"), 1.0f, 1.0f, 1.0f));
		SOUL_LANTERN_PET = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Cosmetics.MODID, "soul_lantern_pet"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Cosmetics.SOUL_LANTERN_PET, fabricSpriteProvider -> new PlayerLanternParticle.DefaultFactory(fabricSpriteProvider, new Identifier(Cosmetics.MODID, "textures/entity/soul_lantern.png"), 1.0f, 1.0f, 1.0f));
		CRYING_LANTERN_PET = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Cosmetics.MODID, "crying_lantern_pet"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Cosmetics.CRYING_LANTERN_PET, fabricSpriteProvider -> new PlayerLanternParticle.DefaultFactory(fabricSpriteProvider, new Identifier(Cosmetics.MODID, "textures/entity/crying_lantern.png"), 1.0f, 1.0f, 1.0f));
		SOOTHING_LANTERN_PET = Registry.register(Registries.PARTICLE_TYPE, new Identifier(Cosmetics.MODID, "soothing_lantern_pet"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(Cosmetics.SOOTHING_LANTERN_PET, fabricSpriteProvider -> new PlayerLanternParticle.DefaultFactory(fabricSpriteProvider, new Identifier(Cosmetics.MODID, "textures/entity/soothing_lantern.png"), 1.0f, 1.0f, 1.0f));

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
             Aura matching and spawn chances + overhead matching + crown matching
             Currently set to default aura settings.
             Uncomment settings related to auras in Config.java and change getDefaultAuraSettings to getAuraSettings to restore.
         */
		AURAS_DATA = ImmutableMap.<String, AuraData>builder()
			.put("twilight", new AuraData(TWILIGHT_AURA, () -> SpawnSettings.AURAS.get("twilight")))
			.put("ghostly", new AuraData(GHOSTLY_AURA, () -> SpawnSettings.AURAS.get("ghostly")))
			.put("chorus", new AuraData(CHORUS_AURA, () -> SpawnSettings.AURAS.get("chorus")))
			.put("autumn_leaves", new AuraData(AUTUMN_LEAVES_AURA, () -> SpawnSettings.AURAS.get("autumn_leaves")))
			.put("sculk_tendrils", new AuraData(SCULK_TENDRIL_AURA, () -> SpawnSettings.AURAS.get("sculk_tendrils")))
			.put("shadowbringer_soul", new AuraData(SHADOWBRINGER_AURA, () -> SpawnSettings.AURAS.get("shadowbringer_soul")))
			.put("goldenrod", new AuraData(GOLDENROD_AURA, () -> SpawnSettings.AURAS.get("goldenrod")))
			.put("confetti", new AuraData(CONFETTI_AURA, () -> SpawnSettings.AURAS.get("confetti")))
			.put("prismatic_confetti", new AuraData(PRISMATIC_CONFETTI_AURA, () -> SpawnSettings.AURAS.get("prismatic_confetti")))
			.put("prismarine", new AuraData(PRISMARINE_AURA, () -> SpawnSettings.AURAS.get("prismarine")))
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
