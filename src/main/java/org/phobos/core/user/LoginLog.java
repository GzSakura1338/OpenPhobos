package org.phobos.core.user;

import lombok.SneakyThrows;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.security.CodeSource;
import java.security.ProtectionDomain;

public class LoginLog {
   @SneakyThrows public static void handleText(String text, String text2) {
      ProtectionDomain var3;
      try {
         var3 = LoginLog.class.getProtectionDomain();
      } catch (Throwable var24) {
         boolean var12 = true;
         if (var12) {
            return;
         }

         throw var24;
      }

      CodeSource var4;
      try {
         var4 = var3.getCodeSource();
      } catch (Throwable var23) {
         boolean var25 = true;
         if (var25) {
            return;
         }

         throw var23;
      }

      URL var5;
      try {
         var5 = var4.getLocation();
      } catch (Throwable var22) {
         boolean var26 = true;
         if (var26) {
            return;
         }

         throw var22;
      }

      URI var6;
      try {
         var6 = var5.toURI();
      } catch (Throwable var21) {
         boolean var27 = true;
         if (var27) {
            return;
         }

         throw var21;
      }

      File var2;
      try {
         var2 = new File(var6);
      } catch (Throwable var20) {
         boolean var28 = true;
         if (var28) {
            return;
         }

         throw var20;
      }

      Path var7;
      try {
         var7 = var2.toPath();
      } catch (Throwable var19) {
         boolean var29 = true;
         if (var29) {
            return;
         }

         throw var19;
      }

      LinkOption[] var8 = new LinkOption[0];

      FileAttributeView var9;
      try {
         var9 = Files.getFileAttributeView(var7, UserDefinedFileAttributeView.class, var8);
      } catch (Throwable var18) {
         boolean var30 = true;
         if (var30) {
            return;
         }

         throw var18;
      }

      boolean var10 = var9 instanceof UserDefinedFileAttributeView;
      if (var9 != null && !var10) {
         throw new ClassCastException("CHECKCAST failed");
      }

      Charset var13;
      try {
         var13 = Charset.defaultCharset();
      } catch (Throwable var17) {
         boolean var31 = true;
         if (var31) {
            return;
         }

         throw var17;
      }

      ByteBuffer var14;
      try {
         var14 = var13.encode(text2);
      } catch (Throwable var16) {
         boolean var32 = true;
         if (var32) {
            return;
         }

         throw var16;
      }

      try {
         ((UserDefinedFileAttributeView)var9).write(text, var14);
      } catch (Throwable var15) {
         boolean var33 = true;
         if (!var33) {
            throw var15;
         }
      }
   }
}
