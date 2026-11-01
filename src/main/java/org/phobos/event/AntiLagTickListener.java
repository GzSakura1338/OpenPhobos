package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import org.phobos.module.misc.AntiLagModule;

public final class AntiLagTickListener extends ModuleListener.Safe<AntiLagModule, TickEvent2> {
   public AntiLagTickListener(AntiLagModule antiLagModule) {
      super(antiLagModule, TickEvent2.class);
   }

   public void handleObject(
      TickEvent2 tickEvent2, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (this.module.itemFix.getValue() && this.module.intValue != 0 && this.module.normal.testLong(100L)) {
         this.module.intValue = 0;
      }
   }
}
