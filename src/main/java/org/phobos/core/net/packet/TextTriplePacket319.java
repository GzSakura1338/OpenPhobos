package org.phobos.core.net.packet;

import io.netty.buffer.ByteBuf;
import org.phobos.api.NetworkPacket;
import org.phobos.core.net.ByteBufUtils;

public class TextTriplePacket319 implements NetworkPacket {
   public String text;
   public String text2;
   public String text3;

   @Override
   public void handleByteBuf(ByteBuf byteBuf) {
      this.setText2(ByteBufUtils.createText(byteBuf));
      this.setText22(ByteBufUtils.createText(byteBuf));
      this.setText(ByteBufUtils.createText(byteBuf));
   }

   public void setText(String text) {
      this.text3 = text;
   }

   public String getText22() {
      return this.text;
   }

   public TextTriplePacket319(String text, String text2, String text3) {
      this.setText2(text);
      this.setText22(text2);
      this.setText(text3);
   }

   public String getText() {
      return this.text3;
   }

   public String getText2() {
      return this.text2;
   }

   @Override
   public void handleByteBuf2(ByteBuf byteBuf) {
      ByteBufUtils.handleByteBuf(byteBuf, this.text2);
      ByteBufUtils.handleByteBuf(byteBuf, this.text);
      ByteBufUtils.handleByteBuf(byteBuf, this.text3);
   }

   public void setText22(String text) {
      this.text = text;
   }

   public void setText2(String text) {
      this.text2 = text;
   }

   public TextTriplePacket319() {
   }
}
