package org.phobos.type;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.jetbrains.annotations.Nullable;

public enum ArrayListLinkedListEnum {
   ArrayList {
      @Override
      public <T> List<T> create(int capacity) {
         return new ArrayList<>(capacity);
      }
   },
   LinkedList {
      @Override
      public <T> List<T> create(int capacity) {
         return new LinkedList<>();
      }
   },
   CopyOnWriteArrayList {
      @Override
      public <T> List<T> create(int capacity) {
         return new CopyOnWriteArrayList<>();
      }
   };

   public <T, K, V> List<T> map2list(Map<K, V> map, BiFunction<K, V, T> operation) {
      List<T> list = this.create(map.size());

      for (Map.Entry<K, V> entry : map.entrySet()) {
         K key = entry.getKey();
         V value = entry.getValue();
         T element = operation.apply(key, value);
         list.add(element);
      }

      return list;
   }

   public <T, S> List<T> list2list(List<S> list0, Function<S, T> operation) {
      List<T> list = this.create(list0.size());

      for (S element : list0) {
         list.add(operation.apply(element));
      }

      return list;
   }

   public abstract <T> List<T> create(int var1);

   @Nullable
   public static ArrayListLinkedListEnum typeOf(List<?> list) {
      return switch (list) {
         case ArrayList<?> ignored -> ArrayList;
         case LinkedList<?> ignored -> LinkedList;
         case CopyOnWriteArrayList<?> ignored -> CopyOnWriteArrayList;
         default -> null;
      };
   }
}
