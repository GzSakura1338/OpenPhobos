package org.phobos.core.command;

import org.jetbrains.annotations.Nullable;

public class CommandSyntax {
   public final String[] textArray;

   public CommandSyntax(String... texts) {
      this.textArray = texts;
   }

   public static CommandSyntax createCommandSyntax(String text) {
      return new CommandSyntax(text.split("/"));
   }

   @Nullable
   public Suggestion resolveSuggestion(String text) {
      for (String text2 : this.textArray) {
         if (text2.startsWith(text)) {
            return Suggestion.createSuggestion3(text2);
         }
      }

      return null;
   }
}
