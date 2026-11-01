package org.phobos.core.block;

import net.minecraft.block.Block;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public final class BlockDirectionUtils {
   @Nullable
   public static Direction resolveDirection(ClientWorld clientWorld, Block block, BlockPos blockPos, boolean flag, Direction... directions) {
      for (Direction direction : directions) {
         BlockPos blockPos2 = blockPos.offset(direction);
         if (!BlockUtils.testBlockPos11(blockPos2, block.getDefaultState().getCollisionShape(clientWorld, blockPos2), clientWorld, flag)) {
            return direction;
         }
      }

      return null;
   }

   public BlockDirectionUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
