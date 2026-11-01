package org.phobos.module.combat;

import org.phobos.core.event.events.CancellableEvent;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.Listener;
import org.phobos.module.Module;

public class KeepSprintModule extends Module {
   public KeepSprintModule(PhobosClient phobosClient) {
      super(phobosClient, "KeepSprint", "Keeps sprint after attacking an entity", Categories.combat);
      this.handleListener(new Listener<KeepSprintModule.SprintResetEvent>() {
         public void handleObject2(KeepSprintModule.SprintResetEvent sprintResetEvent) {
            sprintResetEvent.setCancelled(true);
         }
      });
   }

   public static final class SprintResetEvent extends CancellableEvent {
   }
}
