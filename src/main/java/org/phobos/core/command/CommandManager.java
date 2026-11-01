package org.phobos.core.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.phobos.core.command.ClipCommand.PositionAxis;
import org.phobos.event.Listener;
import org.phobos.event.SendChatMessageEvent;
import org.phobos.module.client.IRCModule;
import org.phobos.module.client.NotificationsModule;
import org.phobos.module.client.PhobosModule;
import org.phobos.core.net.packet.ConsoleMessagePacket;
import org.phobos.core.Feature;
import org.phobos.core.system.OsDetector;
import org.phobos.core.PhobosClient;
import org.phobos.core.user.UserSession;

public class CommandManager extends Feature {
   public final LinkedHashSet<Command> commandLinkedHashSet = new LinkedHashSet<>();
   public final PhobosClient phobosClient;
   public final CommandCompletion commandCompletion = new CommandCompletion();
   private static Object[] phobos$g1806ca778;
   private static Object[] phobos$g1806ca860;
   private static Object[] phobos$g1806ca908;

   public void handleText(String text) {
      String[] var2 = text.split(" ");
      int var3 = var2.length;
      Object var4;
      if (var3 <= 0) {
         var4 = text;
      } else {
         Object var10 = var2[0];
         var4 = var10;
      }

      LinkedHashSet var5 = this.commandLinkedHashSet;
      Iterator var6 = var5.iterator();

      while (true) {
         boolean var7 = var6.hasNext();
         String var19;
         if (var7) {
            Object var8 = var6.next();
            boolean var9 = var8 instanceof Command;
            if (var8 != null && !var9) {
               throw new ClassCastException("CHECKCAST failed");
            }

            String var20 = ((Command)var8).getName();
            boolean var21 = var20.equalsIgnoreCase((String)var4);
            if (!var21) {
               continue;
            }

            int var22 = var2.length;
            if (var22 != 0) {
               int var23 = var2.length;
               Object[] var24 = Arrays.copyOfRange(var2, 1, var23);
               boolean var25 = var24 instanceof String[];
               if (var24 != null && !var25) {
                  throw new ClassCastException("CHECKCAST failed");
               }

               boolean var35 = ((Command)var8).testString2((String[])var24, text);
               if (var35) {
                  break;
               }

               String var36 = ((Command)var8).describe();
               String var44 = "Usage: " + var36;
               var19 = var44;
            } else {
               String var26 = ((Command)var8).describe();
               String var34 = "Usage: " + var26;
               var19 = var34;
            }
         } else {
            String var18 = "Unknown command: " + (String)var4;
            var19 = var18;
         }

         this.handleText2(var19);
         break;
      }
   }

   public CommandManager(PhobosClient phobosClient) {
      this.phobosClient = phobosClient;
      this.run5();
      phobosClient.getPacketBus().handleClassValue(ConsoleMessagePacket.class, consoleMessagePacket -> this.handleText(consoleMessagePacket.getText()));
      this.handleListener(new Listener<SendChatMessageEvent>() {
         public void handleObject2(SendChatMessageEvent sendChatMessageEvent) {
            String text = sendChatMessageEvent.getMessage();
            if (!sendChatMessageEvent.isCancelled()) {
               if (ChatPrefix.getINSTANCE().testText(text) && !IRCModule.testText(text)) {
                  CommandManager.this.handleText(text.substring(1));
                  sendChatMessageEvent.setCancelled(true);
                  CommandManager.this.commandCompletion.clear();
               }
            }
         }
      });
   }

   public void handleText3(String text) {
      if (PhobosModule.getPhobosModule().getNCS().getValue()) {
         this.commandCompletion.clear();
      } else if (!ChatPrefix.getINSTANCE().testText(text)) {
         this.commandCompletion.clear();
      } else {
         String text5 = text.substring(1);
         String text6 = text5.toLowerCase();
         if (text6.isEmpty()) {
            this.commandCompletion.clear();
         } else if (!text6.contains(" ")) {
            for (Command command : this.commandLinkedHashSet) {
               if (command.getName().toLowerCase().startsWith(text6)) {
                  this.commandCompletion.handleText(this.commandCompletion.describeText(text, "+%s".formatted(command.getName())), null);
                  return;
               }
            }

            this.commandCompletion.clear();
         } else {
            String[] texts3 = text6.split(" ");
            if (texts3.length < 2) {
               this.commandCompletion.clear();
            } else {
               String[] texts4 = text.split(" ");
               int i2 = texts3.length - 1;
               String text7 = texts3[0];
               String text8 = texts3[i2];

               for (Command command2 : this.commandLinkedHashSet) {
                  if (command2.getName().equalsIgnoreCase(text7) && command2.getTextArray().length != 0 && i2 <= command2.getTextArray().length) {
                     String text2 = command2.getTextArray()[i2 - 1];
                     if (i2 <= command2.getTextArray().length + 1) {
                        String[] texts = Arrays.copyOfRange(texts3, 1, i2);
                        Suggestion suggestion = command2.resolveSuggestion(text2, text8, texts);
                        if (suggestion != null) {
                           StringBuilder stringBuilder = new StringBuilder();
                           String[] texts2 = text5.split(" ");
                           stringBuilder.append("+").append(texts2[0]).append(" ");

                           for (int i = 0; i < i2 - 1; i++) {
                              stringBuilder.append(texts2[i + 1]).append(" ");
                           }

                           String text3 = suggestion.getText();
                           String text4 = suggestion.isFlag() ? text3 : this.commandCompletion.describeText(texts4[i2], text3);
                           stringBuilder.append(text4);
                           this.commandCompletion.handleText(stringBuilder.toString(), text4);
                           return;
                        }
                     }
                  }
               }

               this.commandCompletion.clear();
            }
         }
      }
   }

   public void handleText7(Text text) {
      NotificationsModule.getNotificationsModule().handleTriConsumer((color, color2, color3) -> NotificationsModule.getNotificationsModule().handleText5(text));
   }

   public CommandCompletion getCommandCompletion() {
      return this.commandCompletion;
   }

   public void run5() {
      PhobosClient var2 = this.phobosClient;
      ResetCommand var1 = new ResetCommand(var2);
      this.handleCommand(var1);
      PhobosClient var4 = this.phobosClient;
      FriendCommand var3 = new FriendCommand(var4);
      this.handleCommand(var3);
      PhobosClient var6 = this.phobosClient;
      EnemyCommand var5 = new EnemyCommand(var6);
      this.handleCommand(var5);
      PhobosClient var8 = this.phobosClient;
      HelpCommand var7 = new HelpCommand(var8);
      this.handleCommand(var7);
      PhobosClient var10 = this.phobosClient;
      BindCommand var9 = new BindCommand(var10);
      this.handleCommand(var9);
      PhobosClient var12 = this.phobosClient;
      ConfigCommand var11 = new ConfigCommand(var12);
      this.handleCommand(var11);
      PhobosClient var14 = this.phobosClient;
      IrcCommand var13 = new IrcCommand(var14);
      this.handleCommand(var13);
      PhobosClient var16 = this.phobosClient;
      ProfileCommand var15 = new ProfileCommand(var16);
      this.handleCommand(var15);
      PhobosClient var18 = this.phobosClient;
      SkinCommand var17 = new SkinCommand(var18);
      this.handleCommand(var17);
      PhobosClient var20 = this.phobosClient;
      CapeCommand var19 = new CapeCommand(var20);
      this.handleCommand(var19);
      PhobosClient var22 = this.phobosClient;
      ToggleCommand var21 = new ToggleCommand(var22);
      this.handleCommand(var21);
      PhobosClient var24 = this.phobosClient;
      DumpCommand var23 = new DumpCommand(var24);
      this.handleCommand(var23);
      PhobosClient var26 = this.phobosClient;
      WaypointCommand var25 = new WaypointCommand(var26);
      this.handleCommand(var25);
      PhobosClient var28 = this.phobosClient;
      MacroCommand var27 = new MacroCommand(var28);
      this.handleCommand(var27);
      PhobosClient var30 = this.phobosClient;
      ProxyCommand var29 = new ProxyCommand(var30);
      this.handleCommand(var29);
      PhobosClient var32 = this.phobosClient;
      FakePlayerCommand var31 = new FakePlayerCommand(var32);
      this.handleCommand(var31);
      PhobosClient var34 = this.phobosClient;
      KitCommand var33 = new KitCommand(var34);
      this.handleCommand(var33);
      PhobosClient var36 = this.phobosClient;
      FolderCommand var35 = new FolderCommand(var36);
      this.handleCommand(var35);
      PhobosClient var38 = this.phobosClient;
      ShowCommand var37 = new ShowCommand(var38);
      this.handleCommand(var37);
      PhobosClient var40 = this.phobosClient;
      RenameCommand var39 = new RenameCommand(var40);
      this.handleCommand(var39);
      PhobosClient var42 = this.phobosClient;
      PrefixCommand var41 = new PrefixCommand(var42);
      this.handleCommand(var41);
      PhobosClient var44 = this.phobosClient;
      ExportCommand var43 = new ExportCommand(var44);
      this.handleCommand(var43);
      PhobosClient var46 = this.phobosClient;
      PositionAxis var47 = PositionAxis.Vertical;
      ClipCommand var45 = new ClipCommand(var46, "vclip", var47);
      this.handleCommand(var45);
      PhobosClient var49 = this.phobosClient;
      PositionAxis var50 = PositionAxis.Horizontal;
      ClipCommand var48 = new ClipCommand(var49, "hclip", var50);
      this.handleCommand(var48);
      PhobosClient var52 = this.phobosClient;
      HudCommand var51 = new HudCommand(var52);
      this.handleCommand(var51);
      boolean var53 = UserSession.flag2;
      if (var53) {
         PhobosClient var55 = this.phobosClient;
         AdminCommand var54 = new AdminCommand(var55);
         this.handleCommand(var54);
         PhobosClient var57 = this.phobosClient;
         SkyboxCommand var56 = new SkyboxCommand(var57);
         this.handleCommand(var56);
         PhobosClient var59 = this.phobosClient;
         MakeModuleCommand var58 = new MakeModuleCommand(var59);
         this.handleCommand(var58);
      }

      OsDetector var60 = OsDetector.getINSTANCE();
      boolean var61 = var60.check();
      if (var61) {
         PhobosClient var63 = this.phobosClient;
         SpotifyCommand var62 = new SpotifyCommand(var63);
         this.handleCommand(var62);
      }
   }

   public LinkedHashSet<Command> getCommandLinkedHashSet() {
      return this.commandLinkedHashSet;
   }

   public void handleCommand(Command command) {
      LinkedHashSet var2 = this.commandLinkedHashSet;
      var2.add(command);
   }

   public void handleText2(String text) {
      NotificationsModule.getNotificationsModule()
         .handleTriConsumer((color, color2, color3) -> NotificationsModule.getNotificationsModule().handleText5(Text.literal(text).formatted(Formatting.RED)));
   }

   public void handleText6(String text) {
      NotificationsModule.getNotificationsModule()
         .handleTriConsumer(
            (color, color2, color3) -> NotificationsModule.getNotificationsModule().handleText5(Text.literal(text).withColor(color2.hashCode()))
         );
   }

   public static class CommandLoader extends Feature {
      public static final CommandManager.CommandLoader INSTANCE = new CommandManager.CommandLoader();
      public final List<Runnable> runnableList = new ArrayList<>();

      public void handleRunnable(Runnable runnable) {
         this.runnableList.add(runnable);
      }

      public CommandLoader() {
         this.handleListener(new Listener<PhobosClient.InitEvent>() {
            public void handleObject2(PhobosClient.InitEvent initEvent) {
               CommandLoader.this.runnableList.forEach(Runnable::run);
            }
         });
      }

      public static CommandManager.CommandLoader getINSTANCE() {
         return INSTANCE;
      }
   }
}
