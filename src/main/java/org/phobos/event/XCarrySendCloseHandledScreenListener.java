package org.phobos.event;

import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import org.phobos.core.event.events.PacketEvent;
import org.phobos.module.player.XCarryModule;

public final class XCarrySendCloseHandledScreenListener extends ModuleListener<XCarryModule, PacketEvent.Send<CloseHandledScreenC2SPacket>> {
   public void handleObject2(PacketEvent.Send<CloseHandledScreenC2SPacket> send) {
      send.setCancelled(true);
   }

   public XCarrySendCloseHandledScreenListener(XCarryModule xCarryModule) {
      super(xCarryModule, PacketEvent.Send.class, CloseHandledScreenC2SPacket.class);
   }
}
