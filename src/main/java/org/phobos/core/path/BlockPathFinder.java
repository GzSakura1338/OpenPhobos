package org.phobos.core.path;

import java.util.function.BiPredicate;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class BlockPathFinder extends AStarPathFinder<BlockNode> {
   public final BiPredicate<BlockPos, @Nullable BlockPos> blockPosBiPredicate;
   public final int intValue;
   public final BiPredicate<BlockPos, BlockPos> blockPosBiPredicate2;
   public final BlockPos blockPos;

   public boolean testPathNode2(BlockNode blockNode, BlockNode blockNode2) {
      blockNode2.handleBlockPos(this.blockPos);
      return this.testPathNode(blockNode2, blockNode)
         || this.blockPosBiPredicate2.test(blockNode.getMutableBlockPos(), blockNode2.getMutableBlockPos());
   }

   public boolean testPathNode(BlockNode blockNode, @Nullable BlockNode blockNode2) {
      return this.blockPosBiPredicate.test(blockNode.getMutableBlockPos(), blockNode2 == null ? null : blockNode2.getMutableBlockPos());
   }

   public BlockPathFinder(
      BlockNode blockNode, BiPredicate<BlockPos, BlockPos> biPredicate, BiPredicate<BlockPos, @Nullable BlockPos> biPredicate2, BlockPos blockPos, int i
   ) {
      super(blockNode, null);
      this.blockPosBiPredicate2 = biPredicate;
      this.blockPosBiPredicate = biPredicate2;
      this.blockPos = blockPos;
      this.intValue = i;
   }

   public void handlePathNode(BlockNode blockNode, BlockNode blockNode2) {
      double d = this.map2.getOrDefault(blockNode, Double.POSITIVE_INFINITY) + this.computePathNode(blockNode, blockNode2);
      if (d < this.map2.getOrDefault(blockNode2, Double.POSITIVE_INFINITY) && d <= this.intValue) {
         this.handlePathNode2(blockNode, blockNode2, d);
      }
   }
}
