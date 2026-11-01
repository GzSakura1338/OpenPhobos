package org.phobos.core.net;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import org.phobos.api.NetworkPacket;
import org.phobos.core.PhobosClient;

public class PacketBus {
   public List<PacketHandler<?>> packetHandlerList = new CopyOnWriteArrayList<>();
   public List<PacketHandler<?>> packetHandlerList2 = new CopyOnWriteArrayList<>();
   public List<NetworkPacket> networkPacketList2 = new CopyOnWriteArrayList<>();
   public boolean flag;
   public PhobosClient phobosClient;

   public void handleNetworkPacket(NetworkPacket networkPacket) {
      this.networkPacketList2.add(networkPacket);
   }

   public <P extends NetworkPacket> void handleClassValue(Class<? extends P> classValue, Consumer<P> consumer) {
      this.packetHandlerList.add(new PacketHandler<>(classValue, consumer));
   }

   public <P extends NetworkPacket> void handleClassValue2(Class<? extends P> classValue, Consumer<P> consumer) {
      this.packetHandlerList2.add(new PacketHandler<>(classValue, consumer));
   }

   public List<PacketHandler<?>> getPacketHandlerList() {
      return this.packetHandlerList;
   }

   public List<PacketHandler<?>> getPacketHandlerList2() {
      return this.packetHandlerList2;
   }

   public List<NetworkPacket> getNetworkPacketList2() {
      return this.networkPacketList2;
   }

   public void setFlag(boolean flag) {
      this.flag = flag;
   }

   public boolean isFlag() {
      return this.flag;
   }

   public PhobosClient getPhobosClient() {
      return this.phobosClient;
   }

   public void setPhobosClient(PhobosClient phobosClient) {
      this.phobosClient = phobosClient;
   }

   public void handleNetworkPacket2(NetworkPacket networkPacket) {
      for (PacketHandler packetHandler : this.packetHandlerList2) {
         if (packetHandler.testNetworkPacket(networkPacket)) {
            packetHandler.handleNetworkPacket(networkPacket);
         }
      }
   }
}
