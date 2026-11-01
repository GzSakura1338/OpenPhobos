package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.phobos.core.event.events.PacketEvent;
import org.phobos.core.inventory.InventoryUtils;
import org.phobos.module.player.FastEatModule;

public final class FastEatSendPlayerActionListener extends ModuleListener.Safe<FastEatModule, PacketEvent.Send<PlayerActionC2SPacket>> {
   public void handleObject(
      PacketEvent.Send<PlayerActionC2SPacket> send,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (this.module.cancel.getValue() && InventoryUtils.testItem3(clientPlayerEntity.getActiveItem().getItem())) {
         if (((PlayerActionC2SPacket)send.getPacket()).getAction().equals(Action.RELEASE_USE_ITEM)) {
            if (((PlayerActionC2SPacket)send.getPacket()).getDirection().equals(Direction.DOWN)) {
               if (((PlayerActionC2SPacket)send.getPacket()).getPos().equals(BlockPos.ORIGIN)) {
                  send.setCancelled(true);
               }
            }
         }
      }
   }

   public FastEatSendPlayerActionListener(FastEatModule fastEatModule) {
      super(fastEatModule, PacketEvent.Send.class, PlayerActionC2SPacket.class);
   }
}
