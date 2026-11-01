package org.phobos.core.command;

import org.phobos.module.Module;
import org.phobos.core.net.packet.ExportPacket;
import org.phobos.core.PhobosClient;

public class ExportCommand extends Command {
   public static final CommandSyntax commandSyntax = new CommandSyntax("module");

   @Override
   public void handleString5(String[] texts) {
      this.phobosClient.getImportHandler().resolveOptional2(texts[0], texts[1]).ifPresent(keyValue -> {
         String text = texts[0].toLowerCase() + "##" + keyValue.getObject().getName() + "##" + keyValue.getObject2();
         this.phobosClient.getPacketBus().handleNetworkPacket2(new ExportPacket(texts[2], text));
      });
   }

   @Override
   public Suggestion resolveSuggestion(String text, String text2, String[] texts) {
      switch (text) {
         case "type":
            return commandSyntax.resolveSuggestion(text2);
         case "generic":
            if (texts.length >= 1) {
               switch (texts[0]) {
                  case "module":
                     Module module = this.phobosClient.getModuleManager().resolveModule(text2);
                     if (module != null) {
                        return Suggestion.createSuggestion3(module.getName());
                     }
               }
            }
            break;
         case "user":
            this.phobosClient.getCloudApiClient().run();
            String text3 = CommandCompletion.describeText3(text2, this.phobosClient.getCloudApiClient().getTextSet());
            if (text3 != null) {
               return Suggestion.createSuggestion(text3);
            }
      }

      return null;
   }

   public ExportCommand(PhobosClient phobosClient) {
      super(phobosClient, "export", "type", "generic", "user");
   }
}
