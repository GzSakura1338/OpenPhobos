package org.phobos.event;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import org.phobos.core.event.events.CancellableEvent;

public class ComputeNextEvent extends CancellableEvent {
   public final BlockPos blockPos;
   public VoxelShape voxelShape;
   public BlockState blockState;

   public void setVoxelShape(VoxelShape voxelShape) {
      this.voxelShape = voxelShape;
   }

   public BlockState getBlockState() {
      return this.blockState;
   }

   public BlockPos getBlockPos() {
      return this.blockPos;
   }

   public ComputeNextEvent(BlockState blockState, BlockPos blockPos, VoxelShape voxelShape) {
      this.blockState = blockState;
      this.blockPos = blockPos;
      this.voxelShape = voxelShape;
   }

   public void setBlockState(BlockState blockState) {
      this.blockState = blockState;
   }

   public VoxelShape getVoxelShape() {
      return this.voxelShape;
   }
}
