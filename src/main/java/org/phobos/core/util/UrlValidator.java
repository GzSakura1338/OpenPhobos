package org.phobos.core.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class UrlValidator {
   private static Pattern pattern;

   public UrlValidator() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static boolean testText(String text) {
      long var4;
      if (text != null) {
         Pattern var1 = getPattern();
         Matcher var2 = var1.matcher(text);
         int var3 = (var2.matches()) ? 1 : 0;
         var4 = (int)(var3 & 0xFF & 0xFF) & 4294967295L;
      } else {
         var4 = 0L;
      }

      return (((int)((int)var4 & 0xFF & 0xFF)) != 0);
   }

   static {
      setPattern(
         Pattern.compile(
            "^(https?://)?(www\\.)?(imgur\\.com|i\\.imgur\\.com|namemc\\.com|cdn\\.discordapp\\.com|media\\.discordapp\\.net)/.*\\.(png|jpg|jpeg)(\\?.*)?$", 2
         )
      );
   }

   public static Pattern getPattern() {
      return pattern;
   }

   public static void setPattern(Pattern var0) {
      pattern = var0;
   }
}
