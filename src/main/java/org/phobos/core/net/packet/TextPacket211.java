package org.phobos.core.net.packet;

import io.netty.buffer.ByteBuf;
import org.phobos.api.NetworkPacket;
import org.phobos.core.net.ByteBufUtils;

public class TextPacket211 implements NetworkPacket {
   public String text;

   @Override
   public void handleByteBuf2(ByteBuf byteBuf) {
      ByteBufUtils.handleByteBuf(byteBuf, this.text);
   }

   public String getText() {
      return this.text;
   }

   public TextPacket211(String text) {
      this.setText(text);
   }

   @Override
   public void handleByteBuf(ByteBuf byteBuf) {
      this.setText(ByteBufUtils.createText(byteBuf));
   }

   public TextPacket211() {
   }

   public void setText(String text) {
      this.text = text;
   }
}
