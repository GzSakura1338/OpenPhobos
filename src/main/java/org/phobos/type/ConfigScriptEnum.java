package org.phobos.type;

public enum ConfigScriptEnum {
   Config("Config"),
   Script("Script"),
   Proxy("Proxy");

   public final String name;

   public static ConfigScriptEnum fromString(String string) {
      return switch (string) {
         case "Config" -> Config;
         case "Script" -> Script;
         case "Proxy" -> Proxy;
         default -> throw new IllegalArgumentException();
      };
   }

   ConfigScriptEnum(final String name) {
      this.name = name;
   }

   @Override
   public String toString() {
      return this.name;
   }
}
