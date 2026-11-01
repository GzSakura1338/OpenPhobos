package org.phobos.core.net.packet;

import io.netty.buffer.ByteBuf;
import org.phobos.api.NetworkPacket;

public class IntFloatPacket320 implements NetworkPacket {
   public int intValue;
   public float floatValue;

   public void setIntValue2(int i) {
      this.intValue = i;
   }

   @Override
   public void handleByteBuf(ByteBuf byteBuf) {
      this.setFloatValue2(byteBuf.readFloat());
      this.setIntValue2(byteBuf.readInt());
   }

   public float getFloatValue2() {
      return this.floatValue;
   }

   public void setFloatValue2(float f) {
      this.floatValue = f;
   }

   public IntFloatPacket320(float f, int i) {
      this.floatValue = f;
      this.intValue = i;
   }

   @Override
   public void handleByteBuf2(ByteBuf byteBuf) {
      byteBuf.writeFloat(this.floatValue);
      byteBuf.writeInt(this.intValue);
   }

   public int getIntValue2() {
      return this.intValue;
   }

   public IntFloatPacket320() {
   }
}
