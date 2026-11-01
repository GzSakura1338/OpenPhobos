package org.phobos.api;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public interface BlockBreakContext {
   Direction getDirection();

   BlockPos getBlockPos();

   void setCancelled(boolean var1);
}
