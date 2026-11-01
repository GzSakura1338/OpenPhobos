package org.phobos.core.task;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskQueue {
   public final AtomicBoolean atomicBoolean;
   public static final Logger LOGGER = LoggerFactory.getLogger(TaskQueue.class);
   public final Deque<Runnable> runnableDeque = new ArrayDeque<>();
   public final AtomicInteger atomicInteger = new AtomicInteger();

   public void run() {
      this.atomicInteger.incrementAndGet();
   }

   public void handleRunnable(Runnable runnable) {
      synchronized (this.atomicBoolean) {
         if (this.check()) {
            this.runnableDeque.add(runnable);
         } else {
            runnable.run();
         }
      }
   }

   public TaskQueue() {
      this.atomicBoolean = new AtomicBoolean();
   }

   public boolean check() {
      return this.atomicBoolean.get();
   }

   public void handleFlag(boolean flag) {
      synchronized (this.atomicBoolean) {
         this.atomicBoolean.set(flag);
         if (flag) {
            this.runnableDeque.clear();
         } else {
            QueueUtils.handleQueue(this.runnableDeque);
         }
      }
   }

   public int value() {
      return this.atomicInteger.get();
   }
}
