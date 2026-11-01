package org.phobos.event;

public class AttackEvent {
   public float floatValue;

   public void setFloatValue(float f) {
      this.floatValue = f;
   }

   public float getFloatValue() {
      return this.floatValue;
   }

   public AttackEvent(float f) {
      this.floatValue = f;
   }
}
