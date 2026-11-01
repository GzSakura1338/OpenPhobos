package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import org.phobos.core.event.events.PacketEvent;
import org.phobos.module.misc.AutoKitModule;

public final class AutoKitPostReceivePlayerPositionLookListener extends ModuleListener.Safe<AutoKitModule, PacketEvent.PostReceive<PlayerPositionLookS2CPacket>> {
   public AutoKitPostReceivePlayerPositionLookListener(AutoKitModule autoKitModule) {
      super(autoKitModule, PacketEvent.PostReceive.class, PlayerPositionLookS2CPacket.class);
   }

   public void handleObject(
      PacketEvent.PostReceive<PlayerPositionLookS2CPacket> postReceive,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (this.module.check()) {
         if (mc.isInSingleplayer()) {
            this.module.flag = false;
         } else {
            if (this.module.flag) {
               this.module.handleClientPlayerEntity(clientPlayerEntity, true);
            }
         }
      }
   }
}
