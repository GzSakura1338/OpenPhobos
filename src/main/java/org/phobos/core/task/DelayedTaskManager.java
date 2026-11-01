package org.phobos.core.task;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BooleanSupplier;
import org.phobos.event.DisconnectEvent2;
import org.phobos.event.Listener;
import org.phobos.core.Feature;
import org.phobos.core.tick.TickCounter;
import org.phobos.core.tick.TimeUtils;

public class DelayedTaskManager extends Feature {
   public final Set<TaskScheduler.Task> taskSet;
   public final Set<DelayedTaskManager.Task> taskSet2 = ConcurrentHashMap.newKeySet();

   public DelayedTaskManager() {
      this.taskSet = ConcurrentHashMap.newKeySet();
      this.handleListener(new Listener<TickCounter>() {
         public void handleObject2(TickCounter tickCounter) {
            for (DelayedTaskManager.Task task : DelayedTaskManager.this.taskSet2) {
               if (task.condition.getAsBoolean()) {
                  try {
                     task.runnable().run();
                  } catch (Throwable var6) {
                  }

                  DelayedTaskManager.this.handleTask(task);
               }
            }

            for (TaskScheduler.Task task2 : DelayedTaskManager.this.taskSet) {
               if (task2.getLongValue() < TimeUtils.value()) {
                  try {
                     task2.getRunnable().run();
                  } catch (Throwable var5) {
                  }

                  DelayedTaskManager.this.handleTask2(task2);
               }
            }
         }
      });
      this.handleListener(new Listener<DisconnectEvent2>() {
         public void handleObject2(DisconnectEvent2 disconnectEvent2) {
            DelayedTaskManager.this.taskSet2.clear();
            DelayedTaskManager.this.taskSet.clear();
         }
      });
   }

   public void handleRunnable(Runnable runnable, long l) {
      this.taskSet.add(new TaskScheduler.Task(runnable, TimeUtils.value() + l));
   }

   public void handleRunnable2(Runnable runnable, BooleanSupplier booleanSupplier) {
      this.taskSet2.add(new DelayedTaskManager.Task(runnable, booleanSupplier));
   }

   public void handleTask(DelayedTaskManager.Task task) {
      this.taskSet2.remove(task);
   }

   public void handleTask2(TaskScheduler.Task task) {
      this.taskSet.remove(task);
   }

   public record Task(Runnable runnable, BooleanSupplier condition) {
   }
}
