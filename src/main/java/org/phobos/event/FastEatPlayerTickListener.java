package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import org.phobos.module.player.FastEatModule;
import org.phobos.module.player.FastEatModule;

public final class FastEatPlayerTickListener extends ModuleListener.Safe<FastEatModule, PlayerTickEvent> {
   public FastEatPlayerTickListener(FastEatModule fastEatModule) {
      super(fastEatModule, PlayerTickEvent.class);
   }

   public void handleObject(
      PlayerTickEvent playerTickEvent,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (this.module.mode.testObject2(FastEatModule.Mode.Grim) && this.module.testClientPlayerEntity(clientPlayerEntity, clientPlayerEntity.getActiveItem())) {
         this.phobosClient
            .getRotationManager()
            .testFloat(
               this.phobosClient.getRotationManager().computeFloat5(clientPlayerEntity.getYaw()),
               this.phobosClient.getRotationManager().computeFloat6(clientPlayerEntity.getPitch())
            );
         this.phobosClient.getRotationManager().handleClientPlayerEntity5(clientPlayerEntity);
      }
   }
}
