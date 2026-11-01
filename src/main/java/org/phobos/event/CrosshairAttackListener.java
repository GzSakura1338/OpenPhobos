package org.phobos.event;

import org.phobos.module.render.CrosshairModule;
import org.phobos.module.render.CrosshairModule;

public final class CrosshairAttackListener extends ModuleListener<CrosshairModule, AttackEvent2> {
   public CrosshairAttackListener(CrosshairModule crosshairModule) {
      super(crosshairModule, AttackEvent2.class);
   }

   public void handleObject2(AttackEvent2 attackEvent2) {
      if (this.module.trigger.getValue().get(CrosshairModule.Trigger.Attack)) {
         this.module.floatAnimator.setFloatValue2(this.module.offset.getValue());
         this.module.floatAnimator.setFloatValue(0.0F);
         if (!this.module.sync.getValue()) {
            this.module.run();
         }
      }
   }
}
