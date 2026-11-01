package org.phobos.event;

import java.util.List;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import org.phobos.module.combat.DoubleHandModule;
import org.phobos.module.combat.DoubleHandModule;

public final class DoubleHandInteractListener extends ModuleListener.Safe<DoubleHandModule, InteractEvent> {
   public void handleObject(
      InteractEvent interactEvent,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (this.module.mode.testObject2(DoubleHandModule.Mode.Legit) && this.module.anchor.getValue()) {
         BlockPos blockPos = interactEvent.getBlockHitResult().getBlockPos();
         BlockState blockState = clientWorld.getBlockState(blockPos);
         if (blockState.isOf(Blocks.RESPAWN_ANCHOR)) {
            int i = (Integer)blockState.get(RespawnAnchorBlock.CHARGES);
            ItemStack itemStack = clientPlayerEntity.getMainHandStack();
            if (i > 0 && (!itemStack.isOf(Items.GLOWSTONE) || i >= 4)) {
               float f = this.phobosClient.getDamageCalculator().computeEntity3(clientPlayerEntity, clientWorld, 5.0F, blockPos.toCenterPos(), List.of(blockPos));
               if (this.module.testFloat(f)) {
                  this.module.run();
               }
            }
         }
      }
   }

   public DoubleHandInteractListener(DoubleHandModule doubleHandModule) {
      super(doubleHandModule, InteractEvent.class);
   }
}
