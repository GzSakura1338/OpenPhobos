package org.phobos.core.net.packet;

import io.netty.buffer.ByteBuf;
import org.phobos.api.NetworkPacket;
import org.phobos.core.net.ByteBufUtils;

public class ConsoleMessagePacket implements NetworkPacket {
   public String text;

   public ConsoleMessagePacket() {
   }

   @Override
   public void handleByteBuf2(ByteBuf byteBuf) {
      ByteBufUtils.handleByteBuf(byteBuf, this.text);
   }

   public String getText() {
      return this.text;
   }

   public ConsoleMessagePacket(String text) {
      this.setText(text);
   }

   public void setText(String text) {
      this.text = text;
   }

   @Override
   public void handleByteBuf(ByteBuf byteBuf) {
      this.setText(ByteBufUtils.createText(byteBuf));
   }
}
