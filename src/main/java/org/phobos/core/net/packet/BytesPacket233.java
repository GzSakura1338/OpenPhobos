package org.phobos.core.net.packet;

import io.netty.buffer.ByteBuf;
import org.phobos.api.NetworkPacket;

public class BytesPacket233 implements NetworkPacket {
   public double doubleValue;
   public double doubleValue2;
   public byte[] byteArray;
   public double doubleValue3;
   public String text;
   public long longValue;

   public double getDoubleValue3() {
      return this.doubleValue;
   }

   public byte[] getByteArray() {
      return this.byteArray;
   }

   public void setLongValue(long l) {
      this.longValue = l;
   }

   public void setText2(String text) {
      this.text = text;
   }

   @Override
   public void handleByteBuf(ByteBuf byteBuf) {
      this.longValue = byteBuf.readLong();
      this.byteArray = new byte[byteBuf.readInt()];
      byteBuf.readBytes(this.byteArray);
   }

   public double getDoubleValue() {
      return this.doubleValue2;
   }

   public void setDoubleValue3(double d) {
      this.doubleValue = d;
   }

   public BytesPacket233() {
   }

   public void setDoubleValue2(double d) {
      this.doubleValue3 = d;
   }

   public double getDoubleValue2() {
      return this.doubleValue3;
   }

   public void setByteArray(byte[] bytes) {
      this.byteArray = bytes;
   }

   public String getText2() {
      return this.text;
   }

   @Override
   public void handleByteBuf2(ByteBuf byteBuf) {
      byteBuf.writeLong(this.longValue);
      byteBuf.writeInt(this.byteArray.length);
      byteBuf.writeBytes(this.byteArray);
   }

   public long getLongValue() {
      return this.longValue;
   }

   public void setDoubleValue(double d) {
      this.doubleValue2 = d;
   }

   public BytesPacket233(String text, double d, double d2, double d3, long l, byte[] bytes) {
      this.text = text;
      this.doubleValue = d;
      this.doubleValue3 = d2;
      this.doubleValue2 = d3;
      this.longValue = l;
      this.byteArray = bytes;
   }
}
