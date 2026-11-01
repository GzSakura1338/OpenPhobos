package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.Full;
import org.phobos.core.event.events.PacketEvent;
import org.phobos.module.movement.TickShiftModule;

public final class TickShiftSendFullListener extends ModuleListener.Safe<TickShiftModule, PacketEvent.Send<Full>> {
   public void handleObject(
      PacketEvent.Send<Full> send,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      this.module.handleSend(send, true);
   }

   public TickShiftSendFullListener(TickShiftModule tickShiftModule) {
      super(tickShiftModule, PacketEvent.Send.class, Full.class, -10000);
   }
}
