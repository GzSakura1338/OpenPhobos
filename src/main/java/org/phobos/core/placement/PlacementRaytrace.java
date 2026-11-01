package org.phobos.core.placement;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.PistonHeadBlock;
import net.minecraft.block.enums.PistonType;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Boxes;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;

public final class PlacementRaytrace {
   public static double computeBox(Box box, Direction direction, Box box2) {
      return switch (direction) {
         case EAST -> box.maxX - box2.minX;
         case WEST -> box2.maxX - box.minX;
         case DOWN -> box2.maxY - box.minY;
         case SOUTH -> box.maxZ - box2.minZ;
         case NORTH -> box2.maxZ - box.minZ;
         default -> box.maxY - box2.minY;
      };
   }

   public PlacementRaytrace() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static Box resolveBox(BlockPos blockPos, Box box, Direction direction) {
      double d = 0.0;
      return box.offset(
         blockPos.getX() + d * direction.getOffsetX(), blockPos.getY() + d * direction.getOffsetY(), blockPos.getZ() + d * direction.getOffsetZ()
      );
   }

   public static PlacementRaytrace.Result resolveResult(World world, BlockPos blockPos, Direction direction) {
      PlacementRaytrace.Result result = new PlacementRaytrace.Result();
      double d5 = 0.5;
      BlockState blockState = (BlockState)((BlockState)((BlockState)Blocks.PISTON_HEAD.getDefaultState().with(FacingBlock.FACING, direction))
            .with(PistonHeadBlock.SHORT, true))
         .with(PistonHeadBlock.TYPE, PistonType.DEFAULT);
      VoxelShape voxelShape = blockState.getCollisionShape(world, blockPos);
      if (voxelShape.isEmpty()) {
         return result;
      }

      Box box4 = resolveBox(blockPos, voxelShape.getBoundingBox(), direction);
      List<Entity> list = world.getOtherEntities(null, Boxes.stretch(box4, direction, d5).union(box4));
      if (list.isEmpty()) {
         return result;
      }

      List<Box> list2 = voxelShape.getBoundingBoxes();
      boolean flag = false;
      Iterator<Entity> iterator = list.iterator();

      while (true) {
         Entity entity;
         while (true) {
            if (!iterator.hasNext()) {
               return result;
            }

            entity = iterator.next();
            if (entity.getPistonBehavior() != PistonBehavior.IGNORE) {
               if (!flag) {
                  break;
               }

               if (!(entity instanceof ServerPlayerEntity)) {
                  Vec3d vec3d = entity.getVelocity();
                  double d = vec3d.x;
                  double d2 = vec3d.y;
                  double d3 = vec3d.z;
                  switch (direction.getAxis()) {
                     case X:
                        d = direction.getOffsetX();
                        break;
                     case Y:
                        d2 = direction.getOffsetY();
                        break;
                     case Z:
                        d3 = direction.getOffsetZ();
                  }

                  entity.setVelocity(d, d2, d3);
                  break;
               }
            }
         }

         double d4 = 0.0;

         for (Box box3 : list2) {
            Box box = Boxes.stretch(resolveBox(blockPos, box3, direction), direction, d5);
            Box box2 = entity.getBoundingBox();
            if (box.intersects(box2)) {
               d4 = Math.max(d4, computeBox(box, direction, box2));
               if (d4 >= d5) {
                  break;
               }
            }
         }

         if (!(d4 <= 0.0)) {
            d4 = Math.min(d4, d5) + 0.01;
            handleResult2(result, entity, d4, direction);
            handleResult(result, blockPos, entity, direction, d5);
         }
      }
   }

   public static void handleResult(PlacementRaytrace.Result result, BlockPos blockPos, Entity entity, Direction direction, double d) {
      Box box = entity.getBoundingBox();
      Box box2 = VoxelShapes.fullCube().getBoundingBox().offset(blockPos);
      if (box.intersects(box2)) {
         Direction direction2 = direction.getOpposite();
         double d2 = computeBox(box2, direction2, box) + 0.01;
         double d3 = computeBox(box2, direction2, box.intersection(box2)) + 0.01;
         if (!(Math.abs(d2 - d3) >= 0.01)) {
            d2 = Math.min(d2, d) + 0.01;
            handleResult2(result, entity, d2, direction2);
         }
      }
   }

   public static void handleResult2(PlacementRaytrace.Result result, Entity entity, double d, Direction direction) {
      result.handleEntity(entity, new Vec3d(d * direction.getOffsetX(), d * direction.getOffsetY(), d * direction.getOffsetZ()));
   }

   public static class Result {
      public final Map<Entity, Vec3d> entityMap = new HashMap<>();

      public boolean testEntity(Entity entity) {
         return this.entityMap.containsKey(entity);
      }

      public void handleEntity(Entity entity, Vec3d vec3d) {
         this.entityMap.put(entity, vec3d);
      }
   }
}
