package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround;
import org.phobos.core.event.events.PacketEvent;
import org.phobos.module.movement.TickShiftModule;

public final class TickShiftSendPositionAndOnGroundListener extends ModuleListener.Safe<TickShiftModule, PacketEvent.Send<PositionAndOnGround>> {
   public TickShiftSendPositionAndOnGroundListener(TickShiftModule tickShiftModule) {
      super(tickShiftModule, PacketEvent.Send.class, PositionAndOnGround.class, -10000);
   }

   public void handleObject(
      PacketEvent.Send<PositionAndOnGround> send,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      this.module.handleSend(send, true);
   }
}
