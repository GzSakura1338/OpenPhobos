package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import org.phobos.core.event.events.UnusedEvent;
import org.phobos.module.movement.PhaseModule;

public final class PhaseUnusedListener extends ModuleListener.Safe<PhaseModule, UnusedEvent> {
   public void handleObject(
      UnusedEvent unusedEvent, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
   }

   public PhaseUnusedListener(PhaseModule phaseModule) {
      super(phaseModule, UnusedEvent.class);
   }
}
