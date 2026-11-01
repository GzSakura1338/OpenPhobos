package org.phobos.core.net;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.phobos.api.MinecraftHolder;
import org.phobos.core.event.events.PacketEvent.Send;
import org.phobos.event.Listener;
import org.phobos.core.ClientHolder;
import org.phobos.core.Feature;
import org.phobos.core.render.texture.SkyboxTextureUtils;

public class PacketBuffer extends Feature {
   public final AtomicInteger atomicInteger;
   public final Object object = new Object[0];
   public final AtomicBoolean atomicBoolean;
   public final List<Consumer<PacketBuffer>> consumerList;
   public final List<Packet<?>> packetList = new ArrayList<>();

   public PacketBuffer(Supplier<Boolean> supplier) {
      this.consumerList = new ArrayList<>();
      this.atomicInteger = new AtomicInteger();
      this.atomicBoolean = new AtomicBoolean();
      this.handleListener(new Listener<Send<?>>() {
         public void handleObject2(Send<?> send) {
            if (!MinecraftHolder.mc.isInSingleplayer()) {
               Packet<?> packet = send.getPacket();
               if (packet instanceof PlayerMoveC2SPacket || supplier.get()) {
                  PacketBuffer.this.atomicInteger.incrementAndGet();
                  PacketBuffer.this.consumerList.forEach(consumer -> consumer.accept(PacketBuffer.this));
                  send.setCancelled(true);
                  PacketBuffer.this.packetList.add(packet);
               }
            }
         }
      });
   }

   public void run() {
      synchronized (this.object) {
         if (!mc.isInSingleplayer()) {
            if (mc.player != null) {
               this.packetList.forEach(SkyboxTextureUtils::handlePacket);
               this.packetList.clear();
               this.atomicInteger.set(0);
            }
         }
      }
   }

   public List<Consumer<PacketBuffer>> getConsumerList() {
      return this.consumerList;
   }

   public List<Packet<?>> getPacketList() {
      return this.packetList;
   }

   public AtomicBoolean getAtomicBoolean() {
      return this.atomicBoolean;
   }

   public AtomicInteger getAtomicInteger() {
      return this.atomicInteger;
   }

   public void handleConsumer(Consumer<PacketBuffer> consumer) {
      this.consumerList.add(consumer);
   }

   public void run2() {
      ClientHolder.getEventBus().handleObject4(this);
      this.atomicBoolean.set(false);
   }

   public boolean check() {
      return this.atomicBoolean.get();
   }

   public void run3() {
      if (mc.player != null) {
         LagState.handleClientPlayerEntity(mc.player);
      }

      ClientHolder.getEventBus().handleObject(this);
      this.atomicBoolean.set(true);
   }
}
