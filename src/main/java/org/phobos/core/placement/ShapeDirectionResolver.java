package org.phobos.core.placement;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.BlockState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import org.jetbrains.annotations.Nullable;

public class ShapeDirectionResolver extends DirectionResolver {
   public static ShapeDirectionResolver shapeDirectionResolver = new ShapeDirectionResolver();

   @Override
   public boolean testBlockPos(BlockPos blockPos, Direction direction, ClientWorld clientWorld, PlayerEntity playerEntity) {
      BlockPos blockPos2 = BlockPos.ofFloored(playerEntity.getX(), playerEntity.getEyeY(), playerEntity.getZ());
      if (this.testBlockPos3(blockPos, blockPos2)) {
         return true;
      }

      VoxelShape voxelShape = clientWorld.getBlockState(blockPos).getOutlineShape(clientWorld, blockPos);
      boolean flag = voxelShape == VoxelShapes.fullCube();
      List<Direction> list = this.resolveList2(blockPos, blockPos2, flag);
      return !list.contains(direction) ? false : !this.testBlockPos2(blockPos, clientWorld, list, direction, flag);
   }

   public boolean testBlockPos2(BlockPos blockPos, ClientWorld clientWorld, List<Direction> list, Direction direction, boolean flag) {
      if (flag) {
         BlockPos blockPos2 = blockPos.offset(direction);
         BlockState blockState = clientWorld.getBlockState(blockPos2);
         return blockState.getOutlineShape(clientWorld, blockPos) == VoxelShapes.fullCube() && !blockState.getCollisionShape(clientWorld, blockPos2).isEmpty();
      }

      for (Direction direction2 : list) {
         BlockPos blockPos3 = blockPos.offset(direction2);
         BlockState blockState2 = clientWorld.getBlockState(blockPos3);
         boolean flag2 = blockState2.getOutlineShape(clientWorld, blockPos) == VoxelShapes.fullCube();
         if (!flag2 || !blockState2.getCollisionShape(clientWorld, blockPos3).isEmpty()) {
            return false;
         }
      }

      return true;
   }

   public boolean testBlockPos3(BlockPos blockPos, BlockPos blockPos2) {
      return blockPos2.getX() == blockPos.getX() && blockPos2.getY() == blockPos.getY() && blockPos2.getZ() == blockPos.getZ();
   }

   public List<Direction> resolveList(int i, int i2, int i3, boolean flag) {
      List<Direction> list = new ArrayList<>(6);
      if (!flag) {
         if (i == 0) {
            list.add(Direction.EAST);
            list.add(Direction.WEST);
         }

         if (i3 == 0) {
            list.add(Direction.SOUTH);
            list.add(Direction.NORTH);
         }
      }

      if (i2 == 0) {
         list.add(Direction.UP);
         list.add(Direction.DOWN);
      } else {
         list.add(i2 > 0 ? Direction.UP : Direction.DOWN);
      }

      if (i != 0) {
         list.add(i > 0 ? Direction.EAST : Direction.WEST);
      }

      if (i3 != 0) {
         list.add(i3 > 0 ? Direction.SOUTH : Direction.NORTH);
      }

      return list;
   }

   public List<Direction> resolveList2(BlockPos blockPos, BlockPos blockPos2, boolean flag) {
      int i = blockPos2.getX();
      int i2 = blockPos2.getY();
      int i3 = blockPos2.getZ();
      int i4 = blockPos.getX();
      int i5 = blockPos.getY();
      int i6 = blockPos.getZ();
      return this.resolveList(i - i4, i2 - i5, i3 - i6, flag);
   }

   @Nullable
   @Override
   public Direction resolveDirection(BlockPos blockPos, PlayerEntity playerEntity, ClientWorld clientWorld) {
      BlockPos blockPos2 = BlockPos.ofFloored(playerEntity.getX(), playerEntity.getEyeY(), playerEntity.getZ());
      if (this.testBlockPos3(blockPos, blockPos2)) {
         return Direction.DOWN;
      }

      List<Direction> list = this.resolveList2(blockPos, blockPos2, true);

      for (Direction direction : list) {
         if (!this.testBlockPos2(blockPos, clientWorld, list, direction, true)) {
            return direction;
         }
      }

      return null;
   }
}
