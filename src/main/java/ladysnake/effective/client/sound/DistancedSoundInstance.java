package ladysnake.effective.client.sound;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.TickableSoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class DistancedSoundInstance extends PositionedSoundInstance implements TickableSoundInstance {
	private final float maxDistance;

	public DistancedSoundInstance(SoundEvent soundEvent, SoundCategory soundCategory, float pitch, BlockPos blockPos, float maxDistance) {
		super(soundEvent, soundCategory, 0.0f, pitch, blockPos);
		this.maxDistance = maxDistance;
		this.repeat = false;
	}

	public static DistancedSoundInstance ambient(SoundEvent soundEvent, float pitch, BlockPos blockPos, float maxDistance) {
		return new DistancedSoundInstance(soundEvent, SoundCategory.AMBIENT, pitch, blockPos, maxDistance);
	}

	@Override
	public AttenuationType getAttenuationType() {
		return AttenuationType.NONE;
	}

	@Override
	public boolean isDone() {
		return false;
	}

	@Override
	public void tick() {
		float distance = MathHelper.sqrt((float) MinecraftClient.getInstance().player.getPos().squaredDistanceTo(this.x, this.y, this.z));
		this.volume = MathHelper.clampedLerp(0f, 1.0f, 1.0f - distance / this.maxDistance);
	}

}
