package org.phobos.core.command;

import org.phobos.core.PhobosClient;

public class FakePlayerCommand extends SyntaxCommand {
   public static final CommandSyntax commandSyntax = CommandSyntax.createCommandSyntax("add/remove/clear/record/complete");

   @Override
   public Suggestion resolveSuggestion(String text, String text2, String[] texts) {
      switch (text) {
         case "action":
            return commandSyntax.resolveSuggestion(text2);
         default:
            return null;
      }
   }

   public FakePlayerCommand(PhobosClient phobosClient) {
      super(phobosClient, "fakeplayer", "action");
   }

   @Override
   public boolean testString(String[] texts) {
      return texts.length == 0 || texts.length == 1;
   }

   @Override
   public void handleString(String[] texts, String text) {
      if (mc.world != null && mc.player != null) {
         if (texts.length != 0) {
            switch (texts[0].toLowerCase()) {
               case "add":
                  this.phobosClient.getPlayerStateTracker().run3();
                  break;
               case "remove":
                  this.phobosClient.getPlayerStateTracker().remove();
                  break;
               case "clear":
                  this.phobosClient.getPlayerStateTracker().clear();
                  break;
               case "record":
                  this.phobosClient.getPlayerStateTracker().run();
                  break;
               case "complete":
                  this.phobosClient.getPlayerStateTracker().run2();
            }
         } else if (this.phobosClient.getPlayerStateTracker().check()) {
            this.phobosClient.getPlayerStateTracker().clear();
         } else {
            this.phobosClient.getPlayerStateTracker().run3();
         }
      }
   }
}
