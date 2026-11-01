package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import org.phobos.core.event.events.PlayerMoveEvent;
import org.phobos.module.movement.PhaseModule;

public final class PhasePlayerMoveListener extends ModuleListener.Safe<PhaseModule, PlayerMoveEvent> {
   public void handleObject(
      PlayerMoveEvent playerMoveEvent,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
   }

   public PhasePlayerMoveListener(PhaseModule phaseModule) {
      super(phaseModule, PlayerMoveEvent.class);
   }
}
