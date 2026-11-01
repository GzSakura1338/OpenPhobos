package org.phobos.core.event.events;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class BlockUpdateEvent extends CancellableEvent {
   public final BlockState blockState;
   public final BlockPos blockPos;

   public BlockUpdateEvent(BlockPos blockPos, BlockState blockState) {
      this.blockPos = blockPos;
      this.blockState = blockState;
   }

   public BlockPos getBlockPos() {
      return this.blockPos;
   }

   public BlockState getBlockState() {
      return this.blockState;
   }
}
