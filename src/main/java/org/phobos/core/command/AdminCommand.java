package org.phobos.core.command;

import java.util.concurrent.TimeUnit;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.phobos.core.net.packet.AdminUserDataPacket;
import org.phobos.core.net.packet.AdminUserPacket;
import org.phobos.core.net.packet.CapePacket;
import org.phobos.core.PhobosClient;
import org.phobos.core.net.packet.SubscriptionPacket;
import org.phobos.core.user.UserSession;

public class AdminCommand extends SyntaxCommand {
   public static final CommandSyntax commandSyntax = CommandSyntax.createCommandSyntax("register/session/avatar/cape");
   public static final CommandSyntax commandSyntax2 = CommandSyntax.createCommandSyntax("User/Beta/Moderator/Admin");
   public static final CommandSyntax commandSyntax3 = CommandSyntax.createCommandSyntax("subscription/password");
   public static final CommandSyntax commandSyntax4 = CommandSyntax.createCommandSyntax("set/add/subtract");

   @Override
   public Suggestion resolveSuggestion(String text, String text2, String[] texts) {
      switch (text) {
         case "action":
            return commandSyntax.resolveSuggestion(text2);
         case "user":
            this.phobosClient.getCloudApiClient().run();
            String text3 = CommandCompletion.describeText3(text2, this.phobosClient.getCloudApiClient().getTextSet());
            if (text3 != null) {
               return Suggestion.createSuggestion(text3);
            }
            break;
         case "type":
            return commandSyntax3.resolveSuggestion(text2);
         case "func":
            if (texts.length != 0) {
               if (texts[0].equalsIgnoreCase("register")) {
                  return commandSyntax2.resolveSuggestion(text2);
               }

               if (texts.length >= 3 && texts[2].equalsIgnoreCase("subscription")) {
                  return commandSyntax4.resolveSuggestion(text2);
               }
            }
      }

      return null;
   }

   @Override
   public boolean testString(String[] texts) {
      return texts.length >= 3;
   }

   public AdminCommand(PhobosClient phobosClient) {
      super(phobosClient, "admin", "action", "user", "type", "func", "time");
   }

   public long computeText(String text) {
      if (text.endsWith("mi")) {
         String text2 = text.replaceAll("mi", "");
         return TimeUnit.MINUTES.toMillis(Long.parseLong(text2));
      } else if (text.endsWith("d")) {
         String text3 = text.replaceAll("d", "");
         return TimeUnit.DAYS.toMillis(Long.parseLong(text3));
      } else if (text.endsWith("m")) {
         String text4 = text.replaceAll("m", "");
         return TimeUnit.DAYS.toMillis(Long.parseLong(text4)) * 30L;
      } else {
         return 0L;
      }
   }

   @Override
   public void handleString(String[] texts, String text) {
      if (!UserSession.userSession.getUserProfile().getPermission().isAdmin()) {
         this.handleText7(Text.literal("No access! You are: " + UserSession.userSession.getUserProfile().getPermission()).formatted(Formatting.RED));
      } else if (texts.length == 0) {
         this.handleText6(
            "Syntax error! (register, user, password, permission, days, hours, minutes) / (mute/unmute, user, reason) / (session, user, (subscription, set/plus/minus, dateformat (10d)) (password, newPassword))"
         );
      } else {
         switch (texts[0].toLowerCase()) {
            case "register":
               if (texts.length == 7) {
                  this.phobosClient
                     .getPacketBus()
                     .handleNetworkPacket2(
                        new AdminUserDataPacket(texts[1], texts[2], texts[3], Long.parseLong(texts[4]), Long.parseLong(texts[5]), Long.parseLong(texts[6]))
                     );
                  this.handleText6("Registered user " + texts[1]);
                  return;
               }

               this.handleText6("Syntax error! (user, password, permission, days, hours, minutes)");
               break;
            case "cape":
               if (texts.length == 3) {
                  this.phobosClient.getPacketBus().handleNetworkPacket2(new CapePacket(texts[1], texts[2]));
                  this.handleText6("Set %s's cape to %s".formatted(texts[1], texts[2]));
                  return;
               }

               this.handleText6("Syntax error! (user, url)");
               break;
            case "session":
               switch (texts[2].toLowerCase()) {
                  case "subscription":
                     if (texts.length != 5) {
                        this.handleText6("Syntax error! (subscription, set/add/subtract, time)");
                        return;
                     }

                     switch (texts[3].toLowerCase()) {
                        case "set":
                           long l = this.computeText(texts[4]);
                           if (l != 0L) {
                              this.phobosClient.getPacketBus().handleNetworkPacket2(new SubscriptionPacket(texts[1], SubscriptionPacket.SubscriptionAction.Set, l));
                              this.handleText6("Set subscription to " + texts[4]);
                           }

                           return;
                        case "add":
                           long l2 = this.computeText(texts[4]);
                           if (l2 != 0L) {
                              this.phobosClient.getPacketBus().handleNetworkPacket2(new SubscriptionPacket(texts[1], SubscriptionPacket.SubscriptionAction.Plus, l2));
                              this.handleText6("Added subscription by " + texts[4]);
                           }

                           return;
                        case "subtract":
                           long l3 = this.computeText(texts[4]);
                           if (l3 != 0L) {
                              this.phobosClient.getPacketBus().handleNetworkPacket2(new SubscriptionPacket(texts[1], SubscriptionPacket.SubscriptionAction.Minus, l3));
                              this.handleText6("Negated subscription by " + texts[4]);
                           }

                           return;
                        default:
                           return;
                     }
                  case "password":
                     if (texts.length != 4) {
                        this.handleText6("Syntax error! (password, pw)");
                        return;
                     }

                     this.phobosClient.getPacketBus().handleNetworkPacket2(new AdminUserPacket(texts[1], texts[3]));
                     this.handleText6("Set %s's password to %s".formatted(texts[1], texts[3]));
               }
         }
      }
   }
}
