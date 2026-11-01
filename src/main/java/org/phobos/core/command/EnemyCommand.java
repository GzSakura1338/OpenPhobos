package org.phobos.core.command;

import com.mojang.authlib.GameProfile;
import java.awt.Color;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.phobos.core.chat.ChatTextUtils;
import org.phobos.core.entity.EntityUtils;
import org.phobos.core.PhobosClient;

public class EnemyCommand extends SyntaxCommand {
   public static final CommandSyntax commandSyntax = CommandSyntax.createCommandSyntax("add/del/remove/delete/list/clear");

   @Override
   public boolean testString(String[] texts) {
      return texts.length > 0 && texts.length < 3;
   }

   @Override
   public Suggestion resolveSuggestion(String text, String text2, String[] texts) {
      switch (text) {
         case "action":
            return commandSyntax.resolveSuggestion(text2);
         case "player":
            if (texts.length != 0) {
               switch (texts[0].toLowerCase()) {
                  case "add":
                     if (mc.getNetworkHandler() != null) {
                        PlayerListEntry playerListEntry = CommandCompletion.resolveObject(
                           text2,
                           mc.getNetworkHandler().getPlayerList(),
                           playerListEntryx -> playerListEntryx.getProfile().getName().toLowerCase(),
                           playerListEntryx -> !this.phobosClient.getEnemyList().testUUID(playerListEntryx.getProfile().getId())
                        );
                        if (playerListEntry != null) {
                           return Suggestion.createSuggestion(playerListEntry.getProfile().getName());
                        }
                     }
                     break;
                  case "remove":
                  case "delete":
                  case "del":
                     String text3 = CommandCompletion.describeText3(text2, this.phobosClient.getEnemyList().getTextMap().keySet());
                     if (text3 != null) {
                        return Suggestion.createSuggestion(text3);
                     }
               }
            }
         default:
            return null;
      }
   }

   public EnemyCommand(PhobosClient phobosClient) {
      super(phobosClient, "enemy", "action", "player");
   }

   @Override
   public void handleString(String[] texts, String text) {
      if (texts.length == 1) {
         switch (texts[0].toLowerCase()) {
            case "list":
               StringBuilder stringBuilder = new StringBuilder();

               for (String text2 : this.phobosClient.getFriendList().getSet()) {
                  stringBuilder.append(text2).append(Formatting.RESET).append(", ");
               }

               String text3 = stringBuilder.toString();
               String text4 = text3.substring(0, text3.length() - 2);
               this.handleText6("Friends: " + text4);
               break;
            case "clear":
               this.phobosClient.getFriendList().clear();
         }
      } else {
         EntityUtils.resolveOptional(texts[1]).ifPresent(playerListEntry -> {
            GameProfile gameProfile2 = playerListEntry.getProfile();
            String textx = ChatTextUtils.describeText6(gameProfile2.getName());
            switch (texts[0].toLowerCase()) {
               case "add":
                  if (this.phobosClient.getFriendList().testText(textx, gameProfile2.getId())) {
                     this.handleText2("%s is already an enemy".formatted(textx));
                     return;
                  }

                  this.phobosClient.getEnemyList().handleText(textx, gameProfile2.getId());
                  this.handleText7(Text.literal("Added " + textx + " as an enemy").withColor(Color.RED.hashCode()));
                  break;
               case "del":
               case "remove":
               case "delete":
                  if (this.phobosClient.getEnemyList().resolveUUID(textx) != null) {
                     this.handleText6("Deleted " + textx + " from the enemy list");
                     return;
                  }

                  if (mc.getNetworkHandler() == null) {
                     return;
                  }

                  PlayerListEntry playerListEntry2 = mc.getNetworkHandler().getPlayerListEntry(textx);
                  if (playerListEntry2 == null) {
                     return;
                  }

                  GameProfile gameProfile = playerListEntry2.getProfile();
                  this.phobosClient.getEnemyList().handleUUID(gameProfile.getId());
                  this.handleText7(Text.literal("Deleted " + gameProfile.getName() + " from the enemy list").withColor(Color.RED.hashCode()));
            }
         });
      }
   }
}
