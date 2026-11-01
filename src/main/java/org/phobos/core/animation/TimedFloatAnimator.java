package org.phobos.core.animation;

import java.util.function.BiFunction;
import net.minecraft.util.math.MathHelper;
import org.phobos.core.math.MathUtils;
import org.phobos.core.tick.TimerManager;

public class TimedFloatAnimator extends FloatAnimator {
   @Override
   public boolean isFinished() {
      return Math.round(MathHelper.wrapDegrees(this.getFloatValue2())) == Math.round(MathHelper.wrapDegrees(this.getFloatValue()));
   }

   @Override
   public boolean testFloat(float f) {
      return MathUtils.testFloat(MathHelper.wrapDegrees(this.getFloatValue2()), MathHelper.wrapDegrees(this.getFloatValue()), f);
   }

   @Override
   public void handleFloat3(float f, float f2) {
      float f3 = TimerManager.value() * 6.7F;
      float f4 = this.getFloatValue2();
      float f5 = this.getFloatValue();
      f5 = this.computeFloat(f5);
      float f6 = f5 - f4;
      if (f6 > 180.0F) {
         f6 -= 360.0F;
      } else if (f6 < -180.0F) {
         f6 += 360.0F;
      }

      double d = Math.abs(f6);
      double d2 = Math.min(f, d * f2);
      float f7 = (float)(f4 + Math.signum(f6) * d2 * f3);
      float f8 = Math.abs(f6) < d2 * f3 ? f5 : f7;
      f8 = this.computeFloat(f8);
      this.setFloatValue2(f8);
   }

   public float computeFloat(float f) {
      while (f > 180.0F) {
         f -= 360.0F;
      }

      while (f < -180.0F) {
         f += 360.0F;
      }

      if (f == -180.0F) {
         f = 180.0F;
      }

      return f;
   }

   public void handleFloat4(float f, float f2, BiFunction<Float, Float, Float> biFunction) {
      float f3 = TimerManager.value() * 6.7F;
      float f4 = this.getFloatValue2();
      float f5 = this.getFloatValue();
      f5 = this.computeFloat(f5);
      float f6 = f5 - f4;
      if (f6 > 180.0F) {
         f6 -= 360.0F;
      } else if (f6 < -180.0F) {
         f6 += 360.0F;
      }

      double d = Math.abs(f6);
      double d2 = Math.min(f, d * f2);
      float f7 = (float)(f4 + Math.signum(f6) * d2 * f3);
      float f8 = Math.abs(f6) < d2 * f3 ? f5 : f7;
      f8 = this.computeFloat(f8);
      this.setFloatValue2(biFunction.apply(this.getFloatValue2(), f8));
   }
}
