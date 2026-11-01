package org.phobos.core.render;

public class Rect4f {
   public final float floatValue;
   public final float floatValue2;
   public final float floatValue3;
   public final float floatValue4;

   public float getFloatValue2() {
      return this.floatValue2;
   }

   public static Rect4f createRect4f() {
      return new Rect4f(0.0F, 0.0F, 1.0F, 1.0F);
   }

   public Rect4f(float f, float f2, float f3, float f4) {
      this.floatValue3 = f;
      this.floatValue = f2;
      this.floatValue4 = f3;
      this.floatValue2 = f4;
   }

   public float getFloatValue3() {
      return this.floatValue3;
   }

   public float getFloatValue4() {
      return this.floatValue4;
   }

   public float getFloatValue() {
      return this.floatValue;
   }
}
