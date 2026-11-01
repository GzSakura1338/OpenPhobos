package org.phobos.mixin.game.render.entity;

import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.world.BlockView;
import org.earthhack.mixin.EventSubscriber;
import org.phobos.module.client.AntiCheatModule;
import org.phobos.module.render.CameraModule;
import org.phobos.module.render.FreecamModule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(Camera.class)
public abstract class MixinCamera implements EventSubscriber {
   @Shadow
   private float cameraY;
   @Shadow
   private Entity focusedEntity;
   @Shadow
   private float lastCameraY;
   @Shadow
   private boolean thirdPerson;

   @ModifyArgs(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;setPos(DDD)V"))
   private void updateHook3(Args args) {
      if (FreecamModule.getFreecamModule().check7()) {
         double x = FreecamModule.getFreecamModule().getDoubleValue3().getDoubleValue();
         double y = FreecamModule.getFreecamModule().getDoubleValue2().getDoubleValue();
         double z = FreecamModule.getFreecamModule().getDoubleValue().getDoubleValue();
         args.setAll(new Object[]{x, y, z});
      }
   }

   @Shadow
   protected abstract float clipToSpace(float var1);

   @Redirect(method = "clipToSpace", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/hit/HitResult;getType()Lnet/minecraft/util/hit/HitResult$Type;"))
   private Type clipToSpaceHook(HitResult instance) {
      return CameraModule.resolveType(instance.getType());
   }

   @Inject(method = "updateEyeHeight", at = @At("HEAD"), cancellable = true)
   private void updateEyeHeightHook(CallbackInfo ci) {
      if (this.focusedEntity != null && AntiCheatModule.Client.check()) {
         this.cameraY = this.lastCameraY = this.focusedEntity.getStandingEyeHeight();
         ci.cancel();
      }
   }

   @Shadow
   protected abstract void moveBy(float var1, float var2, float var3);

   @Inject(method = "update", at = @At("TAIL"))
   private void updateHook1(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
      if (FreecamModule.getFreecamModule().check7()) {
         this.thirdPerson = true;
      }
   }

   @Redirect(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;clipToSpace(F)F"))
   private float moveByHook(Camera instance, float f) {
      return this.clipToSpace(CameraModule.computeFloat(f));
   }
}
