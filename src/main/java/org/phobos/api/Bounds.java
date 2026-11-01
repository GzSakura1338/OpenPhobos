package org.phobos.api;

import org.phobos.core.hud.HudElement;

public interface Bounds {
   default float[] values() {
      return HudElement.values2(this.getFloatValue2(), this.getFloatValue(), this.getFloatValue3(), this.getFloatValue4());
   }

   default void handleFloat6(float f, float f2, float f3, float f4) {
      this.setFloatValue2(f);
      this.setFloatValue(f2);
      this.setFloatValue3(f3);
      this.setFloatValue4(f4);
   }

   float getFloatValue4();

   float getFloatValue();

   default boolean check5() {
      return this.getFloatValue4() < 0.0F;
   }

   float getFloatValue3();

   void setFloatValue(float var1);

   default boolean check6() {
      return this.getFloatValue3() < 0.0F;
   }

   void setFloatValue4(float var1);

   void setFloatValue3(float var1);

   void setFloatValue2(float var1);

   float getFloatValue2();

   float value2();

   float value3();
}
