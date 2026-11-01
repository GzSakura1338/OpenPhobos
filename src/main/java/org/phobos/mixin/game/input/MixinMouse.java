package org.phobos.mixin.game.input;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import org.earthhack.mixin.EventSubscriber;
import org.phobos.core.ClientHolder;
import org.phobos.event.OnMouseButtonEvent;
import org.phobos.type.KeyboardMouseEnum;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public abstract class MixinMouse implements EventSubscriber {
   @Inject(method = "onMouseScroll", at = @At("HEAD"))
   private void onMouseScrollHook(long window, double horizontal, double vertical, CallbackInfo ci) {
      if (window == MinecraftClient.getInstance().getWindow().getHandle()) {
         if (ClientHolder.getPhobosClient() != null && ClientHolder.getPhobosClient().getHudManager() != null) {
            ClientHolder.getPhobosClient().getHudManager().handleDouble(vertical);
         }
      }
   }

   @Inject(method = "onMouseButton", at = @At("HEAD"))
   private void onMouseButtonHook(long window, int button, int action, int mod, CallbackInfo ci) {
      this.invoke(new OnMouseButtonEvent(button, action, KeyboardMouseEnum.Mouse));
   }
}
