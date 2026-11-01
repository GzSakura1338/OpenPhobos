package org.phobos.core.util.collection;

public class SimpleList<T> {
   public transient int size;
   public transient Object[] objectArray = new Object[10];

   public SimpleList() {
      this.size = 0;
   }

   public int size() {
      return this.size;
   }

   public void run() {
      int i2 = this.objectArray.length * 2;
      Object[] objects = new Object[i2];

      for (int i = 0; i < this.size; i++) {
         objects[i] = this.objectArray[i];
      }

      this.objectArray = objects;
   }

   public T resolveObject(int i) {
      return (T)this.objectArray[i];
   }

   public void handleObject(T object) {
      if (this.size == this.objectArray.length) {
         this.run();
      }

      this.objectArray[this.size++] = object;
   }
}
