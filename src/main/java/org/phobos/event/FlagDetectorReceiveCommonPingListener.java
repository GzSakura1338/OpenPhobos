package org.phobos.event;

import net.minecraft.network.packet.s2c.common.CommonPingS2CPacket;
import org.phobos.core.event.events.PacketEvent;
import org.phobos.module.misc.FlagDetectorModule;

public final class FlagDetectorReceiveCommonPingListener extends ModuleListener<FlagDetectorModule, PacketEvent.Receive<CommonPingS2CPacket>> {
   public FlagDetectorReceiveCommonPingListener(FlagDetectorModule flagDetectorModule) {
      super(flagDetectorModule, PacketEvent.Receive.class, CommonPingS2CPacket.class);
   }

   public void handleObject2(PacketEvent.Receive<CommonPingS2CPacket> receive) {
      this.module.intValue3 = ((CommonPingS2CPacket)receive.getPacket()).getParameter();
   }
}
