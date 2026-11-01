package org.phobos.core;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.Nullable;
import org.phobos.event.Listener;

public class ServiceBuilder<T> extends Feature {
   public final MutableObject<T> mutableObject = new MutableObject(null);
   public final Map<Integer, Object> integerMap = new HashMap<>();

   @Nullable
   public <V> V resolveObject(int i, Class<? super V> classValue) {
      Object object = this.integerMap.get(i);
      if (object == null) {
         return null;
      } else {
         return (V)(!object.getClass().equals(classValue) ? null : this.integerMap.get(i));
      }
   }

   public void run4() {
      ClientHolder.getEventBus().handleObject4(this);
   }

   @Nullable
   public T getObject() {
      return (T)this.mutableObject.getValue();
   }

   public void start() {
      ClientHolder.getEventBus().handleObject(this);
   }

   public static class Builder<T> {
      public final ServiceBuilder<T> serviceBuilder = new ServiceBuilder<>();

      public <V> void handleInt(int i, V object) {
         this.serviceBuilder.integerMap.put(i, object);
      }

      public void handleListener(Listener<?> listener) {
         this.serviceBuilder.handleListener(listener);
      }

      public ServiceBuilder<T> getServiceBuilder() {
         return this.serviceBuilder;
      }
   }
}
