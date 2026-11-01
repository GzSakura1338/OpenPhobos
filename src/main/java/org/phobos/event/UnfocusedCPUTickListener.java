package org.phobos.event;

import org.phobos.module.misc.UnfocusedCPUModule;

public final class UnfocusedCPUTickListener extends ModuleListener<UnfocusedCPUModule, TickEvent2> {
   public boolean flag = true;
   public int intValue2;

   public void handleObject2(TickEvent2 tickEvent2) {
      if (!mc.isWindowFocused()) {
         this.flag = true;
         mc.options.getMaxFps().setValue(this.module.fPS.getValue());
      } else {
         if (this.flag) {
            mc.options.getMaxFps().setValue(this.intValue2);
            this.flag = false;
         }

         this.intValue2 = (Integer)mc.options.getMaxFps().getValue();
      }
   }

   public UnfocusedCPUTickListener(UnfocusedCPUModule unfocusedCPUModule) {
      super(unfocusedCPUModule, TickEvent2.class);
   }
}
