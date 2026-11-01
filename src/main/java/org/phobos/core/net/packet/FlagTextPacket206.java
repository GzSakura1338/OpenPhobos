package org.phobos.core.net.packet;

import io.netty.buffer.ByteBuf;
import org.phobos.api.NetworkPacket;
import org.phobos.core.net.ByteBufUtils;

public class FlagTextPacket206 implements NetworkPacket {
   public boolean flag;
   public String text;

   @Override
   public void handleByteBuf2(ByteBuf byteBuf) {
      byteBuf.writeBoolean(this.flag);
      ByteBufUtils.handleByteBuf(byteBuf, this.text);
   }

   public void setText(String text) {
      this.text = text;
   }

   @Override
   public void handleByteBuf(ByteBuf byteBuf) {
      this.setFlag(byteBuf.readBoolean());
      this.setText(ByteBufUtils.createText(byteBuf));
   }

   public boolean isFlag() {
      return this.flag;
   }

   public void setFlag(boolean flag) {
      this.flag = flag;
   }

   public FlagTextPacket206(boolean flag, String text) {
      this.flag = flag;
      this.text = text;
   }

   public String getText() {
      return this.text;
   }

   public FlagTextPacket206() {
   }
}
