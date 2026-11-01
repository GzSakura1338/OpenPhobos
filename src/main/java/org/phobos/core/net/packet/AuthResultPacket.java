package org.phobos.core.net.packet;

import io.netty.buffer.ByteBuf;
import org.phobos.api.NetworkPacket;
import org.phobos.core.net.ByteBufUtils;

public class AuthResultPacket implements NetworkPacket {
   public String text;
   public AuthResultPacket.AuthResult authResult;
   public String text2;

   public AuthResultPacket.AuthResult getAuthResult() {
      return this.authResult;
   }

   public AuthResultPacket(AuthResultPacket.AuthResult authResult, String text, String text2) {
      this.authResult = authResult;
      this.text = text;
      this.text2 = text2;
   }

   public void setText(String text) {
      this.text = text;
   }

   public String getText() {
      return this.text;
   }

   public void setAuthResult(AuthResultPacket.AuthResult authResult) {
      this.authResult = authResult;
   }

   public void setText2(String text) {
      this.text2 = text;
   }

   public AuthResultPacket() {
   }

   @Override
   public void handleByteBuf(ByteBuf byteBuf) {
      this.setAuthResult(AuthResultPacket.AuthResult.fromString(ByteBufUtils.createText(byteBuf)));
      this.setText(ByteBufUtils.createText(byteBuf));
      this.setText2(ByteBufUtils.createText(byteBuf));
   }

   public String getText2() {
      return this.text2;
   }

   @Override
   public void handleByteBuf2(ByteBuf byteBuf) {
      ByteBufUtils.handleByteBuf(byteBuf, this.authResult.toString());
      ByteBufUtils.handleByteBuf(byteBuf, this.text);
      ByteBufUtils.handleByteBuf(byteBuf, this.text2);
   }

   public enum AuthResult {
      SUCCESS("SUCCESS"),
      FAIL("FAIL");

      public final String message;

      AuthResult(String message) {
         this.message = message;
      }

      public static AuthResultPacket.AuthResult fromString(String message) {
         return message.equals("SUCCESS") ? SUCCESS : FAIL;
      }

      @Override
      public String toString() {
         return this.message;
      }
   }
}
