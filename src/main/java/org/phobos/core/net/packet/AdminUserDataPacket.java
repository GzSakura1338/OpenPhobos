package org.phobos.core.net.packet;

import io.netty.buffer.ByteBuf;
import org.phobos.api.NetworkPacket;
import org.phobos.core.net.ByteBufUtils;

public class AdminUserDataPacket implements NetworkPacket {
   public String text;
   public String text2;
   public long longValue;
   public long longValue2;
   public long longValue3;
   public String text3;

   public void setLongValue3(long l) {
      this.longValue3 = l;
   }

   public void setText23(String text) {
      this.text2 = text;
   }

   public long getLongValue2() {
      return this.longValue2;
   }

   public AdminUserDataPacket() {
   }

   public void setText2(String text) {
      this.text3 = text;
   }

   public String getText2() {
      return this.text3;
   }

   public String getText23() {
      return this.text2;
   }

   @Override
   public void handleByteBuf(ByteBuf byteBuf) {
      this.setText2(ByteBufUtils.createText(byteBuf));
      this.setText22(ByteBufUtils.createText(byteBuf));
      this.setText23(ByteBufUtils.createText(byteBuf));
      this.setLongValue(byteBuf.readLong());
      this.setLongValue3(byteBuf.readLong());
      this.setLongValue2(byteBuf.readLong());
   }

   public void setLongValue(long l) {
      this.longValue = l;
   }

   @Override
   public void handleByteBuf2(ByteBuf byteBuf) {
      ByteBufUtils.handleByteBuf(byteBuf, this.text3);
      ByteBufUtils.handleByteBuf(byteBuf, this.text);
      ByteBufUtils.handleByteBuf(byteBuf, this.text2);
      byteBuf.writeLong(this.longValue);
      byteBuf.writeLong(this.longValue3);
      byteBuf.writeLong(this.longValue2);
   }

   public String getText22() {
      return this.text;
   }

   public void setLongValue2(long l) {
      this.longValue2 = l;
   }

   public AdminUserDataPacket(String text, String text2, String text3, long l, long l2, long l3) {
      this.setText2(text);
      this.setText22(text2);
      this.setText23(text3);
      this.setLongValue(l);
      this.setLongValue3(l2);
      this.setLongValue2(l3);
   }

   public long getLongValue3() {
      return this.longValue3;
   }

   public void setText22(String text) {
      this.text = text;
   }

   public long getLongValue() {
      return this.longValue;
   }
}
