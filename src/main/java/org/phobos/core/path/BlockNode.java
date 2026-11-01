package org.phobos.core.path;

import java.util.Objects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import org.jetbrains.annotations.NotNull;
import org.phobos.api.PathNode;
import org.phobos.core.block.MutableBlockPos;

public class BlockNode implements PathNode<BlockNode> {
   public final MutableBlockPos mutableBlockPos;
   public final BlockPos blockPos;
   public final BlockNode[] blockNodeArray = new BlockNode[6];

   @Override
   public String toString() {
      return "BlockNode(offsetToCenter=" + this.getBlockPos() + ", current=" + this.getMutableBlockPos() + ")";
   }

   public BlockPos getBlockPos() {
      return this.blockPos;
   }

   public void handleBlockPos(BlockPos blockPos) {
      this.mutableBlockPos.set(blockPos.getX() + this.blockPos.getX(), blockPos.getY() + this.blockPos.getY(), blockPos.getZ() + this.blockPos.getZ());
   }

   public MutableBlockPos getMutableBlockPos() {
      return this.mutableBlockPos;
   }

   public double computePathNode(BlockNode blockNode) {
      return blockNode.getMutableBlockPos().getSquaredDistance(this.mutableBlockPos);
   }

   public BlockNode[] getBlockNodeArray() {
      return this.blockNodeArray;
   }

   @Override
   public int value() {
      return ChunkSectionPos.getSectionCoord(this.mutableBlockPos.getZ());
   }

   @Override
   public int value2() {
      return ChunkSectionPos.getSectionCoord(this.mutableBlockPos.getX());
   }

   public int compareTo(@NotNull BlockNode blockNode) {
      int i = Double.compare(this.blockPos.getManhattanDistance(BlockPos.ZERO), blockNode.blockPos.getManhattanDistance(BlockPos.ZERO));
      if (i == 0) {
         i = Integer.compare(this.blockPos.getY(), blockNode.blockPos.getY());
         if (i == 0) {
            i = this.blockPos.compareTo(blockNode.blockPos);
         }
      }

      return i;
   }

   @Override
   public int hashCode() {
      return Objects.hash(this.getBlockPos());
   }

   public BlockNode(BlockPos blockPos) {
      this.mutableBlockPos = new MutableBlockPos();
      this.blockPos = blockPos;
   }

   @Override
   public boolean equals(Object object) {
      if (this == object) {
         return true;
      } else {
         return object instanceof BlockNode blockNode ? Objects.equals(this.getBlockPos(), blockNode.getBlockPos()) : false;
      }
   }
}
