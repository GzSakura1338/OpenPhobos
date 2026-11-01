package org.phobos.core.net.packet;

import io.netty.buffer.ByteBuf;
import org.phobos.api.NetworkPacket;
import org.phobos.core.net.ByteBufUtils;

public class TextBytesPacket273 implements NetworkPacket {
   public String text;
   public byte[] byteArray;

   @Override
   public void handleByteBuf(ByteBuf byteBuf) {
      byte[] bytes = new byte[64];
      byteBuf.readBytes(bytes);
      this.setByteArray(bytes);
      this.setText(ByteBufUtils.createText(byteBuf));
   }

   public void setByteArray(byte[] bytes) {
      this.byteArray = bytes;
   }

   public void setText(String text) {
      this.text = text;
   }

   @Override
   public void handleByteBuf2(ByteBuf byteBuf) {
      byteBuf.writeBytes(this.byteArray);
      ByteBufUtils.handleByteBuf(byteBuf, this.text);
   }

   public TextBytesPacket273(byte[] bytes, String text) {
      this.setByteArray(bytes);
      this.setText(text);
   }

   public TextBytesPacket273() {
   }

   public byte[] getByteArray() {
      return this.byteArray;
   }

   public String getText() {
      return this.text;
   }
}
