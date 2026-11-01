package org.phobos.core.task;

import lombok.SneakyThrows;
import java.util.function.Supplier;
import org.phobos.api.ThrowingSupplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class RetryUtils {
   public static final Logger LOGGER = LoggerFactory.getLogger(RetryUtils.class);

   @SneakyThrows
   public static <T> T resolveObject(int i, Supplier<Throwable> supplier, ThrowingSupplier<T, ?> throwingSupplier) {
      for (int i2 = 0; i2 < i; i2++) {
         try {
            return throwingSupplier.get();
         } catch (Exception exception) {
            exception.printStackTrace();
         }
      }

      throw (Throwable)supplier.get();
   }

   public RetryUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
