package org.phobos.api;

import lombok.SneakyThrows;
import java.util.function.Supplier;

@FunctionalInterface
public interface ThrowingSupplier<T, EX extends Throwable> extends Supplier<T> {
   @Override
   @SneakyThrows
   default T get() {
      return this.getObject();
   }

   T getObject() throws EX;
}
