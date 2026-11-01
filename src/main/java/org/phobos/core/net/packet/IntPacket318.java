package org.phobos.core.net.packet;

import io.netty.buffer.ByteBuf;
import org.phobos.api.NetworkPacket;

public class IntPacket318 implements NetworkPacket {
   public int intValue;

   public IntPacket318() {
   }

   public void setIntValue(int i) {
      this.intValue = i;
   }

   @Override
   public void handleByteBuf2(ByteBuf byteBuf) {
      byteBuf.writeInt(this.intValue);
   }

   @Override
   public void handleByteBuf(ByteBuf byteBuf) {
      this.setIntValue(byteBuf.readInt());
   }

   public IntPacket318(int i) {
      this.setIntValue(i);
   }

   public int getIntValue() {
      return this.intValue;
   }
}
