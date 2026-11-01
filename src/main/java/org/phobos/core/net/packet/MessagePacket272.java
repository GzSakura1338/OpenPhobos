package org.phobos.core.net.packet;

import io.netty.buffer.ByteBuf;
import org.phobos.api.NetworkPacket;
import org.phobos.core.net.ByteBufUtils;

public class MessagePacket272 implements NetworkPacket {
   public String message;
   public String text;
   public String text2;

   public MessagePacket272() {
   }

   public void setText(String text) {
      this.text = text;
   }

   public String getText2() {
      return this.text2;
   }

   public MessagePacket272(String text, String text2, String text3) {
      this.text = text;
      this.text2 = text2;
      this.message = text3;
   }

   @Override
   public void handleByteBuf2(ByteBuf byteBuf) {
      ByteBufUtils.handleByteBuf(byteBuf, this.text);
      ByteBufUtils.handleByteBuf(byteBuf, this.text2);
      ByteBufUtils.handleByteBuf(byteBuf, this.message);
   }

   public void setText2(String text) {
      this.text2 = text;
   }

   public String getMessage() {
      return this.message;
   }

   @Override
   public void handleByteBuf(ByteBuf byteBuf) {
      this.setText(ByteBufUtils.createText(byteBuf));
      this.setText2(ByteBufUtils.createText(byteBuf));
      this.setMessage(ByteBufUtils.createText(byteBuf));
   }

   public String getText() {
      return this.text;
   }

   public void setMessage(String text) {
      this.message = text;
   }
}
