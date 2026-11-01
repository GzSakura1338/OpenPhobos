package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.Text;
import org.phobos.core.tick.TimerManager;
import org.phobos.core.player.PlayerActionUtils;
import org.phobos.module.movement.DisablerModule;
import org.phobos.module.movement.DisablerModule;
import org.phobos.module.client.NotificationsModule;
import org.phobos.module.player.ScaffoldModule;

public final class DisablerTick2Listener extends ModuleListener.Safe<DisablerModule, TickEvent2> {
   public void handleObject(
      TickEvent2 tickEvent2, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      switch ((DisablerModule.Mode)this.module.mode.getValue()) {
         case Watchdog:
            if (this.module.motion.getValue() && this.module.flag11 && clientPlayerEntity.isOnGround()) {
               NotificationsModule.getNotificationsModule().handleText9(Text.literal("Failed disabler task"), "WatchdogMotionDisabler");
               this.module.flag11 = false;
               this.module.intValue3 = 0;
            }

            if (this.module.scaffold.getValue()) {
               if (!ScaffoldModule.getScaffoldModule().check7()) {
                  this.module.flag8 = false;
                  this.module.flag = false;
                  this.module.flag5 = false;
                  return;
               }

               if (clientPlayerEntity.isOnGround() && this.module.flag) {
                  this.module.flag8 = true;
                  return;
               }

               if (!this.module.flag5 && clientPlayerEntity.isOnGround()) {
                  this.module.flag5 = true;
                  PlayerActionUtils.handleClientPlayerEntity29(clientPlayerEntity, 0.01);
                  this.module.flag = true;
               }
            }
            break;
         case Intave:
            if (this.module.intValue2++ < 400) {
               this.phobosClient.getTimerManager().handleFloat(0.2F, TimerManager.TimerPriority.High);
               return;
            }

            if (this.phobosClient.getTimerManager().testFloat(0.2F)) {
               this.phobosClient.getTimerManager().run2();
            }
      }
   }

   public DisablerTick2Listener(DisablerModule disablerModule) {
      super(disablerModule, TickEvent2.class);
   }
}
