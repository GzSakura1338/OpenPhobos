package org.phobos.core.command;

import org.phobos.core.PhobosClient;
import org.phobos.core.net.packet.ProfilePicturePacket;
import org.phobos.core.util.UrlValidator;
import org.phobos.core.user.UserSession;

public class ProfileCommand extends Command {
   @Override
   public void handleString5(String[] texts) {
      String text = texts[0];
      if (!UrlValidator.testText(text)) {
         this.handleText2("The link provided is either invalid or is not on an allowed website");
      } else {
         this.phobosClient.getPacketBus().handleNetworkPacket2(new ProfilePicturePacket(UserSession.userSession.getText(), text));
         this.handleText6("Updated profile picture to " + text);
      }
   }

   public ProfileCommand(PhobosClient phobosClient) {
      super(phobosClient, "profile", "url");
   }
}
