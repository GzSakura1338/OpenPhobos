package org.earthhack.ducks.generic;

public class Property<T> {
   private T value;

   public T getValue() {
      return this.value;
   }

   public void setValue(T value) {
      this.value = value;
   }

   public Property() {
   }

   public Property(T value) {
      this.value = value;
   }
}
