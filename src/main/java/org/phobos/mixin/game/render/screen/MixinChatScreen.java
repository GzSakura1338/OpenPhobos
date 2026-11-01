package org.phobos.mixin.game.render.screen;

import java.awt.Color;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import org.earthhack.ducks.IChatScreen;
import org.earthhack.ducks.IClickEvent;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.MinecraftHolder;
import org.phobos.core.command.ChatPrefix;
import org.phobos.core.command.CommandManager;
import org.phobos.core.gui.MenuNavigation;
import org.phobos.core.ClientHolder;
import org.phobos.core.render.ColorUtils;
import org.phobos.core.gui.CursorUtils;
import org.phobos.core.gui.CursorUtils;
import org.phobos.mixin.game.ui.MixinScreen;
import org.phobos.module.misc.ChatModule;
import org.phobos.module.client.ColorsModule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChatScreen.class)
public abstract class MixinChatScreen extends MixinScreen implements IChatScreen, MinecraftHolder {
   @Shadow
   protected TextFieldWidget chatField;

   @Unique
   private void renderOutline(MenuNavigation uiManager, DrawContext instance, int x1, int y1, int x2, int y2) {
      float alpha = uiManager.getFloatAnimator5().getFloatValue2();
      if (!(alpha <= 0.05F)) {
         Color color = ColorUtils.createColor6(ColorsModule.getColorsModule().color2.getValue(), alpha);
         instance.drawBorder(x1, y1, x2 - x1, y2 - y1, color.hashCode());
      }
   }

   @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;pop()V", ordinal = 0), cancellable = true)
   private void renderHook2(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
      if (ChatModule.check2()) {
         ci.cancel();
      }
   }

   @Inject(method = "onChatFieldUpdate", at = @At("HEAD"))
   private void onChatFieldUpdateHook(String chatText, CallbackInfo ci) {
      ClientHolder.getPhobosClient().getCommandManager().handleText3(this.chatField.getText());
   }

   @Override
   public TextFieldWidget getChatField() {
      return this.chatField;
   }

   @Shadow
   @Nullable
   protected abstract Style getTextStyleAt(double var1, double var3);

   @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
   private void keyPressedHook(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
      if (keyCode == 258) {
         CommandManager manager = ClientHolder.getPhobosClient().getCommandManager();
         String suggestion = manager.getCommandCompletion().describe2();
         if (suggestion != null) {
            this.chatField.setText(suggestion);
            cir.setReturnValue(true);
         }
      }
   }

   @Inject(
      method = "render",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/TextFieldWidget;render(Lnet/minecraft/client/gui/DrawContext;IIF)V")
   )
   private void renderHook(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
      if (!ChatModule.check2()) {
         CommandManager manager = ClientHolder.getPhobosClient().getCommandManager();
         String suggestion = manager.getCommandCompletion().describe2();
         if (suggestion != null) {
            context.drawTextWithShadow(mc.textRenderer, suggestion, this.chatField.getX(), this.chatField.getY(), ChatModule.intValue);
         }
      }
   }

   @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V"))
   private void renderHook(DrawContext instance, int x1, int y1, int x2, int y2, int color) {
      MenuNavigation uiManager = ClientHolder.getPhobosClient().getMenuNavigation();
      uiManager.getFloatAnimator5().setFloatValue(ChatPrefix.getINSTANCE().testText(this.chatField.getText()) ? 1.0F : 0.0F);
      if (!ChatModule.check2()) {
         instance.fill(x1, y1, x2, y2, color);
         this.renderOutline(uiManager, instance, x1, y1, x2, y2);
      }
   }

   @Inject(method = "render", at = @At("TAIL"))
   private void renderHook1(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
      Style style = this.getTextStyleAt(mouseX, mouseY);
      if (style == null) {
         CursorUtils.run2();
      } else {
         ClickEvent event = style.getClickEvent();
         if (event == null) {
            CursorUtils.run2();
         } else {
            IClickEvent clickEvent = IClickEvent.cast(event);
            if (clickEvent.getCallbacks().isEmpty()) {
               CursorUtils.run2();
            } else {
               CursorUtils.handleStyle(CursorUtils.Style.Link);
            }
         }
      }
   }
}
