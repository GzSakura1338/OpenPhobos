package org.phobos.core.command;

import org.phobos.module.Module;
import org.phobos.core.PhobosClient;

public class RenameCommand extends Command {
   public RenameCommand(PhobosClient phobosClient) {
      super(phobosClient, "rename", "module", "name");
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

   @Override
   public void handleString5(String[] texts) {
      this.phobosClient.getModuleManager().resolveOptional3(texts[0]).ifPresentOrElse(module -> {
         module.setText3(texts[1]);
         this.handleText6("Renamed %s to %s".formatted(module.describe4(), texts[1]));
      }, () -> this.handleText2("Module %s not found".formatted(texts[0])));
   }
}
