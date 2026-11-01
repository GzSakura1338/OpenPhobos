package org.phobos.event;

import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import org.phobos.module.movement.JesusModule;

public final class JesusComputeNextListener extends ModuleListener.Safe<JesusModule, ComputeNextEvent> {
   public JesusComputeNextListener(JesusModule jesusModule) {
      super(jesusModule, ComputeNextEvent.class);
   }

   public void handleObject(
      ComputeNextEvent computeNextEvent,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (!computeNextEvent.getBlockState().getFluidState().isEmpty()) {
         if (!(clientPlayerEntity.fallDistance >= 3.0F)) {
            if (clientPlayerEntity.hasVehicle() && clientPlayerEntity.getControllingVehicle() != null) {
               Entity entity = clientPlayerEntity.getControllingVehicle();
               if (entity.getY() > computeNextEvent.getBlockPos().getY()) {
                  computeNextEvent.setVoxelShape(Blocks.STONE.getDefaultState().getCollisionShape(clientWorld, computeNextEvent.getBlockPos()));
               }
            } else if (clientPlayerEntity.getY() > computeNextEvent.getBlockPos().getY()) {
               computeNextEvent.setVoxelShape(Blocks.STONE.getDefaultState().getCollisionShape(clientWorld, computeNextEvent.getBlockPos()));
            }
         }
      }
   }
}
