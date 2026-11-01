package org.phobos.event;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import org.phobos.core.block.VoxelShapeUtils;
import org.phobos.module.movement.WallClimbModule;
import org.phobos.module.movement.WallClimbModule;

public final class WallClimbComputeNextListener extends ModuleListener.Safe<WallClimbModule, ComputeNextEvent> {
   public void handleObject(
      ComputeNextEvent computeNextEvent,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (this.module.mode.testObject2(WallClimbModule.Mode.Polar) && !this.module.check()) {
         if (computeNextEvent.getBlockPos().getY() >= clientPlayerEntity.getBlockPos().getY()) {
            computeNextEvent.setVoxelShape(VoxelShapeUtils.resolveVoxelShape(computeNextEvent.getVoxelShape(), 1.0E-4, 0.0, 1.0E-4));
         }
      }
   }

   public WallClimbComputeNextListener(WallClimbModule wallClimbModule) {
      super(wallClimbModule, ComputeNextEvent.class);
   }
}
