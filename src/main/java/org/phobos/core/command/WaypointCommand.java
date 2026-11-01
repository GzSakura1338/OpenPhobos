package org.phobos.core.command;

import java.util.Set;
import org.phobos.module.render.WaypointsModule;
import org.phobos.module.render.WaypointsModule;
import org.phobos.core.chat.ChatTextUtils;
import org.phobos.core.PhobosClient;
import org.phobos.core.net.packet.WaypointPacket;

public class WaypointCommand extends SyntaxCommand {
   public static final CommandSyntax commandSyntax = CommandSyntax.createCommandSyntax("overworld/ovw/nether/end/o/n/e");
   public static final CommandSyntax commandSyntax2 = CommandSyntax.createCommandSyntax("add/del/remove/list/share");

   @Override
   public Suggestion resolveSuggestion(String text, String text2, String[] texts) {
      switch (text) {
         case "action":
            return commandSyntax2.resolveSuggestion(text2);
         case "name":
            switch (texts[0].toLowerCase()) {
               case "del":
               case "remove":
               case "share":
                  WaypointsModule.Waypoint waypoint = this.phobosClient.getWaypointManager().resolveWaypoint(text2);
                  if (waypoint != null) {
                     return Suggestion.createSuggestion3(waypoint.getName());
                  }
            }
         default:
            return null;
         case "dimension":
            switch (texts[0].toLowerCase()) {
               case "share":
                  this.phobosClient.getCloudApiClient().run();
                  Set<String> set = this.phobosClient.getCloudApiClient().getTextSet();
                  set.add("all");
                  String text3 = CommandCompletion.describeText3(text2, set);
                  if (text3 != null) {
                     return Suggestion.createSuggestion(text3);
                  }
               default:
                  return commandSyntax.resolveSuggestion(text2);
            }
      }
   }

   @Override
   public boolean testString(String[] texts) {
      return texts.length >= 2;
   }

   @Override
   public void handleString(String[] texts, String text) {
      switch (texts[0].toLowerCase()) {
         case "add":
            if (texts.length != 6) {
               this.handleText2("Invalid argument count! Usage: waypoint add name dimension x y z");
               return;
            }

            String text6 = texts[2].toLowerCase();
            String text2;
            if (text6.contains("overworld") || text6.contains("ovw") || text6.equalsIgnoreCase("o")) {
               text2 = "O";
            } else if (text6.contains("nether") || text6.equalsIgnoreCase("n")) {
               text2 = "N";
            } else if (!text6.contains("end") && !text6.equalsIgnoreCase("e")) {
               text2 = null;
            } else {
               text2 = "E";
            }

            if (text2 == null) {
               return;
            }

            WaypointsModule.WaypointDimension waypointDimension = WaypointsModule.WaypointDimension.fromString(text2);

            double d;
            double d2;
            double d3;
            try {
               d = Double.parseDouble(texts[3]);
               d2 = Double.parseDouble(texts[4]);
               d3 = Double.parseDouble(texts[5]);
            } catch (Throwable throwable) {
               this.handleText2("Invalid argument count! Usage: waypoint del name");
               return;
            }

            this.phobosClient.getWaypointManager().testDouble(d, d2, d3, ChatTextUtils.describeText4(texts[1]).replaceAll(";", ":"), waypointDimension);
            break;
         case "del":
         case "remove":
            if (texts.length != 2) {
               this.handleText2("Invalid argument count!");
               return;
            }

            if (this.phobosClient.getWaypointManager().testText(texts[1])) {
               this.handleText6("Waypoint %s was deleted".formatted(texts[1]));
               return;
            }

            this.handleText2("Waypoint %s not found".formatted(texts[1]));
            break;
         case "list":
            if (texts.length != 1) {
               this.handleText2("Invalid argument count!");
               return;
            }

            for (WaypointsModule.Waypoint waypoint : this.phobosClient.getWaypointManager()) {
               this.handleText6(
                  "%s - X: %s, Y: %s, Z: %s"
                     .formatted(waypoint.getName(), waypoint.getDoubleValue22(), waypoint.getDoubleValue42(), waypoint.getDoubleValue3())
               );
            }
            break;
         case "share":
            if (texts.length != 3) {
               this.handleText2("Invalid argument count! Usage: waypoint share name user/all");
               return;
            }

            boolean flag = texts[2].equalsIgnoreCase("all");
            this.phobosClient
               .getWaypointManager()
               .resolveOptional2(texts[1])
               .ifPresentOrElse(
                  waypointx -> {
                     this.phobosClient
                        .getPacketBus()
                        .handleNetworkPacket2(
                           new WaypointPacket(
                              flag ? null : texts[2],
                              waypointx.getName(),
                              waypointx.getWaypointDimension().getName(),
                              waypointx.getDoubleValue22(),
                              waypointx.getDoubleValue42(),
                              waypointx.getDoubleValue3()
                           )
                        );
                     this.handleText6("Shared %s with %s".formatted(waypointx.getName(), flag ? "everyone" : texts[2]));
                  },
                  () -> this.handleText2("Waypoint %s not found".formatted(texts[1]))
               );
      }
   }

   public WaypointCommand(PhobosClient phobosClient) {
      super(phobosClient, "waypoint", "action", "name", "dimension", "x", "y", "z");
   }
}
