package org.phobos.core.net.packet;

import io.netty.buffer.ByteBuf;
import org.phobos.api.NetworkPacket;
import org.phobos.core.net.ByteBufUtils;

public class SharedEspPacket implements NetworkPacket {
   public String text;
   public String text2;
   public String name;
   public double doubleValue;
   public double doubleValue2;
   public double doubleValue3;

   public void setDoubleValue2(double d) {
      this.doubleValue2 = d;
   }

   public void setDoubleValue3(double d) {
      this.doubleValue3 = d;
   }

   public double getDoubleValue2() {
      return this.doubleValue2;
   }

   public void setText2(String text) {
      this.text2 = text;
   }

   public void setDoubleValue(double d) {
      this.doubleValue = d;
   }

   @Override
   public void handleByteBuf(ByteBuf byteBuf) {
      this.setText2(ByteBufUtils.createText(byteBuf));
      this.setName(ByteBufUtils.createText(byteBuf));
      this.setText(ByteBufUtils.createText(byteBuf));
      this.setDoubleValue3(byteBuf.readDouble());
      this.setDoubleValue2(byteBuf.readDouble());
      this.setDoubleValue(byteBuf.readDouble());
   }

   public double getDoubleValue() {
      return this.doubleValue;
   }

   public SharedEspPacket() {
   }

   public void setText(String text) {
      this.text = text;
   }

   public String getText2() {
      return this.text2;
   }

   @Override
   public void handleByteBuf2(ByteBuf byteBuf) {
      ByteBufUtils.handleByteBuf(byteBuf, this.text2);
      ByteBufUtils.handleByteBuf(byteBuf, this.name);
      ByteBufUtils.handleByteBuf(byteBuf, this.text);
      byteBuf.writeDouble(this.doubleValue3);
      byteBuf.writeDouble(this.doubleValue2);
      byteBuf.writeDouble(this.doubleValue);
   }

   public void setName(String text) {
      this.name = text;
   }

   public String getText() {
      return this.text;
   }

   public double getDoubleValue3() {
      return this.doubleValue3;
   }

   public SharedEspPacket(String text, String text2, String text3, double d, double d2, double d3) {
      this.text2 = text;
      this.name = text2;
      this.text = text3;
      this.doubleValue3 = d;
      this.doubleValue2 = d2;
      this.doubleValue = d3;
   }

   public String getName() {
      return this.name;
   }
}
