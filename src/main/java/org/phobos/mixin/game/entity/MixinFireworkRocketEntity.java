package org.phobos.mixin.game.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.phobos.core.rotation.RotationManager;
import org.phobos.core.rotation.RotationManager;
import org.phobos.core.ClientHolder;
import org.phobos.module.movement.ElytraFlyModule;
import org.phobos.module.movement.FireworksModule;
import org.phobos.module.client.RotationsModule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FireworkRocketEntity.class)
public abstract class MixinFireworkRocketEntity extends MixinEntity {
   @Shadow
   @Nullable
   private LivingEntity shooter;

   @ModifyExpressionValue(
      method = "tick",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getRotationVector()Lnet/minecraft/util/math/Vec3d;")
   )
   private Vec3d tickHook2(Vec3d original) {
      return this.getRotation(original);
   }

   @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V"))
   private void tickHook1(LivingEntity instance, Vec3d vel) {
      if (!FireworksModule.isFlag()) {
         instance.setVelocity(vel);
      } else {
         Vec3d rotation = this.getRotation(instance.getRotationVector());
         Vec3d velocity = instance.getVelocity();
         double delta = FireworksModule.computeDouble(1.5);
         instance.setVelocity(
            velocity.add(
               rotation.x * 0.1 + (rotation.x * delta - velocity.x) * 0.5,
               rotation.y * 0.1 + (rotation.y * delta - velocity.y) * 0.5,
               rotation.z * 0.1 + (rotation.z * delta - velocity.z) * 0.5
            )
         );
      }
   }

   @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isFallFlying()Z"))
   private boolean tickHook(LivingEntity instance) {
      return ElytraFlyModule.check3() ? false : instance.isFallFlying();
   }

   @Unique
   private Vec3d getRotation(Vec3d original) {
      if (!(this.shooter instanceof ClientPlayerEntity)) {
         return original;
      }

      if (!RotationsModule.check2()) {
         return original;
      }

      RotationManager manager = ClientHolder.getPhobosClient().getRotationManager();
      RotationManager.Rotation rotation = RotationManager.Rotation.createRotation(manager.getFloatValue15(), manager.getFloatValue17());
      return rotation.getVec3d();
   }
}
