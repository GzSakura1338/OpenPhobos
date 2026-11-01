package org.phobos.core.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.MathHelper;
import org.phobos.api.AnimationValue;
import org.phobos.core.math.MathUtils;

public class UiValues {
   public static class Double implements AnimationValue.Double {
      public double doubleValue;
      public double doubleValue2;
      public double doubleValue3;

      @Override
      public double getDoubleValue2() {
         return this.doubleValue;
      }

      @Override
      public void run2() {
         this.setDoubleValue(0.0);
         this.setDoubleValue2(0.0);
      }

      public void handleDouble2(double d) {
         this.setDoubleValue(this.computeDouble(this.doubleValue3, this.doubleValue, d));
      }

      @Override
      public void setDoubleValue2(double d) {
         this.doubleValue = d;
      }

      public double computeDouble(double d, double d2, double d3) {
         return (1.0F - MathHelper.clamp((float)(this.value() * d3), 0.0F, 1.0F)) * d + MathHelper.clamp((float)(this.value() * d3), 0.0F, 1.0F) * d2;
      }

      @Override
      public double getDoubleValue() {
         return this.doubleValue3;
      }

      @Override
      public void setDoubleValue(double d) {
         this.doubleValue3 = d;
      }

      public boolean testDouble(double d) {
         return MathUtils.testDouble(this.doubleValue3, this.doubleValue, d);
      }

      public double getDoubleValue22() {
         return this.doubleValue2;
      }

      public double value() {
         int i = MinecraftClient.getInstance().getCurrentFps();
         return i > 0 ? 1.0 / i : 1.0;
      }

      @Override
      public void handleDouble(double d, double d2) {
         this.handleDouble2(d);
      }

      public void handleDouble3(double d) {
         this.doubleValue2 = MathUtils.computeDouble9(-d, d);
         this.doubleValue3 = this.doubleValue3 + this.doubleValue2;
      }

      public void setDoubleValue22(double d) {
         this.doubleValue2 = d;
      }
   }

   public static class Float implements AnimationValue.Float {
      public float floatValue;
      public float floatValue2;

      @Override
      public void run2() {
         this.setFloatValue2(0.0F);
         this.setFloatValue(0.0F);
      }

      @Override
      public void setFloatValue(float f) {
         this.floatValue2 = f;
      }

      public boolean testFloat(float f) {
         return MathUtils.testFloat(this.floatValue, this.floatValue2, f);
      }

      @Override
      public float getFloatValue() {
         return this.floatValue2;
      }

      public void handleFloat(float f) {
         this.setFloatValue2(this.computeFloat(this.floatValue, this.floatValue2, f));
      }

      public double value() {
         int i = MinecraftClient.getInstance().getCurrentFps();
         return i > 0 ? 1.0 / i : 1.0;
      }

      @Override
      public void handleFloat3(float f, float f2) {
         this.handleFloat(f);
      }

      @Override
      public float getFloatValue2() {
         return this.floatValue;
      }

      @Override
      public void setFloatValue2(float f) {
         this.floatValue = f;
      }

      public float computeFloat(float f, float f2, float f3) {
         return (1.0F - MathHelper.clamp((float)(this.value() * f3), 0.0F, 1.0F)) * f + MathHelper.clamp((float)(this.value() * f3), 0.0F, 1.0F) * f2;
      }
   }
}
