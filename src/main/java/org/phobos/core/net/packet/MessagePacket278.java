package org.phobos.core.net.packet;

import io.netty.buffer.ByteBuf;
import org.phobos.api.NetworkPacket;
import org.phobos.core.net.ByteBufUtils;

public class MessagePacket278 implements NetworkPacket {
   public String message;

   public String getMessage() {
      return this.message;
   }

   @Override
   public void handleByteBuf2(ByteBuf byteBuf) {
      ByteBufUtils.handleByteBuf(byteBuf, this.message);
   }

   @Override
   public void handleByteBuf(ByteBuf byteBuf) {
      this.setMessage(ByteBufUtils.createText(byteBuf));
   }

   public MessagePacket278() {
   }

   public void setMessage(String text) {
      this.message = text;
   }

   public MessagePacket278(String text) {
      this.setMessage(text);
   }
}
