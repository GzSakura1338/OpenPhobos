package org.phobos.core.util;

public final class PathUtils {
   public PathUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static String describeText(String text) {
      return text.substring(text.lastIndexOf(47) + 1);
   }
}
