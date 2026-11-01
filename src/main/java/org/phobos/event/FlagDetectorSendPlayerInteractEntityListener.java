package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import org.earthhack.ducks.IPlayerInteractEntityC2SPacket;
import org.phobos.core.event.events.PacketEvent;
import org.phobos.module.misc.FlagDetectorModule;

public final class FlagDetectorSendPlayerInteractEntityListener extends ModuleListener.Safe<FlagDetectorModule, PacketEvent.Send<PlayerInteractEntityC2SPacket>> {
   public FlagDetectorSendPlayerInteractEntityListener(FlagDetectorModule flagDetectorModule) {
      super(flagDetectorModule, PacketEvent.Send.class, PlayerInteractEntityC2SPacket.class);
   }

   public void handleObject(
      PacketEvent.Send<PlayerInteractEntityC2SPacket> send,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (this.module.combat.getValue()) {
         PlayerInteractEntityC2SPacket playerInteractEntityC2SPacket = (PlayerInteractEntityC2SPacket)send.getPacket();
         IPlayerInteractEntityC2SPacket iPlayerInteractEntityC2SPacket = (IPlayerInteractEntityC2SPacket)(Object)playerInteractEntityC2SPacket;
         if (iPlayerInteractEntityC2SPacket.attack() && iPlayerInteractEntityC2SPacket.entity() instanceof PlayerEntity playerEntity) {
            this.module.flag = true;
            this.module.intValue2 = 0;
            this.module.playerEntity = playerEntity;
         }
      }
   }
}
