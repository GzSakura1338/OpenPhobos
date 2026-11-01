package org.phobos.core.net.packet;

import io.netty.buffer.ByteBuf;
import org.phobos.api.NetworkPacket;
import org.phobos.core.net.ByteBufUtils;

public class ExportPacket implements NetworkPacket {
   public String text;
   public String text2;

   @Override
   public void handleByteBuf2(ByteBuf byteBuf) {
      ByteBufUtils.handleByteBuf(byteBuf, this.text2);
      ByteBufUtils.handleByteBuf(byteBuf, this.text);
   }

   public void setText(String text) {
      this.text2 = text;
   }

   @Override
   public void handleByteBuf(ByteBuf byteBuf) {
      this.setText(ByteBufUtils.createText(byteBuf));
      this.setText3(ByteBufUtils.createText(byteBuf));
   }

   public String getText3() {
      return this.text;
   }

   public String getText() {
      return this.text2;
   }

   public ExportPacket(String text, String text2) {
      this.text2 = text;
      this.text = text2;
   }

   public void setText3(String text) {
      this.text = text;
   }

   public ExportPacket() {
   }
}
