package org.phobos.api;

import java.util.function.Consumer;
import java.util.function.Function;

public interface Fluent<S> {
   S get();

   default S resolveObject(Consumer<S> consumer) {
      S object = this.get();
      consumer.accept(object);
      return object;
   }

   default <N> N resolveObject2(Function<S, N> function) {
      return function.apply(this.get());
   }

   default <T, N extends Fluent<T>> N resolveFluent(Function<S, N> function) {
      return function.apply(this.get());
   }
}
