package org.phobos.mixin.game.render.process;

import net.minecraft.client.render.model.json.Transformation;
import net.minecraft.client.util.math.MatrixStack;
import org.phobos.module.render.ItemPhysicsModule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Transformation.class)
public abstract class MixinTransformation {
   @Inject(method = "apply", at = @At("HEAD"), cancellable = true)
   private void applyHook(boolean leftHanded, MatrixStack matrices, CallbackInfo ci) {
      if (ItemPhysicsModule.check()) {
         ci.cancel();
      }
   }
}
