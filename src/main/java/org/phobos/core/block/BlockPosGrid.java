package org.phobos.core.block;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import net.minecraft.util.math.BlockPos;

public class BlockPosGrid<T> {
   public final T[] objectArray;

   public T[] getObjectArray() {
      return this.objectArray;
   }

   public BlockPosGrid(BlockPosGrid.Builder<T> builder, int i, Function<BlockPos, T> function, T[] objects) {
      this.objectArray = (T[])builder.resolveList(i, function).toArray(objects);
   }

   public BlockPosGrid(int i, Function<BlockPos, T> function, T[] objects) {
      this(new BlockPosGrid.Builder<>(), i, function, objects);
   }

   public static class Builder<T> {
      public boolean testBlockPos(BlockPos blockPos, int i) {
         return blockPos.getSquaredDistance(BlockPos.ZERO) < i * i;
      }

      public void handleBlockNode2(T object, List<T> list) {
         list.add(object);
      }

      public List<T> resolveList(int i, Function<BlockPos, T> function) {
         List<T> list = new ArrayList<>();

         for (int i4 = -i; i4 <= i; i4++) {
            for (int i3 = -i; i3 <= i; i3++) {
               for (int i2 = -i; i2 <= i; i2++) {
                  BlockPos blockPos = new BlockPos(i4, i3, i2);
                  if (this.testBlockPos(blockPos, i)) {
                     this.handleBlockNode2(function.apply(blockPos), list);
                  }
               }
            }
         }

         this.handleList(list);
         return list;
      }

      public void handleList(List<T> list) {
      }
   }

   public static class Pos extends MutableBlockPos {
      public final BlockPos blockPos;

      public Pos(BlockPos blockPos) {
         this.blockPos = blockPos;
      }

      public BlockPos getBlockPos() {
         return this.blockPos;
      }

      public void handleBlockPos(BlockPos blockPos) {
         this.set(this.blockPos.getX() + blockPos.getX(), this.blockPos.getY() + blockPos.getY(), this.blockPos.getZ() + blockPos.getZ());
      }
   }
}
