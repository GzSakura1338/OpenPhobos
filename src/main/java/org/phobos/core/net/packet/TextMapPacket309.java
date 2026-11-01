package org.phobos.core.net.packet;

import io.netty.buffer.ByteBuf;
import java.util.Map;
import org.phobos.api.NetworkPacket;
import org.phobos.core.net.ByteBufUtils;

public class TextMapPacket309 implements NetworkPacket {
   public Map<String, String> textMap;

   public TextMapPacket309(Map<String, String> map) {
      this.setTextMap(map);
   }

   @Override
   public void handleByteBuf(ByteBuf byteBuf) {
      this.setTextMap(ByteBufUtils.resolveMap(byteBuf, text -> text, text -> text));
   }

   public Map<String, String> getTextMap() {
      return this.textMap;
   }

   @Override
   public void handleByteBuf2(ByteBuf byteBuf) {
      ByteBufUtils.handleByteBuf5(byteBuf, this.textMap, text -> text, text -> text);
   }

   public TextMapPacket309() {
   }

   public void setTextMap(Map<String, String> map) {
      this.textMap = map;
   }
}
