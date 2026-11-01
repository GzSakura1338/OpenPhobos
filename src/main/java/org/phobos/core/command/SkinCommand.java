package org.phobos.core.command;

import org.phobos.core.PhobosClient;
import org.phobos.core.net.packet.SkinPacket;
import org.phobos.core.util.UrlValidator;

public class SkinCommand extends SyntaxCommand {
   public static final CommandSyntax commandSyntax = CommandSyntax.createCommandSyntax("default/slim");

   @Override
   public void handleString(String[] texts, String text) {
      String text2 = texts[0];
      if (!UrlValidator.testText(text2)) {
         this.handleText2("The link provided is either invalid or is not on an allowed website");
      } else if (texts.length != 2) {
         if (texts.length == 1) {
            this.phobosClient.getPacketBus().handleNetworkPacket2(new SkinPacket(text2, "default"));
            this.handleText6("Updating skin URL to %s (default)".formatted(text2));
         } else {
            this.handleText2("Syntax error!");
         }
      } else {
         switch (texts[1].toLowerCase()) {
            case "default":
            case "slim":
               this.phobosClient.getPacketBus().handleNetworkPacket2(new SkinPacket(text2, texts[1]));
               this.handleText6("Updating skin URL to %s (%s)".formatted(text2, texts[1].toLowerCase()));
               return;
            default:
               this.handleText2("Invalid skin type, use default, slim");
         }
      }
   }

   @Override
   public boolean testString(String[] texts) {
      return texts.length >= 1;
   }

   @Override
   public Suggestion resolveSuggestion(String text, String text2, String[] texts) {
      switch (text) {
         case "type":
            return commandSyntax.resolveSuggestion(text2);
         default:
            return null;
      }
   }

   public SkinCommand(PhobosClient phobosClient) {
      super(phobosClient, "skin", "url", "type");
   }
}
