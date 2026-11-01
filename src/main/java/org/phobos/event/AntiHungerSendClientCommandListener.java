package org.phobos.event;

import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;
import org.phobos.core.event.events.PacketEvent;
import org.phobos.module.player.AntiHungerModule;

public final class AntiHungerSendClientCommandListener extends ModuleListener<AntiHungerModule, PacketEvent.Send<ClientCommandC2SPacket>> {
   public AntiHungerSendClientCommandListener(AntiHungerModule antiHungerModule) {
      super(antiHungerModule, PacketEvent.Send.class, ClientCommandC2SPacket.class);
   }

   public void handleObject2(PacketEvent.Send<ClientCommandC2SPacket> send) {
      if (this.module.sprint.getValue() && ((ClientCommandC2SPacket)send.getPacket()).getMode().equals(Mode.START_SPRINTING)) {
         send.setCancelled(true);
      }
   }
}
