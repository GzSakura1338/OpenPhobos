package org.phobos.core.loader;

import lombok.SneakyThrows;
import java.lang.invoke.StringConcatFactory;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Base64.Decoder;
import java.util.function.Consumer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.io.FilenameUtils;
import org.phobos.api.LoadableModule;
import org.phobos.module.Module;
import org.phobos.core.module.ModuleManager;
import org.phobos.core.PhobosClient;
import org.phobos.core.PhobosLog;
import org.phobos.core.crypto.StringCipher;

public class OptionalModuleLoader {
   public static final Path path = FabricLoader.getInstance().getGameDir().resolve("phoboslite");
   private static Object[] phobos$g1806c9bc0;
   private static Object[] phobos$g1806c9f08;
   private static Object[] phobos$g1806c9fc0;
   private List list;

   @SneakyThrows public void handlePhobosClient(PhobosClient phobosClient, ModuleManager moduleManager) {
      CallSite var25 = null;
      PhobosLog.handleText("OML", "Initializing OML");
      ArrayList var3 = new ArrayList();
      List var4 = moduleManager.getList();
      Iterator var5 = var4.iterator();

      while (true) {
         boolean var6 = var5.hasNext();
         if (!var6) {
            List var9 = this.getList();
            Iterator var10 = var9.iterator();

            while (true) {
               boolean var11 = var10.hasNext();
               if (!var11) {
                  List var15 = this.getList();
                  Iterator var16 = var15.iterator();

                  while (true) {
                     boolean var17 = var16.hasNext();
                     if (!var17) {
                        return;
                     }

                     Object var18 = var16.next();
                     boolean var19 = var18 instanceof LoadableModule;
                     if (var18 != null && !var19) {
                        throw new ClassCastException("CHECKCAST failed");
                     }

                     String var21 = ((LoadableModule)var18).getName();
                     boolean var22 = ((LoadableModule)var18).testList(var3);
                     if (!var22) {
                        List var23 = ((LoadableModule)var18).getTextList();
                        String var24 = String.valueOf(var23);
                        if (var25 == null) {
                           Lookup var26 = MethodHandles.lookup();
                           Class<?> var27 = this.getClass();
                           ClassLoader var28 = var27.getClassLoader();
                           MethodType var29 = MethodType.fromMethodDescriptorString("(Ljava/lang/String;)Ljava/lang/String;", var28);
                           Object[] var30 = new Object[0];
                           var25 = StringConcatFactory.makeConcatWithConstants(var26, "makeConcatWithConstants", var29, "Failed to load, not all dependencies are installed: \u0001", var30);
                        }

                        MethodHandle var31 = var25.getTarget();
                        if (var24 != null) {
                           Object[] var10000 = phobos$g1806c9bc0;
                           if (phobos$g1806c9bc0 == null) {
                              var10000 = phobos$g1806c9bc0 = new Object[1];
                           }

                           var10000[0] = var24;
                        }

                        Object[] var10001 = phobos$g1806c9bc0;
                        if (phobos$g1806c9bc0 == null) {
                           var10001 = phobos$g1806c9bc0 = new Object[1];
                        }

                        Object var32 = var31.invokeWithArguments(var10001);
                        PhobosLog.handleText("OML", (String)var32);
                     }

                     Optional var33 = moduleManager.resolveOptional3(var21);
                     boolean var34 = var33.isPresent();
                     if (!var34) {
                        Throwable var41;
                        label111: {
                           Class var35;
                           try {
                              var35 = ((LoadableModule)var18).getClassValue();
                           } catch (Throwable var47) {
                              var41 = var47;
                              break label111;
                           }

                           Class[] var36 = new Class[1];
                           var36[0] = PhobosClient.class;

                           Constructor var37;
                           try {
                              var37 = var35.getConstructor(var36);
                           } catch (Throwable var46) {
                              var41 = var46;
                              break label111;
                           }

                           Object[] var38 = new Object[1];
                           var38[0] = phobosClient;

                           Object var39;
                           try {
                              var39 = var37.newInstance(var38);
                           } catch (Throwable var45) {
                              var41 = var45;
                              break label111;
                           }

                           boolean var40 = var39 instanceof Module;
                           if (var39 != null && !var40) {
                              throw new ClassCastException("CHECKCAST failed");
                           }

                           try {
                              moduleManager.handleModule2((Module)var39);
                              continue;
                           } catch (Throwable var44) {
                              var41 = var44;
                           }
                        }

                        boolean var42 = var41 instanceof InvocationTargetException;
                        if (!var42) {
                           throw var41;
                        }

                        PhobosLog.handleText("OML", "Failed to load due to reflection");
                        Throwable var43 = ((InvocationTargetException)var41).getCause();
                        var43.printStackTrace();
                     }
                  }
               }

               Object var12 = var10.next();
               boolean var13 = var12 instanceof LoadableModule;
               if (var12 != null && !var13) {
                  throw new ClassCastException("CHECKCAST failed");
               }

               String var20 = ((LoadableModule)var12).getName();
               var3.add(var20);
            }
         }

         Object var7 = var5.next();
         boolean var8 = var7 instanceof Module;
         if (var7 != null && !var8) {
            throw new ClassCastException("CHECKCAST failed");
         }

         String var14 = ((Module)var7).getName();
         var3.add(var14);
      }
   }

   @SneakyThrows public void run() {
      CallSite var374 = null;
      CallSite var167 = null;
      long var9 = 0L;
      PhobosLog.handleText("OML", "Reading optional modules...");
      Path var1 = path;
      Path var2 = var1.resolve("modules");
      File var3 = var2.toFile();
      boolean var4 = var3.exists();
      if (!var4) {
         var3.mkdirs();
      } else {
         File[] var5 = var3.listFiles();
         if (var5 != null) {
            int var6 = var5.length;
            long var7 = 0L;

            while (true) {
               long var11 = (int)var7 & 4294967295L | var9 & -4294967296L;
               long var13 = var11;
               long var15 = var13;
               long var17 = var15;
               if ((int)((int)var7 & 4294967295L) >= var6) {
                  break;
               }

               long var127;
               label99: {
                  long var19 = var17;
                  long var21 = var19;
                  long var23 = var21;
                  long var25 = var23;
                  long var27 = var25;
                  Object var29 = var5[(int)((int)var7 & 4294967295L)];
                  long var30 = var27;
                  long var32 = var30;
                  long var34 = var32;
                  long var36 = var34;
                  long var38 = var36;
                  long var40 = var38;
                  long var42 = var40;
                  long var44 = var42;
                  String var46 = ((File)var29).getPath();
                  long var47 = var44;
                  long var49 = var47;
                  long var51 = var49;
                  long var53 = var51;
                  long var55 = var53;
                  long var57 = var55;
                  long var59 = var57;
                  long var61 = var59;
                  boolean var63 = FilenameUtils.isExtension(var46, "spom");
                  long var64 = var61;
                  long var66 = var64;
                  long var68 = var66;
                  long var72;
                  if (var63) {
                     long var70 = var68;
                     long var74 = var70;
                     long var76 = var74;
                     long var78 = var76;
                     long var80 = var78;
                     long var82 = var80;
                     long var84 = var82;
                     String var86 = ((File)var29).getName();
                     long var87 = var84;
                     long var89 = var87;
                     long var91 = var89;
                     long var93 = var91;
                     long var95 = var93;
                     long var97 = var95;
                     long var99 = var97;
                     long var101 = var99;
                     long var103 = var101;
                     long var105 = var103;
                     String[] var107 = var86.split("\\.");
                     long var108 = var105;
                     long var110 = var108;
                     long var112 = var110;
                     long var114 = var112;
                     long var116 = var114;
                     int var118 = var107.length;
                     long var119 = var116;
                     long var121 = var119;
                     long var144;
                     Object var146;
                     if (var118 <= 0) {
                        long var123 = var121;
                        long var129 = var123;
                        long var131 = var129;
                        long var133 = var131;
                        long var135 = var133;
                        long var137 = var135;
                        long var139 = var137;
                        String var141 = ((File)var29).getName();
                        long var142 = var139;
                        var144 = var142;
                        var146 = var141;
                     } else {
                        long var125 = var121;
                        long var172 = var125;
                        long var174 = var172;
                        long var176 = var174;
                        long var178 = var176;
                        int var180 = var107.length;
                        long var181 = var178;
                        long var183 = var181;
                        long var185 = var183;
                        long var187 = var185;
                        long var189 = var187;
                        long var191 = var189;
                        Object var193 = var107[var180 + -2];
                        var144 = var191;
                        var146 = var193;
                     }

                     long var147 = var144;
                     long var149 = var147;
                     long var151 = var149;
                     long var153 = var151;
                     long var155 = var153;
                     long var157 = var155;
                     long var159 = var157;
                     long var161 = var159;
                     long var163 = var161;
                     long var165 = var163;
                     long var170;
                     if (var167 == null) {
                        long var168 = var165;
                        long var194 = var168;
                        long var196 = var194;
                        long var198 = var196;
                        long var200 = var198;
                        long var202 = var200;
                        long var204 = var202;
                        long var206 = var204;
                        long var208 = var206;
                        Lookup var210 = MethodHandles.lookup();
                        long var211 = var208;
                        long var213 = var211;
                        long var215 = var213;
                        long var217 = var215;
                        long var219 = var217;
                        long var221 = var219;
                        long var223 = var221;
                        Class<?> var225 = this.getClass();
                        ClassLoader var226 = var225.getClassLoader();
                        MethodType var227 = MethodType.fromMethodDescriptorString(
                           "(Lorg/phobos/core/loader/OptionalModuleLoader;Ljava/lang/String;)Ljava/util/function/Consumer;", var226
                        );
                        long var228 = var223;
                        long var230 = var228;
                        long var232 = var230;
                        long var234 = var232;
                        long var236 = var234;
                        Class<?> var238 = this.getClass();
                        ClassLoader var239 = var238.getClassLoader();
                        MethodType var240 = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", var239);
                        long var241 = var236;
                        long var243 = var241;
                        long var245 = var243;
                        long var247 = var245;
                        long var249 = var247;
                        long var251 = var249;
                        Class<?> var253 = this.getClass();
                        ClassLoader var254 = var253.getClassLoader();
                        MethodType var255 = MethodType.fromMethodDescriptorString("(Ljava/lang/String;Lorg/phobos/api/LoadableModule;)V", var254);
                        long var256 = var251;
                        long var258 = var256;
                        long var260 = var258;
                        long var262 = var260;
                        long var264 = var262;
                        MethodHandle var266 = var210.findVirtual(OptionalModuleLoader.class, "handleText", var255);
                        long var267 = var264;
                        long var269 = var267;
                        long var271 = var269;
                        long var273 = var271;
                        long var275 = var273;
                        Class<?> var277 = this.getClass();
                        ClassLoader var278 = var277.getClassLoader();
                        MethodType var279 = MethodType.fromMethodDescriptorString("(Lorg/phobos/api/LoadableModule;)V", var278);
                        long var280 = var275;
                        long var282 = var280;
                        long var284 = var282;
                        long var286 = var284;
                        Object[] var10000 = new Object[0];
                        long var288 = var286;
                        long var290 = var288;
                        var167 = LambdaMetafactory.metafactory(var210, "accept", var227, var240, var266, var279);
                        long var292 = var290;
                        long var294 = var292;
                        long var296 = var294;
                        long var298 = var296;
                        var170 = var298;
                     } else {
                        var170 = var165;
                     }

                     long var300 = var170;
                     long var302 = var300;
                     long var304 = var302;
                     long var306 = var304;
                     MethodHandle var308 = var167.getTarget();
                     long var309 = var306;
                     long var311 = var309;
                     long var313 = var311;
                     long var315 = var313;
                     long var317 = var315;
                     long var319 = var317;
                     long var321 = var319;
                     long var323 = var321;
                     long var325 = var323;
                     long var327 = var325;
                     long var331;
                     if (this != null) {
                        long var329 = var327;
                        Object[] var497 = phobos$g1806c9f08;
                        if (phobos$g1806c9f08 == null) {
                           var497 = phobos$g1806c9f08 = new Object[2];
                        }

                        var497[0] = this;
                        var331 = var329;
                     } else {
                        var331 = var327;
                     }

                     long var335;
                     if (var146 != null) {
                        long var333 = var331;
                        Object[] var498 = phobos$g1806c9f08;
                        if (phobos$g1806c9f08 == null) {
                           var498 = phobos$g1806c9f08 = new Object[2];
                        }

                        var498[1] = var146;
                        var335 = var333;
                     } else {
                        var335 = var331;
                     }

                     Object[] var10001 = phobos$g1806c9f08;
                     if (phobos$g1806c9f08 == null) {
                        var10001 = phobos$g1806c9f08 = new Object[2];
                     }

                     Object var337 = var308.invokeWithArguments(var10001);
                     long var338 = var335;
                     long var340 = var338;
                     long var342 = var340;
                     long var344 = var342;
                     long var346 = var344;
                     long var348 = var346;
                     long var350 = var348;
                     long var352 = var350;
                     long var354 = var352;

                     try {
                        this.handleFile((File)var29, (Consumer<LoadableModule>)var337);
                     } catch (Throwable var496) {
                        boolean var357 = true;
                        if (var357) {
                           long var358 = var354;
                           long var360 = var358;
                           long var362 = var360;
                           long var364 = var362;
                           long var366 = var364;
                           long var368 = var366;
                           long var370 = var368;
                           long var372 = var370;
                           long var377;
                           if (var374 == null) {
                              long var375 = var372;
                              long var379 = var375;
                              long var381 = var379;
                              long var383 = var381;
                              long var385 = var383;
                              long var387 = var385;
                              long var389 = var387;
                              long var391 = var389;
                              long var393 = var391;
                              Lookup var395 = MethodHandles.lookup();
                              long var396 = var393;
                              long var398 = var396;
                              long var400 = var398;
                              long var402 = var400;
                              long var404 = var402;
                              long var406 = var404;
                              long var408 = var406;
                              Class<?> var410 = this.getClass();
                              ClassLoader var411 = var410.getClassLoader();
                              MethodType var412 = MethodType.fromMethodDescriptorString("(Ljava/lang/String;)Ljava/lang/String;", var411);
                              long var413 = var408;
                              long var415 = var413;
                              long var417 = var415;
                              long var419 = var417;
                              long var421 = var419;
                              Object[] var423 = new Object[0];
                              long var424 = var421;
                              long var426 = var424;
                              var374 = StringConcatFactory.makeConcatWithConstants(var395, "makeConcatWithConstants", var412, "Failed to load \u0001", var423);
                              long var428 = var426;
                              long var430 = var428;
                              long var432 = var430;
                              long var434 = var432;
                              var377 = var434;
                           } else {
                              var377 = var372;
                           }

                           long var436 = var377;
                           long var438 = var436;
                           long var440 = var438;
                           long var442 = var440;
                           MethodHandle var444 = var374.getTarget();
                           long var445 = var442;
                           long var447 = var445;
                           long var449 = var447;
                           long var451 = var449;
                           long var453 = var451;
                           long var455 = var453;
                           long var457 = var455;
                           long var459 = var457;
                           long var461 = var459;
                           long var463 = var461;
                           long var467;
                           if (var146 != null) {
                              long var465 = var463;
                              Object[] var499 = phobos$g1806c9fc0;
                              if (phobos$g1806c9fc0 == null) {
                                 var499 = phobos$g1806c9fc0 = new Object[1];
                              }

                              var499[0] = var146;
                              var467 = var465;
                           } else {
                              var467 = var463;
                           }

                           var10001 = phobos$g1806c9fc0;
                           if (phobos$g1806c9fc0 == null) {
                              var10001 = phobos$g1806c9fc0 = new Object[1];
                           }

                           Object var469 = var444.invokeWithArguments(var10001);
                           long var470 = var467;
                           long var472 = var470;
                           long var474 = var472;
                           long var476 = var474;
                           long var478 = var476;
                           long var480 = var478;
                           PhobosLog.handleText("OML", (String)var469);
                           long var482 = var480;
                           long var484 = var482;
                           long var486 = var484;
                           long var488 = var486;
                           long var490 = var488;
                           long var492 = var490;
                           long var494 = var492;
                           var496.printStackTrace();
                           var127 = var494;
                           break label99;
                        }

                        throw var496;
                     }

                     var72 = var352;
                  } else {
                     var72 = var68;
                  }

                  var127 = var72;
               }

               long var500 = (int)var127 + 1 & 4294967295L;
               var9 = var127;
               var7 = var500;
            }
         }
      }
   }

   public void handleText(String text, LoadableModule loadableModule) {
      PhobosLog.handleText("OML", "Loading optional: " + text);
      this.getList().add(loadableModule);
   }

   public void handleFile(File file, Consumer<LoadableModule> consumer) throws Exception {
      Path var3 = file.toPath();
      String var4 = Files.readString(var3);
      String var5 = StringCipher.describeText2(var4);
      JsonElement var6 = JsonParser.parseString(var5);
      boolean var7 = var6.isJsonObject();
      if (var7) {
         JsonObject var8 = var6.getAsJsonObject();
         JsonElement var9 = var8.get("Name");
         String var10 = var9.getAsString();
         JsonElement var11 = var8.get("Dependencies");
         JsonArray var12 = var11.getAsJsonArray();
         ArrayList var13 = new ArrayList();
         Iterator var14 = var12.iterator();

         while (true) {
            boolean var15 = var14.hasNext();
            if (!var15) {
               JsonElement var18 = var8.get("Data");
               String var19 = var18.getAsString();
               Decoder var20 = Base64.getDecoder();
               byte[] var21 = var20.decode(var19);
               ModuleClassData var22 = new ModuleClassData(var10, var21, var13);
               consumer.accept(var22);
               break;
            }

            Object var16 = var14.next();
            boolean var17 = var16 instanceof JsonElement;
            if (var16 != null && !var17) {
               throw new ClassCastException("CHECKCAST failed");
            }

            String var23 = ((JsonElement)var16).getAsString();
            var13.add(var23);
         }
      }
   }

   public OptionalModuleLoader() {
      this.setList(new ArrayList());
   }

   public List getList() {
      return this.list;
   }

   public void setList(List var1) {
      this.list = var1;
   }
}
