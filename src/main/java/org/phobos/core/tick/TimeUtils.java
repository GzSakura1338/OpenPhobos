package org.phobos.core.tick;

import java.util.concurrent.TimeUnit;

public final class TimeUtils {
   public static final long longValue = TimeUnit.MILLISECONDS.toNanos(1L);

   public TimeUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static boolean testLong(long l, long l2, Runnable... runnables) {
      if (computeLong(l) < l2) {
         return false;
      }

      for (Runnable runnable : runnables) {
         runnable.run();
      }

      return true;
   }

   public static boolean testLong2(long l, long l2) {
      return computeLong(l) >= l2;
   }

   public static long computeLong(long l) {
      return value() - l;
   }

   public static long value() {
      return System.nanoTime() / longValue;
   }
}
