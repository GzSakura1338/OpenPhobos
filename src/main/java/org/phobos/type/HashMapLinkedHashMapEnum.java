package org.phobos.type;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.KeyValue;

public enum HashMapLinkedHashMapEnum {
   HashMap {
      @Override
      public <K, V> Map<K, V> create(int capacity) {
         return new HashMap<>(capacity);
      }
   },
   LinkedHashMap {
      @Override
      public <K, V> Map<K, V> create(int capacity) {
         return new LinkedHashMap<>(capacity);
      }
   },
   WeakHashMap {
      @Override
      public <K, V> Map<K, V> create(int capacity) {
         return new WeakHashMap<>(capacity);
      }
   },
   ConcurrentHashMap {
      @Override
      public <K, V> Map<K, V> create(int capacity) {
         return new ConcurrentHashMap<>();
      }
   };

   public <T, K, V> Map<K, V> list2map(List<T> list, Function<T, KeyValue<K, V>> operation) {
      Map<K, V> map = this.create(list.size());

      for (T element : list) {
         KeyValue<K, V> operator = operation.apply(element);
         map.put(operator.getObject(), operator.getObject2());
      }

      return map;
   }

   public <K, V, T, S> Map<K, V> map2map(Map<T, S> map0, BiFunction<T, S, KeyValue<K, V>> operation) {
      Map<K, V> map = this.create(map0.size());

      for (Map.Entry<T, S> entry : map0.entrySet()) {
         KeyValue<K, V> operator = operation.apply(entry.getKey(), entry.getValue());
         map.put(operator.getObject(), operator.getObject2());
      }

      return map;
   }

   @Nullable
   public static HashMapLinkedHashMapEnum typeOf(Map<?, ?> map) {
      return switch (map) {
         case LinkedHashMap<?, ?> ignored -> LinkedHashMap;
         case WeakHashMap<?, ?> ignored -> WeakHashMap;
         case HashMap<?, ?> ignored -> HashMap;
         case ConcurrentHashMap<?, ?> ignored -> ConcurrentHashMap;
         default -> null;
      };
   }

   public abstract <K, V> Map<K, V> create(int var1);
}
