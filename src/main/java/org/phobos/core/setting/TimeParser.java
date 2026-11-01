package org.phobos.core.setting;

import org.jetbrains.annotations.Nullable;
import org.phobos.core.chat.ChatTextUtils;

public final class TimeParser {
   public TimeParser() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   @Nullable
   public static Long resolveLongValue(String text) {
      try {
         if (text.endsWith("s")) {
            String text2 = text.substring(0, text.length() - 1);
            if (!ChatTextUtils.testText2(text2)) {
               throw new IllegalStateException("Invalid time format");
            }

            int i = Integer.parseInt(text2);
            return i * 1000L;
         } else {
            return (long)Integer.parseInt(text);
         }
      } catch (Throwable throwable) {
         return null;
      }
   }
}
