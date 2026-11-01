package org.phobos.core.media.linux;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class DbusSend {
   public static final DbusArgument printReply = new DbusArgument("print-reply");
   public final DbusArgument[] dbusArgumentArray;
   public static final DbusInterface dbusInterface = new DbusInterface("org.freedesktop.DBus.Properties.Get");
   public final String text;
   public final Runtime runtime;

   public DbusVariant resolveDbusVariant(DbusInterface dbusInterface, String... texts) throws Exception {
      String[] texts2 = new String[2 + this.dbusArgumentArray.length + 2 + texts.length];
      texts2[0] = "dbus-send";
      texts2[1] = printReply.toString();

      for (int i = 0; i < this.dbusArgumentArray.length; i++) {
         texts2[2 + i] = this.dbusArgumentArray[i].toString();
      }

      texts2[2 + this.dbusArgumentArray.length] = this.text;
      texts2[2 + this.dbusArgumentArray.length + 1] = dbusInterface.toString();

      for (int i2 = 0; i2 < texts.length; i2++) {
         texts2[2 + this.dbusArgumentArray.length + 2 + i2] = texts[i2];
      }

      Process process = this.runtime.exec(texts2);
      int i3 = process.waitFor();
      if (i3 == 0) {
         BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
         StringBuilder stringBuilder = new StringBuilder();

         String text;
         while ((text = bufferedReader.readLine()) != null) {
            if (!text.startsWith("method ")) {
               stringBuilder.append(text).append("\n");
            }
         }

         return stringBuilder.toString().isEmpty() ? new DbusVariant("success", true) : DbusVariant.createDbusVariant(stringBuilder.toString());
      } else {
         BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(process.getErrorStream()));
         StringBuilder stringBuilder2 = new StringBuilder();

         String text2;
         while ((text2 = bufferedReader2.readLine()) != null) {
            stringBuilder2.append(text2);
         }

         throw new Exception("dbus-send execution \"" + String.join(" ", texts2) + "\" failed with exit code " + i3 + ": " + stringBuilder2);
      }
   }

   public DbusVariant resolveDbusVariant2(String... texts) throws Exception {
      String[] texts2 = new String[texts.length];

      for (int i = 0; i < texts.length; i++) {
         texts2[i] = String.format("string:%s", texts[i]);
      }

      return this.resolveDbusVariant(dbusInterface, texts2);
   }

   public DbusSend(DbusArgument[] dbusArguments, String text) {
      this.dbusArgumentArray = dbusArguments;
      this.text = text;
      this.runtime = Runtime.getRuntime();
   }
}
