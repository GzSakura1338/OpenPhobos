package org.phobos.core.net.packet;

import io.netty.buffer.ByteBuf;
import org.phobos.api.NetworkPacket;
import org.phobos.core.net.ByteBufUtils;

public class SubscriptionPacket implements NetworkPacket {
   public long longValue;
   public String text;
   public SubscriptionPacket.SubscriptionAction subscriptionAction;

   public String getText() {
      return this.text;
   }

   @Override
   public void handleByteBuf(ByteBuf byteBuf) {
      this.setText(ByteBufUtils.createText(byteBuf));
      this.setSubscriptionAction(SubscriptionPacket.SubscriptionAction.fromString(ByteBufUtils.createText(byteBuf)));
      this.setLongValue(byteBuf.readLong());
   }

   public void setLongValue(long l) {
      this.longValue = l;
   }

   public void setText(String text) {
      this.text = text;
   }

   public long getLongValue() {
      return this.longValue;
   }

   public SubscriptionPacket(String text, SubscriptionPacket.SubscriptionAction subscriptionAction, long l) {
      this.setText(text);
      this.setSubscriptionAction(subscriptionAction);
      this.setLongValue(l);
   }

   public SubscriptionPacket() {
   }

   public SubscriptionPacket.SubscriptionAction getSubscriptionAction() {
      return this.subscriptionAction;
   }

   public void setSubscriptionAction(SubscriptionPacket.SubscriptionAction subscriptionAction) {
      this.subscriptionAction = subscriptionAction;
   }

   @Override
   public void handleByteBuf2(ByteBuf byteBuf) {
      ByteBufUtils.handleByteBuf(byteBuf, this.text);
      ByteBufUtils.handleByteBuf(byteBuf, this.subscriptionAction.toString());
      byteBuf.writeLong(this.longValue);
   }

   public enum SubscriptionAction {
      Set("SET"),
      Plus("PLUS"),
      Minus("MINUS");

      public final String message;

      @Override
      public String toString() {
         return this.message;
      }

      public static SubscriptionPacket.SubscriptionAction fromString(String message) {
         return switch (message) {
            case "SET" -> Set;
            case "PLUS" -> Plus;
            case "MINUS" -> Minus;
            default -> null;
         };
      }

      SubscriptionAction(String message) {
         this.message = message;
      }
   }
}
