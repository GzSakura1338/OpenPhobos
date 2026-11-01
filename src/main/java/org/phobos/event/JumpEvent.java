package org.phobos.event;

public class JumpEvent {
   public float floatValue;

   public void setFloatValue(float f) {
      this.floatValue = f;
   }

   public float getFloatValue() {
      return this.floatValue;
   }

   public JumpEvent(float f) {
      this.floatValue = f;
   }
}
