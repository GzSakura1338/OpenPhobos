package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.Hand;
import org.phobos.core.event.events.PlayerMoveEvent;
import org.phobos.core.tick.TickUtils;
import org.phobos.module.player.FastEatModule;
import org.phobos.module.player.FastEatModule;

public final class FastEatPlayerMoveListener extends ModuleListener.Safe<FastEatModule, PlayerMoveEvent> {
   public void handleObject(
      PlayerMoveEvent playerMoveEvent,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (this.module.mode.testObject2(FastEatModule.Mode.Update) && this.module.testClientPlayerEntity(clientPlayerEntity, clientPlayerEntity.getActiveItem())
         )
       {
         Hand hand = clientPlayerEntity.getActiveHand() == null
            ? (clientPlayerEntity.getOffHandStack().equals(clientPlayerEntity.getActiveItem()) ? Hand.OFF_HAND : Hand.MAIN_HAND)
            : clientPlayerEntity.getActiveHand();
         TickUtils.handleClientWorld(
            clientWorld,
            integer -> clientPlayerEntity.networkHandler
               .sendPacket(new PlayerInteractItemC2SPacket(hand, integer, clientPlayerEntity.getYaw(), clientPlayerEntity.getPitch()))
         );
      }
   }

   public FastEatPlayerMoveListener(FastEatModule fastEatModule) {
      super(fastEatModule, PlayerMoveEvent.class);
   }
}
