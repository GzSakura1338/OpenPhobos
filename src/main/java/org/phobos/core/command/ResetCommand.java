package org.phobos.core.command;

import org.phobos.module.Module;
import org.phobos.core.PhobosClient;

public class ResetCommand extends Command {
   public ResetCommand(PhobosClient phobosClient) {
      super(phobosClient, "reset", "module");
   }

   @Override
   public void handleString5(String[] texts) {
      this.phobosClient.getModuleManager().resolveOptional3(texts[0]).ifPresentOrElse(module -> {
         module.handleFlag3(false);
         this.handleText6("Resetting " + module.getName());
      }, () -> this.handleText2("Module %s not found".formatted(texts[0])));
   }

   @Override
   public Suggestion resolveSuggestion(String text, String text2, String[] texts) {
      switch (text) {
         case "module":
            Module module = this.phobosClient.getModuleManager().resolveModule(text2);
            if (module != null) {
               return Suggestion.createSuggestion3(module.getName());
            }
         default:
            return null;
      }
   }
}
