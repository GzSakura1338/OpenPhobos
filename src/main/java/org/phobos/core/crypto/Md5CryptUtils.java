package org.phobos.core.crypto;

import java.nio.charset.StandardCharsets;
import org.apache.commons.codec.digest.Md5Crypt;

public class Md5CryptUtils {
   public static String describeText(String text, String text2) {
      return Md5Crypt.md5Crypt(text.getBytes(StandardCharsets.UTF_8), text2);
   }

   public static String describeText2(String text) {
      return Md5Crypt.md5Crypt(text.getBytes(StandardCharsets.UTF_8));
   }
}
