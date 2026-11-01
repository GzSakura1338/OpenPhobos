package org.phobos.core.crypto;

import lombok.SneakyThrows;
import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

public final class FileHashUtils {
   @SneakyThrows public static byte[] values(File file) throws Exception {
      MessageDigest var1 = MessageDigest.getInstance("MD5");
      FileInputStream var2 = new FileInputStream(file);
      byte[] var3 = new byte[1024];

      while (true) {
         Throwable var5;
         label53: {
            label54: {
               int var4;
               try {
                  var4 = var2.read(var3);
               } catch (Throwable var14) {
                  var5 = var14;
                  boolean var6 = true;
                  if (!var6) {
                     break label53;
                  }
                  break label54;
               }

               if (var4 == -1) {
                  var2.close();
                  byte[] var7 = var1.digest();
                  byte[] var8 = var7;
                  return var8;
               }

               try {
                  var1.update(var3, 0, var4);
                  continue;
               } catch (Throwable var13) {
                  var5 = var13;
                  boolean var15 = true;
                  if (!var15) {
                     break label53;
                  }
               }
            }

            try {
               var2.close();
            } catch (Throwable var12) {
               boolean var11 = true;
               if (!var11) {
                  throw var12;
               }

               var5.addSuppressed(var12);
            }

            throw var5;
         }

         Throwable var9 = var5;
         throw var9;
      }
   }

   public FileHashUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
