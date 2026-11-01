package org.phobos.event;

import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.phobos.core.event.events.PacketEvent;

public abstract class MovePacketListener extends Listener<PacketEvent.Send<?>> {
   public MovePacketListener() {
      super(PacketEvent.Send.class, null, Integer.MIN_VALUE);
   }

   public void handleObject2(PacketEvent.Send<?> send) {
      if (!send.isCancelled()) {
         if (send.getPacket() instanceof PlayerMoveC2SPacket playerMoveC2SPacket) {
            this.handlePlayerMoveC2SPacket(playerMoveC2SPacket);
         }
      }
   }

   public abstract void handlePlayerMoveC2SPacket(PlayerMoveC2SPacket var1);
}
