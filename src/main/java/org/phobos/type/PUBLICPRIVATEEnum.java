package org.phobos.type;

public enum PUBLICPRIVATEEnum {
   PUBLIC("public"),
   PRIVATE("private");

   public final String name;

   @Override
   public String toString() {
      return this.name;
   }

   PUBLICPRIVATEEnum(final String name) {
      this.name = name;
   }

   public static PUBLICPRIVATEEnum fromString(String name) {
      for (PUBLICPRIVATEEnum visibility : values()) {
         if (visibility.name.equalsIgnoreCase(name)) {
            return visibility;
         }
      }

      return null;
   }
}
