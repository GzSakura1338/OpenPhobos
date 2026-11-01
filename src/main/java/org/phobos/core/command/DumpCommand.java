package org.phobos.core.command;

import org.phobos.core.PhobosClient;
import org.phobos.core.user.UserSession;

public class DumpCommand extends Command {
   public DumpCommand(PhobosClient phobosClient) {
      super(phobosClient, "dump");
   }

   @Override
   public void handleString5(String[] texts) {
      if (UserSession.userSession.getUserProfile().getPermission().isBeta()) {
         ;
      }
   }
}
