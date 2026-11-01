package org.phobos.mixin.game.misc;

import com.mojang.authlib.yggdrasil.TextureUrlChecker;
import org.phobos.core.PhobosLog;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = TextureUrlChecker.class, remap = false)
public abstract class MixinTextureUrlChecker {
   @Inject(method = "isAllowedTextureDomain", at = @At("HEAD"), cancellable = true)
   private static void isAllowedTextureDomainHook(String url, CallbackInfoReturnable<Boolean> cir) {
      if (url == null) {
         PhobosLog.handleText("Fixer", "The server has attempted to crash the client using null textures!");
         cir.setReturnValue(false);
      }
   }
}
