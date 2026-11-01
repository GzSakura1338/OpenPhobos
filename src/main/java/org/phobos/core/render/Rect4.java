package org.phobos.core.render;

import java.awt.Color;

public class Rect4 {
   public float floatValue;
   public float floatValue2;
   public float floatValue3;
   public float floatValue4;

   public void handleRect4(Rect4 rect4) {
      this.handleFloat(rect4.getFloatValue(), rect4.getFloatValue4(), rect4.getFloatValue3(), rect4.getFloatValue2());
   }

   public void setFloatValue3(float f) {
      this.floatValue3 = f;
   }

   public void setFloatValue2(float f) {
      this.floatValue2 = f;
   }

   public void setFloatValue(float f) {
      this.floatValue = f;
   }

   public float getFloatValue() {
      return this.floatValue;
   }

   public float getFloatValue4() {
      return this.floatValue4;
   }

   public void setFloatValue4(float f) {
      this.floatValue4 = f;
   }

   public float getFloatValue2() {
      return this.floatValue2;
   }

   public float getFloatValue3() {
      return this.floatValue3;
   }

   public void handleColor(Color color) {
      this.handleFloat(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
   }

   public void handleFloat(float f, float f2, float f3, float f4) {
      this.setFloatValue(f);
      this.setFloatValue4(f2);
      this.setFloatValue3(f3);
      this.setFloatValue2(f4);
   }
}
