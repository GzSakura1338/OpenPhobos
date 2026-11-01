package org.phobos.event;

import org.phobos.module.movement.TargetStrafeModule;

public final class TargetStrafePlayerTickListener extends ModuleListener<TargetStrafeModule, PlayerTickEvent> {
   public void handleObject2(PlayerTickEvent playerTickEvent) {
      if (TargetStrafeModule.entity != null && TargetStrafeModule.flag3) {
         playerTickEvent.setFloatValue(TargetStrafeModule.floatValue);
      }
   }

   public TargetStrafePlayerTickListener(TargetStrafeModule targetStrafeModule) {
      super(targetStrafeModule, PlayerTickEvent.class);
   }
}
