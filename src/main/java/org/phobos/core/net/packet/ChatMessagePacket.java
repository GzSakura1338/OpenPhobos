package org.phobos.core.net.packet;

import io.netty.buffer.ByteBuf;
import org.phobos.api.NetworkPacket;
import org.phobos.core.net.ByteBufUtils;

public class ChatMessagePacket implements NetworkPacket {
   public String text;
   public String message;
   public String text2;

   public void setText(String text) {
      this.text = text;
   }

   public void setMessage(String text) {
      this.message = text;
   }

   public void setText2(String text) {
      this.text2 = text;
   }

   public String getText2() {
      return this.text2;
   }

   public ChatMessagePacket() {
   }

   @Override
   public void handleByteBuf2(ByteBuf byteBuf) {
      ByteBufUtils.handleByteBuf(byteBuf, this.message);
      ByteBufUtils.handleByteBuf(byteBuf, this.text2);
      ByteBufUtils.handleByteBuf(byteBuf, this.text);
   }

   public String getText() {
      return this.text;
   }

   public ChatMessagePacket(String text, String text2, String text3) {
      this.setMessage(text);
      this.setText2(text2);
      this.setText(text3);
   }

   @Override
   public void handleByteBuf(ByteBuf byteBuf) {
      this.setMessage(ByteBufUtils.createText(byteBuf));
      this.setText2(ByteBufUtils.createText(byteBuf));
      this.setText(ByteBufUtils.createText(byteBuf));
   }

   public String getMessage() {
      return this.message;
   }
}
