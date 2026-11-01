package org.phobos.mixin.game.input;

import net.minecraft.client.Keyboard;
import org.earthhack.mixin.EventSubscriber;
import org.phobos.event.OnMouseButtonEvent;
import org.phobos.type.KeyboardMouseEnum;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public abstract class MixinKeyboardHandler implements EventSubscriber {
   @Inject(method = "onKey", at = @At("HEAD"))
   private void onKeyHook(long ctx, int key, int scan, int action, int mod, CallbackInfo ci) {
      this.invoke(new OnMouseButtonEvent(key, action, KeyboardMouseEnum.Keyboard));
   }
}
