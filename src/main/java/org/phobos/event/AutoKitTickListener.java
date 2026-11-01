package org.phobos.event;

import org.phobos.api.TimeGate;
import org.phobos.module.misc.AutoKitModule;

public final class AutoKitTickListener extends ModuleListener<AutoKitModule, TickEvent2> {
   public final TimeGate.Normal normal = new TimeGate.Normal();

   public AutoKitTickListener(AutoKitModule autoKitModule) {
      super(autoKitModule, TickEvent2.class);
   }

   public void handleObject2(TickEvent2 tickEvent2) {
      if (!this.module.check() && mc.player != null) {
         if (this.normal.testNumber2(this.module.delay.getValue())) {
            mc.player.networkHandler.sendChatCommand(this.module.kit.getValue());
         }
      }
   }
}
