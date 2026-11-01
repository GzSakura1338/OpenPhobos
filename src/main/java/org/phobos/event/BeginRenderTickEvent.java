package org.phobos.event;

public class BeginRenderTickEvent {
   public float floatValue = 1.0F;
   public float floatValue2;
   public final float floatValue3;

   public float getFloatValue() {
      return this.floatValue;
   }

   public float getFloatValue3() {
      return this.floatValue3;
   }

   public float getFloatValue2() {
      return this.floatValue2;
   }

   public void setFloatValue(float f) {
      this.floatValue = f;
   }

   public void setFloatValue2(float f) {
      this.floatValue2 = f;
   }

   public BeginRenderTickEvent(float f, float f2) {
      this.floatValue3 = f;
      this.floatValue2 = f2;
   }
}
