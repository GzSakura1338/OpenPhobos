package org.phobos.core.util;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.function.Supplier;
import net.minecraft.client.MinecraftClient;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.MinecraftHolder;

public final class AccessProviderUtils implements MinecraftHolder {
   @Nullable
   public static <T> AccessProviderUtils.AccessProvider<T> resolveAccessProvider(Class<? super T> classValue, String text) {
      MutableObject<Field> mutableObject = new MutableObject();

      for (Field field : MinecraftClient.class.getDeclaredFields()) {
         if (Objects.equals(field.getType(), classValue)) {
            if (mutableObject.getValue() != null) {
               if (field.getName().equals(text)) {
                  mutableObject.setValue(field);
                  break;
               }
            } else {
               mutableObject.setValue(field);
               if (field.getName().equals(text)) {
                  break;
               }
            }
         }
      }

      if (mutableObject.getValue() != null) {
         Field field2 = (Field)mutableObject.getValue();
         return () -> {
            try {
               return (T)field2.get(mc);
            } catch (Throwable throwable) {
               return null;
            }
         };
      } else {
         return null;
      }
   }

   public AccessProviderUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   @FunctionalInterface
   public interface AccessProvider<T> extends Supplier<T> {
      @Override
      T get();
   }
}
