package org.phobos.event;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.shape.VoxelShape;
import org.phobos.module.movement.WallClimbModule;

public final class WallClimbTickListener extends ModuleListener.Safe<WallClimbModule, TickEvent2> {
   public void handleObject(
      TickEvent2 tickEvent2, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
   }

   public WallClimbTickListener(WallClimbModule wallClimbModule) {
      super(wallClimbModule, TickEvent2.class);
   }

   public boolean testAbstractClientPlayerEntity(AbstractClientPlayerEntity abstractClientPlayerEntity) {
      for (VoxelShape voxelShape : abstractClientPlayerEntity.clientWorld
         .getBlockCollisions(abstractClientPlayerEntity, abstractClientPlayerEntity.getBoundingBox().expand(0.5, 0.0, 0.5).offset(0.0, -1.05, 0.0))) {
         if (!voxelShape.isEmpty()) {
            return true;
         }
      }

      return false;
   }
}
