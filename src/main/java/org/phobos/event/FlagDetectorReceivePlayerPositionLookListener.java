package org.phobos.event;

import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import org.phobos.core.event.events.PacketEvent;
import org.phobos.module.misc.FlagDetectorModule;

public final class FlagDetectorReceivePlayerPositionLookListener extends ModuleListener<FlagDetectorModule, PacketEvent.Receive<PlayerPositionLookS2CPacket>> {
   public void handleObject2(PacketEvent.Receive<PlayerPositionLookS2CPacket> receive) {
      if (this.module.movement.getValue()) {
         this.module.run2();
      }
   }

   public FlagDetectorReceivePlayerPositionLookListener(FlagDetectorModule flagDetectorModule) {
      super(flagDetectorModule, PacketEvent.Receive.class, PlayerPositionLookS2CPacket.class);
   }
}
