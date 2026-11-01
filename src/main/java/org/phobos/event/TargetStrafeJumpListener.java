package org.phobos.event;

import org.phobos.module.movement.TargetStrafeModule;

public final class TargetStrafeJumpListener extends ModuleListener<TargetStrafeModule, JumpEvent2> {
   public void handleObject2(JumpEvent2 jumpEvent2) {
      if (TargetStrafeModule.entity != null && TargetStrafeModule.flag3) {
         jumpEvent2.setFloatValue(TargetStrafeModule.floatValue);
      }
   }

   public TargetStrafeJumpListener(TargetStrafeModule targetStrafeModule) {
      super(targetStrafeModule, JumpEvent2.class);
   }
}
