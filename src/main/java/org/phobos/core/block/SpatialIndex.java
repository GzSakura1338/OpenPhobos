package org.phobos.core.block;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;

public final class SpatialIndex {
   public static final Vec3i[] vec3iArray = new Vec3i[]{
      new Vec3i(0, 0, 0),
      new Vec3i(-1, 0, 0),
      new Vec3i(1, 0, 0),
      new Vec3i(0, 0, 1),
      new Vec3i(0, 0, -1),
      new Vec3i(-2, 0, 0),
      new Vec3i(2, 0, 0),
      new Vec3i(0, 0, 2),
      new Vec3i(0, 0, -2),
      new Vec3i(0, -1, 0),
      new Vec3i(1, -1, 0),
      new Vec3i(-1, -1, 0),
      new Vec3i(0, -1, 1),
      new Vec3i(0, -1, -1),
      new Vec3i(0, -2, 0),
      new Vec3i(2, 0, 1),
      new Vec3i(2, 0, -1),
      new Vec3i(-2, 0, 1),
      new Vec3i(-2, 0, -1),
      new Vec3i(1, 0, 2),
      new Vec3i(-1, 0, 2),
      new Vec3i(1, 0, -2),
      new Vec3i(-1, 0, -2),
      new Vec3i(3, 0, 0),
      new Vec3i(-3, 0, 0),
      new Vec3i(0, 0, 3),
      new Vec3i(0, 0, -3),
      new Vec3i(1, -2, 0),
      new Vec3i(-1, -2, 0),
      new Vec3i(0, -2, 1),
      new Vec3i(0, -2, -1),
      new Vec3i(2, -2, 0),
      new Vec3i(-2, -2, 0),
      new Vec3i(0, -2, 2),
      new Vec3i(0, -2, -2),
      new Vec3i(2, -2, 1),
      new Vec3i(2, -2, 2),
      new Vec3i(2, -2, -1),
      new Vec3i(2, -2, -2),
      new Vec3i(-2, -2, 1),
      new Vec3i(-2, -2, 2),
      new Vec3i(-2, -2, -1),
      new Vec3i(-2, -2, -2)
   };
   public static final Vec3i[] vec3iArray2 = new Vec3i[]{
      new Vec3i(0, 0, 0),
      new Vec3i(-1, 0, 0),
      new Vec3i(1, 0, 0),
      new Vec3i(0, 0, 1),
      new Vec3i(0, 0, -1),
      new Vec3i(-2, 0, 0),
      new Vec3i(2, 0, 0),
      new Vec3i(0, 0, 2),
      new Vec3i(0, 0, -2),
      new Vec3i(0, 1, 0),
      new Vec3i(1, 1, 0),
      new Vec3i(-1, 1, 0),
      new Vec3i(0, 1, 1),
      new Vec3i(0, 1, -1),
      new Vec3i(0, 2, 0),
      new Vec3i(2, 0, 1),
      new Vec3i(2, 0, -1),
      new Vec3i(-2, 0, 1),
      new Vec3i(-2, 0, -1),
      new Vec3i(1, 0, 2),
      new Vec3i(-1, 0, 2),
      new Vec3i(1, 0, -2),
      new Vec3i(-1, 0, -2),
      new Vec3i(3, 0, 0),
      new Vec3i(-3, 0, 0),
      new Vec3i(0, 0, 3),
      new Vec3i(0, 0, -3),
      new Vec3i(1, 2, 0),
      new Vec3i(-1, 2, 0),
      new Vec3i(0, 2, 1),
      new Vec3i(0, 2, -1),
      new Vec3i(2, 2, 0),
      new Vec3i(-2, 2, 0),
      new Vec3i(0, 2, 2),
      new Vec3i(0, 2, -2),
      new Vec3i(2, 2, 1),
      new Vec3i(2, 2, 2),
      new Vec3i(2, 2, -1),
      new Vec3i(2, 2, -2),
      new Vec3i(-2, 2, 1),
      new Vec3i(-2, 2, 2),
      new Vec3i(-2, 2, -1),
      new Vec3i(-2, 2, -2)
   };
   public static final SpatialIndex.Pair[] pairArray = new SpatialIndex.Pair[]{
      new SpatialIndex.Pair(new Vec3i(0, -1, 0), Direction.DOWN),
      new SpatialIndex.Pair(new Vec3i(-1, 0, 0), Direction.EAST),
      new SpatialIndex.Pair(new Vec3i(1, 0, 0), Direction.WEST),
      new SpatialIndex.Pair(new Vec3i(0, 0, 1), Direction.NORTH),
      new SpatialIndex.Pair(new Vec3i(0, 0, -1), Direction.SOUTH)
   };
   public static final SpatialIndex.Pair[] pairArray2 = new SpatialIndex.Pair[]{
      new SpatialIndex.Pair(new Vec3i(0, -1, 0), Direction.UP),
      new SpatialIndex.Pair(new Vec3i(-1, 0, 0), Direction.EAST),
      new SpatialIndex.Pair(new Vec3i(1, 0, 0), Direction.WEST),
      new SpatialIndex.Pair(new Vec3i(0, 0, 1), Direction.NORTH),
      new SpatialIndex.Pair(new Vec3i(0, 0, -1), Direction.SOUTH)
   };

   public SpatialIndex() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public enum Down implements SpatialIndex.IHelping {
      INSTANCE;

      @Override
      public Vec3i[] values2() {
         return SpatialIndex.vec3iArray;
      }

      @Override
      public SpatialIndex.Node resolveNode(BlockPos pos, ClientWorld world, Set<BlockPos> placed, BiPredicate<BlockPos, Direction> function) {
         for (SpatialIndex.Pair offset : SpatialIndex.pairArray2) {
            BlockPos var1;
            if ((world.getBlockState(var1 = pos.add(offset.offset())).isSolid() || placed.contains(var1)) && function.test(var1, offset.direction())) {
               return new SpatialIndex.Node(var1, offset.direction());
            }
         }

         return null;
      }
   }

   public interface IHelping {
      SpatialIndex.Node resolveNode(BlockPos var1, ClientWorld var2, Set<BlockPos> var3, BiPredicate<BlockPos, Direction> var4);

      default SpatialIndex.Node resolveNode2(BlockPos blockPos, ClientWorld clientWorld, Set<BlockPos> set, BiPredicate<BlockPos, Direction> biPredicate) {
         for (Vec3i vec3i : this.values2()) {
            SpatialIndex.Node node = this.resolveNode(blockPos.add(vec3i), clientWorld, set, biPredicate);
            if (node != null) {
               return node;
            }
         }

         return null;
      }

      default List<BlockPos> resolveList(BlockPos blockPos, ClientWorld clientWorld, BiPredicate<BlockPos, Direction> biPredicate, int i) {
         List<BlockPos> list = new ArrayList<>();
         Set<BlockPos> set = new HashSet<>();
         if (clientWorld.getBlockState(blockPos).isSolid()) {
            return list;
         }

         SpatialIndex.Node node = this.resolveNode2(blockPos, clientWorld, set, biPredicate);
         int i2 = 0;
         if (node != null) {
            list.add(node.pos);
            set.add(node.pos.offset(node.direction));

            while (true) {
               i2++;
               if (i2 > i || node == null || node.pos.offset(node.direction).equals(blockPos)) {
                  break;
               }

               node = this.resolveNode2(blockPos, clientWorld, set, biPredicate);
               if (node != null) {
                  list.add(node.pos);
                  set.add(node.pos.offset(node.direction));
               }

               i2++;
            }
         }

         return list;
      }

      Vec3i[] values2();
   }

   public record Node(BlockPos pos, Direction direction) {
   }

   public record Pair(Vec3i offset, Direction direction) {
   }

   public enum Up implements SpatialIndex.IHelping {
      INSTANCE;

      @Override
      public Vec3i[] values2() {
         return SpatialIndex.vec3iArray2;
      }

      @Override
      public SpatialIndex.Node resolveNode(BlockPos pos, ClientWorld world, Set<BlockPos> placed, BiPredicate<BlockPos, Direction> function) {
         for (SpatialIndex.Pair offset : SpatialIndex.pairArray) {
            BlockPos var1;
            if ((world.getBlockState(var1 = pos.add(offset.offset())).isSolid() || placed.contains(var1)) && function.test(var1, offset.direction())) {
               return new SpatialIndex.Node(var1, offset.direction());
            }
         }

         return null;
      }
   }
}
