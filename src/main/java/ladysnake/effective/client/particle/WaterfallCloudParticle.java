package ladysnake.effective.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Blocks;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.*;

public class WaterfallCloudParticle extends SpriteBillboardParticle {
    private final SpriteProvider spriteProvider;
    private int invisibleTimer;

    public WaterfallCloudParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(world, x, y, z, velocityX, velocityY, velocityZ);

        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;

        this.spriteProvider = spriteProvider;
        this.maxAge = 500;
        this.scale *= 3.0f + random.nextFloat();
        this.setSpriteForAge(spriteProvider);

        this.alpha = 0f;

        this.invisibleTimer = 1;
        this.setSprite(this.spriteProvider.getSprite(world.random));
    }

    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        this.invisibleTimer--;

        if (this.invisibleTimer == 0) {
            this.alpha = 0.5f + random.nextFloat() * 0.25f;
        }

        if (this.invisibleTimer <= 0) {
            this.prevPosX = this.x;
            this.prevPosY = this.y;
            this.prevPosZ = this.z;

            if (this.age++ >= this.maxAge) {
                this.alpha -= 0.1f;
                this.velocityX *= 0.5f;
                this.velocityY *= 0.5f;
                this.velocityZ *= 0.5f;
            }

            if (this.onGround || (this.age > 10 && this.world.getBlockState(new BlockPos(this.x, this.y + this.velocityY, this.z)).getBlock() == Blocks.WATER)) {
                this.alpha -= 0.1f;
                this.velocityX *= 0.5f;
                this.velocityY *= 0.5f;
                this.velocityZ *= 0.5f;
            }

            if (this.alpha <= 0) {
                this.markDead();
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
        } else {
            this.alpha = 0;
        }
    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        Vec3d vec3d = camera.getPos();
        float f = (float) (MathHelper.lerp(tickDelta, this.prevPosX, this.x) - vec3d.getX());
        float g = (float) (MathHelper.lerp(tickDelta, this.prevPosY, this.y) - vec3d.getY());
        float h = (float) (MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - vec3d.getZ());
        Quaternion quaternion2;
        if (this.angle == 0.0F) {
            quaternion2 = camera.getRotation();
        } else {
            quaternion2 = new Quaternion(camera.getRotation());
            float i = MathHelper.lerp(tickDelta, this.prevAngle, this.angle);
            quaternion2.hamiltonProduct(Vec3f.POSITIVE_Z.getRadialQuaternion(i));
        }

        Vec3f Vec3f = new Vec3f(-1.0F, -1.0F, 0.0F);
        Vec3f.rotate(quaternion2);
        Vec3f[] Vec3fs = new Vec3f[]{new Vec3f(-1.0F, -1.0F, 0.0F), new Vec3f(-1.0F, 1.0F, 0.0F), new Vec3f(1.0F, 1.0F, 0.0F), new Vec3f(1.0F, -1.0F, 0.0F)};
        float j = this.getSize(tickDelta);

        for (int k = 0; k < 4; ++k) {
            Vec3f Vec3f2 = Vec3fs[k];
            Vec3f2.rotate(quaternion2);
            Vec3f2.scale(j);
            Vec3f2.add(f, g, h);
        }

        float minU = this.getMinU();
        float maxU = this.getMaxU();
        float minV = this.getMinV();
        float maxV = this.getMaxV();
        int l = this.getBrightness(tickDelta);

        vertexConsumer.vertex(Vec3fs[0].getX(), Vec3fs[0].getY(), Vec3fs[0].getZ()).texture(maxU, maxV).color(red, green, blue, alpha).light(l).next();
        vertexConsumer.vertex(Vec3fs[1].getX(), Vec3fs[1].getY(), Vec3fs[1].getZ()).texture(maxU, minV).color(red, green, blue, alpha).light(l).next();
        vertexConsumer.vertex(Vec3fs[2].getX(), Vec3fs[2].getY(), Vec3fs[2].getZ()).texture(minU, minV).color(red, green, blue, alpha).light(l).next();
        vertexConsumer.vertex(Vec3fs[3].getX(), Vec3fs[3].getY(), Vec3fs[3].getZ()).texture(minU, maxV).color(red, green, blue, alpha).light(l).next();
    }

    @Environment(EnvType.CLIENT)
    public static class DefaultFactory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public DefaultFactory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new WaterfallCloudParticle(world, x, y, z, velocityX, velocityY, velocityZ, this.spriteProvider);
        }
    }
}
