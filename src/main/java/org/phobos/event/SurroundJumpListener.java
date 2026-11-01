package org.phobos.event;

import org.phobos.module.combat.SurroundModule;

public final class SurroundJumpListener extends ModuleListener<SurroundModule, JumpEvent3> {
   public SurroundJumpListener(SurroundModule surroundModule) {
      super(surroundModule, JumpEvent3.class);
   }

   public void handleObject2(JumpEvent3 jumpEvent3) {
      this.module.conditionalActions.run();
   }
}
