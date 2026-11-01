package org.phobos.core.command;

import org.phobos.type.PLAYPAUSENEXTEnum;
import org.phobos.core.PhobosClient;

public class SpotifyCommand extends Command {
   public static final CommandSyntax commandSyntax = CommandSyntax.createCommandSyntax("play/stop/</>");

   public SpotifyCommand(PhobosClient phobosClient) {
      super(phobosClient, "spotify", "action");
   }

   @Override
   public Suggestion resolveSuggestion(String text, String text2, String[] texts) {
      switch (text) {
         case "action":
            return commandSyntax.resolveSuggestion(text2);
         default:
            return null;
      }
   }

   @Override
   public void handleString5(String[] texts) {
      if (this.phobosClient.getMediaPlayerApi() != null) {
         switch (texts[0].toLowerCase()) {
            case "play":
               if (!this.phobosClient.getMediaPlayerApi().isFlag2() && this.phobosClient.getMediaPlayerApi().check2()) {
                  this.phobosClient.getMediaPlayerApi().handlePLAYPAUSENEXTEnum(PLAYPAUSENEXTEnum.PLAY_PAUSE);
               }
               break;
            case "stop":
               if (this.phobosClient.getMediaPlayerApi().isFlag2()) {
                  this.phobosClient.getMediaPlayerApi().handlePLAYPAUSENEXTEnum(PLAYPAUSENEXTEnum.PLAY_PAUSE);
               }
               break;
            case ">":
               if (this.phobosClient.getMediaPlayerApi().check2()) {
                  this.phobosClient.getMediaPlayerApi().handlePLAYPAUSENEXTEnum(PLAYPAUSENEXTEnum.NEXT);
               }
               break;
            case "<":
               if (this.phobosClient.getMediaPlayerApi().check2()) {
                  this.phobosClient.getMediaPlayerApi().handlePLAYPAUSENEXTEnum(PLAYPAUSENEXTEnum.PREV);
               }
         }
      }
   }
}
