package org.phobos.mixin.game.input;

import net.minecraft.client.input.KeyboardInput;
import org.earthhack.mixin.EventSubscriber;
import org.phobos.event.TickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public abstract class MixinKeyboardInput implements EventSubscriber {
   @Inject(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/client/input/KeyboardInput;sneaking:Z", shift = Shift.AFTER))
   private void tickHook(boolean slowDown, float slowDownFactor, CallbackInfo ci) {
      this.invoke(new TickEvent((KeyboardInput)(Object)this));
   }
}
