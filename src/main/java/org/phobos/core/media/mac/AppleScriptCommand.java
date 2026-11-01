package org.phobos.core.media.mac;

public class AppleScriptCommand {
   public static final AppleScriptCommand INSTANCE = new AppleScriptCommand("of");
   public final String text;
   public static final AppleScriptCommand INSTANCE2 = new AppleScriptCommand("get", "the");

   public static String describeWHFofauYLzMy2FRcxWpMvM3TQ7SVAO4F(AppleScriptCommand... appleScriptCommands) {
      String[] texts = new String[appleScriptCommands.length];

      for (int i = 0; i < appleScriptCommands.length; i++) {
         texts[i] = appleScriptCommands[i].toString();
      }

      return String.join(" ", texts);
   }

   public AppleScriptCommand(AppleScriptCommand... appleScriptCommands) {
      this.text = describeWHFofauYLzMy2FRcxWpMvM3TQ7SVAO4F(appleScriptCommands);
   }

   public AppleScriptCommand(String text) {
      this.text = text;
   }

   @Override
   public String toString() {
      return this.text;
   }

   public AppleScriptCommand(String... texts) {
      this.text = String.join(" ", texts);
   }
}
