package org.phobos.core.command;

import org.phobos.module.Module;
import org.phobos.core.PhobosClient;

public class ToggleCommand extends Command {
   @Override
   public void handleString5(String[] texts) {
      this.phobosClient
         .getModuleManager()
         .resolveOptional3(texts[0])
         .ifPresentOrElse(Module::run9, () -> this.handleText2("Module %s not found".formatted(texts[0])));
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

   public ToggleCommand(PhobosClient phobosClient) {
      super(phobosClient, "toggle", "module");
   }
}
