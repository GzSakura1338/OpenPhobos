package org.phobos.api;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.phobos.core.task.TaskQueue;

public interface ChunkProcessor {
   TaskQueue resolveTaskQueue(WorldChunk var1);

   void handleClientPlayerEntity(ClientPlayerEntity var1, World var2);

   void handleWorldChunk(WorldChunk var1);

   void handleClientPlayerEntity2(ClientPlayerEntity var1, World var2);

   void handleRunnable(Runnable var1);
}
