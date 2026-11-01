package org.phobos.core.movement;

public class SpeedState {
   public int intValue;
   public double doubleValue;

   public void handleDouble(double d) {
      this.doubleValue /= d;
   }

   public void handleDouble2(double d, double d2, double d3) {
      this.setDoubleValue(Math.sqrt(d * d + d2 * d2));
      this.setIntValue(0);
      if (d3 != 1.0) {
         this.handleDouble(d3);
      }
   }

   public int getIntValue() {
      return this.intValue;
   }

   public void handleInt(int i) {
      if (this.intValue++ > i) {
         this.setDoubleValue(0.0);
      }
   }

   public void run() {
      this.setIntValue(0);
      this.setDoubleValue(0.0);
   }

   public double getDoubleValue() {
      return this.doubleValue;
   }

   public void setIntValue(int i) {
      this.intValue = i;
   }

   public void setDoubleValue(double d) {
      this.doubleValue = d;
   }
}
