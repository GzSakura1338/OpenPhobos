package org.phobos.core.script;

import lombok.SneakyThrows;
import java.lang.invoke.StringConcatFactory;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import java.io.File;
import java.io.IOException;
import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.stream.Stream;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.NotNull;
import org.phobos.api.Named;
import org.phobos.api.JsonSerializable;
import org.phobos.api.TimeGate.Normal;
import org.phobos.event.Listener;
import org.phobos.event.RunEvent;
import org.phobos.type.ConfigScriptEnum;
import org.phobos.type.PUBLICPRIVATEEnum;
import org.phobos.type.UnknownTypeUnknownMethodEnum;
import org.phobos.core.ClientHolder;
import org.phobos.core.config.CloudConfigApi;
import org.phobos.core.config.CloudConfigRef;
import org.phobos.core.event.EventBus;
import org.phobos.core.Feature;
import org.phobos.core.PhobosClient;
import org.phobos.core.gui.screen.ScriptScreen;

public class ScriptManager extends Feature implements Iterable<Script>, JsonSerializable, Named {
   public final List<Script> scriptList = new CopyOnWriteArrayList<>();
   public static final Map<Class<? extends RuntimeException>, UnknownTypeUnknownMethodEnum> classValueMap = new HashMap<>();
   public static boolean flag;
   public static int intValue;
   public boolean flag2;
   public static final Map<UnknownTypeUnknownMethodEnum, Class<? extends RuntimeException>> unknownTypeUnknownMethodEnumMap = new HashMap<>();
   public final PhobosClient phobosClient;
   public static int intValue2;
   public static final Path path = FabricLoader.getInstance().getGameDir().resolve("phoboslite");
   public static final Normal normal = new Normal();
   public final List<Script> scriptList2 = new ArrayList<>();
   private static Object[] phobos$g1806d1300;
   private static Object[] phobos$g1806d1728;
   private static Object[] phobos$g1806d19b0;
   private static Object[] phobos$g1806d10b0;
   private static Object[] phobos$g1806d11c8;
   private static Object[] phobos$g1806d15a0;

   @Override
   public String getName() {
      return "scripts.json";
   }

   public void handleScript2(Script script) {
      List var2 = this.scriptList2;
      var2.add(script);
   }

   public static String[] values(int i) {
      return new String[i];
   }

   public void handleScript(Script script) {
      this.scriptList2.add(script);
      if (this.flag2) {
         ScriptScreen.scriptScreen.handleIterable(this);
      }
   }

   @SneakyThrows public void run() {
      long var574 = 0L;
      CallSite var189 = null;
      CallSite var412 = null;
      long var13 = 0L;
      Path var1 = path;
      Path var2 = var1.resolve("scripts");
      File var3 = var2.toFile();
      boolean var4 = var3.exists();
      if (!var4) {
         File var5 = var2.toFile();
         var5.mkdirs();
      }

      File var6 = var2.toFile();
      File[] var7 = var6.listFiles();
      Object var8 = Objects.requireNonNull(var7);
      boolean var9 = var8 instanceof File[];
      if (var8 != null && !var9) {
         throw new ClassCastException("CHECKCAST failed");
      }

      int var10 = Array.getLength(var8);
      long var11 = 0L;

      while (true) {
         long var15 = (int)var11 & 4294967295L | var13 & -4294967296L;
         long var17 = var15;
         long var19 = var17;
         long var21 = var19;
         if ((int)((int)var11 & 4294967295L) >= var10) {
            return;
         }

         long var129;
         label204: {
            long var74;
            long var23 = var21;
            long var25 = var23;
            long var27 = var25;
            long var29 = var27;
            long var31 = var29;
            Object var33 = ((Object[])var8)[(int)((int)var11 & 4294967295L)];
            long var34 = var31;
            long var36 = var34;
            long var38 = var36;
            long var40 = var38;
            long var42 = var40;
            long var44 = var42;
            long var46 = var44;
            String var48 = ((File)var33).getPath();
            long var49 = var46;
            long var51 = var49;
            long var53 = var51;
            long var55 = var53;
            long var57 = var55;
            long var59 = var57;
            long var61 = var59;
            long var63 = var61;
            boolean var65 = FilenameUtils.isExtension(var48, "pscr");
            long var66 = var63;
            long var68 = var66;
            long var70 = var68;
            label186:
            if (var65) {
               long var72 = var70;
               long var76 = var72;
               long var78 = var76;
               long var80 = var78;
               long var82 = var80;
               long var84 = var82;
               long var86 = var84;
               String var88 = ((File)var33).getName();
               long var89 = var86;
               long var91 = var89;
               long var93 = var91;
               long var95 = var93;
               long var97 = var95;
               long var99 = var97;
               long var101 = var99;
               long var103 = var101;
               long var105 = var103;
               long var107 = var105;
               String[] var109 = var88.split("\\.");
               long var110 = var107;
               long var112 = var110;
               long var114 = var112;
               long var116 = var114;
               long var118 = var116;
               int var120 = var109.length;
               long var121 = var118;
               long var123 = var121;
               long var146;
               Object var148;
               if (var120 <= 0) {
                  long var125 = var123;
                  long var131 = var125;
                  long var133 = var131;
                  long var135 = var133;
                  long var137 = var135;
                  long var139 = var137;
                  long var141 = var139;
                  String var143 = ((File)var33).getName();
                  long var144 = var141;
                  var146 = var144;
                  var148 = var143;
               } else {
                  long var127 = var123;
                  long var194 = var127;
                  long var196 = var194;
                  long var198 = var196;
                  long var200 = var198;
                  int var202 = var109.length;
                  long var203 = var200;
                  long var205 = var203;
                  long var207 = var205;
                  long var209 = var207;
                  long var211 = var209;
                  long var213 = var211;
                  Object var215 = var109[var202 + -2];
                  var146 = var213;
                  var148 = var215;
               }

               long var149 = var146;
               long var151 = var149;
               long var153 = var151;
               long var155 = var153;
               long var157 = var155;
               long var159 = var157;
               long var161 = var159;
               long var163 = var161;
               long var165 = var163;

               long var572;
               label214: {
                  Throwable var220;
                  label181: {
                     Throwable var216;
                     label180: {
                        Throwable var418;
                        label197: {
                           label205: {
                              Path var167;
                              try {
                                 var167 = ((File)var33).toPath();
                              } catch (Throwable var623) {
                                 var216 = var623;
                                 boolean var217 = var623 instanceof IOException;
                                 if (!var217) {
                                    break label180;
                                 }
                                 break label205;
                              }

                              long var168 = var163;
                              long var170 = var168;
                              long var172 = var170;
                              long var174 = var172;
                              long var176 = var174;
                              var165 = var176;

                              Stream var178;
                              try {
                                 var178 = Files.lines(var167);
                              } catch (Throwable var622) {
                                 var216 = var622;
                                 boolean var627 = var622 instanceof IOException;
                                 if (!var627) {
                                    break label180;
                                 }
                                 break label205;
                              }

                              long var179 = var176;
                              long var181 = var179;
                              long var183 = var181;
                              long var185 = var183;
                              long var187 = var185;
                              long var192;
                              if (var189 == null) {
                                 long var190 = var187;
                                 long var221 = var190;
                                 long var223 = var221;
                                 long var225 = var223;
                                 long var227 = var225;
                                 long var229 = var227;
                                 long var231 = var229;
                                 long var233 = var231;
                                 long var235 = var233;
                                 Lookup var237 = MethodHandles.lookup();
                                 long var238 = var235;
                                 long var240 = var238;
                                 long var242 = var240;
                                 long var244 = var242;
                                 long var246 = var244;
                                 long var248 = var246;
                                 long var250 = var248;
                                 Class<?> var252 = this.getClass();
                                 ClassLoader var253 = var252.getClassLoader();
                                 MethodType var254 = MethodType.fromMethodDescriptorString("()Ljava/util/function/IntFunction;", var253);
                                 long var255 = var250;
                                 long var257 = var255;
                                 long var259 = var257;
                                 long var261 = var259;
                                 long var263 = var261;
                                 Class<?> var265 = this.getClass();
                                 ClassLoader var266 = var265.getClassLoader();
                                 MethodType var267 = MethodType.fromMethodDescriptorString("(I)Ljava/lang/Object;", var266);
                                 long var268 = var263;
                                 long var270 = var268;
                                 long var272 = var270;
                                 long var274 = var272;
                                 long var276 = var274;
                                 long var278 = var276;
                                 Class<?> var280 = this.getClass();
                                 ClassLoader var281 = var280.getClassLoader();
                                 MethodType var282 = MethodType.fromMethodDescriptorString("(I)[Ljava/lang/String;", var281);
                                 long var283 = var278;
                                 long var285 = var283;
                                 long var287 = var285;
                                 long var289 = var287;
                                 long var291 = var289;
                                 MethodHandle var293 = var237.findStatic(ScriptManager.class, "values", var282);
                                 long var294 = var291;
                                 long var296 = var294;
                                 long var298 = var296;
                                 long var300 = var298;
                                 long var302 = var300;
                                 Class<?> var304 = this.getClass();
                                 ClassLoader var305 = var304.getClassLoader();
                                 MethodType var306 = MethodType.fromMethodDescriptorString("(I)[Ljava/lang/String;", var305);
                                 long var307 = var302;
                                 long var309 = var307;
                                 long var311 = var309;
                                 long var313 = var311;
                                 Object[] var10000 = new Object[0];
                                 long var315 = var313;
                                 long var317 = var315;
                                 long var319 = var317;
                                 var189 = LambdaMetafactory.metafactory(var237, "apply", var254, var267, var293, var306);
                                 long var321 = var319;
                                 long var323 = var321;
                                 long var325 = var323;
                                 var192 = var325;
                              } else {
                                 var192 = var187;
                              }

                              long var327 = var192;
                              long var329 = var327;
                              long var331 = var329;
                              long var333 = var331;
                              MethodHandle var335 = var189.getTarget();
                              long var336 = var333;
                              long var338 = var336;
                              long var340 = var338;
                              long var342 = var340;
                              long var344 = var342;
                              long var346 = var344;
                              long var348 = var346;
                              long var350 = var348;
                              long var352 = var350;
                              long var354 = var352;
                              Object[] var10001 = phobos$g1806d10b0;
                              if (phobos$g1806d10b0 == null) {
                                 var10001 = phobos$g1806d10b0 = new Object[0];
                              }

                              Object var356 = var335.invokeWithArguments(var10001);
                              long var357 = var354;
                              long var359 = var357;
                              long var361 = var359;
                              long var363 = var361;
                              long var365 = var363;
                              long var367 = var365;
                              long var369 = var367;
                              long var371 = var369;
                              long var373 = var371;

                              long var450;
                              label206: {
                                 label200: {
                                    Object[] var375;
                                    try {
                                       var375 = var178.toArray((IntFunction<Object[]>)var356);
                                    } catch (Throwable var626) {
                                       var418 = var626;
                                       boolean var419 = true;
                                       if (!var419) {
                                          break label197;
                                       }
                                       break label200;
                                    }

                                    long var376 = var371;
                                    long var378 = var376;
                                    long var380 = var378;
                                    boolean var382 = var375 instanceof String[];
                                    if (var375 != null && !var382) {
                                       throw new ClassCastException("CHECKCAST failed");
                                    }

                                    long var385 = var380;
                                    long var424 = var385;
                                    long var426 = var424;
                                    long var428 = var426;
                                    long var430 = var428;
                                    long var432 = var430;
                                    long var434 = var432;
                                    long var436 = var434;
                                    long var438 = var436;
                                    long var440 = var438;
                                    long var442 = var440;
                                    long var444 = var442;
                                    long var446 = var444;
                                    long var448 = var446;
                                    var450 = var448;
                                    var373 = var450;

                                    try {
                                       this.handleText3((String)var148, (String[])var375, (File)var33);
                                       break label206;
                                    } catch (Throwable var625) {
                                       var418 = var625;
                                       boolean var631 = true;
                                       if (!var631) {
                                          break label197;
                                       }
                                    }
                                 }

                                 long var420 = var373;
                                 long var550 = var420;
                                 long var552 = var550;
                                 long var554 = var552;
                                 long var556 = var554;
                                 if (var178 != null) {
                                    long var558 = var556;
                                    long var593 = var558;
                                    long var595 = var593;
                                    long var597 = var595;
                                    long var599 = var597;
                                    long var601 = var599;

                                    try {
                                       var178.close();
                                    } catch (Throwable var624) {
                                       Throwable var607 = var624;
                                       boolean var608 = true;
                                       if (!var608) {
                                          throw var624;
                                       }

                                       long var609 = var601;
                                       long var611 = var609;
                                       long var613 = var611;
                                       long var615 = var613;
                                       long var617 = var615;
                                       var165 = var617;

                                       try {
                                          var418.addSuppressed(var607);
                                       } catch (Throwable var621) {
                                          var216 = var621;
                                          boolean var628 = var621 instanceof IOException;
                                          if (!var628) {
                                             break label180;
                                          }
                                          break label205;
                                       }
                                    }
                                 }

                                 var165 = var574;

                                 try {
                                    throw var418;
                                 } catch (Throwable var619) {
                                    var216 = var619;
                                    boolean var629 = var619 instanceof IOException;
                                    if (!var629) {
                                       break label180;
                                    }
                                    break label205;
                                 }
                              }

                              long var452 = var450;
                              long var454 = var452;
                              long var456 = var454;
                              if (var178 == null) {
                                 var74 = var456;
                                 break label186;
                              }

                              long var458 = var456;
                              long var562 = var458;
                              long var564 = var562;
                              long var566 = var564;
                              long var568 = var566;
                              long var570 = var568;
                              var572 = var570;
                              var165 = var574;

                              try {
                                 var178.close();
                                 break label214;
                              } catch (Throwable var620) {
                                 var216 = var620;
                                 boolean var630 = var620 instanceof IOException;
                                 if (!var630) {
                                    break label180;
                                 }
                              }
                           }

                           long var218 = var165;
                           long var387 = var218;
                           long var389 = var387;
                           long var391 = var389;
                           long var393 = var391;
                           long var395 = var393;
                           PhobosClient var397 = this.phobosClient;
                           long var398 = var395;
                           long var400 = var398;
                           long var402 = var400;
                           long var404 = var402;
                           long var406 = var404;
                           long var408 = var406;
                           long var410 = var408;
                           long var415;
                           if (var412 == null) {
                              long var413 = var410;
                              long var460 = var413;
                              long var462 = var460;
                              long var464 = var462;
                              long var466 = var464;
                              long var468 = var466;
                              long var470 = var468;
                              long var472 = var470;
                              long var474 = var472;
                              Lookup var476 = MethodHandles.lookup();
                              long var477 = var474;
                              long var479 = var477;
                              long var481 = var479;
                              long var483 = var481;
                              long var485 = var483;
                              long var487 = var485;
                              long var489 = var487;
                              Class<?> var491 = this.getClass();
                              ClassLoader var492 = var491.getClassLoader();
                              MethodType var493 = MethodType.fromMethodDescriptorString("(Ljava/lang/String;)Ljava/lang/String;", var492);
                              long var494 = var489;
                              long var496 = var494;
                              long var498 = var496;
                              long var500 = var498;
                              long var502 = var500;
                              Object[] var504 = new Object[0];
                              long var505 = var502;
                              long var507 = var505;
                              var412 = StringConcatFactory.makeConcatWithConstants(var476, "makeConcatWithConstants", var493, "Failed to load \u0001", var504);
                              long var509 = var507;
                              long var511 = var509;
                              long var513 = var511;
                              long var515 = var513;
                              var415 = var515;
                           } else {
                              var415 = var410;
                           }

                           long var517 = var415;
                           long var519 = var517;
                           long var521 = var519;
                           long var523 = var521;
                           MethodHandle var525 = var412.getTarget();
                           long var526 = var523;
                           long var528 = var526;
                           long var530 = var528;
                           long var532 = var530;
                           long var534 = var532;
                           long var536 = var534;
                           long var538 = var536;
                           long var540 = var538;
                           long var542 = var540;
                           long var544 = var542;
                           long var548;
                           if (var148 != null) {
                              long var546 = var544;
                              Object[] var632 = phobos$g1806d11c8;
                              if (phobos$g1806d11c8 == null) {
                                 var632 = phobos$g1806d11c8 = new Object[1];
                              }

                              var632[0] = var148;
                              var548 = var546;
                           } else {
                              var548 = var544;
                           }

                           Object[] var634 = phobos$g1806d11c8;
                           if (phobos$g1806d11c8 == null) {
                              var634 = phobos$g1806d11c8 = new Object[1];
                           }

                           Object var576 = var525.invokeWithArguments(var634);
                           long var577 = var548;
                           long var579 = var577;
                           long var581 = var579;
                           long var583 = var581;
                           long var585 = var583;
                           long var587 = var585;
                           long var589 = var587;
                           long var591 = var589;
                           var397.handleText5("Scripts", (String)var576);
                           var129 = var591;
                           break label204;
                        }

                        var220 = var418;
                        break label181;
                     }

                     var220 = var216;
                  }

                  Throwable var417 = var220;
                  throw var417;
               }

               var574 = var572;
               var74 = var574;
            } else {
               var74 = var70;
            }

            var129 = var74;
         }

         long var633 = (int)var129 + 1 & 4294967295L;
         var13 = var129;
         var11 = var633;
      }
   }

   public void handleScript3(Script script) {
      PhobosClient var2 = this.phobosClient;
      String var3 = script.getName();
      String var11 = "Unloading " + var3;
      var2.handleText5("Scripts", var11);
      script.run8();
      List var12 = this.scriptList;
      var12.remove(script);
      PhobosClient var13 = this.phobosClient;
      EventBus var14 = var13.getEventBus();
      var14.handleObject4(script);
   }

   public ScriptManager(PhobosClient phobosClient) {
      this.phobosClient = phobosClient;
      this.handleListener(new Listener<RunEvent>() {
         public void handleObject2(RunEvent runEvent) {
            for (Script script : ScriptManager.this.scriptList) {
               ScriptManager.this.handleScript3(script);
            }
         }
      });
      this.handleListener(new Listener<PhobosClient.InitEvent>() {
         public void handleObject2(PhobosClient.InitEvent initEvent) {
            for (Script script : ScriptManager.this.scriptList2) {
               ScriptScreen.scriptScreen.handleScript2(script);
            }

            ScriptManager.this.flag2 = true;
         }
      });
      this.run();
   }

   public Optional<Script> resolveOptional(String text) {
      for (Script script : this.scriptList2) {
         if (script.getName().equalsIgnoreCase(text)) {
            return Optional.of(script);
         }
      }

      return Optional.empty();
   }

   public static void handleClassValue(Class<? extends RuntimeException> classValue, UnknownTypeUnknownMethodEnum unknownTypeUnknownMethodEnum) {
      Map var2 = unknownTypeUnknownMethodEnumMap;
      var2.put(unknownTypeUnknownMethodEnum, classValue);
      Map var3 = classValueMap;
      var3.put(classValue, unknownTypeUnknownMethodEnum);
   }

   public static UnknownTypeUnknownMethodEnum resolveUnknownTypeUnknownMethodEnum(Class<? extends RuntimeException> classValue) {
      return classValueMap.get(classValue);
   }

   public void run2() {
      this.phobosClient
         .getCloudConfigApi()
         .handleConsumer(
            list -> {
               CallSite var14 = null;
               ScriptScreen var2 = ScriptScreen.scriptScreen;
               var2.clear();
               flag = true & true;
               intValue2 = 0;
               int var3 = list.size();
               intValue = var3;
               Iterator var4 = list.iterator();

               while (true) {
                  boolean var5 = var4.hasNext();
                  if (!var5) {
                     return;
                  }

                  Object var6 = var4.next();
                  boolean var7 = var6 instanceof CloudConfigRef;
                  if (var6 != null && !var7) {
                     throw new ClassCastException("CHECKCAST failed");
                  }

                  String var8 = ((CloudConfigRef)var6).getName();
                  String var9 = ((CloudConfigRef)var6).getText();
                  PUBLICPRIVATEEnum var10 = ((CloudConfigRef)var6).getPUBLICPRIVATEEnum();
                  PhobosClient var11 = this.phobosClient;
                  CloudConfigApi var12 = var11.getCloudConfigApi();
                  ConfigScriptEnum var13 = ConfigScriptEnum.Script;
                  Consumer<String> var30 = string -> this.handleText(var8, var10, var9, string);
                  Runnable[] var31 = new Runnable[0];
                  var12.resolveCompletableFuture(var8, var13, (Consumer<String>)var30, var31);
               }
            }
         );
   }

   public void handleText(String text, PUBLICPRIVATEEnum pUBLICPRIVATEEnum, String text2, String text3) {
      this.handleText2(text, text3.split("\n"), null, pUBLICPRIVATEEnum, script -> {
         intValue2++;
         script.setText(text2);
         ScriptScreen.scriptScreen.handleScript2(script);
         if (intValue2 >= intValue) {
            flag = false;
         }
      });
   }

   @Override
   public void handleJsonElement(JsonElement jsonElement) {
      for (JsonElement jsonElement2 : jsonElement.getAsJsonArray()) {
         this.scriptList2
            .stream()
            .filter(script -> script.getName().equals(jsonElement2.getAsString()))
            .findFirst()
            .ifPresent(this::handleScript4);
      }
   }

   @NotNull
   @Override
   public Iterator<Script> iterator() {
      return this.getScriptList2().iterator();
   }

   public List<Script> getScriptList2() {
      return this.scriptList2;
   }

   public void handleText2(String text, String[] texts, File file, PUBLICPRIVATEEnum pUBLICPRIVATEEnum, Consumer<Script> consumer) {
      PhobosClient var7 = this.phobosClient;
      String var8 = String.join("\n", texts);
      Script var6 = new Script(var7, text, var8, file, pUBLICPRIVATEEnum);
      var6.handleString(texts);
      consumer.accept(var6);
   }

   public void handleScript4(Script script) {
      PhobosClient var2 = this.phobosClient;
      String var3 = script.getName();
      String var11 = "Loading " + var3;
      var2.handleText5("Scripts", var11);
      script.run();
      List var12 = this.scriptList;
      var12.add(script);
      PhobosClient var13 = this.phobosClient;
      EventBus var14 = var13.getEventBus();
      var14.handleObject(script);
   }

   public static void run9() {
      ClientHolder.getPhobosClient().getScriptManager().run2();
   }

   public void handleText3(String text, String[] texts, File file) {
      PUBLICPRIVATEEnum var4 = PUBLICPRIVATEEnum.PUBLIC;
      Consumer<Script> var21 = this::handleScript;
      this.handleText2(text, texts, file, var4, var21);
   }

   static {
      handleClassValue(ScriptManager.UnknownType.class, UnknownTypeUnknownMethodEnum.UnknownType);
      handleClassValue(ScriptManager.UnknownMethod.class, UnknownTypeUnknownMethodEnum.UnknownMethod);
      handleClassValue(ScriptManager.UnknownVar.class, UnknownTypeUnknownMethodEnum.UnknownVar);
      handleClassValue(ScriptManager.VariableIsImmutable.class, UnknownTypeUnknownMethodEnum.VariableIsImmutable);
      handleClassValue(ScriptManager.UnknownArg.class, UnknownTypeUnknownMethodEnum.UnknownArg);
      handleClassValue(ScriptManager.MethodPipelineException.class, UnknownTypeUnknownMethodEnum.MethodPipelineException);
   }

   public static UnknownTypeUnknownMethodEnum resolveUnknownTypeUnknownMethodEnum2(UnknownTypeUnknownMethodEnum unknownTypeUnknownMethodEnum) {
      return unknownTypeUnknownMethodEnumMap.keySet()
         .stream()
         .filter(unknownTypeUnknownMethodEnum2 -> unknownTypeUnknownMethodEnum2.equals(unknownTypeUnknownMethodEnum))
         .findFirst()
         .orElse(null);
   }

   @Override
   public JsonElement createJsonPrimitive() {
      JsonArray jsonArray = new JsonArray();

      for (Script script : this.scriptList) {
         if (!script.isFlag2()) {
            jsonArray.add(script.getName());
         }
      }

      return jsonArray;
   }

   public static class MethodPipelineException extends RuntimeException {
      public MethodPipelineException() {
         super("Unknown method");
      }
   }

   public static class UnknownArg extends RuntimeException {
      public UnknownArg() {
         super("Unknown argument");
      }
   }

   public static class UnknownMethod extends RuntimeException {
      public UnknownMethod() {
         super("Unknown method");
      }
   }

   public static class UnknownType extends RuntimeException {
      public UnknownType() {
         super("Unknown type");
      }
   }

   public static class UnknownVar extends RuntimeException {
      public UnknownVar() {
         super("Unknown variable");
      }
   }

   public static class VariableIsImmutable extends RuntimeException {
      public VariableIsImmutable() {
         super("Variable is immutable");
      }
   }
}
