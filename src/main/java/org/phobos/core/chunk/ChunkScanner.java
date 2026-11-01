package org.phobos.core.chunk;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.function.Predicate;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.phobos.api.ChunkProcessor;
import org.phobos.api.TaskData;
import org.phobos.core.event.events.PacketEvent.PostReceive;
import org.phobos.event.DisconnectEvent;
import org.phobos.event.Listener;
import org.phobos.event.UnloadEvent;
import org.phobos.event.PostReceiveListener.Safe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.phobos.core.block.MutableBlockPos;
import org.phobos.core.task.TaskQueue;

public abstract class ChunkScanner<T extends TaskData> extends ChunkUpdateTracker<Runnable> implements ChunkProcessor {
   public final MutableBlockPos mutableBlockPos = new MutableBlockPos();
   public final Map<BlockPos, T> blockPosMap;
   public static final Logger LOGGER2 = LoggerFactory.getLogger(ChunkScanner.class);
   public final ExecutorService executorService;

   public abstract ChunkTask<?, ?> createHoleChunkTask(WorldChunk var1, TaskQueue var2);

   public abstract void handleMutableBlockPos(MutableBlockPos var1, BlockState var2, TaskQueue var3);

   @Override
   public void handleClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, World world) {
      for (WorldChunk worldChunk : ChunkUtils.resolveList(clientPlayerEntity, world)) {
         this.handleRunnable(() -> {
            this.resolveTaskQueue(worldChunk).run();
            this.handleWorldChunk(worldChunk);
         });
      }
   }

   @Override
   public void handleBlockPos(BlockPos blockPos, BlockState blockState, WorldChunk worldChunk) {
      this.handleBlockPos2(blockPos, blockState, this.resolveTaskQueue(worldChunk), worldChunk);
   }

   @Override
   public void handleList(List<Runnable> list) {
      list.forEach(Runnable::run);
   }

   public void handleWorldChunk2(WorldChunk worldChunk, TaskQueue taskQueue, ChunkTask<?, ?> chunkTask) {
      this.handleRunnable(() -> {
         try {
            this.handleWorldChunk(worldChunk);
            chunkTask.run();
            mc.submit(() -> taskQueue.handleFlag(false));
         } catch (Exception exception) {
            if (!mc.isOnThread()) {
               chunkTask.run2();
               mc.submit(() -> {
                  chunkTask.run2();
                  chunkTask.run();
                  taskQueue.handleFlag(false);
               });
            } else {
               mc.submit(() -> taskQueue.handleFlag(false));
            }

            throw exception;
         }
      });
   }

   public ChunkScanner(Map<BlockPos, T> map, ExecutorService executorService) {
      this.blockPosMap = map;
      this.executorService = executorService;
      this.handleListener(new Listener<UnloadEvent>() {
         public void handleObject2(UnloadEvent unloadEvent) {
            ChunkScanner.this.handleRunnable(() -> {
               ChunkScanner.this.resolveTaskQueue(unloadEvent.chunk()).run();
               ChunkScanner.this.handleWorldChunk(unloadEvent.chunk());
            });
         }
      });
      this.handleListener(
         new Safe<ChunkDataS2CPacket>(mc) {
            @Override
            public void handlePostReceive(
               PostReceive<ChunkDataS2CPacket> postReceive,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               ChunkDataS2CPacket chunkDataS2CPacket = (ChunkDataS2CPacket)postReceive.getPacket();
               WorldChunk worldChunk = clientWorld.getChunk(chunkDataS2CPacket.getChunkX(), chunkDataS2CPacket.getChunkZ());
               TaskQueue taskQueue = ChunkScanner.this.resolveTaskQueue(worldChunk);
               taskQueue.handleFlag(true);
               taskQueue.run();
               ChunkTask<?, ?> chunkTask = ChunkScanner.this.createHoleChunkTask(worldChunk, taskQueue);
               ChunkScanner.this.handleWorldChunk2(worldChunk, taskQueue, chunkTask);
            }
         }
      );
      this.handleListener(new Listener<DisconnectEvent>() {
         public void handleObject2(DisconnectEvent disconnectEvent) {
            ChunkScanner.this.run2();
         }
      });
   }

   public void run2() {
      this.getBlockPosMap().clear();
   }

   public void handleBlockPos2(BlockPos blockPos, BlockState blockState, TaskQueue taskQueue, WorldChunk worldChunk) {
      this.handleBlockPos4(blockPos, blockState, taskQueue);
      if (this.list == null) {
         this.handleBlockPos3(blockPos, blockState, taskQueue, worldChunk);
      } else {
         this.list.add(() -> this.handleBlockPos3(blockPos, blockState, taskQueue, worldChunk));
      }
   }

   @Override
   public void handleClientPlayerEntity2(ClientPlayerEntity clientPlayerEntity, World world) {
      for (WorldChunk worldChunk : ChunkUtils.resolveList(clientPlayerEntity, world)) {
         TaskQueue taskQueue = this.resolveTaskQueue(worldChunk);
         taskQueue.handleFlag(true);
         taskQueue.run();
         ChunkTask<?, ?> chunkTask = this.createHoleChunkTask(worldChunk, taskQueue);
         this.handleWorldChunk2(worldChunk, taskQueue, chunkTask);
      }
   }

   @Override
   public void handleWorldChunk(WorldChunk worldChunk) {
      this.getBlockPosMap().entrySet().removeIf(Predicate.not(entry -> entry.getValue().isFlag2()));
   }

   @Override
   public void handleRunnable(Runnable runnable) {
      this.executorService.submit(runnable);
   }

   public abstract void handleBlockPos3(BlockPos var1, BlockState var2, TaskQueue var3, WorldChunk var4);

   public Map<BlockPos, T> getBlockPosMap() {
      return this.blockPosMap;
   }

   public void handleBlockPos4(BlockPos blockPos, BlockState blockState, TaskQueue taskQueue) {
      taskQueue.handleRunnable(() -> {
         this.mutableBlockPos.setX(blockPos.getX());
         this.mutableBlockPos.setY(blockPos.getY());
         this.mutableBlockPos.setZ(blockPos.getZ());
         this.handleMutableBlockPos(this.mutableBlockPos, blockState, taskQueue);
      });
   }
}
