package org.phobos.core.command;

import org.jetbrains.annotations.NotNull;

public class Suggestion {
   @NotNull
   public final String text;
   public final boolean flag;

   public boolean isFlag() {
      return this.flag;
   }

   @NotNull
   public String getText() {
      return this.text;
   }

   public static Suggestion createSuggestion(String text) {
      return new Suggestion(text, true);
   }

   public static Suggestion createSuggestion2(String text, String text2) {
      return new Suggestion(CommandCompletion.describeText2(text, text2), false);
   }

   public Suggestion(@NotNull String text, boolean flag) {
      if (text == null) {
         throw new NullPointerException("data is marked non-null but is null");
      }

      this.text = text;
      this.flag = flag;
   }

   public static Suggestion createSuggestion3(String text) {
      return new Suggestion(text, false);
   }
}
