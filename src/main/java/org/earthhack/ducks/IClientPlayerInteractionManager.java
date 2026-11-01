package org.earthhack.ducks;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public interface IClientPlayerInteractionManager {
   boolean updateBreaking(BlockPos var1, Direction var2);
}
