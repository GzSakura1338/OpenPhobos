package org.phobos.core.net.packet;

import io.netty.buffer.ByteBuf;
import org.phobos.api.NetworkPacket;
import org.phobos.core.net.ByteBufUtils;

public class ByteTripletPacket240 implements NetworkPacket {
   public String text;
   public byte[] byteArray;
   public byte byteValue;
   public byte byteValue2;
   public byte byteValue3;

   public void setByteValue2(byte b) {
      this.byteValue2 = b;
   }

   public ByteTripletPacket240() {
   }

   public String getText() {
      return this.text;
   }

   public void setByteValue(byte b) {
      this.byteValue = b;
   }

   @Override
   public void handleByteBuf(ByteBuf byteBuf) {
      this.setByteValue(byteBuf.readByte());
      this.setByteValue2(byteBuf.readByte());
      this.setByteValue3(byteBuf.readByte());
      this.setByteArray(ByteBufUtils.values(byteBuf));
      this.setText(ByteBufUtils.createText(byteBuf));
   }

   public ByteTripletPacket240(byte b, byte b2, byte b3, byte[] bytes, String text) {
      this.byteValue = b;
      this.byteValue2 = b2;
      this.byteValue3 = b3;
      this.byteArray = bytes;
      this.text = text;
   }

   public byte getByteValue() {
      return this.byteValue;
   }

   public byte getByteValue3() {
      return this.byteValue3;
   }

   public byte getByteValue2() {
      return this.byteValue2;
   }

   public void setByteArray(byte[] bytes) {
      this.byteArray = bytes;
   }

   public void setByteValue3(byte b) {
      this.byteValue3 = b;
   }

   public byte[] getByteArray() {
      return this.byteArray;
   }

   @Override
   public void handleByteBuf2(ByteBuf byteBuf) {
      byteBuf.writeByte(this.byteValue);
      byteBuf.writeByte(this.byteValue2);
      byteBuf.writeByte(this.byteValue3);
      ByteBufUtils.handleByteBuf7(byteBuf, this.byteArray);
      ByteBufUtils.handleByteBuf(byteBuf, this.text);
   }

   public void setText(String text) {
      this.text = text;
   }
}
