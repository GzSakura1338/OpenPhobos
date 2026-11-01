package org.phobos.mixin.game.entity;

import net.minecraft.entity.passive.AbstractHorseEntity;
import org.phobos.module.movement.EntityControlModule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractHorseEntity.class)
public abstract class MixinAbstractHorseEntity {
   @Inject(method = "isSaddled", at = @At("HEAD"), cancellable = true)
   private void isSaddledHook(CallbackInfoReturnable<Boolean> cir) {
      if (EntityControlModule.check2()) {
         cir.setReturnValue(true);
      }
   }
}
