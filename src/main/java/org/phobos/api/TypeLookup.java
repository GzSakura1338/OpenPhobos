package org.phobos.api;

import java.util.Optional;

public interface TypeLookup<T> {
   <C extends T> Optional<C> resolveOptional(Class<C> var1);

   interface Lookup<T> extends TypeLookup<T>, StreamableIterable<T> {
      @Override
      default <C extends T> Optional<C> resolveOptional(Class<C> classValue) {
         return this.stream().filter(classValue::isInstance).map(classValue::cast).findFirst();
      }
   }
}
