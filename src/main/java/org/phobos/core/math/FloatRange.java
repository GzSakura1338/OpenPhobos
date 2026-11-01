package org.phobos.core.math;

public class FloatRange {
   public final float floatValue;
   public final float floatValue2;

   public float getFloatValue2() {
      return this.floatValue2;
   }

   public static FloatRange createFloatRange(float f, float f2) {
      return new FloatRange(f, f2);
   }

   public float getFloatValue() {
      return this.floatValue;
   }

   public FloatRange(float f, float f2) {
      this.floatValue = f;
      this.floatValue2 = f2;
   }
}
