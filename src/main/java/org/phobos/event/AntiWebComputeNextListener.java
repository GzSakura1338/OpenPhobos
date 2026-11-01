package org.phobos.event;

import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import org.phobos.core.block.BlockUtils;
import org.phobos.module.movement.AntiWebModule;
import org.phobos.module.movement.AntiWebModule;

public final class AntiWebComputeNextListener extends ModuleListener.Safe<AntiWebModule, ComputeNextEvent> {
   public AntiWebComputeNextListener(AntiWebModule antiWebModule) {
      super(antiWebModule, ComputeNextEvent.class);
   }

   public void handleObject(
      ComputeNextEvent computeNextEvent,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (computeNextEvent.getBlockState().isOf(Blocks.COBWEB) && this.module.mode.getValue().equals(AntiWebModule.Mode.Collide)) {
         computeNextEvent.setVoxelShape(BlockUtils.resolveVoxelShape(clientWorld, computeNextEvent.getBlockPos()));
      }
   }
}
