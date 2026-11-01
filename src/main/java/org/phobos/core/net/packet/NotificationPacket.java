package org.phobos.core.net.packet;

import io.netty.buffer.ByteBuf;
import org.phobos.api.NetworkPacket;
import org.phobos.core.net.ByteBufUtils;

public class NotificationPacket implements NetworkPacket {
   public String message;

   public void setMessage(String text) {
      this.message = text;
   }

   @Override
   public void handleByteBuf2(ByteBuf byteBuf) {
      ByteBufUtils.handleByteBuf(byteBuf, this.message);
   }

   public NotificationPacket(String text) {
      this.setMessage(text);
   }

   @Override
   public void handleByteBuf(ByteBuf byteBuf) {
      this.setMessage(ByteBufUtils.createText(byteBuf));
   }

   public NotificationPacket() {
   }

   public String getMessage() {
      return this.message;
   }
}
