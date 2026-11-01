package org.phobos.api;

import java.util.ArrayList;
import java.util.List;

public interface TickTask {
   List<TickTask> tickTaskList = new ArrayList<>();

   static void run2() {
      for (TickTask tickTask : tickTaskList) {
         tickTask.run();
      }
   }

   void run();

   static void handleTickTask(TickTask tickTask) {
      tickTaskList.add(tickTask);
   }
}
