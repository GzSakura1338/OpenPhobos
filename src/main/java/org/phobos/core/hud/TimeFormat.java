package org.phobos.core.hud;

public final class TimeFormat {
   public static String describeInt(int i, int i2) {
      return (i > 12 ? i % 12 : i) + ":" + (i2 < 10 ? "0" + i2 : i2) + " " + (i >= 12 ? "PM" : "AM");
   }

   public TimeFormat() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static String describeInt2(int i) {
      return i < 10 && i >= 0 ? "0" + i : i + "";
   }
}
