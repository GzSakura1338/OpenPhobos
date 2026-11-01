package org.phobos.type;

public enum TopCenterEnum {
   Top("top"),
   Center("center"),
   Bottom("bottom");

   public final String name;

   @Override
   public String toString() {
      return this.name;
   }

   public TopCenterEnum getOpposite() {
      return this == Top ? Bottom : Top;
   }

   public static TopCenterEnum fromString(String string) {
      return switch (string) {
         case "top" -> Top;
         default -> Bottom;
      };
   }

   TopCenterEnum(String name) {
      this.name = name;
   }
}
