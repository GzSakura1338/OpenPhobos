package org.phobos.module;

import lombok.SneakyThrows;
import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles.Lookup;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import org.jetbrains.annotations.ApiStatus.OverrideOnly;
import org.phobos.module.combat.SurroundModule;
import org.phobos.core.block.BlockPosUtils;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.module.Category;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.core.PhobosClient;
import org.phobos.core.placement.PositionPoolBuilder;
import org.phobos.core.setting.Setting;

public abstract class TrapModule extends PlacementModule {
   public final Setting<Boolean> extend;
   public final Setting<Boolean> block;
   public final Map<BlockPos, Long> blockPosMap;
   public final Setting<Boolean> below;
   public final Setting<Boolean> sides;
   public final Setting<Boolean> top = this.resolveSetting2(new BooleanSetting("Top"));
   private static Object[] phobos$g1806e8e10;
   private static Object[] phobos$g1806e8f08;
   private static Object[] phobos$g1806e93b0;

   @OverrideOnly
   public boolean check() {
      return this.extend.getValue();
   }

   public Map<BlockPos, Long> getBlockPosMap() {
      return this.blockPosMap;
   }

   public Setting<Boolean> getBelow() {
      return this.below;
   }

   public static boolean testEntry(Entry entry) {
      return (Long)entry.getValue() + 150L < System.currentTimeMillis();
   }

   @OverrideOnly
   public boolean check2() {
      return this.top.getValue();
   }

   public static boolean testPlayerEntity(PlayerEntity playerEntity, BlockPos blockPos) {
      return blockPos.getY() < playerEntity.getY() - 0.25;
   }

   public Setting<Boolean> getBlock() {
      return this.block;
   }

   @SneakyThrows public void handlePlacementModule(
      PlacementModule placementModule, ItemSwitcher itemSwitcher, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, Predicate<PlayerEntity> predicate
   ) {
      CallSite var348 = null;
      long var49 = 0L;
      Map var6 = this.blockPosMap;
      Set var7 = var6.entrySet();
      Predicate<Entry> var24 = TrapModule::testEntry;
      var7.removeIf(var24);
      Block var25 = this.resolveBlock2(itemSwitcher);
      Object var46;
      if (var25 == null) {
         Function[] var26 = new Function[1];
         Function<Slot, Block> var43 = this::resolveBlock;
         var26[0] = var43;
         Object var44 = itemSwitcher.resolveObject(var26);
         boolean var45 = var44 instanceof Block;
         if (var44 != null && !var45) {
            throw new ClassCastException("CHECKCAST failed");
         }

         if (var44 == null) {
            return;
         }

         var46 = var44;
      } else {
         var46 = var25;
      }

      List var47 = clientWorld.getPlayers();
      Iterator var48 = var47.iterator();

      while (true) {
         long var51 = var49;

         long var95;
         while (true) {
            long var53 = var51;
            long var55 = var53;
            long var57 = var55;
            boolean var59 = var48.hasNext();
            long var60 = var57;
            if (!var59) {
               return;
            }

            long var62 = var60;
            long var64 = var62;
            long var66 = var64;
            long var68 = var66;
            long var70 = var68;
            Object var72 = var48.next();
            long var73 = var70;
            boolean var75 = var72 instanceof PlayerEntity;
            if (var72 != null && !var75) {
               throw new ClassCastException("CHECKCAST failed");
            }

            long var78 = var73;
            long var82 = var78;
            long var84 = var82;
            long var86 = var84;
            long var88 = var86;
            boolean var90 = predicate.test((PlayerEntity)var72);
            long var91 = var88;
            if (!var90) {
               var95 = var91;
               break;
            }

            long var93 = var91;
            long var97 = var93;
            long var99 = var97;
            long var101 = var99;
            long var103 = var101;
            boolean var105 = this.check2();
            long var106 = var103;
            long var110;
            if (!var105) {
               var110 = var106;
            } else {
               long var108 = var106;
               long var112 = var108;
               long var114 = var112;
               long var116 = var114;
               long var118 = var116;
               Box var120 = ((PlayerEntity)var72).getBoundingBox();
               long var121 = var118;
               long var123 = var121;
               double var125 = var120.minY;
               long var127 = var123;
               long var129 = var127;
               Set var131 = BlockPosUtils.resolveSet3((Entity)var72, var125 + 2.5);
               long var132 = var129;
               long var134 = var132;
               long var136 = var134;
               long var138 = var136;
               long var140 = var138;
               Iterator var142 = var131.iterator();
               long var143 = var140;
               long var145 = var143;

               while (true) {
                  long var147 = var145;
                  long var149 = var147;
                  long var151 = var149;
                  long var153 = var151;
                  long var155 = var153;
                  long var157 = var155;
                  boolean var159 = var142.hasNext();
                  long var160 = var157;
                  long var162 = var160;
                  long var164 = var162;
                  if (!var159) {
                     long var168 = var164;
                     var110 = var168;
                     break;
                  }

                  long var166 = var164;
                  long var185 = var166;
                  long var187 = var185;
                  long var189 = var187;
                  long var191 = var189;
                  Object var193 = var142.next();
                  long var194 = var191;
                  boolean var196 = var193 instanceof BlockPos;
                  if (var193 != null && !var196) {
                     throw new ClassCastException("CHECKCAST failed");
                  }

                  long var383;
                  label204: {
                     long var199 = var194;
                     long var263 = var199;
                     long var265 = var263;
                     long var267 = var265;
                     long var269 = var267;
                     long var271 = var269;
                     long var273 = var271;
                     PlacementModule.Placement var275 = placementModule.resolvePlacement8((Block)var46, (BlockPos)var193);
                     long var276 = var273;
                     long var278 = var276;
                     long var280 = var278;
                     long var282 = var280;
                     long var284 = var282;
                     long var286 = var284;
                     long var288 = var286;
                     boolean var290 = var275.isSuccessful();
                     long var291 = var288;
                     long var293 = var291;
                     long var295 = var293;
                     long var299;
                     if (var290) {
                        long var297 = var295;
                        long var351 = var297;
                        Setting var353 = this.block;
                        long var354 = var351;
                        long var356 = var354;
                        long var358 = var356;
                        long var360 = var358;
                        Object var362 = var353.getValue();
                        long var363 = var360;
                        boolean var365 = var362 instanceof Boolean;
                        if (var362 != null && !var365) {
                           throw new ClassCastException("CHECKCAST failed");
                        }

                        long var368 = var363;
                        long var582 = var368;
                        long var584 = var582;
                        long var586 = var584;
                        long var588 = var586;
                        boolean var590 = (Boolean)var362;
                        long var591 = var588;
                        if (!var590) {
                           var383 = var591;
                           break label204;
                        }

                        long var593 = var591;
                        long var686 = var593;
                        long var688 = var686;
                        long var690 = var688;
                        long var692 = var690;
                        BlockPos var694 = ((BlockPos)var193).up();
                        long var695 = var692;
                        long var697 = var695;
                        long var699 = var697;
                        long var701 = var699;
                        long var703 = var701;
                        PlacementModule.Placement var705 = placementModule.resolvePlacement8((Block)var46, var694);
                        long var706 = var703;
                        long var708 = var706;
                        long var710 = var708;
                        long var712 = var710;
                        long var714 = var712;
                        boolean var716 = var705.isSuccessful();
                        long var717 = var714;
                        if (var716) {
                           var383 = var717;
                           break label204;
                        }

                        var299 = var717;
                     } else {
                        var299 = var295;
                     }

                     long var370 = var299;
                     long var372 = var370;
                     long var374 = var372;
                     long var376 = var374;
                     boolean var378 = this.check();
                     long var379 = var376;
                     if (var378) {
                        long var381 = var379;
                        PositionPoolBuilder[] var595 = new PositionPoolBuilder[0];
                        long var596 = var381;
                        long var598 = var596;
                        long var600 = var598;
                        long var602 = var600;
                        List var604 = placementModule.resolveList2((BlockPos)var193, clientWorld, clientPlayerEntity, (Block)var46, var595);
                        long var605 = var602;
                        long var607 = var605;
                        long var609 = var607;
                        long var611 = var609;
                        int var613 = (var604.isEmpty()) ? 1 : 0;
                        long var614 = var613 & 0xFF & 0xFF | var611 & -256L;
                        if ((int)((int)var614 & 0xFF & 0xFF) == 0) {
                           long var616 = var614;
                           long var719 = var616;
                           long var721 = var719;
                           long var723 = var721;
                           long var725 = var723;
                           Iterator var727 = var604.iterator();
                           long var728 = var725;
                           long var730 = var728;

                           while (true) {
                              long var732 = var730;
                              long var734 = var732;
                              long var736 = var734;
                              long var738 = var736;
                              long var740 = var738;
                              int var742 = (var727.hasNext()) ? 1 : 0;
                              long var743 = var742 & 0xFF & 0xFF | var740 & -256L;
                              long var745 = var743;
                              long var747 = var745;
                              if ((int)((int)var743 & 0xFF & 0xFF) == 0) {
                                 long var751 = var747;
                                 long var824 = var751;
                                 long var826 = var824;
                                 long var828 = var826;
                                 long var830 = var828;
                                 placementModule.resolvePlacement8((Block)var46, (BlockPos)var193);
                                 long var832 = var830;
                                 long var834 = var832;
                                 Setting var836 = this.block;
                                 long var837 = var834;
                                 long var839 = var837;
                                 long var841 = var839;
                                 long var843 = var841;
                                 Object var845 = var836.getValue();
                                 long var846 = var843;
                                 boolean var848 = var845 instanceof Boolean;
                                 if (var845 != null && !var848) {
                                    throw new ClassCastException("CHECKCAST failed");
                                 }

                                 long var851 = var846;
                                 long var992 = var851;
                                 long var994 = var992;
                                 long var996 = var994;
                                 long var998 = var996;
                                 boolean var1000 = (Boolean)var845;
                                 long var1001 = var998;
                                 if (var1000) {
                                    long var1003 = var1001;
                                    long var1052 = var1003;
                                    long var1054 = var1052;
                                    long var1056 = var1054;
                                    long var1058 = var1056;
                                    BlockPos var1060 = ((BlockPos)var193).up();
                                    long var1061 = var1058;
                                    long var1063 = var1061;
                                    long var1065 = var1063;
                                    long var1067 = var1065;
                                    long var1069 = var1067;
                                    placementModule.resolvePlacement8((Block)var46, var1060);
                                    long var1071 = var1069;
                                    var383 = var1071;
                                 } else {
                                    var383 = var1001;
                                 }
                                 break;
                              }

                              long var749 = var747;
                              long var802 = var749;
                              long var804 = var802;
                              long var806 = var804;
                              long var808 = var806;
                              long var810 = var808;
                              Object var812 = var727.next();
                              long var813 = var810;
                              long var815 = var813;
                              long var817 = var815;
                              boolean var819 = var812 instanceof BlockPos;
                              if (var812 != null && !var819) {
                                 throw new ClassCastException("CHECKCAST failed");
                              }

                              long var822 = var817;
                              long var962 = var822;
                              long var964 = var962;
                              long var966 = var964;
                              long var968 = var966;
                              long var970 = var968;
                              long var972 = var970;
                              long var974 = var972;
                              long var976 = var974;
                              long var978 = var976;
                              long var980 = var978;
                              long var982 = var980;
                              long var984 = var982;
                              placementModule.resolvePlacement8((Block)var46, (BlockPos)var812);
                              long var986 = var984;
                              long var988 = var986;
                              var730 = var988;
                           }
                        } else {
                           long var618 = var614;
                           var383 = var618;
                        }
                     } else {
                        var383 = var379;
                     }
                  }

                  var145 = var383;
               }
            }

            long var170 = var110;
            long var172 = var170;
            long var174 = var172;
            long var176 = var174;
            boolean var178 = this.check4();
            long var179 = var176;
            long var246;
            if (!var178) {
               long var183 = var179;
               var246 = var183;
            } else {
               long var181 = var179;
               long var201 = var181;
               long var203 = var201;
               Set var205 = BlockPosUtils.resolveSet7((Entity)var72, 0.5);
               long var206 = var203;
               long var208 = var206;
               long var210 = var208;
               long var212 = var210;
               long var214 = var212;
               Iterator var216 = var205.iterator();
               long var217 = var214;
               long var219 = var217;

               label232:
               while (true) {
                  long var221 = var219;

                  while (true) {
                     long var223 = var221;
                     long var225 = var223;
                     long var227 = var225;
                     long var229 = var227;
                     long var231 = var229;
                     long var233 = var231;
                     boolean var235 = var216.hasNext();
                     long var236 = var233;
                     long var238 = var236;
                     long var240 = var238;
                     if (!var235) {
                        long var244 = var240;
                        var246 = var244;
                        break label232;
                     }

                     long var242 = var240;
                     long var301 = var242;
                     long var303 = var301;
                     long var305 = var303;
                     long var307 = var305;
                     long var309 = var307;
                     long var311 = var309;
                     Object var313 = var216.next();
                     long var314 = var311;
                     long var316 = var314;
                     long var318 = var316;
                     boolean var320 = var313 instanceof BlockPos;
                     if (var313 != null && !var320) {
                        throw new ClassCastException("CHECKCAST failed");
                     }

                     long var323 = var318;
                     long var387 = var323;
                     long var389 = var387;
                     long var391 = var389;
                     long var393 = var391;
                     long var395 = var393;
                     long var397 = var395;
                     long var399 = var397;
                     long var401 = var399;
                     long var403 = var401;
                     long var405 = var403;
                     long var407 = var405;
                     long var409 = var407;
                     PlacementModule.Placement var411 = placementModule.resolvePlacement8((Block)var46, (BlockPos)var313);
                     long var412 = var409;
                     long var414 = var412;
                     long var416 = var414;
                     long var418 = var416;
                     long var420 = var418;
                     long var422 = var420;
                     long var424 = var422;
                     boolean var426 = var411.isSuccessful();
                     long var427 = var424;
                     long var429 = var427;
                     long var431 = var429;
                     long var435;
                     if (!var426) {
                        long var433 = var431;
                        long var620 = var433;
                        long var622 = var620;
                        long var624 = var622;
                        long var626 = var624;
                        boolean var628 = this.check();
                        long var629 = var626;
                        if (!var628) {
                           long var633 = var629;
                           var221 = var633;
                           continue;
                        }

                        long var631 = var629;
                        PositionPoolBuilder[] var753 = new PositionPoolBuilder[0];
                        long var754 = var631;
                        long var756 = var754;
                        long var758 = var756;
                        long var760 = var758;
                        List var762 = placementModule.resolveList2((BlockPos)var313, clientWorld, clientPlayerEntity, (Block)var46, var753);
                        long var763 = var760;
                        long var765 = var763;
                        long var767 = var765;
                        long var769 = var767;
                        long var771 = var769;
                        boolean var773 = var762.isEmpty();
                        long var774 = var771;
                        if (!var773) {
                           long var776 = var774;
                           long var853 = var776;
                           long var855 = var853;
                           long var857 = var855;
                           long var859 = var857;
                           Iterator var861 = var762.iterator();
                           long var862 = var859;
                           long var864 = var862;

                           while (true) {
                              long var866 = var864;
                              long var868 = var866;
                              long var870 = var868;
                              long var872 = var870;
                              long var874 = var872;
                              long var876 = var874;
                              boolean var878 = var861.hasNext();
                              long var879 = var876;
                              long var881 = var879;
                              long var883 = var881;
                              if (!var878) {
                                 long var887 = var883;
                                 long var1029 = var887;
                                 long var1031 = var1029;
                                 long var1033 = var1031;
                                 long var1035 = var1033;
                                 placementModule.resolvePlacement8((Block)var46, (BlockPos)var313);
                                 long var1037 = var1035;
                                 var435 = var1037;
                                 break;
                              }

                              long var885 = var883;
                              long var1005 = var885;
                              long var1007 = var1005;
                              long var1009 = var1007;
                              long var1011 = var1009;
                              long var1013 = var1011;
                              long var1015 = var1013;
                              Object var1017 = var861.next();
                              long var1018 = var1015;
                              long var1020 = var1018;
                              long var1022 = var1020;
                              boolean var1024 = var1017 instanceof BlockPos;
                              if (var1017 != null && !var1024) {
                                 throw new ClassCastException("CHECKCAST failed");
                              }

                              long var1027 = var1022;
                              long var1075 = var1027;
                              long var1077 = var1075;
                              long var1079 = var1077;
                              long var1081 = var1079;
                              long var1083 = var1081;
                              long var1085 = var1083;
                              long var1087 = var1085;
                              long var1089 = var1087;
                              long var1091 = var1089;
                              long var1093 = var1091;
                              long var1095 = var1093;
                              long var1097 = var1095;
                              placementModule.resolvePlacement8((Block)var46, (BlockPos)var1017);
                              long var1099 = var1097;
                              long var1101 = var1099;
                              var864 = var1101;
                           }
                        } else {
                           var435 = var774;
                        }
                     } else {
                        var435 = var431;
                     }

                     var219 = var435;
                     break;
                  }
               }
            }

            long var248 = var246;
            long var250 = var248;
            long var252 = var250;
            long var254 = var252;
            boolean var256 = this.check3();
            long var257 = var254;
            if (!var256) {
               var95 = var257;
               break;
            }

            long var259 = var257;
            long var325 = var259;
            long var327 = var325;
            long var329 = var327;
            SurroundModule var331 = SurroundModule.getSurroundModule();
            long var332 = var329;
            long var334 = var332;
            Block var336 = Blocks.OBSIDIAN;
            long var337 = var334;
            long var339 = var337;
            long var341 = var339;
            long var343 = var341;
            Set var345 = var331.resolveSet((PlayerEntity)var72, clientPlayerEntity, clientWorld, var336, false & true);
            long var346 = var343;
            long var545;
            if (var348 == null) {
               long var349 = var346;
               long var437 = var349;
               long var439 = var437;
               long var441 = var439;
               long var443 = var441;
               long var445 = var443;
               long var447 = var445;
               long var449 = var447;
               long var451 = var449;
               long var453 = var451;
               Lookup var455 = MethodHandles.lookup();
               long var456 = var453;
               long var458 = var456;
               long var460 = var458;
               long var462 = var460;
               long var464 = var462;
               long var466 = var464;
               long var468 = var466;
               Class<?> var470 = this.getClass();
               ClassLoader var471 = var470.getClassLoader();
               MethodType var472 = MethodType.fromMethodDescriptorString("(Lnet/minecraft/class_1657;)Ljava/util/function/Predicate;", var471);
               long var473 = var468;
               long var475 = var473;
               long var477 = var475;
               long var479 = var477;
               long var481 = var479;
               Class<?> var483 = this.getClass();
               ClassLoader var484 = var483.getClassLoader();
               MethodType var485 = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Z", var484);
               long var486 = var481;
               long var488 = var486;
               long var490 = var488;
               long var492 = var490;
               long var494 = var492;
               long var496 = var494;
               Class<?> var498 = this.getClass();
               ClassLoader var499 = var498.getClassLoader();
               MethodType var500 = MethodType.fromMethodDescriptorString("(Lnet/minecraft/class_1657;Lnet/minecraft/class_2338;)Z", var499);
               long var501 = var496;
               long var503 = var501;
               long var505 = var503;
               long var507 = var505;
               long var509 = var507;
               MethodHandle var511 = var455.findStatic(TrapModule.class, "testPlayerEntity", var500);
               long var512 = var509;
               long var514 = var512;
               long var516 = var514;
               long var518 = var516;
               long var520 = var518;
               Class<?> var522 = this.getClass();
               ClassLoader var523 = var522.getClassLoader();
               MethodType var524 = MethodType.fromMethodDescriptorString("(Lnet/minecraft/class_2338;)Z", var523);
               long var525 = var520;
               long var527 = var525;
               long var529 = var527;
               long var531 = var529;
               Object[] var10000 = new Object[0];
               long var533 = var531;
               long var535 = var533;
               var348 = LambdaMetafactory.metafactory(var455, "test", var472, var485, var511, var524);
               long var537 = var535;
               long var539 = var537;
               long var541 = var539;
               long var543 = var541;
               var545 = var543;
            } else {
               var545 = var346;
            }

            long var547 = var545;
            long var549 = var547;
            long var551 = var549;
            long var553 = var551;
            MethodHandle var555 = var348.getTarget();
            long var556 = var553;
            long var558 = var556;
            long var560 = var558;
            long var562 = var560;
            long var564 = var562;
            long var566 = var564;
            long var568 = var566;
            long var570 = var568;
            long var572 = var570;
            long var574 = var572;
            long var578;
            if (var72 != null) {
               long var576 = var574;
               Object[] var1228 = phobos$g1806e93b0;
               if (phobos$g1806e93b0 == null) {
                  var1228 = phobos$g1806e93b0 = new Object[1];
               }

               var1228[0] = var72;
               var578 = var576;
            } else {
               var578 = var574;
            }

            Object[] var10001 = phobos$g1806e93b0;
            if (phobos$g1806e93b0 == null) {
               var10001 = phobos$g1806e93b0 = new Object[1];
            }

            Object var635 = var555.invokeWithArguments(var10001);
            long var636 = var578;
            long var638 = var636;
            long var640 = var638;
            long var642 = var640;
            long var644 = var642;
            long var646 = var644;
            var345.removeIf((Predicate)var635);
            long var648 = var646;
            long var650 = var648;
            long var652 = var650;
            long var654 = var652;
            long var656 = var654;
            Iterator var658 = var345.iterator();
            long var659 = var656;
            long var661 = var659;

            while (true) {
               long var663 = var661;
               long var665 = var663;
               long var667 = var665;
               long var669 = var667;
               long var671 = var669;
               long var673 = var671;
               boolean var675 = var658.hasNext();
               long var676 = var673;
               long var678 = var676;
               long var680 = var678;
               if (!var675) {
                  long var684 = var680;
                  var51 = var684;
                  break;
               }

               long var682 = var680;
               long var778 = var682;
               long var780 = var778;
               long var782 = var780;
               long var784 = var782;
               long var786 = var784;
               long var788 = var786;
               Object var790 = var658.next();
               long var791 = var788;
               long var793 = var791;
               long var795 = var793;
               boolean var797 = var790 instanceof BlockPos;
               if (var790 != null && !var797) {
                  throw new ClassCastException("CHECKCAST failed");
               }

               long var800 = var795;
               long var891 = var800;
               long var893 = var891;
               long var895 = var893;
               long var897 = var895;
               long var899 = var897;
               long var901 = var899;
               long var903 = var901;
               long var905 = var903;
               BlockPos var907 = ((BlockPos)var790).up();
               long var908 = var905;
               long var910 = var908;
               long var912 = var910;
               long var914 = var912;
               long var916 = var914;
               long var918 = var916;
               long var920 = var918;
               long var922 = var920;
               long var924 = var922;
               long var926 = var924;
               long var928 = var926;
               long var930 = var928;
               long var932 = var930;
               PlacementModule.Placement var934 = placementModule.resolvePlacement8((Block)var46, var907);
               long var935 = var932;
               long var937 = var935;
               long var939 = var937;
               long var941 = var939;
               long var943 = var941;
               long var945 = var943;
               long var947 = var945;
               boolean var949 = var934.isSuccessful();
               long var950 = var947;
               long var952 = var950;
               long var954 = var952;
               long var958;
               if (!var949) {
                  long var956 = var954;
                  long var1039 = var956;
                  long var1041 = var1039;
                  long var1043 = var1041;
                  long var1045 = var1043;
                  boolean var1047 = this.check();
                  long var1048 = var1045;
                  if (!var1047) {
                     var661 = var1048;
                     continue;
                  }

                  long var1050 = var1048;
                  PositionPoolBuilder[] var1103 = new PositionPoolBuilder[0];
                  long var1104 = var1050;
                  long var1106 = var1104;
                  long var1108 = var1106;
                  long var1110 = var1108;
                  List var1112 = placementModule.resolveList2(var907, clientWorld, clientPlayerEntity, (Block)var46, var1103);
                  long var1113 = var1110;
                  long var1115 = var1113;
                  long var1117 = var1115;
                  long var1119 = var1117;
                  long var1121 = var1119;
                  boolean var1123 = var1112.isEmpty();
                  long var1124 = var1121;
                  if (!var1123) {
                     long var1126 = var1124;
                     long var1128 = var1126;
                     long var1130 = var1128;
                     long var1132 = var1130;
                     long var1134 = var1132;
                     Iterator var1136 = var1112.iterator();
                     long var1137 = var1134;
                     long var1139 = var1137;

                     while (true) {
                        long var1141 = var1139;
                        long var1143 = var1141;
                        long var1145 = var1143;
                        long var1147 = var1145;
                        long var1149 = var1147;
                        long var1151 = var1149;
                        boolean var1153 = var1136.hasNext();
                        long var1154 = var1151;
                        long var1156 = var1154;
                        long var1158 = var1156;
                        if (!var1153) {
                           long var1162 = var1158;
                           long var1188 = var1162;
                           long var1190 = var1188;
                           long var1192 = var1190;
                           long var1194 = var1192;
                           placementModule.resolvePlacement8((Block)var46, var907);
                           long var1196 = var1194;
                           var958 = var1196;
                           break;
                        }

                        long var1160 = var1158;
                        long var1164 = var1160;
                        long var1166 = var1164;
                        long var1168 = var1166;
                        long var1170 = var1168;
                        long var1172 = var1170;
                        long var1174 = var1172;
                        Object var1176 = var1136.next();
                        long var1177 = var1174;
                        long var1179 = var1177;
                        long var1181 = var1179;
                        boolean var1183 = var1176 instanceof BlockPos;
                        if (var1176 != null && !var1183) {
                           throw new ClassCastException("CHECKCAST failed");
                        }

                        long var1186 = var1181;
                        long var1200 = var1186;
                        long var1202 = var1200;
                        long var1204 = var1202;
                        long var1206 = var1204;
                        long var1208 = var1206;
                        long var1210 = var1208;
                        long var1212 = var1210;
                        long var1214 = var1212;
                        long var1216 = var1214;
                        long var1218 = var1216;
                        long var1220 = var1218;
                        long var1222 = var1220;
                        placementModule.resolvePlacement8((Block)var46, (BlockPos)var1176);
                        long var1224 = var1222;
                        long var1226 = var1224;
                        var1139 = var1226;
                     }
                  } else {
                     var958 = var1124;
                  }
               } else {
                  var958 = var954;
               }

               var661 = var958;
            }
         }

         var49 = var95;
      }
   }

   @OverrideOnly
   public boolean check3() {
      return this.sides.getValue();
   }

   public Setting<Boolean> getExtend() {
      return this.extend;
   }

   public TrapModule(PhobosClient phobosClient, String text, String text2, Category category, int i) {
      super(phobosClient, text, text2, category, i);
      this.block = this.resolveSetting2(new BooleanSetting("Block").resolveSetting(this.top));
      this.sides = this.resolveSetting2(new BooleanSetting("Sides"));
      this.below = this.resolveSetting2(new BooleanSetting("Below"));
      this.extend = this.resolveSetting2(new BooleanSetting("Extend"));
      this.blockPosMap = new HashMap<>();
      this.getSettingGroupList2().add(this.resolveSettingGroup2("Trap", this.top, this.block, this.sides, this.below, this.extend));
      this.handleTriConsumer2((clientPlayerEntity, clientWorld, clientPlayerInteractionManager) -> this.blockPosMap.clear());
   }

   @OverrideOnly
   public boolean check4() {
      return this.below.getValue();
   }

   public Setting<Boolean> getSides() {
      return this.sides;
   }

   public Setting<Boolean> getTop() {
      return this.top;
   }

   public Block resolveBlock(Slot slot) {
      return slot.getStack().getItem() instanceof BlockItem blockItem && !this.getSetSetting().getValue().contains(blockItem.getBlock())
         ? blockItem.getBlock()
         : null;
   }
}
