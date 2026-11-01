package org.phobos.core.server;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import org.earthhack.ducks.IPendingUpdateManager;
import org.phobos.api.TickGate;
import org.phobos.core.event.events.PacketEvent.Send;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent2;
import org.phobos.mixin.game.world.IClientWorld;
import org.phobos.core.ClientHolder;
import org.phobos.core.Feature;
import org.phobos.core.module.ModuleListenerUtils;

public class LatencyManager extends Feature {
   public final TickGate.Normal normal = new TickGate.Normal();
   public final AtomicInteger atomicInteger = new AtomicInteger();
   public final Predicate<Packet<?>> packetPredicate;
   public final Supplier<Integer> integerSupplier;
   public final Supplier<Integer> integerSupplier2;
   public boolean flag;

   public Supplier<Integer> getIntegerSupplier2() {
      return this.integerSupplier2;
   }

   public TickGate.Normal getNormal() {
      return this.normal;
   }

   public AtomicInteger getAtomicInteger() {
      return this.atomicInteger;
   }

   public Supplier<Integer> getIntegerSupplier() {
      return this.integerSupplier;
   }

   public void handleFlag(boolean flag) {
      this.flag = flag;
      if (!flag) {
         this.normal.setIntValue(0);
         this.atomicInteger.set(0);
      }
   }

   public LatencyManager(Supplier<Integer> supplier, Supplier<Integer> supplier2, Predicate<Packet<?>> predicate) {
      this.integerSupplier2 = supplier;
      this.integerSupplier = supplier2;
      this.packetPredicate = predicate;
   }

   public Predicate<Packet<?>> getPacketPredicate() {
      return this.packetPredicate;
   }

   public boolean isFlag() {
      return this.flag;
   }

   public LatencyManager getLatencyManager() {
      this.handleListener(
         new SafeListener<Send<?>>(Integer.MIN_VALUE) {
            public void handleObject(
               Send<?> send, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (LatencyManager.this.flag && !send.isCancelled() && LatencyManager.this.packetPredicate.test(send.getPacket())) {
                  if (LatencyManager.this.atomicInteger.incrementAndGet() >= LatencyManager.this.integerSupplier2.get()) {
                     send.setCancelled(true);
                     Packet<?> packet = send.getPacket();
                     if (packet instanceof PlayerInteractBlockC2SPacket
                        || packet instanceof PlayerInteractItemC2SPacket
                        || packet instanceof PlayerActionC2SPacket) {
                        ClientHolder.getPhobosClient().getServerAntiCheatDetector().getAtomicInteger().decrementAndGet();
                        ((IPendingUpdateManager)(Object)((IClientWorld)(Object)clientWorld).getPendingUpdateManager()).decrementSequence();
                     }
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<TickEvent2>() {
            public void handleObject(
               TickEvent2 tickEvent2,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (LatencyManager.this.flag && LatencyManager.this.normal.testInt3(LatencyManager.this.integerSupplier.get())) {
                  LatencyManager.this.atomicInteger.set(0);
               }
            }
         }
      );
      ModuleListenerUtils.handleListenerHolder4(this, () -> this.atomicInteger.set(0));
      return this;
   }
}
