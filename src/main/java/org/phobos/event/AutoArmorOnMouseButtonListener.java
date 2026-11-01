package org.phobos.event;

import org.phobos.module.combat.AutoArmorModule;

public final class AutoArmorOnMouseButtonListener extends ModuleListener<AutoArmorModule, OnMouseButtonEvent> {
   public void handleObject2(OnMouseButtonEvent onMouseButtonEvent) {
   }

   public AutoArmorOnMouseButtonListener(AutoArmorModule autoArmorModule) {
      super(autoArmorModule, OnMouseButtonEvent.class);
   }
}
