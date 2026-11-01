package org.phobos.core.command;

import net.minecraft.text.Text;
import org.phobos.module.client.IRCModule;
import org.phobos.core.net.packet.ChatMessagePacket;
import org.phobos.core.net.packet.EmptyPacket183;
import org.phobos.core.net.packet.EmptyPacket200;
import org.phobos.core.net.packet.IrcCommandPacket;
import org.phobos.core.PhobosClient;
import org.phobos.core.user.UserSession;

public class IrcCommand extends SyntaxCommand {
   public static final CommandSyntax commandSyntax = CommandSyntax.createCommandSyntax("online/friendall/dm/mute/unmute");

   public IrcCommand(PhobosClient phobosClient) {
      super(phobosClient, "irc", "action", "syntax");
   }

   @Override
   public void handleString(String[] texts, String text) {
      if (texts.length == 0) {
         long[] longs = UserSession.values(UserSession.userSession.getUserProfile().getDate());
         long l = longs[0];
         long l2 = longs[1];
         long l3 = longs[2];
         this.handleText7(
            Text.literal("Logged in as ")
               .append(
                  Text.literal(UserSession.userSession.getText())
                     .withColor(UserSession.userSession.getUserProfile().getPermission().getColor().hashCode())
               )
         );
         this.handleText7(Text.literal("Time left: ").append(Text.literal(String.format("%s days, %s hours, %s minutes.", l, l2, l3))));
      } else {
         switch (texts[0].toLowerCase()) {
            case "mute":
               if (!UserSession.userSession.getUserProfile().getPermission().isModerator()) {
                  this.handleText2("Insufficient permissions");
                  return;
               }

               if (texts.length < 3) {
                  this.handleText2("Syntax error! Usage: irc mute username reason optional:days");
                  return;
               }

               IrcCommandPacket ircCommandPacket = new IrcCommandPacket();
               ircCommandPacket.setText(texts[1]);
               ircCommandPacket.setText2(texts[2]);
               ircCommandPacket.setFlag(true);

               try {
                  ircCommandPacket.setIntValue(texts.length > 3 ? Integer.parseInt(texts[3]) : -1);
               } catch (Throwable throwable) {
                  ircCommandPacket.setIntValue(-1);
               }

               this.phobosClient.getPacketBus().handleNetworkPacket2(ircCommandPacket);
               this.handleText6("Muted %s%s".formatted(texts[1], ircCommandPacket.getIntValue() > 0 ? " for %s days".formatted(ircCommandPacket.getIntValue()) : ""));
               break;
            case "unmute":
               if (!UserSession.userSession.getUserProfile().getPermission().isModerator()) {
                  this.handleText2("Insufficient permissions");
                  return;
               }

               if (texts.length < 2) {
                  this.handleText2("Syntax error! Usage: irc unmute username");
                  return;
               }

               IrcCommandPacket ircCommandPacket2 = new IrcCommandPacket();
               ircCommandPacket2.setText(texts[1]);
               ircCommandPacket2.setText2("none");
               ircCommandPacket2.setFlag(false);
               ircCommandPacket2.setIntValue(-1);
               this.phobosClient.getPacketBus().handleNetworkPacket2(ircCommandPacket2);
               this.handleText6("Unmuted " + texts[1]);
               break;
            case "online":
               if (texts.length != 1) {
                  this.handleText2("Syntax error! Usage: irc online");
                  return;
               }

               this.phobosClient.getPacketBus().handleNetworkPacket2(new EmptyPacket200());
               break;
            case "friendall":
               if (texts.length != 1) {
                  this.handleText2("Syntax error! Usage: irc friendall");
                  return;
               }

               this.phobosClient.getPacketBus().handleNetworkPacket2(new EmptyPacket183());
               break;
            case "dm":
               if (texts.length >= 3) {
                  StringBuilder stringBuilder = new StringBuilder();

                  for (int i = 2; i < texts.length; i++) {
                     stringBuilder.append(texts[i]).append(" ");
                  }

                  this.phobosClient
                     .getPacketBus()
                     .handleNetworkPacket2(new ChatMessagePacket(stringBuilder.toString(), UserSession.userSession.getText(), texts[1]));
                  return;
               }

               this.handleText2("Syntax error! Usage: irc dm username content...");
               break;
            case "r":
               if (texts.length >= 2) {
                  if (IRCModule.getText2() == null) {
                     this.handleText2("Unable to find direct message chain!");
                     return;
                  }

                  StringBuilder stringBuilder2 = new StringBuilder();

                  for (int i2 = 1; i2 < texts.length; i2++) {
                     stringBuilder2.append(texts[i2]).append(" ");
                  }

                  this.phobosClient
                     .getPacketBus()
                     .handleNetworkPacket2(new ChatMessagePacket(stringBuilder2.toString(), UserSession.userSession.getText(), IRCModule.getText2()));
                  return;
               }

               this.handleText2("Syntax error! Usage: irc r content...");
         }
      }
   }

   @Override
   public Suggestion resolveSuggestion(String text, String text2, String[] texts) {
      switch (text) {
         case "action":
            Suggestion suggestion = commandSyntax.resolveSuggestion(text2);
            if (suggestion != null
               && (
                  !suggestion.getText().equals("mute") && !suggestion.getText().equals("unmute")
                     || UserSession.userSession.getUserProfile().getPermission().isModerator()
               )) {
               return suggestion;
            }
            break;
         case "syntax":
            if (texts.length != 0) {
               String text4 = texts[0];
               switch (text4.toLowerCase()) {
                  case "mute":
                  case "unmute":
                  case "dm":
                     this.phobosClient.getCloudApiClient().run();
                     String text3 = CommandCompletion.describeText3(text2, this.phobosClient.getCloudApiClient().getTextSet());
                     if (text3 != null) {
                        return Suggestion.createSuggestion(text3);
                     }
               }
            }
      }

      return null;
   }

   @Override
   public boolean testString(String[] texts) {
      return true;
   }
}
