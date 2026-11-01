package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import org.phobos.core.event.events.PacketEvent;
import org.phobos.module.movement.TickShiftModule;

public final class TickShiftReceivePlayerPositionLookListener extends ModuleListener.Safe<TickShiftModule, PacketEvent.Receive<PlayerPositionLookS2CPacket>> {
   public TickShiftReceivePlayerPositionLookListener(TickShiftModule tickShiftModule) {
      super(tickShiftModule, PacketEvent.Receive.class, PlayerPositionLookS2CPacket.class);
   }

   public void handleObject(
      PacketEvent.Receive<PlayerPositionLookS2CPacket> receive,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      this.module.run2();
   }
}
