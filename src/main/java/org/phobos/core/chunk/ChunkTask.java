package org.phobos.core.chunk;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.thread.ThreadExecutor;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.TaskData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.phobos.core.block.MutableBlockPos;
import org.phobos.core.task.TaskQueue;

public abstract class ChunkTask<I extends TaskData, M extends ChunkScanner<I>> implements Runnable {
   public final int intValue;
   public final int intValue2;
   public final int intValue3;
   public final ThreadExecutor<Runnable> runnableThreadExecutor;
   public final Map<BlockPos, I> blockPosMap;
   @Nullable
   public final TaskQueue taskQueue;
   public final int intValue4;
   public final M chunkScanner;
   public final int intValue5;
   public final MutableBlockPos mutableBlockPos;
   public final int intValue6;
   public static final Logger LOGGER = LoggerFactory.getLogger(ChunkTask.class);
   public final World world;

   public int getIntValue4() {
      return this.intValue4;
   }

   @Nullable
   public TaskQueue getTaskQueue() {
      return this.taskQueue;
   }

   public void run3() {
      for (int i3 = this.getIntValue3(); i3 <= this.getIntValue6(); i3++) {
         for (int i2 = this.getIntValue2(); i2 < this.getIntValue5(); i2++) {
            for (int i = this.getIntValue(); i < this.getIntValue4(); i++) {
               this.mutableBlockPos.set(i2, i3, i);
               if (!this.blockPosMap.containsKey(this.mutableBlockPos)) {
                  this.handleMutableBlockPos(this.mutableBlockPos, i2, i3, i);
               }
            }
         }
      }
   }

   public int getIntValue3() {
      return this.intValue3;
   }

   public ChunkTask(ThreadExecutor<Runnable> threadExecutor, WorldChunk worldChunk, TaskQueue taskQueue, int i, int i2, M chunkScanner) {
      this(
         new HashMap<>(),
         threadExecutor,
         worldChunk.getWorld(),
         new MutableBlockPos(),
         chunkScanner,
         taskQueue,
         worldChunk.getPos().x * 16 - 1,
         worldChunk.getPos().x * 16 + 16 + 1,
         i2,
         i,
         worldChunk.getPos().z * 16 - 1,
         worldChunk.getPos().z * 16 + 16 + 1
      );
   }

   public ThreadExecutor<Runnable> getRunnableThreadExecutor() {
      return this.runnableThreadExecutor;
   }

   public MutableBlockPos getMutableBlockPos() {
      return this.mutableBlockPos;
   }

   public int getIntValue6() {
      return this.intValue6;
   }

   public abstract void handleBlockPos(BlockPos var1, I var2, I var3);

   public Map<BlockPos, I> getBlockPosMap() {
      return this.blockPosMap;
   }

   public M getChunkScanner() {
      return this.chunkScanner;
   }

   public World getWorld() {
      return this.world;
   }

   public int getIntValue2() {
      return this.intValue2;
   }

   public int getIntValue5() {
      return this.intValue5;
   }

   public void run4() {
      this.runnableThreadExecutor.submit(() -> this.blockPosMap.forEach((blockPos, taskData) -> {
         I taskData2 = this.chunkScanner.getBlockPosMap().put(blockPos, (I)taskData);
         if (taskData2 != null && taskData2 != taskData) {
            this.handleBlockPos(blockPos, taskData2, (I)taskData);
         }
      }));
   }

   public abstract void handleMutableBlockPos(MutableBlockPos var1, int var2, int var3, int var4);

   public ChunkTask(
      Map<BlockPos, I> map,
      ThreadExecutor<Runnable> threadExecutor,
      World world,
      MutableBlockPos mutableBlockPos,
      M chunkScanner,
      @Nullable TaskQueue taskQueue,
      int i,
      int i2,
      int i3,
      int i4,
      int i5,
      int i6
   ) {
      this.blockPosMap = map;
      this.runnableThreadExecutor = threadExecutor;
      this.world = world;
      this.mutableBlockPos = mutableBlockPos;
      this.chunkScanner = chunkScanner;
      this.taskQueue = taskQueue;
      this.intValue2 = i;
      this.intValue5 = i2;
      this.intValue3 = i3;
      this.intValue6 = i4;
      this.intValue = i5;
      this.intValue4 = i6;
   }

   @Override
   public void run() {
      this.run3();
      this.run4();
   }

   public int getIntValue() {
      return this.intValue;
   }

   public void run2() {
      this.blockPosMap.clear();
   }
}
