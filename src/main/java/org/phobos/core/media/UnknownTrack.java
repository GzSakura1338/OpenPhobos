package org.phobos.core.media;

public class UnknownTrack {
   public static final UnknownTrack INSTANCE = new UnknownTrack("Unknown", "No song playing");
   public final String name;
   public final String text;
   public static final String text2 = " - ";

   public static UnknownTrack createUnknownTrack(String text) {
      if (!text.contains(" - ")) {
         return null;
      }

      String[] texts = text.split(" - ");
      return new UnknownTrack(texts[1], texts[0]);
   }

   public String getName() {
      return this.name;
   }

   public UnknownTrack(String text, String text2) {
      this.name = text;
      this.text = text2;
   }

   public String getText() {
      return this.text;
   }

   @Override
   public String toString() {
      return this.name + " - " + this.text;
   }
}
