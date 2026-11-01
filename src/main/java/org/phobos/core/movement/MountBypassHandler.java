package org.phobos.core.movement;

import java.util.function.Supplier;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractDonkeyEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket.InteractType;
import org.earthhack.ducks.IPlayerInteractEntityC2SPacket;
import org.phobos.core.event.events.PacketEvent.Send;
import org.phobos.event.SafeListener;
import org.phobos.core.Feature;

public class MountBypassHandler extends Feature {
   public static boolean flag = false;

   public static void run7() {
      flag = true;
   }

   public static void handleRunnable(Runnable runnable) {
      run7();
      runnable.run();
      run2();
   }

   public static void run2() {
      flag = false;
   }

   public MountBypassHandler(Supplier<Boolean> supplier) {
      this.handleListener(
         new SafeListener<Send<PlayerInteractEntityC2SPacket>>() {
            public void handleObject(
               Send<PlayerInteractEntityC2SPacket> send,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (!MountBypassHandler.flag && supplier.get()) {
                  IPlayerInteractEntityC2SPacket iPlayerInteractEntityC2SPacket = (IPlayerInteractEntityC2SPacket)(Object)send.getPacket();
                  if (InteractType.INTERACT_AT.equals(iPlayerInteractEntityC2SPacket.getType().getType())) {
                     Entity entity = iPlayerInteractEntityC2SPacket.getEntityById(clientWorld);
                     if (entity instanceof AbstractDonkeyEntity) {
                        send.setCancelled(true);
                     }
                  }
               }
            }
         }
      );
   }
}
