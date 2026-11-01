package org.phobos.core.chunk;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.phobos.api.MinecraftHolder;

public final class ChunkUtils implements MinecraftHolder {
   public static List<WorldChunk> resolveList(ClientPlayerEntity clientPlayerEntity, World world) {
      ChunkPos chunkPos = new ChunkPos(clientPlayerEntity.getBlockPos());
      int i3 = (Integer)mc.options.getViewDistance().getValue();
      if (i3 <= 0) {
         i3 = 10;
      }

      int i4 = chunkPos.x - i3;
      int i5 = chunkPos.x + i3;
      int i6 = chunkPos.z - i3;
      int i7 = chunkPos.z + i3;
      List<WorldChunk> list = new ArrayList<>();

      for (int i2 = i4; i2 <= i5; i2++) {
         for (int i = i6; i <= i7; i++) {
            WorldChunk worldChunk = world.getChunkManager().getWorldChunk(i2, i);
            if (worldChunk != null) {
               list.add(worldChunk);
            }
         }
      }

      return list;
   }

   public ChunkUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
