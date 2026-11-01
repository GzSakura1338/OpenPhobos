package org.phobos.core.net.packet;

import io.netty.buffer.ByteBuf;
import org.phobos.api.NetworkPacket;
import org.phobos.core.net.ByteBufUtils;

public class SkinPacket implements NetworkPacket {
   public String text;
   public String text2;

   public void setText(String text) {
      this.text = text;
   }

   @Override
   public void handleByteBuf(ByteBuf byteBuf) {
      this.setText(ByteBufUtils.createText(byteBuf));
      this.setText22(ByteBufUtils.createText(byteBuf));
   }

   public SkinPacket(String text, String text2) {
      this.setText(text);
      this.setText22(text2);
   }

   public String getText22() {
      return this.text2;
   }

   public String getText() {
      return this.text;
   }

   public void setText22(String text) {
      this.text2 = text;
   }

   @Override
   public void handleByteBuf2(ByteBuf byteBuf) {
      ByteBufUtils.handleByteBuf(byteBuf, this.text);
      ByteBufUtils.handleByteBuf(byteBuf, this.text2);
   }

   public SkinPacket() {
   }
}
