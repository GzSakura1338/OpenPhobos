package org.phobos.core.net.packet;

import io.netty.buffer.ByteBuf;
import java.util.Map;
import org.phobos.api.NetworkPacket;
import org.phobos.core.net.ByteBufUtils;

public class TextMapPacket230 implements NetworkPacket {
   public Map<String, String> textMap;
   public String text;

   public String getText() {
      return this.text;
   }

   public TextMapPacket230() {
   }

   public void setText(String text) {
      this.text = text;
   }

   @Override
   public void handleByteBuf(ByteBuf byteBuf) {
      this.setText(ByteBufUtils.createText(byteBuf));
      this.setTextMap(ByteBufUtils.resolveMap(byteBuf, text -> text, text -> text));
   }

   public Map<String, String> getTextMap() {
      return this.textMap;
   }

   @Override
   public void handleByteBuf2(ByteBuf byteBuf) {
      ByteBufUtils.handleByteBuf(byteBuf, this.text);
      ByteBufUtils.handleByteBuf5(byteBuf, this.textMap, text -> text, text -> text);
   }

   public TextMapPacket230(String text, Map<String, String> map) {
      this.setText(text);
      this.setTextMap(map);
   }

   public void setTextMap(Map<String, String> map) {
      this.textMap = map;
   }
}
