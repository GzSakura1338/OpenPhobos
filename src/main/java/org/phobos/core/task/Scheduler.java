package org.phobos.core.task;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import org.apache.commons.lang3.function.Consumers;
import org.jetbrains.annotations.Nullable;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent2;
import org.phobos.core.Feature;
import org.phobos.core.module.ModuleListenerUtils;

public class Scheduler extends Feature {
   public final Map<Scheduler.Prediction, Integer> predictionMap = new ConcurrentHashMap<>();
   public final Map<Runnable, Integer> runnableMap;
   public final Map<Scheduler.Repeating, Integer> repeatingMap = new ConcurrentHashMap<>();

   public Scheduler() {
      this.runnableMap = new ConcurrentHashMap<>();
      this.handleListener(
         new SafeListener<TickEvent2>() {
            public void handleObject(
               TickEvent2 tickEvent2,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               Scheduler.this.handleMap(
                  Scheduler.this.predictionMap, prediction -> prediction.result.accept(prediction.condition.get()), Consumers.nop()
               );
               Scheduler.this.handleMap(Scheduler.this.repeatingMap, repeating -> {
                  if (repeating.callback != null) {
                     repeating.callback.run();
                  }
               }, repeating -> repeating.action.run());
               Scheduler.this.handleMap(Scheduler.this.runnableMap, Runnable::run, Consumers.nop());
            }
         }
      );
      ModuleListenerUtils.handleListenerHolder4(this, this::clear);
   }

   public void handleRunnable(Runnable runnable, int i) {
      this.repeatingMap.put(new Scheduler.Repeating(runnable, null), i);
   }

   public void handleRunnable2(Runnable runnable, int i) {
      this.runnableMap.put(runnable, i);
   }

   public void clear() {
      this.predictionMap.clear();
      this.repeatingMap.clear();
      this.runnableMap.clear();
   }

   public <T> void handleMap(Map<T, Integer> map, Consumer<T> consumer, Consumer<T> consumer2) {
      if (!map.isEmpty()) {
         for (T object : map.keySet()) {
            int i = map.get(object);
            if (i <= 0) {
               consumer.accept(object);
               map.remove(object);
            } else {
               consumer2.accept(object);
               map.put(object, i - 1);
            }
         }
      }
   }

   public void handleRunnable3(Runnable runnable, Runnable runnable2, int i) {
      this.repeatingMap.put(new Scheduler.Repeating(runnable, runnable2), i);
   }

   public void handleSupplier(Supplier<Boolean> supplier, Consumer<Boolean> consumer, int i) {
      this.predictionMap.put(new Scheduler.Prediction(supplier, consumer), i);
   }

   private record Prediction(Supplier<Boolean> condition, Consumer<Boolean> result) {
   }

   private record Repeating(Runnable action, @Nullable Runnable callback) {
   }
}
