package org.phobos.event;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket.Entry;
import org.phobos.core.event.events.PacketEvent;
import org.phobos.module.misc.StaffDetectorModule;

public final class StaffDetectorReceivePlayerListListener extends ModuleListener.Safe<StaffDetectorModule, PacketEvent.Receive<PlayerListS2CPacket>> {
   public StaffDetectorReceivePlayerListListener(StaffDetectorModule staffDetectorModule) {
      super(staffDetectorModule, PacketEvent.Receive.class, PlayerListS2CPacket.class);
   }

   public void handleObject(
      PacketEvent.Receive<PlayerListS2CPacket> receive,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      for (Entry entry : ((PlayerListS2CPacket)receive.getPacket()).getPlayerAdditionEntries()) {
         GameProfile gameProfile = entry.profile();
         if (gameProfile != null) {
            this.module.handleClientPlayerEntity(clientPlayerEntity, gameProfile.getName());
         }
      }
   }
}
