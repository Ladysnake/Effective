package org.ladysnake.effective.core.index;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.ladysnake.effective.core.Effective;
import org.ladysnake.effective.core.particle.*;
import org.ladysnake.effective.core.particle.types.SplashParticleType;

import java.util.LinkedHashMap;
import java.util.Map;

public interface EffectiveParticles {

	Map<ParticleType<?>, Identifier> PARTICLES = new LinkedHashMap<>();

	SplashParticleType SPLASH = create("splash", new SplashParticleType(true));
	SplashParticleType GLOW_SPLASH = create("glow_splash", new SplashParticleType(true));
	SimpleParticleType DROPLET = create("droplet", FabricParticleTypes.simple(true));
	SimpleParticleType GLOW_DROPLET = create("glow_droplet", FabricParticleTypes.simple(true));
	SimpleParticleType RIPPLE = create("ripple", FabricParticleTypes.simple(true));
	SimpleParticleType GLOW_RIPPLE = create("glow_ripple", FabricParticleTypes.simple(true));
	SimpleParticleType BUBBLE = create("bubble", FabricParticleTypes.simple(true));
	SimpleParticleType END_BUBBLE = create("end_bubble", FabricParticleTypes.simple(true));
	SimpleParticleType CASCADE = create("cascade", FabricParticleTypes.simple(true));
	SimpleParticleType GLOW_CASCADE = create("glow_cascade", FabricParticleTypes.simple(true));
	SimpleParticleType MIST = create("mist", FabricParticleTypes.simple(true));
	SimpleParticleType CHORUS_PETAL = create("chorus_petal", FabricParticleTypes.simple(true));
	SimpleParticleType FIREFLY = create("firefly", FabricParticleTypes.simple(true));

	static void initialize() {
		PARTICLES.keySet().forEach(particle -> Registry.register(Registries.PARTICLE_TYPE, PARTICLES.get(particle), particle));

		registerFactories();
	}

	private static <T extends ParticleType<?>> T create(String name, T particle) {
		PARTICLES.put(particle, Effective.id(name));
		return particle;
	}

	private static void registerFactories() {
		ParticleFactoryRegistry.getInstance().register(SPLASH, SplashParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(GLOW_SPLASH, GlowSplashParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(DROPLET, DropletParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(GLOW_DROPLET, GlowDropletParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(RIPPLE, RippleParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(GLOW_RIPPLE, GlowRippleParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(BUBBLE, BubbleParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(END_BUBBLE, EndBubbleParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(CASCADE, CascadeParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(GLOW_CASCADE, GlowCascadeParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(MIST, MistParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(CHORUS_PETAL, ChorusPetalParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(FIREFLY, FireflyParticle.Factory::new);
	}
}
