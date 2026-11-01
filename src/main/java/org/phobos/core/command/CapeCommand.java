package org.phobos.core.command;

import org.phobos.core.net.packet.CapePacket;
import org.phobos.core.PhobosClient;
import org.phobos.core.util.UrlValidator;
import org.phobos.core.user.UserSession;

public class CapeCommand extends Command {
   @Override
   public void handleString5(String[] texts) {
      if (mc.player != null) {
         String text = texts[0];
         if (!UrlValidator.testText(text)) {
            this.handleText2("The link provided is either invalid or is not on an allowed website");
         } else {
            this.phobosClient.getPacketBus().handleNetworkPacket2(new CapePacket(UserSession.userSession.getText(), text));
            this.handleText6("Updated cape URL");
         }
      }
   }

   public CapeCommand(PhobosClient phobosClient) {
      super(phobosClient, "cape", "url");
   }
}
