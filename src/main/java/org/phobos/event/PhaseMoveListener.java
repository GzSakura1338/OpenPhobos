package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import org.phobos.module.movement.PhaseModule;

public final class PhaseMoveListener extends ModuleListener.Safe<PhaseModule, MoveEvent2> {
   public PhaseMoveListener(PhaseModule phaseModule) {
      super(phaseModule, MoveEvent2.class);
   }

   public void handleObject(
      MoveEvent2 moveEvent2, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
   }
}
