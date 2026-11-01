package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import org.phobos.module.movement.AutoTridentModule;
import org.phobos.module.movement.AutoTridentModule;

public final class AutoTridentOnStoppedUsingListener extends ModuleListener.Safe<AutoTridentModule, OnStoppedUsingEvent> {
   public void handleObject(
      OnStoppedUsingEvent onStoppedUsingEvent,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      switch ((AutoTridentModule.Mode)this.module.mode.getValue()) {
         case Vanilla:
            onStoppedUsingEvent.setFlag(true);
            onStoppedUsingEvent.setCancelled(true);
            break;
         case Grim:
            onStoppedUsingEvent.setFlag(true);
            onStoppedUsingEvent.setIntValue(10);
            onStoppedUsingEvent.setCancelled(true);
      }
   }

   public AutoTridentOnStoppedUsingListener(AutoTridentModule autoTridentModule) {
      super(autoTridentModule, OnStoppedUsingEvent.class);
   }
}
