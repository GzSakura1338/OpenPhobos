package org.phobos.core.net.packet;

import io.netty.buffer.ByteBuf;
import org.phobos.api.NetworkPacket;
import org.phobos.core.net.ByteBufUtils;

public class FlagTextPairPacket236 implements NetworkPacket {
   public boolean flag;
   public String text;
   public String text2;

   @Override
   public void handleByteBuf2(ByteBuf byteBuf) {
      ByteBufUtils.handleByteBuf8(byteBuf, this.text);
      ByteBufUtils.handleByteBuf(byteBuf, this.text2);
      byteBuf.writeBoolean(this.flag);
   }

   public void setText(String text) {
      this.text = text;
   }

   public String getText2() {
      return this.text2;
   }

   public void setText2(String text) {
      this.text2 = text;
   }

   public FlagTextPairPacket236() {
   }

   @Override
   public void handleByteBuf(ByteBuf byteBuf) {
      this.setText(ByteBufUtils.describeByteBuf(byteBuf));
      this.setText2(ByteBufUtils.createText(byteBuf));
      this.setFlag(byteBuf.readBoolean());
   }

   public void setFlag(boolean flag) {
      this.flag = flag;
   }

   public boolean isFlag() {
      return this.flag;
   }

   public FlagTextPairPacket236(String text, String text2, boolean flag) {
      this.setText(text);
      this.setText2(text2);
      this.setFlag(flag);
   }

   public String getText() {
      return this.text;
   }
}
