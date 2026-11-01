package org.phobos.core.util;

import java.util.function.BiFunction;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ResultUtils<T> {
   @SafeVarargs
   @NotNull
   public static <T> ResultUtils.Result<T> resolveResult(T object, T object2, BiFunction<T, T, T>... biFunctions) {
      MutableObject<ResultUtils.Result<T>> mutableObject = new MutableObject();

      for (BiFunction<T, T, T> biFunction : biFunctions) {
         T object3 = biFunction.apply(object, object2);
         ResultUtils.Result<T> result = new ResultUtils.Result<>(object3);
         mutableObject.setValue(result);
         if (!result.check()) {
            return result;
         }
      }

      return (ResultUtils.Result<T>)mutableObject.getValue();
   }

   public static class Result<T> {
      @Nullable
      public final T result;

      public Result(@Nullable T object) {
         this.result = object;
      }

      public boolean check() {
         return this.result == null;
      }

      @Nullable
      public T getResult() {
         return this.result;
      }
   }
}
