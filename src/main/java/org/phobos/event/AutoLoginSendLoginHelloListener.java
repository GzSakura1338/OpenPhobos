package org.phobos.event;

import net.minecraft.network.packet.c2s.login.LoginHelloC2SPacket;
import org.phobos.core.event.events.PacketEvent;
import org.phobos.module.misc.AutoLoginModule;

public final class AutoLoginSendLoginHelloListener extends ModuleListener<AutoLoginModule, PacketEvent.Send<LoginHelloC2SPacket>> {
   public void handleObject2(PacketEvent.Send<LoginHelloC2SPacket> send) {
      this.module.normal.run();
   }

   public AutoLoginSendLoginHelloListener(AutoLoginModule autoLoginModule) {
      super(autoLoginModule, PacketEvent.Send.class, LoginHelloC2SPacket.class);
   }
}
