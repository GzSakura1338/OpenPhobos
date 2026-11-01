package org.phobos.core.util;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class NullSafe {
   public static void handleMinecraftClient(MinecraftClient minecraftClient, NullSafe.SafeConsumer safeConsumer) {
      ClientWorld clientWorld = minecraftClient.world;
      ClientPlayerEntity clientPlayerEntity = minecraftClient.player;
      ClientPlayerInteractionManager clientPlayerInteractionManager = minecraftClient.interactionManager;
      if (clientWorld != null && clientPlayerEntity != null && clientPlayerInteractionManager != null) {
         safeConsumer.handleClientPlayerEntity(clientPlayerEntity, clientWorld, clientPlayerInteractionManager);
      }
   }

   public NullSafe() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   @NotNull
   public static <S, T> T resolveObject(@Nullable S object, Function<S, T> function, T object2) {
      return object == null ? object2 : function.apply(object);
   }

   public static <T> void handleObject(T object, Consumer<T> consumer) {
      if (object != null) {
         consumer.accept(object);
      }
   }

   public static <T> T resolveObject2(T object, Supplier<T> supplier) {
      return object != null ? object : supplier.get();
   }

   public static <T> void handleObject2(T object, Consumer<T> consumer, Runnable runnable) {
      if (object != null) {
         consumer.accept(object);
      } else {
         runnable.run();
      }
   }

   public static <T> T resolveObject3(T object, T object2) {
      return object != null ? object : object2;
   }

   @FunctionalInterface
   public interface SafeConsumer {
      void handleClientPlayerEntity(ClientPlayerEntity var1, ClientWorld var2, ClientPlayerInteractionManager var3);
   }
}
