package org.phobos.core.media.linux;

import java.util.ArrayList;
import java.util.List;

public class DbusVariant {
   public final String text;
   public final Object value;

   public DbusVariant(String text, Object object) {
      this.text = text;
      this.value = object;
   }

   @Override
   public String toString() {
      return this.text + ":" + this.value;
   }

   public <T> T getValue() {
      return (T)this.value;
   }

   public static Object resolveObject(String text) {
      String[] texts2 = text.split(" ", 2);
      if (texts2.length != 2) {
         throw new IllegalArgumentException("Invalid variant: " + text);
      } else {
         String text2 = texts2[0];
         String text3 = texts2[1];
         if (text2.startsWith("variant")) {
            String[] texts = text3.split(" ", 2);
            return resolveObject2(texts[0], texts[1]);
         } else if (text2.startsWith("dict")) {
            return createDbusVariant2(text3);
         } else {
            throw new IllegalArgumentException("Invalid variant signature: " + text2);
         }
      }
   }

   public String getText() {
      return this.text;
   }

   public static DbusVariant createDbusVariant(String text) {
      text = text.trim().replace("\n", "");

      while (text.contains("  ")) {
         text = text.replace("  ", " ");
      }

      return new DbusVariant("variant", resolveObject(text));
   }

   public static Object resolveObject2(String text, String text2) {
      switch (text) {
         case "array":
            String text5 = text2.substring(1, text2.length() - 1).trim();
            List<Object> list = new ArrayList<>();
            StringBuilder stringBuilder = new StringBuilder();
            boolean flag = false;
            boolean flag2 = false;
            boolean flag3 = false;
            String text6 = null;
            int i = 0;

            for (; i < text5.length(); i++) {
               char c = text5.charAt(i);
               if (c == '"') {
                  flag2 = !flag2;
               }

               if (!flag2) {
                  if (c == '(') {
                     flag = true;
                  }

                  if (c == ')') {
                     flag = false;
                     list.add(createDbusVariant2(stringBuilder + ")"));
                     stringBuilder = new StringBuilder();
                     continue;
                  }

                  if (!flag && c == ' ' && !stringBuilder.isEmpty()) {
                     String text3 = stringBuilder.toString().trim();
                     stringBuilder = new StringBuilder();
                     if (text6 == null) {
                        if (!text3.equals("dict")) {
                           text6 = text3;
                        }
                     } else {
                        Object object = resolveObject2(text6, text3);
                        if (!(object instanceof DbusVariant)) {
                           flag3 = true;
                        }

                        list.add(object);
                        text6 = null;
                     }
                  }
               }

               stringBuilder.append(c);
            }

            if (text6 != null) {
               String text4 = stringBuilder.toString().trim();
               Object object2 = resolveObject2(text6, text4);
               if (!(object2 instanceof DbusVariant)) {
                  flag3 = true;
               }

               list.add(object2);
            }

            if (!flag3) {
               return list.toArray(new DbusVariant[0]);
            } else {
               return list.getFirst() instanceof String ? list.toArray(new String[0]) : list.toArray();
            }
         case "string":
            return text2.substring(1, text2.length() - 1);
         case "int32":
            return Integer.parseInt(text2);
         case "uint32":
            return Integer.parseUnsignedInt(text2);
         case "int64":
            return Long.parseLong(text2);
         case "uint64":
            return Long.parseUnsignedLong(text2);
         case "double":
            return Double.parseDouble(text2);
         default:
            return text2;
      }
   }

   public static DbusVariant createDbusVariant2(String text) {
      String text2 = null;
      String text3 = null;
      StringBuilder stringBuilder = new StringBuilder();
      boolean flag = false;
      boolean flag2 = false;

      for (int i = 0; i < text.length(); i++) {
         char c = text.charAt(i);
         if (c == '"') {
            flag2 = !flag2;
         }

         if (!flag2) {
            if (c == '(') {
               flag = true;
               continue;
            }

            if (c == ')') {
               flag = false;
               continue;
            }

            if (flag && c == ' ') {
               if (stringBuilder.isEmpty()) {
                  continue;
               }

               if (text2 == null) {
                  text2 = stringBuilder.toString();
                  stringBuilder = new StringBuilder();
                  if (!text2.equals("string")) {
                     throw new IllegalArgumentException("Invalid dict sig type: " + text2);
                  }
               } else if (text3 == null) {
                  text3 = (String)resolveObject2(text2, stringBuilder.toString().trim());
                  stringBuilder = new StringBuilder();
               }
            }
         }

         if (flag) {
            stringBuilder.append(c);
         }
      }

      return new DbusVariant(text3, resolveObject(stringBuilder.toString().trim()));
   }
}
