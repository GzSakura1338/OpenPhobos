package org.phobos.core.chat;

public class TextPair {
   public final String text;
   public final String text2;

   public String getText() {
      return this.text;
   }

   public String getText2() {
      return this.text2;
   }

   public TextPair(String text, String text2) {
      this.text2 = text;
      this.text = text2;
   }
}
