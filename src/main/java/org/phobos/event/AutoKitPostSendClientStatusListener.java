package org.phobos.event;

import net.minecraft.network.packet.c2s.play.ClientStatusC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientStatusC2SPacket.Mode;
import org.phobos.core.event.events.PacketEvent;
import org.phobos.module.misc.AutoKitModule;

public final class AutoKitPostSendClientStatusListener extends ModuleListener<AutoKitModule, PacketEvent.PostSend<ClientStatusC2SPacket>> {
   public void handleObject2(PacketEvent.PostSend<ClientStatusC2SPacket> postSend) {
      if (this.module.check()) {
         if (mc.isInSingleplayer()) {
            this.module.flag = false;
         } else if (((ClientStatusC2SPacket)postSend.getPacket()).getMode().equals(Mode.PERFORM_RESPAWN)) {
            this.module.flag = true;
         }
      }
   }

   public AutoKitPostSendClientStatusListener(AutoKitModule autoKitModule) {
      super(autoKitModule, PacketEvent.PostSend.class, ClientStatusC2SPacket.class);
   }
}
