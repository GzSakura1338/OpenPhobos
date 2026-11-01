package org.phobos.event;

import org.phobos.module.movement.DisablerModule;
import org.phobos.module.movement.DisablerModule;

public final class DisablerTickListener extends ModuleListener<DisablerModule, TickEvent> {
   public DisablerTickListener(DisablerModule disablerModule) {
      super(disablerModule, TickEvent.class);
   }

   public void handleObject2(TickEvent tickEvent) {
      if (this.module.mode.getValue().equals(DisablerModule.Mode.Watchdog) && this.module.motion.getValue()) {
         if (this.module.flag9) {
            tickEvent.getKeyboardInput().jumping = true;
         }
      }
   }
}
