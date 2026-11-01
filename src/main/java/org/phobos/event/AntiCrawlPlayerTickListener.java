package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EntityPose;
import org.phobos.module.player.AntiCrawlModule;
import org.phobos.module.player.AntiCrawlModule;

public final class AntiCrawlPlayerTickListener extends ModuleListener.Safe<AntiCrawlModule, PlayerTickEvent> {
   public AntiCrawlPlayerTickListener(AntiCrawlModule antiCrawlModule) {
      super(antiCrawlModule, PlayerTickEvent.class);
   }

   public void handleObject(
      PlayerTickEvent playerTickEvent,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      switch ((AntiCrawlModule.Mode)this.module.mode.getValue()) {
         case Disable:
            if (!clientPlayerEntity.isInSwimmingPose()) {
               return;
            } else {
               clientPlayerEntity.setPose(EntityPose.STANDING);
            }
      }
   }
}
