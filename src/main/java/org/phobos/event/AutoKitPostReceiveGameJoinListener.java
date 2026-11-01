package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import org.phobos.core.event.events.PacketEvent;
import org.phobos.module.misc.AutoKitModule;

public final class AutoKitPostReceiveGameJoinListener extends ModuleListener.Safe<AutoKitModule, PacketEvent.PostReceive<GameJoinS2CPacket>> {
   public AutoKitPostReceiveGameJoinListener(AutoKitModule autoKitModule) {
      super(autoKitModule, PacketEvent.PostReceive.class, GameJoinS2CPacket.class);
   }

   public void handleObject(
      PacketEvent.PostReceive<GameJoinS2CPacket> postReceive,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (this.module.check()) {
         if (mc.isInSingleplayer()) {
            this.module.flag = false;
         } else {
            this.module.handleClientPlayerEntity(clientPlayerEntity, true);
         }
      }
   }
}
