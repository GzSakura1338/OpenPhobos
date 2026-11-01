package org.phobos.core.loader;

import lombok.SneakyThrows;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.Map.Entry;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.phobos.core.ClientGuard;

public class ModsFolderScanner {
   @SneakyThrows public long computeFile(File file) {
      HashMap var2 = new HashMap();

      label273: {
         Throwable var23;
         label278: {
            FileInputStream var4;
            try {
               var4 = new FileInputStream(file);
            } catch (Throwable var69) {
               var23 = var69;
               break label278;
            }

            JarInputStream var3;
            try {
               var3 = new JarInputStream(var4);
            } catch (Throwable var68) {
               var23 = var68;
               break label278;
            }

            label270:
            while (true) {
               Throwable var31;
               while (true) {
                  JarEntry var5;
                  try {
                     var5 = var3.getNextJarEntry();
                  } catch (Throwable var82) {
                     var31 = var82;
                     break;
                  }

                  if (var5 == null) {
                     try {
                        var3.close();
                        break label273;
                     } catch (Throwable var66) {
                        var23 = var66;
                        break label270;
                     }
                  }

                  boolean var6;
                  try {
                     var6 = var5.isDirectory();
                  } catch (Throwable var81) {
                     var31 = var81;
                     break;
                  }

                  if (!var6) {
                     String var7;
                     try {
                        var7 = var5.getName();
                     } catch (Throwable var80) {
                        var31 = var80;
                        break;
                     }

                     String var8;
                     boolean var9 = true;
                     if (!var9) {
                     }

                     char[] var10 = new char[2];
                     var10[0] = '\\';
                     var10[1] = '.';

                     try {
                        var8 = new String(var10);
                     } catch (Throwable var79) {
                        var31 = var79;
                        break;
                     }

                     String[] var11;
                     try {
                        var11 = var7.split(var8);
                     } catch (Throwable var78) {
                        var31 = var78;
                        break;
                     }

                     int var12 = var11.length;
                     if (var12 == 2) {
                        Object var13 = var11[1];
                        String var14;
                        boolean var15 = true;
                        if (!var15) {
                        }

                        char[] var16 = new char[5];
                        var16[0] = 'c';
                        var16[1] = 'l';
                        var16[2] = 'a';
                        var16[3] = 's';
                        var16[4] = 's';

                        try {
                           var14 = new String(var16);
                        } catch (Throwable var77) {
                           var31 = var77;
                           break;
                        }

                        boolean var17;
                        try {
                           var17 = ((String)var13).equals(var14);
                        } catch (Throwable var76) {
                           var31 = var76;
                           break;
                        }

                        if (var17) {
                           ClassReader var18;
                           boolean var19 = true;
                           if (!var19) {
                           }

                           try {
                              var18 = new ClassReader(var3);
                           } catch (Throwable var75) {
                              var31 = var75;
                              break;
                           }

                           ClassNode var20;
                           boolean var21 = true;
                           if (!var21) {
                           }

                           try {
                              var20 = new ClassNode();
                           } catch (Throwable var74) {
                              var31 = var74;
                              break;
                           }

                           try {
                              var18.accept(var20, 0);
                           } catch (Throwable var73) {
                              var31 = var73;
                              break;
                           }

                           String var22 = var20.name;

                           try {
                              var2.put(var22, var20);
                           } catch (Throwable var72) {
                              var31 = var72;
                              break;
                           }

                           try {
                              var3.closeEntry();
                           } catch (Throwable var71) {
                              var31 = var71;
                              break;
                           }
                        }
                     }
                  }
               }

               boolean var32 = true;
               if (var32) {
                  try {
                     var3.close();
                  } catch (Throwable var70) {
                     Throwable var43 = var70;
                     boolean var44 = true;
                     if (!var44) {
                        throw var70;
                     }

                     try {
                        var31.addSuppressed(var43);
                     } catch (Throwable var67) {
                        var23 = var67;
                        break;
                     }
                  }

                  try {
                     throw var31;
                  } catch (Throwable var65) {
                     var23 = var65;
                     break;
                  }
               }

               Throwable var33 = var31;
               throw var33;
            }
         }

         boolean var24 = true;
         if (!var24) {
            throw var23;
         }
      }

      boolean var25 = var2.isEmpty();
      long var34;
      if (var25) {
         var34 = 0L;
      } else {
         Set var26 = var2.entrySet();
         Iterator var27 = var26.iterator();

         while (true) {
            boolean var28 = var27.hasNext();
            if (!var28) {
               var34 = -1L;
               break;
            }

            Object var29 = var27.next();
            boolean var30 = var29 instanceof Entry;
            if (var29 != null && !var30) {
               throw new ClassCastException("CHECKCAST failed");
            }

            Object var36 = ((Entry)var29).getValue();
            boolean var37 = var36 instanceof ClassNode;
            if (var36 != null && !var37) {
               throw new ClassCastException("CHECKCAST failed");
            }

            List var38 = ((ClassNode)var36).methods;
            Iterator var39 = var38.iterator();

            while (true) {
               boolean var40 = var39.hasNext();
               if (!var40) {
                  break;
               }

               Object var41 = var39.next();
               boolean var42 = var41 instanceof MethodNode;
               if (var41 != null && !var42) {
                  throw new ClassCastException("CHECKCAST failed");
               }

               InsnList var45 = ((MethodNode)var41).instructions;
               ListIterator var46 = var45.iterator();

               while (true) {
                  boolean var47 = var46.hasNext();
                  if (!var47) {
                     break;
                  }

                  Object var48 = var46.next();
                  boolean var49 = var48 instanceof AbstractInsnNode;
                  if (var48 != null && !var49) {
                     throw new ClassCastException("CHECKCAST failed");
                  }

                  boolean var50 = var48 instanceof MethodInsnNode;
                  if ((int)((!var50 ? 0 : 1) & 0xFF & 0xFF) != 0) {
                     boolean var51 = var48 instanceof MethodInsnNode;
                     if (var48 != null && !var51) {
                        throw new ClassCastException("CHECKCAST failed");
                     }

                     String var52 = ((MethodInsnNode)var48).owner;
                     String var53;
                     boolean var54 = true;
                     if (!var54) {
                     }

                     char[] var55 = new char[15];
                     var55[0] = 'j';
                     var55[1] = 'a';
                     var55[2] = 'v';
                     var55[3] = 'a';
                     var55[4] = '/';
                     var55[5] = 'l';
                     var55[6] = 'a';
                     var55[7] = 'n';
                     var55[8] = 'g';
                     var55[9] = '/';
                     var55[10] = 'C';
                     var55[11] = 'l';
                     var55[12] = 'a';
                     var55[13] = 's';
                     var55[14] = 's';
                     var53 = new String(var55);
                     boolean var56 = var52.equalsIgnoreCase(var53);
                     if (var56) {
                        return -1041226035465717762L;
                     }

                     String var57;
                     boolean var58 = true;
                     if (!var58) {
                     }

                     char[] var59 = new char[10];
                     var59[0] = 'r';
                     var59[1] = 'e';
                     var59[2] = 'f';
                     var59[3] = 'l';
                     var59[4] = 'e';
                     var59[5] = 'c';
                     var59[6] = 't';
                     var59[7] = 'i';
                     var59[8] = 'o';
                     var59[9] = 'n';
                     var57 = new String(var59);
                     boolean var60 = var52.contains(var57);
                     if (var60) {
                        return 704306237596401010L;
                     }

                     String var61;
                     boolean var62 = true;
                     if (!var62) {
                     }

                     char[] var63 = new char[17];
                     var63[0] = 'o';
                     var63[1] = 'r';
                     var63[2] = 'g';
                     var63[3] = '/';
                     var63[4] = 'p';
                     var63[5] = 'h';
                     var63[6] = 'o';
                     var63[7] = 'b';
                     var63[8] = 'o';
                     var63[9] = 's';
                     var63[10] = '/';
                     var63[11] = 's';
                     var63[12] = 'e';
                     var63[13] = 'c';
                     var63[14] = 'u';
                     var63[15] = 'r';
                     var63[16] = 'e';
                     var61 = new String(var63);
                     boolean var64 = var52.startsWith(var61);
                     if (var64) {
                        return -2481661690424742027L;
                     }
                  }
               }
            }
         }
      }

      return var34;
   }

   public void handlePath(Path path) {
      long var458 = 0L;
      long var11 = 0L;
      long var9 = 0L;
      long var7 = 0L;
      File var2 = path.toFile();
      File[] var3 = var2.listFiles();
      if (var3 != null) {
         int var4 = var3.length;
         long var5 = 0L;

         while (true) {
            long var10000 = ((int)var5 & 4294967295L) << 32 | var7 & 4294967295L;
            long var13 = var11;
            long var15 = var9;
            long var17 = var10000;
            long var19 = var13;
            long var21 = var15;
            long var23 = var17;
            long var25 = var19;
            long var27 = var21;
            long var29 = var23;
            long var31 = var25;
            long var33 = var27;
            long var35 = var29;
            if ((int)((int)var5 & 4294967295L) >= var4) {
               break;
            }

            long var37 = var31;
            long var39 = var33;
            long var41 = var35;
            long var43 = var37;
            long var45 = var39;
            long var47 = var41;
            long var49 = var43;
            long var51 = var45;
            long var53 = var47;
            long var55 = var49;
            long var57 = var51;
            long var59 = var53;
            long var61 = var55;
            long var63 = var57;
            long var65 = var59;
            Object var67 = var3[(int)((int)var5 & 4294967295L)];
            long var68 = var61;
            long var70 = var63;
            long var72 = var65;
            long var74 = var68;
            long var76 = var70;
            long var78 = var72;
            long var80 = var74;
            long var82 = var76;
            long var84 = var78;
            long var86 = var80;
            long var88 = var82;
            long var90 = var84;
            long var92 = var86;
            long var94 = var88;
            long var96 = var90;
            long var98 = var92;
            long var100 = var94;
            long var102 = var96;
            long var104 = var98;
            long var106 = var100;
            long var108 = var102;
            long var110 = var104;
            long var112 = var106;
            long var114 = var108;
            String var116 = ((File)var67).getName();
            long var117 = var110;
            long var119 = var112;
            long var121 = var114;
            long var123 = var117;
            long var125 = var119;
            long var127 = var121;
            long var129 = var123;
            long var131 = var125;
            long var133 = var127;
            long var135 = var129;
            long var137 = var131;
            long var139 = var133;
            long var141 = var135;
            long var143 = var137;
            long var145 = var139;
            long var147 = var141;
            long var149 = var143;
            long var151 = var145;
            long var153 = var147;
            long var155 = var149;
            long var157 = var151;
            long var159 = var153;
            long var161 = var155;
            long var163 = var157;
            long var165 = var159;
            long var167 = var161;
            long var169 = var163;
            long var171 = var165;
            long var173 = var167;
            long var175 = var169;
            long var177 = var171;
            long var179 = var173;
            long var181 = var175;
            long var183 = var177;
            long var185 = var179;
            long var187 = var181;
            long var189 = var183;
            long var191 = var185;
            long var193 = var187;
            long var195 = var189;
            long var197 = var191;
            long var199 = var193;
            long var201 = var195;
            long var203 = var197;
            long var205 = var199;
            var10000 = 261L | var205 & -4294967296L;
            long var207 = 260L | var201 & -4294967296L;
            long var209 = var203;
            long var211 = var10000;
            long var213 = var207;
            long var215 = var209;
            long var217 = var211;
            long var10001 = (int)var213 >>> 24 & 4294967295L | var215 & -4294967296L;
            long var219 = var213;
            long var221 = var10001;
            long var223 = var217;
            long var225 = var219;
            long var227 = var221;
            long var229 = var223;
            long var231 = var225;
            long var233 = var227;
            long var235 = var229;
            long var237 = var231;
            long var239 = var233;
            long var241 = var235;
            long var243 = var237;
            long var245 = var239;
            long var247 = var241;
            long var249 = var243;
            long var251 = var245;
            long var253 = var247;
            long var255 = var249;
            long var257 = var251;
            long var259 = var253;
            long var261 = var255;
            long var263 = var257;
            long var265 = var259;
            var10001 = 1L | var263 & -4294967296L;
            long var267 = 23L | var261 & -4294967296L;
            long var269 = var10001;
            long var271 = var265;
            long var273 = var267;
            long var275 = var269;
            long var277 = var271;
            var10000 = (int)var273 >>> 24 & 4294967295L | var277 & -4294967296L;
            long var279 = var273;
            long var281 = var275;
            long var283 = var10000;
            long var285 = var279;
            long var287 = var281;
            long var289 = var283;
            long var291 = var285;
            long var293 = var287;
            long var295 = var289;
            long var297 = var291;
            long var299 = var293;
            long var301 = var295;
            var10000 = (int)var299 & 4294967295L;
            long var303 = var297;
            long var305 = var299;
            long var307 = var301;
            long var309 = var10000;
            long var311 = var303;
            long var313 = var305;
            long var315 = var307;
            long var317 = var309;
            long var319 = var311;
            long var321 = var313;
            long var323 = var315;
            long var325 = var317;
            long var327 = var319;
            long var329 = var321;
            long var331 = var323;
            long var333 = var325;
            var10000 = 23 << (int)((int)var333 & 0xFF & 0xFF) & 4294967295L;
            long var335 = var327;
            long var337 = var329;
            long var339 = var331;
            long var341 = var10000;
            long var343 = var341;
            long var345 = var335;
            long var347 = var337;
            long var349 = var339;
            long var351 = (int)var343 & 4294967295L | var345 & -4294967296L;
            long var353 = var347;
            long var355 = var349;
            long var357 = var351;
            long var359 = var353;
            long var361 = var355;
            long var363 = var357;
            long var365 = var359;
            long var367 = var361;
            long var369 = var363;
            long var371 = var365;
            long var373 = var367;
            int var375 = var116.lastIndexOf((int)var369);
            long var376 = var369;
            long var378 = var371;
            long var380 = var373;
            long var382 = var376;
            long var384 = var378;
            long var386 = var380;
            long var388 = var382;
            long var390 = var384;
            long var392 = var386;
            var10001 = var375 & 4294967295L | var390 & -4294967296L;
            long var394 = var388;
            long var396 = var10001;
            long var398 = var392;
            long var400 = var394;
            long var402 = var396;
            long var404 = var398;
            long var406 = var400;
            long var408 = var402;
            long var410 = var404;
            long var412 = var406;
            long var414 = var408;
            long var416 = var410;
            long var418 = var412;
            long var420 = var414;
            long var422 = var416;
            long var424 = var418;
            long var426 = var420;
            long var428 = var422;
            long var430 = var424;
            long var432 = var426;
            long var434 = var428;
            long var436 = var430;
            long var438 = var432;
            long var440 = var434;
            long var442 = var436;
            long var444 = var438;
            long var446 = var440;
            long var448 = var442;
            long var450 = var444;
            long var452 = var446;
            long var460;
            long var462;
            if (var375 != -1) {
               long var454 = var450;
               long var456 = var452;
               long var464 = var454;
               long var466 = var456;
               long var468 = var464;
               long var470 = var466;
               long var472 = var468;
               long var474 = var470;
               long var476 = var472;
               long var478 = var474;
               long var480 = var476;
               long var482 = var478;
               long var484 = var480;
               long var486 = var482;
               var10000 = 0L | var486 & -4294967296L;
               long var488 = var484;
               long var490 = var10000;
               long var492 = var488;
               long var494 = var490;
               long var496 = var492;
               long var498 = var494;
               long var500 = var496;
               long var502 = var498;
               String var504 = var116.substring((int)var502, (int)((int)var476 & 4294967295L));
               long var505 = var500;
               long var507 = var502;
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
               String var565 = var116.substring((int)((int)((int)var521 & 4294967295L) + 1 & 4294967295L));
               long var566 = var561;
               long var568 = var563;
               long var570 = var566;
               long var572 = var568;
               long var574 = var570;
               long var576 = var572;
               long var578 = var574;
               long var580 = var576;
               long var582 = var578;
               long var584 = var580;
               long var586 = var582;
               long var588 = var584;
               long var590 = var586;
               long var592 = var588;
               long var594 = var590;
               long var596 = var592;
               long var598 = var594;
               long var600 = var596;
               long var602 = var598;
               long var604 = var600;
               long var606 = var602;
               long var608 = var604;
               long var610 = var606;
               long var612 = var608;
               boolean var614 = "jar".equalsIgnoreCase(var565);
               long var615 = var610;
               long var617 = var612;
               long var619 = var615;
               long var621 = var617;
               long var623 = var619;
               long var625 = var621;
               if (var614) {
                  long var627 = var623;
                  long var629 = var625;
                  long var631 = var627;
                  long var633 = var629;
                  long var635 = var631;
                  long var637 = var633;
                  long var639 = var635;
                  long var641 = var637;
                  long var643 = var639;
                  long var645 = var641;
                  String[] var647 = new String[4];
                  long var648 = var643;
                  long var650 = var645;
                  long var652 = var648;
                  long var654 = var650;
                  long var656 = var652;
                  long var658 = var654;
                  long var660 = var656;
                  long var662 = var658;
                  long var664 = var660;
                  long var666 = var662;
                  long var668 = var664;
                  long var670 = var666;
                  long var672 = var668;
                  long var674 = var670;
                  long var676 = var672;
                  long var678 = var674;
                  long var680 = var676;
                  long var682 = var678;
                  var647[0] = "phobos";
                  long var684 = var680;
                  long var686 = var682;
                  long var688 = var684;
                  long var690 = var686;
                  long var692 = var688;
                  long var694 = var690;
                  long var696 = var692;
                  long var698 = var694;
                  long var700 = var696;
                  long var702 = var698;
                  long var704 = var700;
                  long var706 = var702;
                  long var708 = var704;
                  long var710 = var706;
                  var647[1] = "sodium";
                  long var712 = var708;
                  long var714 = var710;
                  long var716 = var712;
                  long var718 = var714;
                  long var720 = var716;
                  long var722 = var718;
                  long var724 = var720;
                  long var726 = var722;
                  long var728 = var724;
                  long var730 = var726;
                  long var732 = var728;
                  long var734 = var730;
                  long var736 = var732;
                  long var738 = var734;
                  var647[2] = "viafabricplus";
                  long var740 = var736;
                  long var742 = var738;
                  var647[3] = "viafabric";
                  long var744 = outlined_180351d40(null, var67, var647);
                  long var746 = var740;
                  long var748 = var742;
                  if ((int)((int)var744 & 0xFF & 0xFF) == 0) {
                     long var750 = var746;
                     long var752 = var748;
                     long var754 = var750;
                     long var756 = var752;
                     long var758 = var754;
                     long var760 = var756;
                     long var762 = var758;
                     long var764 = var760;
                     long var766 = var762;
                     long var768 = var764;
                     long var770 = this.computeFile((File)var67);
                     long var772 = var766;
                     long var774 = var768;
                     if ((var770 != 0L ? ((int)var770 <= (int)0L ? -1 : 1) : 0) > 0) {
                        long var776 = var772;
                        long var778 = var774;
                        long var780 = var776;
                        long var782 = var778;
                        Object[] var784 = new Object[2];
                        var784[0] = var504;
                        long var785 = var780;
                        long var787 = var782;
                        long var789 = var785;
                        long var791 = var787;
                        long var793 = var789;
                        long var795 = var791;
                        Long var797 = var770;
                        long var798 = var793;
                        long var800 = var795;
                        var784[1] = var797;
                        long var802 = var798;
                        long var804 = var800;
                        long var806 = var802;
                        long var808 = var804;
                        long var810 = var806;
                        long var812 = var808;
                        long var814 = var810;
                        long var816 = var812;
                        String var818 = "Unsafe mods (%s), code: %s".formatted(var784);
                        long var819 = var814;
                        long var821 = var816;
                        long var823 = var819;
                        long var825 = var821;
                        long var827 = var823;
                        long var829 = var825;
                        long var831 = var827;
                        long var833 = var829;
                        ClientGuard.handleText(var818);
                        long var835 = var831;
                        long var837 = var833;
                        long var839 = var835;
                        long var841 = var837;
                        long var843 = var839;
                        long var845 = var841;
                        long var847 = var843;
                        long var849 = var845;
                        ClientGuard.stop();
                        var460 = var847;
                        var462 = var849;
                     } else {
                        var460 = var772;
                        var462 = var774;
                     }
                  } else {
                     var460 = var746;
                     var462 = var748;
                  }
               } else {
                  var460 = var623;
                  var462 = var625;
               }
            } else {
               var458 = var448;
               var460 = var450;
               var462 = var452;
            }

            var10000 = (int)(var462 >>> 32) + 1 & 4294967295L;
            var11 = var458;
            var9 = var460;
            var7 = var462;
            var5 = var10000;
         }
      }
   }

   @SneakyThrows public static long outlined_180351d40(Object var0, Object var1, Object var2) {
      long var28 = 0L;

      label300: {
         Throwable var44;
         label265: {
            Throwable var11;
            label301: {
               ZipFile var3;
               try {
                  var3 = new ZipFile((File)var1);
               } catch (Throwable var99) {
                  var11 = var99;
                  break label301;
               }

               Throwable var21;
               label276: {
                  InputStream var5;
                  Throwable var17;
                  label302: {
                     byte var91;
                     label277: {
                        Throwable var15;
                        label303: {
                           ZipEntry var4;
                           try {
                              var4 = var3.getEntry("fabric.mod.json");
                           } catch (Throwable var104) {
                              var15 = var104;
                              break label303;
                           }

                           if (var4 == null) {
                              try {
                                 var3.close();
                              } catch (Throwable var98) {
                                 var11 = var98;
                                 break label301;
                              }

                              long var13 = 0L;
                              return (int)var13 & 0xFF & 0xFF;
                           }

                           try {
                              var5 = var3.getInputStream(var4);
                           } catch (Throwable var103) {
                              var15 = var103;
                              break label303;
                           }

                           label246: {
                              label273: {
                                 byte[] var7;
                                 try {
                                    var7 = var5.readAllBytes();
                                 } catch (Throwable var111) {
                                    var17 = var111;
                                    boolean var18 = true;
                                    if (var18) {
                                       break label302;
                                    }
                                    break label273;
                                 }

                                 String var6;
                                 try {
                                    var6 = new String(var7);
                                 } catch (Throwable var110) {
                                    var17 = var110;
                                    boolean var112 = true;
                                    if (var112) {
                                       break label302;
                                    }
                                    break label273;
                                 }

                                 JsonElement var8;
                                 try {
                                    var8 = JsonParser.parseString(var6);
                                 } catch (Throwable var109) {
                                    var17 = var109;
                                    boolean var113 = true;
                                    if (var113) {
                                       break label302;
                                    }
                                    break label273;
                                 }

                                 boolean var9 = var8 instanceof JsonObject;
                                 if ((int)((!var9 ? 0 : 1) & 0xFF & 0xFF) == 0) {
                                    break label246;
                                 }

                                 boolean var10 = var8 instanceof JsonObject;
                                 if (var8 != null && !var10) {
                                    throw new ClassCastException("CHECKCAST failed");
                                 }

                                 boolean var22;
                                 try {
                                    var22 = ((JsonObject)var8).has("id");
                                 } catch (Throwable var108) {
                                    var17 = var108;
                                    boolean var114 = true;
                                    if (var114) {
                                       break label302;
                                    }
                                    break label273;
                                 }

                                 if (!var22) {
                                    break label246;
                                 }

                                 JsonElement var23;
                                 try {
                                    var23 = ((JsonObject)var8).get("id");
                                 } catch (Throwable var107) {
                                    var17 = var107;
                                    boolean var115 = true;
                                    if (var115) {
                                       break label302;
                                    }
                                    break label273;
                                 }

                                 String var24;
                                 try {
                                    var24 = var23.getAsString();
                                 } catch (Throwable var106) {
                                    var17 = var106;
                                    boolean var116 = true;
                                    if (var116) {
                                       break label302;
                                    }
                                    break label273;
                                 }

                                 int var25 = Array.getLength(var2);
                                 long var26 = 0L;

                                 while (true) {
                                    long var30 = (int)var26 & 4294967295L | var28 & -4294967296L;
                                    long var32 = var30;
                                    long var34 = var32;
                                    long var36 = var34;
                                    if ((int)((int)var26 & 4294967295L) >= var25) {
                                       if (var5 != null) {
                                          var5.close();
                                       }

                                       var91 = 0;
                                       break label277;
                                    }

                                    long var38 = var36;
                                    long var53 = var38;
                                    long var55 = var53;
                                    long var57 = var55;
                                    long var59 = var57;
                                    Object var61 = ((Object[])var2)[(int)((int)var26 & 4294967295L)];
                                    long var62 = var59;
                                    long var64 = var62;
                                    long var66 = var64;
                                    long var68 = var66;
                                    long var70 = var68;
                                    long var72 = var70;
                                    long var74 = var72;
                                    long var76 = var74;
                                    long var78 = var76;
                                    long var80 = var78;

                                    boolean var82;
                                    try {
                                       var82 = ((String)var61).equals(var24);
                                    } catch (Throwable var105) {
                                       var17 = var105;
                                       boolean var117 = true;
                                       if (var117) {
                                          break label302;
                                       }
                                       break;
                                    }

                                    long var83 = var80;
                                    long var85 = var83;
                                    long var87 = var85;
                                    if (var82) {
                                       if (var5 != null) {
                                          try {
                                             var5.close();
                                          } catch (Throwable var102) {
                                             var15 = var102;
                                             break label303;
                                          }
                                       }

                                       var91 = 1;
                                       break label277;
                                    }

                                    long var89 = var87;
                                    long var10000 = (int)var89 + 1 & 4294967295L;
                                    var28 = var89;
                                    var26 = var10000;
                                 }
                              }

                              boolean var19 = true;
                              if (!var19) {
                                 boolean var20 = true;
                                 if (!var20) {
                                    var21 = var17;
                                    break label276;
                                 }
                              }
                              break label302;
                           }

                           if (var5 != null) {
                              try {
                                 var5.close();
                              } catch (Throwable var101) {
                                 var15 = var101;
                                 break label303;
                              }
                           }

                           try {
                              var3.close();
                           } catch (Throwable var97) {
                              var11 = var97;
                              break label301;
                           }

                           long var13 = 0L;
                           return (int)var13 & 0xFF & 0xFF;
                        }

                        boolean var16 = true;
                        if (!var16) {
                           boolean var45 = true;
                           if (!var45) {
                              boolean var46 = true;
                              if (!var46) {
                                 boolean var47 = true;
                                 if (!var47) {
                                    boolean var48 = true;
                                    if (!var48) {
                                       Throwable var52 = var15;
                                       throw var52;
                                    }
                                 }
                              }
                           }
                        }

                        try {
                           var3.close();
                           throw var15;
                        } catch (Throwable var100) {
                           Throwable var49 = var100;
                           boolean var50 = true;
                           if (var50) {
                              try {
                                 var15.addSuppressed(var49);
                                 throw var15;
                              } catch (Throwable var95) {
                                 var11 = var95;
                                 break label301;
                              }
                           }

                           var44 = var100;
                           break label265;
                        }
                     }

                     try {
                        var3.close();
                     } catch (Throwable var96) {
                        var11 = var96;
                        break label301;
                     }

                     long var13 = var91 & 4294967295L;
                     return (int)var13 & 0xFF & 0xFF;
                  }

                  if (var5 != null) {
                     try {
                        var5.close();
                     } catch (Throwable var94) {
                        boolean var93 = true;
                        if (!var93) {
                           var21 = var94;
                           break label276;
                        }

                        var17.addSuppressed(var94);
                     }
                  }

                  throw var17;
               }

               Throwable var52 = var21;
               throw var52;
            }

            boolean var12 = true;
            if (var12) {
               break label300;
            }

            boolean var40 = true;
            if (var40) {
               break label300;
            }

            boolean var41 = true;
            if (var41) {
               break label300;
            }

            boolean var42 = true;
            if (var42) {
               break label300;
            }

            boolean var43 = true;
            if (var43) {
               break label300;
            }

            var44 = var11;
         }

         Throwable var52 = var44;
         throw var52;
      }

      long var13 = 0L;
      return (int)var13 & 0xFF & 0xFF;
   }
}
