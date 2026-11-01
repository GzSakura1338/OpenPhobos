package org.phobos.type;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.jetbrains.annotations.Nullable;

public enum HashSetLinkedHashSetEnum {
   HashSet {
      @Override
      public <T> Set<T> create(int capacity) {
         return new HashSet<>(capacity);
      }
   },
   LinkedHashSet {
      @Override
      public <T> Set<T> create(int capacity) {
         return new LinkedHashSet<>(capacity);
      }
   },
   ConcurrentHashSet {
      @Override
      public <T> Set<T> create(int capacity) {
         return ConcurrentHashMap.newKeySet(capacity);
      }
   };

   public <T, S> Set<T> list2list(Set<S> list0, Function<S, T> operation) {
      Set<T> list = this.create(list0.size());

      for (S element : list0) {
         list.add(operation.apply(element));
      }

      return list;
   }

   public abstract <T> Set<T> create(int var1);

   @Nullable
   public static HashSetLinkedHashSetEnum typeOf(Set<?> map) {
      return switch (map) {
         case LinkedHashSet<?> ignored -> LinkedHashSet;
         case ConcurrentHashMap.KeySetView<?, ?> ignored -> ConcurrentHashSet;
         case HashSet<?> ignored -> HashSet;
         default -> null;
      };
   }

   public <T, K, V> Set<T> map2set(Map<K, V> map, BiFunction<K, V, T> operation) {
      Set<T> list = this.create(map.size());

      for (Map.Entry<K, V> entry : map.entrySet()) {
         K key = entry.getKey();
         V value = entry.getValue();
         T element = operation.apply(key, value);
         list.add(element);
      }

      return list;
   }
}
