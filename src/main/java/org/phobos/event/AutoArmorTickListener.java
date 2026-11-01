package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import org.phobos.module.combat.AutoArmorModule;

public final class AutoArmorTickListener extends ModuleListener.Safe<AutoArmorModule, TickEvent2> {
   public AutoArmorTickListener(AutoArmorModule autoArmorModule) {
      super(autoArmorModule, TickEvent2.class);
   }

   public void handleObject(
      TickEvent2 tickEvent2, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      this.module.handleClientPlayerEntity3(clientPlayerEntity, clientWorld);
   }
}
