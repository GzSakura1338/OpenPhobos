package org.phobos.core.task;

public class DelayedRunnable implements Runnable {
   public final int intValue;
   public final Runnable runnable;

   public Runnable getRunnable() {
      return this.runnable;
   }

   public static void handleRunnable(Runnable runnable, int i) {
      new DelayedRunnable(runnable, i).run();
   }

   @Override
   public void run() {
      switch (this.intValue) {
         case 0:
            break;
         case 1:
            this.runnable.run();
            break;
         default:
            for (int i = 0; i < this.intValue; i++) {
               this.runnable.run();
            }
      }
   }

   public DelayedRunnable(Runnable runnable, int i) {
      this.runnable = runnable;
      this.intValue = i;
   }

   public int getIntValue() {
      return this.intValue;
   }
}
