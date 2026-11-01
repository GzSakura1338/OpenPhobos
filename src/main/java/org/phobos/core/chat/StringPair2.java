package org.phobos.core.chat;

public class StringPair2 {
   public final String text;
   public final String text2;

   public String getText2() {
      return this.text2;
   }

   public StringPair2(String text, String text2) {
      this.text2 = text;
      this.text = text2;
   }

   public String getText() {
      return this.text;
   }
}
