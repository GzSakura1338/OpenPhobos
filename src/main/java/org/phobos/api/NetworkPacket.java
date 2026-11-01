package org.phobos.api;

import io.netty.buffer.ByteBuf;

public interface NetworkPacket {
   void handleByteBuf(ByteBuf var1);

   void handleByteBuf2(ByteBuf var1);
}
