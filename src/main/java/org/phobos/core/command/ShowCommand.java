package org.phobos.core.command;

import org.phobos.module.Module;
import org.phobos.core.PhobosClient;

public class ShowCommand extends Command {
   public static final CommandSyntax commandSyntax = CommandSyntax.createCommandSyntax("all");

   public ShowCommand(PhobosClient phobosClient) {
      super(phobosClient, "show", "module");
   }

   @Override
   public Suggestion resolveSuggestion(String text, String text2, String[] texts) {
      switch (text) {
         case "module":
            Suggestion suggestion = commandSyntax.resolveSuggestion(text2);
            if (suggestion != null) {
               return suggestion;
            } else {
               Module module = this.phobosClient.getModuleManager().resolveModule(text2);
               if (module != null) {
                  return Suggestion.createSuggestion3(module.getName());
               }
            }
         default:
            return null;
      }
   }

   @Override
   public void handleString5(String[] texts) {
      if (texts[0].equalsIgnoreCase("all")) {
         this.phobosClient.getModuleManager().getList().forEach(module -> module.setFlag4(false));
         this.phobosClient.getHudManager().getHudElementList().forEach(hudElement -> hudElement.setFlag4(false));
         this.handleText6("All modules and hud elements are now visible");
      } else {
         this.phobosClient.getModuleManager().resolveOptional3(texts[0]).ifPresentOrElse(module -> {
            module.setFlag4(false);
            this.handleText6("%s is now visible".formatted(module.getName()));
         }, () -> this.phobosClient.getHudManager().resolveOptional3(texts[0]).ifPresentOrElse(hudElement -> {
            hudElement.setFlag4(false);
            this.handleText6("%s is now visible".formatted(hudElement.getName()));
         }, () -> this.handleText2("Module or Hud element %s doesn't exist".formatted(texts[0]))));
      }
   }
}
