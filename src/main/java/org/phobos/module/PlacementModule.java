package org.phobos.module;

import lombok.SneakyThrows;
import java.awt.Color;
import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MethodHandles.Lookup;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BucketItem;
import net.minecraft.item.FluidModificationItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.LookAndOnGround;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import org.apache.commons.lang3.mutable.MutableObject;
import org.earthhack.ducks.IBucketItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.OverrideOnly;
import org.phobos.api.TextProvider;
import org.phobos.api.PlacementApi;
import org.phobos.api.MinecraftHolder;
import org.phobos.api.TickGate.Normal;
import org.phobos.core.inventory.ItemSwitcher.SwitchResult;
import org.phobos.core.player.InteractionManager.Silent;
import org.phobos.core.render.Render3d.Cuboid;
import org.phobos.event.Listener;
import org.phobos.event.PlayerTickEvent;
import org.phobos.event.SafeListener;
import org.phobos.module.client.AntiCheatModule;
import org.phobos.module.Module;
import org.phobos.module.player.SilentHotbarModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.phobos.core.animation.AnimatedDouble;
import org.phobos.core.block.BlockDirectionUtils;
import org.phobos.core.setting.BlockSetSetting;
import org.phobos.core.block.BlockUtils;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.math.BoxUtils;
import org.phobos.core.module.Category;
import org.phobos.core.setting.ColorSetting;
import org.phobos.core.combat.CrystalUtils;
import org.phobos.core.placement.DefaultDirectionResolver;
import org.phobos.core.placement.DirectionResolver;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.placement.ExactDirectionResolver;
import org.phobos.core.render.Highlights;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.module.ModuleListenerUtils;
import org.phobos.core.module.ModuleManager;
import org.phobos.core.movement.MovementSimulator;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.PhobosClient;
import org.phobos.core.player.PlayerActionUtils;
import org.phobos.core.placement.PositionPoolBuilder;
import org.phobos.core.world.RaycastUtils;
import org.phobos.core.render.Render3d;
import org.phobos.core.render.RenderContext;
import org.phobos.core.rotation.RotationManager;
import org.phobos.core.rotation.RotationUtils;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.SettingGroup;
import org.phobos.core.placement.ShapeDirectionResolver;
import org.phobos.core.tick.TickUtils;
import org.phobos.core.event.events.UnusedEvent;

public abstract class PlacementModule extends Module implements PlacementApi {
   public Setting<Boolean> booleanValueSetting;
   public Setting<Silent> silentSetting;
   public Setting<Double> doubleValueSetting;
   public Setting<Boolean> booleanValueSetting2;
   public Setting<Boolean> booleanValueSetting3;
   public Setting<PlacementModule.Timing> timingSetting;
   public Setting<Boolean> booleanValueSetting4;
   public Setting<Integer> integerSetting;
   public Setting<Set<Block>> setSetting;
   public Setting<Integer> integerSetting2;
   public Setting<Boolean> booleanValueSetting5;
   public static final Logger LOGGER = LoggerFactory.getLogger(PlacementModule.class);
   public Setting<PlacementModule.Rotation> rotationSetting;
   public Setting<PlacementModule.Airplace> airplaceSetting;
   public Setting<Boolean> booleanValueSetting6;
   public Setting<Boolean> booleanValueSetting7;
   public Setting<Integer> integerSetting3;
   public Normal normal;
   public Setting<Long> longValueSetting;
   public List<PlacementModule.Action> actionList;
   public org.phobos.api.TimeGate.Normal normal2;
   public AtomicInteger atomicInteger;
   public PositionPoolBuilder positionPoolBuilder;
   public Setting<Color> colorSetting;
   public Setting<Boolean> booleanValueSetting8;
   public Setting<Integer> integerSetting4;
   public Setting<PlacementModule.SwitchBack> switchBackSetting;
   public Setting<Boolean> booleanValueSetting9;
   public Setting<Color> colorSetting2;
   public List<SettingGroup> settingGroupList;
   @Nullable
   public Consumer<PlacementModule.Action> actionConsumer;
   public Setting<Float> floatValueSetting;
   public AnimatedDouble animatedDouble;
   private static Object[] phobos$g1806e8b78;
   private static Object[] phobos$g1806e7570;
   private static Object[] phobos$g1806e77c8;

   public PlacementModule.Placement resolvePlacement(Item item, BlockPos blockPos) {
      return this.resolvePlacement5(item, blockPos, null);
   }

   public PlacementModule.Placement resolvePlacement2(Item item, BlockPos blockPos, @Nullable Direction direction) {
      return this.resolvePlacement3(item, blockPos, direction, true);
   }

   public PlacementModule(PhobosClient phobosClient, String text, String text2, Category category, int i) {
      super(phobosClient, text, text2, category);
      this.nativeInit(phobosClient, text, text2, category, i);
   }

   public void nativeInit(final PhobosClient var1, String var2, String var3, Category var4, int var5) {
      PlacementModule.Timing var7 = PlacementModule.Timing.Vanilla;
      EnumSetting var6 = new EnumSetting<>("Timing", var7);
      Setting var8 = var6.resolveSetting7("Vanilla - Vanilla, Sequential - NCP");
      Setting var9 = this.resolveSetting2(var8);
      this.timingSetting = var9;
      Integer var11 = 6;
      Integer var12 = 1;
      Integer var13 = 36;
      NumberSetting var10 = new NumberSetting<>("MainTicks", var11, var12, var13);
      Setting var14 = var10.resolveSetting7("Blocks pre tick");
      Setting var15 = this.resolveSetting2(var14);
      this.integerSetting2 = var15;
      Integer var17 = 0;
      Integer var18 = 0;
      Integer var19 = 10;
      NumberSetting var16 = new NumberSetting<>("MainDelay", var17, var18, var19);
      Setting var20 = var16.resolveSetting7("Delay in ticks");
      Setting var21 = this.resolveSetting2(var20);
      this.integerSetting = var21;
      Integer var23 = 6;
      Integer var24 = 1;
      Integer var25 = 10;
      NumberSetting var22 = new NumberSetting<>("MainExtension", var23, var24, var25);
      Setting var26 = var22.resolveSetting7("Extension limit");
      Setting var27 = this.resolveSetting2(var26);
      this.integerSetting3 = var27;
      Boolean var29 = true & true;
      BooleanSetting var28 = new BooleanSetting("MainSwing", var29);
      Setting var30 = var28.resolveSetting7("Hand swing");
      Setting var31 = this.resolveSetting2(var30);
      this.booleanValueSetting4 = var31;
      Boolean var33 = false & true;
      BooleanSetting var32 = new BooleanSetting("MainAttack", var33);
      Setting var34 = var32.resolveSetting7("Attack blocking crystals");
      Setting var35 = this.resolveSetting2(var34);
      this.booleanValueSetting2 = var35;
      Boolean var37 = false & true;
      BooleanSetting var36 = new BooleanSetting("MainMultiTask", var37);
      Setting var38 = var36.resolveSetting7("Place while using");
      Setting var39 = this.resolveSetting2(var38);
      this.booleanValueSetting5 = var39;
      Double var41 = 6.0;
      Double var42 = 0.0;
      Double var43 = 6.0;
      NumberSetting var40 = new NumberSetting<>("MainRange", var41, var42, var43);
      Setting var44 = var40.resolveSetting7("Interaction range");
      Setting var45 = this.resolveSetting2(var44);
      this.doubleValueSetting = var45;
      Block[] var47 = new Block[0];
      BlockSetSetting var46 = new BlockSetSetting("MainBlacklist", var47);
      Setting var48 = var46.resolveSetting7("Blacklisted blocks");
      Setting var49 = this.resolveSetting2(var48);
      this.setSetting = var49;
      PlacementModule.Rotation var51 = PlacementModule.Rotation.Normal;
      EnumSetting var50 = new EnumSetting<>("InteractionRotation", var51);
      Setting var52 = var50.resolveSetting7("Rotation type");
      Setting var53 = this.resolveSetting2(var52);
      this.rotationSetting = var53;
      BooleanSetting var54 = new BooleanSetting("Raytrace");
      Setting var55 = var54.resolveSetting7("Raytrace checks the direction placing on");
      Setting var56 = this.resolveSetting2(var55);
      this.booleanValueSetting6 = var56;
      Boolean var58 = false & true;
      BooleanSetting var57 = new BooleanSetting("InteractionStrictDirection", var58);
      Setting var59 = var57.resolveSetting7("Strict direction");
      Setting var60 = this.resolveSetting2(var59);
      this.booleanValueSetting7 = var60;
      Boolean var62 = false & true;
      BooleanSetting var61 = new BooleanSetting("InteractionGrim", var62);
      Setting var63 = var61.resolveSetting7("Grim");
      Setting var64 = this.resolveSetting2(var63);
      this.booleanValueSetting = var64;
      PlacementModule.Airplace var66 = PlacementModule.Airplace.None;
      EnumSetting var65 = new EnumSetting<>("InteractionAirPlace", var66);
      Setting var67 = var65.resolveSetting7("Allows placing on air");
      Setting var68 = this.resolveSetting2(var67);
      this.airplaceSetting = var68;
      Boolean var70 = false & true;
      BooleanSetting var69 = new BooleanSetting("InteractionNoGlitchBlocks", var70);
      Setting var71 = var69.resolveSetting7("No glitch blocks");
      Setting var72 = this.resolveSetting2(var71);
      this.booleanValueSetting3 = var72;
      Silent var74 = Silent.Normal;
      EnumSetting var73 = new EnumSetting<>("InteractionSwitch", var74);
      Setting var75 = var73.resolveSetting7("Switch type");
      Setting var76 = this.resolveSetting2(var75);
      this.silentSetting = var76;
      PlacementModule.SwitchBack var78 = PlacementModule.SwitchBack.Silent;
      EnumSetting var77 = new EnumSetting<>("Silent", var78);
      Setting var79 = var77.resolveSetting7("Swap back");
      Setting var80 = this.resolveSetting2(var79);
      this.switchBackSetting = var80;
      Integer var82 = 2;
      Integer var83 = 1;
      Integer var84 = 10;
      NumberSetting var81 = new NumberSetting<>("Conceal", var82, var83, var84, "t");
      Setting var85 = this.switchBackSetting;
      PlacementModule.SwitchBack var86 = PlacementModule.SwitchBack.Spoof;
      Setting var87 = var81.resolveSetting4(var85, var86);
      Setting var88 = this.resolveSetting2(var87);
      this.integerSetting4 = var88;
      BooleanSetting var89 = new BooleanSetting("Render");
      Setting var90 = var89.resolveSetting7("Render when placing");
      Setting var91 = this.resolveSetting2(var90);
      this.booleanValueSetting8 = var91;
      Color var93 = new Color(255, 255, 255, 70);
      ColorSetting var92 = new ColorSetting("RenderBox", var93);
      ColorSetting var94 = var92.getColorSetting();
      Setting var95 = this.resolveSetting2(var94);
      this.colorSetting2 = var95;
      Color var97 = Color.WHITE;
      ColorSetting var96 = new ColorSetting("RenderLine", var97);
      ColorSetting var98 = var96.getColorSetting();
      Setting var99 = this.resolveSetting2(var98);
      this.colorSetting = var99;
      Float var101 = (float)1.0F;
      Float var102 = (float)0.0F;
      Float var103 = (float)3.0F;
      NumberSetting var100 = new NumberSetting<>("RenderWidth", var101, var102, var103);
      Setting var104 = var100.resolveSetting7("Line width");
      Setting var105 = this.resolveSetting2(var104);
      this.floatValueSetting = var105;
      LongNumberSetting var106 = new LongNumberSetting("RenderFade", 500L, 0L, 2000L, true & true);
      Setting var107 = var106.resolveSetting7("Fade time");
      Setting var108 = this.resolveSetting2(var107);
      this.longValueSetting = var108;
      BooleanSetting var109 = new BooleanSetting("Debug");
      Setting var110 = var109.getSetting2();
      Setting var111 = this.resolveSetting2(var110);
      this.booleanValueSetting9 = var111;
      PositionPoolBuilder var112 = new PositionPoolBuilder();
      this.positionPoolBuilder = var112;
      org.phobos.api.TimeGate.Normal var113 = new org.phobos.api.TimeGate.Normal();
      this.normal2 = var113;
      ArrayList var114 = new ArrayList();
      this.actionList = var114;
      AtomicInteger var115 = new AtomicInteger(0);
      this.atomicInteger = var115;
      Normal var116 = new Normal();
      this.normal = var116;
      ArrayList var117 = new ArrayList();
      this.settingGroupList = var117;
      AnimatedDouble var118 = new AnimatedDouble();
      this.animatedDouble = var118;
      this.actionConsumer = (Consumer<PlacementModule.Action>)null;
      Setting var119 = this.setSetting;
      Object var120 = var119.get();
      boolean var121 = var120 instanceof Set;
      if (var120 != null && !var121) {
         throw new ClassCastException("CHECKCAST failed");
      }

      Block var122 = Blocks.TNT;
      ((Set)var120).add(var122);
      List var123 = this.settingGroupList;
      Setting[] var124 = new Setting[9];
      Setting var125 = this.timingSetting;
      var124[0] = var125;
      Setting var126 = this.integerSetting2;
      var124[1] = var126;
      Setting var127 = this.integerSetting;
      var124[2] = var127;
      Setting var128 = this.integerSetting3;
      var124[3] = var128;
      Setting var129 = this.booleanValueSetting4;
      var124[4] = var129;
      Setting var130 = this.booleanValueSetting2;
      var124[5] = var130;
      Setting var131 = this.booleanValueSetting5;
      var124[6] = var131;
      Setting var132 = this.doubleValueSetting;
      var124[7] = var132;
      Setting var133 = this.setSetting;
      var124[8] = var133;
      SettingGroup var134 = this.resolveSettingGroup2("Main", var124);
      var123.add(var134);
      List var135 = this.settingGroupList;
      Setting[] var136 = new Setting[9];
      Setting var137 = this.rotationSetting;
      var136[0] = var137;
      Setting var138 = this.booleanValueSetting6;
      var136[1] = var138;
      Setting var139 = this.booleanValueSetting7;
      var136[2] = var139;
      Setting var140 = this.booleanValueSetting;
      var136[3] = var140;
      Setting var141 = this.airplaceSetting;
      var136[4] = var141;
      Setting var142 = this.booleanValueSetting3;
      var136[5] = var142;
      Setting var143 = this.silentSetting;
      var136[6] = var143;
      Setting var144 = this.switchBackSetting;
      var136[7] = var144;
      Setting var145 = this.integerSetting4;
      var136[8] = var145;
      SettingGroup var146 = this.resolveSettingGroup2("Interaction", var136);
      var135.add(var146);
      List var147 = this.settingGroupList;
      Setting var148 = this.booleanValueSetting8;
      Setting[] var149 = new Setting[4];
      Setting var150 = this.colorSetting2;
      var149[0] = var150;
      Setting var151 = this.colorSetting;
      var149[1] = var151;
      Setting var152 = this.floatValueSetting;
      var149[2] = var152;
      Setting var153 = this.longValueSetting;
      var149[3] = var153;
      SettingGroup var154 = this.resolveSettingGroup(var148, var149);
      var147.add(var154);
      var var155 = new SafeListener<PlayerTickEvent>(var5 + 10) {
         public void handleObject(
            PlayerTickEvent playerTickEvent,
            ClientPlayerEntity clientPlayerEntity,
            ClientWorld clientWorld,
            ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            if (PlacementModule.this.normal.testInt3(PlacementModule.this.integerSetting.getValue())) {
               var1.getInventoryManager()
                  .handleConsumer2(
                     itemSwitcher -> {
                        PlacementModule.this.handleItemSwitcher(itemSwitcher, clientPlayerEntity, clientWorld, clientPlayerInteractionManager);
                        if (PlacementModule.this.timingSetting.testObject2(PlacementModule.Timing.Vanilla)
                           || !PlacementModule.this.rotationSetting.testObject2(PlacementModule.Rotation.None) && !PlacementModule.this.actionList.isEmpty()) {
                           PlacementModule.this.handleItemSwitcher2(
                              itemSwitcher,
                              clientPlayerEntity,
                              clientWorld,
                              clientPlayerInteractionManager,
                              PlacementModule.this.timingSetting.testObject2(PlacementModule.Timing.Vanilla)
                           );
                        }
                     }
                  );
            }
         }
      };
      this.handleListener(var155);
      var var156 = new SafeListener<UnusedEvent>(var5 + 10) {
         public void handleObject(
            UnusedEvent unusedEvent, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            if (PlacementModule.this.timingSetting.testObject2(PlacementModule.Timing.Sequential)) {
               var1.getInventoryManager()
                  .handleConsumer2(
                     itemSwitcher -> PlacementModule.this.handleItemSwitcher2(itemSwitcher, clientPlayerEntity, clientWorld, clientPlayerInteractionManager, true)
                  );
            }
         }
      };
      this.handleListener(var156);
      var var157 = new Listener<RenderContext>() {
         public void handleObject2(RenderContext renderContext) {
            if (PlacementModule.this.normal2.testLong(250L)) {
               PlacementModule.this.animatedDouble.handleDouble4(0.0);
            }

            PlacementModule.this.animatedDouble.run();
         }
      };
      this.handleListener(var157);
      List var158 = this.actionList;
      Runnable var175 = var158::clear;
      ModuleListenerUtils.handleListenerHolder4(this, var175);
      ModuleManager var176 = var1.getModuleManager();
      PlacementModuleSet var177 = var176.getPlacementModuleSet();
      var177.handlePlacementModule(this);
   }

   public PlacementModule.Placement resolvePlacement3(Item item, BlockPos blockPos, @Nullable Direction direction, boolean flag) {
      return this.resolvePlacement11(item, blockPos, direction, flag, false, null);
   }

   public void handleAction(PlacementModule.Action action) {
      if (this.actionConsumer != null) {
         this.actionConsumer.accept(action);
         this.actionConsumer = null;
      }
   }

   public boolean testClientPlayerEntity2(
      ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, Block block, BlockPos blockPos, PositionPoolBuilder... positionPoolBuilders
   ) {
      if (this.resolvePlacement8(block, blockPos).isSuccessful()) {
         return true;
      }

      for (BlockPos blockPos2 : this.resolveList2(blockPos, clientWorld, clientPlayerEntity, block, positionPoolBuilders)) {
         this.resolvePlacement8(block, blockPos2);
      }

      return this.resolvePlacement8(block, blockPos).isSuccessful();
   }

   public boolean check6() {
      return this.isFlag13() && this.booleanValueSetting9.getValue();
   }

   public PlacementModule.Placement resolvePlacement4(Block block, BlockPos blockPos, Direction direction) {
      return this.resolvePlacement3(block.asItem(), blockPos, direction, true);
   }

   public void clear() {
      this.actionConsumer = null;
   }

   public abstract void handleItemSwitcher(ItemSwitcher var1, ClientPlayerEntity var2, ClientWorld var3, ClientPlayerInteractionManager var4);

   public void handleClientPlayerEntity4(
      ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      this.phobosClient.getInventoryManager().handleConsumer2(itemSwitcher -> {
         this.handleItemSwitcher(itemSwitcher, clientPlayerEntity, clientWorld, clientPlayerInteractionManager);
         this.handleItemSwitcher2(itemSwitcher, clientPlayerEntity, clientWorld, clientPlayerInteractionManager, true);
      });
   }

   public PlacementModule.Placement resolvePlacement5(Item item, BlockPos blockPos, @Nullable Direction direction) {
      return this.resolvePlacement6(item, blockPos, direction, true);
   }

   public PlacementModule(PhobosClient phobosClient, String text, Category category, int i) {
      this(phobosClient, text, "", category, i);
   }

   public void setActionConsumer(Consumer<PlacementModule.Action> consumer) {
      this.actionConsumer = consumer;
   }

   public PlacementModule.Placement resolvePlacement6(Item item, BlockPos blockPos, @Nullable Direction direction, boolean flag) {
      return this.resolvePlacement13(item, blockPos, direction, flag, false, null);
   }

   public PlacementModule.Placement resolvePlacement7(Block block, BlockPos blockPos, Direction direction) {
      return this.resolvePlacement6(block.asItem(), blockPos, direction, true);
   }

   @SneakyThrows public void handleItemSwitcher2(
      ItemSwitcher itemSwitcher,
      ClientPlayerEntity clientPlayerEntity,
      ClientWorld clientWorld,
      ClientPlayerInteractionManager clientPlayerInteractionManager,
      boolean flag
   ) {
      CallSite var226 = null;
      long var22 = 0L;
      List var6 = this.actionList;
      boolean var7 = var6.isEmpty();
      if (!var7) {
         org.phobos.api.TimeGate.Normal var8 = this.normal2;
         var8.run();
         AntiCheatModule var9 = AntiCheatModule.getAntiCheatModule();
         Setting var10 = var9.getSneak();
         Object var11 = var10.getValue();
         boolean var12 = var11 instanceof Boolean;
         if (var11 != null && !var12) {
            throw new ClassCastException("CHECKCAST failed");
         }

         boolean var14 = (Boolean)var11;
         if (var14) {
            PhobosClient var15 = this.phobosClient;
            MovementSimulator var16 = var15.getMovementSimulator();
            var16.handleClientPlayerEntity2(clientPlayerEntity);
         }

         List var18 = this.actionList;
         int var19 = var18.size();
         ArrayList var17 = new ArrayList(var19);
         long var20 = 0L;

         while (true) {
            long var24 = (int)var20 & 4294967295L | var22 & -4294967296L;
            long var26 = var24;
            long var28 = var26;
            long var30 = var28;
            long var32 = var30;
            List var34 = this.actionList;
            long var35 = var32;
            long var37 = var35;
            long var39 = var37;
            long var41 = var39;
            long var43 = var41;
            long var45 = var43;
            int var47 = var34.size();
            long var48 = var45;
            long var50 = var48;
            long var52 = var50;
            if ((int)((int)var20 & 4294967295L) >= var47) {
               break;
            }

            long var54 = var52;
            long var56 = var54;
            long var58 = var56;
            long var60 = var58;
            long var62 = var60;
            long var64 = var62;
            Setting var66 = this.integerSetting2;
            long var67 = var64;
            long var69 = var67;
            long var71 = var69;
            long var73 = var71;
            long var75 = var73;
            long var77 = var75;
            Object var79 = var66.getValue();
            long var80 = var77;
            long var82 = var80;
            long var84 = var82;
            boolean var86 = var79 instanceof Integer;
            if (var79 != null && !var86) {
               throw new ClassCastException("CHECKCAST failed");
            }

            long var89 = var84;
            long var115 = var89;
            long var117 = var115;
            long var119 = var117;
            long var121 = var119;
            long var123 = var121;
            long var125 = var123;
            int var127 = (Integer)var79;
            long var128 = var125;
            long var130 = var128;
            long var132 = var130;
            if ((int)((int)var20 & 4294967295L) >= var127) {
               break;
            }

            long var134 = var132;
            long var139 = var134;
            long var141 = var139;
            long var143 = var141;
            List var145 = this.actionList;
            long var146 = var143;
            long var148 = var146;
            long var150 = var148;
            long var152 = var150;
            long var154 = var152;
            long var156 = var154;
            long var158 = var156;
            long var160 = var158;
            Object var162 = var145.get((int)((int)((int)var20 & 4294967295L) & 4294967295L));
            long var163 = var160;
            long var165 = var163;
            long var167 = var165;
            boolean var169 = var162 instanceof PlacementModule.Action;
            if (var162 != null && !var169) {
               throw new ClassCastException("CHECKCAST failed");
            }

            long var172 = var167;
            long var176 = var172;
            long var178 = var176;
            long var180 = var178;
            long var182 = var180;
            long var184 = var182;
            long var186 = var184;
            long var188 = var186;
            long var190 = var188;
            Stream var192 = var17.stream();
            long var193 = var190;
            long var195 = var193;
            long var197 = var195;
            long var199 = var197;
            long var201 = var199;
            long var203 = var201;
            Set var205 = ((PlacementModule.Action)var162).actionSet;
            long var206 = var203;
            long var208 = var206;
            long var210 = var208;
            long var212 = var210;
            long var214 = var212;
            long var216 = var214;
            long var218 = var216;
            Objects.requireNonNull(var205);
            long var220 = var218;
            long var222 = var220;
            long var224 = var222;
            long var229;
            if (var226 == null) {
               long var227 = var224;
               long var231 = var227;
               long var233 = var231;
               long var235 = var233;
               long var237 = var235;
               long var239 = var237;
               long var241 = var239;
               long var243 = var241;
               long var245 = var243;
               Lookup var247 = MethodHandles.lookup();
               long var248 = var245;
               long var250 = var248;
               long var252 = var250;
               long var254 = var252;
               long var256 = var254;
               long var258 = var256;
               long var260 = var258;
               Class<?> var262 = this.getClass();
               ClassLoader var263 = var262.getClassLoader();
               MethodType var264 = MethodType.fromMethodDescriptorString("(Ljava/util/Set;)Ljava/util/function/Predicate;", var263);
               long var265 = var260;
               long var267 = var265;
               long var269 = var267;
               long var271 = var269;
               long var273 = var271;
               Class<?> var275 = this.getClass();
               ClassLoader var276 = var275.getClassLoader();
               MethodType var277 = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Z", var276);
               long var278 = var273;
               long var280 = var278;
               long var282 = var280;
               long var284 = var282;
               long var286 = var284;
               long var288 = var286;
               Class<?> var290 = this.getClass();
               ClassLoader var291 = var290.getClassLoader();
               MethodType var292 = MethodType.fromMethodDescriptorString("(Ljava/lang/Object;)Z", var291);
               long var293 = var288;
               long var295 = var293;
               long var297 = var295;
               long var299 = var297;
               long var301 = var299;
               MethodHandle var303 = var247.findVirtual(Set.class, "contains", var292);
               long var304 = var301;
               long var306 = var304;
               long var308 = var306;
               long var310 = var308;
               long var312 = var310;
               Class<?> var314 = this.getClass();
               ClassLoader var315 = var314.getClassLoader();
               MethodType var316 = MethodType.fromMethodDescriptorString("(Lorg/phobos/module/PlacementModule$Action;)Z", var315);
               long var317 = var312;
               long var319 = var317;
               long var321 = var319;
               long var323 = var321;
               Object[] var10000 = new Object[0];
               long var325 = var323;
               long var327 = var325;
               var226 = LambdaMetafactory.metafactory(var247, "test", var264, var277, var303, var316);
               long var329 = var327;
               long var331 = var329;
               long var333 = var331;
               long var335 = var333;
               var229 = var335;
            } else {
               var229 = var224;
            }

            long var337 = var229;
            long var339 = var337;
            long var341 = var339;
            long var343 = var341;
            MethodHandle var345 = var226.getTarget();
            long var346 = var343;
            long var348 = var346;
            long var350 = var348;
            long var352 = var350;
            long var354 = var352;
            long var356 = var354;
            long var358 = var356;
            long var360 = var358;
            long var362 = var360;
            long var364 = var362;
            long var368;
            if (var205 != null) {
               long var366 = var364;
               Object[] var739 = phobos$g1806e7570;
               if (phobos$g1806e7570 == null) {
                  var739 = phobos$g1806e7570 = new Object[1];
               }

               var739[0] = var205;
               var368 = var366;
            } else {
               var368 = var364;
            }

            Object[] var10001 = phobos$g1806e7570;
            if (phobos$g1806e7570 == null) {
               var10001 = phobos$g1806e7570 = new Object[1];
            }

            Object var370 = var345.invokeWithArguments(var10001);
            long var371 = var368;
            long var373 = var371;
            long var375 = var373;
            long var377 = var375;
            long var379 = var377;
            long var381 = var379;
            long var383 = var381;
            long var385 = var383;
            boolean var387 = var192.anyMatch((Predicate)var370);
            long var388 = var385;
            long var390 = var388;
            long var392 = var390;
            long var464;
            if (!var387) {
               long var394 = var392;
               long var398 = var394;
               long var400 = var398;
               long var402 = var400;
               long var404 = var402;
               long var406 = var404;
               long var408 = var406;
               long var410 = var408;
               long var412 = var410;
               long var414 = var412;
               long var416 = var414;
               long var418 = var416;
               List var420 = this.actionList;
               long var421 = var418;
               long var423 = var421;
               long var425 = var423;
               long var427 = var425;
               long var429 = var427;
               long var431 = var429;
               int var433 = var420.size();
               long var434 = var431;
               long var436 = var434;
               long var438 = var436;
               long var442;
               if ((int)((int)((int)((int)var394 & 4294967295L) + 1 & 4294967295L) & 4294967295L) < var433) {
                  long var440 = var438;
                  long var466 = var440;
                  long var468 = var466;
                  long var470 = var468;
                  long var472 = var470;
                  long var474 = var472;
                  Setting var476 = this.integerSetting2;
                  long var477 = var474;
                  long var479 = var477;
                  long var481 = var479;
                  long var483 = var481;
                  long var485 = var483;
                  long var487 = var485;
                  Object var489 = var476.getValue();
                  long var490 = var487;
                  long var492 = var490;
                  long var494 = var492;
                  boolean var496 = var489 instanceof Integer;
                  if (var489 != null && !var496) {
                     throw new ClassCastException("CHECKCAST failed");
                  }

                  long var499 = var494;
                  long var565 = var499;
                  long var567 = var565;
                  long var569 = var567;
                  long var571 = var569;
                  long var573 = var571;
                  long var575 = var573;
                  int var577 = (Integer)var489;
                  long var578 = var575;
                  long var580 = var578;
                  long var582 = var580;
                  if ((int)((int)((int)((int)((int)var394 & 4294967295L) + 1 & 4294967295L) & 4294967295L) & 4294967295L) < var577) {
                     long var584 = var582;
                     long var615 = var584;
                     List var617 = this.actionList;
                     long var618 = var615;
                     long var620 = var618;
                     long var622 = var620;
                     long var624 = var622;
                     Object var626 = var617.get((int)((int)((int)((int)((int)var394 & 4294967295L) + 1 & 4294967295L) & 4294967295L) & 4294967295L));
                     long var627 = var624;
                     boolean var629 = var626 instanceof PlacementModule.Action;
                     if (var626 != null && !var629) {
                        throw new ClassCastException("CHECKCAST failed");
                     }

                     long var632 = var627;
                     if (var626 != null) {
                        long var699 = var632;
                        long var701 = var699;
                        long var703 = var701;
                        long var705 = var703;
                        long var707 = var705;
                        boolean var709 = ((PlacementModule.Action)var162).isFlag4();
                        long var710 = var707;
                        if (!var709) {
                           long var712 = var710;
                           long var714 = var712;
                           long var716 = var714;
                           long var718 = var716;
                           long var720 = var718;
                           boolean var722 = ((PlacementModule.Action)var162).testAction((PlacementModule.Action)var626);
                           long var723 = var720;
                           if (var722) {
                              long var725 = var723;
                              long var729 = var725;
                              long var731 = var729;
                              long var733 = var731;
                              long var735 = var733;
                              ((PlacementModule.Action)var162).handleAction((PlacementModule.Action)var626);
                              long var737 = var735;
                              var442 = var737;
                           } else {
                              long var727 = var723;
                              var442 = var727;
                           }
                        } else {
                           var442 = var710;
                        }
                     } else {
                        var442 = var632;
                     }
                  } else {
                     var442 = var582;
                  }
               } else {
                  var442 = var438;
               }

               long var501 = var442;
               long var503 = var501;
               long var505 = var503;
               long var507 = var505;
               long var509 = var507;
               PhobosClient var511 = this.phobosClient;
               long var512 = var509;
               long var514 = var512;
               long var516 = var514;
               long var518 = var516;
               long var520 = var518;
               long var522 = var520;
               long var524 = var522;
               long var526 = var524;
               long var528 = var526;
               long var530 = var528;
               long var532 = var530;
               long var534 = var532;
               long var536 = var534;
               Setting var538 = this.timingSetting;
               long var539 = var536;
               long var541 = var539;
               long var543 = var541;
               long var545 = var543;
               long var547 = var545;
               long var549 = var547;
               Object var551 = var538.getValue();
               long var552 = var549;
               long var554 = var552;
               long var556 = var554;
               boolean var558 = var551 instanceof PlacementModule.Timing;
               if (var551 != null && !var558) {
                  throw new ClassCastException("CHECKCAST failed");
               }

               long var561 = var556;
               long var588 = var561;
               long var590 = var588;
               long var592 = var590;
               long var594 = var592;
               long var596 = var594;
               long var598 = var596;
               boolean var600 = ((PlacementModule.Action)var162)
                  .testPhobosClient(var511, itemSwitcher, clientPlayerEntity, clientWorld, clientPlayerInteractionManager, (PlacementModule.Timing)var551);
               long var601 = var598;
               long var603 = var601;
               long var605 = var603;
               long var607 = var605;
               long var609 = var607;
               if (var600) {
                  long var611 = var609;
                  long var634 = var611;
                  long var636 = var634;
                  long var638 = var636;
                  long var640 = var638;
                  long var642 = var640;
                  long var644 = var642;
                  long var646 = var644;
                  long var648 = var646;
                  ((PlacementModule.Action)var162).setCompleted(true & true);
                  long var650 = var648;
                  long var652 = var650;
                  long var654 = var652;
                  long var656 = var654;
                  AnimatedDouble var658 = this.animatedDouble;
                  long var659 = var656;
                  long var661 = var659;
                  long var663 = var661;
                  long var665 = var663;
                  long var667 = var665;
                  long var669 = var667;
                  long var671 = var669;
                  long var673 = var671;
                  var658.handleDouble2(1.0);
                  long var675 = var673;
                  long var740 = (int)var675 + 1 & 4294967295L;
                  var22 = var675;
                  var20 = var740;
                  continue;
               }

               long var613 = var609;
               long var677 = var613;
               long var679 = var677;
               long var681 = var679;
               long var683 = var681;
               long var685 = var683;
               long var687 = var685;
               long var689 = var687;
               long var691 = var689;
               var17.add(var162);
               long var693 = var691;
               long var695 = var693;
               var464 = var695;
            } else {
               long var396 = var392;
               long var444 = var396;
               long var446 = var444;
               long var448 = var446;
               long var450 = var448;
               long var452 = var450;
               long var454 = var452;
               long var456 = var454;
               long var458 = var456;
               var17.add(var162);
               long var460 = var458;
               long var462 = var460;
               var464 = var462;
            }

            long var741 = (int)var464 + 1 & 4294967295L;
            var22 = var464;
            var20 = var741;
         }

         List var91 = this.actionList;
         Predicate<PlacementModule.Action> var108 = PlacementModule.Action::isCompleted;
         var91.removeIf(var108);
         AntiCheatModule var109 = AntiCheatModule.getAntiCheatModule();
         Setting var110 = var109.getSneak();
         Object var111 = var110.getValue();
         boolean var112 = var111 instanceof Boolean;
         if (var111 != null && !var112) {
            throw new ClassCastException("CHECKCAST failed");
         }

         boolean var136 = (Boolean)var111;
         if (var136) {
            PhobosClient var137 = this.phobosClient;
            MovementSimulator var138 = var137.getMovementSimulator();
            var138.handleClientPlayerEntity(clientPlayerEntity);
         }
      }

      if (flag) {
         List var13 = this.actionList;
         var13.clear();
      }
   }

   public PlacementModule.Placement resolvePlacement8(Block block, BlockPos blockPos) {
      return this.resolvePlacement7(block, blockPos, null);
   }

   public PlacementModule.Placement resolvePlacement9(Block block, BlockPos blockPos) {
      return this.resolvePlacement4(block, blockPos, null);
   }

   public PlacementModule.Placement resolvePlacement10(Item item, BlockPos blockPos) {
      return this.resolvePlacement2(item, blockPos, null);
   }

   @Nullable
   public Direction resolveDirection(
      BlockPos blockPos, BlockPos blockPos2, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, @Nullable Set<PlacementModule.Action> set
   ) {
      boolean flag = AntiCheatModule.getAntiCheatModule().getSneak().getValue() || clientPlayerEntity.isSneaking();
      Direction direction2 = BlockUtils.resolveDirection2(blockPos, blockPos2);
      if (this.testBlockPos(blockPos2, clientWorld, clientPlayerEntity, this.doubleValueSetting.getValue(), flag)
         && this.getDirectionResolver().testBlockPos(blockPos2, direction2, clientWorld, clientPlayerEntity)
         && this.testClientPlayerEntity3(clientPlayerEntity, clientWorld, blockPos2, direction2)) {
         return direction2.getOpposite();
      }

      Direction direction = direction2.getOpposite();
      return this.testBlockPos2(blockPos2, direction, clientPlayerEntity, clientWorld, set) ? direction : null;
   }

   public boolean testBlockPos5(
      BlockPos blockPos, @Nullable Direction direction, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, @Nullable Set<PlacementModule.Action> set
   ) {
      Iterator var6 = this.actionList.iterator();
      if (var6.hasNext()) {
         PlacementModule.Action action = (PlacementModule.Action)var6.next();
         if (set != null) {
            set.add(action);
         }

         return true;
      } else {
         return false;
      }
   }

   @Override
   public DirectionResolver getDirectionResolver() {
      return this.booleanValueSetting7.getValue()
         ? (this.booleanValueSetting.getValue() ? ExactDirectionResolver.exactDirectionResolver : ShapeDirectionResolver.shapeDirectionResolver)
         : DefaultDirectionResolver.defaultDirectionResolver;
   }

   public void handleClientPlayerEntity5(
      ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, ClientPlayerInteractionManager clientPlayerInteractionManager
   ) {
      this.phobosClient
         .getInventoryManager()
         .handleConsumer2(itemSwitcher -> this.handleItemSwitcher(itemSwitcher, clientPlayerEntity, clientWorld, clientPlayerInteractionManager));
   }

   public boolean testBlockPos6(BlockPos blockPos, ClientWorld clientWorld, ClientPlayerEntity clientPlayerEntity) {
      if (clientWorld.getWorldBorder().contains(blockPos) && !this.testBlockPos7(blockPos, clientPlayerEntity)) {
         BlockState blockState = clientWorld.getBlockState(blockPos);
         return !blockState.isAir() && blockState.getFluidState().isEmpty();
      } else {
         return false;
      }
   }

   public Direction resolveDirection2(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, BlockPos blockPos, Item item) {
      return switch ((PlacementModule.Airplace)this.airplaceSetting.getValue()) {
         case None, Simple -> item instanceof BlockItem blockItem
            ? BlockDirectionUtils.resolveDirection(clientWorld, blockItem.getBlock(), blockPos, true, Direction.values())
            : Direction.UP;
         case Up -> Direction.UP;
         case Down -> Direction.DOWN;
         case Strict -> item instanceof BlockItem blockItem2
            ? BlockDirectionUtils.resolveDirection(
               clientWorld, blockItem2.getBlock(), blockPos, true, ExactDirectionResolver.exactDirectionResolver.values2(blockPos, clientPlayerEntity, clientWorld)
            )
            : null;
      };
   }

   @Override
   public boolean testBlockPos3(BlockPos blockPos, Direction direction, ClientWorld clientWorld, PlayerEntity playerEntity) {
      return !this.booleanValueSetting7.getValue()
         ? true
         : (this.booleanValueSetting.getValue() ? ExactDirectionResolver.exactDirectionResolver : ShapeDirectionResolver.shapeDirectionResolver)
            .testBlockPos(blockPos, direction, clientWorld, playerEntity);
   }

   public PlacementModule.Placement resolvePlacement11(
      Item item, BlockPos blockPos, @Nullable Direction direction, boolean flag, boolean flag2, @Nullable BlockPos blockPos2
   ) {
      if (mc.world == null || mc.player == null || mc.interactionManager == null) {
         return PlacementModule.Placement.ERROR;
      } else if (!this.booleanValueSetting5.getValue() && mc.player.isUsingItem()) {
         return PlacementModule.Placement.MULTITASK;
      } else if (item instanceof BlockItem blockItem && this.setSetting.getValue().contains(blockItem.getBlock())) {
         return PlacementModule.Placement.BLACKLIST;
      } else {
         for (PlacementModule.Action action : this.actionList) {
            if (action.getBlockPos2().equals(blockPos)) {
               return PlacementModule.Placement.EXISTS;
            }
         }

         if (item == null || blockPos == null) {
            return PlacementModule.Placement.ERROR;
         }

         if (this.testBlockPos7(blockPos, mc.player)) {
            return PlacementModule.Placement.RANGE;
         }

         boolean flag3 = this.alwaysTrue();
         Set<PlacementModule.Action> set = new HashSet<>();
         Direction direction2 = blockPos2 != null
            ? this.resolveDirection(blockPos, blockPos2, mc.player, mc.world, set)
            : this.resolveDirection4(blockPos, mc.world, mc.player, set);
         if (direction2 != null || flag3 && blockPos2 == null) {
            Direction direction3 = direction2 == null ? this.resolveDirection2(mc.player, mc.world, blockPos, item) : direction2;
            if (direction3 == null) {
               return PlacementModule.Placement.PLACEMENT;
            } else if (item instanceof BlockItem blockItem2 && flag && this.testBlockPos8(blockPos, blockItem2.getBlock())) {
               return PlacementModule.Placement.BLOCKED;
            } else {
               return !flag2 && !mc.world.getBlockState(blockPos).isReplaceable() ? PlacementModule.Placement.ERROR : PlacementModule.Placement.SUCCESS;
            }
         } else {
            return PlacementModule.Placement.PLACEMENT;
         }
      }
   }

   public PlacementModule.Placement resolvePlacement12(PlacementModule.Placement placement) {
      this.clear();
      return placement;
   }

   public PlacementModule.Placement resolvePlacement13(
      Item item, BlockPos blockPos, @Nullable Direction direction, boolean flag, boolean flag2, @Nullable BlockPos blockPos2
   ) {
      if (mc.world == null || mc.player == null || mc.interactionManager == null) {
         return this.resolvePlacement12(PlacementModule.Placement.ERROR);
      } else if (!this.booleanValueSetting5.getValue() && mc.player.isUsingItem()) {
         return this.resolvePlacement12(PlacementModule.Placement.MULTITASK);
      } else if (item instanceof BlockItem blockItem && this.setSetting.getValue().contains(blockItem.getBlock())) {
         return this.resolvePlacement12(PlacementModule.Placement.BLACKLIST);
      } else {
         for (PlacementModule.Action action : this.actionList) {
            if (action.getBlockPos2().equals(blockPos)) {
               return this.resolvePlacement12(PlacementModule.Placement.EXISTS);
            }
         }

         if (item == null || blockPos == null) {
            return this.resolvePlacement12(PlacementModule.Placement.ERROR);
         }

         if (this.testBlockPos7(blockPos, mc.player)) {
            return this.resolvePlacement12(PlacementModule.Placement.RANGE);
         }

         boolean flag3 = this.alwaysTrue();
         Set<PlacementModule.Action> set = new HashSet<>();
         Direction direction2 = blockPos2 != null
            ? this.resolveDirection(blockPos, blockPos2, mc.player, mc.world, set)
            : this.resolveDirection4(blockPos, mc.world, mc.player, set);
         if (direction2 != null || flag3 && blockPos2 == null) {
            boolean flag4 = flag3 && direction2 == null;
            Direction direction3 = flag4 ? this.resolveDirection2(mc.player, mc.world, blockPos, item) : direction2;
            if (direction3 == null) {
               return this.resolvePlacement12(PlacementModule.Placement.PLACEMENT);
            } else if (item instanceof BlockItem blockItem2 && flag && this.testBlockPos8(blockPos, blockItem2.getBlock())) {
               return this.resolvePlacement12(PlacementModule.Placement.BLOCKED);
            } else {
               if (!flag2 && !mc.world.getBlockState(blockPos).isReplaceable()) {
                  return this.resolvePlacement12(PlacementModule.Placement.ERROR);
               }

               PlacementModule.Action action2 = new PlacementModule.Action(
                  this,
                  blockPos,
                  flag4 ? blockPos : blockPos.offset(direction3),
                  item,
                  flag4 ? direction3 : direction3.getOpposite(),
                  direction,
                  flag3,
                  !this.rotationSetting.getValue().equals(PlacementModule.Rotation.Normal),
                  flag2,
                  this.getItemSelector(),
                  this.getBlockHitSelector(),
                  this.getRotationSequence()
               );
               action2.getActionSet().addAll(set);
               this.handleAction(action2);
               this.actionList.add(action2);
               return this.resolvePlacement12(PlacementModule.Placement.SUCCESS);
            }
         } else {
            return this.resolvePlacement12(PlacementModule.Placement.PLACEMENT);
         }
      }
   }

   public PlacementModule.ItemSelector getItemSelector() {
      return PlacementModule.ItemSelector.Impl.INSTANCE;
   }

   public Direction resolveDirection3(BlockPos blockPos, ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld) {
      return !this.booleanValueSetting7.getValue()
         ? Direction.DOWN
         : (this.booleanValueSetting.getValue() ? ExactDirectionResolver.exactDirectionResolver : ShapeDirectionResolver.shapeDirectionResolver)
            .resolveDirection(blockPos, clientPlayerEntity, clientWorld);
   }

   @Nullable
   public Direction resolveDirection4(BlockPos blockPos, ClientWorld clientWorld, ClientPlayerEntity clientPlayerEntity, @Nullable Set<PlacementModule.Action> set) {
      boolean flag = AntiCheatModule.getAntiCheatModule().getSneak().getValue() || clientPlayerEntity.isSneaking();

      for (Direction direction2 : Direction.values()) {
         BlockPos blockPos2 = blockPos.offset(direction2);
         Direction direction = direction2.getOpposite();
         if (this.testBlockPos(blockPos2, clientWorld, clientPlayerEntity, this.doubleValueSetting.getValue(), flag)
            && this.getDirectionResolver().testBlockPos(blockPos2, direction, clientWorld, clientPlayerEntity)
            && this.testClientPlayerEntity3(clientPlayerEntity, clientWorld, blockPos2, direction)) {
            return direction2;
         }
      }

      for (Direction direction3 : Direction.values()) {
         BlockPos blockPos3 = blockPos.offset(direction3);
         if (this.testBlockPos2(blockPos3, direction3, clientPlayerEntity, clientWorld, set)) {
            return direction3;
         }
      }

      return null;
   }

   @OverrideOnly
   public boolean alwaysFalse() {
      return this.booleanValueSetting8.getValue();
   }

   public boolean testBlockPos7(BlockPos blockPos, PlayerEntity playerEntity) {
      return playerEntity.getPos().squaredDistanceTo(blockPos.getX() + 0.5, blockPos.getY() + 0.5 - playerEntity.getStandingEyeHeight(), blockPos.getZ() + 0.5)
         > MathHelper.square(this.doubleValueSetting.getValue());
   }

   public boolean testBlockPos8(BlockPos blockPos, Block block) {
      return mc.world == null ? true : BlockUtils.testBlockPos11(blockPos, block.getDefaultState().getCollisionShape(mc.world, blockPos), mc.world, true);
   }

   public boolean testClientPlayerEntity3(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld, BlockPos blockPos, Direction direction) {
      return !this.booleanValueSetting6.getValue()
         ? true
         : CrystalUtils.computeClientPlayerEntity2(clientPlayerEntity, clientWorld, blockPos, RotationUtils.resolveVec3d(blockPos, direction, blockPos.toCenterPos()))
            <= 1;
   }

   public List<BlockPos> resolveList(
      BlockPos blockPos, ClientWorld clientWorld, ClientPlayerEntity clientPlayerEntity, Block block, Predicate<BlockPos> predicate
   ) {
      return this.positionPoolBuilder.resolveList2(blockPos, block, clientPlayerEntity, clientWorld, this.integerSetting3.getValue(), this);
   }

   public List<BlockPos> resolveList2(
      BlockPos blockPos, ClientWorld clientWorld, ClientPlayerEntity clientPlayerEntity, Block block, PositionPoolBuilder... positionPoolBuilders
   ) {
      return positionPoolBuilders.length == 0
         ? this.positionPoolBuilder.resolveList2(blockPos, block, clientPlayerEntity, clientWorld, this.integerSetting3.getValue(), this)
         : positionPoolBuilders[0].resolveList2(blockPos, block, clientPlayerEntity, clientWorld, this.integerSetting3.getValue(), this);
   }

   @OverrideOnly
   public boolean alwaysTrue() {
      return !this.airplaceSetting.testObject2(PlacementModule.Airplace.None);
   }

   @Override
   public String nothing() {
      return String.valueOf(Math.round(this.animatedDouble.value()));
   }

   public Setting<PlacementModule.Rotation> getRotationSetting() {
      return this.rotationSetting;
   }

   public Setting<Set<Block>> getSetSetting() {
      return this.setSetting;
   }

   public Setting<PlacementModule.Airplace> getAirplaceSetting() {
      return this.airplaceSetting;
   }

   public Setting<Integer> getIntegerSetting() {
      return this.integerSetting;
   }

   public Setting<Boolean> getBooleanValueSetting6() {
      return this.booleanValueSetting6;
   }

   public Setting<Boolean> getBooleanValueSetting() {
      return this.booleanValueSetting;
   }

   public Setting<PlacementModule.SwitchBack> getSwitchBackSetting() {
      return this.switchBackSetting;
   }

   public Setting<Silent> getSilentSetting() {
      return this.silentSetting;
   }

   public Setting<PlacementModule.Timing> getTimingSetting() {
      return this.timingSetting;
   }

   public Setting<Boolean> getBooleanValueSetting5() {
      return this.booleanValueSetting5;
   }

   public Setting<Double> getDoubleValueSetting() {
      return this.doubleValueSetting;
   }

   public PlacementModule.RotationSequence getRotationSequence() {
      return PlacementModule.RotationSequence.Impl.INSTANCE;
   }

   public PlacementModule.BlockHitSelector getBlockHitSelector() {
      return PlacementModule.BlockHitSelector.Impl.INSTANCE;
   }

   public Setting<Boolean> getBooleanValueSetting7() {
      return this.booleanValueSetting7;
   }

   public Setting<Integer> getIntegerSetting2() {
      return this.integerSetting2;
   }

   public Setting<Boolean> getBooleanValueSetting3() {
      return this.booleanValueSetting3;
   }

   public Setting<Boolean> getBooleanValueSetting4() {
      return this.booleanValueSetting4;
   }

   public Setting<Boolean> getBooleanValueSetting2() {
      return this.booleanValueSetting2;
   }

   public Setting<Integer> getIntegerSetting3() {
      return this.integerSetting3;
   }

   public Setting<Color> getColorSetting() {
      return this.colorSetting;
   }

   public Setting<Boolean> getBooleanValueSetting9() {
      return this.booleanValueSetting9;
   }

   public List<SettingGroup> getSettingGroupList2() {
      return this.settingGroupList;
   }

   public Setting<Integer> getIntegerSetting4() {
      return this.integerSetting4;
   }

   @Nullable
   public Consumer<PlacementModule.Action> getActionConsumer() {
      return this.actionConsumer;
   }

   public Normal getNormal() {
      return this.normal;
   }

   public Setting<Float> getFloatValueSetting() {
      return this.floatValueSetting;
   }

   public AtomicInteger getAtomicInteger() {
      return this.atomicInteger;
   }

   public PositionPoolBuilder getPositionPoolBuilder() {
      return this.positionPoolBuilder;
   }

   @Override
   public List<PlacementModule.Action> getActionList() {
      return this.actionList;
   }

   public AnimatedDouble getAnimatedDouble() {
      return this.animatedDouble;
   }

   public Setting<Long> getLongValueSetting() {
      return this.longValueSetting;
   }

   public org.phobos.api.TimeGate.Normal getNormal2() {
      return this.normal2;
   }

   public Setting<Boolean> getBooleanValueSetting8() {
      return this.booleanValueSetting8;
   }

   public Setting<Color> getColorSetting2() {
      return this.colorSetting2;
   }

   public static class Action {
      public boolean flag;
      @NotNull
      public final BlockPos blockPos;
      public final Set<PlacementModule.Action> actionSet = new HashSet<>();
      @Nullable
      public final Direction direction;
      public boolean flag2;
      public final boolean flag3;
      @NotNull
      public final Direction direction2;
      @Nullable
      public Runnable runnable;
      @Nullable
      public Direction direction3;
      public final PlacementModule.RotationSequence rotationSequence;
      public boolean flag4;
      public final boolean flag5;
      public final PlacementModule.BlockHitSelector blockHitSelector;
      public boolean flag6;
      public final boolean flag7;
      public boolean completed;
      @NotNull
      public final PlacementModule placementModule;
      @NotNull
      public final BlockPos blockPos2;
      @Nullable
      public PlacementModule.Action action;
      @NotNull
      public final Item item;
      public final PlacementModule.ItemSelector itemSelector;

      public void handleClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, Runnable runnable) {
         if (this.direction == null) {
            runnable.run();
         } else {
            float f = clientPlayerEntity.getYaw();
            float f2 = clientPlayerEntity.getPitch();
            float[] floats = new float[]{this.placementModule.computeDirection2(this.direction), this.placementModule.computeDirection(this.direction)};
            clientPlayerEntity.setYaw(floats[0]);
            clientPlayerEntity.setPitch(floats[1]);
            runnable.run();
            clientPlayerEntity.setYaw(f);
            clientPlayerEntity.setPitch(f2);
         }
      }

      public boolean isFlag3() {
         return this.flag3;
      }

      public boolean isFlag5() {
         return this.flag5;
      }

      public boolean isCompleted() {
         return this.completed;
      }

      @Nullable
      public Runnable getRunnable() {
         return this.runnable;
      }

      public Set<PlacementModule.Action> getActionSet() {
         return this.actionSet;
      }

      public boolean isFlag2() {
         return this.flag2;
      }

      public void handleAction(PlacementModule.Action action) {
         this.setFlag6(true);
         this.setAction(action);
         this.setDirection3(BlockUtils.resolveDirection(this.blockPos2, action.blockPos2));
         action.setFlag4(true);
      }

      public PlacementModule.ItemSelector getItemSelector() {
         return this.itemSelector;
      }

      public boolean testAction(PlacementModule.Action action) {
         return BlockUtils.resolveDirection(this.blockPos2, action.blockPos2) != null;
      }

      public PlacementModule.BlockHitSelector getBlockHitSelector() {
         return this.blockHitSelector;
      }

      public boolean isFlag7() {
         return this.flag7;
      }

      @NotNull
      public BlockPos getBlockPos() {
         return this.blockPos;
      }

      @NotNull
      public BlockPos getBlockPos2() {
         return this.blockPos2;
      }

      public boolean testPhobosClient(
         PhobosClient phobosClient,
         ItemSwitcher itemSwitcher,
         ClientPlayerEntity clientPlayerEntity,
         ClientWorld clientWorld,
         ClientPlayerInteractionManager clientPlayerInteractionManager,
         PlacementModule.Timing timing
      ) {
         PlayerEntity playerEntity = phobosClient.getMovementSimulator().resolvePlayerEntity(clientPlayerEntity);
         if (!this.placementModule.testBlockPos4(this.blockPos, playerEntity, this.placementModule.doubleValueSetting.getValue())) {
            return false;
         }

         if (!this.flag7 && !clientWorld.getBlockState(this.blockPos2).isReplaceable()) {
            return false;
         }

         BlockHitResult blockHitResult = this.blockHitSelector
            .createBlockHitResult(playerEntity, clientPlayerEntity, clientWorld, this.blockPos2, this.blockPos, this.direction2);
         PlayerEntity playerEntity2 = phobosClient.getMovementSimulator().resolvePlayerEntity(clientPlayerEntity);
         PlacementModule.Rotation rotation = this.placementModule.rotationSetting.getValue();
         if (!rotation.equals(PlacementModule.Rotation.None)) {
            MutableObject<BlockHitResult> mutableObject = new MutableObject<>(blockHitResult);
            if (!this.flag2) {
               PlacementModule.RotationSequence.Result result = this.rotationSequence
                  .resolveResult(
                     this.placementModule,
                     this,
                     phobosClient,
                     this.actionSet,
                     rotation,
                     timing,
                     playerEntity,
                     clientPlayerEntity,
                     clientWorld,
                     this.blockPos2,
                     this.blockPos,
                     this.direction2,
                     this.direction,
                     this.flag3,
                     mutableObject
                  );
               switch (result) {
                  case SUCCESS_ROTATED:
                     if (this.isFlag6() && this.getAction() != null) {
                        this.getAction().setFlag2(true);
                     }

                     this.setFlag2(true);
                     if (timing.equals(PlacementModule.Timing.Sequential)) {
                        return false;
                     }
                     break;
                  case FAIL_BUSY:
                     return false;
               }
            }

            blockHitResult = mutableObject.getValue();
         } else if (!this.flag3 && !this.placementModule.testBlockPos3(this.blockPos, this.direction2, clientWorld, playerEntity)) {
            return false;
         }

         if (this.direction != null) {
            float[] floats = new float[]{this.placementModule.computeDirection2(this.direction), this.placementModule.computeDirection(this.direction)};
            switch (rotation) {
               case None:
               case Normal:
               case Instant:
                  PlayerActionUtils.handleClientPlayerEntity17(clientPlayerEntity, floats[0], floats[1], playerEntity2.isOnGround());
                  break;
               case Silent:
               case Hybrid:
                  phobosClient.getRotationManager().handleClientPlayerEntity3(clientPlayerEntity, floats[0], floats[1]);
            }
         }

         SwitchResult switchResult = this.itemSelector.resolveSwitchResult(this.placementModule, itemSwitcher, this.item);
         if (switchResult == null) {
            return false;
         }

         boolean flag = this.flag3 && this.placementModule.booleanValueSetting.getValue();
         Hand hand = flag ? Hand.OFF_HAND : switchResult.hand();
         if (this.item instanceof BlockItem blockItem) {
            BlockUtils.testBlockPos6(
               this.blockPos2,
               blockItem.getBlock().getDefaultState().getCollisionShape(MinecraftHolder.mc.world, this.blockPos2),
               MinecraftHolder.mc.world,
               this.placementModule.booleanValueSetting2.getValue()
            );
         }

         BlockHitResult blockHitResult2 = blockHitResult;
         MutableObject<BlockState> mutableObject2 = new MutableObject<>(null);
         ItemPlacementContext itemPlacementContext2 = new ItemPlacementContext(playerEntity2, hand, switchResult.slot().getStack(), blockHitResult2);
         BlockState blockState2 = null;
         if (this.item instanceof BlockItem blockItem2) {
            Block block = blockItem2.getBlock();
            blockState2 = block.getDefaultState();
            ItemPlacementContext itemPlacementContext = blockItem2.getPlacementContext(itemPlacementContext2);
            itemPlacementContext2 = itemPlacementContext == null ? itemPlacementContext2 : itemPlacementContext;
            BlockState blockState = block.getPlacementState(itemPlacementContext2);
            blockState2 = blockState == null ? blockState2 : blockState;
         }

         if (this.item instanceof FluidModificationItem fluidModificationItem) {
            mutableObject2.setValue(Blocks.STONE.getDefaultState());
            if (fluidModificationItem instanceof BucketItem bucketItem) {
               IBucketItem iBucketItem = (IBucketItem)(Object)bucketItem;
               iBucketItem.addPlacement(blockHitResult2);
            }

            if (this.placementModule.booleanValueSetting3.getValue()) {
               TickUtils.handleClientWorld(
                  clientWorld,
                  integer -> clientPlayerEntity.networkHandler
                     .sendPacket(new PlayerInteractItemC2SPacket(hand, integer, playerEntity2.getYaw(), playerEntity2.getPitch()))
               );
               if (!clientPlayerEntity.isCreative()) {
                  clientPlayerEntity.getStackInHand(switchResult.hand()).decrement(1);
               }
            } else {
               clientPlayerInteractionManager.interactItem(clientPlayerEntity, hand);
            }

            if (fluidModificationItem instanceof BucketItem bucketItem2) {
               IBucketItem iBucketItem2 = (IBucketItem)(Object)bucketItem2;
               iBucketItem2.addPlacement(null);
            }
         } else {
            mutableObject2.setValue(Objects.requireNonNullElse(blockState2, Blocks.STONE.getDefaultState()));
            if (flag) {
               clientPlayerEntity.networkHandler
                  .sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.SWAP_ITEM_WITH_OFFHAND, BlockPos.ORIGIN, Direction.DOWN));
            }

            if (this.placementModule.booleanValueSetting3.getValue()) {
               TickUtils.handleClientWorld(
                  clientWorld, integer -> clientPlayerEntity.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(hand, blockHitResult2, integer))
               );
               if (!clientPlayerEntity.isCreative()) {
                  clientPlayerEntity.getStackInHand(switchResult.hand()).decrement(1);
               }
            } else {
               this.handleClientPlayerEntity(clientPlayerEntity, () -> clientPlayerInteractionManager.interactBlock(clientPlayerEntity, hand, blockHitResult2));
               if (this.placementModule.check6()) {
                  Highlights.handleBox(new Box(this.blockPos), Color.RED, 500L);
                  Highlights.handleBox(BoxUtils.createBox8(this.blockPos, this.direction2), Color.BLUE, 500L);
                  Highlights.handleBox(BoxUtils.createBox3(blockHitResult2.getPos()).expand(0.05), Color.WHITE, 500L);
               }

               if (flag && blockState2 != null) {
                  clientWorld.setBlockState(this.blockPos2, blockState2, 11);
               }
            }
         }

         if (this.placementModule.booleanValueSetting4.getValue()) {
            clientPlayerEntity.swingHand(switchResult.hand());
         }

         if (flag) {
            clientPlayerEntity.networkHandler.sendPacket(new HandSwingC2SPacket(Hand.OFF_HAND));
            clientPlayerEntity.networkHandler
               .sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.SWAP_ITEM_WITH_OFFHAND, BlockPos.ORIGIN, Direction.DOWN));
         }

         if (rotation.equals(PlacementModule.Rotation.Silent)) {
            phobosClient.getRotationManager().handleClientPlayerEntity5(clientPlayerEntity);
         }

         BlockState blockState3 = mutableObject2.getValue();
         VoxelShape voxelShape;
         if (this.placementModule.alwaysFalse() && blockState3 != null && !(voxelShape = blockState3.getOutlineShape(clientWorld, this.blockPos2)).isEmpty()) {
            Box box = voxelShape.getBoundingBox().offset(this.blockPos2);
            Render3d.getCuboidQueue().removeIf(cuboid -> cuboid.getBox().equals(box));
            Render3d.handleCuboid(
               Cuboid.createBuilder()
                  .resolveBuilder11(box)
                  .resolveBuilder18(this.placementModule.colorSetting2.getValue(), this.placementModule.colorSetting.getValue())
                  .resolveBuilder12(this.placementModule.floatValueSetting.getValue())
                  .resolveBuilder5(this.placementModule.longValueSetting.getValue())
                  .getCuboid()
            );
         }

         if (this.runnable != null) {
            this.runnable.run();
         }

         return true;
      }

      @Nullable
      public Direction getDirection() {
         return this.direction;
      }

      @NotNull
      public Direction getDirection2() {
         return this.direction2;
      }

      @NotNull
      public Item getItem() {
         return this.item;
      }

      @NotNull
      public PlacementModule getPlacementModule() {
         return this.placementModule;
      }

      public void setAction(@Nullable PlacementModule.Action action) {
         this.action = action;
      }

      public boolean isFlag6() {
         return this.flag6;
      }

      public boolean isFlag() {
         return this.flag;
      }

      @Nullable
      public Direction getDirection3() {
         return this.direction3;
      }

      public void setFlag(boolean flag) {
         this.flag = flag;
      }

      public void setFlag4(boolean flag) {
         this.flag4 = flag;
      }

      public void setDirection3(@Nullable Direction direction) {
         this.direction3 = direction;
      }

      @Nullable
      public PlacementModule.Action getAction() {
         return this.action;
      }

      public void setFlag2(boolean flag) {
         this.flag2 = flag;
      }

      public PlacementModule.RotationSequence getRotationSequence() {
         return this.rotationSequence;
      }

      public boolean isFlag4() {
         return this.flag4;
      }

      public Action(
         @NotNull PlacementModule placementModule,
         @NotNull BlockPos blockPos,
         @NotNull BlockPos blockPos2,
         @NotNull Item item,
         @NotNull Direction direction,
         @Nullable Direction direction2,
         boolean flag,
         boolean flag2,
         boolean flag3,
         PlacementModule.ItemSelector itemSelector,
         PlacementModule.BlockHitSelector blockHitSelector,
         PlacementModule.RotationSequence rotationSequence
      ) {
         if (placementModule == null) {
            throw new NullPointerException("module is marked non-null but is null");
         }

         if (blockPos == null) {
            throw new NullPointerException("pos is marked non-null but is null");
         }

         if (blockPos2 == null) {
            throw new NullPointerException("placeOn is marked non-null but is null");
         }

         if (item == null) {
            throw new NullPointerException("item is marked non-null but is null");
         }

         if (direction == null) {
            throw new NullPointerException("direction is marked non-null but is null");
         }

         this.placementModule = placementModule;
         this.blockPos2 = blockPos;
         this.blockPos = blockPos2;
         this.item = item;
         this.direction2 = direction;
         this.direction = direction2;
         this.flag3 = flag;
         this.flag5 = flag2;
         this.flag7 = flag3;
         this.itemSelector = itemSelector;
         this.blockHitSelector = blockHitSelector;
         this.rotationSequence = rotationSequence;
      }

      public void setRunnable(@Nullable Runnable runnable) {
         this.runnable = runnable;
      }

      public void setCompleted(boolean flag) {
         this.completed = flag;
      }

      public void setFlag6(boolean flag) {
         this.flag6 = flag;
      }
   }

   public enum Airplace {
      None,
      Up,
      Down,
      Simple,
      Strict;
   }

   public interface BlockHitSelector {
      BlockHitResult createBlockHitResult(PlayerEntity var1, ClientPlayerEntity var2, ClientWorld var3, BlockPos var4, BlockPos var5, Direction var6);

      class Impl implements PlacementModule.BlockHitSelector {
         public static final PlacementModule.BlockHitSelector.Impl INSTANCE = new PlacementModule.BlockHitSelector.Impl();

         @Override
         public BlockHitResult createBlockHitResult(
            PlayerEntity playerEntity,
            ClientPlayerEntity clientPlayerEntity,
            ClientWorld clientWorld,
            BlockPos blockPos,
            BlockPos blockPos2,
            Direction direction
         ) {
            Vec3d vec3d = blockPos2.toCenterPos().add(new Vec3d(direction.getUnitVector()).multiply(0.5));
            return new BlockHitResult(vec3d, direction, blockPos2, false);
         }
      }
   }

   public interface ItemSelector {
      SwitchResult resolveSwitchResult(PlacementModule var1, ItemSwitcher var2, Item var3);

      class Impl implements PlacementModule.ItemSelector {
         public static final PlacementModule.ItemSelector.Impl INSTANCE = new PlacementModule.ItemSelector.Impl();

         @Override
         public SwitchResult resolveSwitchResult(PlacementModule placementModule, ItemSwitcher itemSwitcher, Item item) {
            return switch ((PlacementModule.SwitchBack)placementModule.switchBackSetting.getValue()) {
               case Silent -> itemSwitcher.resolveSwitchResult3(Hand.MAIN_HAND, item, placementModule.silentSetting.getValue().getFlag() | 1);
               case Spoof -> {
                  Slot slot = itemSwitcher.resolveObject(slotx -> slotx.getStack().isOf(item) ? slotx : null);
                  if (slot == null) {
                     yield null;
                  } else {
                     SilentHotbarModule.getSilentHotbarManager().handleObject3(this, slot, placementModule.integerSetting4.getValue());
                     yield SwitchResult.resolveSwitchResult(slot);
                  }
               }
               case None -> itemSwitcher.resolveSwitchResult3(Hand.MAIN_HAND, item, placementModule.silentSetting.getValue().getFlag());
            };
         }
      }
   }

   public enum Placement {
      ERROR,
      EXISTS(true),
      BLOCKED,
      MULTITASK,
      BLACKLIST,
      RANGE,
      PLACEMENT,
      SUCCESS(true);

      public final boolean result;

      Placement() {
         this.result = false;
      }

      public boolean isSuccessful() {
         return this.result;
      }

      public boolean isFailed() {
         return !this.result;
      }

      Placement(boolean result) {
         this.result = result;
      }
   }

   public enum Rotation implements TextProvider {
      None("No rotation"),
      Normal("Normal rotations"),
      Instant("Instant packet rotations"),
      Silent("Grim/On-tick rotation"),
      Hybrid("Single silent rotation for NCP + Grim");

      public final String text;

      @Override
      public String getText() {
         return this.text;
      }

      Rotation(final String description) {
         this.text = description;
      }
   }

   public interface RotationSequence {
      PlacementModule.RotationSequence.Result resolveResult(
         PlacementModule var1,
         PlacementModule.Action var2,
         PhobosClient var3,
         Set<PlacementModule.Action> var4,
         PlacementModule.Rotation var5,
         PlacementModule.Timing var6,
         PlayerEntity var7,
         ClientPlayerEntity var8,
         ClientWorld var9,
         BlockPos var10,
         BlockPos var11,
         Direction var12,
         Direction var13,
         boolean var14,
         MutableObject<BlockHitResult> var15
      );

      class Impl implements PlacementModule.RotationSequence {
         public static final PlacementModule.RotationSequence.Impl INSTANCE = new PlacementModule.RotationSequence.Impl();

         @Override
         public PlacementModule.RotationSequence.Result resolveResult(
            PlacementModule placementModule,
            PlacementModule.Action action,
            PhobosClient phobosClient,
            Set<PlacementModule.Action> set,
            PlacementModule.Rotation rotation,
            PlacementModule.Timing timing,
            PlayerEntity playerEntity,
            ClientPlayerEntity clientPlayerEntity,
            ClientWorld clientWorld,
            BlockPos blockPos,
            BlockPos blockPos2,
            Direction direction,
            Direction direction2,
            boolean flag,
            MutableObject<BlockHitResult> mutableObject
         ) {
            BlockHitResult blockHitResult3 = direction2 != null
               ? (
                  placementModule.testBlockPos3(blockPos2, direction, clientWorld, playerEntity)
                     ? RaycastUtils.resolveBlockHitResult(playerEntity, clientWorld, blockPos2, direction, true)
                     : null
               )
               : RaycastUtils.resolveBlockHitResult6(
                  playerEntity,
                  clientWorld,
                  blockPos,
                  (blockPosx, directionx) -> placementModule.testBlockPos3(blockPosx, directionx, clientWorld, playerEntity),
                  true
               );
            if (RotationManager.isFlag() && timing.equals(PlacementModule.Timing.Vanilla)) {
               float f = phobosClient.getRotationManager().getFloatValue8();
               float f2 = phobosClient.getRotationManager().getFloatValue3();
               BlockHitResult blockHitResult = RaycastUtils.resolveBlockHitResult7(
                  playerEntity,
                  new float[]{f, f2},
                  clientWorld,
                  blockPos,
                  (blockPosx, directionx) -> placementModule.testBlockPos3(blockPosx, directionx, clientWorld, playerEntity),
                  true
               );
               if (blockHitResult != null && blockHitResult.getBlockPos().offset(blockHitResult.getSide()).equals(blockPos)) {
                  return PlacementModule.RotationSequence.Result.SUCCESS_LOOKING;
               }
            }

            if (blockHitResult3 != null && blockHitResult3.getBlockPos().offset(blockHitResult3.getSide()).equals(blockPos)) {
               return PlacementModule.RotationSequence.Result.SUCCESS_LOOKING;
            }

            BlockHitResult blockHitResult2 = new BlockHitResult(RotationUtils.createVec3d2(blockPos2, clientWorld, direction), direction, blockPos2, false);
            float[] floats = action.isFlag6()
               ? RotationUtils.values4(playerEntity, clientWorld, action.getAction().getBlockPos2(), action.getDirection3())
               : RotationUtils.values4(playerEntity, clientWorld, blockPos2, direction);
            switch (rotation) {
               case Normal:
                  if ((set.isEmpty() || set.stream().allMatch(PlacementModule.Action::isCompleted))
                     && !RotationManager.isFlag()
                     && phobosClient.getRotationManager().isFlag2()) {
                     phobosClient.getRotationManager().testFloat(floats[0], floats[1]);
                     mutableObject.setValue(blockHitResult2);
                     return PlacementModule.RotationSequence.Result.SUCCESS_ROTATED;
                  } else {
                     return PlacementModule.RotationSequence.Result.FAIL_BUSY;
                  }
               case Instant:
                  clientPlayerEntity.networkHandler.sendPacket(new LookAndOnGround(floats[0], floats[1], playerEntity.isOnGround()));
                  mutableObject.setValue(blockHitResult2);
                  return PlacementModule.RotationSequence.Result.SUCCESS_ROTATED;
               case Silent:
               case Hybrid:
                  phobosClient.getRotationManager().handleClientPlayerEntity3(clientPlayerEntity, floats[0], floats[1]);
                  mutableObject.setValue(blockHitResult2);
                  return PlacementModule.RotationSequence.Result.SUCCESS_ROTATED;
               default:
                  return PlacementModule.RotationSequence.Result.SUCCESS_NOT_SUPPORTED;
            }
         }
      }

      enum Result {
         SUCCESS_NOT_SUPPORTED,
         SUCCESS_LOOKING,
         SUCCESS_ROTATED,
         FAIL_BUSY;
      }
   }

   public enum SwitchBack implements TextProvider {
      Silent("Silent switch back"),
      Spoof("Uses silent hotbar"),
      None("No switch back");

      public final String text;

      SwitchBack(final String description) {
         this.text = description;
      }

      @Override
      public String getText() {
         return this.text;
      }
   }

   public enum Timing {
      Vanilla,
      Sequential;
   }
}
