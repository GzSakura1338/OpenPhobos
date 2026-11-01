package org.phobos.core.block;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import org.phobos.core.math.BoxUtils;

public final class BlockPosUtils {
   public static BlockPos createBlockPos(Position position) {
      return new BlockPos((int)Math.floor(position.getX()), (int)Math.floor(position.getY()), (int)Math.floor(position.getZ()));
   }

   public static Set<BlockPos> resolveSet(Box box, double d) {
      double d2 = box.minY - 1.0;
      if (d2 > Math.floor(d2) + d) {
         d2++;
      }

      return resolveSet2(box, d2);
   }

   public static Set<BlockPos> resolveSet2(Box box, double d) {
      Set<BlockPos> set = new HashSet<>();
      handleSet2(set, box, d);
      return set;
   }

   public static BlockPos createBlockPos2(Position position, int i) {
      return new BlockPos((int)Math.floor(position.getX()), (int)Math.floor(position.getY()) - i, (int)Math.floor(position.getZ()));
   }

   public BlockPosUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static Vec3d createVec3d(Box box, Vec3d vec3d) {
      double d = box.minX - vec3d.x > vec3d.x - box.maxX ? box.minX : box.maxX;
      double d2 = box.minY - vec3d.y > vec3d.y - box.maxY ? box.minY : box.maxY;
      double d3 = box.minZ - vec3d.z > vec3d.z - box.maxZ ? box.minZ : box.maxZ;
      return new Vec3d(d, d2, d3);
   }

   public static void handleSet(Set<BlockPos> set, Entity entity, double d) {
      Box box = BoxUtils.createBox4(entity.getBoundingBox());
      set.add(BlockPos.ofFloored(box.minX, d, box.minZ));
      set.add(BlockPos.ofFloored(box.maxX, d, box.minZ));
      set.add(BlockPos.ofFloored(box.minX, d, box.maxZ));
      set.add(BlockPos.ofFloored(box.maxX, d, box.maxZ));
   }

   public static Set<BlockPos> resolveSet3(Entity entity, double d) {
      Set<BlockPos> set = new HashSet<>();
      handleSet(set, entity, d);
      return set;
   }

   public static boolean testDouble(double d, double d2, double d3) {
      return d2 >= d ? false : d - d2 >= d3;
   }

   public static double computeMutable(Mutable mutable, World world) {
      VoxelShape voxelShape = world.getBlockState(mutable).getCollisionShape(world, mutable);
      mutable.setY(mutable.getY() - 1);
      VoxelShape voxelShape2 = world.getBlockState(mutable).getCollisionShape(world, mutable);
      mutable.setY(mutable.getY() + 1);
      double d = mutable.getY() - 1 + voxelShape2.getMax(Axis.Y);
      double d2 = mutable.getY() + voxelShape.getMax(Axis.Y);
      return Math.max(mutable.getY(), Math.max(d, d2));
   }

   public static Set<BlockPos> resolveSet4(BlockPos blockPos, BlockPos blockPos2) {
      Set<BlockPos> set = new HashSet<>();

      for (BlockPos blockPos3 : BlockPos.iterate(blockPos, blockPos2)) {
         set.add(blockPos3);
      }

      return set;
   }

   public static Box createBox(Entity entity, double d) {
      return new Box(
         MathHelper.floor(entity.getX() - d),
         MathHelper.floor(entity.getY() - d),
         MathHelper.floor(entity.getZ() - d),
         MathHelper.floor(entity.getX() + d),
         MathHelper.floor(entity.getY() + d),
         MathHelper.floor(entity.getZ() + d)
      );
   }

   public static Box createBox2(Vec3d vec3d, double d) {
      return new Box(
         MathHelper.floor(vec3d.getX() - d),
         MathHelper.floor(vec3d.getY() - d),
         MathHelper.floor(vec3d.getZ() - d),
         MathHelper.floor(vec3d.getX() + d),
         MathHelper.floor(vec3d.getY() + d),
         MathHelper.floor(vec3d.getZ() + d)
      );
   }

   public static void handleSet2(Set<BlockPos> set, Box box, double d) {
      set.add(BlockPos.ofFloored(box.minX, d, box.minZ));
      set.add(BlockPos.ofFloored(box.maxX, d, box.minZ));
      set.add(BlockPos.ofFloored(box.minX, d, box.maxZ));
      set.add(BlockPos.ofFloored(box.maxX, d, box.maxZ));
   }

   public static Set<BlockPos> resolveSet5(Entity entity) {
      Set<BlockPos> set = new HashSet<>();
      Box box = BoxUtils.createBox4(entity.getBoundingBox());
      int i4 = (int)Math.floor(box.minX);
      int i5 = (int)Math.floor(box.minY);
      int i6 = (int)Math.floor(box.minZ);
      int i7 = (int)Math.floor(box.maxX);
      int i8 = (int)Math.floor(box.maxY);
      int i9 = (int)Math.floor(box.maxZ);

      for (int i3 = i4; i3 <= i7; i3++) {
         for (int i2 = i5; i2 <= i8; i2++) {
            for (int i = i6; i <= i9; i++) {
               set.add(new BlockPos(i3, i2, i));
            }
         }
      }

      return set;
   }

   public static Set<BlockPos> resolveSet6(Box box) {
      Set<BlockPos> set = new HashSet<>();
      int i4 = (int)Math.floor(box.minX);
      int i5 = (int)Math.floor(box.minY);
      int i6 = (int)Math.floor(box.minZ);
      int i7 = (int)Math.floor(box.maxX);
      int i8 = (int)Math.floor(box.maxY);
      int i9 = (int)Math.floor(box.maxZ);

      for (int i3 = i4; i3 <= i7; i3++) {
         for (int i2 = i5; i2 <= i8; i2++) {
            for (int i = i6; i <= i9; i++) {
               set.add(new BlockPos(i3, i2, i));
            }
         }
      }

      return set;
   }

   public static Set<BlockPos> resolveSet7(Entity entity, double d) {
      double d2 = entity.getBoundingBox().minY - 1.0;
      if (d2 > Math.floor(d2) + d) {
         d2++;
      }

      return resolveSet3(entity, d2);
   }
}
