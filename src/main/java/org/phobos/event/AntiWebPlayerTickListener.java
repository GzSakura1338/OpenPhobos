package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import org.phobos.core.tick.TimerManager;
import org.phobos.module.movement.AntiWebModule;
import org.phobos.module.movement.AntiWebModule;

public final class AntiWebPlayerTickListener extends ModuleListener.Safe<AntiWebModule, PlayerTickEvent> {
   public AntiWebPlayerTickListener(AntiWebModule antiWebModule) {
      super(antiWebModule, PlayerTickEvent.class);
   }

   public void handleObject(
      PlayerTickEvent playerTickEvent,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      switch ((AntiWebModule.Mode)this.module.mode.getValue()) {
         case Grim:
            if (this.module.testClientPlayerEntity(clientPlayerEntity, clientWorld)) {
               if (clientPlayerEntity.isOnGround()) {
                  this.phobosClient.getTimerManager().run2();
               } else {
                  this.phobosClient.getTimerManager().handleFloat(10.0F, TimerManager.TimerPriority.Normal);
                  this.module.flag = true;
               }

               return;
            } else if (this.module.flag) {
               this.phobosClient.getTimerManager().run2();
               this.module.flag = false;
            }
         case Collide:
         case Disable:
      }
   }
}
