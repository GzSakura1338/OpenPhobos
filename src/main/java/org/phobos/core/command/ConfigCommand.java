package org.phobos.core.command;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import org.jetbrains.annotations.Nullable;
import org.phobos.type.BINDSMODULESEnum;
import org.phobos.core.chat.ChatManager;
import org.phobos.core.chat.ChatTextUtils;
import org.phobos.core.config.ConfigManager;
import org.phobos.core.PhobosClient;

public class ConfigCommand extends SyntaxCommand {
   public static final CommandSyntax commandSyntax = new CommandSyntax("save", "load", "delete", "update", "reset", "list");
   public static final CommandSyntax commandSyntax2 = new CommandSyntax("binds", "hud", "settings", "friends", "enemies", "colors", "visuals");
   private static Object[] phobos$g1806c05b8;
   private static Object[] phobos$g1806c06f0;
   private static Object[] phobos$g1806c0838;
   private static Object[] phobos$g1806c0950;
   private static Object[] phobos$g1806c09f8;
   private static Object[] phobos$g1806c0b10;

   @Override
   public boolean testString(String[] texts) {
      return texts.length >= 1;
   }

   public ConfigCommand(PhobosClient phobosClient) {
      super(phobosClient, "config", "action", "name", "flags");
   }

   @Override
   public Suggestion resolveSuggestion(String text, String text2, String[] texts) {
      switch (text) {
         case "action":
            return commandSyntax.resolveSuggestion(text2);
         case "name":
            List<String> list = this.phobosClient.getConfigManager().getList();
            String text3 = CommandCompletion.describeText3(text2, list);
            if (text3 != null) {
               return Suggestion.createSuggestion(text3);
            }
            break;
         case "flags":
            String[] texts2 = text2.split("&");
            if (texts2.length < 2) {
               return commandSyntax2.resolveSuggestion(text2);
            }

            String text4 = texts2[texts2.length - 1];
            Suggestion suggestion = commandSyntax2.resolveSuggestion(text4);
            if (suggestion != null) {
               StringBuilder stringBuilder = new StringBuilder();

               for (int i = 0; i < texts2.length - 1; i++) {
                  stringBuilder.append(texts2[i]).append("&");
               }

               stringBuilder.append(suggestion.getText());
               return Suggestion.createSuggestion3(stringBuilder.toString());
            }
      }

      return null;
   }

   public BINDSMODULESEnum[] values(String[] texts) {
      if (texts.length != 3) {
         return new BINDSMODULESEnum[0];
      }

      String[] texts2 = texts[2].split("&");
      if (texts2.length == 0) {
         return new BINDSMODULESEnum[0];
      }

      BINDSMODULESEnum[] bINDSMODULESEnums = new BINDSMODULESEnum[texts2.length];
      int i = 0;

      for (String text : texts2) {
         BINDSMODULESEnum bINDSMODULESEnum = this.resolveBINDSMODULESEnum(text);
         if (bINDSMODULESEnum != null) {
            bINDSMODULESEnums[i++] = bINDSMODULESEnum;
         }
      }

      return bINDSMODULESEnums;
   }

   public void handleText(String text, ConfigCommand.Action action, BINDSMODULESEnum[] bINDSMODULESEnums) {
      int var4 = action.ordinal();
      if (Integer.compareUnsigned(var4, 5) <= 0) {
         ChatManager var70;
         String var71;
         Runnable var72;
         switch (var4) {
            case 0:
            default:
               PhobosClient var5 = this.phobosClient;
               ConfigManager var6 = var5.getConfigManager();
               CompletableFuture var7 = var6.resolveCompletableFuture(text);
               ConfigCommand var125 = this;
               String cap76 = text;
               ConfigCommand cap75 = var125;
               BiConsumer<ConfigManager.Result, Throwable> var24 = (var2, var3) -> cap75.handleText5(cap76, var2, var3);
               CompletableFuture var25 = var7.whenComplete(var24);
               TimeUnit var26 = TimeUnit.SECONDS;
               var25.completeOnTimeout(null, 5L, var26);
               return;
            case 1:
               PhobosClient var27 = this.phobosClient;
               ConfigManager var28 = var27.getConfigManager();
               CompletableFuture var29 = var28.resolveCompletableFuture2(text, bINDSMODULESEnums);
               ConfigCommand var124 = this;
               String var10001 = text;
               BINDSMODULESEnum[] cap79 = bINDSMODULESEnums;
               String cap78 = var10001;
               ConfigCommand cap77 = var124;
               BiConsumer<ConfigManager.Result, Throwable> var46 = (var3, var4x) -> cap77.handleText3(cap78, cap79, var3, var4x);
               CompletableFuture var47 = var29.whenComplete(var46);
               TimeUnit var48 = TimeUnit.SECONDS;
               var47.completeOnTimeout(null, 5L, var48);
               return;
            case 2:
               PhobosClient var49 = this.phobosClient;
               ChatManager var50 = var49.getChatManager();
               Object[] var51 = new Object[1];
               var51[0] = text;
               String var52 = "Are you sure you want to delete %s?".formatted(var51);
               ConfigCommand var10000 = this;
               String cap81 = text;
               ConfigCommand cap80 = var10000;
               Runnable var69 = () -> cap80.handleText4(cap81);
               var70 = var50;
               var71 = var52;
               var72 = var69;
               break;
            case 3:
               return;
            case 4:
               int[] var73 = new int[ConfigManager.Result.values().length];
               var73[ConfigManager.Result.SUCCESS.ordinal()] = 1;
               var73[ConfigManager.Result.NOT_FOUND.ordinal()] = 2;
               var73[ConfigManager.Result.ERROR_UNKNOWN.ordinal()] = 3;
               var73[ConfigManager.Result.ALREADY_EXISTS.ordinal()] = 4;
               PhobosClient var74 = this.phobosClient;
               ConfigManager var75 = var74.getConfigManager();
               ConfigManager.Result var76 = var75.resolveResult(text);
               int var77 = var76.ordinal();
               int var78 = var73[var77];
               if (var78 + -1 != 0) {
                  String var87;
                  if (var78 + -1 + -1 != 0) {
                     if (var78 + -1 + -1 != 1) {
                        return;
                     }

                     String var86 = "Unknown error while updating " + text + ".json";
                     var87 = var86;
                  } else {
                     String var116 = "Unable to find " + text + ".json";
                     var87 = var116;
                  }

                  this.handleText2(var87);
               } else {
                  ConfigCommand.Action var107 = ConfigCommand.Action.Save;
                  BINDSMODULESEnum[] var108 = new BINDSMODULESEnum[0];
                  this.handleText(text, var107, var108);
               }

               return;
            case 5:
               PhobosClient var88 = this.phobosClient;
               ChatManager var89 = var88.getChatManager();
               Runnable var106 = this::run;
               var70 = var89;
               var71 = "Are you sure you want to reset?";
               var72 = var106;
         }

         var70.handleText7(var71, var72);
      }
   }

   @Override
   public void handleString(String[] texts, String text) {
      if (texts.length <= 1) {
         switch (texts[0].toLowerCase()) {
            case "reset":
               this.handleText(null, ConfigCommand.Action.Reset, new BINDSMODULESEnum[0]);
               return;
            case "list":
               StringBuilder stringBuilder = new StringBuilder();
               stringBuilder.append("Configs: ");

               for (File file : Objects.requireNonNull(this.phobosClient.getConfigManager().getFile().listFiles())) {
                  stringBuilder.append(file.getName()).append(", ");
               }

               stringBuilder.setLength(stringBuilder.length() - 2);
               this.handleText6(stringBuilder.toString());
            default:
               this.handleText2("Received unknown input, allowed keywords: save, load, delete, update, reset");
         }
      } else {
         ConfigCommand.Action action = switch (texts[0].toLowerCase()) {
            case "save" -> ConfigCommand.Action.Save;
            case "load" -> ConfigCommand.Action.Load;
            case "delete" -> ConfigCommand.Action.Delete;
            case "update" -> ConfigCommand.Action.Update;
            case "reset" -> ConfigCommand.Action.Reset;
            default -> null;
         };
         if (action == null) {
            this.handleText6("Received unknown input, allowed keywords: save, load, delete, update, reset");
         } else {
            this.handleText(texts[1], action, this.values(texts));
         }
      }
   }

   @Nullable
   public BINDSMODULESEnum resolveBINDSMODULESEnum(String text) {
      long var6;
      label66: {
         byte var5;
         label65: {
            String var2 = text.toLowerCase();
            int var3 = var2.hashCode();
            if (var3 <= 93742038) {
               if (var3 != 93742038) {
                  if (var3 != -1605575130) {
                     if (var3 != -1354842768) {
                        if (var3 != -600094315) {
                           if (var3 == 103671) {
                              boolean var4 = var2.equals("hud");
                              if (var4) {
                                 var5 = 1;
                                 break label65;
                              }
                           }
                        } else {
                           boolean var12 = var2.equals("friends");
                           if (var12) {
                              var5 = 4;
                              break label65;
                           }
                        }
                     } else {
                        boolean var11 = var2.equals("colors");
                        if (var11) {
                           var5 = 6;
                           break label65;
                        }
                     }
                  } else {
                     boolean var10 = var2.equals("enemies");
                     if (var10) {
                        var5 = 5;
                        break label65;
                     }
                  }
               } else {
                  boolean var9 = var2.equals("binds");
                  if (var9) {
                     var6 = 0L;
                     break label66;
                  }
               }
            } else if (var3 != 467099955) {
               if (var3 != 1227433863) {
                  if (var3 == 1434631203) {
                     boolean var8 = var2.equals("settings");
                     if (var8) {
                        var5 = 2;
                        break label65;
                     }
                  }
               } else {
                  boolean var23 = var2.equals("modules");
                  if (var23) {
                     var5 = 3;
                     break label65;
                  }
               }
            } else {
               boolean var22 = var2.equals("visuals");
               if (var22) {
                  var5 = 7;
                  break label65;
               }
            }

            var6 = 4294967295L;
            break label66;
         }

         var6 = var5 & 4294967295L;
      }

      BINDSMODULESEnum var13;
      if (Integer.compareUnsigned((int)((int)var6 & 4294967295L), 7) <= 0) {
         switch ((int)((int)var6 & 4294967295L)) {
            case 0:
            default:
               BINDSMODULESEnum var14 = BINDSMODULESEnum.BINDS;
               var13 = var14;
               break;
            case 1:
               BINDSMODULESEnum var15 = BINDSMODULESEnum.HUD;
               var13 = var15;
               break;
            case 2:
               BINDSMODULESEnum var16 = BINDSMODULESEnum.SETTINGS;
               var13 = var16;
               break;
            case 3:
               BINDSMODULESEnum var17 = BINDSMODULESEnum.MODULES;
               var13 = var17;
               break;
            case 4:
               BINDSMODULESEnum var18 = BINDSMODULESEnum.FRIENDS;
               var13 = var18;
               break;
            case 5:
               BINDSMODULESEnum var19 = BINDSMODULESEnum.ENEMIES;
               var13 = var19;
               break;
            case 6:
               BINDSMODULESEnum var20 = BINDSMODULESEnum.COLORS;
               var13 = var20;
               break;
            case 7:
               BINDSMODULESEnum var21 = BINDSMODULESEnum.VISUALS;
               var13 = var21;
         }
      } else {
         var13 = null;
      }

      return var13;
   }

   public void handleText3(String text, BINDSMODULESEnum[] bINDSMODULESEnums, ConfigManager.Result result, Throwable throwable) {
      if (throwable != null) {
         this.handleText2("Unknown error while loading " + text + ".json, please check console");
         throwable.printStackTrace(System.err);
      } else if (result == null) {
         this.handleText2("Could not load " + text + ".json in time (Took > 5s)");
      } else {
         switch (result) {
            case NOT_FOUND:
               if (bINDSMODULESEnums.length == 0) {
                  this.handleText6("Loaded " + text + ".json");
                  return;
               }

               StringBuilder stringBuilder = new StringBuilder();

               for (BINDSMODULESEnum bINDSMODULESEnum : bINDSMODULESEnums) {
                  stringBuilder.append(ChatTextUtils.describeText(bINDSMODULESEnum.name())).append(", ");
               }

               stringBuilder.setLength(stringBuilder.length() - 2);
               this.handleText6("Loaded " + stringBuilder + " from " + text + ".json");
               break;
            case SUCCESS:
               this.handleText2("Unable to find " + text + ".json");
               break;
            case ALREADY_EXISTS:
               this.handleText2("Unknown error while loading " + text + ".json");
         }
      }
   }

   public void handleText4(String text) {
      switch (this.phobosClient.getConfigManager().resolveResult(text)) {
         case NOT_FOUND:
            this.handleText6("Deleted " + text + ".json");
            this.phobosClient.getConfigManager().run();
            break;
         case SUCCESS:
            this.handleText2("Unable to find " + text + ".json");
            break;
         case ALREADY_EXISTS:
            this.handleText2("Unknown error while deleting " + text + ".json");
      }
   }

   public void handleText5(String text, ConfigManager.Result result, Throwable throwable) {
      if (throwable != null) {
         this.handleText2("Unknown error while saving " + text + ".json, please check console");
         throwable.printStackTrace(System.err);
      } else if (result == null) {
         this.handleText2("Could not save " + text + ".json in time (Took > 5s)");
      } else {
         switch (result) {
            case NOT_FOUND:
               this.handleText6("Saved " + text + ".json");
               this.phobosClient.getConfigManager().run();
            case SUCCESS:
            default:
               break;
            case ALREADY_EXISTS:
               this.handleText2("Unknown error while saving " + text + ".json");
               break;
            case ERROR_UNKNOWN:
               this.handleText2("Unable to overwrite " + text + ".json");
         }
      }
   }

   public void run() {
      this.phobosClient.getConfigManager().run2();
      this.handleText6("Successfully reset current configuration.");
   }

   private enum Action {
      Save,
      Load,
      Delete,
      List,
      Update,
      Reset;
   }
}
