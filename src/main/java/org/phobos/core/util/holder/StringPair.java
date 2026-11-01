package org.phobos.core.util.holder;

public final class StringPair {
   public final String text;
   public final String text2;

   public String getText2() {
      return this.text2;
   }

   public StringPair(String text, String text2) {
      this.text = text;
      this.text2 = text2;
   }

   public String getText() {
      return this.text;
   }
}
