package org.phobos.core.net.packet;

import io.netty.buffer.ByteBuf;
import org.phobos.api.NetworkPacket;
import org.phobos.core.net.ByteBufUtils;

public class FourTextPacket196 implements NetworkPacket {
   public String text;
   public String text2;
   public String text3;
   public String text4;

   public void setText(String text) {
      this.text = text;
   }

   public FourTextPacket196() {
   }

   public FourTextPacket196(String text, String text2, String text3, String text4) {
      this.text4 = text;
      this.text = text2;
      this.text3 = text3;
      this.text2 = text4;
   }

   @Override
   public void handleByteBuf2(ByteBuf byteBuf) {
      ByteBufUtils.handleByteBuf(byteBuf, this.text4);
      ByteBufUtils.handleByteBuf(byteBuf, this.text);
      ByteBufUtils.handleByteBuf(byteBuf, this.text3);
      ByteBufUtils.handleByteBuf8(byteBuf, this.text2);
   }

   public void setText22(String text) {
      this.text2 = text;
   }

   @Override
   public void handleByteBuf(ByteBuf byteBuf) {
      this.setText2(ByteBufUtils.createText(byteBuf));
      this.setText(ByteBufUtils.createText(byteBuf));
      this.setText3(ByteBufUtils.createText(byteBuf));
      this.setText(ByteBufUtils.describeByteBuf(byteBuf));
   }

   public void setText2(String text) {
      this.text4 = text;
   }

   public String getText3() {
      return this.text3;
   }

   public String getText22() {
      return this.text2;
   }

   public String getText() {
      return this.text;
   }

   public String getText2() {
      return this.text4;
   }

   public void setText3(String text) {
      this.text3 = text;
   }
}
