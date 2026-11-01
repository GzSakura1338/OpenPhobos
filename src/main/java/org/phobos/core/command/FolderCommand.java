package org.phobos.core.command;

import java.awt.Desktop;
import org.phobos.core.config.ConfigManager;
import org.phobos.core.PhobosClient;

public class FolderCommand extends Command {
   public FolderCommand(PhobosClient phobosClient) {
      super(phobosClient, "folder");
   }

   @Override
   public void handleString5(String[] texts) {
      try {
         Desktop.getDesktop().open(ConfigManager.path2.toFile());
      } catch (Throwable var3) {
      }
   }
}
