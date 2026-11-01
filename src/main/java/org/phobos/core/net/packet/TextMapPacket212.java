package org.phobos.core.net.packet;

import io.netty.buffer.ByteBuf;
import java.util.HashMap;
import java.util.Map;
import java.util.Map;
import org.phobos.api.NetworkPacket;
import org.phobos.core.net.ByteBufUtils;

public class TextMapPacket212 implements NetworkPacket {
   public Map<String, String> textMap;
   public static final int intValue = 255;

   public TextMapPacket212() {
   }

   @Override
   public void handleByteBuf(ByteBuf byteBuf) {
      int i2 = byteBuf.readInt();
      this.textMap = new HashMap<>(i2);
      if (i2 != 0) {
         for (int i = 0; i < i2; i++) {
            this.textMap.put(ByteBufUtils.createText(byteBuf), ByteBufUtils.createText(byteBuf));
         }
      }
   }

   public TextMapPacket212(Map<String, String> map) {
      this.setTextMap(map);
   }

   @Override
   public void handleByteBuf2(ByteBuf byteBuf) {
      byteBuf.writeInt(Math.min(this.textMap.size(), 255));
      int i = 0;

      for (Map.Entry<String, String> entry : this.textMap.entrySet()) {
         if (i > 255) {
            break;
         }

         ByteBufUtils.handleByteBuf(byteBuf, entry.getKey());
         ByteBufUtils.handleByteBuf(byteBuf, entry.getValue());
         i++;
      }
   }

   public void setTextMap(Map<String, String> map) {
      this.textMap = map;
   }

   public Map<String, String> getTextMap() {
      return this.textMap;
   }
}
