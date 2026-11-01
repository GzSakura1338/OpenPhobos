package org.phobos.module.client;

import lombok.SneakyThrows;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Base64.Encoder;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.phobos.api.MinecraftHolder;
import org.phobos.api.Easing.InOutEasing;
import org.phobos.api.TimeGate.Normal;
import org.phobos.core.entity.PositionRotationTracker;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.ColorSetting;
import org.phobos.core.setting.StringSetting;
import org.phobos.core.setting.BindSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.render.RenderContext;
import org.phobos.core.command.ChatPrefix;
import org.phobos.core.user.UserSession;
import org.phobos.core.render.CameraState;
import org.phobos.core.net.PacketBus;
import org.phobos.core.animation.EasedAnimation;
import org.phobos.core.net.packet.SharedEspPacket;
import org.phobos.core.net.packet.FourTextPacket196;
import org.phobos.core.net.packet.ChatMessagePacket;
import org.phobos.core.net.packet.FlagTextPacket206;
import org.phobos.core.net.packet.TextPairPacket208;
import org.phobos.core.net.packet.TextPacket211;
import org.phobos.core.net.packet.TextPacket237;
import org.phobos.core.net.packet.FlagMessagePacket251;
import org.phobos.core.net.packet.FlagTextPacket253;
import org.phobos.core.net.packet.MessagePacket278;
import org.phobos.core.net.packet.Vec3Packet287;
import org.phobos.core.setting.SettingGroup;
import org.phobos.core.animation.FloatAnimator;
import org.phobos.core.bind.KeyCombination;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.render.RenderUtils;
import org.phobos.core.chat.ChatTextUtils;
import org.phobos.core.ClientGuard;
import org.phobos.core.math.MathUtils;
import org.phobos.core.render.ColorUtils;
import org.phobos.core.render.Render3d;
import org.phobos.core.world.RaycastUtils;
import org.phobos.core.render.texture.SkyboxTextureUtils;
import org.phobos.core.server.ServerInfoUtils;
import org.phobos.core.setting.SettingSubsection.Builder;
import org.phobos.core.render.Render3d.Cuboid;
import org.phobos.event.Listener;
import org.phobos.event.SafeListener;
import org.phobos.event.SendChatMessageEvent;
import org.phobos.event.TickEvent2;
import org.phobos.module.Module;

public class IRCModule extends Module {
   public Setting<String> textSetting;
   public static IRCModule iRCModule;
   public Normal normal;
   public Setting<Long> longValueSetting;
   public Setting<Color> colorSetting;
   public Setting<KeyCombination> keyCombinationSetting;
   public Color color;
   public boolean flag;
   public Setting<Boolean> booleanValueSetting;
   public Setting<Boolean> booleanValueSetting2;
   public ExecutorService executorService;
   public static String text;
   public List<IRCModule.Beam> beamList;
   private static Object[] phobos$g1806d57e8;
   private static Object[] phobos$g1806d58e0;
   private static Object[] phobos$g1806d59d8;
   private static Object[] phobos$g1806d5ad0;
   private static Object[] phobos$g1806d5bc8;
   private static Object[] phobos$g1806d5cc0;
   private static Object[] phobos$g1806d5db8;
   private static Object[] phobos$g1806d5eb0;
   private static Object[] phobos$g1806d5fa8;
   private static Object[] phobos$g1806d6118;
   private static Object[] phobos$g1806d51a8;
   private static Object[] phobos$g1806d4b70;

   public List<IRCModule.Beam> getBeamList() {
      return this.beamList;
   }

   public Setting<Long> getTime() {
      return this.longValueSetting;
   }

   public Setting<Color> getColorSetting() {
      return this.colorSetting;
   }

   @SneakyThrows public void handleText(String text, String text2) {
      String var3 = ClientGuard.text;
      boolean var4 = var3.equals("127.0.0.1");
      if (!var4) {
         ClientGuard.handleText("Swapping out server IP");
         ClientGuard.stop();
      }

      String var6 = ClientGuard.text;
      String var14 = "http://" + var6 + ":7565/api/v1/client/report";

      Throwable var19;
      label181: {
         Throwable var17;
         label204: {
            Throwable var29;
            label194: {
               OutputStream var20;
               label205: {
                  label191: {
                     URL var5;
                     try {
                        var5 = new URL(var14);
                     } catch (Throwable var51) {
                        var17 = var51;
                        boolean var18 = true;
                        if (!var18) {
                           break label204;
                        }
                        break label191;
                     }

                     URLConnection var15;
                     try {
                        var15 = var5.openConnection();
                     } catch (Throwable var50) {
                        var17 = var50;
                        boolean var52 = true;
                        if (!var52) {
                           break label204;
                        }
                        break label191;
                     }

                     boolean var16 = var15 instanceof HttpURLConnection;
                     if (var15 != null && !var16) {
                        throw new ClassCastException("CHECKCAST failed");
                     }

                     try {
                        ((HttpURLConnection)var15).setRequestMethod(text);
                     } catch (Throwable var49) {
                        var17 = var49;
                        boolean var53 = true;
                        if (!var53) {
                           break label204;
                        }
                        break label191;
                     }

                     try {
                        ((HttpURLConnection)var15).setConnectTimeout(5000);
                     } catch (Throwable var48) {
                        var17 = var48;
                        boolean var54 = true;
                        if (!var54) {
                           break label204;
                        }
                        break label191;
                     }

                     try {
                        ((HttpURLConnection)var15).setReadTimeout(5000);
                     } catch (Throwable var47) {
                        var17 = var47;
                        boolean var55 = true;
                        if (!var55) {
                           break label204;
                        }
                        break label191;
                     }

                     try {
                        ((HttpURLConnection)var15).setDoOutput(true & true);
                     } catch (Throwable var46) {
                        var17 = var46;
                        boolean var56 = true;
                        if (!var56) {
                           break label204;
                        }
                        break label191;
                     }

                     if (text2 != null) {
                        try {
                           ((HttpURLConnection)var15).setRequestProperty("Content-Type", "application/json");
                        } catch (Throwable var45) {
                           var17 = var45;
                           boolean var57 = true;
                           if (!var57) {
                              break label204;
                           }
                           break label191;
                        }

                        try {
                           var20 = ((HttpURLConnection)var15).getOutputStream();
                        } catch (Throwable var44) {
                           var17 = var44;
                           boolean var58 = true;
                           if (!var58) {
                              break label204;
                           }
                           break label191;
                        }

                        Charset var21 = StandardCharsets.UTF_8;

                        byte[] var22;
                        try {
                           var22 = text2.getBytes(var21);
                        } catch (Throwable var35) {
                           var29 = var35;
                           boolean var30 = true;
                           if (!var30) {
                              break label194;
                           }
                           break label205;
                        }

                        try {
                           var20.write(var22);
                        } catch (Throwable var34) {
                           var29 = var34;
                           boolean var67 = true;
                           if (!var67) {
                              break label194;
                           }
                           break label205;
                        }

                        if (var20 != null) {
                           try {
                              var20.close();
                           } catch (Throwable var43) {
                              var17 = var43;
                              boolean var59 = true;
                              if (!var59) {
                                 break label204;
                              }
                              break label191;
                           }
                        }
                     }

                     InputStream var25;
                     try {
                        var25 = ((HttpURLConnection)var15).getInputStream();
                     } catch (Throwable var42) {
                        var17 = var42;
                        boolean var60 = true;
                        if (!var60) {
                           break label204;
                        }
                        break label191;
                     }

                     InputStreamReader var24;
                     try {
                        var24 = new InputStreamReader(var25);
                     } catch (Throwable var41) {
                        var17 = var41;
                        boolean var61 = true;
                        if (!var61) {
                           break label204;
                        }
                        break label191;
                     }

                     BufferedReader var23;
                     try {
                        var23 = new BufferedReader(var24);
                     } catch (Throwable var40) {
                        var17 = var40;
                        boolean var62 = true;
                        if (!var62) {
                           break label204;
                        }
                        break label191;
                     }

                     StringBuilder var26;
                     try {
                        var26 = new StringBuilder();
                     } catch (Throwable var39) {
                        var17 = var39;
                        boolean var63 = true;
                        if (!var63) {
                           break label204;
                        }
                        break label191;
                     }

                     while (true) {
                        String var27;
                        try {
                           var27 = var23.readLine();
                        } catch (Throwable var37) {
                           var17 = var37;
                           boolean var64 = true;
                           if (!var64) {
                              break label204;
                           }
                           break;
                        }

                        if (var27 == null) {
                           try {
                              var23.close();
                              return;
                           } catch (Throwable var36) {
                              var17 = var36;
                              boolean var66 = true;
                              if (!var66) {
                                 break label204;
                              }
                              break;
                           }
                        }

                        try {
                           var26.append(var27);
                        } catch (Throwable var38) {
                           var17 = var38;
                           boolean var65 = true;
                           if (!var65) {
                              break label204;
                           }
                           break;
                        }
                     }
                  }

                  var17.printStackTrace();
                  return;
               }

               if (var20 != null) {
                  try {
                     var20.close();
                  } catch (Throwable var33) {
                     boolean var32 = true;
                     if (!var32) {
                        var19 = var33;
                        break label181;
                     }

                     var29.addSuppressed(var33);
                  }
               }

               throw var29;
            }

            Throwable var28 = var29;
            throw var28;
         }

         var19 = var17;
      }

      Throwable var28 = var19;
      throw var28;
   }

   public Setting<Boolean> getBooleanValueSetting2() {
      return this.booleanValueSetting2;
   }

   public Normal getNormal() {
      return this.normal;
   }

   public static void handleCrashReport(CrashReport crashReport) {
      if (iRCModule != null) {
         iRCModule.handleCrashReport2(crashReport);
      }
   }

   public static boolean check() {
      return iRCModule.check7() && iRCModule.booleanValueSetting.getValue();
   }

   public Setting<Boolean> getBooleanValueSetting() {
      return this.booleanValueSetting;
   }

   public ExecutorService getExecutorService() {
      return this.executorService;
   }

   public void handleText3(Text text) {
      if (this.check7()) {
         NotificationsModule.getNotificationsModule().handleText5(text);
      }
   }

   public Color getColor() {
      return this.color;
   }

   public void handleCrashReport2(CrashReport crashReport) {
      boolean var2 = SkyboxTextureUtils.flag;
      if (!var2) {
         Throwable var3 = crashReport.getCause();
         StringWriter var4 = new StringWriter();
         PrintWriter var5 = new PrintWriter(var4);
         var3.printStackTrace(var5);
         String var6 = var4.toString();
         StackTraceElement[] var7 = var3.getStackTrace();
         int var8 = var7.length;
         if (var8 != 0) {
            String var9 = crashReport.getMessage();
            JsonObject var10 = new JsonObject();
            String var11 = UserSession.describe3();
            var10.addProperty("Token", var11);
            var10.addProperty("Thread", var9);
            Encoder var12 = Base64.getEncoder();
            Charset var13 = StandardCharsets.UTF_8;
            byte[] var14 = var6.getBytes(var13);
            String var15 = var12.encodeToString(var14);
            var10.addProperty("Exception", var15);
            String var16 = var10.toString();
            this.handleText("POST", var16);
            Toolkit var17 = Toolkit.getDefaultToolkit();
            Clipboard var18 = var17.getSystemClipboard();
            StringSelection var19 = new StringSelection(var6);
            var18.setContents(var19, (ClipboardOwner)null);
         }
      }
   }

   public IRCModule(PhobosClient phobosClient) {
      super(phobosClient, "IRC", Categories.client, "IRC and shared features");
      this.nativeInit(phobosClient);
   }

   public void nativeInit(final PhobosClient var1) {
      ExecutorService var2 = Executors.newFixedThreadPool(4);
      this.executorService = var2;
      Color var3 = new Color(0, 0, 0, 0);
      this.color = var3;
      Normal var4 = new Normal();
      this.normal = var4;
      ArrayList var5 = new ArrayList();
      this.beamList = var5;
      this.flag = false & true;
      StringSetting var6 = new StringSetting("Prefix", "@");
      Setting var7 = this.resolveSetting2(var6);
      this.textSetting = var7;
      Boolean var9 = false & true;
      BooleanSetting var8 = new BooleanSetting("Pings", var9);
      Setting var10 = this.resolveSetting2(var8);
      this.booleanValueSetting = var10;
      BindSetting var11 = new BindSetting("Key");
      Setting var12 = this.resolveSetting2(var11);
      this.keyCombinationSetting = var12;
      BooleanSetting var13 = new BooleanSetting("Render");
      Setting var14 = this.resolveSetting2(var13);
      this.booleanValueSetting2 = var14;
      LongNumberSetting var15 = new LongNumberSetting("Time", 250L, 250L, 3000L, true & true);
      Setting var16 = this.resolveSetting2(var15);
      this.longValueSetting = var16;
      Color var18 = Color.WHITE;
      Color var19 = ColorUtils.createColor6(var18, (float)0.4F);
      ColorSetting var17 = new ColorSetting("Box", var19);
      ColorSetting var20 = var17.getColorSetting();
      Setting var21 = this.resolveSetting2(var20);
      this.colorSetting = var21;
      Builder var22 = this.createBuilder();
      Setting var23 = this.booleanValueSetting;
      Builder var24 = var22.resolveBuilder6(var23);
      SettingGroup[] var25 = new SettingGroup[1];
      Setting var26 = this.booleanValueSetting2;
      Setting[] var27 = new Setting[2];
      Setting var28 = this.longValueSetting;
      var27[0] = var28;
      Setting var29 = this.colorSetting;
      var27[1] = var29;
      SettingGroup var30 = this.resolveSettingGroup(var26, var27);
      var25[0] = var30;
      Builder var31 = var24.resolveBuilder5(var25);
      Setting[] var32 = new Setting[1];
      Setting var33 = this.keyCombinationSetting;
      var32[0] = var33;
      Builder var34 = var31.resolveBuilder(var32);
      var34.getSettingSubsection();
      iRCModule = this;
      PacketBus var35 = var1.getPacketBus();
      IRCModule var10000 = this;
      PhobosClient cap6 = var1;
      IRCModule cap5 = var10000;
      Consumer<Vec3Packet287> var52 = var2x -> cap5.handlePhobosClient3(cap6, var2x);
      var35.handleClassValue(Vec3Packet287.class, var52);
      PacketBus var53 = var1.getPacketBus();
      Consumer<ChatMessagePacket> var70 = IRCModule::handleChatMessagePacket;
      var53.handleClassValue(ChatMessagePacket.class, var70);
      PacketBus var71 = var1.getPacketBus();
      Consumer<FlagMessagePacket251> var88 = IRCModule::handleFlagMessagePacket251;
      var71.handleClassValue(FlagMessagePacket251.class, var88);
      PacketBus var89 = var1.getPacketBus();
      Consumer<TextPacket211> var106 = IRCModule::handleTextPacket211;
      var89.handleClassValue(TextPacket211.class, var106);
      PacketBus var107 = var1.getPacketBus();
      Consumer<FlagTextPacket253> var124 = IRCModule::handleFlagTextPacket253;
      var107.handleClassValue(FlagTextPacket253.class, var124);
      PacketBus var125 = var1.getPacketBus();
      Consumer<FourTextPacket196> var142 = IRCModule::handleFourTextPacket196;
      var125.handleClassValue(FourTextPacket196.class, var142);
      PacketBus var143 = var1.getPacketBus();
      PhobosClient cap7 = var1;
      Consumer<SharedEspPacket> var160 = var1x -> handlePhobosClient(cap7, var1x);
      var143.handleClassValue(SharedEspPacket.class, var160);
      PacketBus var161 = var1.getPacketBus();
      PhobosClient cap8 = var1;
      Consumer<TextPairPacket208> var178 = var1x -> handlePhobosClient2(cap8, var1x);
      var161.handleClassValue(TextPairPacket208.class, var178);
      PacketBus var179 = var1.getPacketBus();
      Consumer<TextPacket237> var196 = IRCModule::handleTextPacket237;
      var179.handleClassValue(TextPacket237.class, var196);
      var var197 = new SafeListener<TickEvent2>() {
         public void handleObject(
            TickEvent2 tickEvent2,
            ClientPlayerEntity clientPlayerEntity,
            ClientWorld clientWorld,
            ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            if (mc.currentScreen == null && var1.getBindManager().testKeyCombination(IRCModule.this.keyCombinationSetting.getValue())) {
               if (IRCModule.this.normal.testLong2(5000L)) {
                  BlockHitResult blockHitResult = RaycastUtils.resolveBlockHitResult9(clientPlayerEntity, clientWorld, 60.0);
                  if (blockHitResult != null) {
                     Vec3d vec3d = blockHitResult.getPos();
                     var1.getPacketBus()
                        .handleNetworkPacket2(
                           new Vec3Packet287(MathUtils.computeDouble(vec3d.x, 2), MathUtils.computeDouble(vec3d.y, 2), MathUtils.computeDouble(vec3d.z, 2))
                        );
                  }
               }
            }
         }
      };
      this.handleListener(var197);
      var var198 = new Listener<SendChatMessageEvent>() {
         public void handleObject2(SendChatMessageEvent sendChatMessageEvent) {
            String text = IRCModule.this.textSetting.getValue();
            if (sendChatMessageEvent.getMessage().startsWith(text) && !sendChatMessageEvent.isCancelled()) {
               var1.getPacketBus().handleNetworkPacket2(new MessagePacket278(sendChatMessageEvent.getMessage().substring(text.length()).trim()));
               sendChatMessageEvent.setCancelled(true);
            }
         }
      };
      this.handleListener(var198);
      var var199 = new Listener<RenderContext>() {
         public void handleObject2(RenderContext renderContext) {
            if (!IRCModule.this.beamList.isEmpty()) {
               if (!IRCModule.this.booleanValueSetting2.getValue()) {
                  IRCModule.this.beamList.clear();
               } else {
                  IRCModule.this.beamList.forEach(beam -> {
                     if (beam.check3()) {
                        beam.stop();
                     }

                     beam.handleRenderContext(renderContext, IRCModule.this.colorSetting.getValue(), IRCModule.this.color);
                  });
                  IRCModule.this.beamList.removeIf(IRCModule.Beam::check2);
               }
            }
         }
      };
      this.handleListener(var199);
      Setting var200 = this.textSetting;
      Consumer<String> var217 = this::handleText4;
      var200.handleConsumer2(var217);
   }

   public static boolean testText(String text) {
      return iRCModule.check7() && text.startsWith(iRCModule.textSetting.getValue());
   }

   public void setFlag(boolean flag) {
      this.flag = flag;
   }

   public Setting<KeyCombination> getKeyCombinationSetting() {
      return this.keyCombinationSetting;
   }

   public void handleText2(String text) {
      Thread var2 = Thread.currentThread();
      String var3 = var2.getName();
      JsonObject var4 = new JsonObject();
      String var5 = UserSession.describe3();
      var4.addProperty("Token", var5);
      var4.addProperty("Thread", var3);
      var4.addProperty("Exception", text);
      IRCModule var10000 = this;
      JsonObject cap10 = var4;
      IRCModule cap9 = var10000;
      Runnable var22 = () -> cap9.handleJsonObject(cap10);
      CompletableFuture.runAsync(var22);
   }

   public boolean isFlag3() {
      return this.flag;
   }

   public static IRCModule getIRCModule() {
      return iRCModule;
   }

   public Setting<String> getTextSetting() {
      return this.textSetting;
   }

   public static void handleFlagMessagePacket251(FlagMessagePacket251 flagMessagePacket251) {
      if (getIRCModule() != null) {
         NotificationsModule.getNotificationsModule()
            .handleConsumer(
               color -> {
                  if (flagMessagePacket251.isFlag()) {
                     getIRCModule().handleText3(Text.literal("[IRC] %s".formatted(flagMessagePacket251.getMessage())).withColor(color.hashCode()));
                  } else {
                     getIRCModule()
                        .handleText3(
                           Text.literal("[IRC] ")
                              .withColor(color.hashCode())
                              .append(Text.literal(flagMessagePacket251.getText2() + ": " + flagMessagePacket251.getMessage()).withColor(16777215))
                        );
                  }
               }
            );
      }
   }

   public static void handleTextPacket211(TextPacket211 textPacket211) {
      if (getIRCModule() != null) {
         NotificationsModule.getNotificationsModule()
            .handleConsumer(
               color -> getIRCModule()
                  .handleText3(Text.literal("[IRC] ").withColor(color.hashCode()).append(Text.literal(textPacket211.getText()).withColor(16777215)))
            );
      }
   }

   public static void handleChatMessagePacket(ChatMessagePacket chatMessagePacket) {
      String text = ChatTextUtils.describeText4(chatMessagePacket.getText2());
      if (chatMessagePacket.getText().equals(UserSession.userSession.getText())) {
         setText(text);
      }

      if (getIRCModule() != null) {
         NotificationsModule.getNotificationsModule()
            .handleTriConsumer(
               (color, color2, color3) -> getIRCModule()
                  .handleText3(
                     Text.literal("[IRC] ")
                        .withColor(color2.hashCode())
                        .append(Text.literal("[" + text + " -> " + chatMessagePacket.getText() + "]: " + chatMessagePacket.getMessage()).withColor(16777215))
                  )
            );
      }
   }

   public void handleText4(String text) {
      if (ChatPrefix.getINSTANCE().testText(text)) {
         this.textSetting.setValue("@");
      }
   }

   public static String getText2() {
      return text;
   }

   public static void setText(String text) {
      IRCModule.text = text;
   }

   public static void handleFourTextPacket196(FourTextPacket196 fourTextPacket196) {
   }

   public static void handlePhobosClient(PhobosClient phobosClient, SharedEspPacket sharedEspPacket) {
      if (mc.player != null) {
         String text = sharedEspPacket.getText2();
         String text2 = sharedEspPacket.getName();
         String text3 = sharedEspPacket.getText();
         double d = sharedEspPacket.getDoubleValue3();
         double d2 = sharedEspPacket.getDoubleValue2();
         double d3 = sharedEspPacket.getDoubleValue();
         double d4 = Math.round(Math.sqrt(mc.player.getPos().squaredDistanceTo(d, d2, d3)));
         phobosClient.getChatManager()
            .handleText7(
               "Do you want to add %s (%s blocks away) sent by %s as a waypoint?".formatted(text2, d4, text),
               () -> phobosClient.getWaypointManager().testDouble2(d, d2, d3, text2, text3)
            );
      }
   }

   public static void handlePhobosClient2(PhobosClient phobosClient, TextPairPacket208 textPairPacket208) {
      String text = textPairPacket208.getText();
      phobosClient.getChatManager()
         .handleText6(
            "%s (%s) wants to join you on this server".formatted(text, textPairPacket208.getText2()),
            () -> phobosClient.getPacketBus().handleNetworkPacket2(new FlagTextPacket206(true, text)),
            () -> phobosClient.getPacketBus().handleNetworkPacket2(new FlagTextPacket206(false, text)),
            "Accept",
            "Reject"
         );
   }

   public void handlePhobosClient3(PhobosClient phobosClient, Vec3Packet287 vec3Packet287) {
      if (this.booleanValueSetting.getValue() && this.booleanValueSetting2.getValue()) {
         if (mc.player != null && mc.world != null) {
            this.beamList
               .add(
                  new IRCModule.Beam(
                     new Vec3d(vec3Packet287.getDoubleValue3(), vec3Packet287.getDoubleValue2(), vec3Packet287.getDoubleValue()),
                     phobosClient,
                     this.colorSetting.getValue(),
                     this.longValueSetting.getValue()
                  )
               );
         }
      }
   }

   public static void handleTextPacket237(TextPacket237 textPacket237) {
      mc.submit(() -> ServerInfoUtils.handleText(textPacket237.getText()));
   }

   public static void handleFlagTextPacket253(FlagTextPacket253 flagTextPacket253) {
      if (getIRCModule() != null) {
         NotificationsModule.getNotificationsModule()
            .handleConsumer(
               color -> getIRCModule()
                  .handleText3(
                     Text.literal("[IRC] ")
                        .withColor(color.hashCode())
                        .append(
                           Text.literal(flagTextPacket253.getText2() + " " + (flagTextPacket253.isFlag() ? "has connected." : "has disconnected"))
                              .formatted(Formatting.RESET)
                        )
                  )
            );
      }
   }

   public void handleJsonObject(JsonObject jsonObject) {
      this.handleText("POST", jsonObject.toString());
   }

   public static final class Beam {
      public final FloatAnimator floatAnimator;
      public static final Identifier identifier = Identifier.of("phobos", "textures/warning.png");
      public final Vec3d vec3d;
      public final CameraState cameraState;
      public final EasedAnimation easedAnimation;
      public boolean flag;
      public final FloatAnimator floatAnimator2 = new FloatAnimator();
      public boolean flag2;
      public final long longValue;
      public final Color color;
      public int intValue;
      public final PhobosClient phobosClient;
      public final long longValue2;

      public EasedAnimation getEasedAnimation() {
         return this.easedAnimation;
      }

      public PhobosClient getPhobosClient() {
         return this.phobosClient;
      }

      public long getLongValue() {
         return this.longValue;
      }

      public Beam(Vec3d vec3d, PhobosClient phobosClient, Color color, long l) {
         this.floatAnimator = new FloatAnimator();
         this.flag = false;
         this.flag2 = false;
         this.cameraState = new CameraState();
         this.phobosClient = phobosClient;
         this.color = color;
         this.vec3d = vec3d;
         this.easedAnimation = new EasedAnimation(600L, false);
         this.longValue = System.currentTimeMillis();
         this.longValue2 = l;
         this.run();
      }

      public FloatAnimator getFloatAnimator2() {
         return this.floatAnimator2;
      }

      public boolean check() {
         int i = 0;
         int i2 = (int)this.longValue2;
         return this.testInt(this.intValue, i, i + 16)
            || this.testInt(this.intValue, i + 32, i + 48)
            || this.testInt(this.intValue, i2 - 48, i2 - 32)
            || this.intValue >= i2 - 16;
      }

      public boolean check2() {
         return this.longValue + 750L < System.currentTimeMillis()
            && !this.easedAnimation.isFlag()
            && this.easedAnimation.computeEasing(InOutEasing.INSTANCE3) == 0.0F;
      }

      public void handleRenderContext(RenderContext renderContext, Color color, Color color2) {
         if (MinecraftHolder.mc.player != null && MinecraftHolder.mc.world != null) {
            if (this.longValue + 400L < System.currentTimeMillis() && !this.easedAnimation.isFlag() && !this.flag) {
               this.easedAnimation.handleFlag2(true);
               this.flag = true;
            }

            if (this.longValue2 - this.intValue < 2L && !this.flag2) {
               this.run();
               this.flag2 = true;
            }

            Box box = this.createBox();
            Render3d.handleCuboid(
               Cuboid.createBuilder().resolveBuilder11(box).resolveBuilder13(this.vec3d).resolveBuilder18(color, color2).resolveBuilder12(1.0F).getCuboid()
            );
            this.floatAnimator2.setFloatValue((float)MinecraftHolder.mc.player.getEyePos().y);
            this.floatAnimator2.handleFloat3(10.0F, 0.03F);
            float f = renderContext.getFloatValue();
            if (!this.check() && MinecraftHolder.mc.getCameraEntity() != null) {
               this.cameraState.setClientPlayerEntity(MinecraftHolder.mc.player);
               this.cameraState.setCamera(MinecraftHolder.mc.gameRenderer.getCamera());
               this.cameraState.setVec3d(new Vec3d(this.vec3d.getX(), this.floatAnimator2.getFloatValue2(), this.vec3d.getZ()));
               this.cameraState.setEntity(MinecraftHolder.mc.getCameraEntity());
               this.cameraState.run();
               this.cameraState.handleFloat(f, 1.0F, (matrixStack, vec3d) -> {
                  float fx = MathUtils.computeFloat3(Math.abs(this.floatAnimator2.getFloatValue() - this.floatAnimator2.getFloatValue2()) * 8.5F, 0.0F, 180.0F);
                  matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(fx));
                  RenderSystem.setShaderTexture(0, identifier);
                  RenderSystem.setShaderColor(this.color.getRed() / 255.0F, this.color.getGreen() / 255.0F, this.color.getBlue() / 255.0F, 1.0F);
                  double d = 20.0;
                  RenderUtils.handleMatrixStack2(matrixStack, -(d / 2.0), -(d / 2.0), d);
                  RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
               });
               this.run2();
            } else {
               this.run2();
            }
         }
      }

      public FloatAnimator getFloatAnimator() {
         return this.floatAnimator;
      }

      public void run() {
         if (MinecraftHolder.mc.player != null) {
            Vec3d vec3d = new Vec3d(this.vec3d.x, MinecraftHolder.mc.player.getY() - 24.0 * MathUtils.computeDouble9(0.7, 1.1), this.vec3d.z);

            for (int i = 0; i < 20; i++) {
               double d = 0.08;
               double d2 = MathUtils.computeDouble9(-d, d);
               double d3 = MathUtils.computeDouble9(-d, d);
               double d4 = MathUtils.computeDouble9(0.4, 1.4);
               float f = MathUtils.computeFloat8(0.3F, 1.0F);
               PositionRotationTracker positionRotationTracker = new PositionRotationTracker(vec3d.add(d2, 0.0, d3), new Vec3d(0.0, 12.0 * d4, 0.0), 2.0F, 100.0F * f) {
                  @Override
                  public Color getColor() {
                     float fx = 255.0F / (float)this.getLongValue();
                     float f2 = MathHelper.clamp((int)(fx * (float)(this.getLongValue2() + this.getLongValue() - System.currentTimeMillis())), 0, 255) / 255.0F;
                     return ColorUtils.createColor6(Beam.this.color, f2);
                  }

                  @Override
                  public boolean alwaysTrue() {
                     return true;
                  }

                  @Override
                  public void run() {
                     this.setVec3d(this.getVec3d2());
                     this.setVec3d2(this.getVec3d2().add(this.getVec3d3()));
                     this.setVec3d3(this.getVec3d3().multiply(0.7F));
                  }
               };
               positionRotationTracker.setFloatValue3(0.0F);
               this.phobosClient.getTrackerManager().handlePositionRotationTracker2(positionRotationTracker, 720L);
            }
         }
      }

      public Vec3d getVec3d() {
         return this.vec3d;
      }

      public int getIntValue() {
         return this.intValue;
      }

      public Color getColor() {
         return this.color;
      }

      public void stop() {
         if (this.easedAnimation.isFlag()) {
            this.easedAnimation.handleFlag2(false);
         }
      }

      public long getLongValue2() {
         return this.longValue2;
      }

      public CameraState getCameraState() {
         return this.cameraState;
      }

      public boolean isFlag2() {
         return this.flag2;
      }

      public boolean testInt(int i, int i2, int i3) {
         return i >= i2 && i <= i3;
      }

      public boolean check3() {
         return this.intValue >= this.longValue2;
      }

      public Box createBox() {
         double d = this.easedAnimation.computeEasing(InOutEasing.INSTANCE3) / 2.0;
         return new Box(-(d / 3.0), -300.0, -(d / 3.0), d / 3.0, 500.0, d / 3.0);
      }

      public boolean isFlag() {
         return this.flag;
      }

      public void run2() {
         this.intValue++;
      }
   }
}
