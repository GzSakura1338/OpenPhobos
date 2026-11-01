package org.phobos.core.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LockedExecutor {
   public final AtomicReference<Object> objectAtomicReference;
   public final Object object = new Object[0];
   public static final Logger LOGGER = LoggerFactory.getLogger(LockedExecutor.class);
   public final ExecutorService executorService = Executors.newSingleThreadExecutor();

   public LockedExecutor() {
      this.objectAtomicReference = new AtomicReference<>();
   }

   public void handleRunnable(Runnable runnable) {
      synchronized (this.object) {
         if (this.objectAtomicReference.get() == null) {
            Runnable runnable2 = () -> {
               try {
                  runnable.run();
               } catch (Exception exception) {
                  LOGGER.error("Locked thread executor {} has failed while executing {} -> {}", new Object[]{this, runnable, exception.getStackTrace()});
               } finally {
                  this.objectAtomicReference.set(null);
               }
            };
            this.objectAtomicReference.set(runnable2);
            this.executorService.submit(runnable2);
         }
      }
   }
}
