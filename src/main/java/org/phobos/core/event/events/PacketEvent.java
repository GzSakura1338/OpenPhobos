package org.phobos.core.event.events;

import net.minecraft.network.packet.Packet;

public class PacketEvent<P extends Packet<?>> extends CancellableEvent {
   public final P packet;

   public P getPacket() {
      return this.packet;
   }

   public PacketEvent(P packet) {
      this.packet = packet;
   }

   public static class PostReceive<P extends Packet<?>> extends PacketEvent<P> {
      public PostReceive(P packet) {
         super(packet);
      }
   }

   public static class PostSend<P extends Packet<?>> extends PacketEvent<P> {
      public PostSend(P packet) {
         super(packet);
      }
   }

   public static class Receive<P extends Packet<?>> extends PacketEvent<P> {
      public Receive(P packet) {
         super(packet);
      }
   }

   public static class Send<P extends Packet<?>> extends PacketEvent<P> {
      public Send(P packet) {
         super(packet);
      }
   }
}
