package org.phobos.event;

import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.phobos.core.event.events.PacketEvent;
import org.phobos.module.movement.PacketFlyModule;

public final class PacketFlySendListener extends ModuleListener<PacketFlyModule, PacketEvent.Send<?>> {
   public PacketFlySendListener(PacketFlyModule packetFlyModule) {
      super(packetFlyModule, PacketEvent.Send.class);
   }

   public void handleObject2(PacketEvent.Send<?> send) {
      if (send.getPacket() instanceof PlayerMoveC2SPacket playerMoveC2SPacket) {
         this.module.handlePlayerMoveC2SPacket(playerMoveC2SPacket, send);
      }
   }
}
