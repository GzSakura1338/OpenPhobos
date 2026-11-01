package org.phobos.mixin.game.misc;

import net.minecraft.util.Formatting;
import org.phobos.core.chat.TextFormattingUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Formatting.class)
public abstract class MixinFormatting {
   @Inject(method = "strip", at = @At("HEAD"), cancellable = true)
   private static void stripHook(String string, CallbackInfoReturnable<String> cir) {
      cir.setReturnValue(TextFormattingUtils.describeText(string));
   }

   @Inject(method = "byCode", at = @At("HEAD"), cancellable = true)
   private static void byCode(char code, CallbackInfoReturnable<Formatting> cir) {
      if (code == 'R') {
         cir.setReturnValue(Formatting.RESET);
      }
   }
}
