package org.phobos.core.net.packet;

import io.netty.buffer.ByteBuf;
import org.phobos.api.NetworkPacket;
import org.phobos.core.net.ByteBufUtils;

public class TextPairPacket235 implements NetworkPacket {
   public String text;
   public String text2;

   public TextPairPacket235(String text, String text2) {
      this.setText2(text);
      this.setText(text2);
   }

   @Override
   public void handleByteBuf(ByteBuf byteBuf) {
      this.setText2(ByteBufUtils.createText(byteBuf));
      this.setText(ByteBufUtils.createText(byteBuf));
   }

   public void setText(String text) {
      this.text = text;
   }

   public String getText2() {
      return this.text2;
   }

   @Override
   public void handleByteBuf2(ByteBuf byteBuf) {
      ByteBufUtils.handleByteBuf(byteBuf, this.text2);
      ByteBufUtils.handleByteBuf(byteBuf, this.text);
   }

   public TextPairPacket235() {
   }

   public void setText2(String text) {
      this.text2 = text;
   }

   public String getText() {
      return this.text;
   }
}
