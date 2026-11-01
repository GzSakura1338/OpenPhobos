package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.OnGroundOnly;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.phobos.core.event.events.UnusedEvent;
import org.phobos.module.player.FastEatModule;
import org.phobos.module.player.FastEatModule;

public final class FastEatUnusedListener extends ModuleListener.Safe<FastEatModule, UnusedEvent> {
   public void handleObject(
      UnusedEvent unusedEvent, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (this.module.mode.getValue().equals(FastEatModule.Mode.Packet)
         && this.module.testClientPlayerEntity(clientPlayerEntity, clientPlayerEntity.getActiveItem())) {
         if (!(clientPlayerEntity.getItemUseTimeLeft() <= this.module.speed.getValue() - 1.0F) && !(this.module.speed.getValue() >= 25.0F)) {
            for (int i = 0; i < 32; i++) {
               clientPlayerEntity.networkHandler.sendPacket(new OnGroundOnly(clientPlayerEntity.isOnGround()));
            }

            clientPlayerEntity.networkHandler.sendPacket(new PlayerActionC2SPacket(Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, Direction.DOWN));
            clientPlayerEntity.stopUsingItem();
         }
      }
   }

   public FastEatUnusedListener(FastEatModule fastEatModule) {
      super(fastEatModule, UnusedEvent.class);
   }
}
