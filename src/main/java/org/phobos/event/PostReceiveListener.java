package org.phobos.event;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.Packet;
import org.phobos.core.event.events.PacketEvent;

public abstract class PostReceiveListener<P extends Packet<?>> extends Listener<PacketEvent.PostReceive<P>> {
   public final MinecraftClient minecraftClient;

   public abstract void handlePostReceive2(PacketEvent.PostReceive<P> var1);

   public void handleObject2(PacketEvent.PostReceive<P> postReceive) {
      this.minecraftClient.submit(() -> this.handlePostReceive2(postReceive));
   }

   public PostReceiveListener(MinecraftClient minecraftClient) {
      this(10, minecraftClient);
   }

   public PostReceiveListener(int i, MinecraftClient minecraftClient) {
      super(PacketEvent.PostReceive.class, i);
      this.minecraftClient = minecraftClient;
   }

   public abstract static class Safe<P extends Packet<?>> extends PostReceiveListener<P> {
      public abstract void handlePostReceive(
         PacketEvent.PostReceive<P> var1, ClientPlayerEntity var2, ClientWorld var3, ClientPlayerInteractionManager var4
      );

      public Safe(MinecraftClient minecraftClient) {
         super(minecraftClient);
      }

      public Safe(int i, MinecraftClient minecraftClient) {
         super(i, minecraftClient);
      }

      @Override
      public void handlePostReceive2(PacketEvent.PostReceive<P> postReceive) {
         ClientPlayerEntity clientPlayerEntity = this.minecraftClient.player;
         ClientWorld clientWorld = this.minecraftClient.world;
         ClientPlayerInteractionManager clientPlayerInteractionManager = this.minecraftClient.interactionManager;
         if (clientPlayerEntity != null && clientWorld != null) {
            this.handlePostReceive(postReceive, clientPlayerEntity, clientWorld, clientPlayerInteractionManager);
         }
      }

      public abstract static class Direct<P extends Packet<?>> extends PostReceiveListener.Safe<P> {
         public abstract void handlePacket(P var1, ClientPlayerEntity var2, ClientWorld var3, ClientPlayerInteractionManager var4);

         public Direct(MinecraftClient minecraftClient) {
            super(minecraftClient);
         }

         public Direct(int i, MinecraftClient minecraftClient) {
            super(i, minecraftClient);
         }

         @Override
         public void handlePostReceive(
            PacketEvent.PostReceive<P> postReceive,
            ClientPlayerEntity clientPlayerEntity,
            ClientWorld clientWorld,
            ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            this.handlePacket(postReceive.getPacket(), clientPlayerEntity, clientWorld, clientPlayerInteractionManager);
         }
      }
   }
}
