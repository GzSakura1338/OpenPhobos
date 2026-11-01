package org.phobos.core.crypto;

import java.util.Base64;
import org.apache.commons.lang3.mutable.MutableObject;
import org.phobos.type.EncodeDecodeEnum;

public class CipherPass {
   public final String text;
   public final MutableObject<byte[]> bMutableObject;
   public int intValue;
   public final String text2;
   public final MutableObject<String> textMutableObject = new MutableObject(null);

   public void handleMutableObject(MutableObject<String> mutableObject) {
      mutableObject.setValue((String)this.textMutableObject.getValue());
      this.intValue++;
   }

   public byte[] values(String text) {
      return Base64.getDecoder().decode(text);
   }

   public void run() {
      this.handleByte(this.text.getBytes(), this.text2.getBytes());
      this.textMutableObject.setValue(this.describeByte((byte[])this.bMutableObject.getValue()));
      this.bMutableObject.setValue(new byte[0]);
   }

   public void run2() {
      if (this.intValue != 2) {
         throw new IllegalStateException("Non-Double pass");
      }
   }

   public void handleEncodeDecodeEnum(EncodeDecodeEnum encodeDecodeEnum) {
      switch (encodeDecodeEnum) {
         case Decode:
            this.run();
            break;
         case Encode:
            this.run3();
      }

      this.intValue++;
   }

   public CipherPass(String text, String text2) {
      this.bMutableObject = new MutableObject(new byte[0]);
      this.intValue = 0;
      this.text = text;
      this.text2 = text2;
   }

   public String describeByte(byte[] bytes) {
      return Base64.getEncoder().encodeToString(bytes).replaceAll("\\s", "");
   }

   public void handleByte(byte[] bytes, byte[] bytes2) {
      byte[] bytes3 = new byte[bytes.length];

      for (int i = 0; i < bytes.length; i++) {
         bytes3[i] = (byte)(bytes[i] ^ bytes2[i % bytes2.length]);
      }

      this.bMutableObject.setValue(bytes3);
   }

   public void run3() {
      this.handleByte(this.values(this.text), this.text2.getBytes());
      this.textMutableObject.setValue(new String((byte[])this.bMutableObject.getValue()));
      this.bMutableObject.setValue(new byte[0]);
   }
}
