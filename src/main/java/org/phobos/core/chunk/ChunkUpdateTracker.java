package org.phobos.core.chunk;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ChunkDeltaUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerActionResponseS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.WorldChunk;
import org.phobos.core.event.events.PacketEvent.Receive;
import org.phobos.event.SafeListener;
import org.phobos.event.SetBlockStateEvent;
import org.phobos.mixin.net.interfaces.IChunkDeltaUpdateS2CPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.phobos.core.Feature;
import org.phobos.core.net.PacketListenerBase;

public abstract class ChunkUpdateTracker<T> extends Feature {
   public static final Logger LOGGER = LoggerFactory.getLogger(ChunkUpdateTracker.class);
   public List<T> list;

   public abstract void handleList(List<T> var1);

   public void handlePacketListenerBase(PacketListenerBase<?> packetListenerBase, int i) {
      List<T> list = new ArrayList<>(i);
      mc.submit(() -> this.list = list);
      packetListenerBase.handleThreadExecutor(mc, () -> {
         if (list != this.list) {
            LOGGER.error("Callbacks have changed while processing a BlockChange packet!");
         } else {
            this.list = null;
            this.handleList(list);
         }
      });
   }

   public ChunkUpdateTracker() {
      this.handleListenerHolder(new PacketListenerBase<PlayerActionResponseS2CPacket>(PlayerActionResponseS2CPacket.class) {
         @Override
         public void handleReceive(Receive<PlayerActionResponseS2CPacket> receive) {
            ChunkUpdateTracker.this.handlePacketListenerBase(this, 0);
         }
      });
      this.handleListenerHolder(new PacketListenerBase<BlockUpdateS2CPacket>() {
         @Override
         public void handleReceive(Receive<BlockUpdateS2CPacket> receive) {
            ChunkUpdateTracker.this.handlePacketListenerBase(this, 1);
         }
      });
      this.handleListenerHolder(new PacketListenerBase<ChunkDeltaUpdateS2CPacket>() {
         @Override
         public void handleReceive(Receive<ChunkDeltaUpdateS2CPacket> receive) {
            int i = ((IChunkDeltaUpdateS2CPacket)(Object)receive.getPacket()).getPositions().length;
            ChunkUpdateTracker.this.handlePacketListenerBase(this, i);
         }
      });
      this.handleListener(
         new SafeListener<SetBlockStateEvent>() {
            public void handleObject(
               SetBlockStateEvent setBlockStateEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               ChunkUpdateTracker.this.handleBlockPos(setBlockStateEvent.pos().toImmutable(), setBlockStateEvent.state(), setBlockStateEvent.chunk());
            }
         }
      );
   }

   public abstract void handleBlockPos(BlockPos var1, BlockState var2, WorldChunk var3);
}
