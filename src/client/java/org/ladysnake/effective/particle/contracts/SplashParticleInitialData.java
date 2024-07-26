package org.ladysnake.effective.particle.contracts;

public record SplashParticleInitialData(double width, double velocityY) {
	public SplashParticleInitialData(double width, double velocityY) {
		this.width = width;
		this.velocityY = Math.abs(velocityY);
	}
}
