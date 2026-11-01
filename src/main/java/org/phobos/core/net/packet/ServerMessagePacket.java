package org.phobos.core.net.packet;

import io.netty.buffer.ByteBuf;
import org.phobos.api.NetworkPacket;
import org.phobos.core.net.ByteBufUtils;

public class ServerMessagePacket implements NetworkPacket {
   public String text;
   public String text2;
   public int intValue;

   public ServerMessagePacket() {
   }

   public void setIntValue(int i) {
      this.intValue = i;
   }

   @Override
   public void handleByteBuf(ByteBuf byteBuf) {
      this.setText2(ByteBufUtils.createText(byteBuf));
      this.setText(ByteBufUtils.createText(byteBuf));
      this.setIntValue(byteBuf.readInt());
   }

   public String getText() {
      return this.text;
   }

   public String getText2() {
      return this.text2;
   }

   public void setText2(String text) {
      this.text2 = text;
   }

   public int getIntValue() {
      return this.intValue;
   }

   public ServerMessagePacket(String text, String text2, int i) {
      this.text2 = text;
      this.text = text2;
      this.intValue = i;
   }

   @Override
   public void handleByteBuf2(ByteBuf byteBuf) {
      ByteBufUtils.handleByteBuf(byteBuf, this.text2);
      ByteBufUtils.handleByteBuf(byteBuf, this.text);
      byteBuf.writeInt(this.intValue);
   }

   public void setText(String text) {
      this.text = text;
   }
}
