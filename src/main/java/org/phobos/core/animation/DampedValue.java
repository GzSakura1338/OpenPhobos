package org.phobos.core.animation;

import net.minecraft.client.util.Window;
import org.phobos.api.MinecraftHolder;
import org.phobos.core.math.MathUtils;

public class DampedValue implements MinecraftHolder {
   public double doubleValue;
   public final float floatValue = 500.0F;
   public double doubleValue2;
   public double doubleValue3;
   public double doubleValue4;
   public double doubleValue5;
   public final float floatValue2;
   public double doubleValue6;
   public final float floatValue3 = 2000.0F;

   public void setDoubleValue2(double d) {
      this.doubleValue2 = d;
   }

   public float value() {
      return 2.0F;
   }

   public void setDoubleValue(double d) {
      this.doubleValue = d;
   }

   public double getDoubleValue4() {
      return this.doubleValue4;
   }

   public double getDoubleValue3() {
      return this.doubleValue3;
   }

   public void setDoubleValue5(double d) {
      this.doubleValue5 = d;
   }

   public boolean testDouble(double d) {
      return MathUtils.testDouble(this.doubleValue, this.doubleValue4, d) && MathUtils.testDouble(this.doubleValue3, this.doubleValue6, d);
   }

   public DampedValue() {
      this.floatValue2 = 2.0F;
   }

   public float value2() {
      return 2000.0F;
   }

   public double getDoubleValue6() {
      return this.doubleValue6;
   }

   public float value3() {
      return 500.0F;
   }

   public double getDoubleValue() {
      return this.doubleValue;
   }

   public void setDoubleValue4(double d) {
      this.doubleValue4 = d;
   }

   public void setDoubleValue3(double d) {
      this.doubleValue3 = d;
   }

   public void setDoubleValue6(double d) {
      this.doubleValue6 = d;
   }

   public double getDoubleValue2() {
      return this.doubleValue2;
   }

   public double getDoubleValue5() {
      return this.doubleValue5;
   }

   public void handleFloat(float f) {
      float f2 = 500.0F * f;
      float f3 = 2000.0F * f;
      float f4 = 2.0F * Math.max(0.1F, Math.min(2.0F, f));
      double d = this.doubleValue4 - this.doubleValue;
      double d2 = this.doubleValue6 - this.doubleValue3;
      double d3 = Math.sqrt(d * d + d2 * d2);
      if (d3 < 1.0) {
         this.doubleValue = this.doubleValue + MathUtils.computeFloat8(-0.5F, 0.5F) * f;
         this.doubleValue3 = this.doubleValue3 + MathUtils.computeFloat8(-0.5F, 0.5F) * f;
      } else {
         int i = mc.getCurrentFps();
         double d4 = (float)(i > 0 ? 1.0 / i : 1.0);
         double d5 = MathUtils.computeDouble5(d * f3 * d4, -f2, f2);
         double d6 = MathUtils.computeDouble5(d2 * f3 * d4, -f2, f2);
         this.doubleValue5 = this.doubleValue5 + (d5 - this.doubleValue5) * 0.1F;
         this.doubleValue2 = this.doubleValue2 + (d6 - this.doubleValue2) * 0.1F;
         this.doubleValue = this.doubleValue + this.doubleValue5 * d4;
         this.doubleValue3 = this.doubleValue3 + this.doubleValue2 * d4;
         double d7 = MathUtils.computeFloat8(-f4, f4) * d4;
         double d8 = MathUtils.computeFloat8(-f4, f4) * d4;
         this.doubleValue += d7;
         this.doubleValue3 += d8;
         Window window = mc.getWindow();
         this.doubleValue = MathUtils.computeDouble5(this.doubleValue, 0.0, window.getScaledWidth());
         this.doubleValue3 = MathUtils.computeDouble5(this.doubleValue3, 0.0, window.getScaledHeight());
         if (d3 < 50.0) {
            this.doubleValue5 *= 0.95F;
            this.doubleValue2 *= 0.95F;
         }
      }
   }
}
