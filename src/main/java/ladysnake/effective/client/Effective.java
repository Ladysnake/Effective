package ladysnake.effective.client;

import ladysnake.effective.client.data.PlayerCosmeticData;
import ladysnake.effective.client.particle.*;
import ladysnake.effective.client.render.entity.model.SplashBottomModel;
import ladysnake.effective.client.render.entity.model.SplashModel;
import ladysnake.effective.client.world.WaterfallCloudGenerators;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@Environment(EnvType.CLIENT)
public class Effective implements ClientModInitializer {
    public static final String MODID = "effective";
    public static final Logger logger = LogManager.getLogger("Effective");

    // illuminations constants
//    public static final Gson COSMETICS_GSON = new GsonBuilder().registerTypeAdapter(PlayerCosmeticData.class, new PlayerCosmeticDataParser()).create();


    // illuminations cosmetics
//    private static final String COSMETICS_URL = "https://illuminations.uuid.gg/data";
//    public static ImmutableMap<String, AuraData> AURAS_DATA;
//    public static ImmutableMap<String, DefaultParticleType> PETS_DATA;
//    public static ImmutableMap<String, OverheadData> OVERHEADS_DATA;

    // particle types
    public static DefaultParticleType SPLASH;
    public static DefaultParticleType LAVA_SPLASH;
    public static DefaultParticleType DROPLET;
    public static DefaultParticleType RIPPLE;
    public static DefaultParticleType WATERFALL_CLOUD;

    // sound events
    public static SoundEvent AMBIENCE_WATERFALL = new SoundEvent(new Identifier(MODID, "ambience.waterfall"));

    private static Map<UUID, PlayerCosmeticData> PLAYER_COSMETICS = Collections.emptyMap();

    public static @Nullable PlayerCosmeticData getCosmeticData(PlayerEntity player) {
        return PLAYER_COSMETICS.get(player.getUuid());
    }

    public static @Nullable PlayerCosmeticData getCosmeticData(UUID uuid) {
        return PLAYER_COSMETICS.get(uuid);
    }

//    public static void loadPlayerCosmetics() {
//        // get illuminations player cosmetics
//        CompletableFuture.supplyAsync(() -> {
//            try (Reader reader = new InputStreamReader(new URL(COSMETICS_URL).openStream())) {
//                if (Config.isDebugMode())
//                    logger.log(Level.INFO, "Retrieving Illuminations cosmetics from the dashboard...");
//                return COSMETICS_GSON.<Map<UUID, PlayerCosmeticData>>fromJson(reader, COSMETIC_SELECT_TYPE);
//            } catch (MalformedURLException e) {
//                if (Config.isDebugMode())
//                    logger.log(Level.ERROR, "Could not get player cosmetics because of malformed URL: " + e.getMessage());
//            } catch (IOException e) {
//                if (Config.isDebugMode())
//                    logger.log(Level.ERROR, "Could not get player cosmetics because of I/O Error: " + e.getMessage());
//            }
//
//            return null;
//        }).exceptionally(throwable -> {
//            if (Config.isDebugMode())
//                logger.log(Level.ERROR, "Could not get player cosmetics because wtf is happening", throwable);
//            return null;
//        }).thenAcceptAsync(playerData -> {
//            if (playerData != null) {
//                PLAYER_COSMETICS = playerData;
//                if (Config.isDebugMode()) logger.log(Level.INFO, "Player cosmetics successfully registered");
//            } else {
//                PLAYER_COSMETICS = Collections.emptyMap();
//                if (Config.isDebugMode())
//                    logger.log(Level.WARN, "Player cosmetics could not registered, cosmetics will be ignored");
//            }
//        }, MinecraftClient.getInstance());
//    }

    @Override
    public void onInitializeClient() {
        // load config
        Config.load();

        // get illuminations player cosmetics
//        loadPlayerCosmetics();

        // register model layers
        EntityModelLayerRegistry.registerModelLayer(SplashModel.MODEL_LAYER, SplashModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(SplashBottomModel.MODEL_LAYER, SplashBottomModel::getTexturedModelData);

        // particles
        SPLASH = Registry.register(Registry.PARTICLE_TYPE, "effective:splash", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(Effective.SPLASH, fabricSpriteProvider -> new SplashParticle.DefaultFactory(fabricSpriteProvider, new Identifier(Effective.MODID, "textures/entity/splash/splash_0.png")));
        DROPLET = Registry.register(Registry.PARTICLE_TYPE, "effective:droplet", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(Effective.DROPLET, DropletParticle.DefaultFactory::new);
        RIPPLE = Registry.register(Registry.PARTICLE_TYPE, "effective:ripple", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(Effective.RIPPLE, RippleParticle.DefaultFactory::new);
        WATERFALL_CLOUD = Registry.register(Registry.PARTICLE_TYPE, "effective:waterfall_cloud", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(Effective.WATERFALL_CLOUD, WaterfallCloudParticle.DefaultFactory::new);
        LAVA_SPLASH = Registry.register(Registry.PARTICLE_TYPE, "effective:lava_splash", FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(Effective.LAVA_SPLASH, fabricSpriteProvider -> new LavaSplashParticle.DefaultFactory(fabricSpriteProvider, new Identifier(Effective.MODID, "textures/entity/splash/lava_splash_0.png")));

        // ticking generators
        ClientTickEvents.END_WORLD_TICK.register(world -> {
            WaterfallCloudGenerators.tick();
        });

        // sound events
        AMBIENCE_WATERFALL = Registry.register(Registry.SOUND_EVENT, AMBIENCE_WATERFALL.getId(), AMBIENCE_WATERFALL);
    }

//    private static class PlayerCosmeticDataParser implements JsonDeserializer<PlayerCosmeticData> {
//        @Override
//        public PlayerCosmeticData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
//            JsonObject jsonObject = json.getAsJsonObject();
//            return new PlayerCosmeticData(jsonObject.get("aura")
//                    , jsonObject.get("color")
//                    , jsonObject.get("overhead")
//                    , jsonObject.get("drip")
//                    , jsonObject.get("pet"));
//        }
//    }
}
