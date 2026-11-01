package org.phobos.event;

import net.minecraft.block.BlockState;
import net.minecraft.block.ButtonBlock;
import net.minecraft.block.LeverBlock;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import org.phobos.api.AreaScanner;
import org.phobos.core.placement.DefaultDirectionResolver;
import org.phobos.module.misc.AutoInteractModule;
import org.phobos.type.SingleMultiEnum;

public final class AutoInteractPlayerTickListener extends ModuleListener.Safe<AutoInteractModule, PlayerTickEvent> {
   public AutoInteractPlayerTickListener(AutoInteractModule autoInteractModule) {
      super(autoInteractModule, PlayerTickEvent.class);
   }

   public void handleObject(
      PlayerTickEvent playerTickEvent,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (--this.module.intValue < this.module.delay.getValue()) {
         AreaScanner.Area area = new AreaScanner.Area();
         area.handleVec3d(clientPlayerEntity.getEyePos(), this.module.range.getValue(), blockPosx -> {
            BlockState blockState = clientWorld.getBlockState(blockPosx);
            return blockState.isAir() ? false : blockState.getBlock() instanceof ButtonBlock || blockState.getBlock() instanceof LeverBlock;
         });

         for (BlockPos blockPos : area) {
            this.phobosClient
               .getInteractionManager()
               .handleBlockPos2(blockPos, clientPlayerEntity, clientWorld, DefaultDirectionResolver.defaultDirectionResolver, blockHitResult -> {
                  for (int i = 0; i < this.module.iterations.getValue(); i++) {
                     clientPlayerInteractionManager.interactBlock(clientPlayerEntity, Hand.MAIN_HAND, blockHitResult);
                     if (this.module.swing.getValue()) {
                        clientPlayerEntity.swingHand(Hand.MAIN_HAND);
                     }
                  }
               });
            if (this.module.mode.getValue().equals(SingleMultiEnum.Single)) {
               break;
            }
         }

         this.module.intValue = this.module.delay.getValue();
      }
   }
}
