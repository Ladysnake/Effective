package ladysnake.effective.client.contracts;

public class SplashParticleInitialData {
	public double width;
	public double velocityY;

	public SplashParticleInitialData(double width, double velocityY) {
		this.width = width;
		this.velocityY = Math.abs(velocityY);
	}
}
