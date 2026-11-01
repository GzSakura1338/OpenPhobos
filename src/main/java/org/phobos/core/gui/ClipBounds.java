package org.phobos.core.gui;

import net.minecraft.client.util.Window;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.MinecraftHolder;

public class ClipBounds implements MinecraftHolder {
   public double doubleValue;
   public double doubleValue2;
   public double doubleValue3;
   public double doubleValue4;
   public static final ClipBounds INSTANCE = new ClipBounds(0.0, 0.0, 0.0, 0.0);

   public void setDoubleValue(double d) {
      this.doubleValue = d;
   }

   public double getDoubleValue3() {
      return this.doubleValue3;
   }

   @Nullable
   public ClipBounds resolveClipBounds(ClipBounds clipBounds) {
      double d = Math.max(this.getDoubleValue2(), clipBounds.getDoubleValue2());
      double d2 = Math.max(this.getDoubleValue4(), clipBounds.getDoubleValue4());
      double d3 = Math.min(this.value(), clipBounds.value());
      double d4 = Math.min(this.value2(), clipBounds.value2());
      return d < d3 && d2 < d4 ? new ClipBounds(d, d2, d3 - d, d4 - d2) : null;
   }

   public ClipBounds(double d, double d2, double d3, double d4) {
      this.doubleValue2 = d;
      this.doubleValue4 = d2;
      this.doubleValue3 = d3;
      this.doubleValue = d4;
   }

   public double value() {
      return this.doubleValue2 + this.doubleValue3;
   }

   public double[] values() {
      Window window = mc.getWindow();
      int i = window.getHeight();
      double d = window.getScaleFactor();
      double d2 = this.getDoubleValue2() * d;
      double d3 = i - this.value2() * d;
      double d4 = this.getDoubleValue3() * d;
      double d5 = this.getDoubleValue() * d;
      return new double[]{d2, d3, d4, d5};
   }

   public double getDoubleValue2() {
      return this.doubleValue2;
   }

   public void setDoubleValue3(double d) {
      this.doubleValue3 = d;
   }

   public double getDoubleValue22() {
      return this.doubleValue2;
   }

   public double getDoubleValue4() {
      return this.doubleValue4;
   }

   public double value2() {
      return this.doubleValue4 + this.doubleValue;
   }

   public void setDoubleValue4(double d) {
      this.doubleValue4 = d;
   }

   public int[] values2() {
      return new int[]{
         (int)this.doubleValue2, (int)this.doubleValue4, (int)(this.doubleValue2 + this.doubleValue3), (int)(this.doubleValue4 + this.doubleValue)
      };
   }

   public double getDoubleValue42() {
      return this.doubleValue4;
   }

   public double getDoubleValue() {
      return this.doubleValue;
   }

   public void setDoubleValue2(double d) {
      this.doubleValue2 = d;
   }
}
