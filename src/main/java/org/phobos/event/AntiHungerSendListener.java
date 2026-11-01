package org.phobos.event;

import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.phobos.core.event.events.PacketEvent;
import org.phobos.mixin.net.interfaces.IPlayerMoveC2SPacket;
import org.phobos.module.player.AntiHungerModule;

public final class AntiHungerSendListener extends ModuleListener<AntiHungerModule, PacketEvent.Send<?>> {
   public void handleObject2(PacketEvent.Send<?> send) {
      if (send.getPacket() instanceof PlayerMoveC2SPacket playerMoveC2SPacket && this.module.ground.getValue()) {
         ((IPlayerMoveC2SPacket)(Object)playerMoveC2SPacket).setOnGround(false);
      }
   }

   public AntiHungerSendListener(AntiHungerModule antiHungerModule) {
      super(antiHungerModule, PacketEvent.Send.class);
   }
}
