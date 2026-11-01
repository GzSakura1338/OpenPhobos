package org.phobos.mixin.game.ui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.BossBarHud;
import org.earthhack.mixin.EventSubscriber;
import org.phobos.module.render.NoRenderModule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BossBarHud.class)
public abstract class MixinBossBarHud implements EventSubscriber {
   @Inject(method = "render", at = @At("HEAD"), cancellable = true)
   private void renderHook(DrawContext context, CallbackInfo ci) {
      if (NoRenderModule.check2()) {
         ci.cancel();
      }
   }
}
