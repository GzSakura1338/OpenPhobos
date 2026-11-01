package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import org.phobos.core.tick.TickManager;
import org.phobos.module.movement.FakeLagModule;

public final class FakeLagPlayerTickListener extends ModuleListener.Safe<FakeLagModule, PlayerTickEvent> {
   public void handleObject(
      PlayerTickEvent playerTickEvent,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (!this.phobosClient.getTickManager().check()) {
         if (this.module.normal.testInt3(this.module.delay.getValue())) {
            this.phobosClient
               .getTickManager()
               .handleClientPlayerEntity3(
                  clientPlayerEntity, TickManager.TickData.createTickData(this.module.ticks.getValue().getNumber4()), this.module.tick.getValue()
               );
         }
      }
   }

   public FakeLagPlayerTickListener(FakeLagModule fakeLagModule) {
      super(fakeLagModule, PlayerTickEvent.class);
   }
}
