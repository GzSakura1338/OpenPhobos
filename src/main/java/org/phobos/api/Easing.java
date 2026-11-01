package org.phobos.api;

import net.minecraft.util.math.MathHelper;

public interface Easing {
   static double computeEasing(Easing easing, double d) {
      return MathHelper.clamp(easing.computeDouble(MathHelper.clamp(d, 0.0, 1.0)), 0.0, 1.0);
   }

   double computeDouble(double var1);

   enum Easings {
      NONE(null),
      IO_QUART(Easing.InOutEasing.INSTANCE2),
      IO_QUINT(Easing.InOutEasing.INSTANCE6),
      IO_QUADR(Easing.InOutEasing.INSTANCE5),
      IO_QUAD(Easing.InOutEasing.INSTANCE),
      IO_CIRC(Easing.InOutEasing.INSTANCE4),
      IO_CUBIC(Easing.InOutEasing.INSTANCE3),
      I_EXPO(Easing.InEasing.INSTANCE),
      O_QUART(Easing.OutEasing.INSTANCE2),
      O_CIRC(Easing.OutEasing.INSTANCE);

      public final Easing easing;

      public Easing getEasing() {
         return this.easing;
      }

      Easings(final Easing easing) {
         this.easing = easing;
      }
   }

   interface InEasing extends Easing {
      Easing.InEasing INSTANCE = d -> d == 0.0 ? 0.0 : Math.pow(2.0, 10.0 * d - 10.0);
   }

   interface InOutEasing extends Easing {
      Easing.InOutEasing INSTANCE = d -> d < 0.5 ? 2.0 * d * d : 1.0 - Math.pow(-2.0 * d + 2.0, 2.0) / 2.0;
      Easing.InOutEasing INSTANCE2 = d -> d < 0.5 ? 8.0 * d * d * d * d : 1.0 - Math.pow(-2.0 * d + 2.0, 4.0) / 2.0;
      Easing.InOutEasing INSTANCE3 = d -> d < 0.5 ? 4.0 * d * d * d : 1.0 - Math.pow(-2.0 * d + 2.0, 3.0) / 2.0;
      Easing.InOutEasing INSTANCE4 = d -> Math.sqrt(1.0 - Math.pow(d - 1.0, 2.0));
      Easing.InOutEasing INSTANCE5 = d -> d * d;
      Easing.InOutEasing INSTANCE6 = d -> d < 0.5 ? 16.0 * d * d * d * d * d : 1.0 - Math.pow(-2.0 * d + 2.0, 5.0) / 2.0;
   }

   interface OutEasing extends Easing {
      Easing.OutEasing INSTANCE = d -> Math.sqrt(1.0 - Math.pow(d - 1.0, 2.0));
      Easing.OutEasing INSTANCE2 = d -> 1.0 - Math.pow(1.0 - d, 4.0);
   }
}
