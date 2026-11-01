package org.phobos.event;

import java.util.List;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import org.phobos.module.combat.DoubleHandModule;
import org.phobos.module.combat.DoubleHandModule;

public final class DoubleHandSetBlockStateListener extends ModuleListener.Safe<DoubleHandModule, SetBlockStateEvent> {
   public void handleObject(
      SetBlockStateEvent setBlockStateEvent,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (this.module.mode.testObject2(DoubleHandModule.Mode.Legit) && this.module.anchor.getValue() && this.module.anchorWorld.getValue()) {
         BlockPos blockPos = setBlockStateEvent.pos();
         BlockState blockState = clientWorld.getBlockState(blockPos);
         if (blockState.isAir()) {
            BlockState blockState2 = setBlockStateEvent.state();
            if (blockState2.isOf(Blocks.RESPAWN_ANCHOR)) {
               float f = this.phobosClient.getDamageCalculator().computeEntity3(clientPlayerEntity, clientWorld, 5.0F, blockPos.toCenterPos(), List.of(blockPos));
               if (this.module.testFloat(f)) {
                  this.module.run();
               }
            }
         }
      }
   }

   public DoubleHandSetBlockStateListener(DoubleHandModule doubleHandModule) {
      super(doubleHandModule, SetBlockStateEvent.class);
   }
}
