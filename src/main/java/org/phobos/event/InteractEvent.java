package org.phobos.event;

import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import org.phobos.core.event.events.CancellableEvent;

public class InteractEvent extends CancellableEvent {
   public final BlockHitResult blockHitResult;
   public final ItemStack itemStack;

   public BlockHitResult getBlockHitResult() {
      return this.blockHitResult;
   }

   public ItemStack getItemStack() {
      return this.itemStack;
   }

   public InteractEvent(BlockHitResult blockHitResult, ItemStack itemStack) {
      this.blockHitResult = blockHitResult;
      this.itemStack = itemStack;
   }

   public BlockPos getBlockPos() {
      return this.blockHitResult.getBlockPos().offset(this.blockHitResult.getSide());
   }
}
