package org.phobos.core.event.events;

public class FloatEvent extends CancellableEvent {
   public float floatValue;

   public FloatEvent(float f) {
      this.floatValue = f;
   }

   public void setFloatValue(float f) {
      this.floatValue = f;
   }

   public float getFloatValue() {
      return this.floatValue;
   }
}
