package org.phobos.api;

import java.util.function.Function;
import java.util.function.Supplier;

public interface Constant<S> {
   S get();

   static <S> Constant<S> resolveConstant(S object) {
      return () -> object;
   }

   default Function<S, S> getFunction() {
      return object -> object;
   }

   default Supplier<S> getSupplier() {
      return this::get;
   }
}
