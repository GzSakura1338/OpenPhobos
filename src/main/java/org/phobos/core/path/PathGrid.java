package org.phobos.core.path;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.phobos.core.block.BlockPosGrid;

public class PathGrid extends BlockPosGrid.Builder<BlockNode> {
   public static final int[] intArray = new int[]{5, 2, 1, 4, 3, 0};
   public BlockNode blockNode;
   public final Map<BlockPos, BlockNode> blockPosMap = new HashMap<>();
   public static final Direction[] directionArray = new Direction[]{
      Direction.DOWN, Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST, Direction.UP
   };

   public void handleBlockNode(BlockNode blockNode) {
      for (int i = 0; i < directionArray.length; i++) {
         Direction direction = directionArray[i];
         blockNode.getMutableBlockPos().set(blockNode.getBlockPos());
         blockNode.getMutableBlockPos().handleDirection(direction);
         BlockNode blockNode2 = this.blockPosMap.get(blockNode.getMutableBlockPos());
         if (blockNode2 != null) {
            blockNode.getBlockNodeArray()[i] = blockNode2;
            blockNode2.getBlockNodeArray()[intArray[i]] = blockNode;
         }
      }
   }

   public void handleBlockNode2(BlockNode blockNode, List<BlockNode> list) {
      super.handleBlockNode2(blockNode, list);
      this.blockPosMap.put(blockNode.getBlockPos(), blockNode);
      if (blockNode.getBlockPos().equals(BlockPos.ZERO)) {
         this.blockNode = blockNode;
      }

      this.handleBlockNode(blockNode);
   }

   @Override
   public boolean testBlockPos(BlockPos blockPos, int i) {
      return blockPos.getManhattanDistance(BlockPos.ZERO) <= i;
   }

   public BlockNode getBlockNode() {
      return this.blockNode;
   }

   public Map<BlockPos, BlockNode> getBlockPosMap() {
      return this.blockPosMap;
   }
}
