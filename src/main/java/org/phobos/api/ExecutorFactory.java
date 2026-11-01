package org.phobos.api;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public interface ExecutorFactory {
   default ExecutorService getExecutorService2() {
      AtomicInteger atomicInteger = new AtomicInteger();
      return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), runnable -> {
         Thread thread = new Thread(runnable);
         thread.setName("Phobos-" + atomicInteger.incrementAndGet());
         return thread;
      });
   }
}
