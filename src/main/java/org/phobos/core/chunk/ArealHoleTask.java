package org.phobos.core.chunk;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.thread.ThreadExecutor;
import net.minecraft.world.World;
import org.phobos.core.block.MutableBlockPos;
import org.phobos.core.task.TaskQueue;

public class ArealHoleTask extends HoleChunkTask {
   public TaskQueue taskQueue2;

   @Override
   public TaskQueue getTaskQueue() {
      return this.taskQueue2;
   }

   @Override
   public void run3() {
      ChunkTaskUtils.handleMutableBlockPos(
         this.getMutableBlockPos(), mutableBlockPos -> this.handleMutableBlockPos(mutableBlockPos, mutableBlockPos.getX(), mutableBlockPos.getY(), mutableBlockPos.getZ())
      );
   }

   public void setTaskQueue2(TaskQueue taskQueue) {
      this.taskQueue2 = taskQueue;
   }

   @Override
   public void run() {
      throw new UnsupportedOperationException("ArealHoleTask.run() is not supported");
   }

   public ArealHoleTask(ThreadExecutor<Runnable> threadExecutor, World world, HoleChunkScanner holeChunkScanner) {
      super(holeChunkScanner.getBlockPosMap(), threadExecutor, world, new MutableBlockPos(), holeChunkScanner, null, 0, 0, 0, 0, 0, 0);
   }

   public void setBlockPos(BlockPos blockPos) {
      this.getMutableBlockPos().set(blockPos);
   }
}
