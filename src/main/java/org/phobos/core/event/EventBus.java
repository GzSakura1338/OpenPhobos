package org.phobos.core.event;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.jetbrains.annotations.NotNull;
import org.phobos.api.ObjectTransformer;
import org.phobos.api.ListenerHolder;
import org.phobos.api.MinecraftHolder;
import org.phobos.event.Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventBus implements MinecraftHolder, Iterable<ListenerHolder> {
   public final Map<Class<?>, List<Listener>> classValueMap;
   public static final Logger LOGGER = LoggerFactory.getLogger(EventBus.class);
   public volatile boolean flag = false;
   public final Set<ListenerHolder> listenerHolderSet;

   public void handleObject(Object object) {
      if (object instanceof ListenerHolder listenerHolder) {
         synchronized (object instanceof ObjectTransformer objectTransformer ? objectTransformer.resolveObject(this) : this.listenerHolderSet) {
            if (this.listenerHolderSet.add(listenerHolder)) {
               listenerHolder.getListenerList().forEach(listener -> {
                  List<Listener> list = this.classValueMap.computeIfAbsent(listener.getValueClassValue(), classValue -> new CopyOnWriteArrayList<>());
                  this.handleListener((Listener<?>)listener, list);
               });
            }
         }
      }
   }

   public void handleObject2(Object object) {
      if (!this.flag) {
         List<Listener> list = this.classValueMap.get(object.getClass());
         if (list != null) {
            for (Listener listener : list) {
               listener.handleObject3(object);
            }
         }
      }
   }

   public void handleObject3(Object object, Class<?> classValue) {
      if (!this.flag) {
         List<Listener> list = this.classValueMap.get(object.getClass());
         if (list != null) {
            for (Listener listener : list) {
               if (listener.getClassValue() == null || listener.getClassValue() == classValue) {
                  listener.handleObject3(object);
               }
            }
         }
      }
   }

   public boolean testObject(Object object) {
      if (object instanceof ListenerHolder listenerHolder) {
         synchronized (object instanceof ObjectTransformer objectTransformer ? objectTransformer.resolveObject(this) : this.listenerHolderSet) {
            return this.listenerHolderSet.contains(listenerHolder);
         }
      } else {
         return false;
      }
   }

   @NotNull
   @Override
   public Iterator<ListenerHolder> iterator() {
      return this.listenerHolderSet.iterator();
   }

   public void handleListener(Listener<?> listener, List<Listener> list) {
      int i = 0;

      while (i < list.size() && listener.compareTo(list.get(i)) < 0) {
         i++;
      }

      list.add(i, listener);
   }

   public void handleObject4(Object object) {
      if (object instanceof ListenerHolder listenerHolder) {
         synchronized (object instanceof ObjectTransformer objectTransformer ? objectTransformer.resolveObject(this) : this.listenerHolderSet) {
            if (this.listenerHolderSet.remove(listenerHolder)) {
               listenerHolder.getListenerList().forEach(listener -> {
                  List<Listener> list = this.classValueMap.get(listener.getValueClassValue());
                  if (list != null) {
                     list.remove(listener);
                  }
               });
            }
         }
      }
   }

   public void handleRunnable(Runnable runnable) {
      this.flag = true;
      runnable.run();
      this.flag = false;
   }

   public EventBus() {
      this.classValueMap = new ConcurrentHashMap<>();
      this.listenerHolderSet = ConcurrentHashMap.newKeySet();
   }
}
