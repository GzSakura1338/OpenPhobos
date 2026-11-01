package org.phobos.api;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public interface DelayedTask {
   static void handleLong(long l, Runnable runnable) {
      CompletableFuture.delayedExecutor(l, TimeUnit.MILLISECONDS).execute(runnable);
   }
}
