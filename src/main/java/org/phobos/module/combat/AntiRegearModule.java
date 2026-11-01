package org.phobos.module.combat;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.ShulkerBoxScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ShulkerBoxScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.phobos.api.Visibility;
import org.phobos.api.MinecraftHolder;
import org.phobos.api.AreaScanner.Area;
import org.phobos.core.placement.DirectionResolver;
import org.phobos.core.block.PacketBlockBreaker;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.core.setting.SettingGroup;
import org.phobos.core.player.InteractionManager;
import org.phobos.core.rotation.RotationManager;
import org.phobos.core.PhobosClient;
import org.phobos.module.PlacementModule;
import org.phobos.core.module.Categories;
import org.phobos.core.block.BlockUtils;
import org.phobos.core.rotation.RotationUtils;
import org.phobos.core.entity.EntityUtils;
import org.phobos.core.inventory.InventoryUtils;
import org.phobos.event.InteractEvent;
import org.phobos.event.SafeListener;
import org.phobos.module.player.SpeedmineModule;

public class AntiRegearModule extends PlacementModule {
   public Setting<Long> longValueSetting2;
   public boolean flag;
   public Setting<AntiRegearModule.Mode> modeSetting;
   public Map<BlockPos, Long> blockPosMap;
   private static Object[] phobos$g1806d7dc8;
   private static Object[] phobos$g1806d7ed8;
   private static Object[] phobos$g1806d83b0;
   private static Object[] phobos$g1806d8740;

   public void handleClientPlayerInteractionManager(
      ClientPlayerInteractionManager clientPlayerInteractionManager, ClientPlayerEntity clientPlayerEntity, BlockPos blockPos, BlockHitResult blockHitResult
   ) {
      clientPlayerInteractionManager.interactBlock(clientPlayerEntity, Hand.MAIN_HAND, blockHitResult);
      this.blockPosMap.put(blockPos, System.currentTimeMillis() + 5000L);
      this.flag = true;
      if (this.getBooleanValueSetting4().getValue()) {
         clientPlayerEntity.swingHand(Hand.MAIN_HAND);
      }
   }

   public static boolean testEntry(Entry entry) {
      return (Long)entry.getValue() < System.currentTimeMillis();
   }

   public AntiRegearModule(PhobosClient phobosClient) {
      super(phobosClient, "AntiRegear", "Prevents enemies from regearing", Categories.combat, 9);
      this.nativeInit2(phobosClient);
   }

   public void nativeInit2(PhobosClient var1) {
      AntiRegearModule.Mode var3 = AntiRegearModule.Mode.Break;
      EnumSetting var2 = new EnumSetting<>("Mode", var3);
      Setting var4 = this.resolveSetting2(var2);
      this.modeSetting = var4;
      LongNumberSetting var5 = new LongNumberSetting("Timeout", 1000L, 250L, 5000L, true & true);
      Setting var6 = var5.resolveSetting7("Timeout for shulkers placed by you");
      Setting var7 = this.resolveSetting2(var6);
      this.longValueSetting2 = var7;
      HashMap var8 = new HashMap();
      this.blockPosMap = var8;
      this.flag = false & true;
      List var9 = this.getSettingGroupList2();
      Consumer<SettingGroup> var26 = this::handleSettingGroup;
      var9.forEach(var26);
      var var27 = new SafeListener<InteractEvent>() {
         public void handleObject(
            InteractEvent interactEvent,
            ClientPlayerEntity clientPlayerEntity,
            ClientWorld clientWorld,
            ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            if (interactEvent.getItemStack().getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof ShulkerBoxBlock) {
               BlockPos blockPos = interactEvent.getBlockPos();
               AntiRegearModule.this.blockPosMap.put(blockPos, System.currentTimeMillis() + AntiRegearModule.this.longValueSetting2.getValue());
            }
         }
      };
      this.handleListener(var27);
   }

   public void handleSettingGroup(SettingGroup settingGroup) {
      settingGroup.resolveSettingGroup5(Visibility.resolveVisibility3(this.modeSetting, AntiRegearModule.Mode.Drain, AntiRegearModule.Mode.Block));
   }

   public boolean testClientWorld(ClientWorld clientWorld, BlockPos blockPos) {
      return this.blockPosMap.containsKey(blockPos) ? false : clientWorld.getBlockState(blockPos).getBlock() instanceof ShulkerBoxBlock;
   }

   @Override
   public void handleItemSwitcher(
      ItemSwitcher itemSwitcher, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      long var105 = 0L;
      long var103 = 0L;
      PhobosClient var5 = this.phobosClient;
      Predicate[] var6 = new Predicate[0];
      PlayerEntity var7 = EntityUtils.resolvePlayerEntity(clientWorld, clientPlayerEntity, var5, 14.0, var6);
      if (var7 != null) {
         MinecraftClient var8 = MinecraftHolder.mc;
         Screen var9 = var8.currentScreen;
         boolean var10 = var9 instanceof ShulkerBoxScreen;
         if ((int)((!var10 ? 0 : 1) & 0xFF & 0xFF) == 0) {
            Map var11 = this.blockPosMap;
            Set var12 = var11.entrySet();
            Predicate<Entry> var29 = AntiRegearModule::testEntry;
            var12.removeIf(var29);
         }

         Area var30 = new Area();
         Vec3d var31 = clientPlayerEntity.getEyePos();
         AntiRegearModule var10000 = this;
         ClientWorld cap31 = clientWorld;
         AntiRegearModule cap30 = var10000;
         Predicate<BlockPos> var48 = var2 -> cap30.testClientWorld(cap31, var2);
         var30.handleVec3d(var31, 6.0, var48);
         Setting var49 = this.modeSetting;
         Object var50 = var49.getValue();
         boolean var51 = var50 instanceof AntiRegearModule.Mode;
         if (var50 != null && !var51) {
            throw new ClassCastException("CHECKCAST failed");
         }

         int var52 = ((AntiRegearModule.Mode)var50).ordinal();
         if (var52 == 0) {
            MinecraftClient var58 = MinecraftHolder.mc;
            Screen var59 = var58.currentScreen;
            if (var59 == null) {
               SpeedmineModule var60 = SpeedmineModule.getSpeedmineModule();
               PacketBlockBreaker var61 = var60.getPacketBlockBreaker();
               boolean var62 = var61.check();
               if (var62) {
                  PacketBlockBreaker var63 = var60.getPacketBlockBreaker();
                  BlockState var64 = var63.getBlockState2();
                  Block var65 = var64.getBlock();
                  boolean var66 = var65 instanceof ShulkerBoxBlock;
                  if ((int)((!var66 ? 0 : 1) & 0xFF & 0xFF) != 0) {
                     return;
                  }
               }

               Iterator var67 = var30.iterator();
               boolean var68 = var67.hasNext();
               if (var68) {
                  Object var69 = var67.next();
                  boolean var70 = var69 instanceof BlockPos;
                  if (var69 != null && !var70) {
                     throw new ClassCastException("CHECKCAST failed");
                  }

                  var60.handleBlockPos((BlockPos)var69, clientWorld, clientPlayerEntity);
               }
            }
         } else if (var52 + -1 != 0) {
            if (var52 + -1 == 1) {
               Block var53 = this.resolveBlock3(itemSwitcher);
               if (var53 != null) {
                  Iterator var54 = var30.iterator();

                  while (true) {
                     boolean var55 = var54.hasNext();
                     if (!var55) {
                        break;
                     }

                     Object var56 = var54.next();
                     boolean var57 = var56 instanceof BlockPos;
                     if (var56 != null && !var57) {
                        throw new ClassCastException("CHECKCAST failed");
                     }

                     BlockState var79 = clientWorld.getBlockState((BlockPos)var56);
                     EnumProperty var80 = ShulkerBoxBlock.FACING;
                     Comparable var81 = var79.get(var80);
                     boolean var82 = var81 instanceof Direction;
                     if (var81 != null && !var82) {
                        throw new ClassCastException("CHECKCAST failed");
                     }

                     BlockPos var88 = ((BlockPos)var56).offset((Direction)var81);
                     boolean var89 = BlockUtils.testBlockPos7(var88, clientWorld);
                     if (!var89) {
                        this.resolvePlacement8(var53, var88);
                     }
                  }
               }
            }
         } else {
            MinecraftClient var71 = MinecraftHolder.mc;
            Screen var72 = var71.currentScreen;
            boolean var73 = var72 instanceof ShulkerBoxScreen;
            if ((int)((!var73 ? 0 : 1) & 0xFF & 0xFF) != 0) {
               boolean var83 = var72 instanceof ShulkerBoxScreen;
               if (var72 != null && !var83) {
                  throw new ClassCastException("CHECKCAST failed");
               }

               boolean var90 = this.flag;
               if (var90) {
                  ScreenHandler var91 = ((ShulkerBoxScreen)var72).getScreenHandler();
                  boolean var92 = var91 instanceof ShulkerBoxScreenHandler;
                  if (var91 != null && !var92) {
                     throw new ClassCastException("CHECKCAST failed");
                  }

                  List var101 = InventoryUtils.resolveList3((ShulkerBoxScreenHandler)var91);
                  Iterator var102 = var101.iterator();

                  while (true) {
                     long var107 = var105;
                     long var109 = var103;
                     long var111 = var107;
                     long var113 = var109;
                     long var115 = var111;
                     long var117 = var113;
                     long var119 = var115;
                     long var121 = var117;
                     long var123 = var119;
                     long var125 = var121;
                     boolean var127 = var102.hasNext();
                     long var128 = var123;
                     long var130 = var125;
                     long var132 = var128;
                     long var134 = var130;
                     long var136 = var132;
                     long var138 = var134;
                     if (!var127) {
                        this.flag = false & true;
                        ((ShulkerBoxScreen)var72).close();
                        break;
                     }

                     long var140 = var136;
                     long var142 = var138;
                     long var147 = var140;
                     long var149 = var142;
                     long var151 = var147;
                     long var153 = var149;
                     long var155 = var151;
                     long var157 = var153;
                     long var159 = var155;
                     long var161 = var157;
                     Object var163 = var102.next();
                     long var164 = var159;
                     long var166 = var161;
                     boolean var168 = var163 instanceof Slot;
                     if (var163 != null && !var168) {
                        throw new ClassCastException("CHECKCAST failed");
                     }

                     long var173 = var164;
                     long var175 = var166;
                     long var216 = var173;
                     long var218 = var175;
                     int var220 = ((Slot)var163).id;
                     long var221 = var216;
                     long var223 = var218;
                     SlotActionType var225 = SlotActionType.THROW;
                     long var253 = var220 & 4294967295L | var223 & -4294967296L;
                     long var226 = 1L | var221 & -4294967296L;
                     long var228 = var253;
                     long var230 = var226;
                     long var232 = var228;
                     long var234 = var230;
                     long var236 = var232;
                     long var238 = var234;
                     long var240 = var236;
                     itemSwitcher.handleInt4((int)var240, (int)var238, var225, var91);
                     long var242 = var238;
                     long var244 = var240;
                     var105 = var242;
                     var103 = var244;
                  }
               }
            } else {
               boolean var74 = clientPlayerEntity.isSneaking();
               if (!var74) {
                  Iterator var75 = var30.iterator();

                  while (true) {
                     boolean var76 = var75.hasNext();
                     if (!var76) {
                        break;
                     }

                     Object var77 = var75.next();
                     boolean var78 = var77 instanceof BlockPos;
                     if (var77 != null && !var78) {
                        throw new ClassCastException("CHECKCAST failed");
                     }

                     BlockState var84 = clientWorld.getBlockState((BlockPos)var77);
                     EnumProperty var85 = ShulkerBoxBlock.FACING;
                     Comparable var86 = var84.get(var85);
                     boolean var87 = var86 instanceof Direction;
                     if (var86 != null && !var87) {
                        throw new ClassCastException("CHECKCAST failed");
                     }

                     BlockPos var93 = ((BlockPos)var77).offset((Direction)var86);
                     boolean var94 = BlockUtils.testBlockPos7(var93, clientWorld);
                     if (!var94) {
                        DirectionResolver var95 = this.getDirectionResolver();
                        Direction var96 = var95.resolveDirection((BlockPos)var77, clientPlayerEntity, clientWorld);
                        if (var96 != null) {
                           int[] var97 = new int[PlacementModule.Rotation.values().length];
                           var97[PlacementModule.Rotation.Normal.ordinal()] = 1;
                           var97[PlacementModule.Rotation.Silent.ordinal()] = 2;
                           var97[PlacementModule.Rotation.Hybrid.ordinal()] = 3;
                           Setting var98 = this.getRotationSetting();
                           Object var99 = var98.getValue();
                           boolean var100 = var99 instanceof PlacementModule.Rotation;
                           if (var99 != null && !var100) {
                              throw new ClassCastException("CHECKCAST failed");
                           }

                           int var144 = ((PlacementModule.Rotation)var99).ordinal();
                           int var145 = var97[var144];
                           if (var145 + -1 == 0) {
                              boolean var146 = RotationManager.isFlag();
                              if (var146) {
                                 continue;
                              }

                              float[] var207 = RotationUtils.values4(clientPlayerEntity, clientWorld, (BlockPos)var77, var96);
                              PhobosClient var208 = this.phobosClient;
                              RotationManager var209 = var208.getRotationManager();
                              float var210 = var207[0];
                              float var211 = var207[1];
                              var209.testFloat(
                                 (float)Float.intBitsToFloat(Float.floatToRawIntBits(var210)), (float)Float.intBitsToFloat(Float.floatToRawIntBits(var211))
                              );
                           } else if (var145 + -1 + -1 == 0 || var145 + -1 + -1 == 1) {
                              float[] var177 = RotationUtils.values4(clientPlayerEntity, clientWorld, (BlockPos)var77, var96);
                              PhobosClient var178 = this.phobosClient;
                              RotationManager var179 = var178.getRotationManager();
                              float var180 = var177[0];
                              float var181 = var177[1];
                              var179.handleClientPlayerEntity3(
                                 clientPlayerEntity,
                                 (float)Float.intBitsToFloat(Float.floatToRawIntBits(var180)),
                                 (float)Float.intBitsToFloat(Float.floatToRawIntBits(var181))
                              );
                           }

                           PhobosClient var182 = this.phobosClient;
                           InteractionManager var183 = var182.getInteractionManager();
                           DirectionResolver var184 = this.getDirectionResolver();
                           var10000 = this;
                           ClientPlayerInteractionManager var10001 = clientPlayerInteractionManager;
                           ClientPlayerEntity var10002 = clientPlayerEntity;
                           BlockPos cap35 = (BlockPos)var77;
                           ClientPlayerEntity cap34 = var10002;
                           ClientPlayerInteractionManager cap33 = var10001;
                           AntiRegearModule cap32 = var10000;
                           Consumer<BlockHitResult> var201 = var4 -> cap32.handleClientPlayerInteractionManager(cap33, cap34, cap35, var4);
                           var183.handleBlockPos((BlockPos)var77, clientPlayerEntity, clientWorld, var96, var184, var201);
                           Setting var202 = this.getRotationSetting();
                           PlacementModule.Rotation var203 = PlacementModule.Rotation.Silent;
                           boolean var204 = var202.testObject2(var203);
                           if (var204) {
                              PhobosClient var205 = this.phobosClient;
                              RotationManager var206 = var205.getRotationManager();
                              var206.handleClientPlayerEntity5(clientPlayerEntity);
                           }
                           break;
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public enum Mode {
      Break,
      Drain,
      Block;
   }
}
