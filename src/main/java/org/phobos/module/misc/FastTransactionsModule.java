package org.phobos.module.misc;

import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.network.packet.c2s.common.CommonPongC2SPacket;
import net.minecraft.network.packet.c2s.common.KeepAliveC2SPacket;
import net.minecraft.network.packet.c2s.common.ResourcePackStatusC2SPacket;
import net.minecraft.network.packet.c2s.common.ResourcePackStatusC2SPacket.Status;
import net.minecraft.network.packet.s2c.common.CommonPingS2CPacket;
import net.minecraft.network.packet.s2c.common.KeepAliveS2CPacket;
import net.minecraft.network.packet.s2c.common.ResourcePackSendS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import org.phobos.api.MinecraftHolder;
import org.phobos.core.module.ModuleGroups;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.event.events.PacketEvent.PostReceive;
import org.phobos.core.event.events.PacketEvent.Receive;
import org.phobos.event.Listener;
import org.phobos.module.Module;

public class FastTransactionsModule extends Module {
   public final AtomicInteger atomicInteger = new AtomicInteger();

   public FastTransactionsModule(PhobosClient phobosClient) {
      super(phobosClient, "FastTransactions", Categories.misc);
      this.handleListener(new Listener<Receive<KeepAliveS2CPacket>>() {
         public void handleObject2(Receive<KeepAliveS2CPacket> receive) {
            synchronized (FastTransactionsModule.this.atomicInteger) {
               if (FastTransactionsModule.this.check()) {
                  receive.setCancelled(true);
                  MinecraftHolder.mc.getNetworkHandler().sendPacket(new KeepAliveC2SPacket(((KeepAliveS2CPacket)receive.getPacket()).getId()));
               }
            }
         }
      });
      this.handleListener(new Listener<Receive<CommonPingS2CPacket>>() {
         public void handleObject2(Receive<CommonPingS2CPacket> receive) {
            synchronized (FastTransactionsModule.this.atomicInteger) {
               if (FastTransactionsModule.this.check()) {
                  receive.setCancelled(true);
                  MinecraftHolder.mc.getNetworkHandler().sendPacket(new CommonPongC2SPacket(((CommonPingS2CPacket)receive.getPacket()).getParameter()));
               }
            }
         }
      });
      this.handleListener(
         new Listener<Receive<ResourcePackSendS2CPacket>>() {
            public void handleObject2(Receive<ResourcePackSendS2CPacket> receive) {
               synchronized (FastTransactionsModule.this.atomicInteger) {
                  if (FastTransactionsModule.this.check()) {
                     receive.setCancelled(true);
                     MinecraftHolder.mc
                        .getNetworkHandler()
                        .sendPacket(new ResourcePackStatusC2SPacket(((ResourcePackSendS2CPacket)receive.getPacket()).id(), Status.FAILED_DOWNLOAD));
                  }
               }
            }
         }
      );
      this.handleListener(new Listener<Receive<PlayerPositionLookS2CPacket>>() {
         public void handleObject2(Receive<PlayerPositionLookS2CPacket> receive) {
            synchronized (FastTransactionsModule.this.atomicInteger) {
               FastTransactionsModule.this.atomicInteger.incrementAndGet();
            }
         }
      });
      this.handleListener(new Listener<PostReceive<PlayerPositionLookS2CPacket>>() {
         public void handleObject2(PostReceive<PlayerPositionLookS2CPacket> postReceive) {
            MinecraftHolder.mc.submit(() -> {
               synchronized (FastTransactionsModule.this.atomicInteger) {
                  FastTransactionsModule.this.atomicInteger.decrementAndGet();
               }
            });
         }
      });
      ModuleGroups.getINSTANCE().handleText("beta", this);
   }

   public boolean check() {
      return this.atomicInteger.get() == 0 && mc.getNetworkHandler() != null;
   }
}
