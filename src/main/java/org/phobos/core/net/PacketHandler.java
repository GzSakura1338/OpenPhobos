package org.phobos.core.net;

import java.util.function.Consumer;
import org.phobos.api.NetworkPacket;

public class PacketHandler<P extends NetworkPacket> {
   public final Class<? extends P> valueClassValue;
   public final Consumer<P> consumer;

   public void handleNetworkPacket(NetworkPacket networkPacket) {
      this.consumer.accept((P)networkPacket);
   }

   public boolean testNetworkPacket(NetworkPacket networkPacket) {
      return this.valueClassValue.equals(networkPacket.getClass());
   }

   public PacketHandler(Class<? extends P> classValue, Consumer<P> consumer) {
      this.valueClassValue = classValue;
      this.consumer = consumer;
   }
}
