package org.phobos.mixin.game.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.client.gui.hud.ChatHudLine.Visible;
import net.minecraft.client.gui.hud.MessageIndicator.Icon;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.earthhack.ducks.IChat;
import org.earthhack.mixin.EventSubscriber;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.Easing;
import org.phobos.core.chat.SignedMessage;
import org.phobos.core.animation.EasedAnimation;
import org.phobos.core.hud.element.ChatHudElement;
import org.phobos.event.AddMessageEvent;
import org.phobos.module.misc.ChatModule;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatHud.class)
public abstract class MixinChatHud implements IChat, EventSubscriber {
   @Unique
   private int prevScrolledLines;
   @Shadow
   private boolean hasUnreadNewMessages;
   @Unique
   private boolean logLock = false;
   @Unique
   private final EasedAnimation chatAnimation = new EasedAnimation(150L);
   @Shadow
   private int scrolledLines;
   @Shadow
   @Final
   private List<Visible> visibleMessages;
   @Unique
   private boolean flag = false;
   @Shadow
   @Final
   private MinecraftClient client;
   @Shadow
   @Final
   private List<ChatHudLine> messages;
   @Unique
   private long prevAnimation;

   @Inject(method = "drawIndicatorIcon", at = @At("HEAD"), cancellable = true)
   private void drawIndicatorIconHook(DrawContext context, int x, int y, Icon icon, CallbackInfo ci) {
      if (ChatModule.getChatModule().check7() && !ChatModule.getChatModule().getIndicators().getValue()) {
         ci.cancel();
      }
   }

   @Shadow
   public abstract int getWidth();

   @Shadow
   protected abstract double toChatLineY(double var1);

   @Shadow
   protected abstract int getMessageIndex(double var1, double var3);

   @Inject(method = "addVisibleMessage", at = @At("HEAD"))
   private void addVisibleMessageHook(ChatHudLine message, CallbackInfo ci) {
      this.chatAnimation.setLongValue3(System.currentTimeMillis());
   }

   @Unique
   private void withLogLock(Runnable runnable) {
      this.logLock = true;
      runnable.run();
      this.logLock = false;
   }

   @Shadow
   protected abstract int getIndicatorX(Visible var1);

   @Shadow
   public abstract void addMessage(Text var1, @Nullable MessageSignatureData var2, @Nullable MessageIndicator var3);

   @Unique
   private static double getMessageOpacityMultiplier0(int age) {
      double d = age / 200.0;
      d = 1.0 - d;
      d *= 10.0;
      d = MathHelper.clamp(d, 0.0, 1.0);
      return d * d;
   }

   @Shadow
   protected abstract void refresh();


   @Override
   public void backup() {
      this.prevScrolledLines = this.scrolledLines;
      this.prevAnimation = this.chatAnimation.getLongValue3();
   }

   @Inject(method = "logChatMessage", at = @At("HEAD"), cancellable = true)
   private void logChatMessage(ChatHudLine message, CallbackInfo ci) {
      if (this.logLock) {
         ci.cancel();
      }
   }

   @Shadow
   protected abstract void addVisibleMessage(ChatHudLine var1);

   @Shadow
   public abstract double getChatScale();

   @Shadow
   protected abstract boolean isChatHidden();

   @Shadow
   protected abstract int getLineHeight();

   @Overwrite
   @SuppressWarnings("overwrite")
   public void render(DrawContext context, int currentTick, int mouseX, int mouseY, boolean focused) {
      if (!ChatHudElement.alwaysFalse()) {
         if (!this.isChatHidden()) {
            int visibleCount = this.getVisibleLineCount();
            int chatLinesSize = this.visibleMessages.size();
            if (chatLinesSize != 0) {
               float scaling = (float)this.getChatScale();
               int chatWidth = MathHelper.ceil(this.getWidth() / scaling);
               int height = context.getScaledWindowHeight();
               context.getMatrices().push();
               context.getMatrices().scale(scaling, scaling, 1.0F);
               context.getMatrices().translate(4.0F, 0.0F, 0.0F);
               int startY = MathHelper.floor((height - 40) / scaling);
               int messageIndex = this.getMessageIndex(this.toChatLineX(mouseX), this.toChatLineY(mouseY));
               double chatOpacity = (Double)(Object)this.client.options.getChatOpacity().getValue() * 0.9F + 0.1F;
               double textBgOpacity = (Double)(Object)this.client.options.getTextBackgroundOpacity().getValue();
               double lineSpacing = (Double)(Object)this.client.options.getChatLineSpacing().getValue();
               int lineHeight = this.getLineHeight();
               int yOffset = (int)(
                  (int)Math.round(-8.0 * (lineSpacing + 1.0) + 4.0 * lineSpacing)
                     + (ChatModule.check() ? this.chatAnimation.computeEasing(Easing.InOutEasing.INSTANCE3) * 10.0F : 0.0F)
               );
               Visible first = this.visibleMessages.getFirst();
               int q = 0;

               for (int r = 0; r + this.scrolledLines < this.visibleMessages.size() && r < visibleCount; r++) {
                  int s = r + this.scrolledLines;
                  Visible visible = this.visibleMessages.get(s);
                  int t;
                  if (visible != null && ((t = currentTick - visible.addedTime()) < 200 || focused)) {
                     double h = focused ? 1.0 : getMessageOpacityMultiplier0(t);
                     if (!focused && t <= 10 && ChatModule.check()) {
                        h = MathHelper.clamp(t / 10.0 * 2.2553862, 0.0, h);
                     }

                     int u = (int)(255.0 * h * chatOpacity);
                     int v = (int)(255.0 * h * textBgOpacity);
                     q++;
                     if (u > 3) {
                        int x = startY - r * lineHeight;
                        int y = x + yOffset;
                        int shiftY = 0;
                        int shiftX = 0;
                        y += shiftY;
                        context.fill(-4, x - lineHeight + shiftY, chatWidth + 8, x + shiftY, v << 24);
                        MessageIndicator messageIndicator = visible.indicator();
                        if (messageIndicator != null) {
                           int z = messageIndicator.indicatorColor() | u << 24;
                           context.fill(-4, x - lineHeight + shiftY, -2, x + shiftY, z);
                           if (s == messageIndex && messageIndicator.icon() != null) {
                              int aa = this.getIndicatorX(visible);
                              int ab = y + 9;
                              this.drawIndicatorIcon(context, aa, ab, messageIndicator.icon());
                           }
                        }

                        context.getMatrices().push();
                        context.getMatrices().translate(0.0F, 0.0F, 50.0F);
                        context.drawTextWithShadow(this.client.textRenderer, visible.content(), -shiftX, y, 16777215 + (u << 24));
                        context.getMatrices().pop();
                     }
                  }
               }

               long ac = this.client.getMessageHandler().getUnprocessedMessageCount();
               if (ac > 0L) {
                  int ad = (int)(128.0 * chatOpacity);
                  int t = (int)(255.0 * textBgOpacity);
                  context.getMatrices().push();
                  context.getMatrices().translate(0.0F, startY, 0.0F);
                  context.fill(-2, 0, chatWidth + 4, 9, t << 24);
                  context.getMatrices().translate(0.0F, 0.0F, 50.0F);
                  context.drawTextWithShadow(this.client.textRenderer, Text.translatable("chat.queue", new Object[]{ac}), 0, 1, 16777215 + (ad << 24));
                  context.getMatrices().pop();
               }

               if (focused) {
                  int ad = this.getLineHeight();
                  int t = chatLinesSize * ad;
                  int ae = q * ad;
                  int af = this.scrolledLines * ae / chatLinesSize - startY;
                  int u = ae * ae / t;
                  if (t != ae) {
                     int v = af > 0 ? 170 : 96;
                     int w = this.hasUnreadNewMessages ? 13382451 : 3355562;
                     int x = chatWidth + 4;
                     context.fill(x, -af, x + 2, -af - u, 100, w + (v << 24));
                     context.fill(x + 2, -af, x + 1, -af - u, 100, 13421772 + (v << 24));
                  }
               }

               context.getMatrices().pop();
            }
         }
      }
   }

   @Inject(
      method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V",
      at = @At("HEAD"),
      cancellable = true
   )
   private void addMessageHook(Text message, MessageSignatureData signatureData, MessageIndicator indicator, CallbackInfo ci) {
      if (!this.flag) {
         AddMessageEvent event = this.invoke(new AddMessageEvent(message, signatureData));
         if (event.isCancelled()) {
            ci.cancel();
         } else {
            if (event.isFlag()) {
               ci.cancel();
               this.flag = true;
               this.addMessage(event.getText(), signatureData, indicator);
               this.flag = false;
            }
         }
      }
   }

   @Override
   public void run() {
      List<ChatHudLine> lines = new ArrayList<>(this.messages);
      this.messages.clear();
      this.visibleMessages.clear();
      this.withLogLock(() -> {
         for (ChatHudLine line : lines) {
            this.addMessage(line.content(), line.signature(), line.indicator());
         }
      });
   }

   @Shadow
   protected abstract void drawIndicatorIcon(DrawContext var1, int var2, int var3, Icon var4);

   @Override
   public void delete(MessageSignatureData signature, boolean all) {
      if (signature != null) {
         ListIterator<ChatHudLine> iterator = this.messages.listIterator();
         boolean update = false;

         while (iterator.hasNext()) {
            ChatHudLine line = iterator.next();
            if (signature.equals(line.signature())) {
               this.invoke(new SignedMessage(line.content(), line.signature()));
               iterator.remove();
               update = true;
               if (!all) {
                  break;
               }
            }
         }

         if (update) {
            this.refresh();
         }
      }
   }

   @Override
   public List<ChatHudLine> getMessages() {
      return this.messages;
   }

   @Override
   public void run2() {
      this.scrolledLines = this.prevScrolledLines;
      this.chatAnimation.setLongValue3(this.prevAnimation);
   }

   @Shadow
   protected abstract void addMessage(ChatHudLine var1);

   @Shadow
   protected abstract void logChatMessage(ChatHudLine var1);

   @Shadow
   public abstract int getVisibleLineCount();

   @Shadow
   protected abstract double toChatLineX(double var1);

   @Inject(method = "addMessage(Lnet/minecraft/client/gui/hud/ChatHudLine;)V", at = @At("HEAD"))
   private void addMessageHook(ChatHudLine message, CallbackInfo ci) {
      this.chatAnimation.setLongValue3(System.currentTimeMillis());
   }
}
