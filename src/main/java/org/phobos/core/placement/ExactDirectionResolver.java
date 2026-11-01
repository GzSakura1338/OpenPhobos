package org.phobos.core.placement;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Blocks;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.phobos.core.world.RaycastUtils;

public class ExactDirectionResolver extends DirectionResolver {
   public static final double doubleValue = 0.4;
   public static ExactDirectionResolver exactDirectionResolver = new ExactDirectionResolver();
   public static final double doubleValue2 = 1.62;
   public final double[][] doubleArray = new double[3][];
   public static final double doubleValue3 = 2.0E-4;

   public Vec3d resolveVec3d(Vec3d vec3d, Vec3d vec3d2, double d) {
      double d3 = vec3d2.getX() - vec3d.getX();
      double d4 = vec3d2.getY() - vec3d.getY();
      double d5 = vec3d2.getZ() - vec3d.getZ();
      if (d3 * d3 < 1.0E-7F) {
         return null;
      }

      double d2 = (d - vec3d.getX()) / d3;
      return d2 >= 0.0 && d2 <= 1.0 ? new Vec3d(vec3d.getX() + d3 * d2, vec3d.getY() + d4 * d2, vec3d.getZ() + d5 * d2) : null;
   }

   public boolean testPlayerEntity(PlayerEntity playerEntity, BlockPos blockPos, float f, float f2, double d) {
      Box box = new Box(blockPos);
      List<Vector3f> list = new ArrayList<>(List.of(new Vector3f(f, f2, 0.0F), new Vector3f(playerEntity.getYaw(), playerEntity.getPitch(), 0.0F)));
      double[] doubles = this.values(playerEntity);
      double d4 = Double.MAX_VALUE;
      double d5 = Double.MIN_VALUE;

      for (double d2 : doubles) {
         d4 = Math.min(d4, d2);
         d5 = Math.max(d5, d2);
      }

      Box box2 = new Box(playerEntity.getX(), playerEntity.getY() + d4, playerEntity.getZ(), playerEntity.getX(), playerEntity.getY() + d5, playerEntity.getZ())
         .expand(0.03);
      if (box2.intersects(box)) {
         return true;
      }

      for (double d3 : doubles) {
         for (Vector3f vector3f : list) {
            Vec3d vec3d = playerEntity.getPos().add(0.0, d3, 0.0);
            Vec3d vec3d2 = RaycastUtils.createVec3d2(vector3f.x, vector3f.y);
            Pair<Vec3d, Direction> pair = this.createPair(box, vec3d, vec3d.add(vec3d2.multiply(d)));
            if (pair.getLeft() != null) {
               return true;
            }
         }
      }

      return false;
   }

   public boolean testBox(Box box, Box box2) {
      return box2.maxX - 1.0E-7 > box.minX
         && box2.minX + 1.0E-7 < box.maxX
         && box2.maxY - 1.0E-7 > box.minY
         && box2.minY + 1.0E-7 < box.maxY
         && box2.maxZ - 1.0E-7 > box.minZ
         && box2.minZ + 1.0E-7 < box.maxZ;
   }

   public boolean testBox2(Box box, Vec3d vec3d) {
      return vec3d != null && vec3d.getY() >= box.minY && vec3d.getY() <= box.maxY && vec3d.getZ() >= box.minZ && vec3d.getZ() <= box.maxZ;
   }

   @Override
   public boolean testBlockPos(BlockPos blockPos, Direction direction, ClientWorld clientWorld, PlayerEntity playerEntity) {
      if (clientWorld.getBlockState(blockPos).getBlock() == Blocks.SCAFFOLDING) {
         return true;
      }

      Box box = this.resolveBox(blockPos, clientWorld);
      Box box2 = new Box(
            playerEntity.getX(), playerEntity.getY() + 0.4, playerEntity.getZ(), playerEntity.getX(), playerEntity.getY() + 1.62, playerEntity.getZ()
         )
         .expand(2.0E-4);
      if (this.testBox(box2, box)) {
         return true;
      }

      switch (direction) {
         case NORTH:
            if (!(box2.minZ > box.minZ)) {
               return true;
            }
            break;
         case SOUTH:
            if (!(box2.maxZ < box.maxZ)) {
               return true;
            }
            break;
         case EAST:
            if (!(box2.maxX < box.maxX)) {
               return true;
            }
            break;
         case WEST:
            if (!(box2.minX > box.minX)) {
               return true;
            }
            break;
         case UP:
            if (!(box2.maxY < box.maxY)) {
               return true;
            }
            break;
         case DOWN:
            if (!(box2.minY > box.minY)) {
               return true;
            }
            break;
         default:
            throw new MatchException(null, null);
      }

      return false;
   }

   public Pair<Vec3d, Direction> createPair(Box box, Vec3d vec3d, Vec3d vec3d2) {
      Vec3d vec3d3 = this.resolveVec3d(vec3d, vec3d2, box.minX);
      Vec3d vec3d4 = this.resolveVec3d(vec3d, vec3d2, box.maxX);
      Vec3d vec3d5 = this.resolveVec3d2(vec3d, vec3d2, box.minY);
      Vec3d vec3d6 = this.resolveVec3d2(vec3d, vec3d2, box.maxY);
      Vec3d vec3d7 = this.resolveVec3d3(vec3d, vec3d2, box.minZ);
      Vec3d vec3d8 = this.resolveVec3d3(vec3d, vec3d2, box.maxZ);
      Direction direction = null;
      if (!this.testBox2(box, vec3d3)) {
         vec3d3 = null;
      }

      if (!this.testBox2(box, vec3d4)) {
         vec3d4 = null;
      }

      if (!this.testBox3(box, vec3d5)) {
         vec3d5 = null;
      }

      if (!this.testBox3(box, vec3d6)) {
         vec3d6 = null;
      }

      if (!this.testBox4(box, vec3d7)) {
         vec3d7 = null;
      }

      if (!this.testBox4(box, vec3d8)) {
         vec3d8 = null;
      }

      Vec3d vec3d9 = null;
      if (vec3d3 != null) {
         vec3d9 = vec3d3;
         direction = Direction.WEST;
      }

      if (vec3d4 != null && (vec3d9 == null || vec3d.squaredDistanceTo(vec3d4) < vec3d.squaredDistanceTo(vec3d9))) {
         vec3d9 = vec3d4;
         direction = Direction.EAST;
      }

      if (vec3d5 != null && (vec3d9 == null || vec3d.squaredDistanceTo(vec3d5) < vec3d.squaredDistanceTo(vec3d9))) {
         vec3d9 = vec3d5;
         direction = Direction.DOWN;
      }

      if (vec3d6 != null && (vec3d9 == null || vec3d.squaredDistanceTo(vec3d6) < vec3d.squaredDistanceTo(vec3d9))) {
         vec3d9 = vec3d6;
         direction = Direction.UP;
      }

      if (vec3d7 != null && (vec3d9 == null || vec3d.squaredDistanceTo(vec3d7) < vec3d.squaredDistanceTo(vec3d9))) {
         vec3d9 = vec3d7;
         direction = Direction.NORTH;
      }

      if (vec3d8 != null && (vec3d9 == null || vec3d.squaredDistanceTo(vec3d8) < vec3d.squaredDistanceTo(vec3d9))) {
         vec3d9 = vec3d8;
         direction = Direction.SOUTH;
      }

      return new Pair(vec3d9, direction);
   }

   public void handlePlayerEntity(PlayerEntity playerEntity) {
      float f = playerEntity.getScale();
      this.doubleArray[2] = new double[]{0.4 * f, 1.62 * f, 1.27 * f};
      this.doubleArray[1] = new double[]{1.27 * f, 1.62 * f, 0.4 * f};
      this.doubleArray[0] = new double[]{1.62 * f, 1.27 * f, 0.4 * f};
   }

   public double[] values(PlayerEntity playerEntity) {
      this.handlePlayerEntity(playerEntity);

      return switch (playerEntity.getPose()) {
         case FALL_FLYING, SPIN_ATTACK, SWIMMING -> this.doubleArray[2];
         case CROUCHING -> this.doubleArray[1];
         default -> this.doubleArray[0];
      };
   }

   @Nullable
   @Override
   public Direction resolveDirection(BlockPos blockPos, PlayerEntity playerEntity, ClientWorld clientWorld) {
      for (Direction direction : Direction.values()) {
         if (this.testBlockPos(blockPos, direction, clientWorld, playerEntity)) {
            return direction;
         }
      }

      return null;
   }

   public Box resolveBox(BlockPos blockPos, World world) {
      VoxelShape voxelShape = world.getBlockState(blockPos).getCollisionShape(world, blockPos).offset(blockPos.getX(), blockPos.getY(), blockPos.getZ());
      return voxelShape.isEmpty() ? new Box(blockPos) : voxelShape.getBoundingBox();
   }

   public boolean testBox3(Box box, Vec3d vec3d) {
      return vec3d != null && vec3d.getX() >= box.minX && vec3d.getX() <= box.maxX && vec3d.getZ() >= box.minZ && vec3d.getZ() <= box.maxZ;
   }

   public boolean testBox4(Box box, Vec3d vec3d) {
      return vec3d != null && vec3d.getX() >= box.minX && vec3d.getX() <= box.maxX && vec3d.getY() >= box.minY && vec3d.getY() <= box.maxY;
   }

   public Vec3d resolveVec3d2(Vec3d vec3d, Vec3d vec3d2, double d) {
      double d3 = vec3d2.getX() - vec3d.getX();
      double d4 = vec3d2.getY() - vec3d.getY();
      double d5 = vec3d2.getZ() - vec3d.getZ();
      if (d4 * d4 < 1.0E-7F) {
         return null;
      }

      double d2 = (d - vec3d.getY()) / d4;
      return d2 >= 0.0 && d2 <= 1.0 ? new Vec3d(vec3d.getX() + d3 * d2, vec3d.getY() + d4 * d2, vec3d.getZ() + d5 * d2) : null;
   }

   public Vec3d resolveVec3d3(Vec3d vec3d, Vec3d vec3d2, double d) {
      double d3 = vec3d2.getX() - vec3d.getX();
      double d4 = vec3d2.getY() - vec3d.getY();
      double d5 = vec3d2.getZ() - vec3d.getZ();
      if (d5 * d5 < 1.0E-7F) {
         return null;
      }

      double d2 = (d - vec3d.getZ()) / d5;
      return d2 >= 0.0 && d2 <= 1.0 ? new Vec3d(vec3d.getX() + d3 * d2, vec3d.getY() + d4 * d2, vec3d.getZ() + d5 * d2) : null;
   }
}
