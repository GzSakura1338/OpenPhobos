package org.phobos.core.animation;

import net.minecraft.util.math.MathHelper;
import org.phobos.api.Easing;

public class EasedAnimation {
   public final long longValue;
   public final long longValue2;
   public volatile boolean flag;
   public long longValue3;

   public long getLongValue3() {
      return this.longValue3;
   }

   public float computeEasing(Easing easing) {
      return this.computeEasing2(easing, easing);
   }

   public float value() {
      double d = (float)(System.currentTimeMillis() - this.longValue3) / (float)(this.flag ? this.longValue : this.longValue2);
      if (!this.flag) {
         d = 1.0 - d;
      }

      return (float)MathHelper.clamp(d, 0.0, 1.0);
   }

   public float computeEasing2(Easing easing, Easing easing2) {
      double d = (float)(System.currentTimeMillis() - this.longValue3) / (float)(this.flag ? this.longValue : this.longValue2);
      if (!this.flag) {
         d = 1.0 - d;
      }

      return (float)MathHelper.clamp(Easing.computeEasing(this.flag ? easing : easing2, d), 0.0, 1.0);
   }

   public boolean isFlag() {
      return this.flag;
   }

   public void run() {
      this.handleFlag2(!this.isFlag());
   }

   public EasedAnimation(long l, long l2, boolean flag) {
      this.longValue = l;
      this.longValue2 = l2;
      this.flag = flag;
   }

   public void handleFlag(boolean flag) {
      if (this.flag != flag) {
         this.run();
      }
   }

   public void handleFlag2(boolean flag) {
      this.flag = flag;
      this.longValue3 = System.currentTimeMillis();
   }

   public EasedAnimation(long l) {
      this(l, l, false);
   }

   public EasedAnimation(long l, boolean flag) {
      this(l, l, flag);
   }

   public EasedAnimation(long l, long l2) {
      this(l, l2, false);
   }

   public void setLongValue3(long l) {
      this.longValue3 = l;
   }
}
