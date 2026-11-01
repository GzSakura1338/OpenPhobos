package org.phobos.event;

import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.mutable.MutableObject;
import org.phobos.module.player.AutoToolModule;

public final class AutoToolPlayerTickListener extends ModuleListener.Safe<AutoToolModule, PlayerTickEvent> {
   public AutoToolPlayerTickListener(AutoToolModule autoToolModule) {
      super(autoToolModule, PlayerTickEvent.class);
   }

   public void handleObject(
      PlayerTickEvent playerTickEvent,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      if (!this.module.silent.getValue() && mc.crosshairTarget instanceof BlockHitResult blockHitResult) {
         if (clientPlayerInteractionManager.isBreakingBlock()) {
            BlockPos blockPos = blockHitResult.getBlockPos();
            BlockState blockState = clientWorld.getBlockState(blockPos);
            this.phobosClient.getInventoryManager().handleConsumer2(itemSwitcher -> {
               MutableObject<Slot> mutableObject = new MutableObject(null);
               MutableObject<Float> mutableObject2 = new MutableObject(0.0F);
               itemSwitcher.resolveObject(slotx -> {
                  if (!this.module.inventory.getValue() && !itemSwitcher.testSlot4(slotx)) {
                     return null;
                  }

                  ItemStack itemStack = slotx.getStack();
                  float f = this.module.computeBlockPos(blockPos, blockState, clientWorld, clientPlayerEntity, itemStack);
                  if (f > (Float)mutableObject2.getValue()) {
                     mutableObject.setValue(slotx);
                     mutableObject2.setValue(f);
                  }

                  return null;
               });
               Slot slot = (Slot)mutableObject.getValue();
               if (slot != null) {
                  itemSwitcher.resolveSwitchResult(Hand.MAIN_HAND, slot, 2);
               }
            });
         }
      }
   }
}
