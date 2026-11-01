package org.phobos.core.math;

import org.phobos.api.Bounds;

public final class BoundsUtils {
   public static float computeBounds(Bounds bounds, Bounds bounds2) {
      return Math.max(bounds.value2(), bounds2.value2());
   }

   public static float value() {
      return 4.0F;
   }

   public BoundsUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static float value2() {
      return 2.0F;
   }

   public static float computeBounds2(Bounds bounds, Bounds bounds2) {
      return Math.max(bounds.value3(), bounds2.value3());
   }
}
