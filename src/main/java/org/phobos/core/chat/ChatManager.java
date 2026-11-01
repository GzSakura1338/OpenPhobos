package org.phobos.core.chat;

import java.awt.Color;
import java.nio.charset.StandardCharsets;
import java.util.ConcurrentModificationException;
import java.util.List;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.ClickEvent.Action;
import net.minecraft.util.Formatting;
import org.earthhack.ducks.IChat;
import org.earthhack.ducks.IClickEvent;
import org.jetbrains.annotations.Nullable;
import org.phobos.core.event.events.PacketEvent.Receive;
import org.phobos.event.SafeListener;
import org.phobos.module.client.NotificationsModule;
import org.phobos.core.Feature;
import org.phobos.core.PhobosClient;

public class ChatManager extends Feature {
   public void handleText(String text) {
      this.handleText5(Text.literal(text));
   }

   public void handleText2(String text) {
      this.handleText5(Text.literal(text).withColor(Color.RED.hashCode()));
   }

   public void handleChatHud(ChatHud chatHud, Text text, String text2) {
      try {
         MessageSignatureData messageSignatureData = this.createMessageSignatureData(text2);
         this.handleMessageSignatureData(messageSignatureData);
         chatHud.addMessage(text, messageSignatureData, MessageIndicator.system());
      } catch (ConcurrentModificationException var5) {
      }
   }

   public void handleText3(String text) {
      this.handleText5(Text.literal(text).withColor(Color.GREEN.hashCode()));
   }

   public void handleText4(String text, String text2) {
      this.handleText9(Text.literal(text), text2);
   }

   public void handleText5(Text text) {
      mc.inGameHud.getChatHud().addMessage(text);
   }

   public void handleText6(String text, Runnable runnable, Runnable runnable2, String text2, String text3) {
      NotificationsModule.getNotificationsModule()
         .handleTriConsumer(
            (color, color2, color3) -> NotificationsModule.getNotificationsModule()
               .handleText5(
                  Text.literal(text)
                     .withColor(color2.hashCode())
                     .append(Text.literal(" (").formatted(Formatting.RESET).append(Text.literal(text2).styled(style -> {
                        Style style2 = Style.EMPTY.withFormatting(Formatting.GREEN);
                        ClickEvent clickEvent = new ClickEvent(Action.OPEN_FILE, "yes");
                        IClickEvent.cast(clickEvent).addCallback(runnable);
                        return style2.withClickEvent(clickEvent);
                     })).append(Text.literal("/")).append(Text.literal(text3).styled(style -> {
                        Style style2 = Style.EMPTY.withFormatting(Formatting.RED);
                        ClickEvent clickEvent = new ClickEvent(Action.OPEN_FILE, "no");
                        IClickEvent.cast(clickEvent).addCallback(runnable2);
                        return style2.withClickEvent(clickEvent);
                     })).append(")"))
               )
         );
   }

   public List<ChatHudLine> getMessages() {
      return ((IChat)(Object)mc.inGameHud.getChatHud()).getMessages();
   }

   public void handleText7(String text, Runnable runnable) {
      this.handleText8(text, runnable, () -> {});
   }

   public void handleText8(String text, Runnable runnable, Runnable runnable2) {
      this.handleText6(text, runnable, runnable2, "Yes", "No");
   }

   public void handleText9(Text text, String text2) {
      this.handleChatHud(mc.inGameHud.getChatHud(), text, text2);
   }

   public void handleText10(String text) {
      this.handleMessageSignatureData(this.createMessageSignatureData(text));
   }

   public void handleMessageSignatureData(MessageSignatureData messageSignatureData) {
      ((IChat)(Object)mc.inGameHud.getChatHud()).delete(messageSignatureData, true);
   }

   public void handleObject(@Nullable Object object) {
      this.handleText(object == null ? "null" : object.toString());
   }

   public MessageSignatureData createMessageSignatureData(String text) {
      if (text == null) {
         return null;
      }

      byte[] bytes = new byte[256];
      byte[] bytes2 = text.getBytes(StandardCharsets.UTF_8);
      System.arraycopy(bytes2, 0, bytes, 0, Math.min(bytes.length, bytes2.length));
      return new MessageSignatureData(bytes);
   }

   public void handleText11(String text) {
      this.handleText5(Text.literal(text).withColor(Color.YELLOW.hashCode()));
   }

   public ChatManager(PhobosClient phobosClient) {
      this.handleListener(
         new SafeListener<Receive<GameMessageS2CPacket>>() {
            public void handleObject(
               Receive<GameMessageS2CPacket> receive,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               String text = ((GameMessageS2CPacket)receive.getPacket()).content().getString();
               if (text.startsWith("<")) {
                  int i = text.indexOf(62);
                  if (i != -1) {
                     String text2 = ChatTextUtils.describeText4(text.substring(1, i));
                     String text3 = ChatTextUtils.describeText4(text.substring(i + 1).trim());
                     phobosClient.getEventBus().handleObject2(new StringPair2(text2, text3));
                  }
               }
            }
         }
      );
   }
}
