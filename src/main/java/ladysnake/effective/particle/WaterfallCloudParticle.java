package ladysnake.effective.particle;

import com.sammy.lodestone.systems.rendering.particle.world.GenericParticle;
import com.sammy.lodestone.systems.rendering.particle.world.WorldParticleEffect;
import net.fabricmc.fabric.impl.client.particle.FabricSpriteProviderImpl;
import net.minecraft.block.Blocks;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;

public class WaterfallCloudParticle extends GenericParticle {
	public WaterfallCloudParticle(ClientWorld world, WorldParticleEffect data, FabricSpriteProviderImpl spriteSet, double x, double y, double z, double xd, double yd, double zd) {
		super(world, data, spriteSet, x, y, z, xd, yd, zd);


		this.setSprite(this.spriteProvider.method_18139(world.random));
	}

	@Override
	public void tick() {
		super.tick();

		this.setSpriteForAge(spriteProvider);

		this.prevPosX = this.x;
		this.prevPosY = this.y;
		this.prevPosZ = this.z;

		if (this.onGround || (this.age > 10 && this.world.getBlockState(new BlockPos(this.x, this.y + this.velocityY, this.z)).getBlock() == Blocks.WATER)) {
			this.velocityX *= 0.5f;
			this.velocityY *= 0.5f;
			this.velocityZ *= 0.5f;
		}

		if (this.world.getBlockState(new BlockPos(this.x, this.y + this.velocityY, this.z)).getBlock() == Blocks.WATER && this.world.getBlockState(new BlockPos(this.x, this.y, this.z)).isAir()) {
			this.velocityX *= 0.9;
			this.velocityY *= 0.9;
			this.velocityZ *= 0.9;
		}

		this.velocityX *= 0.95f;
		this.velocityY -= 0.02f;
		this.velocityZ *= 0.95f;

		this.move(velocityX, velocityY, velocityZ);
	}
}
