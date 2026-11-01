package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import org.phobos.core.event.events.PlayerMoveEvent;
import org.phobos.core.player.PlayerUtils;
import org.phobos.module.movement.HoleSnapModule;
import org.phobos.module.movement.TickShiftModule;

public final class TickShiftPlayerMoveListener extends ModuleListener.Safe<TickShiftModule, PlayerMoveEvent> {
   public void handleObject(
      PlayerMoveEvent playerMoveEvent,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (!PlayerUtils.check()) {
         if (!HoleSnapModule.getHoleSnapModule().check7()) {
            this.phobosClient.getTimerManager().run2();
         }

         int i = this.module.packets.getValue();
         this.module.intValue = this.module.intValue >= i ? i : this.module.intValue + 1;
      }
   }

   public TickShiftPlayerMoveListener(TickShiftModule tickShiftModule) {
      super(tickShiftModule, PlayerMoveEvent.class);
   }
}
