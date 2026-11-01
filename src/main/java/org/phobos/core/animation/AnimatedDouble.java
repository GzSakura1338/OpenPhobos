package org.phobos.core.animation;

public class AnimatedDouble {
   public final FloatAnimator.Double doubleValue = new FloatAnimator.Double();

   public void handleDouble(double d) {
      this.handleDouble4(d);
      this.doubleValue.setDoubleValue(d);
   }

   public double value() {
      return this.doubleValue.getDoubleValue();
   }

   public void handleDouble2(double d) {
      this.handleDouble(this.doubleValue.getDoubleValue() + d);
   }

   public void handleFloat(float f, float f2) {
      this.doubleValue.handleDouble(f, f2);
   }

   public void handleDouble3(double d) {
      this.doubleValue.setDoubleValue(d);
   }

   public void handleDouble4(double d) {
      this.doubleValue.setDoubleValue2(d);
   }

   public void run() {
      this.doubleValue.run6();
   }
}
