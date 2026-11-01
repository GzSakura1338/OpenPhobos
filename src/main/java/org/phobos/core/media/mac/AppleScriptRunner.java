package org.phobos.core.media.mac;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class AppleScriptRunner {
   public final Runtime runtime;
   public final String text;
   public static final String text2 = "tell application \"%s\" to %s";
   public final String[] textArray = new String[]{"osascript", "-e", null};

   public String describeWHFofauYLzMy2FRcxWpMvM3TQ7SVAO4F(AppleScriptCommand... appleScriptCommands) throws Exception {
      String text3 = AppleScriptCommand.describeWHFofauYLzMy2FRcxWpMvM3TQ7SVAO4F(appleScriptCommands);
      this.textArray[2] = String.format("tell application \"%s\" to %s", this.text, text3);
      Process process = this.runtime.exec(this.textArray);
      int i = process.waitFor();
      if (i == 0) {
         BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
         StringBuilder stringBuilder = new StringBuilder();

         String text;
         while ((text = bufferedReader.readLine()) != null) {
            stringBuilder.append(text);
         }

         return stringBuilder.toString();
      } else {
         BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(process.getErrorStream()));
         StringBuilder stringBuilder2 = new StringBuilder();

         String text2;
         while ((text2 = bufferedReader2.readLine()) != null) {
            stringBuilder2.append(text2);
         }

         throw new Exception("AppleScript execution \"" + text3 + "\" failed with exit code " + i + ": " + stringBuilder2);
      }
   }

   public String describeAppleScriptCommand(AppleScriptCommand appleScriptCommand, AppleScriptCommand appleScriptCommand2) throws Exception {
      return this.describeWHFofauYLzMy2FRcxWpMvM3TQ7SVAO4F(AppleScriptCommand.INSTANCE2, appleScriptCommand, AppleScriptCommand.INSTANCE, appleScriptCommand2);
   }

   public String describeAppleScriptCommand2(AppleScriptCommand appleScriptCommand) throws Exception {
      return this.describeWHFofauYLzMy2FRcxWpMvM3TQ7SVAO4F(AppleScriptCommand.INSTANCE2, appleScriptCommand);
   }

   public AppleScriptRunner(String text) {
      this.text = text;
      this.runtime = Runtime.getRuntime();
   }
}
