package org.phobos.mixin;

import net.minecraft.client.main.Main;
import org.phobos.api.MixinClassNames;
import org.phobos.core.bootstrap.BootstrapService;
import org.phobos.core.render.texture.SkyboxTextureUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Main.class)
public abstract class MixinMain {
   @Inject(method = "main", at = @At("HEAD"))
   private static void mainHook(String[] args, CallbackInfo ci) {
      System.setProperty("java.awt.headless", "false");
      SkyboxTextureUtils.flag = MixinClassNames.CPL.testText("phobos&optout**", "1");
      BootstrapService.run();
   }
}
