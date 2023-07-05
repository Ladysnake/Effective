package ladysnake.effective.particle;

import com.sammy.lodestone.systems.rendering.particle.world.GenericParticle;
import com.sammy.lodestone.systems.rendering.particle.world.WorldParticleEffect;
import net.fabricmc.fabric.impl.client.particle.FabricSpriteProviderImpl;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;

public class BubbleParticle extends GenericParticle {
	public BubbleParticle(ClientWorld world, WorldParticleEffect data, FabricSpriteProviderImpl spriteSet, double x, double y, double z, double xd, double yd, double zd) {
		super(world, data, spriteSet, x, y, z, xd, yd, zd);
	}

	@Override
	public void tick() {
		super.tick();

		if (!world.isWater(BlockPos.create(this.x, this.y, this.z))) {
			this.markDead();
		}
	}
}
