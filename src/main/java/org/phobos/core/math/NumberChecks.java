package org.phobos.core.math;

public final class NumberChecks {
   public static final double doubleValue = 1.0E307;
   public static final double doubleValue2 = 1.0E38F;

   public NumberChecks() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static boolean testFloat(float f) {
      return Math.abs(f) > 1.0E38F;
   }

   public static boolean testDouble(double d) {
      return Math.abs(d) > 1.0E307;
   }
}
