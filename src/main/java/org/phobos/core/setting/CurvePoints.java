package org.phobos.core.setting;

public class CurvePoints {
   public double doubleValue;
   public double doubleValue2;
   public double doubleValue3;
   public double doubleValue4;

   public double getDoubleValue4() {
      return this.doubleValue4;
   }

   public CurvePoints(double d, double d2, double d3, double d4) {
      this.doubleValue4 = d;
      this.doubleValue3 = d2;
      this.doubleValue2 = d3;
      this.doubleValue = d4;
   }

   public CurvePoints() {
   }

   public double getDoubleValue3() {
      return this.doubleValue3;
   }

   public double getDoubleValue2() {
      return this.doubleValue2;
   }

   public void setDoubleValue2(double d) {
      this.doubleValue2 = d;
   }

   public void setDoubleValue4(double d) {
      this.doubleValue4 = d;
   }

   public void handleInt(int i, double d) {
      switch (i) {
         case 0:
            this.doubleValue4 = d;
            break;
         case 1:
            this.doubleValue3 = d;
            break;
         case 2:
            this.doubleValue2 = d;
            break;
         case 3:
            this.doubleValue = d;
      }
   }

   public double getDoubleValue() {
      return this.doubleValue;
   }

   public static CurvePoints createCurvePoints() {
      return new CurvePoints(0.0, 0.0, 1.0, 1.0);
   }

   public static CurvePoints createCurvePoints2() {
      return new CurvePoints(0.5, 0.5, 0.5, 0.5);
   }

   public void setDoubleValue3(double d) {
      this.doubleValue3 = d;
   }

   public double computeInt(int i) {
      return switch (i) {
         case 0 -> this.doubleValue4;
         case 1 -> this.doubleValue3;
         case 2 -> this.doubleValue2;
         case 3 -> this.doubleValue;
         default -> throw new IllegalStateException("Unexpected value: " + i);
      };
   }

   public static CurvePoints createCurvePoints3(double d, double d2, double d3, double d4) {
      return new CurvePoints(d, d2, d3, d4);
   }

   public void setDoubleValue(double d) {
      this.doubleValue = d;
   }
}
