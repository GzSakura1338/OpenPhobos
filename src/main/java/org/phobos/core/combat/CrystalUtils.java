package org.phobos.core.combat;

import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;
import org.apache.commons.lang3.mutable.MutableInt;
import org.phobos.core.world.RaycastUtils;

public final class CrystalUtils {
   public static boolean testPlayerEntity(PlayerEntity playerEntity, ClientWorld clientWorld, Box box, boolean flag) {
      Vec3d vec3d2 = playerEntity.getEyePos();
      if (flag) {
         Vec3d[] vec3ds = values(box);

         for (Vec3d vec3d : vec3ds) {
            HitResult hitResult2 = clientWorld.raycast(new RaycastContext(vec3d2, vec3d, ShapeType.OUTLINE, FluidHandling.NONE, playerEntity));
            if (hitResult2 != null && !hitResult2.getType().equals(Type.BLOCK)) {
               return true;
            }
         }

         return false;
      } else {
         HitResult hitResult = clientWorld.raycast(new RaycastContext(vec3d2, box.getCenter(), ShapeType.OUTLINE, FluidHandling.NONE, playerEntity));
         return hitResult == null || !hitResult.getType().equals(Type.BLOCK);
      }
   }

   public static int computeClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, BlockPos blockPos) {
      Vec3d vec3d2 = clientPlayerEntity.getEyePos();
      Vec3d vec3d3 = blockPos.toCenterPos();
      Vec3d[] vec3ds = new Vec3d[]{
         vec3d3,
         vec3d3.add(0.5, 0.0, 0.0),
         vec3d3.add(-0.5, 0.0, 0.0),
         vec3d3.add(0.5, 0.0, 0.5),
         vec3d3.add(0.5, 0.0, -0.5),
         vec3d3.add(0.5, 0.5, 0.0),
         vec3d3.add(0.5, -0.5, 0.0)
      };
      AtomicInteger atomicInteger = new AtomicInteger();
      MutableInt mutableInt = new MutableInt(Integer.MAX_VALUE);

      for (Vec3d vec3d : vec3ds) {
         RaycastUtils.resolveBlockHitResult12(vec3d2, vec3d, clientPlayerEntity, clientWorld, blockHitResult -> {
            if (blockHitResult == null) {
               return null;
            }

            BlockPos blockPos2 = blockHitResult.getBlockPos();
            if (!clientWorld.getBlockState(blockPos2).isReplaceable()) {
               atomicInteger.incrementAndGet();
            }

            return blockHitResult.getBlockPos().equals(blockPos) ? blockHitResult : null;
         });
         mutableInt.setValue(Math.min(mutableInt.getValue(), atomicInteger.get()));
         atomicInteger.set(0);
      }

      return mutableInt.getValue();
   }

   public static boolean testClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, BlockPos blockPos, int i) {
      Vec3d vec3d = clientPlayerEntity.getEyePos();
      Vec3d vec3d2 = blockPos.toCenterPos();
      if (i != -1) {
         return computeClientPlayerEntity(clientPlayerEntity, clientWorld, blockPos) <= i;
      }

      BlockHitResult blockHitResult = clientWorld.raycast(new RaycastContext(vec3d, vec3d2, ShapeType.COLLIDER, FluidHandling.NONE, clientPlayerEntity));
      return blockHitResult != null && blockHitResult.getBlockPos().equals(blockPos);
   }

   public static boolean testPlayerEntity2(PlayerEntity playerEntity, ClientWorld clientWorld, Entity entity, boolean flag) {
      Box box = entity.getBoundingBox();
      Vec3d vec3d3 = playerEntity.getEyePos();
      if (!flag) {
         Vec3d vec3d = box.getCenter();
         HitResult hitResult = clientWorld.raycast(new RaycastContext(vec3d3, vec3d, ShapeType.OUTLINE, FluidHandling.NONE, playerEntity));
         return hitResult == null || !hitResult.getType().equals(Type.BLOCK) || hitResult.getPos() == null;
      }

      Vec3d[] vec3ds = values(box);

      for (Vec3d vec3d2 : vec3ds) {
         HitResult hitResult2 = clientWorld.raycast(new RaycastContext(vec3d3, vec3d2, ShapeType.OUTLINE, FluidHandling.NONE, playerEntity));
         if (hitResult2 == null || !hitResult2.getType().equals(Type.BLOCK) || hitResult2.getPos() == null) {
            return true;
         }
      }

      return false;
   }

   public static Vec3d[] values(Box box) {
      Vec3d vec3d = new Vec3d(box.minX, box.minY, box.minZ);
      Vec3d vec3d2 = new Vec3d(box.maxX, box.maxY, box.maxZ);
      return new Vec3d[]{
         vec3d,
         new Vec3d(vec3d2.x, vec3d.y, vec3d.z),
         new Vec3d(vec3d.x, vec3d.y, vec3d2.z),
         new Vec3d(vec3d2.x, vec3d.y, vec3d2.z),
         new Vec3d(vec3d.x, vec3d2.y, vec3d.z),
         new Vec3d(vec3d2.x, vec3d2.y, vec3d.z),
         new Vec3d(vec3d.x, vec3d2.y, vec3d2.z),
         new Vec3d(vec3d2.x, vec3d2.y, vec3d2.z),
         box.getCenter()
      };
   }

   public CrystalUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static int computeClientPlayerEntity2(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, BlockPos blockPos, Vec3d vec3d) {
      Vec3d vec3d2 = clientPlayerEntity.getEyePos();
      AtomicInteger atomicInteger = new AtomicInteger();
      RaycastUtils.resolveBlockHitResult12(vec3d2, vec3d, clientPlayerEntity, clientWorld, blockHitResult -> {
         if (blockHitResult == null) {
            return null;
         }

         BlockPos blockPos2 = blockHitResult.getBlockPos();
         if (!clientWorld.getBlockState(blockPos2).isReplaceable()) {
            atomicInteger.incrementAndGet();
         }

         return blockHitResult.getBlockPos().equals(blockPos) ? blockHitResult : null;
      });
      return atomicInteger.get();
   }

   public static boolean testClientPlayerEntity2(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, BlockPos blockPos, float[] floats, double d) {
      Vec3d vec3d = clientPlayerEntity.getEyePos();
      Vec3d vec3d2 = RaycastUtils.createVec3d2(floats[0], floats[1]);
      Vec3d vec3d3 = vec3d.add(vec3d2.x * d, vec3d2.y * d, vec3d2.z * d);
      BlockHitResult blockHitResult = clientWorld.raycast(new RaycastContext(vec3d, vec3d3, ShapeType.COLLIDER, FluidHandling.NONE, clientPlayerEntity));
      return blockHitResult != null && blockHitResult.getBlockPos().equals(blockPos);
   }
}
