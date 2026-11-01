package org.phobos.core.net.packet;

import io.netty.buffer.ByteBuf;
import org.phobos.api.NetworkPacket;

public class BytesPacket178 implements NetworkPacket {
   public byte[] byteArray;

   public BytesPacket178(byte[] bytes) {
      this.setByteArray(bytes);
   }

   public byte[] getByteArray() {
      return this.byteArray;
   }

   public void setByteArray(byte[] bytes) {
      this.byteArray = bytes;
   }

   @Override
   public void handleByteBuf(ByteBuf byteBuf) {
      int i = byteBuf.readInt();
      this.setByteArray(new byte[i]);
      byteBuf.readBytes(this.byteArray);
   }

   public BytesPacket178() {
   }

   @Override
   public void handleByteBuf2(ByteBuf byteBuf) {
      byteBuf.writeInt(this.byteArray.length);
      byteBuf.writeBytes(this.byteArray);
   }
}
