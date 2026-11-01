package org.phobos.type;

public enum API10API11Enum {
   API_1_0("1.0"),
   API_1_1("1.1"),
   API_1_2_beta("1.2+beta");

   public final String version;

   API10API11Enum(final String version) {
      this.version = version;
   }

   public static API10API11Enum get(String version) {
      for (API10API11Enum value : values()) {
         if (value.getVersion().equals(version)) {
            return value;
         }
      }

      return null;
   }

   public String getVersion() {
      return this.version;
   }
}
