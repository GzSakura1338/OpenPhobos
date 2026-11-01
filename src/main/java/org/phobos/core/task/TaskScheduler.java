package org.phobos.core.task;

import java.util.PriorityQueue;
import java.util.Queue;
import org.jetbrains.annotations.NotNull;
import org.phobos.event.Listener;
import org.phobos.module.Module;
import org.phobos.core.ClientHolder;
import org.phobos.core.Feature;
import org.phobos.core.tick.TickCounter;
import org.phobos.core.tick.TimeUtils;

public class TaskScheduler extends Feature {
   public final Queue<TaskScheduler.Task> taskQueue = new PriorityQueue<>();

   public void handleRunnable(Runnable runnable) {
      this.handleLong(50L, runnable);
   }

   public void handleLong(long l, Runnable runnable) {
      ClientHolder.getEventBus().handleObject(this);
      long l2 = TimeUtils.value() + l;
      mc.submit(() -> this.taskQueue.add(new TaskScheduler.Task(() -> {
         runnable.run();
         ClientHolder.getEventBus().handleObject4(this);
      }, l2)));
   }

   public void handleRunnable2(Runnable runnable) {
      this.handleLong2(50L, runnable);
   }

   public void handleLong2(long l, Runnable runnable) {
      long l2 = TimeUtils.value() + l;
      mc.submit(() -> this.taskQueue.add(new TaskScheduler.Task(runnable, l2)));
   }

   public void handleLong3(long l, Module module, Runnable runnable) {
      this.handleLong2(l, () -> {
         if (module.check7()) {
            runnable.run();
         }
      });
   }

   public void handleInt(int i) {
      mc.submit(() -> this.taskQueue.removeIf(task -> task.intValue == i));
   }

   public void handleLong4(long l, Runnable runnable, int i) {
      long l2 = TimeUtils.value() + l;
      mc.submit(() -> this.taskQueue.add(new TaskScheduler.Task(runnable, l2).resolveTask(i)));
   }

   public TaskScheduler() {
      this.handleListener(new Listener<TickCounter>() {
         public void handleObject2(TickCounter tickCounter) {
            while (!TaskScheduler.this.taskQueue.isEmpty()) {
               TaskScheduler.Task task = TaskScheduler.this.taskQueue.peek();
               if (task == null || !TimeUtils.testLong2(task.longValue, 0L)) {
                  return;
               }

               TaskScheduler.this.taskQueue.poll();
               task.runnable.run();
            }
         }
      });
   }

   public static class Task implements Comparable<TaskScheduler.Task> {
      public final long longValue;
      public final Runnable runnable;
      public int intValue;

      public int compareTo(@NotNull TaskScheduler.Task task) {
         return Long.compare(this.longValue, task.longValue);
      }

      public int getIntValue() {
         return this.intValue;
      }

      public Task(Runnable runnable, long l) {
         this.runnable = runnable;
         this.longValue = l;
      }

      public long getLongValue() {
         return this.longValue;
      }

      public TaskScheduler.Task resolveTask(int i) {
         this.intValue = i;
         return this;
      }

      public Runnable getRunnable() {
         return this.runnable;
      }
   }
}
