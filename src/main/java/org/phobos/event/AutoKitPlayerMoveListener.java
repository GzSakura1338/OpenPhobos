package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import org.phobos.core.event.events.PlayerMoveEvent;
import org.phobos.module.misc.AutoKitModule;

public final class AutoKitPlayerMoveListener extends ModuleListener.Safe<AutoKitModule, PlayerMoveEvent> {
   public AutoKitPlayerMoveListener(AutoKitModule autoKitModule) {
      super(autoKitModule, PlayerMoveEvent.class);
   }

   public void handleObject(
      PlayerMoveEvent playerMoveEvent,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (this.module.check()) {
         ;
      }
   }
}
