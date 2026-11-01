package org.phobos.core.net.packet;

import io.netty.buffer.ByteBuf;
import org.phobos.api.NetworkPacket;
import org.phobos.core.net.ByteBufUtils;

public class MessagePacket239 implements NetworkPacket {
   public String message;
   public String text;

   public String getMessage() {
      return this.message;
   }

   @Override
   public void handleByteBuf(ByteBuf byteBuf) {
      this.setText(ByteBufUtils.createText(byteBuf));
      this.setMessage(ByteBufUtils.createText(byteBuf));
   }

   public MessagePacket239() {
   }

   public void setMessage(String text) {
      this.message = text;
   }

   public MessagePacket239(String text, String text2) {
      this.text = text;
      this.message = text2;
   }

   public void setText(String text) {
      this.text = text;
   }

   @Override
   public void handleByteBuf2(ByteBuf byteBuf) {
      ByteBufUtils.handleByteBuf(byteBuf, this.text);
      ByteBufUtils.handleByteBuf(byteBuf, this.message);
   }

   public String getText() {
      return this.text;
   }
}
