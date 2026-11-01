package org.phobos.core.net.packet;

import io.netty.buffer.ByteBuf;
import org.phobos.api.NetworkPacket;
import org.phobos.core.net.ByteBufUtils;

public class ProfilePicturePacket implements NetworkPacket {
   public String text;
   public String text2;

   public String getText2() {
      return this.text2;
   }

   public void setText2(String text) {
      this.text2 = text;
   }

   @Override
   public void handleByteBuf2(ByteBuf byteBuf) {
      ByteBufUtils.handleByteBuf(byteBuf, this.text2);
      ByteBufUtils.handleByteBuf(byteBuf, this.text);
   }

   public void setText(String text) {
      this.text = text;
   }

   @Override
   public void handleByteBuf(ByteBuf byteBuf) {
      this.setText2(ByteBufUtils.createText(byteBuf));
      this.setText(ByteBufUtils.createText(byteBuf));
   }

   public ProfilePicturePacket() {
   }

   public String getText() {
      return this.text;
   }

   public ProfilePicturePacket(String text, String text2) {
      this.setText2(text);
      this.setText(text2);
   }
}
