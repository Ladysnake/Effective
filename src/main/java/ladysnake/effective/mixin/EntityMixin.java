package ladysnake.effective.mixin;

import ladysnake.effective.client.Effective;
import ladysnake.effective.client.EffectiveConfig;
import ladysnake.effective.client.contracts.SplashParticleInitialData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.Random;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow
    public World world;
    @Shadow
    @Final
    protected Random random;

    @Shadow
    public abstract double getX();

    @Shadow
    public abstract double getZ();

    @Shadow
    public abstract double getY();

    @Shadow
    public abstract boolean hasPassengers();

    @Shadow
    @Nullable
    public abstract Entity getPrimaryPassenger();

    @Shadow
    public abstract float getWidth();

    @Inject(method = "onSwimmingStart", at = @At("TAIL"))
    protected void onSwimmingStart(CallbackInfo callbackInfo) {
        if (this.world.isClient && EffectiveConfig.generateSplashes) {
            Entity entity = this.hasPassengers() && this.getPrimaryPassenger() != null ? this.getPrimaryPassenger() : (Entity) (Object) this;
            if (!(entity instanceof FishingBobberEntity)) {
                float f = entity == (Object) this ? 0.2f : 0.9f;
                Vec3d vec3d = entity.getVelocity();
                float g = Math.min(1.0f, (float) Math.sqrt(vec3d.x * vec3d.x * (double) 0.2f + vec3d.y * vec3d.y + vec3d.z * vec3d.z * (double) 0.2f) * f);
                for (int i = -10; i < 10; i++) {
                    if (this.world.getBlockState(new BlockPos(this.getX(), Math.round(this.getY()) + i, this.getZ())).getFluidState().getFluid() == Fluids.WATER && this.world.getBlockState(new BlockPos(this.getX(), Math.round(this.getY()) + i, this.getZ())).getFluidState().isStill() && this.world.getBlockState(new BlockPos(this.getX(), Math.round(this.getY()) + i, this.getZ())).getFluidState().isStill() && this.world.getBlockState(new BlockPos(this.getX(), Math.round(this.getY()) + i + 1, this.getZ())).isAir()) {
                        this.world.playSound(this.getX(), Math.round(this.getY()) + i + 0.9f, this.getZ(), SoundEvents.ENTITY_GENERIC_SPLASH, SoundCategory.AMBIENT, g * 10f, 0.8f, true);
                        SplashParticleInitialData data = new SplashParticleInitialData(entity.getWidth(), vec3d.getY());
                        this.world.addParticle(Effective.SPLASH.setData(data), this.getX(), Math.round(this.getY()) + i + 0.9f, this.getZ(), 0, 0, 0);
                        break;
                    }
                }

                for (int j = 0; j < this.getWidth() * 25f; j++) {
                    this.world.addParticle(Effective.DROPLET, this.getX() + random.nextGaussian() * this.getWidth() / 5f, this.getY(), this.getZ() + random.nextGaussian() * this.getWidth(), random.nextGaussian() / 15f, random.nextFloat() / 2.5f, random.nextGaussian() / 15f);
                }
            }
        }
    }

//    @Inject(method = "tick", at = @At("TAIL"))
//    public void tick(CallbackInfo callbackInfo) {
//        if (!this.onGround && !this.touchingWater && !this.isInLava() && world.getBlockState(this.getBlockPos().add(this.getVelocity().x, this.getVelocity().y, this.getVelocity().z)).getBlock() == Blocks.LAVA) {
//            if (this.world.isClient) {
//                Entity entity = this.hasPassengers() && this.getPrimaryPassenger() != null ? this.getPrimaryPassenger() : (Entity) (Object) this;
//                float f = entity == (Object) this ? 0.2f : 0.9f;
//                Vec3d vec3d = entity.getVelocity();
//                float g = Math.min(1.0f, (float) Math.sqrt(vec3d.x * vec3d.x * (double) 0.2f + vec3d.y * vec3d.y + vec3d.z * vec3d.z * (double) 0.2f) * f);
//                System.out.println(g);
//                if (g > 0.05f) {
//                    for (int i = -10; i < 10; i++) {
//                        if (this.world.getBlockState(new BlockPos(this.getX(), Math.round(this.getY()) + i, this.getZ())).getBlock() == Blocks.LAVA && this.world.getBlockState(new BlockPos(this.getX(), Math.round(this.getY()) + i, this.getZ())).getFluidState().isStill() && this.world.getBlockState(new BlockPos(this.getX(), Math.round(this.getY()) + i + 1, this.getZ())).isAir()) {
//                            this.world.playSound(this.getX(), Math.round(this.getY()) + i + 0.9f, this.getZ(), SoundEvents.ITEM_BUCKET_FILL_LAVA, SoundCategory.AMBIENT, 1.0f, 0.8f, true);
//                            this.world.addParticle(Effective.LAVA_SPLASH, this.getX(), Math.round(this.getY()) + i + 0.9f, this.getZ(), 0, 0, 0);
//                            break;
//                        }
//                    }
//                }
//            }
//        }
//    }
}