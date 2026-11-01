package org.phobos.core.util.holder;

public final class FloatBox {
   public float floatValue;

   public void setFloatValue(float f) {
      this.floatValue = f;
   }

   public FloatBox(float f) {
      this.floatValue = f;
   }

   public float getFloatValue() {
      return this.floatValue;
   }
}
