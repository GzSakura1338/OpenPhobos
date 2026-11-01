package org.phobos.module;

import java.util.LinkedHashSet;
import java.util.Set;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.jetbrains.annotations.ApiStatus.OverrideOnly;
import org.phobos.core.event.events.PacketEvent.Send;
import org.phobos.event.SafeListener;
import org.phobos.module.Module;
import org.phobos.core.module.Category;
import org.phobos.core.PhobosClient;

public class PacketHoldModule extends Module {
   public final Set<Packet<?>> packetSet = new LinkedHashSet<>();
   public boolean flag2;
   public boolean flag3;

   public PacketHoldModule(PhobosClient phobosClient, String text, String text2, Category category) {
      super(phobosClient, text, text2, category);
      this.handleListener(
         new SafeListener<Send<?>>(2000) {
            public void handleObject(
               Send<?> send, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               Packet<?> packet = send.getPacket();
               if (PacketHoldModule.this.flag3 && (!PacketHoldModule.this.flag2 || packet instanceof PlayerMoveC2SPacket)) {
                  if (PacketHoldModule.this.testPacket(packet)) {
                     synchronized (PacketHoldModule.this.packetSet) {
                        PacketHoldModule.this.packetSet.add(packet);
                        send.setCancelled(true);
                     }
                  }
               }
            }
         }
      );
   }

   public void run() {
      this.flag3 = false;
   }

   public void run2() {
      this.flag3 = true;
   }

   @OverrideOnly
   public boolean testPacket(Packet<?> packet) {
      return true;
   }

   public void setFlag2(boolean flag) {
      this.flag2 = flag;
   }

   public PacketHoldModule(PhobosClient phobosClient, String text, Category category) {
      this(phobosClient, text, "", category);
   }

   public PacketHoldModule(PhobosClient phobosClient, String text, Category category, String text2) {
      this(phobosClient, text, text2, category);
   }

   public void run5() {
      if (mc.player != null) {
         synchronized (this.packetSet) {
            for (Packet<?> packet : this.packetSet) {
               mc.player.networkHandler.sendPacket(packet);
            }

            this.packetSet.clear();
         }
      }
   }
}
