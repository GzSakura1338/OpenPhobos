package org.phobos.core.media.linux;

public class DbusInterface {
   public final String text;

   public DbusInterface(String text) {
      this.text = text;
   }

   @Override
   public String toString() {
      return this.text;
   }
}
