package org.phobos.core.task;

public class CancelToken {
   public boolean cancelled;
   public static final CancelToken INSTANCE = new CancelToken() {
      @Override
      public boolean isCancelled() {
         return false;
      }
   };

   public void setCancelled(boolean flag) {
      this.cancelled = flag;
   }

   public boolean isCancelled() {
      return this.cancelled;
   }

   public void run() {
   }

   public static class ThreadInterrupted extends CancelToken {
      public Thread thread = Thread.currentThread();

      @Override
      public boolean isCancelled() {
         return super.isCancelled() || this.thread.isInterrupted();
      }

      @Override
      public void run() {
         this.thread = Thread.currentThread();
      }
   }
}
