package org.phobos.core.bootstrap;

import io.netty.buffer.ByteBuf;
import java.security.SecureRandom;

public class ChallengeGenerator {
   public final SecureRandom secureRandom = new SecureRandom();

   public long computeByteBuf(ByteBuf byteBuf) {
      long l = this.secureRandom.nextLong();
      byteBuf.writeLong(l);
      return l;
   }

   public long computeByteBuf2(ByteBuf byteBuf) {
      return byteBuf.readLong();
   }
}
