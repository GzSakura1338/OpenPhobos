package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.phobos.core.event.events.PacketEvent;
import org.phobos.module.movement.TickShiftModule;

public final class TickShiftSendPlayerMoveListener extends ModuleListener.Safe<TickShiftModule, PacketEvent.Send<PlayerMoveC2SPacket>> {
   public void handleObject(
      PacketEvent.Send<PlayerMoveC2SPacket> send,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (this.module.ground.getValue()) {
         send.setCancelled(true);
      } else {
         this.module.handleSend(send, false);
      }
   }

   public TickShiftSendPlayerMoveListener(TickShiftModule tickShiftModule) {
      super(tickShiftModule, PacketEvent.Send.class, PlayerMoveC2SPacket.class, -10000);
   }
}
