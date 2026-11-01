package org.phobos.core.net.packet;

import io.netty.buffer.ByteBuf;
import org.phobos.api.NetworkPacket;
import org.phobos.core.net.ByteBufUtils;

public class FlagTextPacket253 implements NetworkPacket {
   public String text;
   public boolean flag;

   @Override
   public void handleByteBuf2(ByteBuf byteBuf) {
      ByteBufUtils.handleByteBuf(byteBuf, this.text);
      byteBuf.writeBoolean(this.flag);
   }

   public void setText2(String text) {
      this.text = text;
   }

   public FlagTextPacket253() {
   }

   public String getText2() {
      return this.text;
   }

   public void setFlag(boolean flag) {
      this.flag = flag;
   }

   @Override
   public void handleByteBuf(ByteBuf byteBuf) {
      this.setText2(ByteBufUtils.createText(byteBuf));
      this.setFlag(byteBuf.readBoolean());
   }

   public FlagTextPacket253(String text, boolean flag) {
      this.setText2(text);
      this.setFlag(flag);
   }

   public boolean isFlag() {
      return this.flag;
   }
}
