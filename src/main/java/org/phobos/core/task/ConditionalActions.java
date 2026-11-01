package org.phobos.core.task;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class ConditionalActions implements Runnable {
   public final Map<Supplier<Boolean>, Runnable> supplierMap;

   public void handleRunnable(Runnable runnable) {
      this.handleRunnable2(runnable, () -> true);
   }

   @Override
   public void run() {
      for (Supplier<Boolean> supplier : this.supplierMap.keySet()) {
         if (supplier.get()) {
            this.supplierMap.get(supplier).run();
         }
      }
   }

   public Map<Supplier<Boolean>, Runnable> getSupplierMap() {
      return this.supplierMap;
   }

   public ConditionalActions(Runnable... runnables) {
      this.supplierMap = new ConcurrentHashMap<>(runnables.length);

      for (Runnable runnable : runnables) {
         this.supplierMap.put(() -> true, runnable);
      }
   }

   public void handleRunnable2(Runnable runnable, Supplier<Boolean> supplier) {
      this.supplierMap.put(supplier, runnable);
   }

   public static class Builder {
      public final ConditionalActions conditionalActions = new ConditionalActions();

      public ConditionalActions.Builder resolveBuilder(Runnable runnable, Supplier<Boolean> supplier) {
         this.conditionalActions.handleRunnable2(runnable, supplier);
         return this;
      }

      public ConditionalActions.Builder resolveBuilder2(Runnable runnable) {
         this.conditionalActions.handleRunnable(runnable);
         return this;
      }

      public ConditionalActions getConditionalActions() {
         return this.conditionalActions;
      }
   }
}
