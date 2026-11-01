package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import org.phobos.core.player.PlayerUtils;
import org.phobos.module.movement.HoleSnapModule;
import org.phobos.module.movement.TickShiftModule;

public final class TickShiftTickListener extends ModuleListener.Safe<TickShiftModule, TickEvent2> {
   public TickShiftTickListener(TickShiftModule tickShiftModule) {
      super(tickShiftModule, TickEvent2.class);
   }

   public void handleObject(
      TickEvent2 tickEvent2, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (this.module.intValue <= 0
         || PlayerUtils.check()
         || !this.module.sneaking.getValue() && clientPlayerEntity.isSneaking() && !HoleSnapModule.getHoleSnapModule().check7()) {
         this.phobosClient.getTimerManager().run2();
      }
   }
}
