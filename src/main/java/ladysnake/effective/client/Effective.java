package ladysnake.effective.client;

import ladysnake.effective.client.particle.*;
import ladysnake.effective.client.particle.types.AllayTwinkleParticleType;
import ladysnake.effective.client.particle.types.FireflyParticleType;
import ladysnake.effective.client.particle.types.SplashParticleType;
import ladysnake.effective.client.render.entity.model.SplashBottomModel;
import ladysnake.effective.client.render.entity.model.SplashBottomRimModel;
import ladysnake.effective.client.render.entity.model.SplashModel;
import ladysnake.effective.client.render.entity.model.SplashRimModel;
import ladysnake.effective.client.world.RenderedHypnotizingEntities;
import ladysnake.effective.client.world.WaterfallCloudGenerators;
import ladysnake.satin.api.event.EntitiesPreRenderCallback;
import ladysnake.satin.api.event.ShaderEffectRenderCallback;
import ladysnake.satin.api.managed.ManagedCoreShader;
import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import ladysnake.satin.api.managed.uniform.Uniform1f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.passive.GlowSquidEntity;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Quaternionf;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

@Environment(EnvType.CLIENT)
public class Effective implements ClientModInitializer {
	public static final String MODID = "effective";
	// rainbow shader for jeb glow squid
	public static final ManagedCoreShader RAINBOW_SHADER = ShaderEffectManager.getInstance().manageCoreShader(new Identifier(MODID, "jeb"));
	private static final Uniform1f uniformSTimeJeb = RAINBOW_SHADER.findUniform1f("Time");
	// squid hypno shader
	private static final ManagedShaderEffect HYPNO_SHADER = ShaderEffectManager.getInstance()
			.manage(new Identifier(MODID, "shaders/post/hypnotize.json"));
	private static final Uniform1f intensityHypno = HYPNO_SHADER.findUniform1f("Intensity");
	private static final Uniform1f sTimeHypno = HYPNO_SHADER.findUniform1f("STime");
	private static final Uniform1f rainbowHypno = HYPNO_SHADER.findUniform1f("Rainbow");
	// particle types
	public static SplashParticleType SPLASH;
	public static DefaultParticleType DROPLET;
	public static DefaultParticleType RIPPLE;
	public static DefaultParticleType WATERFALL_CLOUD;
	public static SplashParticleType GLOW_SPLASH;
	public static DefaultParticleType GLOW_DROPLET;
	public static DefaultParticleType GLOW_RIPPLE;
	public static DefaultParticleType GLOW_WATERFALL_CLOUD;
	public static AllayTwinkleParticleType ALLAY_TWINKLE;
	public static FireflyParticleType FIREFLY;
	public static DefaultParticleType CHORUS_PETAL;

	// sound events
	public static SoundEvent AMBIENCE_WATERFALL = SoundEvent.createVariableRangeEvent(new Identifier(MODID, "ambience.waterfall"));
	private static int ticksJeb;

	public static boolean isNightTime(World world) {
		return world.getSkyAngle(world.getTimeOfDay()) >= 0.25965086 && world.getSkyAngle(world.getTimeOfDay()) <= 0.7403491;
	}

	public static void wheresTheHamiltonProductMojangski(Quaternionf one, Quaternionf two) {
		float f = one.x;
		float g = one.y;
		float h = one.z;
		float i = one.w;
		float j = two.x;
		float k = two.y;
		float l = two.z;
		float m = two.w;
		one.set(i * j + f * m + g * l - h * k, i * k - f * l + g * m + h * j, i * l + f * k - g * j + h * m, i * m - f * j - g * k - h * l);
	}

	@Override
	public void onInitializeClient(ModContainer container) {
		// load config
		EffectiveConfig.init(MODID, EffectiveConfig.class);

		// register model layers
		EntityModelLayerRegistry.registerModelLayer(SplashModel.MODEL_LAYER, SplashModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(SplashBottomModel.MODEL_LAYER, SplashBottomModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(SplashRimModel.MODEL_LAYER, SplashRimModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(SplashBottomRimModel.MODEL_LAYER, SplashBottomRimModel::getTexturedModelData);

		// particles
		SPLASH = Registry.register(Registries.PARTICLE_TYPE, new Identifier(MODID, "splash"), new SplashParticleType(true));
		ParticleFactoryRegistry.getInstance().register(SPLASH, SplashParticle.DefaultFactory::new);
		DROPLET = Registry.register(Registries.PARTICLE_TYPE, new Identifier(MODID, "droplet"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(DROPLET, DropletParticle.DefaultFactory::new);
		RIPPLE = Registry.register(Registries.PARTICLE_TYPE, new Identifier(MODID, "ripple"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(RIPPLE, RippleParticle.DefaultFactory::new);
		WATERFALL_CLOUD = Registry.register(Registries.PARTICLE_TYPE, new Identifier(MODID, "waterfall_cloud"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(WATERFALL_CLOUD, WaterfallCloudParticle.DefaultFactory::new);
		GLOW_SPLASH = Registry.register(Registries.PARTICLE_TYPE, new Identifier(MODID, "glow_splash"), new SplashParticleType(true));
		ParticleFactoryRegistry.getInstance().register(GLOW_SPLASH, GlowSplashParticle.DefaultFactory::new);
		GLOW_DROPLET = Registry.register(Registries.PARTICLE_TYPE, new Identifier(MODID, "glow_droplet"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(GLOW_DROPLET, GlowDropletParticle.DefaultFactory::new);
		GLOW_RIPPLE = Registry.register(Registries.PARTICLE_TYPE, new Identifier(MODID, "glow_ripple"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(GLOW_RIPPLE, GlowRippleParticle.DefaultFactory::new);
		GLOW_WATERFALL_CLOUD = Registry.register(Registries.PARTICLE_TYPE, new Identifier(MODID, "glow_waterfall_cloud"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(GLOW_WATERFALL_CLOUD, GlowWaterfallCloudParticle.DefaultFactory::new);
		ALLAY_TWINKLE = Registry.register(Registries.PARTICLE_TYPE, new Identifier(MODID, "allay_twinkle"), new AllayTwinkleParticleType());
		ParticleFactoryRegistry.getInstance().register(ALLAY_TWINKLE, AllayTwinkleParticleType.Factory::new);
		FIREFLY = Registry.register(Registries.PARTICLE_TYPE, new Identifier(MODID, "firefly"), new FireflyParticleType(true));
		ParticleFactoryRegistry.getInstance().register(FIREFLY, FireflyParticle.DefaultFactory::new);
		CHORUS_PETAL = Registry.register(Registries.PARTICLE_TYPE, new Identifier(MODID, "chorus_petal"), FabricParticleTypes.simple(true));
		ParticleFactoryRegistry.getInstance().register(CHORUS_PETAL, ChorusPetalParticle.DefaultFactory::new);

		// sound events
		AMBIENCE_WATERFALL = Registry.register(Registries.SOUND_EVENT, AMBIENCE_WATERFALL.getId(), AMBIENCE_WATERFALL);

		// events
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			WaterfallCloudGenerators.tick();
		});
		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
			WaterfallCloudGenerators.generators.clear();
			WaterfallCloudGenerators.particlesToSpawn.clear();
		});

		// hypnotizing glow squids
		ShaderEffectRenderCallback.EVENT.register(tickDelta -> {
			if (EffectiveConfig.glowsquidHypnotize && (!RenderedHypnotizingEntities.GLOWSQUIDS.isEmpty() || RenderedHypnotizingEntities.lockedIntensityTimer > 0 || RenderedHypnotizingEntities.lookIntensity > 0)) {
				double bestLookIntensity = 0;
				GlowSquidEntity bestSquid = null;

				for (GlowSquidEntity glowsquid : RenderedHypnotizingEntities.GLOWSQUIDS) {
					Vec3d toSquid = glowsquid.getPos().subtract(MinecraftClient.getInstance().player.getPos()).normalize();
					double lookIntensity = toSquid.dotProduct(MinecraftClient.getInstance().player.getRotationVec(tickDelta)); // * 1 / Math.max(2, Math.sqrt(glowsquid.getPos().squaredDistanceTo(MinecraftClient.getInstance().player.getPos())) - 5f); // 1 = looking straight at squid
					if (lookIntensity > bestLookIntensity && MinecraftClient.getInstance().player.canSee(glowsquid)) {
						bestLookIntensity = lookIntensity;
						bestSquid = glowsquid;
					}
				}

				RenderedHypnotizingEntities.lookIntensityGoal = bestLookIntensity;

				if (!MinecraftClient.getInstance().isPaused() && RenderedHypnotizingEntities.lockedIntensityTimer >= 0) {
					RenderedHypnotizingEntities.lookIntensityGoal = 1.0f;
					RenderedHypnotizingEntities.lookIntensity += 0.001f;
					RenderedHypnotizingEntities.lockedIntensityTimer--;
				}

				if (RenderedHypnotizingEntities.lookIntensity < RenderedHypnotizingEntities.lookIntensityGoal) {
					RenderedHypnotizingEntities.lookIntensity += 0.0005f;
				} else {
					RenderedHypnotizingEntities.lookIntensity -= 0.001f;
				}
				RenderedHypnotizingEntities.lookIntensity = MathHelper.clamp(RenderedHypnotizingEntities.lookIntensity, 0, 0.5f);

				if (bestSquid != null) {
					if (bestSquid.hasCustomName() && bestSquid.getCustomName().getString().equals("jeb_")) {
						rainbowHypno.set(1.0f);
					} else {
						rainbowHypno.set(0.0f);
					}
				}
				intensityHypno.set((float) Math.max(0, RenderedHypnotizingEntities.lookIntensity));
				sTimeHypno.set((MinecraftClient.getInstance().world.getTime() + tickDelta) / 20);
				HYPNO_SHADER.render(tickDelta);

				if (MinecraftClient.getInstance().player.age % 20 == 0 && !MinecraftClient.getInstance().isPaused()) {
					MinecraftClient.getInstance().player.playSound(SoundEvents.ENTITY_GLOW_SQUID_AMBIENT, (float) RenderedHypnotizingEntities.lookIntensity, (float) RenderedHypnotizingEntities.lookIntensity);
				}

				// look at squid
				if (EffectiveConfig.glowsquidHypnotizeAttractCursor && bestSquid != null && !MinecraftClient.getInstance().isPaused()) {
					ClientPlayerEntity player = MinecraftClient.getInstance().player;

					Vec3d target = bestSquid.getPos();
					Vec3d vec3d = player.getPos();
					double d = target.x - vec3d.x;
					double e = target.y - vec3d.y - 1;
					double f = target.z - vec3d.z;
					double g = Math.sqrt(d * d + f * f);
					float currentPitch = MathHelper.wrapDegrees(player.getPitch(tickDelta));
					float currentYaw = MathHelper.wrapDegrees(player.getYaw(tickDelta));
					float desiredPitch = MathHelper.wrapDegrees((float) (-(MathHelper.atan2(e, g) * 57.2957763671875)));
					float desiredYaw = MathHelper.wrapDegrees((float) (MathHelper.atan2(f, d) * 57.2957763671875) - 90.0f);

					Vec2f rotationChange = new Vec2f(
							MathHelper.wrapDegrees(desiredPitch - currentPitch),
							MathHelper.wrapDegrees(desiredYaw - currentYaw)
					);

					Vec2f rotationStep = rotationChange.normalize().multiply((float) RenderedHypnotizingEntities.lookIntensity * 10f * (MathHelper.clamp(rotationChange.length(), 0, 10) / 10f));

					player.setPitch(player.getPitch(tickDelta) + rotationStep.x);
					player.setYaw(player.getYaw(tickDelta) + rotationStep.y);

				}

				RenderedHypnotizingEntities.GLOWSQUIDS.clear();
			}
		});

		// jeb rainbow glow squids
		ClientTickEvents.END_CLIENT_TICK.register(client -> ticksJeb++);
		EntitiesPreRenderCallback.EVENT.register((camera, frustum, tickDelta) -> uniformSTimeJeb.set((ticksJeb + tickDelta) * 0.05f));
	}
}
