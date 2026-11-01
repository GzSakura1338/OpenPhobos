package org.phobos.core.animation;

import net.minecraft.util.math.Box;

public class AnimatedBox {
   public final FloatAnimator.Double doubleValue = new FloatAnimator.Double();
   public final FloatAnimator.Double doubleValue2 = new FloatAnimator.Double();
   public final FloatAnimator.Double doubleValue3;
   public final FloatAnimator.Double doubleValue4;
   public final AnimatorGroup.Double doubleValue5;
   public final FloatAnimator.Double doubleValue6;
   public final FloatAnimator.Double doubleValue7 = new FloatAnimator.Double();

   public boolean check() {
      return this.doubleValue5.getDoubleValueList().stream().allMatch(FloatAnimator.Double::isFinished);
   }

   public void handleFloat(float f, float f2) {
      this.doubleValue5.handleDouble2(f, f2);
   }

   public void run() {
      this.doubleValue5.run2();
   }

   public void handleBox(Box box, boolean flag) {
      if (flag) {
         this.doubleValue.setDoubleValue(box.minX);
         this.doubleValue2.setDoubleValue(box.minY);
         this.doubleValue7.setDoubleValue(box.minZ);
         this.doubleValue4.setDoubleValue(box.maxX);
         this.doubleValue3.setDoubleValue(box.maxY);
         this.doubleValue6.setDoubleValue(box.maxZ);
      } else {
         this.doubleValue.setDoubleValue2(box.minX);
         this.doubleValue2.setDoubleValue2(box.minY);
         this.doubleValue7.setDoubleValue2(box.minZ);
         this.doubleValue4.setDoubleValue2(box.maxX);
         this.doubleValue3.setDoubleValue2(box.maxY);
         this.doubleValue6.setDoubleValue2(box.maxZ);
      }
   }

   public Box createBox() {
      return new Box(
         this.doubleValue.getDoubleValue(),
         this.doubleValue2.getDoubleValue(),
         this.doubleValue7.getDoubleValue(),
         this.doubleValue4.getDoubleValue(),
         this.doubleValue3.getDoubleValue(),
         this.doubleValue6.getDoubleValue()
      );
   }

   public AnimatedBox() {
      this.doubleValue4 = new FloatAnimator.Double();
      this.doubleValue3 = new FloatAnimator.Double();
      this.doubleValue6 = new FloatAnimator.Double();
      this.doubleValue5 = new AnimatorGroup.Double(
         this.doubleValue, this.doubleValue2, this.doubleValue7, this.doubleValue4, this.doubleValue3, this.doubleValue6
      );
   }
}
