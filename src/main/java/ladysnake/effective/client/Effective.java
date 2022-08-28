package ladysnake.effective.client;

import ladysnake.effective.client.particle.DropletParticle;
import ladysnake.effective.client.particle.RippleParticle;
import ladysnake.effective.client.particle.SplashParticle;
import ladysnake.effective.client.particle.WaterfallCloudParticle;
import ladysnake.effective.client.render.entity.model.SplashBottomModel;
import ladysnake.effective.client.render.entity.model.SplashModel;
import ladysnake.effective.client.world.WaterfallCloudGenerators;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

@Environment(EnvType.CLIENT)
public class Effective implements ClientModInitializer {
    public static final String MODID = "effective";

    // particle types
    public static DefaultParticleType SPLASH;
    //    public static DefaultParticleType LAVA_SPLASH;
    public static DefaultParticleType DROPLET;
    public static DefaultParticleType RIPPLE;
    public static DefaultParticleType WATERFALL_CLOUD;

    // sound events
    public static SoundEvent AMBIENCE_WATERFALL = new SoundEvent(new Identifier(MODID, "ambience.waterfall"));

    @Override
    public void onInitializeClient() {
        // load config
        EffectiveConfig.init(MODID, EffectiveConfig.class);

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
//        LAVA_SPLASH = Registry.register(Registry.PARTICLE_TYPE, "effective:lava_splash", FabricParticleTypes.simple(true));
//        ParticleFactoryRegistry.getInstance().register(Effective.LAVA_SPLASH, fabricSpriteProvider -> new LavaSplashParticle.DefaultFactory(fabricSpriteProvider, new Identifier(Effective.MODID, "textures/entity/splash/lava_splash_0.png")));

        // sound events
        AMBIENCE_WATERFALL = Registry.register(Registry.SOUND_EVENT, AMBIENCE_WATERFALL.getId(), AMBIENCE_WATERFALL);

        // events
        ClientTickEvents.END_CLIENT_TICK.register(client -> WaterfallCloudGenerators.tick());
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            WaterfallCloudGenerators.generators.clear();
            WaterfallCloudGenerators.particlesToSpawn.clear();
        });
    }
}
