package org.phobos.core.net.packet;

import io.netty.buffer.ByteBuf;
import org.phobos.api.NetworkPacket;
import org.phobos.core.net.ByteBufUtils;

public class TextPacket225 implements NetworkPacket {
   public String text;

   public void setText3(String text) {
      this.text = text;
   }

   @Override
   public void handleByteBuf(ByteBuf byteBuf) {
      this.setText3(ByteBufUtils.createText(byteBuf));
   }

   @Override
   public void handleByteBuf2(ByteBuf byteBuf) {
      ByteBufUtils.handleByteBuf(byteBuf, this.text);
   }

   public TextPacket225() {
   }

   public String getText3() {
      return this.text;
   }

   public TextPacket225(String text) {
      this.setText3(text);
   }
}
