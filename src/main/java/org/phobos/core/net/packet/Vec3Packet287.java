package org.phobos.core.net.packet;

import io.netty.buffer.ByteBuf;
import org.phobos.api.NetworkPacket;

public class Vec3Packet287 implements NetworkPacket {
   public double doubleValue;
   public double doubleValue2;
   public double doubleValue3;

   @Override
   public void handleByteBuf2(ByteBuf byteBuf) {
      byteBuf.writeDouble(this.doubleValue);
      byteBuf.writeDouble(this.doubleValue3);
      byteBuf.writeDouble(this.doubleValue2);
   }

   public Vec3Packet287() {
   }

   public void setDoubleValue3(double d) {
      this.doubleValue = d;
   }

   @Override
   public void handleByteBuf(ByteBuf byteBuf) {
      this.setDoubleValue3(byteBuf.readDouble());
      this.setDoubleValue2(byteBuf.readDouble());
      this.setDoubleValue(byteBuf.readDouble());
   }

   public void setDoubleValue(double d) {
      this.doubleValue2 = d;
   }

   public Vec3Packet287(double d, double d2, double d3) {
      this.setDoubleValue3(d);
      this.setDoubleValue2(d2);
      this.setDoubleValue(d3);
   }

   public void setDoubleValue2(double d) {
      this.doubleValue3 = d;
   }

   public double getDoubleValue3() {
      return this.doubleValue;
   }

   public double getDoubleValue() {
      return this.doubleValue2;
   }

   public double getDoubleValue2() {
      return this.doubleValue3;
   }
}
