package org.phobos.event;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.Packet;
import org.phobos.core.event.events.PacketEvent;

public abstract class PriorityListener<P extends Packet<?>> extends Listener<PacketEvent.Receive<P>> {
   public PriorityListener() {
      this(10);
   }

   public PriorityListener(int i) {
      super(PacketEvent.Receive.class, i);
   }

   public abstract static class Direct<P extends Packet<?>> extends PriorityListener<P> {
      public void handleObject2(PacketEvent.Receive<P> receive) {
         this.handlePacket2(receive.getPacket());
      }

      public abstract void handlePacket2(P var1);
   }

   public abstract static class Scheduled<P extends Packet<?>> extends PriorityListener<P> {
      public final MinecraftClient minecraftClient;

      public Scheduled(MinecraftClient minecraftClient) {
         this.minecraftClient = minecraftClient;
      }

      public Scheduled(MinecraftClient minecraftClient, int i) {
         super(i);
         this.minecraftClient = minecraftClient;
      }

      public abstract void handleReceive(PacketEvent.Receive<P> var1);

      public void handleObject2(PacketEvent.Receive<P> receive) {
         this.minecraftClient.submit(() -> this.handleReceive(receive));
      }

      public abstract static class Safe<P extends Packet<?>> extends PriorityListener.Scheduled<P> {
         @Override
         public void handleReceive(PacketEvent.Receive<P> receive) {
            ClientPlayerEntity clientPlayerEntity = this.minecraftClient.player;
            ClientWorld clientWorld = this.minecraftClient.world;
            ClientPlayerInteractionManager clientPlayerInteractionManager = this.minecraftClient.interactionManager;
            if (clientPlayerEntity != null && clientWorld != null && clientPlayerInteractionManager != null) {
               this.handleReceive2(receive, clientPlayerEntity, clientWorld, clientPlayerInteractionManager);
            }
         }

         public Safe(MinecraftClient minecraftClient) {
            super(minecraftClient);
         }

         public Safe(MinecraftClient minecraftClient, int i) {
            super(minecraftClient, i);
         }

         public abstract void handleReceive2(PacketEvent.Receive<P> var1, ClientPlayerEntity var2, ClientWorld var3, ClientPlayerInteractionManager var4);
      }
   }
}
