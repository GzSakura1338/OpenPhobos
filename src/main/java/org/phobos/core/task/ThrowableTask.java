package org.phobos.core.task;

public class ThrowableTask {
   public final Throwable throwable;

   public Throwable getThrowable() {
      return this.throwable;
   }

   public ThrowableTask(Throwable throwable) {
      this.throwable = throwable;
   }

   public void run() {
      this.throwable.printStackTrace();
   }
}
