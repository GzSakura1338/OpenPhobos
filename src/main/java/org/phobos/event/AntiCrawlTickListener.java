package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import org.phobos.module.player.AntiCrawlModule;
import org.phobos.module.player.AntiCrawlModule;

public final class AntiCrawlTickListener extends ModuleListener.Safe<AntiCrawlModule, TickEvent> {
   public AntiCrawlTickListener(AntiCrawlModule antiCrawlModule) {
      super(antiCrawlModule, TickEvent.class);
   }

   public void handleObject(
      TickEvent tickEvent, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (this.module.mode.testObject2(AntiCrawlModule.Mode.Clip) && clientPlayerEntity.isInSwimmingPose() && !clientPlayerEntity.isTouchingWater()) {
         if (!this.module.normal.testLong(500L)) {
            tickEvent.getKeyboardInput().jumping = false;
         }
      }
   }
}
