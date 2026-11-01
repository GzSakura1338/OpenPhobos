package org.phobos.core.util.collection;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;
import org.phobos.api.Fluent;

public class ArrayWrapper<E> implements Iterable<E>, Fluent<ArrayWrapper<E>> {
   public final E[] objectArray;

   public ArrayWrapper<E> get() {
      return this;
   }

   public E[] toArray() {
      return this.objectArray;
   }

   public boolean contains(E object) {
      for (E object2 : this.objectArray) {
         if (object2.equals(object)) {
            return true;
         }
      }

      return false;
   }

   public void handleInt(int i, Consumer<E> consumer) {
      if (i >= 0 && i < this.objectArray.length) {
         consumer.accept(this.objectArray[i]);
      }
   }

   public ArrayWrapper(E[] objects) {
      this.objectArray = objects;
   }

   @Override
   public void forEach(Consumer<? super E> consumer) {
      for (E object : this.objectArray) {
         consumer.accept(object);
      }
   }

   public static <E> E elementAt(Object[] objects, int i) {
      return (E)objects[i];
   }

   @NotNull
   @Override
   public Iterator<E> iterator() {
      return new ArrayWrapper.ArrayStreamIterator<>(this.objectArray, 0);
   }

   private static final class ArrayStreamIterator<E> implements ListIterator<E> {
      public int intValue;
      public final Object[] objectArray;

      @Override
      public boolean hasPrevious() {
         return this.intValue > 0;
      }

      @Override
      public void forEachRemaining(Consumer<? super E> consumer) {
         Objects.requireNonNull(consumer);
         int i = this.objectArray.length;
         int i2 = this.intValue;

         for (this.intValue = i; i2 < i; i2++) {
            consumer.accept(ArrayWrapper.elementAt(this.objectArray, i2));
         }
      }

      public ArrayStreamIterator(Object[] objects, int i) {
         this.intValue = i;
         this.objectArray = objects;
      }

      @Override
      public E previous() {
         if (!this.hasPrevious()) {
            throw new NoSuchElementException();
         } else {
            return (E)this.objectArray[--this.intValue];
         }
      }

      @Override
      public void add(E object) {
         throw new UnsupportedOperationException();
      }

      @Override
      public boolean hasNext() {
         return this.intValue < this.objectArray.length;
      }

      @Override
      public E next() {
         if (!this.hasNext()) {
            throw new NoSuchElementException();
         } else {
            return (E)this.objectArray[this.intValue++];
         }
      }

      @Override
      public void remove() {
         throw new UnsupportedOperationException();
      }

      @Override
      public void set(E object) {
         throw new UnsupportedOperationException();
      }

      @Override
      public int nextIndex() {
         return this.intValue;
      }

      @Override
      public int previousIndex() {
         return this.intValue - 1;
      }
   }
}
