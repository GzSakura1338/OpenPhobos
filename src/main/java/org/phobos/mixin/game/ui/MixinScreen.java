package org.phobos.mixin.game.ui;

import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import org.earthhack.ducks.IClickEvent;
import org.jetbrains.annotations.Nullable;
import org.phobos.core.ClientHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Screen.class)
public abstract class MixinScreen {
   @Shadow
   public int height;
   @Shadow
   @Nullable
   protected MinecraftClient client;
   @Shadow
   protected TextRenderer textRenderer;
   @Shadow
   public int width;

   @Inject(method = "handleTextClick", at = @At("HEAD"), cancellable = true)
   private void handleTextClickHook2(Style style, CallbackInfoReturnable<Boolean> cir) {
      ClickEvent event = style.getClickEvent();
      if (event != null) {
         IClickEvent clickEvent = IClickEvent.cast(event);
         List<Runnable> callbacks = clickEvent.getCallbacks();
         if (!callbacks.isEmpty()) {
            for (Runnable callback : callbacks) {
               callback.run();
            }

            cir.setReturnValue(true);
            callbacks.clear();
         }
      }
   }

   @Shadow
   public abstract void render(DrawContext var1, int var2, int var3, float var4);

   @Shadow
   protected abstract <T extends Element & Drawable & Selectable> T addDrawableChild(T var1);

   @Inject(method = "close", at = @At("TAIL"))
   private void closeHook(CallbackInfo ci) {
      if ((Object)this instanceof ChatScreen) {
         ClientHolder.getPhobosClient().getCommandManager().getCommandCompletion().getTextMutableObject().setValue(null);
      }
   }
}
