package org.phobos.mixin.game.misc;

import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import org.earthhack.mixin.EventSubscriber;
import org.phobos.module.render.NoRenderModule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ToastManager.class)
public abstract class MixinToastManager implements EventSubscriber {
   @Inject(method = "add", at = @At("HEAD"), cancellable = true)
   private void addHook(Toast toast, CallbackInfo ci) {
      this.invoke(new NoRenderModule.Toast(), ci);
   }
}
