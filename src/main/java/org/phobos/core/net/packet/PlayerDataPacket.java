package org.phobos.core.net.packet;

import io.netty.buffer.ByteBuf;
import java.util.UUID;
import org.phobos.api.NetworkPacket;
import org.phobos.core.net.ByteBufUtils;

public class PlayerDataPacket implements NetworkPacket {
   public double doubleValue;
   public float floatValue;
   public String name;
   public UUID uUID;
   public double doubleValue2;
   public float floatValue2;
   public int intValue;
   public String text;
   public int intValue2;
   public double doubleValue3;

   public void setUUID(UUID uUID) {
      this.uUID = uUID;
   }

   public void setFloatValue(float f) {
      this.floatValue = f;
   }

   public PlayerDataPacket() {
   }

   @Override
   public void handleByteBuf(ByteBuf byteBuf) {
      this.setIntValue2(byteBuf.readInt());
      this.setDoubleValue3(byteBuf.readDouble());
      this.setDoubleValue2(byteBuf.readDouble());
      this.setDoubleValue(byteBuf.readDouble());
      this.setName(ByteBufUtils.createText(byteBuf));
      this.setUUID(ByteBufUtils.createUUID(byteBuf));
      this.setText(ByteBufUtils.createText(byteBuf));
      this.setFloatValue2(byteBuf.readFloat());
      this.setIntValue(byteBuf.readInt());
      this.setFloatValue(byteBuf.readFloat());
   }

   public String getText() {
      return this.text;
   }

   public void setName(String text) {
      this.name = text;
   }

   @Override
   public void handleByteBuf2(ByteBuf byteBuf) {
      byteBuf.writeInt(this.intValue2);
      byteBuf.writeDouble(this.doubleValue3);
      byteBuf.writeDouble(this.doubleValue);
      byteBuf.writeDouble(this.doubleValue2);
      ByteBufUtils.handleByteBuf(byteBuf, this.name);
      ByteBufUtils.handleByteBuf10(byteBuf, this.uUID);
      ByteBufUtils.handleByteBuf(byteBuf, this.text);
      byteBuf.writeFloat(this.floatValue2);
      byteBuf.writeInt(this.intValue);
      byteBuf.writeFloat(this.floatValue);
   }

   public int getIntValue() {
      return this.intValue;
   }

   public void setFloatValue2(float f) {
      this.floatValue2 = f;
   }

   public void setDoubleValue2(double d) {
      this.doubleValue = d;
   }

   public void setDoubleValue3(double d) {
      this.doubleValue3 = d;
   }

   public double getDoubleValue3() {
      return this.doubleValue3;
   }

   public double getDoubleValue() {
      return this.doubleValue2;
   }

   public int getIntValue2() {
      return this.intValue2;
   }

   public void setIntValue2(int i) {
      this.intValue2 = i;
   }

   public PlayerDataPacket(int i, double d, double d2, double d3, String text, UUID uUID, String text2, float f, int i2, float f2) {
      this.setIntValue2(i);
      this.setDoubleValue3(d);
      this.setDoubleValue2(d2);
      this.setDoubleValue(d3);
      this.setName(text);
      this.setUUID(uUID);
      this.setText(text2);
      this.setFloatValue2(f);
      this.setIntValue(i2);
      this.setFloatValue(f2);
   }

   public void setIntValue(int i) {
      this.intValue = i;
   }

   public void setText(String text) {
      this.text = text;
   }

   public String getName() {
      return this.name;
   }

   public double getDoubleValue2() {
      return this.doubleValue;
   }

   public float getFloatValue2() {
      return this.floatValue2;
   }

   public void setDoubleValue(double d) {
      this.doubleValue2 = d;
   }

   public float getFloatValue() {
      return this.floatValue;
   }

   public UUID getUUID() {
      return this.uUID;
   }
}
