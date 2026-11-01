package org.phobos.core.media.linux;

public class DbusArgument {
   public final String text;
   public final String text2;

   @Override
   public String toString() {
      return String.format("--%s%s", this.text, this.text2 == null ? "" : "=" + this.text2);
   }

   public DbusArgument(String text) {
      this(text, null);
   }

   public DbusArgument(String text, String text2) {
      this.text = text;
      this.text2 = text2;
   }
}
