package org.phobos.core.rotation;

import java.util.function.BiFunction;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import org.joml.Vector2d;
import org.phobos.api.MinecraftHolder;
import org.phobos.module.render.FreeLookModule;
import org.phobos.core.world.RaycastUtils;

public final class RotationUtils implements MinecraftHolder {
   public static float computeFloat2(float[] floats, float[] floats2) {
      return MathHelper.angleBetween(floats[0], floats2[0]) + MathHelper.angleBetween(floats[1], floats2[1]);
   }

   public static float computeFloat3(float f, float f2) {
      return f + (MathHelper.wrapDegrees(f2) - MathHelper.wrapDegrees(f));
   }

   public static float computeClientPlayerEntity2(ClientPlayerEntity clientPlayerEntity) {
      return FreeLookModule.getFreeLookModule().check7() ? FreeLookModule.getFreeLookModule().getRotation().getFloatValue2() : clientPlayerEntity.getYaw();
   }

   public RotationUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static float[] values(Entity entity, Entity entity2) {
      return values6(
         entity.getX(), entity.getEyeY(), entity.getZ(), entity2.getX(), entity2.getY() + entity2.getStandingEyeHeight() / 2.0, entity2.getZ(), entity.prevYaw
      );
   }

   public static Vec3d resolveVec3d(BlockPos blockPos, Direction direction, Vec3d vec3d) {
      return switch (direction) {
         case EAST -> vec3d.add(0.5, 0.0, 0.0);
         case WEST -> vec3d.add(-0.5, 0.0, 0.0);
         case UP -> vec3d.add(0.0, 0.5, 0.0);
         case DOWN -> vec3d.add(0.0, -0.5, 0.0);
         case SOUTH -> vec3d.add(0.0, 0.0, 0.5);
         case NORTH -> vec3d.add(0.0, 0.0, -0.5);
         default -> throw new MatchException(null, null);
      };
   }

   public static Vec3d createVec3d(BlockPos blockPos, World world, Direction direction, Vec3d vec3d, BiFunction<Double, Double, Double> biFunction) {
      BlockState blockState = world.getBlockState(blockPos);
      VoxelShape voxelShape = blockState.getOutlineShape(world, blockPos);
      Box box = voxelShape.isEmpty() ? VoxelShapes.fullCube().getBoundingBox() : voxelShape.getBoundingBox();
      double d = vec3d.getX();
      double d2 = blockPos.getY() + (direction == Direction.UP ? 1.0 : (direction == Direction.DOWN ? box.minY : biFunction.apply(box.minY, box.maxY)));
      double d3 = vec3d.getZ();
      return new Vec3d(d, d2, d3);
   }

   public static double computeDoubleValue(Double doubleValue) {
      double d = doubleValue;
      d %= 360.0;
      if (d < -180.0) {
         d += 360.0;
      }

      return d;
   }

   public static Vec3d createVec3d2(BlockPos blockPos, World world, Direction direction) {
      BlockState blockState = world.getBlockState(blockPos);
      VoxelShape voxelShape = blockState.getOutlineShape(world, blockPos);
      Box box = voxelShape.isEmpty() ? VoxelShapes.fullCube().getBoundingBox() : voxelShape.getBoundingBox();
      double d = blockPos.getX() + (direction == Direction.EAST ? box.maxX : (direction == Direction.WEST ? box.minX : (box.minX + box.maxX) / 2.0));
      double d2 = blockPos.getY() + (direction == Direction.UP ? box.maxY : (direction == Direction.DOWN ? box.minY : (box.minY + box.maxY) / 2.0));
      double d3 = blockPos.getZ() + (direction == Direction.SOUTH ? box.maxZ : (direction == Direction.NORTH ? box.minZ : (box.minZ + box.maxZ) / 2.0));
      return new Vec3d(d, d2, d3);
   }

   public static Vec3d createVec3d3(BlockPos blockPos, World world, Direction direction, BiFunction<Double, Double, Double> biFunction) {
      BlockState blockState = world.getBlockState(blockPos);
      VoxelShape voxelShape = blockState.getOutlineShape(world, blockPos);
      Box box = voxelShape.isEmpty() ? VoxelShapes.fullCube().getBoundingBox() : voxelShape.getBoundingBox();
      double d = blockPos.getX() + (direction == Direction.EAST ? box.maxX : (direction == Direction.WEST ? box.minX : biFunction.apply(box.minX, box.maxX)));
      double d2 = blockPos.getY() + (direction == Direction.UP ? box.maxY : (direction == Direction.DOWN ? box.minY : biFunction.apply(box.minY, box.maxY)));
      double d3 = blockPos.getZ()
         + (direction == Direction.SOUTH ? box.maxZ : (direction == Direction.NORTH ? box.minZ : biFunction.apply(box.minZ, box.maxZ)));
      return new Vec3d(d, d2, d3);
   }

   public static Vector2d resolveVector2d(Vec3d vec3d, Vec3d vec3d2) {
      return createVector2d(vec3d.subtract(vec3d2));
   }

   public static Vec3d resolveVec3d2(BlockPos blockPos, Vec3d vec3d) {
      return vec3d.subtract(blockPos.getX(), blockPos.getY(), blockPos.getZ());
   }

   public static double computeClientPlayerEntity3(ClientPlayerEntity clientPlayerEntity, Entity entity, double d) {
      double d2 = entity.getX() - clientPlayerEntity.getX();
      double d3 = entity.getZ() - clientPlayerEntity.getZ();
      float f = (float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(d3, d2)) - 90.0);
      return Math.abs(f - MathHelper.wrapDegrees(clientPlayerEntity.getYaw()));
   }

   public static float computeFloat4(float f, float f2, float f3, float f4) {
      return MathHelper.angleBetween(f, f3) + MathHelper.angleBetween(f2, f4);
   }

   public static float[] values2(Entity entity, Vec3d vec3d) {
      return values6(entity.getX(), entity.getEyeY(), entity.getZ(), vec3d.x, vec3d.y, vec3d.z, entity.prevYaw);
   }

   public static float[] values3(ClientPlayerEntity clientPlayerEntity, Entity entity, int i) {
      float[] floats = values(clientPlayerEntity, entity);
      if (i == 0) {
         return floats;
      }

      Vec3d vec3d = RaycastUtils.resolveVec3d(clientPlayerEntity, entity.getBoundingBox(), i);
      return vec3d == null ? floats : values2(clientPlayerEntity, vec3d);
   }

   public static float computeFloat(float f) {
      return Math.round(f / (float)(value() * 0.15)) * (float)(value() * 0.15);
   }

   public static float[] values4(Entity entity, World world, BlockPos blockPos, Direction direction) {
      Vec3d vec3d = createVec3d2(blockPos, world, direction);
      return values7(entity, vec3d.x, vec3d.y, vec3d.z);
   }

   public static float[] values5(float[] floats) {
      return new float[]{floats[0] + 180.0F, floats[1] * -1.0F};
   }

   public static float[] values6(double d, double d2, double d3, double d4, double d5, double d6, float f) {
      double d7 = d4 - d;
      double d8 = d5 - d2;
      double d9 = d6 - d3;
      double d10 = Math.sqrt(d7 * d7 + d9 * d9);
      float f3 = (float)(Math.atan2(d9, d7) * 180.0 / Math.PI) - 90.0F;
      float f4 = (float)(-(Math.atan2(d8, d10) * 180.0 / Math.PI));
      float f5 = f3 - f;
      if (f5 < -180.0F || f5 > 180.0F) {
         float f2 = Math.round(Math.abs(f5 / 360.0F));
         f5 = f5 < 0.0F ? f5 + 360.0F * f2 : f5 - 360.0F * f2;
      }

      return new float[]{f + f5, f4};
   }

   public static void handleFloat(float[] floats, float[] floats2) {
      floats2[0] = floats[0];
      floats2[1] = floats[1];
   }

   public static Vec3d createVec3d4(BlockPos blockPos, Direction direction, double d, double d2, double d3) {
      double d4 = blockPos.getX() + (direction == Direction.EAST ? 1.0 : (direction == Direction.WEST ? 0.0 : d));
      double d5 = blockPos.getY() + (direction == Direction.UP ? 1.0 : (direction == Direction.DOWN ? 0.0 : d2));
      double d6 = blockPos.getZ() + (direction == Direction.SOUTH ? 1.0 : (direction == Direction.NORTH ? 0.0 : d3));
      return new Vec3d(d4, d5, d6);
   }

   public static float computeClientPlayerEntity(ClientPlayerEntity clientPlayerEntity) {
      return FreeLookModule.getFreeLookModule().check7() ? FreeLookModule.getFreeLookModule().getRotation().getFloatValue() : clientPlayerEntity.getPitch();
   }

   public static float[] values7(Entity entity, double d, double d2, double d3) {
      return values6(entity.getX(), entity.getEyeY(), entity.getZ(), d, d2, d3, entity.prevYaw);
   }

   public static Vec3d createVec3d5(Box box, Vec3d vec3d) {
      double d;
      if (vec3d.x >= box.maxX) {
         d = box.maxX;
      } else {
         d = Math.max(vec3d.x, box.minX);
      }

      double d2;
      if (vec3d.y >= box.maxY) {
         d2 = box.maxY;
      } else {
         d2 = Math.max(vec3d.y, box.minY);
      }

      double d3;
      if (vec3d.z >= box.maxZ) {
         d3 = box.maxZ;
      } else {
         d3 = Math.max(vec3d.z, box.minZ);
      }

      return new Vec3d(d, d2, d3);
   }

   public static float value() {
      double d = (Double)mc.options.getMouseSensitivity().getValue();
      double d2 = d * 0.6 + 0.2;
      double d3 = Math.pow(d2, 3.0) * 8.0;
      return (float)d3;
   }

   public static Vector2d createVector2d(Vec3d vec3d) {
      double d = Math.hypot(vec3d.x, vec3d.z);
      float f = (float)computeDoubleValue(Math.toDegrees(Math.atan2(vec3d.z, vec3d.x)) - 90.0);
      float f2 = (float)computeDoubleValue(Math.toDegrees(-Math.atan2(vec3d.y, d)));
      return new Vector2d(f, f2);
   }
}
