package org.phobos.event;

import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import org.phobos.core.event.events.PacketEvent;
import org.phobos.module.movement.FastSwimModule;

public final class FastSwimReceivePlayerPositionLookListener extends ModuleListener<FastSwimModule, PacketEvent.Receive<PlayerPositionLookS2CPacket>> {
   public FastSwimReceivePlayerPositionLookListener(FastSwimModule fastSwimModule) {
      super(fastSwimModule, PacketEvent.Receive.class, PlayerPositionLookS2CPacket.class);
   }

   public void handleObject2(PacketEvent.Receive<PlayerPositionLookS2CPacket> receive) {
      this.module.floatValue = 0.0F;
   }
}
