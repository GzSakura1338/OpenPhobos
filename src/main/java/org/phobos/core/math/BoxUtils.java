package org.phobos.core.math;

import net.minecraft.block.BlockState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public final class BoxUtils {
   public static final double doubleValue = 1.0E-8;

   public static Vec3d[] values(Box box, int i) {
      return values2(box, i, i, i);
   }

   public static Box createBox(Box box, double d) {
      return new Box(box.minX - d, box.minY, box.minZ - d, box.maxX + d, box.maxY, box.maxZ + d);
   }

   public static Box createBox2(Box box, double d, double d2, double d3, double d4, double d5, double d6) {
      return new Box(box.minX - d, box.minY - d2, box.minZ - d3, box.maxX + d4, box.maxY + d5, box.maxZ + d6);
   }

   public static Box createBox3(Vec3d vec3d) {
      return new Box(vec3d, vec3d);
   }

   public static Box createBox4(Box box) {
      return new Box(box.minX + 1.0E-8, box.minY, box.minZ + 1.0E-8, box.maxX - 1.0E-8, box.maxY, box.maxZ - 1.0E-8);
   }

   public static boolean testBox(Box box, Box box2) {
      return box.minX >= box2.minX && box.maxX <= box2.maxX && box.minZ >= box2.minZ && box.maxZ <= box2.maxZ;
   }

   public BoxUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static Box createBox5(BlockPos blockPos, float f) {
      return new Box(blockPos.getX(), blockPos.getY(), blockPos.getZ(), blockPos.getX() + 1, blockPos.getY() + f, blockPos.getZ() + 1);
   }

   public static Box createBox6() {
      return new Box(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
   }

   public static Box resolveBox(ClientWorld clientWorld, BlockState blockState, BlockPos blockPos) {
      return blockState.getOutlineShape(clientWorld, blockPos).getBoundingBox().offset(blockPos);
   }

   public static Vec3d[] values2(Box box, int i, int i2, int i3) {
      double d4 = box.minX;
      double d5 = box.minY;
      double d6 = box.minZ;
      double d7 = box.maxX;
      double d8 = box.maxY;
      double d9 = box.maxZ;
      int i7 = (i + 1) * (i2 + 1) * (i3 + 1);
      Vec3d[] vec3ds = new Vec3d[i7];
      int i8 = 0;

      for (int i6 = 0; i6 <= i; i6++) {
         for (int i5 = 0; i5 <= i2; i5++) {
            for (int i4 = 0; i4 <= i3; i4++) {
               double d = d4 + (d7 - d4) * i6 / i;
               double d2 = d5 + (d8 - d5) * i5 / i2;
               double d3 = d6 + (d9 - d6) * i4 / i3;
               vec3ds[i8++] = new Vec3d(d, d2, d3);
            }
         }
      }

      return vec3ds;
   }

   public static Box createBox7(Iterable<BlockPos> iterable) {
      int i = Integer.MAX_VALUE;
      int i2 = Integer.MAX_VALUE;
      int i3 = Integer.MAX_VALUE;
      int i4 = Integer.MIN_VALUE;
      int i5 = Integer.MIN_VALUE;
      int i6 = Integer.MIN_VALUE;

      for (BlockPos blockPos : iterable) {
         i = Math.min(i, blockPos.getX());
         i2 = Math.min(i2, blockPos.getY());
         i3 = Math.min(i3, blockPos.getZ());
         i4 = Math.max(i4, blockPos.getX() + 1);
         i5 = Math.max(i5, blockPos.getY() + 1);
         i6 = Math.max(i6, blockPos.getZ() + 1);
      }

      return new Box(i, i2, i3, i4, i5, i6);
   }

   public static boolean testBox2(Box box, Box box2) {
      return box.minX <= box2.maxX && box.maxX >= box2.minX && box.minY <= box2.maxY && box.maxY >= box2.minY && box.minZ <= box2.maxZ && box.maxZ >= box2.minZ;
   }

   public static Box createBox8(BlockPos blockPos, Direction direction) {
      Box box = new Box(blockPos);

      return switch (direction) {
         case SOUTH -> new Box(box.minX, box.minY, box.maxZ, box.maxX, box.maxY, box.maxZ);
         case NORTH -> new Box(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.minZ);
         case EAST -> new Box(box.maxX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ);
         case WEST -> new Box(box.minX, box.minY, box.minZ, box.minX, box.maxY, box.maxZ);
         case UP -> new Box(box.minX, box.maxY, box.minZ, box.maxX, box.maxY, box.maxZ);
         case DOWN -> new Box(box.minX, box.minY, box.minZ, box.maxX, box.minY, box.maxZ);
         default -> throw new MatchException(null, null);
      };
   }

   public static Box createBox9(BlockPos blockPos) {
      return new Box(blockPos);
   }

   public static Vec3d[] values3(Box box) {
      return new Vec3d[]{
         new Vec3d(box.minX, box.minY, box.minZ),
         new Vec3d(box.minX, box.minY, box.maxZ),
         new Vec3d(box.minX, box.maxY, box.minZ),
         new Vec3d(box.minX, box.maxY, box.maxZ),
         new Vec3d(box.maxX, box.minY, box.minZ),
         new Vec3d(box.maxX, box.minY, box.maxZ),
         new Vec3d(box.maxX, box.maxY, box.minZ),
         new Vec3d(box.maxX, box.maxY, box.maxZ)
      };
   }

   public static Box createBox10(Box box) {
      return new Box(box.minX - 1.0E-8, box.minY, box.minZ - 1.0E-8, box.maxX + 1.0E-8, box.maxY, box.maxZ + 1.0E-8);
   }
}
