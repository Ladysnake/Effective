package ladysnake.effective.mixin.screeshake;

import com.sammy.ortus.handlers.ScreenshakeHandler;
import com.sammy.ortus.systems.rendering.particle.Easing;
import com.sammy.ortus.systems.screenshake.PositionedScreenshakeInstance;
import com.sammy.ortus.systems.screenshake.ScreenshakeInstance;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Explosion.class)
public class ExplosionMixin {
	@Inject(method = "<init>(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;Lnet/minecraft/world/explosion/ExplosionBehavior;DDDFZLnet/minecraft/world/explosion/Explosion$DestructionType;)V", at = @At("TAIL"))
	public void init(World world, Entity entity, DamageSource damageSource, ExplosionBehavior behavior, double x, double y, double z, float power, boolean createFire, Explosion.DestructionType destructionType, CallbackInfo ci) {
		ScreenshakeInstance screenShake = new PositionedScreenshakeInstance(10, new Vec3d(x, y, z), 20f, 0f, 25f, Easing.CIRC_IN_OUT).setIntensity(0.0f, 1.0f, 0.0f);
		ScreenshakeHandler.addScreenshake(screenShake);
	}
}
