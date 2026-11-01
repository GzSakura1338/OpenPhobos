package org.phobos.core.crypto;

import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.NotNull;
import org.phobos.type.EncodeDecodeEnum;

public class StringCipher {
   public static void handleText(String text, MutableObject<String> mutableObject, EncodeDecodeEnum encodeDecodeEnum) {
      String text2 = createText("906034".toCharArray());
      String text3 = createText("819645".toCharArray());
      String text4 = createText("855661".toCharArray());
      switch (encodeDecodeEnum) {
         case Decode:
            CipherPass cipherPass = new CipherPass(text, text2);
            cipherPass.handleEncodeDecodeEnum(EncodeDecodeEnum.Encode);
            cipherPass.handleMutableObject(mutableObject);
            CipherPass cipherPass2 = new CipherPass((String)mutableObject.getValue(), text3);
            cipherPass2.handleEncodeDecodeEnum(EncodeDecodeEnum.Encode);
            cipherPass2.handleMutableObject(mutableObject);
            CipherPass cipherPass3 = new CipherPass((String)mutableObject.getValue(), text4);
            cipherPass3.handleEncodeDecodeEnum(EncodeDecodeEnum.Encode);
            cipherPass3.handleMutableObject(mutableObject);
            break;
         case Encode:
            CipherPass cipherPass4 = new CipherPass(text, text4);
            cipherPass4.handleEncodeDecodeEnum(EncodeDecodeEnum.Decode);
            cipherPass4.handleMutableObject(mutableObject);
            CipherPass cipherPass5 = new CipherPass((String)mutableObject.getValue(), text3);
            cipherPass5.handleEncodeDecodeEnum(EncodeDecodeEnum.Decode);
            cipherPass5.handleMutableObject(mutableObject);
            CipherPass cipherPass6 = new CipherPass((String)mutableObject.getValue(), text2);
            cipherPass6.handleEncodeDecodeEnum(EncodeDecodeEnum.Decode);
            cipherPass6.handleMutableObject(mutableObject);
      }
   }

   @NotNull
   public static String describeText(@NotNull String text) {
      return describeText4(describeText4(text));
   }

   @NotNull
   public static String describeText2(@NotNull String text) {
      return describeText3(describeText3(text));
   }

   public static String createText(char[] chars) {
      if (new String(chars).isEmpty()) {
         return "";
      }

      return switch (chars.length) {
         case 2 -> new String(new char[]{chars[1], chars[0]});
         default -> new String(chars);
         case 4 -> new String(new char[]{chars[2], chars[3], chars[0], chars[1]});
         case 6 -> new String(new char[]{chars[2], chars[1], chars[0], chars[5], chars[4], chars[3]});
         case 8 -> new String(new char[]{chars[7], chars[2], chars[1], chars[4], chars[3], chars[6], chars[5], chars[0]});
      };
   }

   @NotNull
   public static String describeText3(@NotNull String text) {
      MutableObject<String> mutableObject = new MutableObject();
      handleText(text, mutableObject, EncodeDecodeEnum.Decode);
      return (String)mutableObject.getValue();
   }

   @NotNull
   public static String describeText4(@NotNull String text) {
      MutableObject<String> mutableObject = new MutableObject();
      handleText(text, mutableObject, EncodeDecodeEnum.Encode);
      return (String)mutableObject.getValue();
   }
}
