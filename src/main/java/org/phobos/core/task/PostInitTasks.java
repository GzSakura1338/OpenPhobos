package org.phobos.core.task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PostInitTasks {
   public final List<Runnable> runnableList = new ArrayList<>();
   public static final PostInitTasks INSTANCE = new PostInitTasks();

   public static PostInitTasks getINSTANCE() {
      return INSTANCE;
   }

   public void run() {
      List var1 = this.runnableList;
      Iterator var2 = var1.iterator();

      while (true) {
         boolean var3 = var2.hasNext();
         if (!var3) {
            return;
         }

         Object var4 = var2.next();
         boolean var5 = var4 instanceof Runnable;
         if (var4 != null && !var5) {
            throw new ClassCastException("CHECKCAST failed");
         }

         ((Runnable)var4).run();
      }
   }

   public void handleRunnable(Runnable runnable) {
      List var2 = this.runnableList;
      var2.add(runnable);
   }
}
