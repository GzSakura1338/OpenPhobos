package org.phobos.core.gui;

public class ClipRect {
   public boolean empty;
   public float floatValue;
   public float floatValue2;
   public static final ClipRect INSTANCE = new ClipRect();
   public float floatValue3;
   public float floatValue4;

   static {
      INSTANCE.setEmpty(true);
   }

   public void setFloatValue(float f) {
      this.floatValue = f;
   }

   public void setEmpty(boolean flag) {
      this.empty = flag;
   }

   public void setFloatValue3(float f) {
      this.floatValue3 = f;
   }

   public static ClipRect createClipRect(float f, float f2, float f3, float f4) {
      return new ClipRect(f, f2, f3, f4, false);
   }

   public float getFloatValue() {
      return this.floatValue;
   }

   public ClipRect() {
   }

   public static ClipRect getINSTANCE() {
      return INSTANCE;
   }

   public void setFloatValue4(float f) {
      this.floatValue4 = f;
   }

   public void setFloatValue2(float f) {
      this.floatValue2 = f;
   }

   public boolean testDouble(double d, double d2) {
      return !this.empty
         && d >= this.floatValue
         && d <= this.floatValue + this.floatValue4
         && d2 >= this.floatValue2
         && d2 <= this.floatValue2 + this.floatValue3;
   }

   public float getFloatValue4() {
      return this.floatValue4;
   }

   public ClipRect(float f, float f2, float f3, float f4, boolean flag) {
      this.floatValue = f;
      this.floatValue2 = f2;
      this.floatValue4 = f3;
      this.floatValue3 = f4;
      this.empty = flag;
   }

   public boolean isEmpty() {
      return this.empty;
   }

   public float getFloatValue3() {
      return this.floatValue3;
   }

   public float getFloatValue2() {
      return this.floatValue2;
   }
}
