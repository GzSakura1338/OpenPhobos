package org.phobos.event;

import org.phobos.module.render.CrosshairModule;
import org.phobos.module.render.CrosshairModule;
import org.phobos.type.KeyboardMouseEnum;

public final class CrosshairOnMouseButtonListener extends ModuleListener<CrosshairModule, OnMouseButtonEvent> {
   public void handleObject2(OnMouseButtonEvent onMouseButtonEvent) {
      if (this.module.trigger.getValue().get(CrosshairModule.Trigger.Click)
         && onMouseButtonEvent.getKeyboardMouseEnum().equals(KeyboardMouseEnum.Mouse)
         && mc.currentScreen == null) {
         if (onMouseButtonEvent.getIntValue2() == 0) {
            if (onMouseButtonEvent.getIntValue() == 1) {
               this.module.floatAnimator.setFloatValue2(this.module.offset.getValue());
               this.module.floatAnimator.setFloatValue(0.0F);
            }
         }
      }
   }

   public CrosshairOnMouseButtonListener(CrosshairModule crosshairModule) {
      super(crosshairModule, OnMouseButtonEvent.class);
   }
}
