package org.ladysnake.effective;

import net.minecraft.util.math.MathHelper;
import org.joml.Vector3f;
import team.lodestar.lodestone.systems.rendering.particle.world.GenericParticle;

import java.util.function.Consumer;


/**
 * Arathain removed setForcedMotion and motion coefficient.
 * Here is my implementation of it or rather a copy paste from LodestoneLib 1.19.2
 * It makes a particle travel from one point to another and can be applied using
 * {@link team.lodestar.lodestone.systems.rendering.particle.WorldParticleBuilder#addActor(Consumer)}
 * @author SzczurekYT
 */
public class LinearForcedMotionImpl implements Consumer<GenericParticle> {

	private final Vector3f start;
	private final Vector3f end;
	private final float coefficient;

	public LinearForcedMotionImpl(Vector3f start, Vector3f end, float coefficient) {
		this.start = start;
		this.end = end;
		this.coefficient = coefficient;
	}

	@Override
	public void accept(GenericParticle particle) {
		float motionAge = particle.getCurve(coefficient);
		float velocityX = MathHelper.lerp(motionAge, start.x(), end.x());
		float velocityY = MathHelper.lerp(motionAge, start.y(), end.y());
		float velocityZ = MathHelper.lerp(motionAge, start.z(), end.z());
		particle.setVelocity(velocityX, velocityY, velocityZ);
	}
}
