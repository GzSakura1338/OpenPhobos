package org.phobos.event;

public class GetStepHeightEvent {
   public float floatValue;

   public GetStepHeightEvent(float f) {
      this.floatValue = f;
   }

   public float getFloatValue() {
      return this.floatValue;
   }

   public void setFloatValue(float f) {
      this.floatValue = f;
   }
}
