package org.phobos.core.math;

public class FloatPair {
   public final float floatValue;
   public final float floatValue2;
   public static final FloatPair INSTANCE = createFloatPair(50.0F, 150.0F);

   public FloatPair(float f, float f2) {
      this.floatValue = f;
      this.floatValue2 = f2;
   }

   public static FloatPair getINSTANCE() {
      return INSTANCE;
   }

   public float value() {
      return MathUtils.computeFloat8(this.floatValue, this.floatValue2);
   }

   public float getFloatValue() {
      return this.floatValue;
   }

   public float getFloatValue2() {
      return this.floatValue2;
   }

   public static FloatPair createFloatPair(float f, float f2) {
      return new FloatPair(f, f2);
   }
}
