package org.phobos.event;

import net.minecraft.util.math.BlockPos;
import org.phobos.core.event.events.CancellableEvent;

public class BreakBlockEvent extends CancellableEvent {
   public final BlockPos blockPos;

   public BlockPos getBlockPos() {
      return this.blockPos;
   }

   public BreakBlockEvent(BlockPos blockPos) {
      this.blockPos = blockPos;
   }
}
