package org.phobos.type;

public enum CenterRightEnum {
   Center("center"),
   Right("right"),
   Left("left");

   public final String name;

   CenterRightEnum(String name) {
      this.name = name;
   }

   @Override
   public String toString() {
      return this.name;
   }

   public static CenterRightEnum fromString(String string) {
      return switch (string) {
         case "right" -> Right;
         case "left" -> Left;
         default -> Center;
      };
   }
}
