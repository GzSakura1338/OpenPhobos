package org.phobos.core.chat;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import org.apache.commons.lang3.RandomStringUtils;

public final class ChatTextUtils {
   public static final Map<TextColor, Formatting> textColorMap = (Map<TextColor, Formatting>)Util.make(new HashMap(), hashMap -> {
      for (Formatting formatting : Formatting.values()) {
         TextColor textColor = TextColor.fromFormatting(formatting);
         if (textColor != null) {
            hashMap.put(textColor, formatting);
         }
      }
   });

   public static String describeText(String text) {
      return text != null && !text.isEmpty() ? text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase() : text;
   }

   public static String describeList(List<String> list) {
      if (list != null && !list.isEmpty()) {
         String text = list.getFirst();

         for (int i = 1; i < list.size(); i++) {
            while (list.get(i).indexOf(text) != 0) {
               text = text.substring(0, text.length() - 1);
               if (text.isEmpty()) {
                  return "";
               }
            }
         }

         return text;
      } else {
         return "";
      }
   }

   public static String describeInt(int i) {
      return RandomStringUtils.random(i, true, true);
   }

   public static String describeText2(String text) {
      try {
         MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
         messageDigest.update(text.getBytes(StandardCharsets.UTF_8));
         byte[] bytes = messageDigest.digest();
         StringBuilder stringBuilder = new StringBuilder();

         for (byte b : bytes) {
            String text2 = Integer.toHexString(255 & b);
            if (text2.length() == 1) {
               stringBuilder.append('0');
            }

            stringBuilder.append(text2);
         }

         return stringBuilder.toString();
      } catch (Throwable throwable) {
         return text;
      }
   }

   public static String describeText3(Text text, String text2, boolean flag) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(text2);
      text.visit((style, text2x) -> {
         if (style.isEmpty()) {
            stringBuilder.append(text2);
            stringBuilder.append(text2x);
            return Optional.empty();
         }

         if (style.isBold()) {
            stringBuilder.append('§').append("l");
         }

         if (style.isItalic()) {
            stringBuilder.append('§').append("o");
         }

         if (style.isStrikethrough()) {
            stringBuilder.append('§').append("m");
         }

         if (style.isObfuscated()) {
            stringBuilder.append('§').append("k");
         }

         if (style.isUnderlined()) {
            stringBuilder.append('§').append("n");
         }

         TextColor textColor = style.getColor();
         if (textColor != null) {
            Formatting formatting = textColorMap.get(textColor);
            if (formatting != null) {
               stringBuilder.append('§').append(formatting.getCode());
            } else if (flag) {
               stringBuilder.append('§').append(textColor.getHexCode());
            }
         }

         stringBuilder.append(text2x);
         stringBuilder.append(text2);
         return Optional.empty();
      }, Style.EMPTY);
      return stringBuilder.toString();
   }

   public static String describeText4(String text) {
      return TextFormattingUtils.describeText(text).replaceAll("§#[0-9A-Fa-f]{6}", "");
   }

   public static String describeText5(String text, int i) {
      return text.length() > i ? text.substring(0, i) : text;
   }

   public ChatTextUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static boolean testText(String text) {
      if (text != null && !text.isEmpty()) {
         String text2 = "^[a-zA-Z0-9]*$";
         return text.matches(text2);
      } else {
         return false;
      }
   }

   public static String describeFloat(float f) {
      if (f <= 4.0F) {
         return "§4";
      } else if (f <= 8.0F) {
         return "§c";
      } else if (f <= 12.0F) {
         return "§e";
      } else if (f <= 16.0F) {
         return "§6";
      } else {
         return f <= 18.0F ? "§2" : "§a";
      }
   }

   public static boolean testText2(String text) {
      if (text.isEmpty()) {
         return false;
      }

      for (char c : text.toCharArray()) {
         if (!Character.isDigit(c)) {
            return false;
         }
      }

      return true;
   }

   public static String describeDouble(double d) {
      if (d <= 4.0) {
         return "§4";
      } else if (d <= 8.0) {
         return "§c";
      } else if (d <= 16.0) {
         return "§e";
      } else if (d <= 24.0) {
         return "§6";
      } else {
         return d <= 36.0 ? "§2" : "§a";
      }
   }

   public static String describeText6(String text) {
      return TextFormattingUtils.describeText(text);
   }
}
