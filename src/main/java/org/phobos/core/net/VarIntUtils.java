package org.phobos.core.net;

import io.netty.buffer.ByteBuf;

public class VarIntUtils {
   public static int computeByteBuf(ByteBuf byteBuf) {
      int i = 0;
      int i2 = 0;

      byte b;
      do {
         b = byteBuf.readByte();
         i |= (b & 127) << i2++ * 7;
         if (i2 > 5) {
            throw new RuntimeException("VarInt too big");
         }
      } while (testByte(b));

      return i;
   }

   public static boolean testByte(byte b) {
      return (b & 128) == 128;
   }

   public static int computeInt(int i) {
      for (int i2 = 1; i2 < 5; i2++) {
         if ((i & -1 << i2 * 7) == 0) {
            return i2;
         }
      }

      return 5;
   }

   public static ByteBuf resolveByteBuf(ByteBuf byteBuf, int i) {
      while ((i & -128) != 0) {
         byteBuf.writeByte(i & 127 | 128);
         i >>>= 7;
      }

      byteBuf.writeByte(i);
      return byteBuf;
   }
}
