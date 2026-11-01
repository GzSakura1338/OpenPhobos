package org.phobos.core.command;

import org.phobos.core.PhobosClient;

public class HelpCommand extends Command {
   public HelpCommand(PhobosClient phobosClient) {
      super(phobosClient, "help");
   }

   @Override
   public void handleString5(String[] texts) {
      StringBuilder stringBuilder = new StringBuilder("Commands: ");

      for (Command command : this.phobosClient.getCommandManager().getCommandLinkedHashSet()) {
         if (!(command instanceof ModuleSettingCommand)) {
            stringBuilder.append(command.getName()).append(", ");
         }
      }

      this.handleText6(stringBuilder.toString());
   }
}
