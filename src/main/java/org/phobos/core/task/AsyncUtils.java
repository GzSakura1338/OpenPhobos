package org.phobos.core.task;

import java.util.concurrent.CompletableFuture;

public class AsyncUtils {
   public static void handleRunnable(Runnable runnable) {
      CompletableFuture.runAsync(runnable);
   }
}
