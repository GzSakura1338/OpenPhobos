package org.phobos.core.command;

import org.phobos.module.Module;
import org.phobos.type.NormalHoldEnum;
import org.phobos.core.bind.BindKey;
import org.phobos.core.bind.KeyCombination;
import org.phobos.core.PhobosClient;
import org.phobos.core.setting.Setting;

public class BindCommand extends Command {
   @Override
   public Suggestion resolveSuggestion(String text, String text2, String[] texts) {
      switch (text) {
         case "module":
            Module module = this.phobosClient.getModuleManager().resolveModule(text2);
            if (module != null) {
               return Suggestion.createSuggestion3(module.getName());
            }
            break;
         case "key":
            BindKey bindKey = this.phobosClient.getBindManager().resolveBindKey3(text2);
            if (bindKey != null) {
               return Suggestion.createSuggestion3(bindKey.getName());
            }
      }

      return null;
   }

   public BindCommand(PhobosClient phobosClient) {
      super(phobosClient, "bind", "module", "key");
   }

   @Override
   public void handleString5(String[] texts) {
      this.phobosClient.getModuleManager().resolveOptional3(texts[0]).ifPresent(module -> {
         Setting<KeyCombination> setting = module.getBind();
         BindKey bindKey = this.phobosClient.getBindManager().resolveBindKey2(texts[1]);
         NormalHoldEnum normalHoldEnum = module.getBind().getValue().getNormalHoldEnum();
         if (bindKey != null) {
            setting.setValue(new KeyCombination(normalHoldEnum, bindKey));
            this.handleText6("Set %s key to %s".formatted(module.getName(), bindKey.getName()));
         }
      });
   }
}
