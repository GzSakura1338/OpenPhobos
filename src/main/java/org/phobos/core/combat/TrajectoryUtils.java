package org.phobos.core.combat;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.phobos.core.world.RaycastUtils;

public final class TrajectoryUtils {
   public static Vec3d resolveVec3d(Vec3d vec3d, boolean flag, float f, float f2, float f3, float f4) {
      float f5 = -MathHelper.sin(f2 * (float) (Math.PI / 180.0)) * MathHelper.cos(f * (float) (Math.PI / 180.0));
      float f6 = -MathHelper.sin((f + f3) * (float) (Math.PI / 180.0));
      float f7 = MathHelper.cos(f2 * (float) (Math.PI / 180.0)) * MathHelper.cos(f * (float) (Math.PI / 180.0));
      Vec3d vec3d2 = resolveVec3d3(f5, f6, f7, f4);
      return vec3d2.add(0.0, flag ? 0.0 : vec3d.y, 0.0);
   }

   public static Vec3d resolveVec3d2(Entity entity, float f, float f2, float f3, float f4) {
      float f5 = -MathHelper.sin(f2 * (float) (Math.PI / 180.0)) * MathHelper.cos(f * (float) (Math.PI / 180.0));
      float f6 = -MathHelper.sin((f + f3) * (float) (Math.PI / 180.0));
      float f7 = MathHelper.cos(f2 * (float) (Math.PI / 180.0)) * MathHelper.cos(f * (float) (Math.PI / 180.0));
      Vec3d vec3d = resolveVec3d3(f5, f6, f7, f4);
      Vec3d vec3d2 = entity.getVelocity();
      return vec3d.add(0.0, entity.isOnGround() ? 0.0 : vec3d2.y, 0.0);
   }

   public static Vec3d resolveVec3d3(float f, float f2, float f3, float f4) {
      return new Vec3d(f, f2, f3).normalize().add(0.0, 0.0, 0.0).multiply(f4);
   }

   public TrajectoryUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static float computeEntityGravityType(TrajectoryUtils.EntityGravityType entityGravityType) {
      return switch (entityGravityType) {
         case Normal -> 0.03F;
         case High -> 0.05F;
         case None -> 0.0F;
      };
   }

   @Nullable
   public static TrajectoryUtils.TrajectoryData resolveTrajectoryData(
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      @Nullable Entity entity,
      Vec3d vec3d,
      Vec3d vec3d2,
      TrajectoryUtils.EntityGravityType entityGravityType,
      TrajectoryUtils.Drag drag,
      Entity... entitys
   ) {
      int i2 = 0;
      double d = vec3d.x;
      double d2 = vec3d.y;
      double d3 = vec3d.z;
      double d4 = vec3d2.x;
      double d5 = vec3d2.y;
      double d6 = vec3d2.z;
      double d7 = 0.0;
      float f = computeEntityGravityType(entityGravityType);
      double d8 = drag.value;
      List<Vec3d> list = new ArrayList<>();
      List<Vec3d> list2 = new ArrayList<>();

      for (int i = 0; i < 250; i++) {
         Vec3d vec3d4 = new Vec3d(d, d2, d3);
         list.add(vec3d4);
         d += d4;
         d2 += d5;
         d3 += d6;
         d4 *= d8;
         d5 *= d8;
         d6 *= d8;
         list2.add(new Vec3d(d4, d5, d6));
         d5 -= f;
         Vec3d vec3d3 = new Vec3d(d, d2, d3);
         BlockHitResult blockHitResult = clientWorld.raycast(new RaycastContext(vec3d4, vec3d3, ShapeType.COLLIDER, FluidHandling.NONE, clientPlayerEntity));
         if (blockHitResult.getType().equals(Type.BLOCK)) {
            list.add(vec3d3);
            return new TrajectoryUtils.TrajectoryData(i2, blockHitResult.getBlockPos(), vec3d3, list, list2, blockHitResult.getSide(), d7, null);
         }

         for (Entity entity2 : clientWorld.getEntities()) {
            if (entity2.canBeHitByProjectile()
               && !entity2.equals(clientPlayerEntity)
               && !(entity2 instanceof EnderPearlEntity)
               && !entity2.equals(entity)
               && entity2.getBoundingBox().intersects(new Box(d - 0.3, d2 - 0.3, d3 - 0.3, d + 0.3, d2 + 0.3, d3 + 0.3))) {
               list.add(vec3d3);
               return new TrajectoryUtils.TrajectoryData(i2, null, vec3d3, list, list2, null, d7, entity2);
            }
         }

         for (Entity entity3 : entitys) {
            if (!(entity3 instanceof ArrowEntity)
               && !entity3.equals(clientPlayerEntity)
               && !(entity3 instanceof EnderPearlEntity)
               && !entity3.equals(entity)
               && entity3.getBoundingBox().intersects(new Box(d - 0.3, d2 - 0.3, d3 - 0.3, d + 0.3, d2 + 0.3, d3 + 0.3))) {
               return new TrajectoryUtils.TrajectoryData(i2, null, vec3d3, list, list2, null, d7, entity3);
            }
         }

         EntityHitResult entityHitResult = RaycastUtils.resolveEntityHitResult(clientPlayerEntity, vec3d4, vec3d3);
         if (entityHitResult != null && entityHitResult.getEntity() != clientPlayerEntity && (entity == null || entityHitResult.getEntity() != entity)) {
            list.add(entityHitResult.getPos());
            return new TrajectoryUtils.TrajectoryData(i2, null, entityHitResult.getPos(), list, list2, null, d7, entityHitResult.getEntity());
         }

         d7 += vec3d4.distanceTo(vec3d3);
         i2++;
         if (d2 <= -65.0) {
            break;
         }
      }

      return null;
   }

   public enum Drag {
      Normal(0.99),
      Immutable(1.0),
      Trident(0.95);

      public final double value;

      Drag(final double value) {
         this.value = value;
      }

      public double getValue() {
         return this.value;
      }
   }

   public enum EntityGravityType {
      Normal,
      High,
      None;
   }

   public static class TrajectoryData {
      public List<Vec3d> vec3dList;
      @Nullable
      public BlockPos blockPos;
      public int intValue;
      public List<Vec3d> vec3dList2;
      @Nullable
      public Direction direction;
      @Nullable
      public Entity entity;
      @NotNull
      public Vec3d vec3d;
      public double doubleValue;

      public void setIntValue(int i) {
         this.intValue = i;
      }

      public void setVec3dList(List<Vec3d> list) {
         this.vec3dList = list;
      }

      public void setBlockPos(@Nullable BlockPos blockPos) {
         this.blockPos = blockPos;
      }

      public void setEntity(@Nullable Entity entity) {
         this.entity = entity;
      }

      @Nullable
      public Entity getEntity() {
         return this.entity;
      }

      @Nullable
      public Direction getDirection() {
         return this.direction;
      }

      @Nullable
      public BlockPos getBlockPos() {
         return this.blockPos;
      }

      @Nullable
      public Vec3d resolveVec3d(int i) {
         return this.vec3dList2.get(i);
      }

      public int getIntValue() {
         return this.intValue;
      }

      @NotNull
      public Vec3d getVec3d() {
         return this.vec3d;
      }

      public void setDirection(@Nullable Direction direction) {
         this.direction = direction;
      }

      public TrajectoryData(
         int i,
         @Nullable BlockPos blockPos,
         @NotNull Vec3d vec3d,
         List<Vec3d> list,
         List<Vec3d> list2,
         @Nullable Direction direction,
         double d,
         @Nullable Entity entity
      ) {
         if (vec3d == null) {
            throw new NullPointerException("position is marked non-null but is null");
         }

         this.intValue = i;
         this.blockPos = blockPos;
         this.vec3d = vec3d;
         this.vec3dList2 = list;
         this.vec3dList = list2;
         this.direction = direction;
         this.doubleValue = d;
         this.entity = entity;
      }

      public void setVec3dList2(List<Vec3d> list) {
         this.vec3dList2 = list;
      }

      public double getDoubleValue() {
         return this.doubleValue;
      }

      public void setDoubleValue(double d) {
         this.doubleValue = d;
      }

      public void handleVec3d(@NotNull Vec3d vec3d) {
         if (vec3d == null) {
            throw new NullPointerException("position is marked non-null but is null");
         }

         this.vec3d = vec3d;
      }

      public List<Vec3d> getVec3dList() {
         return this.vec3dList;
      }

      @NotNull
      public TrajectoryUtils.TrajectoryData resolveTrajectoryData(TrajectoryUtils.TrajectoryData trajectoryData) {
         if (this.intValue == trajectoryData.intValue) {
            return this;
         } else {
            return this.intValue > trajectoryData.intValue ? this : trajectoryData;
         }
      }

      public List<Vec3d> getVec3dList2() {
         return this.vec3dList2;
      }
   }
}
