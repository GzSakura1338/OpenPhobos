package org.phobos.event;

import org.phobos.core.event.events.CancellableEvent;

public class PlayerTickEvent extends CancellableEvent {
   public float floatValue;

   public void setFloatValue(float f) {
      this.floatValue = f;
   }

   public float getFloatValue() {
      return this.floatValue;
   }
}
