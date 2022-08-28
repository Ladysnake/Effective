package ladysnake.effective.client.sound;

import ladysnake.effective.client.Effective;
import ladysnake.effective.client.EffectiveConfig;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class WaterfallSoundInstance extends DistancedSoundInstance {
    public WaterfallSoundInstance(SoundEvent soundEvent, SoundCategory soundCategory, float pitch, BlockPos blockPos, float maxDistance) {
        super(soundEvent, soundCategory, pitch, blockPos, maxDistance);
    }

    @Override
    public void tick() {
        super.tick();
        final float volumeAdjustor = EffectiveConfig.waterfallSoundVolume / 100.f;
        this.volume = MathHelper.clampedLerp(0f, volumeAdjustor, this.volume);
    }

    public static WaterfallSoundInstance ambient(SoundEvent soundEvent, float pitch, BlockPos blockPos, float maxDistance) {
        return new WaterfallSoundInstance(soundEvent, SoundCategory.AMBIENT, pitch, blockPos, maxDistance);
    }
}
