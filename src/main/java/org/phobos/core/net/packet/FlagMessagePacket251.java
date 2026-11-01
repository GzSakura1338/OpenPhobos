package org.phobos.core.net.packet;

import io.netty.buffer.ByteBuf;
import org.phobos.api.NetworkPacket;
import org.phobos.core.net.ByteBufUtils;

public class FlagMessagePacket251 implements NetworkPacket {
   public boolean flag;
   public String message;
   public String text;

   public FlagMessagePacket251() {
   }

   public boolean isFlag() {
      return this.flag;
   }

   @Override
   public void handleByteBuf2(ByteBuf byteBuf) {
      ByteBufUtils.handleByteBuf(byteBuf, this.text);
      ByteBufUtils.handleByteBuf(byteBuf, this.message);
      byteBuf.writeBoolean(this.flag);
   }

   public String getMessage() {
      return this.message;
   }

   public FlagMessagePacket251(String text, String text2, boolean flag) {
      this.text = text;
      this.message = text2;
      this.flag = flag;
   }

   @Override
   public void handleByteBuf(ByteBuf byteBuf) {
      this.setText2(ByteBufUtils.createText(byteBuf));
      this.setMessage(ByteBufUtils.createText(byteBuf));
      this.setFlag(byteBuf.readBoolean());
   }

   public void setFlag(boolean flag) {
      this.flag = flag;
   }

   public String getText2() {
      return this.text;
   }

   public void setMessage(String text) {
      this.message = text;
   }

   public void setText2(String text) {
      this.text = text;
   }
}
