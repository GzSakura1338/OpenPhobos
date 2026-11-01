package org.phobos.core.tick;

import java.util.function.Consumer;
import net.minecraft.client.network.PendingUpdateManager;
import net.minecraft.client.world.ClientWorld;
import org.phobos.api.MinecraftHolder;
import org.phobos.mixin.game.world.IClientWorld;

public final class TickUtils implements MinecraftHolder {
   public static void run() {
      if (mc.world != null) {
         IClientWorld iClientWorld = (IClientWorld)(Object)mc.world;
         iClientWorld.getPendingUpdateManager().incrementSequence().close();
      }
   }

   public static void handleClientWorld(ClientWorld clientWorld, Consumer<Integer> consumer) {
      PendingUpdateManager pendingUpdateManager = ((IClientWorld)(Object)clientWorld).getPendingUpdateManager().incrementSequence();

      try {
         int i = pendingUpdateManager.getSequence();
         consumer.accept(i);
      } catch (Throwable var6) {
         if (pendingUpdateManager != null) {
            try {
               pendingUpdateManager.close();
            } catch (Throwable var5) {
               var6.addSuppressed(var5);
            }
         }

         throw var6;
      }

      if (pendingUpdateManager != null) {
         pendingUpdateManager.close();
      }
   }

   public TickUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static void handleConsumer2(Consumer<Integer> consumer) {
      if (mc.world != null) {
         handleClientWorld(mc.world, consumer);
      }
   }
}
