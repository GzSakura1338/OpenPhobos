package org.phobos.core.net.packet;

import io.netty.buffer.ByteBuf;
import org.phobos.api.NetworkPacket;

public class EmptyPacket220 implements NetworkPacket {
   @Override
   public void handleByteBuf2(ByteBuf byteBuf) {
   }

   @Override
   public void handleByteBuf(ByteBuf byteBuf) {
   }
}
