package org.phobos.core.net;

import io.netty.buffer.ByteBuf;

public class LongData {
   public long longValue;

   public long getLongValue() {
      return this.longValue;
   }

   public void handleByteBuf(ByteBuf byteBuf) {
      int i3 = byteBuf.readableBytes();
      byte[] bytes = new byte[8];

      for (int i = 0; i < 8; i++) {
         bytes[i] = (byte)(this.longValue >> i * 8 & 255L);
      }

      for (int i2 = 0; i2 < i3; i2++) {
         byte b = byteBuf.getByte(byteBuf.readerIndex() + i2);
         byte b2 = (byte)(b ^ bytes[i2 % 8]);
         byteBuf.setByte(byteBuf.readerIndex() + i2, b2);
      }
   }

   public void setLongValue(long l) {
      this.longValue = l;
   }
}
