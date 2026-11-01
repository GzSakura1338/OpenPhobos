package org.phobos.core;

import lombok.SneakyThrows;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import org.jetbrains.annotations.Nullable;

public class ServiceRegistry {
   public final Map<String, ServiceRegistry.Reference<?>> textMap = new HashMap<>();
   public static final ServiceRegistry INSTANCE = new ServiceRegistry();

   @Nullable
   @SneakyThrows
   public <S> S resolveObject(String text, Class<S> classValue) {
      ServiceRegistry.Reference<?> reference = this.textMap.get(text);
      return (S)(reference == null ? null : reference.getObject());
   }

   public <S> void handleText(String text, Supplier<S> supplier) {
      this.textMap.put(text, new ServiceRegistry.Reference<>(supplier));
   }

   public static ServiceRegistry getINSTANCE() {
      return INSTANCE;
   }

   private record Reference<S>(Supplier<S> supplier) {

      public S getObject() {
         return this.supplier.get();
      }
   }
}
