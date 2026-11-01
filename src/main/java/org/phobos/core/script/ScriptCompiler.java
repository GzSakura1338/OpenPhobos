package org.phobos.core.script;

import lombok.SneakyThrows;
import java.lang.invoke.StringConcatFactory;
import java.io.Serializable;
import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles.Lookup;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.phobos.api.ScriptObject;
import org.phobos.core.script.Script.Arg;
import org.phobos.core.script.Script.Var;
import org.phobos.type.API10API11Enum;
import org.phobos.core.PhobosClient;

public class ScriptCompiler {
   private static Object[] phobos$g1806fd1e8;
   private static Object[] phobos$g1806fd7b0;
   private static Object[] phobos$g1806fd890;
   private static Object[] phobos$g1806fdcb0;
   private static Object[] phobos$g1806fdd80;
   private static Object[] phobos$g1806fd420;

   public static Class<?> resolveClassValue(String text) {
      long var2;
      label184: {
         long var16;
         label183: {
            long var14;
            label182: {
               int var1 = text.hashCode();
               byte var21;
               if (var1 <= 3029738) {
                  if (var1 == 3029738) {
                     boolean var4 = text.equals("bool");
                     if (!var4) {
                        var2 = 4294967295L;
                        break label184;
                     }

                     var14 = 8L;
                     break label182;
                  }

                  if (var1 <= 3146) {
                     if (var1 == 3146) {
                        boolean var5 = text.equals("bl");
                        if (!var5) {
                           var2 = 4294967295L;
                           break label184;
                        }

                        var14 = 9L;
                        break label182;
                     }

                     if (var1 == -2001130489) {
                        boolean var6 = text.equals("nullptr");
                        if (!var6) {
                           var2 = 4294967295L;
                           break label184;
                        }

                        var14 = 14L;
                        break label182;
                     }

                     if (var1 == -1325958191) {
                        boolean var7 = text.equals("double");
                        if (!var7) {
                           var2 = 4294967295L;
                           break label184;
                        }

                        var16 = 3L;
                        break label183;
                     }

                     if (var1 != -891985903) {
                        if (var1 != -318750117) {
                           var2 = 4294967295L;
                           break label184;
                        }

                        boolean var9 = text.equals("precise");
                        if (!var9) {
                           var2 = 4294967295L;
                           break label184;
                        }

                        var21 = 6;
                     } else {
                        boolean var8 = text.equals("string");
                        if (!var8) {
                           var2 = 4294967295L;
                           break label184;
                        }

                        var21 = 10;
                     }
                  } else {
                     if (var1 == 104431) {
                        boolean var18 = text.equals("int");
                        if (!var18) {
                           var2 = 4294967295L;
                           break label184;
                        }

                        var14 = 0L;
                        break label182;
                     }

                     if (var1 == 109073) {
                        boolean var19 = text.equals("nil");
                        if (!var19) {
                           var2 = 4294967295L;
                           break label184;
                        }

                        var14 = 13L;
                        break label182;
                     }

                     if (var1 != 114225) {
                        var2 = 4294967295L;
                        break label184;
                     }

                     boolean var20 = text.equals("str");
                     if (!var20) {
                        var2 = 4294967295L;
                        break label184;
                     }

                     var21 = 11;
                  }
               } else {
                  if (var1 > 97526364) {
                     if (var1 != 100359822) {
                        if (var1 != 1958052158) {
                           if (var1 != 2010122246) {
                              var2 = 4294967295L;
                              break label184;
                           }

                           boolean var35 = text.equals("floating");
                           if (!var35) {
                              var2 = 4294967295L;
                              break label184;
                           }

                           var14 = 5L;
                        } else {
                           boolean var34 = text.equals("integer");
                           if (!var34) {
                              var2 = 4294967295L;
                              break label184;
                           }

                           var14 = 1L;
                        }
                     } else {
                        boolean var33 = text.equals("int32");
                        if (!var33) {
                           var2 = 4294967295L;
                           break label184;
                        }

                        var14 = 2L;
                     }
                     break label182;
                  }

                  if (var1 == 97526364) {
                     boolean var10 = text.equals("float");
                     if (!var10) {
                        var2 = 4294967295L;
                        break label184;
                     }

                     var14 = 4L;
                     break label182;
                  }

                  if (var1 == 3392903) {
                     boolean var11 = text.equals("null");
                     if (!var11) {
                        var2 = 4294967295L;
                        break label184;
                     }

                     var14 = 12L;
                     break label182;
                  }

                  if (var1 == 3625364) {
                     boolean var12 = text.equals("void");
                     if (!var12) {
                        var2 = 4294967295L;
                        break label184;
                     }

                     var14 = 15L;
                     break label182;
                  }

                  if (var1 != 64711720) {
                     var2 = 4294967295L;
                     break label184;
                  }

                  boolean var13 = text.equals("boolean");
                  if (!var13) {
                     var2 = 4294967295L;
                     break label184;
                  }

                  var21 = 7;
               }

               var16 = var21 & 4294967295L;
               break label183;
            }

            var16 = var14;
         }

         var2 = (int)var16 & 4294967295L;
      }

      if (Integer.compareUnsigned((int)((int)var2 & 4294967295L), 15) > 0) {
         String var29 = "Unknown type: " + text;
         IllegalArgumentException var22 = new IllegalArgumentException(var29);
         throw var22;
      }

      Class<?> var30;
      switch ((int)((int)var2 & 4294967295L)) {
         case 0:
         case 1:
         case 2:
         default:
            var30 = Integer.class;
            break;
         case 3:
         case 4:
         case 5:
         case 6:
            var30 = Double.class;
            break;
         case 7:
         case 8:
         case 9:
            var30 = Boolean.class;
            break;
         case 10:
         case 11:
            var30 = String.class;
            break;
         case 12:
         case 13:
         case 14:
         case 15:
            return null;
      }

      return var30;
   }

   @SneakyThrows public static Object[] values(String... texts) {
      CallSite var964 = null;
      long var8 = 0L;
      long var6 = 0L;
      int var1 = texts.length;
      Object[] var40;
      if (var1 != 0) {
         int var2 = texts.length;
         Object[] var3 = new Object[var2];
         long var4 = 0L;

         label109:
         while (true) {
            long var10 = (int)var4 & 4294967295L | var8 & -4294967296L;
            long var12 = var6;
            long var14 = var10;
            long var16 = var12;
            long var18 = var14;
            long var20 = var16;
            long var22 = var18;
            long var24 = var20;
            int var26 = texts.length;
            long var27 = var22;
            long var29 = var24;
            long var31 = var27;
            long var33 = var29;
            if ((int)((int)var4 & 4294967295L) >= var26) {
               var40 = var3;
               break;
            }

            long var35 = var31;
            long var37 = var33;
            long var41 = var35;
            long var43 = var37;
            long var45 = var41;
            long var47 = var43;
            long var49 = var45;
            long var51 = var47;
            long var53 = var49;
            long var55 = var51;
            Object var57 = texts[(int)((int)var4 & 4294967295L)];
            long var58 = var53;
            long var60 = var55;
            long var62 = var58;
            long var64 = var60;
            long var66 = var62;
            long var68 = var64;
            long var70 = var66;
            long var72 = var68;
            long var74 = var70;
            long var76 = var72;
            long var78 = var74;
            long var80 = var76;
            long var82 = var78;
            long var84 = var80;
            long var86 = var82;
            long var88 = var84;
            boolean var90 = ((String)var57).isEmpty();
            long var91 = var86;
            long var93 = var88;
            long var95 = var91;
            long var97 = var93;
            long var99 = var95;
            long var101 = var97;
            long var107;
            long var109;
            if (!var90) {
               long var103 = var99;
               long var105 = var101;
               long var111 = var103;
               long var113 = var105;
               long var115 = var111;
               long var117 = var113;
               long var119 = var115;
               long var121 = var117;
               long var123 = var119;
               long var125 = var121;
               long var127 = var123;
               long var129 = var125;
               long var131 = var127;
               long var133 = var129;
               long var135 = var131;
               long var137 = var133;
               long var139 = var135;
               long var141 = var137;
               long var143 = var139;
               long var145 = var141;
               boolean var147 = ((String)var57).startsWith("\"");
               long var148 = var143;
               long var150 = var145;
               long var152 = var148;
               long var154 = var150;
               long var156 = var152;
               long var158 = var154;
               if (!var147) {
                  Serializable var471;
                  long var472;
                  long var474;
                  long var476;
                  label118: {
                     long var160 = var156;
                     long var162 = var158;
                     long var168 = var160;
                     long var170 = var162;
                     long var172 = var168;
                     long var174 = var170;
                     long var176 = var172;
                     long var178 = var174;
                     long var180 = var176;
                     long var182 = var178;
                     long var184 = var180;
                     long var186 = var182;
                     long var188 = var184;
                     long var190 = var186;
                     long var192 = var188;
                     long var194 = var190;
                     long var196 = var192;
                     long var198 = var194;
                     long var200 = var196;
                     long var202 = var198;
                     boolean var204 = ((String)var57).contains(".");
                     long var205 = var200;
                     long var207 = var202;
                     long var209 = var205;
                     long var211 = var207;
                     long var213 = var209;
                     long var215 = var211;
                     long var221;
                     long var223;
                     if (var204) {
                        long var217 = var213;
                        long var219 = var215;
                        long var249 = var217;
                        long var251 = var219;
                        long var253 = var249;
                        long var255 = var251;
                        long var257 = var253;
                        long var259 = var255;
                        long var261 = var257;
                        long var263 = var259;
                        long var265 = var261;
                        long var267 = var263;
                        long var269 = var265;
                        long var271 = var267;
                        long var273 = var269;
                        long var275 = var271;
                        long var277 = var273;
                        long var279 = var275;
                        long var281 = var277;
                        long var283 = var279;
                        String[] var285 = ((String)var57).split("\\.");
                        long var286 = var281;
                        long var288 = var283;
                        long var290 = var286;
                        long var292 = var288;
                        long var294 = var290;
                        long var296 = var292;
                        int var298 = var285.length;
                        long var299 = var294;
                        long var301 = var296;
                        long var303 = var299;
                        long var305 = var301;
                        long var307 = var303;
                        long var309 = var305;
                        long var311 = var307;
                        long var313 = var309;
                        if (var298 == 2) {
                           long var315 = var311;
                           long var317 = var313;
                           long var376 = var315;
                           long var378 = var317;
                           long var380 = var376;
                           long var382 = var378;
                           long var10001 = (int)var380 & 4294967295L;
                           long var384 = var380;
                           long var388 = var382;
                           long var390 = (int)var380 & 4294967295L;
                           long var392 = var384;
                           long var394 = var388;
                           long var396 = (int)var380 & 4294967295L;
                           long var398 = var392;
                           long var400 = var394;
                           long var402 = (int)var380 & 4294967295L;
                           long var404 = var398;
                           long var406 = var400;
                           long var408 = (int)var380 & 4294967295L;
                           long var410 = var404;
                           long var412 = var406;
                           long var414 = (int)var380 & 4294967295L;
                           long var416 = var410;
                           long var418 = var412;
                           double var420 = Double.parseDouble((String)var57);
                           long var422 = (int)var380 & 4294967295L;
                           long var424 = var416;
                           long var426 = var418;
                           long var428 = (int)var380 & 4294967295L;
                           long var430 = var424;
                           long var432 = var426;
                           long var434 = (int)var380 & 4294967295L;
                           long var436 = var430;
                           long var438 = var432;
                           long var440 = (int)var380 & 4294967295L;
                           long var442 = var436;
                           long var444 = var438;
                           long var446 = (int)var380 & 4294967295L;
                           long var448 = var442;
                           long var450 = var444;
                           long var452 = (int)var380 & 4294967295L;
                           long var454 = var448;
                           long var456 = var450;
                           Double var458 = var420;
                           long var459 = (int)var380 & 4294967295L;
                           long var461 = var454;
                           long var463 = var456;
                           long var465 = (int)var380 & 4294967295L;
                           long var467 = var461;
                           long var469 = var463;
                           long var10002 = (int)var380 & 4294967295L;
                           var471 = var458;
                           var472 = var10002;
                           var474 = var467;
                           var476 = var469;
                           break label118;
                        }

                        var221 = var311;
                        var223 = var313;
                     } else {
                        var221 = var213;
                        var223 = var215;
                     }

                     long var612;
                     long var614;
                     long var616;
                     int var618;
                     long var619;
                     label98: {
                        long var319 = var221;
                        long var321 = var223;
                        long var323 = var319;
                        long var325 = var321;
                        long var327 = var323;
                        long var329 = var325;
                        long var331 = var327;
                        long var333 = var329;
                        long var335 = var331;
                        long var337 = var333;
                        long var339 = var335;
                        long var341 = var337;
                        long var343 = var339;
                        long var345 = var341;
                        long var347 = var343;
                        long var349 = var345;
                        long var351 = var347;
                        long var353 = var349;
                        boolean var355 = ((String)var57).equals("true");
                        long var356 = var351;
                        long var358 = var353;
                        long var360 = var356;
                        long var362 = var358;
                        long var364 = var360;
                        long var366 = var362;
                        long var372;
                        long var374;
                        if (!var355) {
                           long var368 = var364;
                           long var370 = var366;
                           long var478 = var368;
                           long var480 = var370;
                           long var482 = var478;
                           long var484 = var480;
                           long var486 = var482;
                           long var488 = var484;
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
                           boolean var514 = ((String)var57).equals("false");
                           long var515 = var510;
                           long var517 = var512;
                           long var519 = var515;
                           long var521 = var517;
                           long var523 = var519;
                           long var525 = var521;
                           if (!var514) {
                              long var527 = var523;
                              long var529 = var525;
                              long var634 = var527;
                              long var636 = var529;
                              long var638 = var634;
                              long var640 = var636;
                              long var10000 = 1L | var640 & -4294967296L;
                              long var642 = var638;
                              long var644 = var10000;
                              long var646 = var642;
                              long var648 = var644;
                              long var650 = var646;
                              long var652 = var648;
                              long var654 = var650;
                              long var656 = var652;
                              long var658 = var654;
                              long var660 = var656;
                              long var662 = var658;
                              long var664 = var660;
                              char[] var666 = ((String)var57).toCharArray();
                              long var667 = var662;
                              long var669 = var664;
                              long var671 = var667;
                              long var673 = var669;
                              long var675 = var671;
                              long var677 = var673;
                              long var679 = var675;
                              long var681 = var677;
                              long var683 = var679;
                              long var685 = var681;
                              int var687 = var666.length;
                              long var688 = var683;
                              long var690 = var685;
                              long var692 = var688;
                              long var694 = var690;
                              long var696 = var692;
                              long var698 = var694;
                              long var700 = var696;
                              long var702 = var698;
                              long var10003 = var687 & 4294967295L;
                              long var704 = var700;
                              long var706 = var10003;
                              long var708 = var702;
                              long var710 = 0L;
                              long var712 = 1L;

                              while (true) {
                                 long var1070 = (int)var710 & 4294967295L | var706 & -4294967296L;
                                 long var714 = var704;
                                 long var716 = var1070;
                                 long var718 = var708;
                                 long var720 = var714;
                                 long var722 = var716;
                                 long var724 = var718;
                                 long var726 = var720;
                                 long var728 = var722;
                                 long var730 = var724;
                                 long var732 = var726;
                                 long var734 = var728;
                                 long var736 = var730;
                                 if ((int)((int)var710 & 4294967295L) >= var687) {
                                    long var744 = var732;
                                    long var746 = var736;
                                    long var840 = var744;
                                    long var842 = var746;
                                    long var844 = var840;
                                    long var846 = var842;
                                    if ((int)((int)var712 & 4294967295L) == 0) {
                                       long var852 = var844;
                                       long var952 = var852;
                                       long var954 = var952;
                                       long var956 = var954;
                                       long var958 = var956;
                                       long var960 = var958;
                                       long var962 = var960;
                                       long var967;
                                       if (var964 == null) {
                                          long var965 = var962;
                                          long var969 = var965;
                                          long var971 = var969;
                                          long var973 = var971;
                                          long var975 = var973;
                                          long var977 = var975;
                                          long var979 = var977;
                                          long var981 = var979;
                                          long var983 = var981;
                                          Lookup var985 = MethodHandles.lookup();
                                          long var986 = var983;
                                          long var988 = var986;
                                          long var990 = var988;
                                          long var992 = var990;
                                          long var994 = var992;
                                          long var996 = var994;
                                          long var998 = var996;
                                          ClassLoader var1000 = ScriptCompiler.class.getClassLoader();
                                          MethodType var1001 = MethodType.fromMethodDescriptorString("(Ljava/lang/String;)Ljava/lang/String;", var1000);
                                          long var1002 = var998;
                                          long var1004 = var1002;
                                          long var1006 = var1004;
                                          long var1008 = var1006;
                                          long var1010 = var1008;
                                          Object[] var1012 = new Object[0];
                                          long var1013 = var1010;
                                          long var1015 = var1013;
                                          var964 = StringConcatFactory.makeConcatWithConstants(var985, "makeConcatWithConstants", var1001, "&\u0001", var1012);
                                          long var1017 = var1015;
                                          long var1019 = var1017;
                                          long var1021 = var1019;
                                          long var1023 = var1021;
                                          var967 = var1023;
                                       } else {
                                          var967 = var962;
                                       }

                                       long var1025 = var967;
                                       long var1027 = var1025;
                                       long var1029 = var1027;
                                       long var1031 = var1029;
                                       MethodHandle var1033 = var964.getTarget();
                                       long var1034 = var1031;
                                       long var1036 = var1034;
                                       long var1038 = var1036;
                                       long var1040 = var1038;
                                       long var1042 = var1040;
                                       long var1044 = var1042;
                                       long var1046 = var1044;
                                       long var1048 = var1046;
                                       long var1050 = var1048;
                                       long var1052 = var1050;
                                       long var1056;
                                       if (var57 != null) {
                                          long var1054 = var1052;
                                          Object[] var1065 = phobos$g1806fd420;
                                          if (phobos$g1806fd420 == null) {
                                             var1065 = phobos$g1806fd420 = new Object[1];
                                          }

                                          var1065[0] = var57;
                                          var1056 = var1054;
                                       } else {
                                          var1056 = var1052;
                                       }

                                       Object[] var1074 = phobos$g1806fd420;
                                       if (phobos$g1806fd420 == null) {
                                          var1074 = phobos$g1806fd420 = new Object[1];
                                       }

                                       Object var1058 = var1033.invokeWithArguments(var1074);
                                       long var1059 = var1056;
                                       long var1061 = var1059;
                                       var3[(int)((int)var954 & 4294967295L)] = var1058;
                                       var10000 = (int)var1061 + 1 & 4294967295L;
                                       var8 = var1061;
                                       var4 = var10000;
                                       continue label109;
                                    }

                                    long var848 = var844;
                                    long var850 = var846;
                                    var1070 = (int)var848 & 4294967295L;
                                    long var866 = var848;
                                    long var870 = var850;
                                    long var872 = (int)var848 & 4294967295L;
                                    long var874 = var866;
                                    long var876 = var870;
                                    long var878 = (int)var848 & 4294967295L;
                                    long var880 = var874;
                                    long var882 = var876;
                                    long var884 = (int)var848 & 4294967295L;
                                    long var886 = var880;
                                    long var888 = var882;
                                    long var890 = (int)var848 & 4294967295L;
                                    long var892 = var886;
                                    long var894 = var888;
                                    long var896 = (int)var848 & 4294967295L;
                                    long var898 = var892;
                                    long var900 = var894;
                                    long var902 = Long.parseLong((String)var57);
                                    long var904 = (int)var848 & 4294967295L;
                                    long var906 = var898;
                                    long var908 = var900;
                                    long var910 = (int)var848 & 4294967295L;
                                    long var912 = var906;
                                    long var914 = var908;
                                    long var916 = (int)var848 & 4294967295L;
                                    long var918 = var912;
                                    long var920 = var914;
                                    long var922 = (int)var848 & 4294967295L;
                                    long var924 = var918;
                                    long var926 = var920;
                                    long var928 = (int)var848 & 4294967295L;
                                    long var930 = var924;
                                    long var932 = var926;
                                    long var934 = (int)var848 & 4294967295L;
                                    long var936 = var930;
                                    long var938 = var932;
                                    long var940 = (int)var848 & 4294967295L;
                                    long var942 = var936;
                                    long var944 = var938;
                                    long var946 = (int)var848 & 4294967295L;
                                    long var948 = var942;
                                    long var950 = var944;
                                    var10000 = (int)var902 & 4294967295L;
                                    var612 = (int)var848 & 4294967295L;
                                    var614 = var948;
                                    var616 = var950;
                                    var618 = 7328832;
                                    var619 = var10000;
                                    break label98;
                                 }

                                 long var738 = var732;
                                 long var740 = var734;
                                 long var742 = var736;
                                 long var748 = var738;
                                 long var750 = var740;
                                 long var752 = var742;
                                 long var754 = var748;
                                 long var756 = var750;
                                 long var758 = var752;
                                 long var760 = var754;
                                 long var762 = var756;
                                 long var764 = var758;
                                 long var766 = var760;
                                 long var768 = var762;
                                 long var770 = var764;
                                 int var772 = var666[(int)((int)var710 & 4294967295L)];
                                 long var773 = var766;
                                 long var775 = var768;
                                 long var777 = var770;
                                 long var779 = var773;
                                 long var781 = var775;
                                 long var783 = var777;
                                 long var785 = var779;
                                 long var787 = var781;
                                 long var789 = var783;
                                 long var791 = var785;
                                 long var793 = var787;
                                 long var795 = var789;
                                 long var797 = var791;
                                 long var799 = var793;
                                 long var801 = var795;
                                 long var803 = var797;
                                 long var805 = var799;
                                 long var807 = var801;
                                 boolean var809 = Character.isDigit((char)((int)(var772 & 65535 & 65535) & 65535));
                                 long var810 = var803;
                                 long var812 = var805;
                                 long var814 = var807;
                                 long var816 = var810;
                                 long var818 = var812;
                                 long var820 = var814;
                                 long var822 = var816;
                                 long var824 = var818;
                                 long var826 = var820;
                                 if (!var809) {
                                    long var828 = var822;
                                    long var830 = var824;
                                    long var832 = var826;
                                    long var854 = var828;
                                    long var856 = var830;
                                    long var858 = var832;
                                    long var860 = var854;
                                    long var862 = var856;
                                    long var864 = var858;
                                    var1070 = (int)var862 + 1 & 4294967295L;
                                    long var1076 = 0L | var864 & -4294967296L;
                                    var704 = var860;
                                    var706 = var862;
                                    var708 = var1076;
                                    var710 = var1070;
                                    var712 = 0L;
                                 } else {
                                    long var834 = var822;
                                    long var836 = var824;
                                    long var838 = var826;
                                    var10000 = (int)var838 & 4294967295L;
                                    var1070 = (int)var836 + 1 & 4294967295L;
                                    var704 = var834;
                                    var706 = var836;
                                    var708 = var838;
                                    var710 = var1070;
                                    var712 = var10000;
                                 }
                              }
                           }

                           var372 = var523;
                           var374 = var525;
                        } else {
                           var372 = var364;
                           var374 = var366;
                        }

                        long var531 = var372;
                        long var533 = var374;
                        long var535 = var531;
                        long var537 = var533;
                        long var1075 = (int)var535 & 4294967295L;
                        long var539 = var535;
                        long var543 = var537;
                        long var545 = (int)var535 & 4294967295L;
                        long var547 = var539;
                        long var549 = var543;
                        long var551 = (int)var535 & 4294967295L;
                        long var553 = var547;
                        long var555 = var549;
                        long var557 = (int)var535 & 4294967295L;
                        long var559 = var553;
                        long var561 = var555;
                        long var563 = (int)var535 & 4294967295L;
                        long var565 = var559;
                        long var567 = var561;
                        long var569 = (int)var535 & 4294967295L;
                        long var571 = var565;
                        long var573 = var567;
                        int var575 = (Boolean.parseBoolean((String)var57)) ? 1 : 0;
                        long var576 = (int)var535 & 4294967295L;
                        long var578 = var571;
                        long var580 = var573;
                        long var582 = (int)var535 & 4294967295L;
                        long var584 = var578;
                        long var586 = var580;
                        long var588 = (int)var535 & 4294967295L;
                        long var590 = var584;
                        long var592 = var586;
                        long var594 = (int)var535 & 4294967295L;
                        long var596 = var590;
                        long var598 = var592;
                        long var600 = (int)var535 & 4294967295L;
                        long var602 = var596;
                        long var604 = var598;
                        long var606 = (int)var535 & 4294967295L;
                        long var608 = var602;
                        long var610 = var604;
                        long var1067 = var575 & 0xFF & 0xFF;
                        var612 = (int)var535 & 4294967295L;
                        var614 = var608;
                        var616 = var610;
                        var618 = 7328616;
                        var619 = var1067;
                     }

                     Serializable var621;
                     switch (var618) {
                        case 7328616:
                        default:
                           var621 = Boolean.valueOf((((int)var619 & 1) != 0));
                           break;
                        case 7328832:
                           var621 = (int)var619;
                     }

                     long var624 = var614;
                     long var626 = var616;
                     long var630 = var624;
                     long var632 = var626;
                     var471 = var621;
                     var472 = var612;
                     var474 = var630;
                     var476 = var632;
                  }

                  var3[(int)var472] = var471;
                  long var1068 = (int)var474 + 1 & 4294967295L;
                  var8 = var474;
                  var6 = var476;
                  var4 = var1068;
                  continue;
               }

               long var164 = var156;
               long var166 = var158;
               long var225 = var164;
               long var227 = var166;
               long var229 = var225;
               long var231 = var227;
               long var233 = var229;
               long var235 = var231;
               long var237 = var233;
               long var239 = var235;
               long var241 = var237;
               long var243 = var239;
               long var245 = var241;
               long var247 = var243;
               var3[(int)((int)var229 & 4294967295L)] = var57;
               var107 = var245;
               var109 = var247;
            } else {
               var107 = var99;
               var109 = var101;
            }

            long var1069 = (int)var107 + 1 & 4294967295L;
            var8 = var107;
            var6 = var109;
            var4 = var1069;
         }
      } else {
         Object[] var39 = new Object[0];
         var40 = var39;
      }

      return var40;
   }

   public static Class<?> resolveClassValue2(String text, String text2) {
      long var3;
      label199: {
         long var18;
         label198: {
            long var16;
            label197: {
               int var2 = text.hashCode();
               byte var24;
               if (var2 <= 114225) {
                  if (var2 == 114225) {
                     boolean var5 = text.equals("str");
                     if (!var5) {
                        var3 = 4294967295L;
                        break label199;
                     }

                     var16 = 11L;
                     break label197;
                  }

                  if (var2 <= -318750117) {
                     if (var2 == -318750117) {
                        boolean var6 = text.equals("precise");
                        if (!var6) {
                           var3 = 4294967295L;
                           break label199;
                        }

                        var16 = 6L;
                        break label197;
                     }

                     if (var2 != -2001130489) {
                        if (var2 == -1325958191) {
                           boolean var8 = text.equals("double");
                           if (!var8) {
                              var3 = 4294967295L;
                              break label199;
                           }

                           var18 = 3L;
                           break label198;
                        }

                        if (var2 != -1249586564) {
                           if (var2 != -891985903) {
                              var3 = 4294967295L;
                              break label199;
                           }

                           boolean var10 = text.equals("string");
                           if (!var10) {
                              var3 = 4294967295L;
                              break label199;
                           }

                           var24 = 10;
                        } else {
                           boolean var9 = text.equals("variable");
                           if (!var9) {
                              var3 = 4294967295L;
                              break label199;
                           }

                           var24 = 17;
                        }
                     } else {
                        boolean var7 = text.equals("nullptr");
                        if (!var7) {
                           var3 = 4294967295L;
                           break label199;
                        }

                        var24 = 14;
                     }
                  } else {
                     if (var2 == 3146) {
                        boolean var20 = text.equals("bl");
                        if (!var20) {
                           var3 = 4294967295L;
                           break label199;
                        }

                        var16 = 9L;
                        break label197;
                     }

                     if (var2 != 104431) {
                        if (var2 != 107035) {
                           if (var2 != 109073) {
                              var3 = 4294967295L;
                              break label199;
                           }

                           boolean var23 = text.equals("nil");
                           if (!var23) {
                              var3 = 4294967295L;
                              break label199;
                           }

                           var24 = 13;
                        } else {
                           boolean var22 = text.equals("let");
                           if (!var22) {
                              var3 = 4294967295L;
                              break label199;
                           }

                           var24 = 15;
                        }
                     } else {
                        boolean var21 = text.equals("int");
                        if (!var21) {
                           var3 = 4294967295L;
                           break label199;
                        }

                        var24 = 0;
                     }
                  }
               } else {
                  if (var2 > 97526364) {
                     if (var2 != 100359822) {
                        if (var2 != 1958052158) {
                           if (var2 != 2010122246) {
                              var3 = 4294967295L;
                              break label199;
                           }

                           boolean var48 = text.equals("floating");
                           if (!var48) {
                              var3 = 4294967295L;
                              break label199;
                           }

                           var18 = 5L;
                           break label198;
                        }

                        boolean var47 = text.equals("integer");
                        if (!var47) {
                           var3 = 4294967295L;
                           break label199;
                        }

                        var16 = 1L;
                     } else {
                        boolean var46 = text.equals("int32");
                        if (!var46) {
                           var3 = 4294967295L;
                           break label199;
                        }

                        var16 = 2L;
                     }
                     break label197;
                  }

                  if (var2 == 97526364) {
                     boolean var11 = text.equals("float");
                     if (!var11) {
                        var3 = 4294967295L;
                        break label199;
                     }

                     var16 = 4L;
                     break label197;
                  }

                  if (var2 == 116519) {
                     boolean var12 = text.equals("var");
                     if (!var12) {
                        var3 = 4294967295L;
                        break label199;
                     }

                     var16 = 16L;
                     break label197;
                  }

                  if (var2 != 3029738) {
                     if (var2 != 3392903) {
                        if (var2 != 64711720) {
                           var3 = 4294967295L;
                           break label199;
                        }

                        boolean var15 = text.equals("boolean");
                        if (!var15) {
                           var3 = 4294967295L;
                           break label199;
                        }

                        var24 = 7;
                     } else {
                        boolean var14 = text.equals("null");
                        if (!var14) {
                           var3 = 4294967295L;
                           break label199;
                        }

                        var24 = 12;
                     }
                  } else {
                     boolean var13 = text.equals("bool");
                     if (!var13) {
                        var3 = 4294967295L;
                        break label199;
                     }

                     var24 = 8;
                  }
               }

               var18 = var24 & 4294967295L;
               break label198;
            }

            var18 = var16;
         }

         var3 = (int)var18 & 4294967295L;
      }

      Class var35;
      if (Integer.compareUnsigned((int)((int)var3 & 4294967295L), 17) <= 0) {
         switch ((int)((int)var3 & 4294967295L)) {
            case 0:
            case 1:
            case 2:
            default:
               var35 = Integer.class;
               break;
            case 3:
            case 4:
            case 5:
            case 6:
               var35 = Double.class;
               break;
            case 7:
            case 8:
            case 9:
               var35 = Boolean.class;
               break;
            case 10:
            case 11:
               var35 = String.class;
               break;
            case 12:
            case 13:
            case 14:
               String var44 = "Null types are not supported: " + text;
               IllegalArgumentException var37 = new IllegalArgumentException(var44);
               throw var37;
            case 15:
            case 16:
            case 17:
               Class<?> var45 = resolveClassValue3(text2);
               var35 = var45;
         }
      } else {
         EventClassRegistry var25 = EventClassRegistry.INSTANCE;
         Class<?> var26 = var25.resolveClassValue(text);
         if (var26 == null) {
            String var34 = "Unknown type: " + text;
            IllegalArgumentException var27 = new IllegalArgumentException(var34);
            throw var27;
         }

         var35 = var26;
      }

      return var35;
   }

   public static Class<?> resolveClassValue3(String text) {
      boolean var1 = text.startsWith("\"");
      if (!var1) {
         boolean var2 = text.contains(".");
         if (var2) {
            String[] var3 = text.split("\\.");
            int var4 = var3.length;
            if (var4 == 2) {
               return Double.class;
            }
         }

         boolean var9 = text.equals("true");
         if (!var9) {
            boolean var10 = text.equals("false");
            if (!var10) {
               char[] var11 = text.toCharArray();
               int var12 = var11.length;
               long var13 = 1L;
               long var15 = 0L;
               long var17 = 1L;

               while (true) {
                  long var19 = var13;
                  long var21 = var15;
                  long var23 = var19;
                  long var25 = var21;
                  long var27 = var23;
                  long var29 = var25;
                  long var31 = var27;
                  long var33 = var29;
                  if ((int)((int)var15 & 4294967295L) >= var12) {
                     Class<?> var8;
                     if ((int)((int)var17 & 4294967295L) != 0) {
                        var8 = Integer.class;
                     } else {
                        var8 = String.class;
                     }

                     return var8;
                  }

                  long var35 = var31;
                  long var37 = var33;
                  long var39 = var35;
                  long var41 = var37;
                  long var43 = var39;
                  long var45 = var41;
                  long var47 = var43;
                  long var49 = var45;
                  long var51 = var47;
                  long var53 = var49;
                  int var55 = var11[(int)((int)var45 & 4294967295L)];
                  long var56 = var51;
                  long var58 = var53;
                  long var60 = var56;
                  long var62 = var58;
                  long var64 = var60;
                  long var66 = var62;
                  long var68 = var64;
                  long var70 = var66;
                  long var72 = var68;
                  long var74 = var70;
                  long var76 = var72;
                  long var78 = var74;
                  boolean var80 = Character.isDigit((char)((int)(var55 & 65535 & 65535) & 65535));
                  long var81 = var76;
                  long var83 = var78;
                  long var85 = var81;
                  long var87 = var83;
                  long var89 = var85;
                  long var91 = var87;
                  if (!var80) {
                     long var93 = var89;
                     long var95 = var91;
                     long var101 = var93;
                     long var103 = var95;
                     long var10001 = (int)var103 + 1 & 4294967295L;
                     var13 = 0L | var101 & -4294967296L;
                     var15 = var10001;
                     var17 = 0L;
                  } else {
                     long var97 = var89;
                     long var99 = var91;
                     long var10000 = (int)var97 & 4294967295L;
                     long var105 = (int)var99 + 1 & 4294967295L;
                     var13 = var97;
                     var15 = var105;
                     var17 = var10000;
                  }
               }
            }
         }

         return Boolean.class;
      } else {
         return String.class;
      }
   }

   public static Class<?> resolveClassValue4(String text) {
      long var2;
      label175: {
         long var15;
         label174: {
            long var13;
            label173: {
               int var1 = text.hashCode();
               byte var20;
               if (var1 <= 114225) {
                  if (var1 == 114225) {
                     boolean var4 = text.equals("str");
                     if (!var4) {
                        var2 = 4294967295L;
                        break label175;
                     }

                     var13 = 11L;
                     break label173;
                  }

                  if (var1 <= -318750117) {
                     if (var1 == -318750117) {
                        boolean var5 = text.equals("precise");
                        if (!var5) {
                           var2 = 4294967295L;
                           break label175;
                        }

                        var13 = 6L;
                        break label173;
                     }

                     if (var1 != -2001130489) {
                        if (var1 == -1325958191) {
                           boolean var7 = text.equals("double");
                           if (!var7) {
                              var2 = 4294967295L;
                              break label175;
                           }

                           var15 = 3L;
                           break label174;
                        }

                        if (var1 != -891985903) {
                           var2 = 4294967295L;
                           break label175;
                        }

                        boolean var8 = text.equals("string");
                        if (!var8) {
                           var2 = 4294967295L;
                           break label175;
                        }

                        var20 = 10;
                     } else {
                        boolean var6 = text.equals("nullptr");
                        if (!var6) {
                           var2 = 4294967295L;
                           break label175;
                        }

                        var20 = 14;
                     }
                  } else {
                     if (var1 == 3146) {
                        boolean var17 = text.equals("bl");
                        if (!var17) {
                           var2 = 4294967295L;
                           break label175;
                        }

                        var13 = 9L;
                        break label173;
                     }

                     if (var1 == 104431) {
                        boolean var18 = text.equals("int");
                        if (!var18) {
                           var2 = 4294967295L;
                           break label175;
                        }

                        var13 = 0L;
                        break label173;
                     }

                     if (var1 != 109073) {
                        var2 = 4294967295L;
                        break label175;
                     }

                     boolean var19 = text.equals("nil");
                     if (!var19) {
                        var2 = 4294967295L;
                        break label175;
                     }

                     var20 = 13;
                  }
               } else {
                  if (var1 > 97526364) {
                     if (var1 != 100359822) {
                        if (var1 != 1958052158) {
                           if (var1 != 2010122246) {
                              var2 = 4294967295L;
                              break label175;
                           }

                           boolean var43 = text.equals("floating");
                           if (!var43) {
                              var2 = 4294967295L;
                              break label175;
                           }

                           var15 = 5L;
                           break label174;
                        }

                        boolean var42 = text.equals("integer");
                        if (!var42) {
                           var2 = 4294967295L;
                           break label175;
                        }

                        var13 = 1L;
                     } else {
                        boolean var41 = text.equals("int32");
                        if (!var41) {
                           var2 = 4294967295L;
                           break label175;
                        }

                        var13 = 2L;
                     }
                     break label173;
                  }

                  if (var1 == 97526364) {
                     boolean var9 = text.equals("float");
                     if (!var9) {
                        var2 = 4294967295L;
                        break label175;
                     }

                     var13 = 4L;
                     break label173;
                  }

                  if (var1 == 3029738) {
                     boolean var10 = text.equals("bool");
                     if (!var10) {
                        var2 = 4294967295L;
                        break label175;
                     }

                     var13 = 8L;
                     break label173;
                  }

                  if (var1 != 3392903) {
                     if (var1 != 64711720) {
                        var2 = 4294967295L;
                        break label175;
                     }

                     boolean var12 = text.equals("boolean");
                     if (!var12) {
                        var2 = 4294967295L;
                        break label175;
                     }

                     var20 = 7;
                  } else {
                     boolean var11 = text.equals("null");
                     if (!var11) {
                        var2 = 4294967295L;
                        break label175;
                     }

                     var20 = 12;
                  }
               }

               var15 = var20 & 4294967295L;
               break label174;
            }

            var15 = var13;
         }

         var2 = (int)var15 & 4294967295L;
      }

      Class var31;
      if (Integer.compareUnsigned((int)((int)var2 & 4294967295L), 14) <= 0) {
         switch ((int)((int)var2 & 4294967295L)) {
            case 0:
            case 1:
            case 2:
            default:
               var31 = Integer.class;
               break;
            case 3:
            case 4:
            case 5:
            case 6:
               var31 = Double.class;
               break;
            case 7:
            case 8:
            case 9:
               var31 = Boolean.class;
               break;
            case 10:
            case 11:
               var31 = String.class;
               break;
            case 12:
            case 13:
            case 14:
               String var40 = "Null types are not supported: " + text;
               IllegalArgumentException var33 = new IllegalArgumentException(var40);
               throw var33;
         }
      } else {
         EventClassRegistry var21 = EventClassRegistry.INSTANCE;
         Class<?> var22 = var21.resolveClassValue(text);
         if (var22 == null) {
            String var30 = "Unknown type: " + text;
            IllegalArgumentException var23 = new IllegalArgumentException(var30);
            throw var23;
         }

         var31 = var22;
      }

      return var31;
   }

   public static class S {
      private static Object[] phobos$g1806fc078;
      private static Object[] phobos$g1806fb988;
      private static Object[] phobos$g1806fba38;
      private static Object[] phobos$g1806fbcb0;
      private static Object[] phobos$g1806fc6d8;
      private static Object[] phobos$g1806fc2a0;
      private static Object[] phobos$g1806fca18;
      private static Object[] phobos$g1806fcb40;
      private static Object[] phobos$g1806fcd88;

      public static boolean testList(List<String> list) {
         Iterator var1 = list.iterator();
         long var2 = 0L;
         String var4 = "||";

         while (true) {
            long var5 = var2;
            String var7 = var4;
            long var8 = var5;
            String var10 = var7;
            long var11 = var8;
            String var13 = var10;
            long var14 = var11;
            String var16 = var13;
            long var17 = var14;
            String var19 = var16;
            boolean var20 = var1.hasNext();
            long var21 = var17;
            String var23 = var19;
            long var24 = var21;
            String var26 = var23;
            long var27 = var24;
            String var29 = var26;
            if (!var20) {
               return (((int)((int)((int)((int)var27 & 4294967295L) & 4294967295L) & 0xFF & 0xFF)) != 0);
            }

            long var30 = var27;
            String var32 = var29;
            long var33 = var30;
            String var35 = var32;
            long var36 = var33;
            String var38 = var35;
            long var39 = var36;
            String var41 = var38;
            long var42 = var39;
            String var44 = var41;
            long var45 = var42;
            String var47 = var44;
            long var48 = var45;
            String var50 = var47;
            Object var51 = var1.next();
            long var52 = var48;
            String var54 = var50;
            long var55 = var52;
            String var57 = var54;
            long var58 = var55;
            String var60 = var57;
            boolean var61 = var51 instanceof String;
            if (var51 != null && !var61) {
               throw new ClassCastException("CHECKCAST failed");
            }

            long var65 = var58;
            String var67 = var60;
            long var71 = var65;
            String var73 = var67;
            long var74 = var71;
            String var76 = var73;
            long var77 = var74;
            String var79 = var76;
            long var80 = var77;
            String var82 = var79;
            long var83 = var80;
            String var85 = var82;
            long var86 = var83;
            String var88 = var85;
            long var89 = var86;
            String var91 = var88;
            long var92 = var89;
            String var94 = var91;
            long var95 = var92;
            String var97 = var94;
            long var98 = var95;
            String var100 = var97;
            long var101 = var98;
            String var103 = var100;
            boolean var104 = ((String)var51).equals("&&");
            long var105 = var101;
            String var107 = var103;
            long var108 = var105;
            String var110 = var107;
            long var111 = var108;
            String var113 = var110;
            if (!var104) {
               long var114 = var111;
               String var116 = var113;
               long var119 = var114;
               String var121 = var116;
               long var122 = var119;
               String var124 = var121;
               long var125 = var122;
               String var127 = var124;
               long var128 = var125;
               String var130 = var127;
               long var131 = var128;
               String var133 = var130;
               long var134 = var131;
               String var136 = var133;
               long var137 = var134;
               String var139 = var136;
               long var140 = var137;
               String var142 = var139;
               long var143 = var140;
               String var145 = var142;
               boolean var146 = ((String)var51).equals("||");
               long var147 = var143;
               String var149 = var145;
               long var150 = var147;
               String var152 = var149;
               long var153 = var150;
               String var155 = var152;
               if (!var146) {
                  byte var270;
                  long var271;
                  String var273;
                  label58: {
                     long var252;
                     String var254;
                     label57: {
                        long var156 = var153;
                        String var158 = var155;
                        long var165 = var156;
                        String var167 = var158;
                        long var168 = var165;
                        String var170 = var167;
                        long var171 = var168;
                        String var173 = var170;
                        long var174 = var171;
                        String var176 = var173;
                        boolean var177 = Boolean.parseBoolean((String)var51);
                        long var178 = var174;
                        String var180 = var176;
                        long var181 = var178;
                        String var183 = var180;
                        long var184 = var181;
                        String var186 = var183;
                        long var187 = var184;
                        String var189 = var186;
                        long var190 = var187;
                        String var192 = var189;
                        long var193 = var190;
                        String var195 = var192;
                        long var196 = var193;
                        String var198 = var195;
                        long var199 = var196;
                        String var201 = var198;
                        long var202 = var199;
                        String var204 = var201;
                        long var205 = var202;
                        String var207 = var204;
                        long var208 = var205;
                        String var210 = var207;
                        long var211 = var208;
                        String var213 = var210;
                        boolean var214 = var186.equals("&&");
                        long var215 = var211;
                        String var217 = var213;
                        long var218 = var215;
                        String var220 = var217;
                        long var221 = var218;
                        String var223 = var220;
                        long var267;
                        String var269;
                        if (!var214) {
                           long var224 = var221;
                           String var226 = var223;
                           long var234 = var224;
                           String var236 = var226;
                           long var237 = var234;
                           String var239 = var236;
                           long var243;
                           String var245;
                           if ((int)((int)var221 & 4294967295L) == 0) {
                              long var240 = var237;
                              String var242 = var239;
                              long var255 = var240;
                              String var257 = var242;
                              long var258 = var255;
                              String var260 = var257;
                              if (!var177) {
                                 long var261 = var258;
                                 String var263 = var260;
                                 var270 = 0;
                                 var271 = var261;
                                 var273 = var263;
                                 break label58;
                              }

                              var243 = var258;
                              var245 = var260;
                           } else {
                              var243 = var237;
                              var245 = var239;
                           }

                           long var264 = var243;
                           String var266 = var245;
                           var267 = var264;
                           var269 = var266;
                        } else {
                           long var227 = var221;
                           String var229 = var223;
                           long var246 = var227;
                           String var248 = var229;
                           long var249 = var246;
                           String var251 = var248;
                           if ((int)((int)var221 & 4294967295L) == 0) {
                              var252 = var249;
                              var254 = var251;
                              break label57;
                           }

                           if (!var177) {
                              var252 = var249;
                              var254 = var251;
                              break label57;
                           }

                           var267 = var249;
                           var269 = var251;
                        }

                        var270 = 1;
                        var271 = var267;
                        var273 = var269;
                        break label58;
                     }

                     long var274 = var252;
                     String var276 = var254;
                     var2 = 0L | var274 & -4294967296L;
                     var4 = var276;
                     continue;
                  }

                  var2 = var270 & 4294967295L | var271 & -4294967296L;
                  var4 = var273;
               } else {
                  long var159 = var153;
                  long var230 = var159;
                  long var232 = var230;
                  var2 = var232;
                  var4 = "||";
               }
            } else {
               long var117 = var111;
               long var161 = var117;
               long var163 = var161;
               var2 = var163;
               var4 = "&&";
            }
         }
      }

      public static String describeText(String text) {
         Pattern var1 = Pattern.compile("([\\w.]+)\\s*\\(");
         Matcher var2 = var1.matcher(text);
         boolean var3 = var2.find();
         String var4;
         if (!var3) {
            var4 = "";
         } else {
            String var5 = var2.group(1);
            var4 = var5;
         }

         return var4;
      }

      public static Arg<?>[] values(String text) {
         long var8 = 0L;
         boolean var1 = text.isEmpty();
         if (!var1) {
            String[] var2 = text.split(",");
            int var3 = var2.length;
            if (var3 != 0) {
               int var4 = var2.length;
               Arg[] var5 = new Arg[var4];
               long var6 = 0L;

               while (true) {
                  long var10 = (int)var6 & 4294967295L | var8 & -4294967296L;
                  long var12 = var10;
                  long var14 = var12;
                  long var16 = var14;
                  int var18 = var2.length;
                  long var19 = var16;
                  long var21 = var19;
                  if ((int)((int)var6 & 4294967295L) >= var18) {
                     Arg[] var26 = var5;
                     return var26;
                  }

                  long var23 = var21;
                  long var31 = var23;
                  long var33 = var31;
                  long var35 = var33;
                  long var37 = var35;
                  Object var39 = var2[(int)((int)var6 & 4294967295L)];
                  long var40 = var37;
                  long var42 = var40;
                  long var44 = var42;
                  long var46 = var44;
                  long var48 = var46;
                  long var50 = var48;
                  long var52 = var50;
                  long var54 = var52;
                  long var56 = var54;
                  long var58 = var56;
                  long var60 = var58;
                  String[] var62 = ((String)var39).split(" ");
                  long var63 = var60;
                  long var65 = var63;
                  long var67 = var65;
                  long var69 = var67;
                  long var71 = var69;
                  int var73 = var62.length;
                  long var74 = var71;
                  long var76 = var74;
                  long var78 = var76;
                  long var80 = var78;
                  if (var73 != 2) {
                     ScriptManager.UnknownArg var84 = new ScriptManager.UnknownArg();
                     throw var84;
                  }

                  long var82 = var80;
                  long var85 = var82;
                  long var87 = var85;
                  long var89 = var87;
                  long var91 = var89;
                  Object var93 = var62[0];
                  long var94 = var91;
                  long var96 = var94;
                  long var98 = var96;
                  long var100 = var98;
                  Class<?> var102 = ScriptCompiler.resolveClassValue4((String)var93);
                  long var103 = var100;
                  long var105 = var103;
                  long var107 = var105;
                  long var109 = var107;
                  long var111 = var109;
                  long var113 = var111;
                  long var115 = var113;
                  Arg var117;
                  boolean var120 = true;
                  long var121;
                  if (!var120) {
                     long var118 = var115;
                     var121 = var118;
                  } else {
                     var121 = var115;
                  }

                  long var123 = var121;
                  long var125 = var123;
                  long var127 = var125;
                  long var129 = var127;
                  long var131 = var129;
                  long var133 = var131;
                  long var135 = var133;
                  long var137 = var135;
                  Object var139 = var62[1];
                  long var140 = var137;
                  long var142 = var140;
                  long var144 = var142;
                  long var146 = var144;
                  long var148 = var146;
                  long var150 = var148;
                  long var152 = var150;
                  long var154 = var152;
                  var117 = new Arg((String)var139, var102);
                  long var156 = var154;
                  var5[(int)((int)var111 & 4294967295L)] = var117;
                  long var10000 = (int)var156 + 1 & 4294967295L;
                  var8 = var156;
                  var6 = var10000;
               }
            }

            String var27 = text.trim();
            boolean var28 = var27.isEmpty();
            if (!var28) {
               Arg[] var29 = new Arg[1];
               Arg var30 = resolveArg(text);
               var29[0] = var30;
               Arg[] var26 = var29;
               return var26;
            }
         }

         Arg[] var25 = new Arg[0];
         Arg[] var26 = var25;
         return var26;
      }

      public static ScriptMember<?> resolveScriptMember(Script script, String text, String text2, List<String> list) {
         Pattern var4 = Pattern.compile(
            "^(func|def|method|function|fun|fn)\\s+(null|nil|nullptr|int|integer|double|float|boolean|string|void)\\s+(&)?([a-zA-Z_]\\w*)\\s*\\(([^)]*)\\)\\s*\\{"
         );
         Matcher var5 = var4.matcher(text);
         boolean var6 = var5.find();
         if (!var6) {
            String var14 = "Invalid method declaration: " + text;
            IllegalArgumentException var7 = new IllegalArgumentException(var14);
            throw var7;
         }

         String var17;
         Class var19;
         ScriptMember var21;
         boolean var31;
         label36: {
            label35: {
               String var15 = var5.group(2);
               String var16 = var5.group(3);
               var17 = var5.group(4);
               String var18 = var5.group(5);
               var19 = ScriptCompiler.resolveClassValue(var15);
               Arg[] var20 = values(var18);
               var21 = new ScriptMember(script, var17, var19, var20);
               var21.handleList(list);
               if (var16 != null) {
                  Arg[] var22 = var21.getArgArray();
                  int var23 = var22.length;
                  if (var23 == 1) {
                     EventClassRegistry var24 = EventClassRegistry.INSTANCE;
                     Arg[] var25 = var21.getArgArray();
                     Object var26 = var25[0];
                     Class<?> var27 = ((Arg)var26).getValueClassValue();
                     String var28 = var27.getSimpleName();
                     Class<?> var29 = var24.resolveClassValue(var28);
                     if (var29 != null) {
                        break label35;
                     }
                  }

                  boolean var30 = var17.equals("unload");
                  if (var30) {
                     break label35;
                  }
               }

               var31 = false;
               break label36;
            }

            var31 = true;
         }

         var21.setFlag(var31);
         if (var19 != null) {
            boolean var32 = Void.class == var19;
            if (!var32) {
               boolean var33 = testText2(text2);
               if (!var33) {
                  String var41 = "Method " + var17 + " must have a return statement.";
                  IllegalArgumentException var34 = new IllegalArgumentException(var41);
                  throw var34;
               }
            }
         }

         return var21;
      }

      public static void handleScript(Script script, String text, String text2, List<String> list) {
         Pattern var4 = Pattern.compile("if\\s*\\(\\s*([^()]*?)\\s*\\)\\s*\\{?|\\bif\\s*\\{?");
         Matcher var5 = var4.matcher(text);
         boolean var6 = var5.find();
         if (!var6) {
            String var14 = "Invalid method declaration: " + text;
            IllegalArgumentException var7 = new IllegalArgumentException(var14);
            throw var7;
         }

         String var15 = var5.group(1);
         if (var15 == null) {
            boolean var16 = text2.contains("@");
            if (!var16) {
               IllegalArgumentException var17 = new IllegalArgumentException("If statement without instant args used without condition.");
               throw var17;
            }
         }

         if (var15 == null) {
            Iterator var18 = list.iterator();

            while (true) {
               boolean var19 = var18.hasNext();
               if (!var19) {
                  break;
               }

               Object var20 = var18.next();
               boolean var21 = var20 instanceof String;
               if (var20 != null && !var21) {
                  throw new ClassCastException("CHECKCAST failed");
               }

               boolean var22 = ScriptKeywords.testText((String)var20);
               if (!var22) {
                  Pattern var23 = Pattern.compile("@\\(([^()]*?)\\)");
                  String var24 = ((String)var20).trim();
                  Matcher var25 = var23.matcher(var24);
                  boolean var26 = var25.find();
                  if (var26) {
                     var25.group(1);
                  }
               }
            }
         }
      }

      public static String describeText2(String text) {
         String var1 = text.replaceAll("\\band\\b", "&&");
         String var2 = var1.replaceAll("\\bor\\b", "||");
         return var2.trim();
      }

      public static void handleScript2(Script script, ScriptHook scriptHook) {
         script.getScriptHookList().add(scriptHook);
      }

      public static void handleText(String text, Consumer<Var<?>> consumer) {
         Pattern var2 = Pattern.compile(
            "^(int|integer|double|float|floating|precise|boolean|bool|bl|string|let|var|variable|typedef)\\s+(const|static|final)?\\s*([a-zA-Z_]\\w*)\\s*(=|#|=>|:)?\\s*([^;]*)\\s*;"
         );
         Matcher var3 = var2.matcher(text);
         boolean var4 = var3.find();
         if (!var4) {
            String var12 = "Invalid variable declaration: " + text;
            IllegalArgumentException var5 = new IllegalArgumentException(var12);
            throw var5;
         }

         String var13;
         String var14;
         String var15;
         String var16;
         byte var20;
         label63: {
            var13 = var3.group(1);
            var14 = var3.group(2);
            var15 = var3.group(3);
            var3.group(4);
            var16 = var3.group(5);
            boolean var17 = var16.equals("null");
            if (!var17) {
               boolean var18 = var16.equals("nil");
               if (!var18) {
                  boolean var19 = var16.equals("nullptr");
                  if (!var19) {
                     var20 = 0;
                     break label63;
                  }
               }
            }

            var20 = 1;
         }

         boolean var21;
         if (var14 == null) {
            var21 = false;
         } else {
            var21 = true;
         }

         Class<?> var22 = ScriptCompiler.resolveClassValue2(var13, var16);
         boolean var23 = var22.equals(Integer.class);
         Var var24;
         if (var23) {
            Integer var29;
            if ((int)(var20 & 4294967295L) == 0) {
               long var26 = Long.parseLong(var16);
               Integer var28 = (int)var26;
               var29 = var28;
            } else {
               var29 = null;
            }

            Var var25 = new Var<>(var15, Integer.class, var29);
            var24 = var25;
         } else {
            var24 = null;
         }

         boolean var38 = var22.equals(Double.class);
         Var var42;
         if (var38) {
            Double var47;
            if ((int)(var20 & 4294967295L) == 0) {
               double var44 = Double.parseDouble(var16);
               Double var46 = var44;
               var47 = var46;
            } else {
               var47 = null;
            }

            Var var43 = new Var<>(var15, Double.class, var47);
            var42 = var43;
         } else {
            var42 = var24;
         }

         boolean var56 = var22.equals(String.class);
         Var var60;
         if (var56) {
            String var62;
            if ((int)(var20 & 4294967295L) == 0) {
               var62 = var16;
            } else {
               var62 = null;
            }

            Var var61 = new Var<>(var15, String.class, var62);
            var60 = var61;
         } else {
            var60 = var42;
         }

         if (var60 != null) {
            if (var21) {
               var60.run();
            }

            consumer.accept(var60);
         }
      }

      public static boolean testText(String text) {
         long var6 = 0L;
         String[] var1 = text.split(" ");
         ArrayList var2 = new ArrayList();
         int var3 = var1.length;
         long var4 = 0L;

         while (true) {
            long var8 = (int)var4 & 4294967295L | var6 & -4294967296L;
            long var10 = var8;
            long var12 = var10;
            long var14 = var12;
            if ((int)((int)var4 & 4294967295L) >= var3) {
               return testList(var2);
            }

            Object var104;
            long var105;
            label23: {
               long var16 = var14;
               long var18 = var16;
               long var20 = var18;
               long var22 = var20;
               long var24 = var22;
               Object var26 = var1[(int)((int)var4 & 4294967295L)];
               long var27 = var24;
               long var29 = var27;
               long var31 = var29;
               long var33 = var31;
               long var35 = var33;
               long var37 = var35;
               long var39 = var37;
               long var41 = var39;
               long var43 = var41;
               long var45 = var43;
               long var47 = var45;
               boolean var49 = ((String)var26).equals("&&");
               long var50 = var47;
               long var52 = var50;
               long var54 = var52;
               long var58;
               if (!var49) {
                  long var56 = var54;
                  long var61 = var56;
                  long var63 = var61;
                  long var65 = var63;
                  long var67 = var65;
                  long var69 = var67;
                  long var71 = var69;
                  long var73 = var71;
                  long var75 = var73;
                  long var77 = var75;
                  boolean var79 = ((String)var26).equals("||");
                  long var80 = var77;
                  long var82 = var80;
                  long var84 = var82;
                  if (!var79) {
                     long var86 = var84;
                     long var111 = var86;
                     long var113 = var111;
                     long var115 = var113;
                     long var117 = var115;
                     long var119 = var117;
                     long var121 = var119;
                     long var123 = var121;
                     long var125 = var123;
                     long var127 = var125;
                     var104 = "true";
                     var105 = var127;
                     break label23;
                  }

                  var58 = var84;
               } else {
                  var58 = var54;
               }

               long var88 = var58;
               long var90 = var88;
               long var92 = var90;
               long var94 = var92;
               long var96 = var94;
               long var98 = var96;
               long var100 = var98;
               long var102 = var100;
               var104 = var26;
               var105 = var102;
            }

            var2.add(var104);
            long var107 = var105;
            long var109 = var107;
            long var10000 = (int)var109 + 1 & 4294967295L;
            var6 = var109;
            var4 = var10000;
         }
      }

      public static void handleText2(String text, Consumer<ScriptHook> consumer) {
         final String var2 = describeText(text);
         String[] var3 = values2(text);
         final Object[] var4 = ScriptCompiler.values(var3);
         String var12 = "invokestatic&" + var2;
         var var5 = new ScriptHook(var12) {
            @Override
            public void handleScriptObject(ScriptObject scriptObject, Object... objects) {
               ScriptMember<?> scriptMember = scriptObject.resolveScriptMember(var2);
               if (scriptMember != null) {
                  scriptMember.handleObject(var4);
               }
            }
         };
         consumer.accept(var5);
      }

      public static void handleScript3(Script script, Var varValue) {
         script.getVarValueList().add(varValue);
      }

      public static int computeText(String text, char c) {
         char[] var2 = text.toCharArray();
         int var3 = var2.length;
         long var4 = 0L;
         long var6 = 0L;

         while (true) {
            long var8 = var4;
            long var10 = var6;
            long var12 = var8;
            long var14 = var10;
            long var16 = var12;
            long var18 = var14;
            long var20 = var16;
            long var22 = var18;
            if ((int)((int)var4 & 4294967295L) >= var3) {
               return (int)((int)((int)var22 & 4294967295L) & 4294967295L);
            }

            long var24 = var20;
            long var26 = var22;
            long var28 = var24;
            long var30 = var26;
            long var32 = var28;
            long var34 = var30;
            long var36 = var32;
            long var38 = var34;
            long var40 = var36;
            long var42 = var38;
            int var44 = var2[(int)((int)var32 & 4294967295L)];
            long var45 = var40;
            long var47 = var42;
            long var49 = var45;
            long var51 = var47;
            long var53 = var49;
            long var55 = var51;
            long var57 = var53;
            long var59 = var55;
            long var61 = var57;
            long var63 = var59;
            long var65 = var61;
            long var67 = var63;
            long var73;
            long var75;
            if ((int)(var44 & 65535 & 65535) == (int)(c & 65535 & 65535)) {
               long var69 = var65;
               long var71 = var67;
               long var10000 = (int)var71 + 1 & 4294967295L;
               var73 = var69;
               var75 = var10000;
            } else {
               var73 = var65;
               var75 = var67;
            }

            var4 = (int)var73 + 1 & 4294967295L;
            var6 = var75;
         }
      }

      public static String[] values2(String text) {
         long var13 = 0L;
         Pattern var1 = Pattern.compile("\\(([^)]*)\\)");
         Matcher var2 = var1.matcher(text);
         boolean var3 = var2.find();
         Object[] var5;
         if (!var3) {
            String[] var4 = new String[0];
            var5 = var4;
         } else {
            String var6 = var2.group(1);
            ArrayList var7 = new ArrayList();
            StringBuilder var8 = new StringBuilder();
            long var9 = 0L;
            long var11 = 0L;

            while (true) {
               long var15 = (int)var11 & 4294967295L | var13 & -4294967296L;
               long var17 = var9;
               long var19 = var15;
               long var21 = var17;
               long var23 = var19;
               long var25 = var21;
               long var27 = var23;
               long var29 = var25;
               long var31 = var27;
               long var33 = var29;
               long var35 = var31;
               long var37 = var33;
               long var39 = var35;
               long var41 = var37;
               long var43 = var39;
               long var45 = var41;
               int var47 = var6.length();
               long var48 = var43;
               long var50 = var45;
               long var52 = var48;
               long var54 = var50;
               long var56 = var52;
               long var58 = var54;
               if ((int)((int)var11 & 4294967295L) >= var47) {
                  boolean var175 = var8.isEmpty();
                  if (!var175) {
                     String var176 = var8.toString();
                     String var177 = var176.trim();
                     var7.add(var177);
                  }

                  String[] var178 = new String[0];
                  Object[] var179 = var7.toArray(var178);
                  boolean var180 = var179 instanceof String[];
                  if (var179 != null && !var180) {
                     throw new ClassCastException("CHECKCAST failed");
                  }

                  var5 = var179;
                  break;
               }

               long var60 = var56;
               long var62 = var58;
               long var64 = var60;
               long var66 = var62;
               long var68 = var64;
               long var70 = var66;
               long var10000 = (int)var68 & 4294967295L;
               long var72 = var68;
               long var74 = var70;
               long var78 = (int)var68 & 4294967295L;
               long var80 = var72;
               long var82 = var74;
               int var84 = (int)((int)var68 & 4294967295L);
               long var85 = var80;
               long var87 = var82;
               int var89 = (int)((int)var68 & 4294967295L);
               long var90 = var85;
               long var92 = var87;
               int var94 = (int)((int)var68 & 4294967295L);
               long var95 = var90;
               long var97 = var92;
               int var99 = (int)((int)var68 & 4294967295L);
               long var100 = var95;
               long var102 = var97;
               int var104 = var6.charAt((int)((int)var68 & 4294967295L));
               long var105 = var104 & 65535 & 65535 | (int)((int)var68 & 4294967295L) & -65536L;
               long var107 = var100;
               long var109 = var102;
               var10000 = var104 & 65535 & 65535 & 65535;
               long var111 = var104 & 65535 & 65535 | (int)((int)var68 & 4294967295L) & -65536L;
               long var113 = var107;
               long var115 = var109;
               long var10002 = var104 & 65535 & 65535 | (int)((int)var68 & 4294967295L) & -65536L;
               long var119 = var104 & 65535 & 65535 & 65535;
               long var123 = var113;
               long var125 = var115;
               var10000 = (int)(var104 & 65535 & 65535 & 65535) & 4294967295L;
               long var127 = (int)(var104 & 65535 & 65535 & 65535) & 4294967295L
                  | (var104 & 65535 & 65535 | (int)((int)var68 & 4294967295L) & -65536L) & -4294967296L;
               long var129 = var123;
               long var131 = var125;
               var10000 = (int)(var104 & 65535 & 65535 & 65535) & 4294967295L;
               long var135 = (int)(var104 & 65535 & 65535 & 65535) & 4294967295L
                  | (var104 & 65535 & 65535 | (int)((int)var68 & 4294967295L) & -65536L) & -4294967296L;
               long var137 = var129;
               long var139 = var131;
               var10000 = (int)(var104 & 65535 & 65535 & 65535) & 4294967295L;
               long var143 = (int)(var104 & 65535 & 65535 & 65535) & 4294967295L
                  | (var104 & 65535 & 65535 | (int)((int)var68 & 4294967295L) & -65536L) & -4294967296L;
               long var145 = var137;
               long var147 = var139;
               var10000 = (int)(var104 & 65535 & 65535 & 65535) & 4294967295L;
               long var151 = (int)(var104 & 65535 & 65535 & 65535) & 4294967295L
                  | (var104 & 65535 & 65535 | (int)((int)var68 & 4294967295L) & -65536L) & -4294967296L;
               long var153 = var145;
               long var155 = var147;
               long var207;
               long var209;
               long var213;
               if ((int)((int)(var104 & 65535 & 65535 & 65535) & 4294967295L) == 34) {
                  var10000 = (int)(var104 & 65535 & 65535 & 65535) & 4294967295L;
                  long var159 = (int)(var104 & 65535 & 65535 & 65535) & 4294967295L
                     | (var104 & 65535 & 65535 | (int)((int)var68 & 4294967295L) & -65536L) & -4294967296L;
                  long var161 = var153;
                  long var163 = var155;
                  var10000 = (int)(var104 & 65535 & 65535 & 65535) & 4294967295L;
                  long var181 = (int)(var104 & 65535 & 65535 & 65535) & 4294967295L
                     | (var104 & 65535 & 65535 | (int)((int)var68 & 4294967295L) & -65536L) & -4294967296L;
                  long var183 = var161;
                  long var185 = var163;
                  var10000 = (int)(var104 & 65535 & 65535 & 65535) & 4294967295L;
                  long var189 = (int)(var104 & 65535 & 65535 & 65535) & 4294967295L
                     | (var104 & 65535 & 65535 | (int)((int)var68 & 4294967295L) & -65536L) & -4294967296L;
                  long var191 = var183;
                  long var193 = var185;
                  byte var253;
                  long var256;
                  long var258;
                  if ((int)((int)var163 & 4294967295L) != 0) {
                     var10000 = (int)(var104 & 65535 & 65535 & 65535) & 4294967295L;
                     long var10003 = (int)(var104 & 65535 & 65535 & 65535) & 4294967295L
                        | (var104 & 65535 & 65535 | (int)((int)var68 & 4294967295L) & -65536L) & -4294967296L;
                     var253 = 0;
                     var256 = var191;
                     var258 = var193;
                  } else {
                     var10000 = (int)(var104 & 65535 & 65535 & 65535) & 4294967295L;
                     long var197 = (int)(var104 & 65535 & 65535 & 65535) & 4294967295L
                        | (var104 & 65535 & 65535 | (int)((int)var68 & 4294967295L) & -65536L) & -4294967296L;
                     long var199 = var191;
                     long var201 = var193;
                     var10000 = (int)(var104 & 65535 & 65535 & 65535) & 4294967295L;
                     long var270 = (int)(var104 & 65535 & 65535 & 65535) & 4294967295L
                        | (var104 & 65535 & 65535 | (int)((int)var68 & 4294967295L) & -65536L) & -4294967296L;
                     long var272 = var199;
                     long var274 = var201;
                     var10000 = (int)(var104 & 65535 & 65535 & 65535) & 4294967295L;
                     long var555 = (int)(var104 & 65535 & 65535 & 65535) & 4294967295L
                        | (var104 & 65535 & 65535 | (int)((int)var68 & 4294967295L) & -65536L) & -4294967296L;
                     var253 = 1;
                     var256 = var272;
                     var258 = var274;
                  }

                  var10000 = (int)(var104 & 65535 & 65535 & 65535) & 4294967295L;
                  long var262 = (int)(var104 & 65535 & 65535 & 65535) & 4294967295L
                     | (var104 & 65535 & 65535 | (int)((int)var68 & 4294967295L) & -65536L) & -4294967296L;
                  long var264 = var256;
                  long var266 = var258;
                  var10000 = var253 & 4294967295L;
                  long var10001 = (int)(var104 & 65535 & 65535 & 65535) & 4294967295L;
                  var10002 = var253 & 4294967295L | var266 & -4294967296L;
                  long var205 = (int)(var104 & 65535 & 65535 & 65535) & 4294967295L
                     | (var104 & 65535 & 65535 | (int)((int)var68 & 4294967295L) & -65536L) & -4294967296L;
                  var207 = var264;
                  var209 = var10002;
                  var213 = var10000;
               } else {
                  var10000 = (int)(var104 & 65535 & 65535 & 65535) & 4294967295L;
                  long var167 = (int)(var104 & 65535 & 65535 & 65535) & 4294967295L
                     | (var104 & 65535 & 65535 | (int)((int)var68 & 4294967295L) & -65536L) & -4294967296L;
                  long var169 = var153;
                  long var171 = var155;
                  var10000 = (int)var171 & 4294967295L;
                  long var552 = (int)(var104 & 65535 & 65535 & 65535) & 4294967295L;
                  long var531 = (int)(var104 & 65535 & 65535 & 65535) & 4294967295L
                     | (var104 & 65535 & 65535 | (int)((int)var68 & 4294967295L) & -65536L) & -4294967296L;
                  var207 = var169;
                  var209 = var171;
                  var213 = var10000;
               }

               long var352;
               long var354;
               label50: {
                  var10000 = (int)((int)(var104 & 65535 & 65535 & 65535) & 4294967295L) & 4294967295L;
                  long var215 = (int)(var104 & 65535 & 65535 & 65535) & 4294967295L
                     | (var104 & 65535 & 65535 | (int)((int)var68 & 4294967295L) & -65536L) & -4294967296L;
                  long var217 = var207;
                  long var219 = var209;
                  long var221 = var10000;
                  long var223 = (int)(var104 & 65535 & 65535 & 65535) & 4294967295L
                     | (var104 & 65535 & 65535 | (int)((int)var68 & 4294967295L) & -65536L) & -4294967296L;
                  long var225 = var217;
                  long var227 = var219;
                  long var229 = (int)(var104 & 65535 & 65535 & 65535) & 4294967295L
                     | (var104 & 65535 & 65535 | (int)((int)var68 & 4294967295L) & -65536L) & -4294967296L;
                  long var231 = var225;
                  long var233 = var227;
                  long var235 = (int)(var104 & 65535 & 65535 & 65535) & 4294967295L
                     | (var104 & 65535 & 65535 | (int)((int)var68 & 4294967295L) & -65536L) & -4294967296L;
                  long var237 = var231;
                  long var239 = var233;
                  long var249;
                  long var251;
                  if ((int)var221 == 44) {
                     long var241 = (int)(var104 & 65535 & 65535 & 65535) & 4294967295L
                        | (var104 & 65535 & 65535 | (int)((int)var68 & 4294967295L) & -65536L) & -4294967296L;
                     long var243 = var237;
                     long var245 = var239;
                     long var278 = (int)(var104 & 65535 & 65535 & 65535) & 4294967295L
                        | (var104 & 65535 & 65535 | (int)((int)var68 & 4294967295L) & -65536L) & -4294967296L;
                     long var280 = var243;
                     long var282 = var245;
                     long var284 = (int)(var104 & 65535 & 65535 & 65535) & 4294967295L
                        | (var104 & 65535 & 65535 | (int)((int)var68 & 4294967295L) & -65536L) & -4294967296L;
                     long var286 = var280;
                     long var288 = var282;
                     if ((int)((int)var213 & 4294967295L) == 0) {
                        long var290 = var286;
                        long var292 = var288;
                        long var356 = var290;
                        long var358 = var292;
                        long var360 = var356;
                        long var362 = var358;
                        long var364 = var360;
                        long var366 = var362;
                        long var368 = var364;
                        long var370 = var366;
                        long var372 = var368;
                        long var374 = var370;
                        long var376 = var372;
                        long var378 = var374;
                        boolean var380 = var8.isEmpty();
                        long var381 = var376;
                        long var383 = var378;
                        long var385 = var381;
                        long var387 = var383;
                        long var389 = var385;
                        long var391 = var387;
                        if (!var380) {
                           long var393 = var389;
                           long var395 = var391;
                           long var397 = var393;
                           long var399 = var395;
                           long var401 = var397;
                           long var403 = var399;
                           long var405 = var401;
                           long var407 = var403;
                           long var409 = var405;
                           long var411 = var407;
                           long var413 = var409;
                           long var415 = var411;
                           long var417 = var413;
                           long var419 = var415;
                           long var421 = var417;
                           long var423 = var419;
                           long var425 = var421;
                           long var427 = var423;
                           String var429 = var8.toString();
                           long var430 = var425;
                           long var432 = var427;
                           long var434 = var430;
                           long var436 = var432;
                           long var438 = var434;
                           long var440 = var436;
                           long var442 = var438;
                           long var444 = var440;
                           long var446 = var442;
                           long var448 = var444;
                           long var450 = var446;
                           long var452 = var448;
                           long var454 = var450;
                           long var456 = var452;
                           String var458 = var429.trim();
                           long var459 = var454;
                           long var461 = var456;
                           long var463 = var459;
                           long var465 = var461;
                           long var467 = var463;
                           long var469 = var465;
                           long var471 = var467;
                           long var473 = var469;
                           long var475 = var471;
                           long var477 = var473;
                           long var479 = var475;
                           long var481 = var477;
                           long var483 = var479;
                           long var485 = var481;
                           var7.add(var458);
                           long var487 = var483;
                           long var489 = var485;
                           long var491 = var487;
                           long var493 = var489;
                           long var495 = var491;
                           long var497 = var493;
                           long var499 = var495;
                           long var501 = var497;
                           long var503 = var499;
                           long var505 = var501;
                           long var507 = var503;
                           long var509 = var505;
                           long var511 = var507;
                           long var513 = var509;
                           long var515 = var511;
                           long var517 = var513;
                           long var519 = var515;
                           long var521 = var517;
                           long var523 = var519;
                           long var525 = var521;
                           long var527 = var523;
                           long var529 = var525;
                           var8.setLength(0);
                           var352 = var527;
                           var354 = var529;
                        } else {
                           var352 = var389;
                           var354 = var391;
                        }
                        break label50;
                     }

                     long var247 = (int)(var104 & 65535 & 65535 & 65535) & 4294967295L
                        | (var104 & 65535 & 65535 | (int)((int)var68 & 4294967295L) & -65536L) & -4294967296L;
                     var249 = var286;
                     var251 = var288;
                  } else {
                     long var532 = (int)(var104 & 65535 & 65535 & 65535) & 4294967295L
                        | (var104 & 65535 & 65535 | (int)((int)var68 & 4294967295L) & -65536L) & -4294967296L;
                     var249 = var237;
                     var251 = var239;
                  }

                  long var294 = (int)(var104 & 65535 & 65535 & 65535) & 4294967295L
                     | (var104 & 65535 & 65535 | (int)((int)var68 & 4294967295L) & -65536L) & -4294967296L;
                  long var296 = var249;
                  long var298 = var251;
                  long var300 = (int)(var104 & 65535 & 65535 & 65535) & 4294967295L
                     | (var104 & 65535 & 65535 | (int)((int)var68 & 4294967295L) & -65536L) & -4294967296L;
                  long var302 = var296;
                  long var304 = var298;
                  var10000 = (int)(var104 & 65535 & 65535 & 65535) & 4294967295L;
                  long var306 = (int)(var104 & 65535 & 65535 & 65535) & 4294967295L
                     | (var104 & 65535 & 65535 | (int)((int)var68 & 4294967295L) & -65536L) & -4294967296L;
                  long var308 = var302;
                  long var310 = var304;
                  long var312 = var10000;
                  var10002 = (int)(var104 & 65535 & 65535 & 65535) & 4294967295L
                     | (var104 & 65535 & 65535 | (int)((int)var68 & 4294967295L) & -65536L) & -4294967296L;
                  long var318 = var308;
                  long var320 = var310;
                  long var322 = (int)var312 & 65535 & 65535
                     | (
                           (int)(var104 & 65535 & 65535 & 65535) & 4294967295L
                              | (var104 & 65535 & 65535 | (int)((int)var68 & 4294967295L) & -65536L) & -4294967296L
                        )
                        & -65536L;
                  long var324 = var318;
                  long var326 = var320;
                  long var328 = var324;
                  long var330 = var326;
                  long var332 = var328;
                  long var334 = var330;
                  long var336 = var332;
                  long var338 = var334;
                  var8.append((char)((int)((int)var322 & 65535 & 65535) & 65535));
                  long var340 = var336;
                  long var342 = var338;
                  long var344 = var340;
                  long var346 = var342;
                  long var348 = var344;
                  long var350 = var346;
                  var352 = var348;
                  var354 = var350;
               }

               var10000 = (int)var352 + 1 & 4294967295L;
               var13 = var352;
               var9 = var354;
               var11 = var10000;
            }
         }

         return (String[])var5;
      }

      public static Arg<?> resolveArg(String text) {
         Pattern var1 = Pattern.compile("^(int|double|float|boolean|string)\\s+([a-zA-Z_]\\w*)");
         Matcher var2 = var1.matcher(text);
         boolean var3 = var2.find();
         if (!var3) {
            String var11 = "Invalid variable declaration: " + text;
            IllegalArgumentException var4 = new IllegalArgumentException(var11);
            throw var4;
         }

         String var12 = var2.group(1);
         String var13 = var2.group(2);
         Class<?> var14 = ScriptCompiler.resolveClassValue4(var12);
         boolean var15 = var14.equals(Integer.class);
         Arg var16;
         if (var15) {
            Arg var17 = new Arg<>(var13, Integer.class);
            var16 = var17;
         } else {
            var16 = null;
         }

         boolean var25 = var14.equals(Double.class);
         Arg var29;
         if (var25) {
            Arg var30 = new Arg<>(var13, Double.class);
            var29 = var30;
         } else {
            var29 = var16;
         }

         boolean var39 = var14.equals(String.class);
         Arg var43;
         if (var39) {
            Arg var44 = new Arg<>(var13, String.class);
            var43 = var44;
         } else {
            var43 = var29;
         }

         return var43;
      }

      @SneakyThrows public static void handleScript4(Script script, String[] texts) {
         MethodType var2716 = null;
         CallSite var2110 = null;
         String var1539 = null;
         CallSite var1035 = null;
         Lookup var2319 = null;
         CallSite var1936 = null;
         long var1860 = 0L;
         Lookup var1949 = null;
         long var15 = 0L;
         StringBuilder var2 = new StringBuilder();
         StringBuilder var3 = new StringBuilder();
         ArrayList var4 = new ArrayList();
         ArrayList var5 = new ArrayList();
         int var6 = texts.length;
         long var10003 = (var6 & 4294967295L) << 32;
         long var7 = 0L << 32 | 0L;
         long var9 = var10003;
         String var11 = null;
         String var12 = null;
         long var13 = 0L;

         while (true) {
            var10003 = (int)var13 & 4294967295L | var9 & -4294967296L;
            long var17 = var7;
            long var19 = var10003;
            String var21 = var11;
            String var22 = var12;
            long var23 = var15;
            long var25 = var17;
            long var27 = var19;
            String var29 = var21;
            String var30 = var22;
            long var31 = var23;
            long var33 = var25;
            long var35 = var27;
            String var37 = var29;
            String var38 = var30;
            long var39 = var31;
            long var41 = var33;
            long var43 = var35;
            String var45 = var37;
            String var46 = var38;
            long var47 = var39;
            if ((int)((int)var13 & 4294967295L) >= (int)((int)(var27 >>> 32) & 4294967295L)) {
               return;
            }

            long var49 = var41;
            long var51 = var43;
            String var53 = var45;
            String var54 = var46;
            long var55 = var47;
            long var57 = var49;
            long var59 = var51;
            String var61 = var53;
            String var62 = var54;
            long var63 = var55;
            long var65 = var57;
            long var67 = var59;
            String var69 = var61;
            String var70 = var62;
            long var71 = var63;
            long var73 = var65;
            long var75 = var67;
            String var77 = var69;
            String var78 = var70;
            long var79 = var71;
            long var81 = var73;
            long var83 = var75;
            String var85 = var77;
            String var86 = var78;
            long var87 = var79;
            Object var89 = texts[(int)((int)var13 & 4294967295L)];
            long var90 = var81;
            long var92 = var83;
            String var94 = var85;
            String var95 = var86;
            long var96 = var87;
            long var98 = var90;
            long var100 = var92;
            String var102 = var94;
            String var103 = var95;
            long var104 = var96;
            long var106 = var98;
            long var108 = var100;
            String var110 = var102;
            String var111 = var103;
            long var112 = var104;
            long var114 = var106;
            long var116 = var108;
            String var118 = var110;
            String var119 = var111;
            long var120 = var112;
            long var122 = var114;
            long var124 = var116;
            String var126 = var118;
            String var127 = var119;
            long var128 = var120;
            long var130 = var122;
            long var132 = var124;
            String var134 = var126;
            String var135 = var127;
            long var136 = var128;
            long var138 = var130;
            long var140 = var132;
            String var142 = var134;
            String var143 = var135;
            long var144 = var136;
            int var146 = ((String)var89).length();
            long var147 = var138;
            long var149 = var140;
            String var151 = var142;
            String var152 = var143;
            long var153 = var144;
            long var155 = var147;
            long var157 = var149;
            String var159 = var151;
            String var160 = var152;
            long var161 = var153;
            long var163 = var155;
            long var165 = var157;
            String var167 = var159;
            String var168 = var160;
            long var169 = var161;
            long var171 = var163;
            long var173 = var165;
            String var175 = var167;
            String var176 = var168;
            long var177 = var169;
            long var179 = var171;
            long var181 = var173;
            String var183 = var175;
            String var184 = var176;
            long var185 = var177;
            long var195;
            long var197;
            String var199;
            String var200;
            long var201;
            if (var146 <= 1024) {
               long var187 = var179;
               long var189 = var181;
               String var191 = var183;
               String var192 = var184;
               long var193 = var185;
               long var203 = var187;
               long var205 = var189;
               String var207 = var191;
               String var208 = var192;
               long var209 = var193;
               long var211 = var203;
               long var213 = var205;
               String var215 = var207;
               String var216 = var208;
               long var217 = var209;
               long var219 = var211;
               long var221 = var213;
               String var223 = var215;
               String var224 = var216;
               long var225 = var217;
               long var227 = var219;
               long var229 = var221;
               String var231 = var223;
               String var232 = var224;
               long var233 = var225;
               long var235 = var227;
               long var237 = var229;
               String var239 = var231;
               String var240 = var232;
               long var241 = var233;
               String var243 = ((String)var89).trim();
               long var244 = var235;
               long var246 = var237;
               String var248 = var239;
               String var249 = var240;
               long var250 = var241;
               long var252 = var244;
               long var254 = var246;
               String var256 = var248;
               String var257 = var249;
               long var258 = var250;
               long var260 = var252;
               long var262 = var254;
               String var264 = var256;
               String var265 = var257;
               long var266 = var258;
               long var268 = var260;
               long var270 = var262;
               String var272 = var264;
               String var273 = var265;
               long var274 = var266;
               long var276 = var268;
               long var278 = var270;
               String var280 = var272;
               String var281 = var273;
               long var282 = var274;
               long var284 = var276;
               long var286 = var278;
               String var288 = var280;
               String var289 = var281;
               long var290 = var282;
               long var292 = var284;
               long var294 = var286;
               String var296 = var288;
               String var297 = var289;
               long var298 = var290;
               long var300 = var292;
               long var302 = var294;
               String var304 = var296;
               String var305 = var297;
               long var306 = var298;
               long var308 = var300;
               long var310 = var302;
               String var312 = var304;
               String var313 = var305;
               long var314 = var306;
               long var316 = var308;
               long var318 = var310;
               String var320 = var312;
               String var321 = var313;
               long var322 = var314;
               long var324 = var316;
               long var326 = var318;
               String var328 = var320;
               String var329 = var321;
               long var330 = var322;
               boolean var332 = var243.startsWith("//");
               long var333 = var324;
               long var335 = var326;
               String var337 = var328;
               String var338 = var329;
               long var339 = var330;
               long var341 = var333;
               long var343 = var335;
               String var345 = var337;
               String var346 = var338;
               long var347 = var339;
               long var349 = var341;
               long var351 = var343;
               String var353 = var345;
               String var354 = var346;
               long var355 = var347;
               if (!var332) {
                  long var357 = var349;
                  long var359 = var351;
                  String var361 = var353;
                  String var362 = var354;
                  long var363 = var355;
                  long var365 = var357;
                  long var367 = var359;
                  String var369 = var361;
                  String var370 = var362;
                  long var371 = var363;
                  long var373 = var365;
                  long var375 = var367;
                  String var377 = var369;
                  String var378 = var370;
                  long var379 = var371;
                  long var381 = var373;
                  long var383 = var375;
                  String var385 = var377;
                  String var386 = var378;
                  long var387 = var379;
                  long var389 = var381;
                  long var391 = var383;
                  String var393 = var385;
                  String var394 = var386;
                  long var395 = var387;
                  long var397 = var389;
                  long var399 = var391;
                  String var401 = var393;
                  String var402 = var394;
                  long var403 = var395;
                  long var405 = var397;
                  long var407 = var399;
                  String var409 = var401;
                  String var410 = var402;
                  long var411 = var403;
                  long var413 = var405;
                  long var415 = var407;
                  String var417 = var409;
                  String var418 = var410;
                  long var419 = var411;
                  long var421 = var413;
                  long var423 = var415;
                  String var425 = var417;
                  String var426 = var418;
                  long var427 = var419;
                  boolean var429 = var243.startsWith("#");
                  long var430 = var421;
                  long var432 = var423;
                  String var434 = var425;
                  String var435 = var426;
                  long var436 = var427;
                  long var438 = var430;
                  long var440 = var432;
                  String var442 = var434;
                  String var443 = var435;
                  long var444 = var436;
                  long var446 = var438;
                  long var448 = var440;
                  String var450 = var442;
                  String var451 = var443;
                  long var452 = var444;
                  label190:
                  if (!var429) {
                     long var1854;
                     long var1856;
                     String var1858;
                     String var1859;
                     long var454 = var446;
                     long var456 = var448;
                     String var458 = var450;
                     String var459 = var451;
                     long var460 = var452;
                     long var462 = var454;
                     long var464 = var456;
                     String var466 = var458;
                     String var467 = var459;
                     long var468 = var460;
                     long var470 = var462;
                     long var472 = var464;
                     String var474 = var466;
                     String var475 = var467;
                     long var476 = var468;
                     long var478 = var470;
                     long var480 = var472;
                     String var482 = var474;
                     String var483 = var475;
                     long var484 = var476;
                     long var486 = var478;
                     long var488 = var480;
                     String var490 = var482;
                     String var491 = var483;
                     long var492 = var484;
                     long var494 = var486;
                     long var496 = var488;
                     String var498 = var490;
                     String var499 = var491;
                     long var500 = var492;
                     long var502 = var494;
                     long var504 = var496;
                     String var506 = var498;
                     String var507 = var499;
                     long var508 = var500;
                     long var510 = var502;
                     long var512 = var504;
                     String var514 = var506;
                     String var515 = var507;
                     long var516 = var508;
                     long var518 = var510;
                     long var520 = var512;
                     String var522 = var514;
                     String var523 = var515;
                     long var524 = var516;
                     boolean var526 = var243.startsWith("%%params");
                     long var527 = var518;
                     long var529 = var520;
                     String var531 = var522;
                     String var532 = var523;
                     long var533 = var524;
                     long var535 = var527;
                     long var537 = var529;
                     String var539 = var531;
                     String var540 = var532;
                     long var541 = var533;
                     long var543 = var535;
                     long var545 = var537;
                     String var547 = var539;
                     String var548 = var540;
                     long var549 = var541;
                     label188:
                     if (!var526) {
                        long var551 = var543;
                        long var553 = var545;
                        String var555 = var547;
                        String var556 = var548;
                        long var557 = var549;
                        long var567 = var551;
                        long var569 = var553;
                        String var571 = var555;
                        String var572 = var556;
                        long var573 = var557;
                        long var575 = var567;
                        long var577 = var569;
                        String var579 = var571;
                        String var580 = var572;
                        long var581 = var573;
                        long var583 = var575;
                        long var585 = var577;
                        String var587 = var579;
                        String var588 = var580;
                        long var589 = var581;
                        long var591 = var583;
                        long var593 = var585;
                        String var595 = var587;
                        String var596 = var588;
                        long var597 = var589;
                        long var599 = var591;
                        long var601 = var593;
                        String var603 = var595;
                        String var604 = var596;
                        long var605 = var597;
                        long var607 = var599;
                        long var609 = var601;
                        String var611 = var603;
                        String var612 = var604;
                        long var613 = var605;
                        long var615 = var607;
                        long var617 = var609;
                        String var619 = var611;
                        String var620 = var612;
                        long var621 = var613;
                        long var623 = var615;
                        long var625 = var617;
                        String var627 = var619;
                        String var628 = var620;
                        long var629 = var621;
                        boolean var631 = var243.matches(
                           "^(int|integer|double|float|floating|precise|boolean|bool|bl|string|let|var|variable|typedef)\\s+(const|static|final)?\\s*([a-zA-Z_]\\w*)\\s*(=|#|=>|:)?\\s*([^;]*)\\s*;"
                        );
                        long var632 = var623;
                        long var634 = var625;
                        String var636 = var627;
                        String var637 = var628;
                        long var638 = var629;
                        long var640 = var632;
                        long var642 = var634;
                        String var644 = var636;
                        String var645 = var637;
                        long var646 = var638;
                        long var648 = var640;
                        long var650 = var642;
                        String var652 = var644;
                        String var653 = var645;
                        long var654 = var646;
                        String var1844;
                        long var1845;
                        long var1847;
                        String var1849;
                        String var1850;
                        int var1852;
                        Object var1853;
                        if (!var631) {
                           long var656 = var648;
                           long var658 = var650;
                           String var660 = var652;
                           String var661 = var653;
                           long var662 = var654;
                           long var915 = var656;
                           long var917 = var658;
                           String var919 = var660;
                           String var920 = var661;
                           long var921 = var662;
                           long var923 = var915;
                           long var925 = var917;
                           String var927 = var919;
                           String var928 = var920;
                           long var929 = var921;
                           long var931 = var923;
                           long var933 = var925;
                           String var935 = var927;
                           String var936 = var928;
                           long var937 = var929;
                           long var939 = var931;
                           long var941 = var933;
                           String var943 = var935;
                           String var944 = var936;
                           long var945 = var937;
                           long var947 = var939;
                           long var949 = var941;
                           String var951 = var943;
                           String var952 = var944;
                           long var953 = var945;
                           long var955 = var947;
                           long var957 = var949;
                           String var959 = var951;
                           String var960 = var952;
                           long var961 = var953;
                           boolean var963 = var243.matches(
                              "^(func|def|method|function|fun|fn)\\s+(null|nil|nullptr|int|integer|double|float|boolean|string|void)\\s+(&)?([a-zA-Z_]\\w*)\\s*\\(([^)]*)\\)\\s*\\{"
                           );
                           long var964 = var955;
                           long var966 = var957;
                           String var968 = var959;
                           String var969 = var960;
                           long var970 = var961;
                           long var972 = var964;
                           long var974 = var966;
                           String var976 = var968;
                           String var977 = var969;
                           long var978 = var970;
                           long var980 = var972;
                           long var982 = var974;
                           String var984 = var976;
                           String var985 = var977;
                           long var986 = var978;
                           if (var963) {
                              long var996 = var980;
                              long var998 = var982;
                              String var1000 = var985;
                              long var1001 = var986;
                              long var3164 = (int)var998 + 1 & 4294967295L;
                              var7 = 1L | var996 & -4294967296L;
                              var9 = var998;
                              var11 = var243;
                              var12 = var1000;
                              var15 = var1001;
                              var13 = var3164;
                              continue;
                           }

                           long var988 = var980;
                           long var990 = var982;
                           String var992 = var984;
                           String var993 = var985;
                           long var994 = var986;
                           if ((int)var988 != 0) {
                              String var1244 = var993;
                              long var1245 = var994;
                              String var1800 = var1244;
                              long var1801 = var1245;
                              String var1803 = var1800;
                              long var1804 = var1801;
                              String var1806 = var1803;
                              long var1807 = var1804;
                              String var1809 = var1806;
                              long var1810 = var1807;
                              boolean var1812 = ((String)var89).contains("{");
                              String var1813 = var1809;
                              long var1814 = var1810;
                              String var1819;
                              long var1820;
                              if (var1812) {
                                 String var1816 = var1813;
                                 long var1817 = var1814;
                                 String var2123 = var1816;
                                 long var2124 = var1817;
                                 String var2126 = var2123;
                                 long var2127 = var2124;
                                 int var2129 = computeText((String)var89, (char)('{' & '\uffff'));
                                 String var2130 = var2126;
                                 long var2131 = var2127;
                                 long var3162 = var2129 & 4294967295L | var2131 & -4294967296L;
                                 String var2133 = var2130;
                                 long var2134 = var3162;
                                 String var2136 = var2133;
                                 long var2137 = var2134;
                                 int var2139 = computeText((String)var89, (char)('}' & '\uffff'));
                                 String var2140 = var2136;
                                 long var2141 = var2137;
                                 if ((int)var2141 > var2139) {
                                    var195 = var988;
                                    var197 = var990;
                                    var199 = var992;
                                    var200 = var2140;
                                    var201 = var2141;
                                    break label190;
                                 }

                                 var1819 = var2140;
                                 var1820 = var2141;
                              } else {
                                 var1819 = var1813;
                                 var1820 = var1814;
                              }

                              String var2143 = var1819;
                              long var2144 = var1820;
                              String var2146 = var2143;
                              long var2147 = var2144;
                              String var2149 = var2146;
                              long var2150 = var2147;
                              String var2152 = var2149;
                              long var2153 = var2150;
                              boolean var2155 = ((String)var89).contains("}");
                              String var2156 = var2152;
                              long var2157 = var2153;
                              if (!var2155) {
                                 String var2159 = var2156;
                                 long var2160 = var2157;
                                 String var2830 = var2159;
                                 long var2831 = var2160;
                                 String var2833 = var2830;
                                 long var2834 = var2831;
                                 String var2836 = var2833;
                                 long var2837 = var2834;
                                 StringBuilder var2839 = var2.append((String)var89);
                                 String var2840 = var2836;
                                 long var2841 = var2837;
                                 String var2843 = var2840;
                                 long var2844 = var2841;
                                 String var2846 = var2843;
                                 long var2847 = var2844;
                                 String var2849 = var2846;
                                 long var2850 = var2847;
                                 String var2852 = var2849;
                                 long var2853 = var2850;
                                 var2839.append("\n");
                                 String var2855 = var2852;
                                 long var2856 = var2853;
                                 String var2858 = var2855;
                                 long var2859 = var2856;
                                 String var2861 = var2858;
                                 long var2862 = var2859;
                                 String var2864 = var2861;
                                 long var2865 = var2862;
                                 String var2867 = var2864;
                                 long var2868 = var2865;
                                 var4.add(var89);
                                 String var2870 = var2867;
                                 long var2871 = var2868;
                                 long var3163 = (int)var990 + 1 & 4294967295L;
                                 var7 = var988;
                                 var9 = var990;
                                 var11 = var992;
                                 var12 = var2870;
                                 var15 = var2871;
                                 var13 = var3163;
                                 continue;
                              }

                              String var2162 = var2156;
                              long var2163 = var2157;
                              String var2873 = var2162;
                              long var2874 = var2163;
                              String var2876 = var2873;
                              long var2877 = var2874;
                              int var2879 = computeText((String)var89, (char)('}' & '\uffff'));
                              String var2880 = var2876;
                              long var2881 = var2877;
                              String var2883 = var2880;
                              long var2884 = var2881;
                              String var2886 = var2883;
                              long var2887 = var2884;
                              int var2889 = computeText((String)var89, (char)('{' & '\uffff'));
                              String var2890 = var2886;
                              long var2891 = var2887;
                              if (var2879 <= var2889) {
                                 var195 = var988;
                                 var197 = var990;
                                 var199 = var992;
                                 var200 = var2890;
                                 var201 = var2891;
                                 break label190;
                              }

                              String var2893 = var2890;
                              long var2894 = var2891;
                              String var2976 = var2893;
                              long var2977 = var2894;
                              String var2979 = var2976;
                              long var2980 = var2977;
                              String var2982 = var2979;
                              long var2983 = var2980;
                              String var2985 = var2982;
                              long var2986 = var2983;
                              String var2988 = var2.toString();
                              String var2989 = var2985;
                              long var2990 = var2986;
                              String var2992 = var2989;
                              long var2993 = var2990;
                              String var2995 = var2992;
                              long var2996 = var2993;
                              ScriptMember var2998 = resolveScriptMember(script, var992, var2988, var4);
                              String var2999 = var2995;
                              long var3000 = var2996;
                              String var3002 = var2999;
                              long var3003 = var3000;
                              String var3005 = var3002;
                              long var3006 = var3003;
                              String var3008 = var3005;
                              long var3009 = var3006;
                              String var3011 = var3008;
                              long var3012 = var3009;
                              script.handleScriptMember(var2998);
                              String var3014 = var3011;
                              long var3015 = var3012;
                              String var3017 = var3014;
                              long var3018 = var3015;
                              String var3020 = var3017;
                              long var3021 = var3018;
                              String var3023 = var3020;
                              long var3024 = var3021;
                              String var3026 = var3023;
                              long var3027 = var3024;
                              var2.setLength(0);
                              String var3029 = var3026;
                              long var3030 = var3027;
                              String var3032 = var3029;
                              long var3033 = var3030;
                              String var3035 = var3032;
                              long var3036 = var3033;
                              String var3038 = var3035;
                              long var3039 = var3036;
                              String var3041 = var3038;
                              long var3042 = var3039;
                              var4.clear();
                              var1854 = 0L | var988 & -4294967296L;
                              var1856 = var990;
                              var1858 = var992;
                              var1859 = var3041;
                              var1860 = var3042;
                              break label188;
                           }

                           long var1236 = var988;
                           long var1238 = var990;
                           String var1240 = var992;
                           String var1241 = var993;
                           long var1242 = var994;
                           long var1743 = var1236;
                           long var1745 = var1238;
                           String var1747 = var1240;
                           String var1748 = var1241;
                           long var1749 = var1242;
                           long var1751 = var1743;
                           long var1753 = var1745;
                           String var1755 = var1747;
                           String var1756 = var1748;
                           long var1757 = var1749;
                           long var1759 = var1751;
                           long var1761 = var1753;
                           String var1763 = var1755;
                           String var1764 = var1756;
                           long var1765 = var1757;
                           long var1767 = var1759;
                           long var1769 = var1761;
                           String var1771 = var1763;
                           String var1772 = var1764;
                           long var1773 = var1765;
                           boolean var1775 = var243.matches("^([a-zA-Z_.]\\w*)\\s*\\(([^)]*)\\)");
                           long var1776 = var1767;
                           long var1778 = var1769;
                           String var1780 = var1771;
                           String var1781 = var1772;
                           long var1782 = var1773;
                           if (!var1775) {
                              long var1784 = var1776;
                              long var1786 = var1778;
                              String var1788 = var1780;
                              String var1789 = var1781;
                              long var1790 = var1782;
                              long var2013 = var1784;
                              long var2015 = var1786;
                              String var2017 = var1788;
                              String var2018 = var1789;
                              long var2019 = var1790;
                              long var2021 = var2013;
                              long var2023 = var2015;
                              String var2025 = var2017;
                              String var2026 = var2018;
                              long var2027 = var2019;
                              long var2029 = var2021;
                              long var2031 = var2023;
                              String var2033 = var2025;
                              String var2034 = var2026;
                              long var2035 = var2027;
                              long var2037 = var2029;
                              long var2039 = var2031;
                              String var2041 = var2033;
                              String var2042 = var2034;
                              long var2043 = var2035;
                              boolean var2045 = var243.matches("if\\s*\\(\\s*([^()]*?)\\s*\\)\\s*\\{?|\\bif\\s*\\{?");
                              long var2046 = var2037;
                              long var2048 = var2039;
                              String var2050 = var2041;
                              String var2051 = var2042;
                              long var2052 = var2043;
                              if (var2045) {
                                 long var2062 = var2046;
                                 long var2064 = var2048;
                                 String var2066 = var2050;
                                 long var2067 = var2052;
                                 long var3161 = (int)var2064 + 1 & 4294967295L;
                                 var7 = 1L << 32 | var2062 & 4294967295L;
                                 var9 = var2064;
                                 var11 = var2066;
                                 var12 = var243;
                                 var15 = var2067;
                                 var13 = var3161;
                                 continue;
                              }

                              long var2054 = var2046;
                              long var2056 = var2048;
                              String var2058 = var2050;
                              String var2059 = var2051;
                              long var2060 = var2052;
                              if ((int)(var2054 >>> 32) == 0) {
                                 var195 = var2054;
                                 var197 = var2056;
                                 var199 = var2058;
                                 var200 = var2059;
                                 var201 = var2060;
                                 break label190;
                              }

                              String var2433 = var2059;
                              long var2434 = var2060;
                              String var2932 = var2433;
                              long var2933 = var2434;
                              String var2935 = var2932;
                              long var2936 = var2933;
                              String var2938 = var2935;
                              long var2939 = var2936;
                              String var2941 = var2938;
                              long var2942 = var2939;
                              boolean var2944 = ((String)var89).contains("{");
                              String var2945 = var2941;
                              long var2946 = var2942;
                              String var2951;
                              long var2952;
                              if (var2944) {
                                 String var2948 = var2945;
                                 long var2949 = var2946;
                                 String var3044 = var2948;
                                 long var3045 = var2949;
                                 String var3047 = var3044;
                                 long var3048 = var3045;
                                 int var3050 = computeText((String)var89, (char)('{' & '\uffff'));
                                 String var3051 = var3047;
                                 long var3052 = var3048;
                                 long var3160 = var3050 & 4294967295L | var3052 & -4294967296L;
                                 String var3054 = var3051;
                                 long var3055 = var3160;
                                 String var3057 = var3054;
                                 long var3058 = var3055;
                                 int var3060 = computeText((String)var89, (char)('}' & '\uffff'));
                                 String var3061 = var3057;
                                 long var3062 = var3058;
                                 if ((int)var3062 > var3060) {
                                    var195 = var2054;
                                    var197 = var2056;
                                    var199 = var2058;
                                    var200 = var3061;
                                    var201 = var3062;
                                    break label190;
                                 }

                                 var2951 = var3061;
                                 var2952 = var3062;
                              } else {
                                 var2951 = var2945;
                                 var2952 = var2946;
                              }

                              String var3064 = var2951;
                              long var3065 = var2952;
                              String var3067 = var3064;
                              long var3068 = var3065;
                              String var3070 = var3067;
                              long var3071 = var3068;
                              String var3073 = var3070;
                              long var3074 = var3071;
                              boolean var3076 = ((String)var89).contains("}");
                              String var3077 = var3073;
                              long var3078 = var3074;
                              if (!var3076) {
                                 var195 = var2054;
                                 var197 = var2056;
                                 var199 = var2058;
                                 var200 = var3077;
                                 var201 = var3078;
                                 break label190;
                              }

                              String var3080 = var3077;
                              long var3081 = var3078;
                              String var3083 = var3080;
                              long var3084 = var3081;
                              String var3086 = var3083;
                              long var3087 = var3084;
                              int var3089 = computeText((String)var89, (char)('}' & '\uffff'));
                              String var3090 = var3086;
                              long var3091 = var3087;
                              String var3093 = var3090;
                              long var3094 = var3091;
                              String var3096 = var3093;
                              long var3097 = var3094;
                              int var3099 = computeText((String)var89, (char)('{' & '\uffff'));
                              String var3100 = var3096;
                              long var3101 = var3097;
                              if (var3089 <= var3099) {
                                 var195 = var2054;
                                 var197 = var2056;
                                 var199 = var2058;
                                 var200 = var3100;
                                 var201 = var3101;
                                 break label190;
                              }

                              String var3103 = var3100;
                              long var3104 = var3101;
                              String var3106 = var3103;
                              long var3107 = var3104;
                              String var3109 = var3106;
                              long var3110 = var3107;
                              String var3112 = var3109;
                              long var3113 = var3110;
                              String var3115 = var3112;
                              long var3116 = var3113;
                              String var3118 = var3.toString();
                              String var3119 = var3115;
                              long var3120 = var3116;
                              String var3122 = var3119;
                              long var3123 = var3120;
                              String var3125 = var3122;
                              long var3126 = var3123;
                              handleScript(script, var3103, var3118, var5);
                              String var3128 = var3125;
                              long var3129 = var3126;
                              String var3131 = var3128;
                              long var3132 = var3129;
                              String var3134 = var3131;
                              long var3135 = var3132;
                              String var3137 = var3134;
                              long var3138 = var3135;
                              String var3140 = var3137;
                              long var3141 = var3138;
                              var3.setLength(0);
                              String var3143 = var3140;
                              long var3144 = var3141;
                              String var3146 = var3143;
                              long var3147 = var3144;
                              String var3149 = var3146;
                              long var3150 = var3147;
                              String var3152 = var3149;
                              long var3153 = var3150;
                              String var3155 = var3152;
                              long var3156 = var3153;
                              var5.clear();
                              var1854 = 0L << 32 | var2054 & 4294967295L;
                              var1856 = var2056;
                              var1858 = var2058;
                              var1859 = var3155;
                              var1860 = var3156;
                              break label188;
                           }

                           long var1792 = var1776;
                           long var1794 = var1778;
                           String var1796 = var1780;
                           String var1797 = var1781;
                           long var2069 = var1792;
                           long var2071 = var1794;
                           String var2073 = var1796;
                           String var2074 = var1797;
                           long var2077 = var2069;
                           long var2079 = var2071;
                           String var2081 = var2073;
                           String var2082 = var2074;
                           long var2085 = var2077;
                           long var2087 = var2079;
                           String var2089 = var2081;
                           String var2090 = var2082;
                           long var2093 = var2085;
                           long var2095 = var2087;
                           String var2097 = var2089;
                           String var2098 = var2090;
                           String var2101 = ((String)var89).trim();
                           long var2102 = var2093;
                           long var2104 = var2095;
                           String var2106 = var2097;
                           String var2107 = var2098;
                           long var2117;
                           long var2119;
                           String var2121;
                           String var2122;
                           if (var2110 == null) {
                              long var2111 = var2102;
                              long var2113 = var2104;
                              String var2115 = var2106;
                              String var2116 = var2107;
                              long var2436 = var2111;
                              long var2438 = var2113;
                              String var2440 = var2115;
                              String var2441 = var2116;
                              long var2442 = var2436;
                              long var2444 = var2438;
                              String var2446 = var2440;
                              String var2447 = var2441;
                              long var2448 = var2442;
                              long var2450 = var2444;
                              String var2452 = var2446;
                              String var2453 = var2447;
                              long var2454 = var2448;
                              long var2456 = var2450;
                              String var2458 = var2452;
                              String var2459 = var2453;
                              long var2460 = var2454;
                              long var2462 = var2456;
                              String var2464 = var2458;
                              String var2465 = var2459;
                              long var2466 = var2460;
                              long var2468 = var2462;
                              String var2470 = var2464;
                              String var2471 = var2465;
                              long var2472 = var2466;
                              long var2474 = var2468;
                              String var2476 = var2470;
                              String var2477 = var2471;
                              long var2478 = var2472;
                              long var2480 = var2474;
                              String var2482 = var2476;
                              String var2483 = var2477;
                              Lookup var2484 = MethodHandles.lookup();
                              long var2485 = var2478;
                              long var2487 = var2480;
                              String var2489 = var2482;
                              String var2490 = var2483;
                              long var2491 = var2485;
                              long var2493 = var2487;
                              String var2495 = var2489;
                              String var2496 = var2490;
                              long var2497 = var2491;
                              long var2499 = var2493;
                              String var2501 = var2495;
                              String var2502 = var2496;
                              long var2503 = var2497;
                              long var2505 = var2499;
                              String var2507 = var2501;
                              String var2508 = var2502;
                              long var2509 = var2503;
                              long var2511 = var2505;
                              String var2513 = var2507;
                              String var2514 = var2508;
                              long var2515 = var2509;
                              long var2517 = var2511;
                              String var2519 = var2513;
                              String var2520 = var2514;
                              long var2521 = var2515;
                              long var2523 = var2517;
                              String var2525 = var2519;
                              String var2526 = var2520;
                              ClassLoader var2527 = ScriptCompiler.S.class.getClassLoader();
                              MethodType var2528 = MethodType.fromMethodDescriptorString(
                                 "(Lorg/phobos/core/script/Script;)Ljava/util/function/Consumer;", var2527
                              );
                              long var2529 = var2521;
                              long var2531 = var2523;
                              String var2533 = var2525;
                              String var2534 = var2526;
                              long var2535 = var2529;
                              long var2537 = var2531;
                              String var2539 = var2533;
                              String var2540 = var2534;
                              long var2541 = var2535;
                              long var2543 = var2537;
                              String var2545 = var2539;
                              String var2546 = var2540;
                              long var2547 = var2541;
                              long var2549 = var2543;
                              String var2551 = var2545;
                              String var2552 = var2546;
                              long var2553 = var2547;
                              long var2555 = var2549;
                              String var2557 = var2551;
                              String var2558 = var2552;
                              ClassLoader var2559 = ScriptCompiler.S.class.getClassLoader();
                              MethodType var2560 = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", var2559);
                              long var2561 = var2553;
                              long var2563 = var2555;
                              String var2565 = var2557;
                              String var2566 = var2558;
                              long var2567 = var2561;
                              long var2569 = var2563;
                              String var2571 = var2565;
                              String var2572 = var2566;
                              long var2573 = var2567;
                              long var2575 = var2569;
                              String var2577 = var2571;
                              String var2578 = var2572;
                              long var2579 = var2573;
                              long var2581 = var2575;
                              String var2583 = var2577;
                              String var2584 = var2578;
                              long var2585 = var2579;
                              long var2587 = var2581;
                              String var2589 = var2583;
                              String var2590 = var2584;
                              long var2591 = var2585;
                              long var2593 = var2587;
                              String var2595 = var2589;
                              String var2596 = var2590;
                              ClassLoader var2597 = ScriptCompiler.S.class.getClassLoader();
                              MethodType var2598 = MethodType.fromMethodDescriptorString(
                                 "(Lorg/phobos/core/script/Script;Lorg/phobos/core/script/ScriptHook;)V", var2597
                              );
                              long var2599 = var2591;
                              long var2601 = var2593;
                              String var2603 = var2595;
                              String var2604 = var2596;
                              long var2605 = var2599;
                              long var2607 = var2601;
                              String var2609 = var2603;
                              String var2610 = var2604;
                              long var2611 = var2605;
                              long var2613 = var2607;
                              String var2615 = var2609;
                              String var2616 = var2610;
                              long var2617 = var2611;
                              long var2619 = var2613;
                              String var2621 = var2615;
                              String var2622 = var2616;
                              long var2623 = var2617;
                              long var2625 = var2619;
                              String var2627 = var2621;
                              String var2628 = var2622;
                              MethodHandle var2629 = var2484.findStatic(ScriptCompiler.S.class, "handleScript2", var2598);
                              long var2630 = var2623;
                              long var2632 = var2625;
                              String var2634 = var2627;
                              String var2635 = var2628;
                              long var2636 = var2630;
                              long var2638 = var2632;
                              String var2640 = var2634;
                              String var2641 = var2635;
                              long var2642 = var2636;
                              long var2644 = var2638;
                              String var2646 = var2640;
                              String var2647 = var2641;
                              long var2648 = var2642;
                              long var2650 = var2644;
                              String var2652 = var2646;
                              String var2653 = var2647;
                              long var2654 = var2648;
                              long var2656 = var2650;
                              String var2658 = var2652;
                              String var2659 = var2653;
                              ClassLoader var2660 = ScriptCompiler.S.class.getClassLoader();
                              MethodType var2661 = MethodType.fromMethodDescriptorString("(Lorg/phobos/core/script/ScriptHook;)V", var2660);
                              long var2662 = var2654;
                              long var2664 = var2656;
                              String var2666 = var2658;
                              String var2667 = var2659;
                              long var2668 = var2662;
                              long var2670 = var2664;
                              String var2672 = var2666;
                              String var2673 = var2667;
                              long var2674 = var2668;
                              long var2676 = var2670;
                              String var2678 = var2672;
                              String var2679 = var2673;
                              long var2680 = var2674;
                              long var2682 = var2676;
                              String var2684 = var2678;
                              String var2685 = var2679;
                              Object[] var10000 = new Object[0];
                              long var2686 = var2680;
                              long var2688 = var2682;
                              String var2690 = var2684;
                              String var2691 = var2685;
                              long var2692 = var2686;
                              long var2694 = var2688;
                              String var2696 = var2690;
                              String var2697 = var2691;
                              var2110 = LambdaMetafactory.metafactory(var2484, "accept", var2528, var2560, var2629, var2661);
                              long var2698 = var2692;
                              long var2700 = var2694;
                              String var2702 = var2696;
                              String var2703 = var2697;
                              long var2704 = var2698;
                              long var2706 = var2700;
                              String var2708 = var2702;
                              String var2709 = var2703;
                              long var2710 = var2704;
                              long var2712 = var2706;
                              String var2714 = var2708;
                              String var2715 = var2709;
                              var2117 = var2710;
                              var2119 = var2712;
                              var2121 = var2714;
                              var2122 = var2715;
                           } else {
                              var2117 = var2102;
                              var2119 = var2104;
                              var2121 = var2106;
                              var2122 = var2107;
                           }

                           long var2717 = var2117;
                           long var2719 = var2119;
                           String var2721 = var2121;
                           String var2722 = var2122;
                           long var2724 = var2717;
                           long var2726 = var2719;
                           String var2728 = var2721;
                           String var2729 = var2722;
                           long var2731 = var2724;
                           long var2733 = var2726;
                           String var2735 = var2728;
                           String var2736 = var2729;
                           long var2738 = var2731;
                           long var2740 = var2733;
                           String var2742 = var2735;
                           String var2743 = var2736;
                           MethodHandle var2745 = var2110.getTarget();
                           long var2746 = var2738;
                           long var2748 = var2740;
                           String var2750 = var2742;
                           String var2751 = var2743;
                           long var2753 = var2746;
                           long var2755 = var2748;
                           String var2757 = var2750;
                           String var2758 = var2751;
                           long var2760 = var2753;
                           long var2762 = var2755;
                           String var2764 = var2757;
                           String var2765 = var2758;
                           long var2767 = var2760;
                           long var2769 = var2762;
                           String var2771 = var2764;
                           String var2772 = var2765;
                           long var2774 = var2767;
                           long var2776 = var2769;
                           String var2778 = var2771;
                           String var2779 = var2772;
                           long var2781 = var2774;
                           long var2783 = var2776;
                           String var2785 = var2778;
                           String var2786 = var2779;
                           long var2788 = var2781;
                           long var2790 = var2783;
                           String var2792 = var2785;
                           String var2793 = var2786;
                           long var2795 = var2788;
                           long var2797 = var2790;
                           String var2799 = var2792;
                           String var2800 = var2793;
                           long var2802 = var2795;
                           long var2804 = var2797;
                           String var2806 = var2799;
                           String var2807 = var2800;
                           long var2809 = var2802;
                           long var2811 = var2804;
                           String var2813 = var2806;
                           String var2814 = var2807;
                           long var2823;
                           long var2825;
                           String var2827;
                           String var2828;
                           if (script != null) {
                              long var2816 = var2809;
                              long var2818 = var2811;
                              String var2820 = var2813;
                              String var2821 = var2814;
                              Object[] var3159 = phobos$g1806fcd88;
                              if (phobos$g1806fcd88 == null) {
                                 var3159 = phobos$g1806fcd88 = new Object[1];
                              }

                              var3159[0] = script;
                              var2823 = var2816;
                              var2825 = var2818;
                              var2827 = var2820;
                              var2828 = var2821;
                           } else {
                              var2823 = var2809;
                              var2825 = var2811;
                              var2827 = var2813;
                              var2828 = var2814;
                           }

                           Object[] var10001 = phobos$g1806fcd88;
                           if (phobos$g1806fcd88 == null) {
                              var10001 = phobos$g1806fcd88 = new Object[1];
                           }

                           Object var2954 = var2745.invokeWithArguments(var10001);
                           long var2955 = var2823;
                           long var2957 = var2825;
                           String var2959 = var2827;
                           String var2960 = var2828;
                           long var2962 = var2955;
                           long var2964 = var2957;
                           String var2966 = var2959;
                           String var2967 = var2960;
                           long var2969 = var2962;
                           long var2971 = var2964;
                           String var2973 = var2966;
                           String var2974 = var2967;
                           var1844 = var2101;
                           var1845 = var2969;
                           var1847 = var2971;
                           var1849 = var2973;
                           var1850 = var2974;
                           var1852 = 7327128;
                           var1853 = var2954;
                        } else {
                           long var664 = var648;
                           long var666 = var650;
                           String var668 = var652;
                           String var669 = var653;
                           long var1003 = var664;
                           long var1005 = var666;
                           String var1007 = var668;
                           String var1008 = var669;
                           long var1011 = var1003;
                           long var1013 = var1005;
                           String var1015 = var1007;
                           String var1016 = var1008;
                           long var1019 = var1011;
                           long var1021 = var1013;
                           String var1023 = var1015;
                           String var1024 = var1016;
                           long var1027 = var1019;
                           long var1029 = var1021;
                           String var1031 = var1023;
                           String var1032 = var1024;
                           long var1533;
                           long var1535;
                           String var1537;
                           String var1538;
                           if (var1035 == null) {
                              long var1036 = var1027;
                              long var1038 = var1029;
                              String var1040 = var1031;
                              String var1041 = var1032;
                              long var1247 = var1036;
                              long var1249 = var1038;
                              String var1251 = var1040;
                              String var1252 = var1041;
                              long var1253 = var1247;
                              long var1255 = var1249;
                              String var1257 = var1251;
                              String var1258 = var1252;
                              long var1259 = var1253;
                              long var1261 = var1255;
                              String var1263 = var1257;
                              String var1264 = var1258;
                              long var1265 = var1259;
                              long var1267 = var1261;
                              String var1269 = var1263;
                              String var1270 = var1264;
                              long var1271 = var1265;
                              long var1273 = var1267;
                              String var1275 = var1269;
                              String var1276 = var1270;
                              long var1277 = var1271;
                              long var1279 = var1273;
                              String var1281 = var1275;
                              String var1282 = var1276;
                              long var1283 = var1277;
                              long var1285 = var1279;
                              String var1287 = var1281;
                              String var1288 = var1282;
                              long var1289 = var1283;
                              long var1291 = var1285;
                              String var1293 = var1287;
                              String var1294 = var1288;
                              long var1295 = var1289;
                              long var1297 = var1291;
                              String var1299 = var1293;
                              String var1300 = var1294;
                              Lookup var1301 = MethodHandles.lookup();
                              long var1302 = var1295;
                              long var1304 = var1297;
                              String var1306 = var1299;
                              String var1307 = var1300;
                              long var1308 = var1302;
                              long var1310 = var1304;
                              String var1312 = var1306;
                              String var1313 = var1307;
                              long var1314 = var1308;
                              long var1316 = var1310;
                              String var1318 = var1312;
                              String var1319 = var1313;
                              long var1320 = var1314;
                              long var1322 = var1316;
                              String var1324 = var1318;
                              String var1325 = var1319;
                              long var1326 = var1320;
                              long var1328 = var1322;
                              String var1330 = var1324;
                              String var1331 = var1325;
                              long var1332 = var1326;
                              long var1334 = var1328;
                              String var1336 = var1330;
                              String var1337 = var1331;
                              long var1338 = var1332;
                              long var1340 = var1334;
                              String var1342 = var1336;
                              String var1343 = var1337;
                              ClassLoader var1344 = ScriptCompiler.S.class.getClassLoader();
                              MethodType var1345 = MethodType.fromMethodDescriptorString(
                                 "(Lorg/phobos/core/script/Script;)Ljava/util/function/Consumer;", var1344
                              );
                              long var1346 = var1338;
                              long var1348 = var1340;
                              String var1350 = var1342;
                              String var1351 = var1343;
                              long var1352 = var1346;
                              long var1354 = var1348;
                              String var1356 = var1350;
                              String var1357 = var1351;
                              long var1358 = var1352;
                              long var1360 = var1354;
                              String var1362 = var1356;
                              String var1363 = var1357;
                              long var1364 = var1358;
                              long var1366 = var1360;
                              String var1368 = var1362;
                              String var1369 = var1363;
                              long var1370 = var1364;
                              long var1372 = var1366;
                              String var1374 = var1368;
                              String var1375 = var1369;
                              ClassLoader var1376 = ScriptCompiler.S.class.getClassLoader();
                              MethodType var1377 = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)V", var1376);
                              long var1378 = var1370;
                              long var1380 = var1372;
                              String var1382 = var1374;
                              String var1383 = var1375;
                              long var1384 = var1378;
                              long var1386 = var1380;
                              String var1388 = var1382;
                              String var1389 = var1383;
                              long var1390 = var1384;
                              long var1392 = var1386;
                              String var1394 = var1388;
                              String var1395 = var1389;
                              long var1396 = var1390;
                              long var1398 = var1392;
                              String var1400 = var1394;
                              String var1401 = var1395;
                              long var1402 = var1396;
                              long var1404 = var1398;
                              String var1406 = var1400;
                              String var1407 = var1401;
                              long var1408 = var1402;
                              long var1410 = var1404;
                              String var1412 = var1406;
                              String var1413 = var1407;
                              ClassLoader var1414 = ScriptCompiler.S.class.getClassLoader();
                              MethodType var1415 = MethodType.fromMethodDescriptorString(
                                 "(Lorg/phobos/core/script/Script;Lorg/phobos/core/script/Script$Var;)V", var1414
                              );
                              long var1416 = var1408;
                              long var1418 = var1410;
                              String var1420 = var1412;
                              String var1421 = var1413;
                              long var1422 = var1416;
                              long var1424 = var1418;
                              String var1426 = var1420;
                              String var1427 = var1421;
                              long var1428 = var1422;
                              long var1430 = var1424;
                              String var1432 = var1426;
                              String var1433 = var1427;
                              long var1434 = var1428;
                              long var1436 = var1430;
                              String var1438 = var1432;
                              String var1439 = var1433;
                              long var1440 = var1434;
                              long var1442 = var1436;
                              String var1444 = var1438;
                              String var1445 = var1439;
                              MethodHandle var1446 = var1301.findStatic(ScriptCompiler.S.class, "handleScript3", var1415);
                              long var1447 = var1440;
                              long var1449 = var1442;
                              String var1451 = var1444;
                              String var1452 = var1445;
                              long var1453 = var1447;
                              long var1455 = var1449;
                              String var1457 = var1451;
                              String var1458 = var1452;
                              long var1459 = var1453;
                              long var1461 = var1455;
                              String var1463 = var1457;
                              String var1464 = var1458;
                              long var1465 = var1459;
                              long var1467 = var1461;
                              String var1469 = var1463;
                              String var1470 = var1464;
                              long var1471 = var1465;
                              long var1473 = var1467;
                              String var1475 = var1469;
                              String var1476 = var1470;
                              ClassLoader var1477 = ScriptCompiler.S.class.getClassLoader();
                              MethodType var1478 = MethodType.fromMethodDescriptorString("(Lorg/phobos/core/script/Script$Var;)V", var1477);
                              long var1479 = var1471;
                              long var1481 = var1473;
                              String var1483 = var1475;
                              String var1484 = var1476;
                              long var1485 = var1479;
                              long var1487 = var1481;
                              String var1489 = var1483;
                              String var1490 = var1484;
                              long var1491 = var1485;
                              long var1493 = var1487;
                              String var1495 = var1489;
                              String var1496 = var1490;
                              long var1497 = var1491;
                              long var1499 = var1493;
                              String var1501 = var1495;
                              String var1502 = var1496;
                              Object[] var3165 = new Object[0];
                              long var1503 = var1497;
                              long var1505 = var1499;
                              String var1507 = var1501;
                              String var1508 = var1502;
                              long var1509 = var1503;
                              long var1511 = var1505;
                              String var1513 = var1507;
                              String var1514 = var1508;
                              var1035 = LambdaMetafactory.metafactory(var1301, "accept", var1345, var1377, var1446, var1478);
                              long var1515 = var1509;
                              long var1517 = var1511;
                              String var1519 = var1513;
                              String var1520 = var1514;
                              long var1521 = var1515;
                              long var1523 = var1517;
                              String var1525 = var1519;
                              String var1526 = var1520;
                              long var1527 = var1521;
                              long var1529 = var1523;
                              String var1531 = var1525;
                              String var1532 = var1526;
                              var1533 = var1527;
                              var1535 = var1529;
                              var1537 = var1531;
                              var1538 = var1532;
                              var1539 = "accept";
                           } else {
                              var1533 = var1027;
                              var1535 = var1029;
                              var1537 = var1031;
                              var1538 = var1032;
                           }

                           long var1540 = var1533;
                           long var1542 = var1535;
                           String var1544 = var1537;
                           String var1545 = var1538;
                           long var1547 = var1540;
                           long var1549 = var1542;
                           String var1551 = var1544;
                           String var1552 = var1545;
                           long var1554 = var1547;
                           long var1556 = var1549;
                           String var1558 = var1551;
                           String var1559 = var1552;
                           long var1561 = var1554;
                           long var1563 = var1556;
                           String var1565 = var1558;
                           String var1566 = var1559;
                           MethodHandle var1568 = var1035.getTarget();
                           long var1569 = var1561;
                           long var1571 = var1563;
                           String var1573 = var1565;
                           String var1574 = var1566;
                           long var1576 = var1569;
                           long var1578 = var1571;
                           String var1580 = var1573;
                           String var1581 = var1574;
                           long var1583 = var1576;
                           long var1585 = var1578;
                           String var1587 = var1580;
                           String var1588 = var1581;
                           long var1590 = var1583;
                           long var1592 = var1585;
                           String var1594 = var1587;
                           String var1595 = var1588;
                           long var1597 = var1590;
                           long var1599 = var1592;
                           String var1601 = var1594;
                           String var1602 = var1595;
                           long var1604 = var1597;
                           long var1606 = var1599;
                           String var1608 = var1601;
                           String var1609 = var1602;
                           long var1611 = var1604;
                           long var1613 = var1606;
                           String var1615 = var1608;
                           String var1616 = var1609;
                           long var1618 = var1611;
                           long var1620 = var1613;
                           String var1622 = var1615;
                           String var1623 = var1616;
                           long var1625 = var1618;
                           long var1627 = var1620;
                           String var1629 = var1622;
                           String var1630 = var1623;
                           long var1632 = var1625;
                           long var1634 = var1627;
                           String var1636 = var1629;
                           String var1637 = var1630;
                           long var1646;
                           long var1648;
                           String var1650;
                           String var1651;
                           if (script != null) {
                              long var1639 = var1632;
                              long var1641 = var1634;
                              String var1643 = var1636;
                              String var1644 = var1637;
                              Object[] var3166 = phobos$g1806fcb40;
                              if (phobos$g1806fcb40 == null) {
                                 var3166 = phobos$g1806fcb40 = new Object[1];
                              }

                              var3166[0] = script;
                              var1646 = var1639;
                              var1648 = var1641;
                              var1650 = var1643;
                              var1651 = var1644;
                           } else {
                              var1646 = var1632;
                              var1648 = var1634;
                              var1650 = var1636;
                              var1651 = var1637;
                           }

                           Object[] var3169 = phobos$g1806fcb40;
                           if (phobos$g1806fcb40 == null) {
                              var3169 = phobos$g1806fcb40 = new Object[1];
                           }

                           Object var1822 = var1568.invokeWithArguments(var3169);
                           long var1823 = var1646;
                           long var1825 = var1648;
                           String var1827 = var1650;
                           String var1828 = var1651;
                           long var1830 = var1823;
                           long var1832 = var1825;
                           String var1834 = var1827;
                           String var1835 = var1828;
                           long var1837 = var1830;
                           long var1839 = var1832;
                           String var1841 = var1834;
                           String var1842 = var1835;
                           var1844 = var243;
                           var1845 = var1837;
                           var1847 = var1839;
                           var1849 = var1841;
                           var1850 = var1842;
                           var1852 = 7326544;
                           var1853 = var1822;
                        }

                        switch (var1852) {
                           case 7326544:
                           default:
                              handleText(var1844, (Consumer<Var<?>>)var1853);
                              break;
                           case 7327128:
                              handleText2(var1844, (Consumer<ScriptHook>)var1853);
                        }

                        var1854 = var1845;
                        var1856 = var1847;
                        var1858 = var1849;
                        var1859 = var1850;
                     } else {
                        long var559 = var543;
                        long var561 = var545;
                        String var563 = var547;
                        String var564 = var548;
                        long var565 = var549;
                        long var672 = var559;
                        long var674 = var561;
                        String var676 = var563;
                        String var677 = var564;
                        long var678 = var565;
                        long var680 = var672;
                        long var682 = var674;
                        String var684 = var676;
                        String var685 = var677;
                        long var686 = var678;
                        long var688 = var680;
                        long var690 = var682;
                        String var692 = var684;
                        String var693 = var685;
                        long var694 = var686;
                        long var696 = var688;
                        long var698 = var690;
                        String var700 = var692;
                        String var701 = var693;
                        long var702 = var694;
                        long var704 = var696;
                        long var706 = var698;
                        String var708 = var700;
                        String var709 = var701;
                        long var710 = var702;
                        long var712 = var704;
                        long var714 = var706;
                        String var716 = var708;
                        String var717 = var709;
                        long var718 = var710;
                        long var720 = var712;
                        long var722 = var714;
                        String var724 = var716;
                        String var725 = var717;
                        long var726 = var718;
                        Pattern var728 = Pattern.compile("%%params(?:\\s+author=(\\S+))?(?:\\s+api=(\\S+))?");
                        long var729 = var720;
                        long var731 = var722;
                        String var733 = var724;
                        String var734 = var725;
                        long var735 = var726;
                        long var737 = var729;
                        long var739 = var731;
                        String var741 = var733;
                        String var742 = var734;
                        long var743 = var735;
                        long var745 = var737;
                        long var747 = var739;
                        String var749 = var741;
                        String var750 = var742;
                        long var751 = var743;
                        long var753 = var745;
                        long var755 = var747;
                        String var757 = var749;
                        String var758 = var750;
                        long var759 = var751;
                        long var761 = var753;
                        long var763 = var755;
                        String var765 = var757;
                        String var766 = var758;
                        long var767 = var759;
                        long var769 = var761;
                        long var771 = var763;
                        String var773 = var765;
                        String var774 = var766;
                        long var775 = var767;
                        long var777 = var769;
                        long var779 = var771;
                        String var781 = var773;
                        String var782 = var774;
                        long var783 = var775;
                        long var785 = var777;
                        long var787 = var779;
                        String var789 = var781;
                        String var790 = var782;
                        long var791 = var783;
                        long var793 = var785;
                        long var795 = var787;
                        String var797 = var789;
                        String var798 = var790;
                        long var799 = var791;
                        long var801 = var793;
                        long var803 = var795;
                        String var805 = var797;
                        String var806 = var798;
                        long var807 = var799;
                        long var809 = var801;
                        long var811 = var803;
                        String var813 = var805;
                        String var814 = var806;
                        long var815 = var807;
                        Matcher var817 = var728.matcher(var243);
                        long var818 = var809;
                        long var820 = var811;
                        String var822 = var813;
                        String var823 = var814;
                        long var824 = var815;
                        long var826 = var818;
                        long var828 = var820;
                        String var830 = var822;
                        String var831 = var823;
                        long var832 = var824;
                        long var834 = var826;
                        long var836 = var828;
                        String var838 = var830;
                        String var839 = var831;
                        long var840 = var832;
                        long var842 = var834;
                        long var844 = var836;
                        String var846 = var838;
                        String var847 = var839;
                        long var848 = var840;
                        long var850 = var842;
                        long var852 = var844;
                        String var854 = var846;
                        String var855 = var847;
                        long var856 = var848;
                        long var858 = var850;
                        long var860 = var852;
                        String var862 = var854;
                        String var863 = var855;
                        long var864 = var856;
                        long var866 = var858;
                        long var868 = var860;
                        String var870 = var862;
                        String var871 = var863;
                        long var872 = var864;
                        long var874 = var866;
                        long var876 = var868;
                        String var878 = var870;
                        String var879 = var871;
                        long var880 = var872;
                        boolean var882 = var817.find();
                        long var883 = var874;
                        long var885 = var876;
                        String var887 = var878;
                        String var888 = var879;
                        long var889 = var880;
                        long var891 = var883;
                        long var893 = var885;
                        String var895 = var887;
                        String var896 = var888;
                        long var897 = var889;
                        long var899 = var891;
                        long var901 = var893;
                        String var903 = var895;
                        String var904 = var896;
                        long var905 = var897;
                        if (!var882) {
                           var195 = var899;
                           var197 = var901;
                           var199 = var903;
                           var200 = var904;
                           var201 = var905;
                           break label190;
                        }

                        long var907 = var899;
                        long var909 = var901;
                        String var911 = var903;
                        String var912 = var904;
                        long var1042 = var907;
                        long var1044 = var909;
                        String var1046 = var911;
                        String var1047 = var912;
                        long var1050 = var1042;
                        long var1052 = var1044;
                        String var1054 = var1046;
                        String var1055 = var1047;
                        long var1058 = var1050;
                        long var1060 = var1052;
                        String var1062 = var1054;
                        String var1063 = var1055;
                        long var1066 = var1058;
                        long var1068 = var1060;
                        String var1070 = var1062;
                        String var1071 = var1063;
                        long var1074 = var1066;
                        long var1076 = var1068;
                        String var1078 = var1070;
                        String var1079 = var1071;
                        long var1082 = var1074;
                        long var1084 = var1076;
                        String var1086 = var1078;
                        String var1087 = var1079;
                        long var1090 = var1082;
                        long var1092 = var1084;
                        String var1094 = var1086;
                        String var1095 = var1087;
                        String var1098 = var817.group(1);
                        long var1099 = var1090;
                        long var1101 = var1092;
                        String var1103 = var1094;
                        String var1104 = var1095;
                        long var1107 = var1099;
                        long var1109 = var1101;
                        String var1111 = var1103;
                        String var1112 = var1104;
                        long var1115 = var1107;
                        long var1117 = var1109;
                        String var1119 = var1111;
                        String var1120 = var1112;
                        long var1123 = var1115;
                        long var1125 = var1117;
                        String var1127 = var1119;
                        String var1128 = var1120;
                        long var1131 = var1123;
                        long var1133 = var1125;
                        String var1135 = var1127;
                        String var1136 = var1128;
                        long var1139 = var1131;
                        long var1141 = var1133;
                        String var1143 = var1135;
                        String var1144 = var1136;
                        long var1147 = var1139;
                        long var1149 = var1141;
                        String var1151 = var1143;
                        String var1152 = var1144;
                        long var1155 = var1147;
                        long var1157 = var1149;
                        String var1159 = var1151;
                        String var1160 = var1152;
                        long var1163 = var1155;
                        long var1165 = var1157;
                        String var1167 = var1159;
                        String var1168 = var1160;
                        long var1171 = var1163;
                        long var1173 = var1165;
                        String var1175 = var1167;
                        String var1176 = var1168;
                        long var1179 = var1171;
                        long var1181 = var1173;
                        String var1183 = var1175;
                        String var1184 = var1176;
                        String var1187 = var817.group(2);
                        long var1188 = var1179;
                        long var1190 = var1181;
                        String var1192 = var1183;
                        String var1193 = var1184;
                        long var1196 = var1188;
                        long var1198 = var1190;
                        String var1200 = var1192;
                        String var1201 = var1193;
                        long var1204 = var1196;
                        long var1206 = var1198;
                        String var1208 = var1200;
                        String var1209 = var1201;
                        long var1212 = var1204;
                        long var1214 = var1206;
                        String var1216 = var1208;
                        String var1217 = var1209;
                        long var1220 = var1212;
                        long var1222 = var1214;
                        String var1224 = var1216;
                        String var1225 = var1217;
                        if (var1187 == null) {
                           IllegalArgumentException var1653 = new IllegalArgumentException("Missing api version");
                           throw var1653;
                        }

                        long var1228 = var1220;
                        long var1230 = var1222;
                        String var1232 = var1224;
                        String var1233 = var1225;
                        long var1654 = var1228;
                        long var1656 = var1230;
                        String var1658 = var1232;
                        String var1659 = var1233;
                        long var1662 = var1654;
                        long var1664 = var1656;
                        String var1666 = var1658;
                        String var1667 = var1659;
                        long var1670 = var1662;
                        long var1672 = var1664;
                        String var1674 = var1666;
                        String var1675 = var1667;
                        API10API11Enum var1678 = API10API11Enum.get(var1187);
                        long var1679 = var1670;
                        long var1681 = var1672;
                        String var1683 = var1674;
                        String var1684 = var1675;
                        long var1687 = var1679;
                        long var1689 = var1681;
                        String var1691 = var1683;
                        String var1692 = var1684;
                        API10API11Enum var1695 = PhobosClient.aPI10API11Enum;
                        long var1696 = var1687;
                        long var1698 = var1689;
                        String var1700 = var1691;
                        String var1701 = var1692;
                        long var1704 = var1696;
                        long var1706 = var1698;
                        String var1708 = var1700;
                        String var1709 = var1701;
                        long var1712 = var1704;
                        long var1714 = var1706;
                        String var1716 = var1708;
                        String var1717 = var1709;
                        boolean var1720 = var1695.equals(var1678);
                        long var1721 = var1712;
                        long var1723 = var1714;
                        String var1725 = var1716;
                        String var1726 = var1717;
                        long var1737;
                        long var1739;
                        String var1741;
                        String var1742;
                        if (!var1720) {
                           long var1729 = var1721;
                           long var1731 = var1723;
                           String var1733 = var1725;
                           String var1734 = var1726;
                           long var1862 = var1729;
                           long var1864 = var1731;
                           String var1866 = var1733;
                           String var1867 = var1734;
                           long var1870 = var1862;
                           long var1872 = var1864;
                           String var1874 = var1866;
                           String var1875 = var1867;
                           long var1878 = var1870;
                           long var1880 = var1872;
                           String var1882 = var1874;
                           String var1883 = var1875;
                           PhobosClient var1886 = script.getPhobosClient();
                           long var1887 = var1878;
                           long var1889 = var1880;
                           String var1891 = var1882;
                           String var1892 = var1883;
                           long var1895 = var1887;
                           long var1897 = var1889;
                           String var1899 = var1891;
                           String var1900 = var1892;
                           long var1903 = var1895;
                           long var1905 = var1897;
                           String var1907 = var1899;
                           String var1908 = var1900;
                           long var1911 = var1903;
                           long var1913 = var1905;
                           String var1915 = var1907;
                           String var1916 = var1908;
                           long var1919 = var1911;
                           long var1921 = var1913;
                           String var1923 = var1915;
                           String var1924 = var1916;
                           String var1927 = script.getName();
                           long var1928 = var1919;
                           long var1930 = var1921;
                           String var1932 = var1923;
                           String var1933 = var1924;
                           long var1943;
                           long var1945;
                           String var1947;
                           String var1948;
                           if (var1936 == null) {
                              long var1937 = var1928;
                              long var1939 = var1930;
                              String var1941 = var1932;
                              String var1942 = var1933;
                              long var2165 = var1937;
                              long var2167 = var1939;
                              String var2169 = var1941;
                              String var2170 = var1942;
                              long var2171 = var2165;
                              long var2173 = var2167;
                              String var2175 = var2169;
                              String var2176 = var2170;
                              long var2177 = var2171;
                              long var2179 = var2173;
                              String var2181 = var2175;
                              String var2182 = var2176;
                              long var2183 = var2177;
                              long var2185 = var2179;
                              String var2187 = var2181;
                              String var2188 = var2182;
                              long var2189 = var2183;
                              long var2191 = var2185;
                              String var2193 = var2187;
                              String var2194 = var2188;
                              long var2195 = var2189;
                              long var2197 = var2191;
                              String var2199 = var2193;
                              String var2200 = var2194;
                              long var2201 = var2195;
                              long var2203 = var2197;
                              String var2205 = var2199;
                              String var2206 = var2200;
                              long var2207 = var2201;
                              long var2209 = var2203;
                              String var2211 = var2205;
                              String var2212 = var2206;
                              Lookup var2213 = MethodHandles.lookup();
                              long var2214 = var2207;
                              long var2216 = var2209;
                              String var2218 = var2211;
                              String var2219 = var2212;
                              long var2220 = var2214;
                              long var2222 = var2216;
                              String var2224 = var2218;
                              String var2225 = var2219;
                              long var2226 = var2220;
                              long var2228 = var2222;
                              String var2230 = var2224;
                              String var2231 = var2225;
                              long var2232 = var2226;
                              long var2234 = var2228;
                              String var2236 = var2230;
                              String var2237 = var2231;
                              long var2238 = var2232;
                              long var2240 = var2234;
                              String var2242 = var2236;
                              String var2243 = var2237;
                              long var2244 = var2238;
                              long var2246 = var2240;
                              String var2248 = var2242;
                              String var2249 = var2243;
                              long var2250 = var2244;
                              long var2252 = var2246;
                              String var2254 = var2248;
                              String var2255 = var2249;
                              ClassLoader var2256 = ScriptCompiler.S.class.getClassLoader();
                              MethodType var2257 = MethodType.fromMethodDescriptorString("(Ljava/lang/String;)Ljava/lang/String;", var2256);
                              long var2258 = var2250;
                              long var2260 = var2252;
                              String var2262 = var2254;
                              String var2263 = var2255;
                              long var2264 = var2258;
                              long var2266 = var2260;
                              String var2268 = var2262;
                              String var2269 = var2263;
                              long var2270 = var2264;
                              long var2272 = var2266;
                              String var2274 = var2268;
                              String var2275 = var2269;
                              long var2276 = var2270;
                              long var2278 = var2272;
                              String var2280 = var2274;
                              String var2281 = var2275;
                              long var2282 = var2276;
                              long var2284 = var2278;
                              String var2286 = var2280;
                              String var2287 = var2281;
                              Object[] var2288 = new Object[0];
                              long var2289 = var2282;
                              long var2291 = var2284;
                              String var2293 = var2286;
                              String var2294 = var2287;
                              long var2295 = var2289;
                              long var2297 = var2291;
                              String var2299 = var2293;
                              String var2300 = var2294;
                              var1936 = StringConcatFactory.makeConcatWithConstants(var2213, "makeConcatWithConstants", var2257, "Script \u0001 uses outdated or deprecated API.", var2288);
                              long var2301 = var2295;
                              long var2303 = var2297;
                              String var2305 = var2299;
                              String var2306 = var2300;
                              long var2307 = var2301;
                              long var2309 = var2303;
                              String var2311 = var2305;
                              String var2312 = var2306;
                              long var2313 = var2307;
                              long var2315 = var2309;
                              String var2317 = var2311;
                              String var2318 = var2312;
                              var1943 = var2313;
                              var1945 = var2315;
                              var1947 = var2317;
                              var1948 = var2318;
                           } else {
                              var1943 = var1928;
                              var1945 = var1930;
                              var1947 = var1932;
                              var1948 = var1933;
                           }

                           long var2320 = var1943;
                           long var2322 = var1945;
                           String var2324 = var1947;
                           String var2325 = var1948;
                           long var2327 = var2320;
                           long var2329 = var2322;
                           String var2331 = var2324;
                           String var2332 = var2325;
                           long var2334 = var2327;
                           long var2336 = var2329;
                           String var2338 = var2331;
                           String var2339 = var2332;
                           long var2341 = var2334;
                           long var2343 = var2336;
                           String var2345 = var2338;
                           String var2346 = var2339;
                           MethodHandle var2348 = var1936.getTarget();
                           long var2349 = var2341;
                           long var2351 = var2343;
                           String var2353 = var2345;
                           String var2354 = var2346;
                           long var2356 = var2349;
                           long var2358 = var2351;
                           String var2360 = var2353;
                           String var2361 = var2354;
                           long var2363 = var2356;
                           long var2365 = var2358;
                           String var2367 = var2360;
                           String var2368 = var2361;
                           long var2370 = var2363;
                           long var2372 = var2365;
                           String var2374 = var2367;
                           String var2375 = var2368;
                           long var2377 = var2370;
                           long var2379 = var2372;
                           String var2381 = var2374;
                           String var2382 = var2375;
                           long var2384 = var2377;
                           long var2386 = var2379;
                           String var2388 = var2381;
                           String var2389 = var2382;
                           long var2391 = var2384;
                           long var2393 = var2386;
                           String var2395 = var2388;
                           String var2396 = var2389;
                           long var2398 = var2391;
                           long var2400 = var2393;
                           String var2402 = var2395;
                           String var2403 = var2396;
                           long var2405 = var2398;
                           long var2407 = var2400;
                           String var2409 = var2402;
                           String var2410 = var2403;
                           long var2412 = var2405;
                           long var2414 = var2407;
                           String var2416 = var2409;
                           String var2417 = var2410;
                           long var2426;
                           long var2428;
                           String var2430;
                           String var2431;
                           if (var1927 != null) {
                              long var2419 = var2412;
                              long var2421 = var2414;
                              String var2423 = var2416;
                              String var2424 = var2417;
                              Object[] var3167 = phobos$g1806fca18;
                              if (phobos$g1806fca18 == null) {
                                 var3167 = phobos$g1806fca18 = new Object[1];
                              }

                              var3167[0] = var1927;
                              var2426 = var2419;
                              var2428 = var2421;
                              var2430 = var2423;
                              var2431 = var2424;
                           } else {
                              var2426 = var2412;
                              var2428 = var2414;
                              var2430 = var2416;
                              var2431 = var2417;
                           }

                           Object[] var3170 = phobos$g1806fca18;
                           if (phobos$g1806fca18 == null) {
                              var3170 = phobos$g1806fca18 = new Object[1];
                           }

                           Object var2896 = var2348.invokeWithArguments(var3170);
                           long var2897 = var2426;
                           long var2899 = var2428;
                           String var2901 = var2430;
                           String var2902 = var2431;
                           long var2904 = var2897;
                           long var2906 = var2899;
                           String var2908 = var2901;
                           String var2909 = var2902;
                           long var2911 = var2904;
                           long var2913 = var2906;
                           String var2915 = var2908;
                           String var2916 = var2909;
                           long var2918 = var2911;
                           long var2920 = var2913;
                           String var2922 = var2915;
                           String var2923 = var2916;
                           long var2925 = var2918;
                           long var2927 = var2920;
                           String var2929 = var2922;
                           String var2930 = var2923;
                           var1886.handleText5("API", (String)var2896);
                           var1737 = var2925;
                           var1739 = var2927;
                           var1741 = var2929;
                           var1742 = var2930;
                        } else {
                           var1737 = var1721;
                           var1739 = var1723;
                           var1741 = var1725;
                           var1742 = var1726;
                        }

                        long var1950 = var1737;
                        long var1952 = var1739;
                        String var1954 = var1741;
                        String var1955 = var1742;
                        long var1957 = var1950;
                        long var1959 = var1952;
                        String var1961 = var1954;
                        String var1962 = var1955;
                        long var1964 = var1957;
                        long var1966 = var1959;
                        String var1968 = var1961;
                        String var1969 = var1962;
                        long var1971 = var1964;
                        long var1973 = var1966;
                        String var1975 = var1968;
                        String var1976 = var1969;
                        script.setText(var1098);
                        long var1978 = var1971;
                        long var1980 = var1973;
                        String var1982 = var1975;
                        String var1983 = var1976;
                        long var1985 = var1978;
                        long var1987 = var1980;
                        String var1989 = var1982;
                        String var1990 = var1983;
                        long var1992 = var1985;
                        long var1994 = var1987;
                        String var1996 = var1989;
                        String var1997 = var1990;
                        long var1999 = var1992;
                        long var2001 = var1994;
                        String var2003 = var1996;
                        String var2004 = var1997;
                        long var2006 = var1999;
                        long var2008 = var2001;
                        String var2010 = var2003;
                        String var2011 = var2004;
                        script.setAPI10API11Enum(var1678);
                        var1854 = var2006;
                        var1856 = var2008;
                        var1858 = var2010;
                        var1859 = var2011;
                     }

                     var195 = var1854;
                     var197 = var1856;
                     var199 = var1858;
                     var200 = var1859;
                     var201 = var1860;
                  } else {
                     var195 = var446;
                     var197 = var448;
                     var199 = var450;
                     var200 = var451;
                     var201 = var452;
                  }
               } else {
                  var195 = var349;
                  var197 = var351;
                  var199 = var353;
                  var200 = var354;
                  var201 = var355;
               }
            } else {
               var195 = var179;
               var197 = var181;
               var199 = var183;
               var200 = var184;
               var201 = var185;
            }

            long var3168 = (int)var197 + 1 & 4294967295L;
            var7 = var195;
            var9 = var197;
            var11 = var199;
            var12 = var200;
            var15 = var201;
            var13 = var3168;
         }
      }

      public static boolean testText2(String text) {
         long var5 = 0L;
         String[] var1 = text.split("\n");
         int var2 = var1.length;
         long var3 = 0L;

         boolean var70;
         while (true) {
            long var7 = (int)var3 & 4294967295L | var5 & -4294967296L;
            long var9 = var7;
            long var11 = var9;
            long var13 = var11;
            if ((int)((int)var3 & 4294967295L) < var2) {
               long var15 = var13;
               long var17 = var15;
               long var19 = var17;
               long var21 = var19;
               long var23 = var21;
               Object var25 = var1[(int)((int)var3 & 4294967295L)];
               long var26 = var23;
               long var28 = var26;
               long var30 = var28;
               long var32 = var30;
               long var34 = var32;
               long var36 = var34;
               long var38 = var36;
               String var40 = ((String)var25).trim();
               long var41 = var38;
               long var43 = var41;
               long var45 = var43;
               long var47 = var45;
               long var49 = var47;
               long var51 = var49;
               long var53 = var51;
               long var55 = var53;
               long var57 = var55;
               long var59 = var57;
               boolean var61 = var40.startsWith("return");
               long var62 = var59;
               long var64 = var62;
               long var66 = var64;
               if (!var61) {
                  long var68 = var66;
                  long var10000 = (int)var68 + 1 & 4294967295L;
                  var5 = var68;
                  var3 = var10000;
                  continue;
               }

               var70 = true;
               break;
            }

            var70 = false;
            break;
         }

         return var70;
      }
   }
}
