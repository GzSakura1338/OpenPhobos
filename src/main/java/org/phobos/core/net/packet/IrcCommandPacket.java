package org.phobos.core.net.packet;

import io.netty.buffer.ByteBuf;
import org.phobos.api.NetworkPacket;
import org.phobos.core.net.ByteBufUtils;

public class IrcCommandPacket implements NetworkPacket {
   public int intValue;
   public boolean flag;
   public String text;
   public String text2;

   @Override
   public void handleByteBuf(ByteBuf byteBuf) {
      this.setText(ByteBufUtils.createText(byteBuf));
      this.setText2(ByteBufUtils.createText(byteBuf));
      this.setFlag(byteBuf.readBoolean());
      this.setIntValue(byteBuf.readInt());
   }

   public boolean isFlag() {
      return this.flag;
   }

   public String getText() {
      return this.text;
   }

   @Override
   public void handleByteBuf2(ByteBuf byteBuf) {
      ByteBufUtils.handleByteBuf(byteBuf, this.text);
      ByteBufUtils.handleByteBuf(byteBuf, this.text2);
      byteBuf.writeBoolean(this.flag);
      byteBuf.writeInt(this.intValue);
   }

   public String getText2() {
      return this.text2;
   }

   public void setText(String text) {
      this.text = text;
   }

   public void setIntValue(int i) {
      this.intValue = i;
   }

   public void setText2(String text) {
      this.text2 = text;
   }

   public int getIntValue() {
      return this.intValue;
   }

   public IrcCommandPacket() {
   }

   public void setFlag(boolean flag) {
      this.flag = flag;
   }

   public IrcCommandPacket(String text, String text2, boolean flag, int i) {
      this.text = text;
      this.text2 = text2;
      this.flag = flag;
      this.intValue = i;
   }
}
