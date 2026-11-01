package org.phobos.core.world;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.MinecraftHolder;
import org.phobos.core.math.BoxUtils;
import org.phobos.core.entity.EntityInterpolation;
import org.phobos.core.PhobosClient;
import org.phobos.core.rotation.RotationManager;
import org.phobos.core.tick.TimerManager;

public final class RaycastUtils implements MinecraftHolder {
   public static boolean testPhobosClient(PhobosClient phobosClient, Entity entity) {
      Entity entity2 = phobosClient.getMovementSimulator().resolvePlayerEntity(null);
      return entity2 != null && testEntity(entity2, entity);
   }

   @Nullable
   public static BlockHitResult resolveBlockHitResult(Entity entity, World world, BlockPos blockPos, @Nullable Direction direction, boolean flag) {
      return resolveBlockHitResult3(entity, world, blockPos, direction == null ? null : blockHitResult -> blockHitResult.getSide().equals(direction), flag);
   }

   public static boolean testEntity(Entity entity, Entity entity2) {
      EntityHitResult entityHitResult;
      return (entityHitResult = resolveEntityHitResult2(entity, entity2)) != null && entity2.equals(entityHitResult.getEntity());
   }

   @Nullable
   public static BlockHitResult resolveBlockHitResult2(Entity entity, World world, BlockPos blockPos, @Nullable Direction direction, float[] floats) {
      return resolveBlockHitResult10(entity, world, blockPos, direction == null ? null : blockHitResult -> blockHitResult.getSide().equals(direction), floats);
   }

   @Nullable
   public static EntityHitResult createEntityHitResult(@Nullable Entity entity, Vec3d vec3d, Vec3d vec3d2, double d, Collection<Entity> collection) {
      double d3 = d;
      Entity entity3 = null;
      Vec3d vec3d4 = null;

      for (Entity entity2 : collection) {
         Box box = entity2.getBoundingBox().expand(entity2.getTargetingMargin());
         Optional<Vec3d> optional = box.raycast(vec3d, vec3d2);
         if (box.contains(vec3d)) {
            if (d3 >= 0.0) {
               entity3 = entity2;
               vec3d4 = optional.orElse(vec3d);
               d3 = 0.0;
            }
         } else {
            Vec3d vec3d3;
            double d2;
            if (!optional.isEmpty() && ((d2 = vec3d.squaredDistanceTo(vec3d3 = optional.get())) < d3 || d3 == 0.0)) {
               if (entity == null || entity2.getRootVehicle() != entity.getRootVehicle()) {
                  entity3 = entity2;
                  vec3d4 = vec3d3;
                  d3 = d2;
               } else if (d3 == 0.0) {
                  entity3 = entity2;
                  vec3d4 = vec3d3;
               }
            }
         }
      }

      return entity3 == null ? null : new EntityHitResult(entity3, vec3d4);
   }

   @Nullable
   public static Vec3d resolveVec3d(ClientPlayerEntity clientPlayerEntity, Box box, int i) {
      float f = TimerManager.getFloatValue();
      Vec3d vec3d2 = EntityInterpolation.createVec3d(clientPlayerEntity, f).add(0.0, clientPlayerEntity.getEyeHeight(clientPlayerEntity.getPose()), 0.0);
      Vec3d[] vec3ds = BoxUtils.values(box, i);
      double d2 = Float.MAX_VALUE;
      Vec3d vec3d3 = null;

      for (Vec3d vec3d : vec3ds) {
         double d = vec3d2.distanceTo(vec3d);
         if (d < d2) {
            vec3d3 = vec3d;
            d2 = d;
         }
      }

      return vec3d3;
   }

   @Nullable
   public static BlockHitResult resolveBlockHitResult3(
      Entity entity, World world, BlockPos blockPos, @Nullable Predicate<BlockHitResult> predicate, boolean flag
   ) {
      double d = Math.sqrt(entity.squaredDistanceTo(blockPos.getX() + 0.5, blockPos.getY() + 0.5 - entity.getStandingEyeHeight(), blockPos.getZ() + 0.5)) + 3.0;
      BlockHitResult blockHitResult = resolveBlockHitResult16(
         entity,
         world,
         d,
         blockHitResultx -> blockHitResultx == null || !blockHitResultx.getBlockPos().equals(blockPos) || predicate != null && !predicate.test(blockHitResultx)
            ? null
            : blockHitResultx
      );
      if (flag && blockHitResult == null && predicate != null) {
         blockHitResult = resolveBlockHitResult13(
            entity,
            world,
            d,
            blockHitResultx -> blockHitResultx != null && blockHitResultx.getBlockPos().equals(blockPos) && predicate.test(blockHitResultx)
               ? blockHitResultx
               : null
         );
      }

      return blockHitResult;
   }

   @Nullable
   public static BlockHitResult resolveBlockHitResult4(Entity entity, World world, BlockPos blockPos, Direction direction, Vec3d vec3d) {
      return resolveBlockHitResult8(
         entity, world, blockPos, vec3d, direction == null ? null : blockHitResult -> blockHitResult.getSide().equals(direction), false
      );
   }

   public static BlockHitResult resolveBlockHitResult5(Entity entity, ClientWorld clientWorld, float[] floats, double d) {
      Vec3d vec3d = entity.getEyePos();
      Vec3d vec3d2 = new RotationManager.Rotation(floats[0], floats[1]).getVec3d();
      Vec3d vec3d3 = vec3d.add(vec3d2.x * d, vec3d2.y * d, vec3d2.z * d);
      return clientWorld.raycast(new RaycastContext(vec3d, vec3d3, ShapeType.OUTLINE, FluidHandling.NONE, entity));
   }

   @Nullable
   public static EntityHitResult resolveEntityHitResult(ClientPlayerEntity clientPlayerEntity, Vec3d vec3d, Vec3d vec3d2) {
      return createEntityHitResult(clientPlayerEntity, vec3d, vec3d2, vec3d.distanceTo(vec3d2), List.of(clientPlayerEntity));
   }

   @Nullable
   public static BlockHitResult resolveBlockHitResult6(
      Entity entity, World world, BlockPos blockPos, BiPredicate<BlockPos, Direction> biPredicate, boolean flag
   ) {
      double d = Math.sqrt(entity.squaredDistanceTo(blockPos.getX() + 0.5, blockPos.getY() + 0.5 - entity.getStandingEyeHeight(), blockPos.getZ() + 0.5)) + 3.0;
      Function<BlockHitResult, BlockHitResult> function = blockHitResultx -> blockHitResultx != null
            && blockHitResultx.getBlockPos().offset(blockHitResultx.getSide()).equals(blockPos)
            && biPredicate.test(blockHitResultx.getBlockPos(), blockHitResultx.getSide())
         ? blockHitResultx
         : null;
      BlockHitResult blockHitResult = resolveBlockHitResult16(entity, world, d, function);
      if (blockHitResult != null) {
         return blockHitResult;
      } else {
         return flag ? resolveBlockHitResult13(entity, world, d, function) : null;
      }
   }

   public static boolean testPhobosClient2(PhobosClient phobosClient, Entity entity, double d) {
      Entity entity2 = phobosClient.getMovementSimulator().resolvePlayerEntity(null);
      if (entity2 == null) {
         return false;
      }

      Vec3d vec3d = createVec3d(entity2, entity2.getYaw(), entity2.getPitch());
      return ProjectileUtil.raycast(
            entity2,
            entity2.getPos().add(0.0, entity2.getEyeHeight(entity2.getPose()), 0.0),
            vec3d.multiply(d),
            entity2.getBoundingBox().stretch(vec3d).expand(1.0, 1.0, 1.0),
            entity2x -> !entity2x.isSpectator() && entity2x.canHit() && entity2x.equals(entity),
            d
         )
         != null;
   }

   @Nullable
   public static BlockHitResult resolveBlockHitResult7(
      Entity entity, float[] floats, World world, BlockPos blockPos, BiPredicate<BlockPos, Direction> biPredicate, boolean flag
   ) {
      double d = Math.sqrt(entity.squaredDistanceTo(blockPos.getX() + 0.5, blockPos.getY() + 0.5 - entity.getStandingEyeHeight(), blockPos.getZ() + 0.5)) + 3.0;
      Function<BlockHitResult, BlockHitResult> function = blockHitResultx -> blockHitResultx != null
            && blockHitResultx.getBlockPos().offset(blockHitResultx.getSide()).equals(blockPos)
            && biPredicate.test(blockHitResultx.getBlockPos(), blockHitResultx.getSide())
         ? blockHitResultx
         : null;
      BlockHitResult blockHitResult = resolveBlockHitResult15(entity, world, d, function, floats);
      if (blockHitResult != null) {
         return blockHitResult;
      } else {
         return flag ? resolveBlockHitResult11(entity, world, d, function, floats) : null;
      }
   }

   @Nullable
   public static EntityHitResult resolveEntityHitResult2(Entity entity, Entity entity2) {
      double d = entity.distanceTo(entity2) + 4.0;
      Vec3d vec3d = entity.getEyePos();
      Vec3d vec3d2 = createVec3d(entity, entity.getYaw(), entity.getPitch());
      Vec3d vec3d3 = vec3d.add(vec3d2.x * d, vec3d2.y * d, vec3d2.z * d);
      return createEntityHitResult(entity, vec3d, vec3d3, d * d, List.of(entity2));
   }

   public static Vec3d createVec3d(Entity entity, float f, float f2) {
      float f3 = f2 * (float) (Math.PI / 180.0);
      float f4 = -f * (float) (Math.PI / 180.0);
      float f5 = MathHelper.cos(f4);
      float f6 = MathHelper.sin(f4);
      float f7 = MathHelper.cos(f3);
      float f8 = MathHelper.sin(f3);
      return new Vec3d(f6 * f7, -f8, f5 * f7);
   }

   public static boolean testEntity2(Entity entity, ClientWorld clientWorld, BlockPos blockPos, Direction direction) {
      return resolveBlockHitResult3(entity, clientWorld, blockPos, blockHitResult -> blockHitResult != null && blockHitResult.getSide().equals(direction), true)
         != null;
   }

   public static boolean testEntity3(Entity entity, Entity entity2, float[] floats) {
      EntityHitResult entityHitResult;
      return (entityHitResult = resolveEntityHitResult3(entity, entity2, floats)) != null && entity2.equals(entityHitResult.getEntity());
   }

   public static Vec3d createVec3d2(float f, float f2) {
      float f3 = f2 * (float) (Math.PI / 180.0);
      float f4 = -f * (float) (Math.PI / 180.0);
      float f5 = MathHelper.cos(f4);
      float f6 = MathHelper.sin(f4);
      float f7 = MathHelper.cos(f3);
      float f8 = MathHelper.sin(f3);
      return new Vec3d(f6 * f7, -f8, f5 * f7);
   }

   @Nullable
   public static EntityHitResult resolveEntityHitResult3(Entity entity, Entity entity2, float[] floats) {
      double d = entity.distanceTo(entity2) + 4.0;
      Vec3d vec3d = entity.getEyePos();
      Vec3d vec3d2 = createVec3d(entity, floats[0], floats[1]);
      Vec3d vec3d3 = vec3d.add(vec3d2.x * d, vec3d2.y * d, vec3d2.z * d);
      return createEntityHitResult(entity, vec3d, vec3d3, d * d, List.of(entity2));
   }

   @Nullable
   public static BlockHitResult resolveBlockHitResult8(
      Entity entity, World world, BlockPos blockPos, Vec3d vec3d, @Nullable Predicate<BlockHitResult> predicate, boolean flag
   ) {
      double d = Math.sqrt(entity.squaredDistanceTo(blockPos.getX() + 0.5, blockPos.getY() + 0.5 - entity.getStandingEyeHeight(), blockPos.getZ() + 0.5)) + 3.0;
      BlockHitResult blockHitResult = resolveBlockHitResult14(
         entity,
         world,
         vec3d,
         d,
         blockHitResultx -> blockHitResultx == null || !blockHitResultx.getBlockPos().equals(blockPos) || predicate != null && !predicate.test(blockHitResultx)
            ? null
            : blockHitResultx
      );
      if (flag && blockHitResult == null && predicate != null) {
         blockHitResult = resolveBlockHitResult13(
            entity,
            world,
            d,
            blockHitResultx -> blockHitResultx != null && blockHitResultx.getBlockPos().equals(blockPos) && predicate.test(blockHitResultx)
               ? blockHitResultx
               : null
         );
      }

      return blockHitResult;
   }

   public static BlockHitResult resolveBlockHitResult9(Entity entity, ClientWorld clientWorld, double d) {
      Vec3d vec3d = entity.getEyePos();
      Vec3d vec3d2 = createVec3d(entity, entity.getYaw(), entity.getPitch());
      Vec3d vec3d3 = vec3d.add(vec3d2.x * d, vec3d2.y * d, vec3d2.z * d);
      return clientWorld.raycast(new RaycastContext(vec3d, vec3d3, ShapeType.OUTLINE, FluidHandling.NONE, entity));
   }

   @Nullable
   public static BlockHitResult resolveBlockHitResult10(
      Entity entity, World world, BlockPos blockPos, @Nullable Predicate<BlockHitResult> predicate, float[] floats
   ) {
      double d = Math.sqrt(entity.squaredDistanceTo(blockPos.getX() + 0.5, blockPos.getY() + 0.5 - entity.getStandingEyeHeight(), blockPos.getZ() + 0.5)) + 3.0;
      return resolveBlockHitResult15(
         entity,
         world,
         d,
         blockHitResult -> blockHitResult == null || !blockHitResult.getBlockPos().equals(blockPos) || predicate != null && !predicate.test(blockHitResult)
            ? null
            : blockHitResult,
         floats
      );
   }

   @Nullable
   public static BlockHitResult resolveBlockHitResult11(
      Entity entity, World world, double d, Function<@Nullable BlockHitResult, @Nullable BlockHitResult> function, float[] floats
   ) {
      Vec3d vec3d = entity.getEyePos();
      Vec3d vec3d2 = createVec3d2(floats[0], floats[1]);
      Vec3d vec3d3 = vec3d.add(vec3d2.x * d, vec3d2.y * d, vec3d2.z * d);
      return resolveBlockHitResult12(vec3d3, vec3d, entity, world, function);
   }

   @Nullable
   public static BlockHitResult resolveBlockHitResult12(
      Vec3d vec3d, Vec3d vec3d2, Entity entity, World world, Function<@Nullable BlockHitResult, @Nullable BlockHitResult> function
   ) {
      RaycastContext raycastContext = new RaycastContext(vec3d, vec3d2, ShapeType.OUTLINE, FluidHandling.NONE, entity);
      return (BlockHitResult)BlockView.raycast(raycastContext.getStart(), raycastContext.getEnd(), raycastContext, (raycastContextx, blockPos) -> {
         BlockState blockState = world.getBlockState(blockPos);
         FluidState fluidState = world.getFluidState(blockPos);
         Vec3d vec3dx = raycastContextx.getStart();
         Vec3d vec3d2x = raycastContextx.getEnd();
         VoxelShape voxelShape = raycastContextx.getBlockShape(blockState, world, blockPos);
         BlockHitResult blockHitResult = world.raycastBlock(vec3dx, vec3d2x, blockPos, voxelShape, blockState);
         VoxelShape voxelShape2 = raycastContextx.getFluidShape(fluidState, world, blockPos);
         BlockHitResult blockHitResult2 = voxelShape2.raycast(vec3dx, vec3d2x, blockPos);
         double d = blockHitResult == null ? Double.MAX_VALUE : raycastContextx.getStart().squaredDistanceTo(blockHitResult.getPos());
         double d2 = blockHitResult2 == null ? Double.MAX_VALUE : raycastContextx.getStart().squaredDistanceTo(blockHitResult2.getPos());
         return function.apply(d <= d2 ? blockHitResult : blockHitResult2);
      }, raycastContextx -> null);
   }

   @Nullable
   public static BlockHitResult resolveBlockHitResult13(
      Entity entity, World world, double d, Function<@Nullable BlockHitResult, @Nullable BlockHitResult> function
   ) {
      Vec3d vec3d = entity.getEyePos();
      Vec3d vec3d2 = createVec3d(entity, entity.getYaw(), entity.getPitch());
      Vec3d vec3d3 = vec3d.add(vec3d2.x * d, vec3d2.y * d, vec3d2.z * d);
      return resolveBlockHitResult12(vec3d3, vec3d, entity, world, function);
   }

   @Nullable
   public static BlockHitResult resolveBlockHitResult14(
      Entity entity, World world, Vec3d vec3d, double d, Function<@Nullable BlockHitResult, @Nullable BlockHitResult> function
   ) {
      return resolveBlockHitResult12(entity.getEyePos(), vec3d, entity, world, function);
   }

   @Nullable
   public static EntityHitResult createEntityHitResult2(
      Entity entity, List<Entity> list, Vec3d vec3d, Vec3d vec3d2, Box box, Predicate<Entity> predicate, double d
   ) {
      double d3 = d;
      Entity entity3 = null;
      Vec3d vec3d4 = null;

      for (Entity entity2 : list) {
         if (entity2.getBoundingBox().intersects(box) && predicate.test(entity2)) {
            Box box2 = entity2.getBoundingBox().expand(entity2.getTargetingMargin());
            Optional<Vec3d> optional = box2.raycast(vec3d, vec3d2);
            if (box2.contains(vec3d)) {
               if (!(d3 < 0.0)) {
                  entity3 = entity2;
                  vec3d4 = optional.orElse(vec3d);
                  d3 = 0.0;
               }
            } else if (!optional.isEmpty()) {
               Vec3d vec3d3 = optional.get();
               double d2 = vec3d.squaredDistanceTo(vec3d3);
               if (!(d2 >= d3) || d3 == 0.0) {
                  if (entity2.getRootVehicle() == entity.getRootVehicle()) {
                     if (d3 == 0.0) {
                        entity3 = entity2;
                        vec3d4 = vec3d3;
                     }
                  } else {
                     entity3 = entity2;
                     vec3d4 = vec3d3;
                     d3 = d2;
                  }
               }
            }
         }
      }

      return entity3 == null ? null : new EntityHitResult(entity3, vec3d4);
   }

   public RaycastUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   @Nullable
   public static BlockHitResult resolveBlockHitResult15(
      Entity entity, World world, double d, Function<@Nullable BlockHitResult, @Nullable BlockHitResult> function, float[] floats
   ) {
      Vec3d vec3d = entity.getEyePos();
      Vec3d vec3d2 = createVec3d2(floats[0], floats[1]);
      Vec3d vec3d3 = vec3d.add(vec3d2.x * d, vec3d2.y * d, vec3d2.z * d);
      return resolveBlockHitResult12(vec3d, vec3d3, entity, world, function);
   }

   @Nullable
   public static BlockHitResult resolveBlockHitResult16(
      Entity entity, World world, double d, Function<@Nullable BlockHitResult, @Nullable BlockHitResult> function
   ) {
      Vec3d vec3d = entity.getEyePos();
      Vec3d vec3d2 = createVec3d(entity, entity.getYaw(), entity.getPitch());
      Vec3d vec3d3 = vec3d.add(vec3d2.x * d, vec3d2.y * d, vec3d2.z * d);
      return resolveBlockHitResult12(vec3d, vec3d3, entity, world, function);
   }

   public static class PseudoRay implements Runnable {
      public Vec3d vec3d;
      public final Set<BlockPos> blockPosSet = new HashSet<>();
      public float floatValue;
      public Vec3d vec3d2;

      public void setVec3d(Vec3d vec3d) {
         this.vec3d = vec3d;
      }

      public Set<BlockPos> getBlockPosSet() {
         return this.blockPosSet;
      }

      public float getFloatValue() {
         return this.floatValue;
      }

      @Override
      public void run() {
         float f = 0.0F;

         while (f <= 1.0F) {
            Vec3d vec3d = this.vec3d.lerp(this.vec3d2, f);
            this.blockPosSet.add(BlockPos.ofFloored(vec3d));
            f += this.floatValue;
         }
      }

      public Vec3d getVec3d2() {
         return this.vec3d2;
      }

      public void setFloatValue(float f) {
         this.floatValue = f;
      }

      public Vec3d getVec3d() {
         return this.vec3d;
      }

      public void setVec3d2(Vec3d vec3d) {
         this.vec3d2 = vec3d;
      }

      public static RaycastUtils.PseudoRay.Builder createBuilder(float f) {
         return new RaycastUtils.PseudoRay.Builder(f);
      }

      public static class Builder {
         public final float floatValue;
         public final RaycastUtils.PseudoRay pseudoRay = new RaycastUtils.PseudoRay();

         public Builder(float f) {
            this.floatValue = f;
         }

         public RaycastUtils.PseudoRay getPseudoRay() {
            return this.pseudoRay;
         }

         public RaycastUtils.PseudoRay getPseudoRay2() {
            this.pseudoRay.setFloatValue(this.floatValue);
            return this.pseudoRay;
         }

         public float getFloatValue() {
            return this.floatValue;
         }

         public RaycastUtils.PseudoRay.Builder resolveBuilder(Vec3d vec3d) {
            this.pseudoRay.setVec3d(vec3d);
            return this;
         }

         public RaycastUtils.PseudoRay.Builder resolveBuilder2(Vec3d vec3d) {
            this.pseudoRay.setVec3d2(vec3d);
            return this;
         }
      }
   }
}
