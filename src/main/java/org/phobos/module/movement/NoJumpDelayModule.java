package org.phobos.module.movement;

import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.event.BaseTickEvent;
import org.phobos.event.Listener;
import org.phobos.module.Module;

public class NoJumpDelayModule extends Module {
   public NoJumpDelayModule(PhobosClient phobosClient) {
      super(phobosClient, "NoJumpDelay", Categories.movement, "Disables jump delay");
      this.handleListener(new Listener<BaseTickEvent>() {
         public void handleObject2(BaseTickEvent baseTickEvent) {
            baseTickEvent.setIntValue(0);
         }
      });
   }
}
