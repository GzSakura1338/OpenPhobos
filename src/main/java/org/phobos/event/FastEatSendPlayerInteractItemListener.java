package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.earthhack.ducks.IClientPlayerNetworkHandler;
import org.phobos.core.event.events.PacketEvent;
import org.phobos.module.player.FastEatModule;
import org.phobos.module.player.FastEatModule;

public final class FastEatSendPlayerInteractItemListener extends ModuleListener.Safe<FastEatModule, PacketEvent.Send<PlayerInteractItemC2SPacket>> {
   public FastEatSendPlayerInteractItemListener(FastEatModule fastEatModule) {
      super(fastEatModule, PacketEvent.Send.class, PlayerInteractItemC2SPacket.class);
   }

   public void handleObject(
      PacketEvent.Send<PlayerInteractItemC2SPacket> send,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (this.module.mode.getValue().equals(FastEatModule.Mode.Update)
         && this.module
            .testClientPlayerEntity(clientPlayerEntity, clientPlayerEntity.getStackInHand(((PlayerInteractItemC2SPacket)send.getPacket()).getHand()))) {
         ((IClientPlayerNetworkHandler)(Object)clientPlayerEntity.networkHandler)
            .handlePacket(new PlayerActionC2SPacket(Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, Direction.DOWN));
      }
   }
}
