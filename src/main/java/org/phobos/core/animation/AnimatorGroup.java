package org.phobos.core.animation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AnimatorGroup {
   public final List<FloatAnimator> floatAnimatorList = new ArrayList<>();

   public List<FloatAnimator> getFloatAnimatorList() {
      return this.floatAnimatorList;
   }

   public void run() {
      this.floatAnimatorList.forEach(FloatAnimator::run6);
   }

   public void handleFloat(float f, float f2) {
      for (FloatAnimator floatAnimator : this.floatAnimatorList) {
         floatAnimator.handleFloat3(f, f2);
      }
   }

   public AnimatorGroup() {
   }

   public AnimatorGroup(FloatAnimator... floatAnimators) {
      this.floatAnimatorList.addAll(Arrays.asList(floatAnimators));
   }

   public void handleFloat2(float f) {
      for (FloatAnimator floatAnimator : this.floatAnimatorList) {
         floatAnimator.setFloatValue(f);
      }
   }

   public void handleFloat3(float f) {
      for (FloatAnimator floatAnimator : this.floatAnimatorList) {
         floatAnimator.setFloatValue2(f);
      }
   }

   public void handleFloatAnimator(FloatAnimator floatAnimator) {
      this.floatAnimatorList.add(floatAnimator);
   }

   public void run2() {
      for (FloatAnimator floatAnimator : this.floatAnimatorList) {
         floatAnimator.run2();
      }
   }

   public static class Double {
      public final List<FloatAnimator.Double> doubleValueList = new ArrayList<>();

      public void handleDouble(double d) {
         for (FloatAnimator.Double doubleValue : this.doubleValueList) {
            doubleValue.setDoubleValue(d);
         }
      }

      public Double(FloatAnimator.Double... doubleValues) {
         this.doubleValueList.addAll(Arrays.asList(doubleValues));
      }

      public List<FloatAnimator.Double> getDoubleValueList() {
         return this.doubleValueList;
      }

      public void handleDoubleValue(FloatAnimator.Double doubleValue) {
         this.doubleValueList.add(doubleValue);
      }

      public void run() {
         for (FloatAnimator.Double doubleValue : this.doubleValueList) {
            doubleValue.run2();
         }
      }

      public Double() {
      }

      public void handleDouble2(double d, double d2) {
         for (FloatAnimator.Double doubleValue : this.doubleValueList) {
            doubleValue.handleDouble(d, d2);
         }
      }

      public void handleDouble3(double d) {
         for (FloatAnimator.Double doubleValue : this.doubleValueList) {
            doubleValue.setDoubleValue2(d);
         }
      }

      public void run2() {
         this.doubleValueList.forEach(FloatAnimator.Double::run6);
      }
   }
}
