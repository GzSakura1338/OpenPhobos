package org.phobos.core.event.events;

public class FloatPairEvent extends CancellableEvent {
   public float floatValue;
   public float floatValue2;

   public FloatPairEvent(float f, float f2) {
      this.floatValue = f;
      this.floatValue2 = f2;
   }

   public float getFloatValue2() {
      return this.floatValue2;
   }

   public float getFloatValue() {
      return this.floatValue;
   }

   public void setFloatValue2(float f) {
      this.floatValue2 = f;
   }

   public void setFloatValue(float f) {
      this.floatValue = f;
   }
}
