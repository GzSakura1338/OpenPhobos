package org.phobos.api;

public interface ValueGuard<T> {
   void setValue(T var1);

   default T resolveObject(T object) {
      T object2 = this.getValue();
      return object2 == null ? object : object2;
   }

   default T resolveObject2(T object) {
      T object2 = this.getValue();
      this.setValue(object);
      return object2;
   }

   default void clear() {
      this.setValue(null);
   }

   T getValue();

   class Normal<T> implements ValueGuard<T> {
      public T value;

      @Override
      public T getValue() {
         return this.value;
      }

      @Override
      public void setValue(T object) {
         this.value = object;
      }
   }

   class Threaded<T> implements ValueGuard<T> {
      public volatile T value;

      @Override
      public void setValue(T object) {
         this.value = object;
      }

      @Override
      public T getValue() {
         return this.value;
      }
   }
}
