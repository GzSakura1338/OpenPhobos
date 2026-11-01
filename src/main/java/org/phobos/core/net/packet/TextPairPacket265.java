package org.phobos.core.net.packet;

import io.netty.buffer.ByteBuf;
import org.phobos.api.NetworkPacket;
import org.phobos.core.net.ByteBufUtils;

public class TextPairPacket265 implements NetworkPacket {
   public String text;
   public String text2;

   public void setText(String text) {
      this.text = text;
   }

   public String getText() {
      return this.text;
   }

   public TextPairPacket265(String text, String text2) {
      this.text = text;
      this.text2 = text2;
   }

   @Override
   public void handleByteBuf2(ByteBuf byteBuf) {
      ByteBufUtils.handleByteBuf(byteBuf, this.text);
      ByteBufUtils.handleByteBuf(byteBuf, this.text2);
   }

   public void setText2(String text) {
      this.text2 = text;
   }

   @Override
   public void handleByteBuf(ByteBuf byteBuf) {
      this.setText(ByteBufUtils.createText(byteBuf));
      this.setText2(ByteBufUtils.createText(byteBuf));
   }

   public String getText2() {
      return this.text2;
   }

   public TextPairPacket265() {
   }
}
