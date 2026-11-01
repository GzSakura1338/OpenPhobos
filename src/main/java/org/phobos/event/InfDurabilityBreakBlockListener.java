package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import org.phobos.module.player.InfDurabilityModule;
import org.phobos.module.player.InfDurabilityModule;

public final class InfDurabilityBreakBlockListener extends ModuleListener.Safe<InfDurabilityModule, BreakBlockEvent> {
   public InfDurabilityBreakBlockListener(InfDurabilityModule infDurabilityModule) {
      super(infDurabilityModule, BreakBlockEvent.class);
   }

   public void handleObject(
      BreakBlockEvent breakBlockEvent,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      this.module
         .handleClientPlayerEntity(
            clientPlayerEntity, clientWorld, clientPlayerInteractionManager, InfDurabilityModule.Type.Break, breakBlockEvent.getBlockPos()
         );
   }
}
