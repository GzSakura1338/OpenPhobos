package org.phobos.core.media;

public class MediaTrack {
   public final int intValue;
   public final String text;
   public final String text2;
   public final String name;

   public int getIntValue() {
      return this.intValue;
   }

   public String getText2() {
      return this.text2;
   }

   public String describe() {
      return String.format("%s - %s", this.text2, this.name);
   }

   public MediaTrack(String text, String text2, String text3, int i) {
      this.text = text;
      this.name = text2;
      this.text2 = text3;
      this.intValue = i;
   }

   @Override
   public boolean equals(Object object) {
      return object instanceof MediaTrack && this.text.equals(((MediaTrack)object).text);
   }

   public String getName() {
      return this.name;
   }

   @Override
   public int hashCode() {
      return this.text.hashCode();
   }

   public String getText() {
      return this.text;
   }

   @Override
   public String toString() {
      return String.format("[%s] %s - %s", this.text, this.name, this.text2);
   }
}
