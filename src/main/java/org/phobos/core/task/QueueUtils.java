package org.phobos.core.task;

import java.util.Queue;
import java.util.function.Consumer;

public final class QueueUtils {
   public QueueUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static void handleQueue(Queue<Runnable> queue) {
      handleQueue2(queue, Runnable::run);
   }

   public static <T> void handleQueue2(Queue<T> queue, Consumer<T> consumer) {
      while (!queue.isEmpty()) {
         T object = queue.poll();
         if (object != null) {
            consumer.accept(object);
         }
      }
   }
}
