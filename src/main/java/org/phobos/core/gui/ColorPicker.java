package org.phobos.core.gui;

import org.phobos.core.animation.AnimatedColor;

public abstract class ColorPicker {
   public float floatValue;
   public float floatValue2;
   public float floatValue3;
   public float floatValue4;
   public boolean flag;
   public final AnimatedColor animatedColor = new AnimatedColor();

   public abstract void run();

   public void setFloatValue(float f) {
      this.floatValue = f;
   }

   public float getFloatValue2() {
      return this.floatValue2;
   }

   public float getFloatValue4() {
      return this.floatValue4;
   }

   public void setFloatValue3(float f) {
      this.floatValue3 = f;
   }

   public boolean testDouble(double d, double d2) {
      return d >= this.floatValue && d2 >= this.floatValue3 && d <= this.floatValue + this.floatValue4 && d2 <= this.floatValue3 + this.floatValue2;
   }

   public void setFloatValue4(float f) {
      this.floatValue4 = f;
   }

   public float getFloatValue3() {
      return this.floatValue3;
   }

   public void setFloatValue2(float f) {
      this.floatValue2 = f;
   }

   public void handleDouble(double d, double d2) {
      if (this.testDouble(d, d2)) {
         this.run2();
      }
   }

   public AnimatedColor getAnimatedColor() {
      return this.animatedColor;
   }

   public abstract void run2();

   public void setFlag(boolean flag) {
      this.flag = flag;
   }

   public float getFloatValue() {
      return this.floatValue;
   }

   public boolean isFlag() {
      return this.flag;
   }
}
