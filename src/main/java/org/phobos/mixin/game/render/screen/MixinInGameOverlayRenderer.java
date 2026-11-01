package org.phobos.mixin.game.render.screen;

import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import org.earthhack.mixin.EventSubscriber;
import org.phobos.core.event.EventCallbackUtils;
import org.phobos.module.render.NoRenderModule;
import org.phobos.module.render.NoRenderModule;
import org.phobos.module.render.NoRenderModule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameOverlayRenderer.class)
public abstract class MixinInGameOverlayRenderer implements EventSubscriber {
   @Inject(method = "renderFireOverlay", at = @At("HEAD"), cancellable = true)
   private static void renderFireOverlay(CallbackInfo ci) {
      EventCallbackUtils.handleCancellableEvent2(new NoRenderModule.Fire(), ci);
   }

   @Inject(method = "renderUnderwaterOverlay", at = @At("HEAD"), cancellable = true)
   private static void renderUnderwaterOverlay(CallbackInfo ci) {
      EventCallbackUtils.handleCancellableEvent2(new NoRenderModule.Liquids(), ci);
   }

   @Inject(method = "renderInWallOverlay", at = @At("HEAD"), cancellable = true)
   private static void renderInWallOverlay(CallbackInfo ci) {
      EventCallbackUtils.handleCancellableEvent2(new NoRenderModule.Blocks(), ci);
   }
}
