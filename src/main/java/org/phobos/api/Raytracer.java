package org.phobos.api;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.phobos.core.math.MutableBox;
import org.phobos.mixin.game.render.interfaces.IVoxelShape;

@FunctionalInterface
public interface Raytracer {
   static Raytracer createRaytracerForCC() {
      return new Raytracer.RaytracerForCC();
   }

   static Raytracer getRaytracer() {
      return (world, mutableBox, raycastContext) -> world.raycast(raycastContext);
   }

   BlockHitResult resolveBlockHitResult2(World var1, MutableBox var2, RaycastContext var3);

   class RaytracerForCC implements Raytracer {
      public static final Box box = new Box(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
      public static final Box box2 = new Box(0.0625, 0.0, 0.0625, 0.9375, 0.875, 0.9375);

      public BlockHitResult resolveBlockHitResult(
         MutableBox mutableBox, VoxelShape voxelShape, Vec3d vec3d, Vec3d vec3d2, Vec3d vec3d3, Vec3d vec3d4, BlockPos blockPos, BlockState blockState
      ) {
         if (voxelShape.isEmpty()) {
            return null;
         } else {
            IVoxelShape iVoxelShape = (IVoxelShape)(Object)voxelShape;
            if (iVoxelShape.getVoxels()
               .inBoundsAndContains(
                  iVoxelShape.index(Axis.X, vec3d4.x - blockPos.getX()),
                  iVoxelShape.index(Axis.Y, vec3d4.y - blockPos.getY()),
                  iVoxelShape.index(Axis.Z, vec3d4.z - blockPos.getZ())
               )) {
               return new BlockHitResult(vec3d4, Direction.getFacing(vec3d3.x, vec3d3.y, vec3d3.z).getOpposite(), blockPos, true);
            } else if (voxelShape == VoxelShapes.fullCube()) {
               mutableBox.handleBox(box);
               return mutableBox.createBlockHitResult(vec3d, vec3d2, blockPos);
            } else if (blockState.isOf(Blocks.ENDER_CHEST)) {
               mutableBox.handleBox(box2);
               return mutableBox.createBlockHitResult(vec3d, vec3d2, blockPos);
            } else {
               return Box.raycast(voxelShape.getBoundingBoxes(), vec3d, vec3d2, blockPos);
            }
         }
      }

      @Override
      public BlockHitResult resolveBlockHitResult2(World world, MutableBox mutableBox, RaycastContext raycastContext) {
         Vec3d vec3d = raycastContext.getEnd().subtract(raycastContext.getStart());
         if (vec3d.lengthSquared() < 1.0E-7) {
            return BlockHitResult.createMissed(
               raycastContext.getEnd(), Direction.getFacing(vec3d.x, vec3d.y, vec3d.z), BlockPos.ofFloored(raycastContext.getEnd())
            );
         }

         Vec3d vec3d2 = raycastContext.getStart().add(vec3d.multiply(0.001));
         return (BlockHitResult)BlockView.raycast(
            raycastContext.getStart(),
            raycastContext.getEnd(),
            raycastContext,
            (raycastContextx, blockPos) -> {
               BlockState blockState = world.getBlockState(blockPos);
               if (blockState.isAir()) {
                  return null;
               }

               VoxelShape voxelShape = raycastContextx.getBlockShape(blockState, world, blockPos);
               return this.resolveBlockHitResult(
                  mutableBox, voxelShape, raycastContextx.getStart(), raycastContextx.getEnd(), vec3d, vec3d2, blockPos, blockState
               );
            },
            raycastContext2 -> BlockHitResult.createMissed(
               raycastContext.getEnd(), Direction.getFacing(vec3d.x, vec3d.y, vec3d.z), BlockPos.ofFloored(raycastContext.getEnd())
            )
         );
      }
   }
}
