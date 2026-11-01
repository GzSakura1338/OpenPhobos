package org.phobos.core.util;

import java.util.function.BiConsumer;
import org.phobos.core.task.ThrowableTask;

public final class ExceptionUtils {
   public ExceptionUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   @SafeVarargs
   public static void handleRunnable(Runnable runnable, BiConsumer<Throwable, ThrowableTask>... biConsumers) {
      try {
         runnable.run();
      } catch (Throwable throwable) {
         if (biConsumers.length == 1) {
            biConsumers[0].accept(throwable, new ThrowableTask(throwable));
         }
      }
   }
}
