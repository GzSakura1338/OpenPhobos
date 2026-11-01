package org.phobos.api;

import java.util.SortedSet;
import org.jetbrains.annotations.Nullable;

public interface OpenSet<N> {
   void handleObject(N var1, Runnable var2);

   default boolean isEmpty() {
      return this.size() == 0;
   }

   @Nullable
   N removeFirst();

   void add(N var1);

   void clear();

   static <T> OpenSet<T> createValue(SortedSet<T> sortedSet) {
      return new OpenSet<T>() {
         @Override
         public void add(T object) {
            sortedSet.add(object);
         }

         @Override
         public void handleObject(T object, Runnable runnable) {
            sortedSet.remove(object);
            runnable.run();
            sortedSet.add(object);
         }

         @Override
         public void clear() {
            sortedSet.clear();
         }

         @Override
         public int size() {
            return sortedSet.size();
         }

         @Nullable
         @Override
         public T removeFirst() {
            T object = sortedSet.first();
            sortedSet.remove(object);
            return object;
         }
      };
   }

   int size();
}
