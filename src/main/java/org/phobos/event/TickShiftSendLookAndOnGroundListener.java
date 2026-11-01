package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.LookAndOnGround;
import org.phobos.core.event.events.PacketEvent;
import org.phobos.module.movement.TickShiftModule;

public final class TickShiftSendLookAndOnGroundListener extends ModuleListener.Safe<TickShiftModule, PacketEvent.Send<LookAndOnGround>> {
   public void handleObject(
      PacketEvent.Send<LookAndOnGround> send,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (!this.module.rotation.getValue()
         || !this.module.ground.getValue() && ((LookAndOnGround)send.getPacket()).isOnGround() != clientPlayerEntity.isOnGround()) {
         this.module.handleSend(send, false);
      } else {
         send.setCancelled(true);
      }
   }

   public TickShiftSendLookAndOnGroundListener(TickShiftModule tickShiftModule) {
      super(tickShiftModule, PacketEvent.Send.class, LookAndOnGround.class, -10000);
   }
}
