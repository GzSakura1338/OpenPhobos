package org.phobos.core.chat;

import java.awt.Color;
import java.util.regex.Pattern;

public final class TextFormattingUtils {
   public static final Color color = new Color(0, 0, 0, 0);
   public static final Pattern pattern = Pattern.compile("(?i)§[0-9A-FK-OR+]");

   public static String describeText(String text) {
      return text == null ? null : pattern.matcher(text).replaceAll("");
   }

   public TextFormattingUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
