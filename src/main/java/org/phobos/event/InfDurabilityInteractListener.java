package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import org.phobos.module.player.InfDurabilityModule;

public final class InfDurabilityInteractListener extends ModuleListener.Safe<InfDurabilityModule, InteractEvent> {
   public InfDurabilityInteractListener(InfDurabilityModule infDurabilityModule) {
      super(infDurabilityModule, InteractEvent.class);
   }

   public void handleObject(
      InteractEvent interactEvent,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
   }
}
