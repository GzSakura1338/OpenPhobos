package org.phobos.core.net.packet;

import io.netty.buffer.ByteBuf;
import org.phobos.api.NetworkPacket;

public class EmptyPacket261 implements NetworkPacket {
   @Override
   public void handleByteBuf(ByteBuf byteBuf) {
   }

   @Override
   public void handleByteBuf2(ByteBuf byteBuf) {
   }
}
