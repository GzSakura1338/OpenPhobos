package org.phobos.core.config;

import lombok.SneakyThrows;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles.Lookup;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import net.fabricmc.loader.api.FabricLoader;
import org.earthhack.mixin.EventSubscriber;
import org.phobos.api.Named;
import org.phobos.api.JsonSupport;
import org.phobos.api.JsonSerializable;
import org.phobos.api.ConfigInfo;
import org.phobos.module.Module;
import org.phobos.type.BINDSMODULESEnum;
import org.phobos.type.ConfigScriptEnum;
import org.phobos.type.PUBLICPRIVATEEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.phobos.core.ClientGuard;
import org.phobos.core.ClientHolder;
import org.phobos.core.gui.screen.ConfigEntryElement;
import org.phobos.core.gui.screen.ConfigScreen;
import org.phobos.core.social.EnemyList;
import org.phobos.core.Feature;
import org.phobos.core.social.FriendList;
import org.phobos.core.hud.HudElement;
import org.phobos.core.hud.HudManager;
import org.phobos.core.bind.MacroManager;
import org.phobos.core.gui.MenuNavigation;
import org.phobos.core.module.ModuleManager;
import org.phobos.core.PhobosClient;
import org.phobos.core.PhobosLog;
import org.phobos.core.system.Profiler;
import org.phobos.core.user.UserProfile;
import org.phobos.core.user.UserSession;

public class ConfigManager extends Feature implements JsonSupport, EventSubscriber {
   public final Map<String, String> textMap = new HashMap<>();
   public static final ProgressState progressState = new ProgressState();
   public static final Path path = FabricLoader.getInstance().getGameDir().resolve("phoboslite");
   public static final Path path2 = Path.of(System.getProperty("user.home"), "earthhack", "configs");
   public static final ConfigSectionFilter configSectionFilter = new ConfigSectionFilter();
   public final PhobosClient phobosClient;
   public static final ProgressState progressState2 = new ProgressState();
   public static ConfigManager configManager;
   public static final CloudSettings cloudSettings = new CloudSettings();
   public final List<Object> objectList = new ArrayList<>();
   public static final Logger LOGGER = LoggerFactory.getLogger(ConfigManager.class);
   public static boolean flag = false;
   public static boolean flag2 = false;
   private static Object[] phobos$g1806cc028;
   private static Object[] phobos$g1806cc160;
   private static Object[] phobos$g1806cbc20;
   private static Object[] phobos$g1806cb730;
   private static Object[] phobos$g1806cb848;
   private static Object[] phobos$g1806cc528;
   private static Object[] phobos$g1806cc7b0;
   private static Object[] phobos$g1806cc8d8;
   private static Object[] phobos$g1806ccca8;

   public CompletableFuture<ConfigManager.Result> resolveCompletableFuture(String text) {
      return PhobosClient.resolveCompletableFuture(() -> {
         if (this.resolveFile(text) != null) {
            return ConfigManager.Result.ALREADY_EXISTS;
         }

         JsonObject jsonObject = new JsonObject();
         flag2 = true;
         ConfigJsonUtils.handleJsonObject(jsonObject);

         for (Object object : this.objectList) {
            if (object instanceof Named) {
               Named named = (Named)object;
               if (named instanceof JsonSerializable) {
                  JsonSerializable jsonSerializable = (JsonSerializable)named;
                  jsonObject.add(this.describeNamed(named), jsonSerializable.createJsonPrimitive());
               }
            }
         }

         flag2 = false;
         return this.resolveResult2(text, gson.toJson(jsonObject));
      });
   }

   @SneakyThrows public static void stop() {
      Path var0 = path;
      Path var1 = var0.resolve("config.json");

      Throwable var8;
      label88: {
         File var2;
         try {
            var2 = var1.toFile();
         } catch (Throwable var24) {
            var8 = var24;
            break label88;
         }

         boolean var3;
         try {
            var3 = var2.exists();
         } catch (Throwable var23) {
            var8 = var23;
            break label88;
         }

         if (!var3) {
            FileAttribute[] var4 = new FileAttribute[0];

            try {
               Files.createFile(var1, var4);
            } catch (Throwable var22) {
               var8 = var22;
               break label88;
            }
         }

         CloudSettings var5 = cloudSettings;

         JsonElement var6;
         try {
            var6 = var5.getJsonElement();
         } catch (Throwable var21) {
            var8 = var21;
            break label88;
         }

         label69:
         if (var6 == null) {
            IllegalStateException var7;
            try {
               var7 = new IllegalStateException("Serialization failed");
            } catch (Throwable var16) {
               var8 = var16;
               break label69;
            }

            try {
               throw var7;
            } catch (Throwable var15) {
               var8 = var15;
            }
         } else {
            label89: {
               Gson var11 = JsonSupport.gson;

               String var12;
               try {
                  var12 = var11.toJson(var6);
               } catch (Throwable var20) {
                  var8 = var20;
                  break label89;
               }

               label64:
               if (var12 == null) {
                  IllegalStateException var13;
                  try {
                     var13 = new IllegalStateException("Content is null");
                  } catch (Throwable var18) {
                     var8 = var18;
                     break label64;
                  }

                  try {
                     throw var13;
                  } catch (Throwable var17) {
                     var8 = var17;
                  }
               } else {
                  OpenOption[] var14 = new OpenOption[0];

                  try {
                     Files.writeString(var1, var12, var14);
                     return;
                  } catch (Throwable var19) {
                     var8 = var19;
                  }
               }
            }
         }
      }

      boolean var9 = true;
      if (var9) {
         IllegalStateException var10 = new IllegalStateException("stop failed");
         throw var10;
      } else {
         throw var8;
      }
   }

   public boolean testConfigInfo(ConfigInfo configInfo) {
      String text = this.textMap.get(configInfo.getName());
      return text == null ? false : text.equals(UserSession.userSession.getText());
   }

   public File resolveFile(String text) {
      File[] files = this.getFile().listFiles();
      if (files == null) {
         return null;
      }

      for (File file : files) {
         String[] texts = file.getName().split("\\.");
         String text2 = texts.length > 0 ? texts[texts.length - 2] : file.getName();
         if (text2.equalsIgnoreCase(text)) {
            return file;
         }
      }

      return null;
   }

   public void handleText(String text, String text2) {
      flag = true;
      PhobosClient.resolveCompletableFuture2(() -> {
         try {
            JsonElement jsonElement = JsonParser.parseString(text2);
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            for (Entry<String, JsonElement> entry : jsonObject.entrySet()) {
               for (Object object : this.objectList) {
                  if (object instanceof Named) {
                     Named named = (Named)object;
                     if (named instanceof JsonSerializable) {
                        JsonSerializable jsonSerializable = (JsonSerializable)named;
                        String text3 = named.getName().split("\\.")[0];
                        if (text3.equalsIgnoreCase(entry.getKey())) {
                           try {
                              jsonSerializable.handleJsonElement(entry.getValue());
                           } catch (Throwable throwable) {
                              this.phobosClient.getChatManager().handleText("Could not load %s from %s".formatted(text3, text));
                           }
                           break;
                        }
                     }
                  }
               }
            }
         } catch (Throwable throwable2) {
            this.phobosClient.getChatManager().handleText("Could not load config, error: " + throwable2.getMessage());
         }
      }).orTimeout(5L, TimeUnit.SECONDS).thenRun(() -> {
         this.invoke(new NamedHandle(text));
         flag = false;
      });
   }

   public boolean testConfigInfo2(ConfigInfo configInfo) {
      return this.textMap.containsKey(configInfo.getName());
   }

   public ConfigManager(PhobosClient phobosClient) {
      this.phobosClient = phobosClient;
      this.run5();
      configManager = this;
   }

   @SneakyThrows public static void handlePhobosClient(PhobosClient phobosClient) {
      Path var1 = path;
      Path var2 = var1.resolve("config.json");
      File var3 = var2.toFile();
      boolean var4 = var3.exists();
      Throwable var15;
      if (var4) {
         Throwable var13;
         label85: {
            label72: {
               String var5;
               try {
                  var5 = Files.readString(var2);
               } catch (Throwable var20) {
                  var13 = var20;
                  boolean var14 = true;
                  if (!var14) {
                     break label85;
                  }
                  break label72;
               }

               JsonElement var6;
               try {
                  var6 = JsonParser.parseString(var5);
               } catch (Throwable var19) {
                  var13 = var19;
                  boolean var24 = true;
                  if (!var24) {
                     break label85;
                  }
                  break label72;
               }

               CloudSettings var7 = cloudSettings;

               try {
                  var7.handleJsonElement(var6);
                  return;
               } catch (Throwable var18) {
                  var13 = var18;
                  boolean var25 = true;
                  if (!var25) {
                     break label85;
                  }
               }
            }

            phobosClient.handleText5("ConfigManager", "Failed to load data.");
            return;
         }

         var15 = var13;
      } else {
         CloudSettings var8 = cloudSettings;

         Throwable var16;
         label74: {
            JsonElement var9;
            try {
               var9 = var8.getJsonElement();
            } catch (Throwable var23) {
               var16 = var23;
               boolean var17 = var23 instanceof IOException;
               if (var17) {
                  return;
               }
               break label74;
            }

            Gson var10 = JsonSupport.gson;

            String var11;
            try {
               var11 = var10.toJson(var9);
            } catch (Throwable var22) {
               var16 = var22;
               boolean var26 = var22 instanceof IOException;
               if (var26) {
                  return;
               }
               break label74;
            }

            OpenOption[] var12 = new OpenOption[0];

            try {
               Files.writeString(var2, var11, var12);
               return;
            } catch (Throwable var21) {
               var16 = var21;
               boolean var27 = var21 instanceof IOException;
               if (var27) {
                  return;
               }
            }
         }

         var15 = var16;
      }

      throw var15;
   }

   public void run2() {
      PhobosClient var1 = this.phobosClient;
      ModuleManager var2 = var1.getModuleManager();
      List var3 = var2.getList();
      Consumer<Module> var20 = ConfigManager::handleModule;
      var3.forEach(var20);
      PhobosClient var21 = this.phobosClient;
      HudManager var22 = var21.getHudManager();
      List var23 = var22.getColorPickerList();
      Consumer<HudElement> var40 = ConfigManager::handleHudElement;
      var23.forEach(var40);
   }

   @SneakyThrows public void run3() {
      this.run7();
      Path var1 = path;
      Path var2 = var1.resolve("current.json");
      File var3 = var2.toFile();
      boolean var4 = var3.exists();
      if (var4) {
         flag = true & true;
         Profiler.handleText2("Loading");

         Throwable var14;
         label151: {
            String var5;
            try {
               var5 = Files.readString(var2);
            } catch (Throwable var50) {
               var14 = var50;
               break label151;
            }

            JsonElement var6;
            try {
               var6 = JsonParser.parseString(var5);
            } catch (Throwable var49) {
               var14 = var49;
               break label151;
            }

            JsonObject var7;
            try {
               var7 = var6.getAsJsonObject();
            } catch (Throwable var48) {
               var14 = var48;
               break label151;
            }

            List var8 = this.objectList;

            Iterator var9;
            try {
               var9 = var8.iterator();
            } catch (Throwable var47) {
               var14 = var47;
               break label151;
            }

            while (true) {
               boolean var10;
               try {
                  var10 = var9.hasNext();
               } catch (Throwable var39) {
                  var14 = var39;
                  break;
               }

               if (!var10) {
                  try {
                     Profiler.handleText("Loading");
                  } catch (Throwable var38) {
                     var14 = var38;
                     break;
                  }

                  flag = false & true;
                  return;
               }

               Object var11;
               try {
                  var11 = var9.next();
               } catch (Throwable var46) {
                  var14 = var46;
                  break;
               }

               boolean var12 = var11 instanceof Named;
               if ((int)((!var12 ? 0 : 1) & 0xFF & 0xFF) != 0) {
                  boolean var13 = var11 instanceof Named;
                  if (var11 != null && !var13) {
                     throw new ClassCastException("CHECKCAST failed");
                  }

                  boolean var17 = var11 instanceof JsonSerializable;
                  if ((int)((!var17 ? 0 : 1) & 0xFF & 0xFF) != 0) {
                     boolean var18 = var11 instanceof JsonSerializable;
                     if (var11 != null && !var18) {
                        throw new ClassCastException("CHECKCAST failed");
                     }

                     String var29;
                     try {
                        var29 = this.describeNamed((Named)var11);
                     } catch (Throwable var45) {
                        var14 = var45;
                        break;
                     }

                     JsonElement var30;
                     try {
                        var30 = var7.get(var29);
                     } catch (Throwable var44) {
                        var14 = var44;
                        break;
                     }

                     if (var30 != null) {
                        try {
                           ((JsonSerializable)var11).handleJsonElement(var30);
                        } catch (Throwable var51) {
                           Throwable var31 = var51;
                           boolean var32 = true;
                           if (!var32) {
                              throw var51;
                           }

                           PhobosClient var33 = this.phobosClient;
                           Object[] var34 = new Object[2];

                           String var35;
                           try {
                              var35 = ((Named)var11).getName();
                           } catch (Throwable var43) {
                              var14 = var43;
                              break;
                           }

                           var34[0] = var35;

                           String var36;
                           try {
                              var36 = var31.getMessage();
                           } catch (Throwable var42) {
                              var14 = var42;
                              break;
                           }

                           var34[1] = var36;

                           String var37;
                           try {
                              var37 = "Failed to load %s, error: %s".formatted(var34);
                           } catch (Throwable var41) {
                              var14 = var41;
                              break;
                           }

                           try {
                              var33.handleText5("ConfigLoader", var37);
                           } catch (Throwable var40) {
                              var14 = var40;
                              break;
                           }
                        }
                     }
                  }
               }
            }
         }

         boolean var15 = true;
         if (var15) {
            PhobosClient var19 = this.phobosClient;
            String var20 = var14.getMessage();
            String var28 = "Failed to load, error: " + var20;
            var19.handleText5("ConfigLoader", var28);
         } else {
            Throwable var16 = var14;
            throw var16;
         }
      }
   }

   @SneakyThrows public void handleList(List<ConfigMeta> list) {
      CallSite var33 = null;
      ProgressState var2 = getProgressState();
      boolean var3 = var2.isFlag();
      if (!var3) {
         ConfigScreen var4 = ConfigScreen.configScreen;
         var4.run();
         ProgressState var5 = progressState;
         int var6 = list.size();
         var5.handleInt(var6);
         Map var7 = this.textMap;
         var7.clear();
         HashMap var8 = new HashMap();
         Iterator var9 = list.iterator();

         while (true) {
            boolean var10 = var9.hasNext();
            if (!var10) {
               ArrayList var13 = new ArrayList();
               Iterator var14 = list.iterator();

               while (true) {
                  boolean var15 = var14.hasNext();
                  if (!var15) {
                     CompletableFuture[] var23 = new CompletableFuture[0];
                     Object[] var24 = var13.toArray(var23);
                     boolean var25 = var24 instanceof CompletableFuture[];
                     if (var24 != null && !var25) {
                        throw new ClassCastException("CHECKCAST failed");
                     }

                     CompletableFuture var53 = CompletableFuture.allOf((CompletableFuture<?>[])var24);
                     ConfigManager var79 = this;
                     Map cap15 = var8;
                     ConfigManager cap14 = var79;
                     Runnable var70 = () -> cap14.handleMap(cap15);
                     var53.thenRun(var70);
                     return;
                  }

                  Object var16 = var14.next();
                  boolean var17 = var16 instanceof ConfigMeta;
                  if (var16 != null && !var17) {
                     throw new ClassCastException("CHECKCAST failed");
                  }

                  String var26 = ((ConfigMeta)var16).getName();
                  String var27 = ((ConfigMeta)var16).getText();
                  PUBLICPRIVATEEnum var28 = ((ConfigMeta)var16).getPUBLICPRIVATEEnum();
                  boolean var29 = ((ConfigMeta)var16).isFlag();
                  PhobosClient var30 = this.phobosClient;
                  CloudConfigApi var31 = var30.getCloudConfigApi();
                  ConfigScriptEnum var32 = ConfigScriptEnum.Config;
                  if (var33 == null) {
                     Lookup var34 = MethodHandles.lookup();
                     Class<?> var35 = this.getClass();
                     ClassLoader var36 = var35.getClassLoader();
                     MethodType var37 = MethodType.fromMethodDescriptorString(
                        "(Ljava/lang/String;Ljava/lang/String;ZLorg/phobos/type/PUBLICPRIVATEEnum;Ljava/util/Map;Lorg/phobos/core/config/ConfigMeta;)Ljava/util/function/Consumer;",
                        var36
                     );
                     Class<?> var38 = this.getClass();
                     ClassLoader var39 = var38.getClassLoader();
                     MethodType var40 = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", var39);
                     Class<?> var41 = this.getClass();
                     ClassLoader var42 = var41.getClassLoader();
                     MethodType var43 = MethodType.fromMethodDescriptorString(
                        "(Ljava/lang/String;Ljava/lang/String;ZLorg/phobos/type/PUBLICPRIVATEEnum;Ljava/util/Map;Lorg/phobos/core/config/ConfigMeta;Ljava/lang/String;)V",
                        var42
                     );
                     MethodHandle var44 = var34.findStatic(ConfigManager.class, "handleText2", var43);
                     Class<?> var45 = this.getClass();
                     ClassLoader var46 = var45.getClassLoader();
                     MethodType var47 = MethodType.fromMethodDescriptorString("(Ljava/lang/String;)V", var46);
                     Object[] var10000 = new Object[0];
                     var33 = LambdaMetafactory.metafactory(var34, "accept", var37, var40, var44, var47);
                  }

                  MethodHandle var48 = var33.getTarget();
                  if (var26 != null) {
                     Object[] var73 = phobos$g1806cc028;
                     if (phobos$g1806cc028 == null) {
                        var73 = phobos$g1806cc028 = new Object[6];
                     }

                     var73[0] = var26;
                  }

                  if (var27 != null) {
                     Object[] var74 = phobos$g1806cc028;
                     if (phobos$g1806cc028 == null) {
                        var74 = phobos$g1806cc028 = new Object[6];
                     }

                     var74[1] = var27;
                  }

                  Boolean var49 = var29;
                  Object[] var75 = phobos$g1806cc028;
                  if (phobos$g1806cc028 == null) {
                     var75 = phobos$g1806cc028 = new Object[6];
                  }

                  var75[2] = var49;
                  if (var28 != null) {
                     var75 = phobos$g1806cc028;
                     if (phobos$g1806cc028 == null) {
                        var75 = phobos$g1806cc028 = new Object[6];
                     }

                     var75[3] = var28;
                  }

                  var75 = phobos$g1806cc028;
                  if (phobos$g1806cc028 == null) {
                     var75 = phobos$g1806cc028 = new Object[6];
                  }

                  var75[4] = var8;
                  if (var16 != null) {
                     var75 = phobos$g1806cc028;
                     if (phobos$g1806cc028 == null) {
                        var75 = phobos$g1806cc028 = new Object[6];
                     }

                     var75[5] = var16;
                  }

                  Object[] var10001 = phobos$g1806cc028;
                  if (phobos$g1806cc028 == null) {
                     var10001 = phobos$g1806cc028 = new Object[6];
                  }

                  Object var50 = var48.invokeWithArguments(var10001);
                  Runnable[] var51 = new Runnable[0];
                  CompletableFuture var52 = var31.resolveCompletableFuture(var26, var32, (Consumer<String>)var50, var51);
                  var13.add(var52);
               }
            }

            Object var11 = var9.next();
            boolean var12 = var11 instanceof ConfigMeta;
            if (var11 != null && !var12) {
               throw new ClassCastException("CHECKCAST failed");
            }

            ConfigScreen var18 = ConfigScreen.configScreen;
            ConfigEntryElement var19 = var18.resolveConfigEntryElement4((ConfigMeta)var11);
            var8.put(var11, var19);
            Map var20 = this.textMap;
            String var21 = ((ConfigMeta)var11).getName();
            String var22 = ((ConfigMeta)var11).getText();
            var20.put(var21, var22);
         }
      }
   }

   public void run5() {
      List var1 = this.objectList;
      PhobosClient var2 = this.phobosClient;
      ModuleManager var3 = var2.getModuleManager();
      var1.add(var3);
      List var4 = this.objectList;
      PhobosClient var5 = this.phobosClient;
      FriendList var6 = var5.getFriendList();
      var4.add(var6);
      List var7 = this.objectList;
      PhobosClient var8 = this.phobosClient;
      EnemyList var9 = var8.getEnemyList();
      var7.add(var9);
      List var10 = this.objectList;
      PhobosClient var11 = this.phobosClient;
      HudManager var12 = var11.getHudManager();
      var10.add(var12);
      List var13 = this.objectList;
      PhobosClient var14 = this.phobosClient;
      MenuNavigation var15 = var14.getMenuNavigation();
      var13.add(var15);
      List var16 = this.objectList;
      PhobosClient var17 = this.phobosClient;
      MacroManager var18 = var17.getMacroManager();
      var16.add(var18);
   }

   @SneakyThrows public void run() {
      CallSite var278 = null;
      long var28 = 0L;
      long var26 = 0L;
      File var1 = this.getFile();
      if (var1 != null) {
         FilenameFilter var18 = ConfigManager::testFile;
         File[] var19 = var1.listFiles(var18);
         if (var19 != null) {
            ConfigScreen var20 = ConfigScreen.configScreen;
            var20.run3();
            HashMap var21 = new HashMap();
            HashMap var22 = new HashMap();
            int var23 = var19.length;
            long var24 = 0L;

            while (true) {
               long var30 = (int)var24 & 4294967295L | var28 & -4294967296L;
               long var32 = var26;
               long var34 = var30;
               long var36 = var32;
               long var38 = var34;
               long var40 = var36;
               long var42 = var38;
               long var44 = var40;
               if ((int)((int)var24 & 4294967295L) >= var23) {
                  ProgressState var178 = progressState2;
                  int var179 = var21.size();
                  var178.handleInt(var179);
                  ArrayList var180 = new ArrayList();
                  Set var181 = var21.entrySet();
                  Iterator var182 = var181.iterator();

                  while (true) {
                     boolean var183 = var182.hasNext();
                     if (!var183) {
                        CompletableFuture[] var275 = new CompletableFuture[0];
                        Object[] var276 = var180.toArray(var275);
                        boolean var277 = var276 instanceof CompletableFuture[];
                        if (var276 != null && !var277) {
                           throw new ClassCastException("CHECKCAST failed");
                        }

                        CompletableFuture var463 = CompletableFuture.allOf((CompletableFuture<?>[])var276);
                        Map cap16 = var22;
                        Runnable var480 = () -> handleMap2(cap16);
                        var463.thenRun(var480);
                        return;
                     }

                     Object var184 = var182.next();
                     boolean var185 = var184 instanceof Entry;
                     if (var184 != null && !var185) {
                        throw new ClassCastException("CHECKCAST failed");
                     }

                     if (var278 == null) {
                        Lookup var279 = MethodHandles.lookup();
                        Class<?> var280 = this.getClass();
                        ClassLoader var281 = var280.getClassLoader();
                        MethodType var282 = MethodType.fromMethodDescriptorString(
                           "(Lorg/phobos/core/config/ConfigManager;Ljava/util/Map$Entry;Ljava/util/Map;)Ljava/lang/Runnable;", var281
                        );
                        Class<?> var283 = this.getClass();
                        ClassLoader var284 = var283.getClassLoader();
                        MethodType var285 = MethodType.fromMethodDescriptorString("()V", var284);
                        Class<?> var286 = this.getClass();
                        ClassLoader var287 = var286.getClassLoader();
                        MethodType var288 = MethodType.fromMethodDescriptorString("(Ljava/util/Map$Entry;Ljava/util/Map;)V", var287);
                        MethodHandle var289 = var279.findVirtual(ConfigManager.class, "handleEntry", var288);
                        Class<?> var290 = this.getClass();
                        ClassLoader var291 = var290.getClassLoader();
                        MethodType var292 = MethodType.fromMethodDescriptorString("()V", var291);
                        Object[] var585 = new Object[0];
                        var278 = LambdaMetafactory.metafactory(var279, "run", var282, var285, var289, var292);
                     }

                     MethodHandle var293 = var278.getTarget();
                     if (this != null) {
                        Object[] var586 = phobos$g1806cc7b0;
                        if (phobos$g1806cc7b0 == null) {
                           var586 = phobos$g1806cc7b0 = new Object[3];
                        }

                        var586[0] = this;
                     }

                     if (var184 != null) {
                        Object[] var587 = phobos$g1806cc7b0;
                        if (phobos$g1806cc7b0 == null) {
                           var587 = phobos$g1806cc7b0 = new Object[3];
                        }

                        var587[1] = var184;
                     }

                     Object[] var588 = phobos$g1806cc7b0;
                     if (phobos$g1806cc7b0 == null) {
                        var588 = phobos$g1806cc7b0 = new Object[3];
                     }

                     var588[2] = var22;
                     Object[] var10001 = phobos$g1806cc7b0;
                     if (phobos$g1806cc7b0 == null) {
                        var10001 = phobos$g1806cc7b0 = new Object[3];
                     }

                     Object var294 = var293.invokeWithArguments(var10001);
                     CompletableFuture var295 = PhobosClient.resolveCompletableFuture2((Runnable)var294);
                     var180.add(var295);
                  }
               }

               long var46 = var42;
               long var48 = var44;
               long var50 = var46;
               long var52 = var48;
               long var54 = var50;
               long var56 = var52;
               long var58 = var54;
               long var60 = var56;
               long var62 = var58;
               long var64 = var60;
               Object var66 = var19[(int)((int)var24 & 4294967295L)];
               long var67 = var62;
               long var69 = var64;
               long var71 = var67;
               long var73 = var69;
               long var75 = var71;
               long var77 = var73;
               long var79 = var75;
               long var81 = var77;
               long var83 = var79;
               long var85 = var81;
               long var87 = var83;
               long var89 = var85;
               long var91 = var87;
               long var93 = var89;
               long var95 = var91;
               long var97 = var93;
               String var99 = ((File)var66).getName();
               long var100 = var95;
               long var102 = var97;
               long var104 = var100;
               long var106 = var102;
               long var108 = var104;
               long var110 = var106;
               long var112 = var108;
               long var114 = var110;
               long var116 = var112;
               long var118 = var114;
               long var120 = var116;
               long var122 = var118;
               long var124 = var120;
               long var126 = var122;
               long var128 = var124;
               long var130 = var126;
               long var132 = var128;
               long var134 = var130;
               long var136 = var132;
               long var138 = var134;
               String[] var140 = var99.split("\\.");
               long var141 = var136;
               long var143 = var138;
               long var145 = var141;
               long var147 = var143;
               long var149 = var145;
               long var151 = var147;
               long var153 = var149;
               long var155 = var151;
               long var157 = var153;
               long var159 = var155;
               int var161 = var140.length;
               long var162 = var157;
               long var164 = var159;
               long var166 = var162;
               long var168 = var164;
               Object var219;
               long var220;
               long var222;
               if (var161 <= 0) {
                  long var170 = var166;
                  long var172 = var168;
                  long var186 = var170;
                  long var188 = var172;
                  long var190 = var186;
                  long var192 = var188;
                  long var194 = var190;
                  long var196 = var192;
                  long var198 = var194;
                  long var200 = var196;
                  long var202 = var198;
                  long var204 = var200;
                  long var206 = var202;
                  long var208 = var204;
                  String var210 = ((File)var66).getName();
                  long var211 = var206;
                  long var213 = var208;
                  long var215 = var211;
                  long var217 = var213;
                  var219 = var210;
                  var220 = var215;
                  var222 = var217;
               } else {
                  long var174 = var166;
                  long var176 = var168;
                  long var229 = var174;
                  long var231 = var176;
                  long var233 = var229;
                  long var235 = var231;
                  long var237 = var233;
                  long var239 = var235;
                  long var241 = var237;
                  long var243 = var239;
                  int var245 = var140.length;
                  long var246 = var241;
                  long var248 = var243;
                  long var250 = var246;
                  long var252 = var248;
                  long var254 = var250;
                  long var256 = var252;
                  long var258 = var254;
                  long var260 = var256;
                  long var262 = var258;
                  long var264 = var260;
                  long var266 = var262;
                  long var268 = var264;
                  Object var270 = var140[var245 + -2];
                  long var271 = var266;
                  long var273 = var268;
                  var219 = var270;
                  var220 = var271;
                  var222 = var273;
               }

               ConfigMeta var224;
               boolean var296 = true;
               long var297;
               long var299;
               if (!var296) {
                  long var225 = var220;
                  long var227 = var222;
                  var297 = var225;
                  var299 = var227;
               } else {
                  var297 = var220;
                  var299 = var222;
               }

               long var301 = var297;
               long var303 = var299;
               long var305 = var301;
               long var307 = var303;
               long var309 = var305;
               long var311 = var307;
               long var313 = var309;
               long var315 = var311;
               long var317 = var313;
               long var319 = var315;
               long var321 = var317;
               long var323 = var319;
               long var325 = var321;
               long var327 = var323;
               long var329 = var325;
               long var331 = var327;
               long var333 = var329;
               long var335 = var331;
               PUBLICPRIVATEEnum var337 = PUBLICPRIVATEEnum.PUBLIC;
               long var338 = var333;
               long var340 = var335;
               long var342 = var338;
               long var344 = var340;
               long var346 = var342;
               long var348 = var344;
               long var350 = var346;
               long var352 = var348;
               long var354 = var350;
               long var356 = var352;
               long var358 = var354;
               long var360 = var356;
               long var362 = var358;
               long var364 = var360;
               long var366 = var362;
               long var368 = var364;
               long var10000 = 0L | var368 & -256L;
               long var370 = var366;
               long var372 = var10000;
               long var374 = var370;
               long var376 = var372;
               long var378 = var374;
               long var380 = var376;
               long var382 = var378;
               long var384 = var380;
               var224 = new ConfigMeta((String)var219, (String)null, var337, (((int)((int)var384 & 0xFF & 0xFF) & 1) != 0), (((int)(0 & 0xFF) & 1) != 0), 0);
               long var386 = var382;
               long var388 = var384;
               long var390 = var386;
               long var392 = var388;
               long var394 = var390;
               long var396 = var392;
               long var398 = var394;
               long var400 = var396;
               long var402 = var398;
               long var404 = var400;
               long var406 = var402;
               long var408 = var404;
               long var410 = var406;
               long var412 = var408;
               long var414 = var410;
               long var416 = var412;
               long var418 = var414;
               long var420 = var416;
               long var422 = var418;
               long var424 = var420;
               long var426 = var422;
               long var428 = var424;
               var21.put(var224, var66);
               long var430 = var426;
               long var432 = var428;
               long var434 = var430;
               long var436 = var432;
               long var438 = var434;
               long var440 = var436;
               long var442 = var438;
               long var444 = var440;
               ConfigScreen var446 = ConfigScreen.configScreen;
               long var447 = var442;
               long var449 = var444;
               long var451 = var447;
               long var453 = var449;
               long var459;
               long var461;
               if (var446 != null) {
                  long var455 = var451;
                  long var457 = var453;
                  long var481 = var455;
                  long var483 = var457;
                  ConfigScreen var485 = ConfigScreen.configScreen;
                  long var486 = var481;
                  long var488 = var483;
                  long var490 = var486;
                  long var492 = var488;
                  long var494 = var490;
                  long var496 = var492;
                  long var498 = var494;
                  long var500 = var496;
                  long var502 = var498;
                  long var504 = var500;
                  long var506 = var502;
                  long var508 = var504;
                  long var510 = var506;
                  long var512 = var508;
                  long var514 = var510;
                  long var516 = var512;
                  ConfigEntryElement var518 = var485.resolveConfigEntryElement(var224);
                  long var519 = var514;
                  long var521 = var516;
                  long var523 = var519;
                  long var525 = var521;
                  long var527 = var523;
                  long var529 = var525;
                  long var531 = var527;
                  long var533 = var529;
                  long var535 = var531;
                  long var537 = var533;
                  long var539 = var535;
                  long var541 = var537;
                  long var543 = var539;
                  long var545 = var541;
                  long var547 = var543;
                  long var549 = var545;
                  long var551 = var547;
                  long var553 = var549;
                  long var555 = var551;
                  long var557 = var553;
                  long var559 = var555;
                  long var561 = var557;
                  long var563 = var559;
                  long var565 = var561;
                  long var567 = var563;
                  long var569 = var565;
                  var22.put(var224, var518);
                  long var571 = var567;
                  long var573 = var569;
                  long var575 = var571;
                  long var577 = var573;
                  long var579 = var575;
                  long var581 = var577;
                  var459 = var579;
                  var461 = var581;
               } else {
                  var459 = var451;
                  var461 = var453;
               }

               var10000 = (int)var459 + 1 & 4294967295L;
               var28 = var459;
               var26 = var461;
               var24 = var10000;
            }
         }
      }
   }

   public File getFile() {
      return path2.toFile();
   }

   public ConfigManager.Result resolveResult(String text) {
      File file = this.resolveFile(text);
      if (file == null) {
         return ConfigManager.Result.NOT_FOUND;
      } else {
         return file.delete() ? ConfigManager.Result.SUCCESS : ConfigManager.Result.ERROR_UNKNOWN;
      }
   }

   @SneakyThrows public void run8() {
      label74: {
         UserSession var1 = UserSession.userSession;
         if (var1 != null) {
            UserSession var2 = UserSession.userSession;
            UserProfile var3 = var2.getUserProfile();
            if (var3 != null) {
               break label74;
            }
         }

         ClientGuard.stop();
      }

      this.run7();
      JsonObject var4 = new JsonObject();
      ConfigJsonUtils.handleJsonObject(var4);
      List var5 = this.objectList;
      Iterator var6 = var5.iterator();

      while (true) {
         boolean var7 = var6.hasNext();
         if (!var7) {
            Path var11 = path;

            Path var12;
            try {
               var12 = var11.resolve("current.json");
            } catch (Throwable var24) {
               boolean var19 = var24 instanceof IOException;
               if (var19) {
                  return;
               }

               throw var24;
            }

            Gson var13 = JsonSupport.gson;

            String var14;
            try {
               var14 = var13.toJson(var4);
            } catch (Throwable var23) {
               boolean var25 = var23 instanceof IOException;
               if (var25) {
                  return;
               }

               throw var23;
            }

            OpenOption[] var15 = new OpenOption[0];

            try {
               Files.writeString(var12, var14, var15);
               return;
            } catch (Throwable var22) {
               boolean var26 = var22 instanceof IOException;
               if (var26) {
                  return;
               }

               throw var22;
            }
         }

         Object var8 = var6.next();
         boolean var9 = var8 instanceof Named;
         if ((int)((!var9 ? 0 : 1) & 0xFF & 0xFF) != 0) {
            boolean var10 = var8 instanceof Named;
            if (var8 != null && !var10) {
               throw new ClassCastException("CHECKCAST failed");
            }

            boolean var16 = var8 instanceof JsonSerializable;
            if ((int)((!var16 ? 0 : 1) & 0xFF & 0xFF) != 0) {
               boolean var17 = var8 instanceof JsonSerializable;
               if (var8 != null && !var17) {
                  throw new ClassCastException("CHECKCAST failed");
               }

               String var20 = this.describeNamed((Named)var8);
               JsonElement var21 = ((JsonSerializable)var8).createJsonPrimitive();
               var4.add(var20, var21);
            }
         }
      }
   }

   public CompletableFuture<ConfigManager.Result> resolveCompletableFuture2(String text, BINDSMODULESEnum[] bINDSMODULESEnums) {
      return PhobosClient.resolveCompletableFuture(() -> {
         File file = this.resolveFile(text);
         if (file == null) {
            return ConfigManager.Result.NOT_FOUND;
         }

         configSectionFilter.clear();
         if (bINDSMODULESEnums.length != 0) {
            configSectionFilter.handleB8OeGUh5Wu89mLeMNeBPr1SnBQ2MmivU(bINDSMODULESEnums);
         }

         ConfigManager.Result var15;
         label41: {
            try {
               try {
                  flag = true;
                  String text3 = Files.readString(file.toPath());
                  JsonObject jsonObject = JsonParser.parseString(text3).getAsJsonObject();

                  for (Object object : this.objectList) {
                     if (object instanceof Named) {
                        Named named = (Named)object;
                        if (named instanceof JsonSerializable) {
                           JsonSerializable jsonSerializable = (JsonSerializable)named;
                           String text2 = this.describeNamed(named);
                           JsonElement jsonElement = jsonObject.get(text2);
                           if (jsonElement != null) {
                              PhobosLog.handleText2("Loading " + text2);
                              jsonSerializable.handleJsonElement(jsonElement);
                              PhobosLog.handleText2("Loaded " + text2);
                           }
                        }
                     }
                  }

                  flag = false;
                  this.invoke(new NamedHandle(text));
                  var15 = ConfigManager.Result.SUCCESS;
                  break label41;
               } catch (IOException var13) {
               }
            } catch (Throwable var14) {
               configSectionFilter.clear();
               throw var14;
            }

            configSectionFilter.clear();
            return ConfigManager.Result.ERROR_UNKNOWN;
         }

         configSectionFilter.clear();
         return var15;
      });
   }

   public String describeNamed(Named named) {
      return named.getName().split("\\.")[0];
   }

   public void run4() {
      PhobosClient var1 = this.phobosClient;
      CloudConfigApi var2 = var1.getCloudConfigApi();
      Consumer<List<ConfigMeta>> var19 = this::handleList;
      Runnable[] var20 = new Runnable[0];
      var2.handleConsumer2(var19, var20);
   }

   public Path getPath() {
      return path;
   }

   public Path getPath2() {
      return Path.of(System.getProperty("user.home"), "earthhack");
   }

   public ConfigManager.Result resolveResult2(String text, String text2) {
      if (this.resolveFile(text) != null) {
         return ConfigManager.Result.ALREADY_EXISTS;
      }

      try {
         Files.writeString(path2.resolve(text + ".json"), text2);
         return ConfigManager.Result.SUCCESS;
      } catch (IOException iOException) {
         PhobosLog.handleText("Configs", "Could not save %s.json".formatted(text));
         iOException.printStackTrace(System.err);
         return ConfigManager.Result.ERROR_UNKNOWN;
      }
   }

   public List<String> getList() {
      File file2 = this.getFile();
      if (file2 == null) {
         return null;
      }

      File[] files = file2.listFiles((filex, textx) -> textx.endsWith(".json"));
      if (files == null) {
         return null;
      }

      List<String> list = new ArrayList<>();

      for (File file : files) {
         String[] texts = file.getName().split("\\.");
         String text = texts.length > 0 ? texts[texts.length - 2] : file.getName();
         list.add(text);
      }

      return list;
   }

   public void run7() {
      if (!path2.toFile().exists()) {
         path2.toFile().mkdirs();
      }

      if (!path.toFile().exists()) {
         path.toFile().mkdirs();
      }
   }

   public Map<String, String> getTextMap() {
      return this.textMap;
   }

   public static ConfigManager getConfigManager() {
      return configManager;
   }

   public PhobosClient getPhobosClient() {
      return this.phobosClient;
   }

   public void handleMap(Map map) {
      map.clear();
      progressState.stop();
      this.run();
   }

   public static ProgressState getProgressState() {
      return progressState;
   }

   public static void handleHudElement(HudElement hudElement) {
      hudElement.getSettingList().forEach(setting -> setting.testObject(setting.getObject()));
      hudElement.handleFlag3(true);
      hudElement.handleFlag2(true);
   }

   public static ConfigSectionFilter getConfigSectionFilter() {
      return configSectionFilter;
   }

   public static boolean testFile(File file, String text) {
      return text.endsWith(".json");
   }

   public List<Object> getObjectList() {
      return this.objectList;
   }

   public static ProgressState getProgressState2() {
      return progressState2;
   }

   public static void run9() {
      ClientHolder.getPhobosClient().getConfigManager().run4();
   }

   public static void handleMap2(Map map) {
      progressState2.stop();
      map.clear();
   }

   public static void handleText2(
      String text, String text2, boolean flag, PUBLICPRIVATEEnum pUBLICPRIVATEEnum, Map map, ConfigMeta configMeta, String text3
   ) {
      ConfigInfo configInfo = new CloudConfigInfo(text, text2, text3, true, flag, pUBLICPRIVATEEnum, ConfigJsonUtils.resolveBuildMetadata(text3));
      ConfigEntryElement configEntryElement = (ConfigEntryElement)map.remove(configMeta);
      if (configEntryElement != null) {
         configEntryElement.setConfigInfo(configInfo);
         configEntryElement.run7();
         ConfigScreen.configScreen.handleConfigEntryElement(configEntryElement);
      }

      progressState.run();
   }

   public static void handleModule(Module module) {
      module.handleFlag3(true);
   }

   public static CloudSettings getCloudSettings() {
      return cloudSettings;
   }

   public void handleEntry(Entry entry, Map map) {
      try {
         ConfigMeta configMeta = (ConfigMeta)entry.getKey();
         String text = Files.readString(((File)entry.getValue()).toPath());
         ConfigInfo configInfo = new CloudConfigEntry(this.phobosClient, configMeta.getName(), text, ConfigJsonUtils.resolveBuildMetadata(text));
         progressState2.run();
         ConfigEntryElement configEntryElement = (ConfigEntryElement)map.remove(configMeta);
         if (configEntryElement != null) {
            configEntryElement.setConfigInfo(configInfo);
            configEntryElement.run7();
            ConfigScreen.configScreen.handleConfigEntryElement2(configEntryElement);
         }
      } catch (IOException var7) {
      }
   }

   public enum Result {
      SUCCESS,
      NOT_FOUND,
      ALREADY_EXISTS,
      ERROR_UNKNOWN;
   }
}
