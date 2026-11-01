package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import org.phobos.module.render.FreeLookModule;
import org.phobos.module.movement.WallClimbModule;
import org.phobos.module.movement.WallClimbModule;

public final class WallClimbPlayerTickListener extends ModuleListener.Safe<WallClimbModule, PlayerTickEvent> {
   public void handleObject(
      PlayerTickEvent playerTickEvent,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (this.module.mode.testObject2(WallClimbModule.Mode.Polar) && this.module.rotate.getValue()) {
         this.phobosClient.getRotationManager().testFloat(FreeLookModule.computeClientPlayerEntity2(clientPlayerEntity), 65.0F);
      }
   }

   public WallClimbPlayerTickListener(WallClimbModule wallClimbModule) {
      super(wallClimbModule, PlayerTickEvent.class);
   }
}
