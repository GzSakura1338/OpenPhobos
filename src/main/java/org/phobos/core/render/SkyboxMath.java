package org.phobos.core.render;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.lang.reflect.Array;
import java.util.function.BiFunction;

public final class SkyboxMath {
   private static Object[] phobos$g1806faad0;
   private static Object[] phobos$g1806fabd0;
   private static Object[] phobos$g1806facd0;
   private static Object[] phobos$g1806fadd0;
   private static Object[] phobos$g1806faed0;
   private static Object[] phobos$g1806fafd0;

   public static void handleDouble(double[] doubles, double d, double d2) {
      doubles[0] = -d;
      doubles[1] = 1.0;
      doubles[2] = -d2;
   }

   public static void handleDouble2(double[] doubles, double d, double d2) {
      doubles[0] = d2;
      doubles[1] = -d;
      doubles[2] = -1.0;
   }

   public SkyboxMath() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static void handleDouble3(double[] doubles, double d, double d2) {
      doubles[0] = -1.0;
      doubles[1] = -d;
      doubles[2] = -d2;
   }

   public static double computeDouble(double d, double d2) {
      return (d % d2 + d2) % d2;
   }

   public static void handleDouble4(double[] doubles, double d, double d2) {
      doubles[0] = -d2;
      doubles[1] = -d;
      doubles[2] = 1.0;
   }

   public static int computeInt(int i, int i2, int i3) {
      return Math.min(i3, Math.max(i, i2));
   }

   public static void handleDouble5(double[] doubles, double d, double d2) {
      doubles[0] = d;
      doubles[1] = -1.0;
      doubles[2] = -d2;
   }

   public static BufferedImage resolveBufferedImage(BufferedImage bufferedImage) {
      long var97 = 0L;
      int var1 = bufferedImage.getWidth();
      int var2 = bufferedImage.getHeight();
      BufferedImage var3 = new BufferedImage(var1 / 4 * 3, var1 / 4 * 2, 1);
      WritableRaster var4 = var3.getRaster();
      SkyboxMath.FaceData[] var5 = new SkyboxMath.FaceData[6];
      SkyboxMath.Orientation var19 = SkyboxMath::handleDouble2;
      SkyboxMath.FaceData var6 = new SkyboxMath.FaceData("bottom", 0, 0, var19);
      var5[0] = var6;
      SkyboxMath.Orientation var33 = SkyboxMath::handleDouble4;
      SkyboxMath.FaceData var20 = new SkyboxMath.FaceData("top", 1, 0, var33);
      var5[1] = var20;
      SkyboxMath.Orientation var47 = SkyboxMath::handleDouble6;
      SkyboxMath.FaceData var34 = new SkyboxMath.FaceData("front", 2, 0, var47);
      var5[2] = var34;
      SkyboxMath.Orientation var61 = SkyboxMath::handleDouble;
      SkyboxMath.FaceData var48 = new SkyboxMath.FaceData("right", 0, 1, var61);
      var5[3] = var48;
      SkyboxMath.Orientation var75 = SkyboxMath::handleDouble3;
      SkyboxMath.FaceData var62 = new SkyboxMath.FaceData("back", 1, 1, var75);
      var5[4] = var62;
      SkyboxMath.Orientation var89 = SkyboxMath::handleDouble5;
      SkyboxMath.FaceData var76 = new SkyboxMath.FaceData("left", 2, 1, var89);
      var5[5] = var76;
      BiFunction var90 = resolveBiFunction(bufferedImage);
      int var91 = Array.getLength(var5);
      if (0 < var91) {
         long var92 = 0L << 32;
         long var94 = 0L;

         while (true) {
            Object var96 = var5[(int)var94];
            long var103;
            long var105;
            if (0 >= var1 / 4) {
               long var1033 = 0L | var92 & -4294967296L;
               var103 = var97;
               var105 = var1033;
            } else {
               long var10000 = 0L | var92 & -4294967296L;
               long var99 = var97;
               long var101 = var10000;
               long var107 = var99;
               long var109 = var101;

               label36:
               while (true) {
                  long var111 = var107;
                  long var113 = var109;
                  long var115 = 0L;

                  while (true) {
                     long var117 = (int)var115 & 4294967295L | var111 & -4294967296L;
                     long var119 = var113;
                     long var121 = var117;
                     long var123 = var119;
                     long var125 = var121;
                     long var127 = var123;
                     long var129 = var125;
                     long var131 = var127;
                     if ((int)((int)var115 & 4294967295L) >= var1 / 4) {
                        long var137 = var129;
                        long var139 = var131;
                        if ((int)var139 + 1 >= var1 / 4) {
                           var10000 = (int)var139 + 1 & 4294967295L | var139 & -4294967296L;
                           long var1023 = var137;
                           long var1025 = var10000;
                           var103 = var1023;
                           var105 = var1025;
                           break label36;
                        }

                        var10000 = (int)var139 + 1 & 4294967295L | var139 & -4294967296L;
                        var107 = var137;
                        var109 = var10000;
                        break;
                     }

                     long var133 = var129;
                     long var135 = var131;
                     double[] var141 = new double[3];
                     double var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var142 = var133;
                     long var146 = var135;
                     double var148 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var150 = var142;
                     long var152 = var146;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var154 = var150;
                     long var158 = var152;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var160 = var154;
                     long var164 = var158;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var166 = var160;
                     long var170 = var164;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var172 = var166;
                     long var176 = var170;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var178 = var172;
                     long var182 = var176;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var184 = var178;
                     long var188 = var182;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var190 = var184;
                     long var194 = var188;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var196 = var190;
                     long var200 = var194;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var202 = var196;
                     long var206 = var200;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var208 = var202;
                     long var212 = var206;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var214 = var208;
                     long var218 = var212;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var220 = var214;
                     long var224 = var218;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var226 = var220;
                     long var230 = var224;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var232 = var226;
                     long var236 = var230;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var238 = var232;
                     long var242 = var236;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var244 = var238;
                     long var248 = var242;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var250 = var244;
                     long var254 = var248;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var256 = var250;
                     long var260 = var254;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var262 = var256;
                     long var266 = var260;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var268 = var262;
                     long var272 = var266;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var274 = var268;
                     long var278 = var272;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var280 = var274;
                     long var284 = var278;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var286 = var280;
                     long var290 = var284;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var292 = var286;
                     long var296 = var290;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var298 = var292;
                     long var302 = var296;
                     SkyboxMath.Orientation var304 = ((SkyboxMath.FaceData)var96).orientation;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var305 = var298;
                     long var309 = var302;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var311 = var305;
                     long var315 = var309;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var317 = var311;
                     long var321 = var315;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var323 = var317;
                     long var327 = var321;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var329 = var323;
                     long var333 = var327;
                     double var335 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var337 = var329;
                     long var339 = var333;
                     double var341 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var343 = var337;
                     long var345 = var339;
                     double var347 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var349 = var343;
                     long var351 = var345;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var353 = var349;
                     long var357 = var351;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var359 = var353;
                     long var363 = var357;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var365 = var359;
                     long var369 = var363;
                     var304.handleDouble(
                        var141, 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0, 2.0 * ((int)((int)var164 & 4294967295L) + 0.5) / (var1 / 4) - 1.0
                     );
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var371 = var365;
                     long var375 = var369;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var377 = var371;
                     long var381 = var375;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var383 = var377;
                     long var387 = var381;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var389 = var383;
                     long var393 = var387;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var395 = var389;
                     long var399 = var393;
                     double var401 = var141[0];
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var403 = var395;
                     long var407 = var399;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var409 = var403;
                     long var413 = var407;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var415 = var409;
                     long var419 = var413;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var421 = var415;
                     long var425 = var419;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var427 = var421;
                     long var431 = var425;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var433 = var427;
                     long var437 = var431;
                     double var439 = var141[0];
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var441 = var433;
                     long var445 = var437;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var447 = var441;
                     long var451 = var445;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var453 = var447;
                     long var457 = var451;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var459 = var453;
                     long var463 = var457;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var465 = var459;
                     long var469 = var463;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var471 = var465;
                     long var475 = var469;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var477 = var471;
                     long var481 = var475;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var483 = var477;
                     long var487 = var481;
                     double var489 = var141[1];
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var491 = var483;
                     long var495 = var487;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var497 = var491;
                     long var501 = var495;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var503 = var497;
                     long var507 = var501;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var509 = var503;
                     long var513 = var507;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var515 = var509;
                     long var519 = var513;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var521 = var515;
                     long var525 = var519;
                     double var527 = var141[1];
                     double var529 = var141[2];
                     double var531 = var141[2];
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var533 = var521;
                     long var537 = var525;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var539 = var533;
                     long var543 = var537;
                     double var545 = Math.sqrt(var401 * var439 + var489 * var527 + var529 * var531);
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var547 = var539;
                     long var551 = var543;
                     double var553 = var141[1];
                     double var555 = var141[0];
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var557 = var547;
                     long var561 = var551;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var563 = var557;
                     long var567 = var561;
                     double var569 = Math.atan2(var553, var555);
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var571 = var563;
                     long var575 = var567;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var577 = var571;
                     long var581 = var575;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var583 = var577;
                     long var587 = var581;
                     double var589 = computeDouble(var569, Math.PI * 2);
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var591 = var583;
                     long var595 = var587;
                     double var597 = var141[2];
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var599 = var591;
                     long var603 = var595;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var605 = var599;
                     long var609 = var603;
                     double var611 = Math.acos(var597 / var545);
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var613 = var605;
                     long var617 = var609;
                     int[] var619 = new int[3];
                     int[] var620 = new int[2];
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var621 = var613;
                     long var625 = var617;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var627 = var621;
                     long var631 = var625;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var633 = var627;
                     long var637 = var631;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var639 = var633;
                     long var643 = var637;
                     double var645 = Math.floor(var1 * var589 / (Math.PI * 2) - 0.5);
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var647 = var639;
                     long var651 = var643;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var653 = var647;
                     long var657 = var651;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var659 = var653;
                     long var663 = var657;
                     var620[0] = (int)var645;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var665 = var659;
                     long var669 = var663;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var671 = var665;
                     long var675 = var669;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var677 = var671;
                     long var681 = var675;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var683 = var677;
                     long var687 = var681;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var689 = var683;
                     long var693 = var687;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var695 = var689;
                     long var699 = var693;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var701 = var695;
                     long var705 = var699;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var707 = var701;
                     long var711 = var705;
                     double var713 = Math.floor(var2 * var611 / Math.PI - 0.5);
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var715 = var707;
                     long var719 = var711;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var721 = var715;
                     long var725 = var719;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var727 = var721;
                     long var731 = var725;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var733 = var727;
                     long var737 = var731;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var739 = var733;
                     long var743 = var737;
                     var620[1] = (int)var713;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var745 = var739;
                     long var749 = var743;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var751 = var745;
                     long var755 = var749;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var757 = var751;
                     long var761 = var755;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var763 = var757;
                     long var767 = var761;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var769 = var763;
                     long var773 = var767;
                     var90.apply(var620, var619);
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var775 = var769;
                     long var779 = var773;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var781 = var775;
                     long var785 = var779;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var787 = var781;
                     long var791 = var785;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var793 = var787;
                     long var797 = var791;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var799 = var793;
                     long var803 = var797;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var805 = var799;
                     long var809 = var803;
                     int var811 = ((SkyboxMath.FaceData)var96).intValue;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var812 = var805;
                     long var816 = var809;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var818 = var812;
                     long var822 = var816;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var824 = var818;
                     long var828 = var822;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var830 = var824;
                     long var834 = var828;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var836 = var830;
                     long var840 = var834;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var842 = var836;
                     long var846 = var840;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var10002 = (int)var842 & 4294967295L;
                     long var848 = var842;
                     long var854 = var846;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var856 = (int)var842 & 4294967295L;
                     long var858 = var848;
                     long var862 = var854;
                     var10000 = var811 * (var1 / 4) + (int)((int)var842 & 4294967295L) & 4294967295L;
                     double var1162 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var864 = var858;
                     long var868 = var862;
                     var10001 = 2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0;
                     long var872 = var811 * (var1 / 4) + (int)((int)var842 & 4294967295L) & 4294967295L;
                     long var874 = var864;
                     long var878 = var868;
                     long var1145 = (int)(var811 * (var1 / 4) + (int)((int)var842 & 4294967295L) & 4294967295L) & 4294967295L
                        | Double.doubleToRawLongBits(2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0) & -4294967296L;
                     long var880 = var874;
                     long var884 = var878;
                     long var1146 = (int)(var811 * (var1 / 4) + (int)((int)var842 & 4294967295L) & 4294967295L) & 4294967295L
                        | Double.doubleToRawLongBits(2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0) & -4294967296L;
                     long var886 = var880;
                     long var890 = var884;
                     long var1147 = (int)(var811 * (var1 / 4) + (int)((int)var842 & 4294967295L) & 4294967295L) & 4294967295L
                        | Double.doubleToRawLongBits(2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0) & -4294967296L;
                     long var892 = var886;
                     long var896 = var890;
                     int var898 = ((SkyboxMath.FaceData)var96).intValue2;
                     long var1148 = (int)(var811 * (var1 / 4) + (int)((int)var842 & 4294967295L) & 4294967295L) & 4294967295L
                        | Double.doubleToRawLongBits(2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0) & -4294967296L;
                     long var899 = var892;
                     long var903 = var896;
                     long var1149 = (int)(var811 * (var1 / 4) + (int)((int)var842 & 4294967295L) & 4294967295L) & 4294967295L
                        | Double.doubleToRawLongBits(2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0) & -4294967296L;
                     long var905 = var899;
                     long var909 = var903;
                     long var1150 = (int)(var811 * (var1 / 4) + (int)((int)var842 & 4294967295L) & 4294967295L) & 4294967295L
                        | Double.doubleToRawLongBits(2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0) & -4294967296L;
                     long var911 = var905;
                     long var915 = var909;
                     long var1151 = (int)(var811 * (var1 / 4) + (int)((int)var842 & 4294967295L) & 4294967295L) & 4294967295L
                        | Double.doubleToRawLongBits(2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0) & -4294967296L;
                     long var917 = var911;
                     long var921 = var915;
                     long var1152 = (int)(var811 * (var1 / 4) + (int)((int)var842 & 4294967295L) & 4294967295L) & 4294967295L
                        | Double.doubleToRawLongBits(2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0) & -4294967296L;
                     long var923 = var917;
                     long var927 = var921;
                     long var1153 = (int)(var811 * (var1 / 4) + (int)((int)var842 & 4294967295L) & 4294967295L) & 4294967295L
                        | Double.doubleToRawLongBits(2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0) & -4294967296L;
                     long var929 = var923;
                     long var933 = var927;
                     var10000 = (int)var933 & 4294967295L;
                     var10002 = (int)(var811 * (var1 / 4) + (int)((int)var842 & 4294967295L) & 4294967295L) & 4294967295L
                        | Double.doubleToRawLongBits(2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0) & -4294967296L;
                     long var935 = var929;
                     long var939 = var933;
                     long var1154 = (int)(var811 * (var1 / 4) + (int)((int)var842 & 4294967295L) & 4294967295L) & 4294967295L
                        | Double.doubleToRawLongBits(2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0) & -4294967296L;
                     long var943 = (int)var933 & 4294967295L;
                     long var945 = var935;
                     long var949 = var939;
                     var10000 = var898 * (var1 / 4) + (int)((int)var933 & 4294967295L) & 4294967295L;
                     var10002 = (int)(var811 * (var1 / 4) + (int)((int)var842 & 4294967295L) & 4294967295L) & 4294967295L
                        | Double.doubleToRawLongBits(2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0) & -4294967296L;
                     long var951 = var945;
                     long var955 = var949;
                     long var1155 = (int)(var811 * (var1 / 4) + (int)((int)var842 & 4294967295L) & 4294967295L) & 4294967295L
                        | Double.doubleToRawLongBits(2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0) & -4294967296L;
                     long var959 = var898 * (var1 / 4) + (int)((int)var933 & 4294967295L) & 4294967295L;
                     long var961 = var951;
                     long var965 = var955;
                     long var1156 = (int)(var811 * (var1 / 4) + (int)((int)var842 & 4294967295L) & 4294967295L) & 4294967295L
                        | Double.doubleToRawLongBits(2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0) & -4294967296L;
                     int var1165 = (int)(var898 * (var1 / 4) + (int)((int)var933 & 4294967295L) & 4294967295L);
                     long var967 = var961;
                     long var972 = var965;
                     long var1157 = (int)(var811 * (var1 / 4) + (int)((int)var842 & 4294967295L) & 4294967295L) & 4294967295L
                        | Double.doubleToRawLongBits(2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0) & -4294967296L;
                     int var1166 = (int)(var898 * (var1 / 4) + (int)((int)var933 & 4294967295L) & 4294967295L);
                     long var974 = var967;
                     long var979 = var972;
                     long var1158 = (int)(var811 * (var1 / 4) + (int)((int)var842 & 4294967295L) & 4294967295L) & 4294967295L
                        | Double.doubleToRawLongBits(2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0) & -4294967296L;
                     int var1167 = (int)(var898 * (var1 / 4) + (int)((int)var933 & 4294967295L) & 4294967295L);
                     long var10003 = (int)(var811 * (var1 / 4) + (int)((int)var842 & 4294967295L) & 4294967295L) & 4294967295L;
                     long var981 = var974;
                     long var983 = var10003;
                     long var988 = var979;
                     long var1159 = (int)(var811 * (var1 / 4) + (int)((int)var842 & 4294967295L) & 4294967295L) & 4294967295L
                        | Double.doubleToRawLongBits(2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0) & -4294967296L;
                     int var1168 = (int)(var898 * (var1 / 4) + (int)((int)var933 & 4294967295L) & 4294967295L);
                     long var992 = var981;
                     long var997 = var988;
                     long var1160 = (int)var983 & 4294967295L
                        | (
                              (int)(var811 * (var1 / 4) + (int)((int)var842 & 4294967295L) & 4294967295L) & 4294967295L
                                 | Double.doubleToRawLongBits(2.0 * ((int)var133 + 0.5) / (var1 / 4) - 1.0) & -4294967296L
                           )
                           & -4294967296L;
                     var10002 = (int)(var898 * (var1 / 4) + (int)((int)var933 & 4294967295L) & 4294967295L) & 4294967295L
                        | (int)(var898 * (var1 / 4) + (int)((int)var933 & 4294967295L) & 4294967295L) & -4294967296L;
                     long var999 = var992;
                     long var1001 = var10002;
                     long var1003 = var1160;
                     long var1005 = var997;
                     long var1007 = var999;
                     long var1009 = var1005;
                     long var1011 = var1007;
                     long var1013 = var1009;
                     long var1015 = var1011;
                     long var1017 = var1013;
                     var4.setPixel((int)var1003, (int)var1001, var619);
                     long var1019 = var1015;
                     long var1021 = var1017;
                     var10000 = (int)var1019 + 1 & 4294967295L;
                     var111 = var1019;
                     var113 = var1021;
                     var115 = var10000;
                  }
               }
            }

            if ((int)(var105 >>> 32) + 1 >= var91) {
               break;
            }

            long var1034 = (int)(var105 >>> 32) + 1 & 4294967295L;
            long var1161 = ((int)(var105 >>> 32) + 1 & 4294967295L) << 32 | var105 & 4294967295L;
            var97 = var103;
            var92 = var1161;
            var94 = var1034;
         }
      }

      return var3;
   }

   public static double computeDouble2(double d, int i) {
      if (d == 0.0) {
         return 1.0;
      }

      double d2 = Math.PI * d;
      return i * Math.sin(d2) * Math.sin(d2 / i) / (d2 * d2);
   }

   public static void handleDouble6(double[] doubles, double d, double d2) {
      doubles[0] = 1.0;
      doubles[1] = d;
      doubles[2] = -d2;
   }

   public static double computeDouble3(double d, double d2, double d3) {
      return Math.min(d3, Math.max(d, d2));
   }

   public static BiFunction<int[], int[], Integer> resolveBiFunction(BufferedImage bufferedImage) {
      int i = 5;
      int i2 = bufferedImage.getWidth();
      int i3 = bufferedImage.getHeight();
      WritableRaster writableRaster = bufferedImage.getRaster();
      int i4 = bufferedImage.getSampleModel().getNumBands();
      return (ints, ints2) -> {
         double d3 = ints[0] + 0.5;
         double d4 = ints[1] + 0.5;
         int i13 = (int)Math.floor(d3);
         int i14 = (int)Math.floor(d4);
         int i15 = i13 - i + 1;
         int i16 = i14 - i + 1;
         double[] doubles = new double[2 * i];
         double[] doubles2 = new double[2 * i];

         for (int i5 = 0; i5 < 2 * i; i5++) {
            doubles[i5] = computeDouble2(d3 - (i15 + i5), i);
            doubles2[i5] = computeDouble2(d4 - (i16 + i5), i);
         }

         double[] doubles3 = new double[3];

         for (int i12 = 0; i12 < 3; i12++) {
            double d2 = 0.0;

            for (int i11 = 0; i11 < 2 * i; i11++) {
               int i9 = i16 + i11;
               int i10 = computeInt(i9, 0, i3 - 1);
               double d = 0.0;

               for (int i8 = 0; i8 < 2 * i; i8++) {
                  int i6 = i15 + i8;
                  int i7 = computeInt(i6, 0, i2 - 1);
                  int[] ints3 = new int[i4];
                  writableRaster.getPixel(i7, i10, ints3);
                  d += ints3[i12] * doubles[i8];
               }

               d2 += d * doubles2[i11];
            }

            doubles3[i12] = Math.round(d2);
         }

         ints2[0] = (int)computeDouble3(doubles3[0], 0.0, 255.0);
         ints2[1] = (int)computeDouble3(doubles3[1], 0.0, 255.0);
         ints2[2] = (int)computeDouble3(doubles3[2], 0.0, 255.0);
         return 0;
      };
   }

   private static class FaceData {
      public int intValue;
      public SkyboxMath.Orientation orientation;
      public int intValue2;
      public String name;

      public FaceData(String text, int i, int i2, SkyboxMath.Orientation orientation) {
         this.name = text;
         this.intValue = i;
         this.intValue2 = i2;
         this.orientation = orientation;
      }
   }

   @FunctionalInterface
   private interface Orientation {
      void handleDouble(double[] var1, double var2, double var4);
   }
}
