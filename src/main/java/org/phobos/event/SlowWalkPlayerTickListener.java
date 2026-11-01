package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import org.phobos.core.tick.TimerManager;
import org.phobos.module.movement.SlowWalkModule;

public final class SlowWalkPlayerTickListener extends ModuleListener.Safe<SlowWalkModule, PlayerTickEvent> {
   public SlowWalkPlayerTickListener(SlowWalkModule slowWalkModule) {
      super(slowWalkModule, PlayerTickEvent.class);
   }

   public void handleObject(
      PlayerTickEvent playerTickEvent,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (this.module.normal.testNumber2(this.module.balance.getValue())) {
         this.module.flag = !this.module.flag;
      }

      if (this.module.flag) {
         this.phobosClient.getTimerManager().handleFloat(this.module.acceleration.getValue(), TimerManager.TimerPriority.High);
      } else {
         this.phobosClient.getTimerManager().handleFloat(this.module.shift.getValue(), TimerManager.TimerPriority.High);
      }
   }
}
