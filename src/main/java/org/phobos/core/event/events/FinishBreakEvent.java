package org.phobos.core.event.events;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.phobos.api.BlockBreakContext;

public class FinishBreakEvent extends CancellableEvent implements BlockBreakContext {
   public final BlockPos blockPos;
   public final Direction direction;

   public FinishBreakEvent(BlockPos blockPos, Direction direction) {
      this.blockPos = blockPos;
      this.direction = direction;
   }

   @Override
   public Direction getDirection() {
      return this.direction;
   }

   @Override
   public BlockPos getBlockPos() {
      return this.blockPos;
   }
}
