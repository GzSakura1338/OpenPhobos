package org.phobos.core.chunk;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.WorldChunk;
import org.phobos.api.HoleScanner;
import org.phobos.api.ChunkWorkerHolder;
import org.phobos.api.SafeBlocks;
import org.phobos.core.block.MutableBlockPos;
import org.phobos.core.task.TaskQueue;

public class HoleChunkScanner extends ChunkScanner<Hole> implements SafeBlocks, HoleScanner {
   @Override
   public void handleBlockPos3(BlockPos blockPos, BlockState blockState, TaskQueue taskQueue, WorldChunk worldChunk) {
      taskQueue.handleRunnable(() -> {
         ArealHoleTask arealHoleTask = new ArealHoleTask(mc, worldChunk.getWorld(), this);
         arealHoleTask.setTaskQueue2(taskQueue);
         arealHoleTask.setBlockPos(blockPos);
         arealHoleTask.run3();
      });
   }

   public HoleChunkScanner(Map<BlockPos, Hole> map, ExecutorService executorService) {
      super(map, executorService);
   }

   @Override
   public void handleMutableBlockPos(MutableBlockPos mutableBlockPos, BlockState blockState, TaskQueue taskQueue) {
      ChunkTaskUtils.handleMutableBlockPos(this.mutableBlockPos, mutableBlockPosx -> {
         Hole hole = this.blockPosMap.get(mutableBlockPosx);
         if (hole != null && hole.testBlockPos(mutableBlockPosx)) {
            this.blockPosMap.remove(mutableBlockPosx);
            hole.run();
         }
      });
   }

   public Hole resolveHole(PlayerEntity playerEntity) {
      return this.resolveHole3(playerEntity, hole -> true);
   }

   public Hole resolveHole2(
      Predicate<Hole> predicate, Function<Hole, Double> function, Function<Hole, Double> function2
   ) {
      return this.getBlockPosMap().values().stream().filter(predicate).min(Comparator.comparingDouble(function::apply).thenComparing(function2)).orElse(null);
   }

   @Override
   public TaskQueue resolveTaskQueue(WorldChunk worldChunk) {
      return ((ChunkWorkerHolder)worldChunk).getHoleChunkWorker();
   }

   public Hole resolveHole3(PlayerEntity playerEntity, Predicate<Hole> predicate) {
      return this.getBlockPosMap()
         .values()
         .stream()
         .filter(predicate)
         .min(Comparator.comparingDouble(hole -> hole.computeEntity(playerEntity)))
         .orElse(null);
   }

   @Override
   public ChunkTask<?, ?> createHoleChunkTask(WorldChunk worldChunk, TaskQueue taskQueue) {
      return new HoleChunkTask(mc, worldChunk, taskQueue, 320, -64, this);
   }

   public Collection<Hole> getCollection() {
      return this.getBlockPosMap().values();
   }
}
