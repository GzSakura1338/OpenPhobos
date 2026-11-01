package org.phobos.module.combat;

import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MaceItem;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.SwordItem;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Hand;
import org.apache.commons.lang3.mutable.MutableObject;
import org.earthhack.ducks.IInventoryScreen;
import org.earthhack.ducks.ISlot;
import org.phobos.api.TextProvider;
import org.phobos.api.Visibility;
import org.phobos.api.KeyValue;
import org.phobos.api.DelayedTask;
import org.phobos.api.NumberRange;
import org.phobos.api.TimeGate.Normal;
import org.phobos.api.TimeGate.Threaded;
import org.phobos.api.NumberRange.Mutable;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.MutableNumberSetting;
import org.phobos.core.setting.MutableLongSetting;
import org.phobos.core.setting.MultiSelectSetting;
import org.phobos.core.setting.BindSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.LongNumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.math.FloatPair;
import org.phobos.core.inventory.ClickScheduler;
import org.phobos.core.tick.TickCounter;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.core.setting.SettingGroup;
import org.phobos.core.bind.KeyCombination;
import org.phobos.core.PhobosClient;
import org.phobos.core.inventory.ActionTimer;
import org.phobos.core.module.Categories;
import org.phobos.core.math.MathUtils;
import org.phobos.core.player.PlayerActionUtils;
import org.phobos.core.entity.EntityUtils;
import org.phobos.event.Listener;
import org.phobos.event.OnDeathScreenEvent;
import org.phobos.event.SafeListener;
import org.phobos.module.Module;
import org.phobos.module.player.SuicideModule;

public class AutoTotemModule extends Module {
   public Setting<AutoTotemModule.Offhand> offhandSetting;
   public Setting<Long> longValueSetting;
   public Setting<Mutable<Long>> mutableSetting;
   public Setting<Float> floatValueSetting;
   public Setting<Boolean> booleanValueSetting;
   public static AutoTotemModule autoTotemModule;
   public Setting<Mutable<Long>> mutableSetting2;
   public Setting<Mutable<Long>> mutableSetting3;
   public Setting<Boolean> booleanValueSetting2;
   public Setting<Boolean> booleanValueSetting3;
   public Setting<AutoTotemModule.Offhand> offhandSetting2;
   public Setting<Map<AutoTotemModule.ItemType, Boolean>> mapSetting;
   public Setting<AutoTotemModule.Behavior> behaviorSetting;
   public Setting<AutoTotemModule.Mode> modeSetting;
   public Setting<Boolean> booleanValueSetting4;
   public Setting<Integer> integerSetting;
   public Setting<Long> longValueSetting2;
   public Setting<Float> floatValueSetting2;
   public Setting<Long> longValueSetting3;
   public Threaded threaded;
   public Normal normal;
   public Setting<Boolean> booleanValueSetting5;
   public Setting<Boolean> booleanValueSetting6;
   public Setting<Mutable<Long>> mutableSetting4;
   public Setting<Mutable<Float>> mutableSetting5;
   public Setting<Mutable<Long>> mutableSetting6;
   public Threaded threaded2;
   public Threaded threaded3;
   public Setting<Boolean> booleanValueSetting7;
   public int intValue;
   public Setting<KeyCombination> keyCombinationSetting;
   private static Object[] phobos$g1806d6b00;
   private static Object[] phobos$g1806d70d8;

   public static boolean check() {
      return !autoTotemModule.normal.testNumber(autoTotemModule.longValueSetting3.getValue()) && autoTotemModule.booleanValueSetting6.getValue();
   }

   public boolean check2() {
      return autoTotemModule.modeSetting.testObject2(AutoTotemModule.Mode.Legit);
   }

   public AutoTotemModule(PhobosClient phobosClient) {
      super(phobosClient, "AutoTotem", Categories.combat, "Automatically puts a totem or a specified item if your offhand");
      this.nativeInit(phobosClient);
   }

   public void nativeInit(final PhobosClient var1) {
      BooleanSetting var2 = new BooleanSetting("Verbose");
      Setting var3 = this.resolveSetting2(var2);
      this.booleanValueSetting4 = var3;
      AutoTotemModule.Behavior var5 = AutoTotemModule.Behavior.Default;
      EnumSetting var4 = new EnumSetting<>("Type", var5);
      Setting var6 = this.resolveSetting2(var4);
      this.behaviorSetting = var6;
      AutoTotemModule.Mode var8 = AutoTotemModule.Mode.Normal;
      EnumSetting var7 = new EnumSetting<>("Mode", var8);
      Setting var9 = this.resolveSetting2(var7);
      this.modeSetting = var9;
      LongNumberSetting var10 = new LongNumberSetting("Delay", 0L, 0L, 500L, true & true);
      Visibility var27 = this::check5;
      Setting var28 = var10.resolveSetting3(var27);
      Setting var29 = this.resolveSetting2(var28);
      this.longValueSetting = var29;
      BooleanSetting var30 = new BooleanSetting("DoubleHand");
      Setting var31 = this.resolveSetting2(var30);
      this.booleanValueSetting3 = var31;
      Integer var33 = 1;
      Integer var34 = 1;
      Integer var35 = 9;
      NumberSetting var32 = new NumberSetting<>("DoubleHandSlot", var33, var34, var35);
      Setting var36 = this.resolveSetting2(var32);
      this.integerSetting = var36;
      MutableLongSetting var37 = new MutableLongSetting("DoubleHandDelay", 0L, 0L, 250L);
      Setting var38 = this.resolveSetting2(var37);
      this.mutableSetting3 = var38;
      MutableLongSetting var39 = new MutableLongSetting("Global", 0L, 0L, 500L);
      Setting var40 = var39.resolveSetting7("Mutual delay, reset on both offhand and mainhand clicks");
      Setting var41 = this.modeSetting;
      AutoTotemModule.Mode var42 = AutoTotemModule.Mode.Legit;
      Setting var43 = var40.resolveSetting4(var41, var42);
      Setting var44 = this.booleanValueSetting3;
      Setting var45 = var43.resolveSetting(var44);
      Setting var46 = this.resolveSetting2(var45);
      this.mutableSetting2 = var46;
      MutableLongSetting var47 = new MutableLongSetting("Interval", 0L, 0L, 500L);
      Setting var48 = var47.resolveSetting7("Offhand click delay");
      Setting var49 = this.modeSetting;
      AutoTotemModule.Mode var50 = AutoTotemModule.Mode.Legit;
      Setting var51 = var48.resolveSetting4(var49, var50);
      Setting var52 = this.resolveSetting2(var51);
      this.mutableSetting = var52;
      BooleanSetting var53 = new BooleanSetting("Interrupt");
      Setting var54 = this.modeSetting;
      AutoTotemModule.Mode[] var55 = new AutoTotemModule.Mode[2];
      AutoTotemModule.Mode var56 = AutoTotemModule.Mode.Normal;
      var55[0] = var56;
      AutoTotemModule.Mode var57 = AutoTotemModule.Mode.Strict;
      var55[1] = var57;
      Setting var58 = var53.resolveSetting8(var54, (Object[])var55);
      Setting var59 = var58.resolveSetting7("For UNCP, makes you stop eating when toteming");
      Setting var60 = this.resolveSetting2(var59);
      this.booleanValueSetting2 = var60;
      AutoTotemModule.Offhand var62 = AutoTotemModule.Offhand.None;
      EnumSetting var61 = new EnumSetting<>("Offhand", var62);
      Setting var63 = this.modeSetting;
      AutoTotemModule.Mode[] var64 = new AutoTotemModule.Mode[1];
      AutoTotemModule.Mode var65 = AutoTotemModule.Mode.Legit;
      var64[0] = var65;
      Visibility var66 = Visibility.resolveVisibility3(var63, var64);
      Visibility var67 = var66.getVisibility2();
      Setting var68 = var61.resolveSetting2(var67, true & true);
      Setting var69 = this.resolveSetting2(var68);
      this.offhandSetting = var69;
      AutoTotemModule.Offhand var71 = AutoTotemModule.Offhand.Crystals;
      EnumSetting var70 = new EnumSetting<>("Fallback", var71);
      Setting var72 = this.modeSetting;
      AutoTotemModule.Mode[] var73 = new AutoTotemModule.Mode[1];
      AutoTotemModule.Mode var74 = AutoTotemModule.Mode.Legit;
      var73[0] = var74;
      Visibility var75 = Visibility.resolveVisibility3(var72, var73);
      Visibility var76 = var75.getVisibility2();
      Setting var77 = var70.resolveSetting2(var76, true & true);
      Setting var78 = this.resolveSetting2(var77);
      this.offhandSetting2 = var78;
      Boolean var80 = false & true;
      BooleanSetting var79 = new BooleanSetting("Action", var80);
      Setting var81 = this.resolveSetting2(var79);
      this.booleanValueSetting = var81;
      MultiSelectSetting var82 = new MultiSelectSetting<>("ActionTypes", AutoTotemModule.ItemType.class);
      Consumer<MultiSelectSetting> var99 = AutoTotemModule::handleMultiSelectSetting2;
      MultiSelectSetting var100 = var82.resolveMultiSelectSetting5(var99);
      Setting var101 = this.resolveSetting2(var100);
      this.mapSetting = var101;
      Float var103 = (float)5.0F;
      Float var104 = (float)0.1F;
      Float var105 = (float)36.0F;
      NumberSetting var102 = new NumberSetting<>("ActionHealth", var103, var104, var105);
      Setting var106 = this.behaviorSetting;
      AutoTotemModule.Behavior[] var107 = new AutoTotemModule.Behavior[1];
      AutoTotemModule.Behavior var108 = AutoTotemModule.Behavior.Default;
      var107[0] = var108;
      Visibility var109 = Visibility.resolveVisibility3(var106, var107);
      Setting var110 = var102.resolveSetting3(var109);
      Setting var111 = this.resolveSetting2(var110);
      this.floatValueSetting = var111;
      Float var113 = (float)14.0F;
      Float var114 = (float)0.0F;
      Float var115 = (float)36.0F;
      NumberSetting var112 = new NumberSetting<>("Health", var113, var114, var115);
      Setting var116 = this.modeSetting;
      AutoTotemModule.Mode[] var117 = new AutoTotemModule.Mode[1];
      AutoTotemModule.Mode var118 = AutoTotemModule.Mode.Legit;
      var117[0] = var118;
      Visibility var119 = Visibility.resolveVisibility3(var116, var117);
      Visibility var120 = var119.getVisibility2();
      Setting var121 = this.behaviorSetting;
      AutoTotemModule.Behavior[] var122 = new AutoTotemModule.Behavior[1];
      AutoTotemModule.Behavior var123 = AutoTotemModule.Behavior.Default;
      var122[0] = var123;
      Visibility var124 = Visibility.resolveVisibility3(var121, var122);
      Visibility var125 = var120.resolveVisibility5(var124);
      Setting var126 = var112.resolveSetting2(var125, true & true);
      Setting var127 = this.resolveSetting2(var126);
      this.floatValueSetting2 = var127;
      LongNumberSetting var128 = new LongNumberSetting("Timeout", 2000L, 500L, 15000L, true & true);
      Setting var129 = this.resolveSetting2(var128);
      boolean var130 = var129 instanceof LongNumberSetting;
      if (var129 != null && !var130) {
         throw new ClassCastException("CHECKCAST failed");
      }

      Setting var131 = this.modeSetting;
      AutoTotemModule.Mode[] var132 = new AutoTotemModule.Mode[1];
      AutoTotemModule.Mode var133 = AutoTotemModule.Mode.Legit;
      var132[0] = var133;
      Visibility var134 = Visibility.resolveVisibility3(var131, var132);
      Setting var135 = ((LongNumberSetting)var129).resolveSetting3(var134);
      this.longValueSetting2 = var135;
      MutableLongSetting var136 = new MutableLongSetting("End", 0L, 0L, 1500L, true & true);
      Setting var137 = var136.resolveSetting7("Ticks to wait before closing inventory");
      Setting var138 = this.modeSetting;
      AutoTotemModule.Mode var139 = AutoTotemModule.Mode.Legit;
      Setting var140 = var137.resolveSetting4(var138, var139);
      Setting var141 = this.resolveSetting2(var140);
      this.mutableSetting4 = var141;
      BooleanSetting var142 = new BooleanSetting("Manual");
      Setting var143 = var142.resolveSetting7("Does not open or close automatically, use with caution");
      Setting var144 = this.modeSetting;
      AutoTotemModule.Mode var145 = AutoTotemModule.Mode.Legit;
      Setting var146 = var143.resolveSetting4(var144, var145);
      Setting var147 = this.resolveSetting2(var146);
      this.booleanValueSetting7 = var147;
      Float var149 = (float)50.0F;
      Float var150 = (float)150.0F;
      Mutable var151 = NumberRange.createMutable(var149, var150);
      Float var152 = (float)10.0F;
      Float var153 = (float)200.0F;
      String[] var154 = new String[0];
      MutableNumberSetting var148 = new MutableNumberSetting<>("Speed", var151, var152, var153, var154);
      Setting var155 = var148.resolveSetting7("Aim speed");
      Setting var156 = this.modeSetting;
      AutoTotemModule.Mode var157 = AutoTotemModule.Mode.Legit;
      Setting var158 = var155.resolveSetting4(var156, var157);
      Setting var159 = this.resolveSetting2(var158);
      this.mutableSetting5 = var159;
      MutableLongSetting var160 = new MutableLongSetting("Await", 0L, 0L, 1000L);
      Setting var161 = var160.resolveSetting7("Delay to wait before moving cursor in inventory");
      Setting var162 = this.modeSetting;
      AutoTotemModule.Mode var163 = AutoTotemModule.Mode.Legit;
      Setting var164 = var161.resolveSetting4(var162, var163);
      Setting var165 = this.resolveSetting2(var164);
      this.mutableSetting6 = var165;
      BooleanSetting var166 = new BooleanSetting("Control");
      Setting var167 = var166.resolveSetting7("Moves your cursor to totems automatically");
      Setting var168 = this.modeSetting;
      AutoTotemModule.Mode var169 = AutoTotemModule.Mode.Legit;
      Setting var170 = var167.resolveSetting4(var168, var169);
      Setting var171 = this.resolveSetting2(var170);
      this.booleanValueSetting5 = var171;
      Boolean var173 = false & true;
      BooleanSetting var172 = new BooleanSetting("Check", var173);
      Setting var174 = this.resolveSetting2(var172);
      this.booleanValueSetting6 = var174;
      LongNumberSetting var175 = new LongNumberSetting("CheckTimeout", 0L, 0L, 500L, true & true);
      Setting var176 = this.resolveSetting2(var175);
      this.longValueSetting3 = var176;
      BindSetting var177 = new BindSetting("Shield");
      Setting var178 = this.modeSetting;
      AutoTotemModule.Mode[] var179 = new AutoTotemModule.Mode[1];
      AutoTotemModule.Mode var180 = AutoTotemModule.Mode.Legit;
      var179[0] = var180;
      Visibility var181 = Visibility.resolveVisibility3(var178, var179);
      Visibility var182 = var181.getVisibility2();
      Setting var183 = var177.resolveSetting2(var182, true & true);
      Setting var184 = this.resolveSetting2(var183);
      this.keyCombinationSetting = var184;
      Threaded var185 = new Threaded();
      this.threaded3 = var185;
      Normal var186 = new Normal();
      this.normal = var186;
      Threaded var187 = new Threaded();
      this.threaded = var187;
      Threaded var188 = new Threaded();
      this.threaded2 = var188;
      this.intValue = 0;
      Setting var189 = this.booleanValueSetting6;
      Setting[] var190 = new Setting[1];
      Setting var191 = this.longValueSetting3;
      var190[0] = var191;
      SettingGroup var192 = this.resolveSettingGroup(var189, var190);
      Setting var193 = this.modeSetting;
      AutoTotemModule.Mode var194 = AutoTotemModule.Mode.Legit;
      var192.resolveSettingGroup4(var193, var194);
      Setting var195 = this.booleanValueSetting;
      Setting[] var196 = new Setting[2];
      Setting var197 = this.mapSetting;
      var196[0] = var197;
      Setting var198 = this.floatValueSetting;
      var196[1] = var198;
      SettingGroup var199 = this.resolveSettingGroup(var195, var196);
      Setting var200 = this.modeSetting;
      AutoTotemModule.Mode[] var201 = new AutoTotemModule.Mode[1];
      AutoTotemModule.Mode var202 = AutoTotemModule.Mode.Legit;
      var201[0] = var202;
      Visibility var203 = Visibility.resolveVisibility3(var200, var201);
      Visibility var204 = var203.getVisibility2();
      var199.resolveSettingGroup5(var204);
      Setting var205 = this.booleanValueSetting3;
      Setting[] var206 = new Setting[2];
      Setting var207 = this.integerSetting;
      var206[0] = var207;
      Setting var208 = this.mutableSetting3;
      var206[1] = var208;
      SettingGroup var209 = this.resolveSettingGroup(var205, var206);
      Setting var210 = this.modeSetting;
      AutoTotemModule.Mode var211 = AutoTotemModule.Mode.Legit;
      var209.resolveSettingGroup4(var210, var211);
      var var212 = new SafeListener<TickCounter>() {
         public void handleObject(
            TickCounter tickCounter,
            ClientPlayerEntity clientPlayerEntity,
            ClientWorld clientWorld,
            ClientPlayerInteractionManager clientPlayerInteractionManager
         ) {
            AutoTotemModule.this.handleClientPlayerEntity2(clientPlayerEntity);
         }
      };
      this.handleListener(var212);
      var var213 = new Listener<OnDeathScreenEvent>() {
         public void handleObject2(OnDeathScreenEvent onDeathScreenEvent) {
            if (AutoTotemModule.this.booleanValueSetting4.getValue()) {
               long l = var1.getEntityIdManager().getThreaded().value();
               if (l < 450L) {
                  AutoTotemModule.this.handleText6("Death due to latency / server lag, last totem: %sms".formatted(l));
               }
            }
         }
      };
      this.handleListener(var213);
      autoTotemModule = this;
   }

   public boolean testItem(Item item) {
      for (Entry<AutoTotemModule.ItemType, Boolean> entry : this.mapSetting.getValue().entrySet()) {
         if (entry.getValue() && entry.getKey().getFilter().test(item)) {
            return true;
         }
      }

      return false;
   }

   public void handleItemSwitcher(ItemSwitcher itemSwitcher, Item item) {
      if (this.threaded3.testNumber(this.longValueSetting.getValue())) {
         if (!itemSwitcher.getSlot2().getStack().isOf(item)) {
            if (this.normal.testNumber(this.longValueSetting3.getValue()) || !this.booleanValueSetting6.getValue()) {
               MutableObject<Slot> mutableObject = new MutableObject<>();
               MutableObject<Slot> mutableObject2 = new MutableObject<>();
               itemSwitcher.resolveObject(slotx -> {
                  ItemStack itemStack = slotx.getStack();
                  if (!itemStack.isOf(item)) {
                     return null;
                  } else if (itemSwitcher.testSlot4(slotx)) {
                     mutableObject2.setValue(slotx);
                     return null;
                  } else {
                     mutableObject.setValue(slotx);
                     return null;
                  }
               });
               Slot slot = mutableObject2.getValue() == null ? mutableObject.getValue() : mutableObject2.getValue();
               if (slot != null) {
                  if (this.booleanValueSetting2.getValue()) {
                     assert mc.player != null;
                     assert mc.interactionManager != null;
                     if (mc.player.isUsingItem()) {
                        mc.interactionManager.stopUsingItem(mc.player);
                     }
                  }

                  switch ((AutoTotemModule.Mode)this.modeSetting.getValue()) {
                     case Legit:
                        itemSwitcher.resolveSwitchResult(Hand.OFF_HAND, slot, 0);
                        break;
                     case Normal:
                        itemSwitcher.resolveSwitchResult(Hand.OFF_HAND, slot, 4);
                  }

                  this.threaded3.run();
               }
            }
         }
      }
   }

   public static boolean check3() {
      PhobosClient phobosClient = autoTotemModule.phobosClient;
      if (mc.currentScreen instanceof HandledScreen<?> handledScreen) {
         ItemStack itemStack = handledScreen.getScreenHandler().getCursorStack();
         if (itemStack != null && !itemStack.isEmpty()) {
            return false;
         }
      }

      if (phobosClient.getRotationDelay().check()) {
         return false;
      } else {
         return phobosClient.getEntityIdManager().testLong(autoTotemModule.longValueSetting2.getValue())
            ? false
            : autoTotemModule.normal.testNumber(autoTotemModule.longValueSetting3.getValue());
      }
   }

   public void handleClientPlayerEntity2(ClientPlayerEntity clientPlayerEntity) {
      AtomicInteger atomicInteger = new AtomicInteger();
      this.phobosClient
         .getInventoryManager()
         .handleConsumer2(itemSwitcher -> atomicInteger.set(itemSwitcher.computePredicate(itemStack -> itemStack.isOf(Items.TOTEM_OF_UNDYING))));
      if (this.intValue != atomicInteger.get()) {
         if (atomicInteger.get() < this.intValue) {
            this.threaded3.run();
         }

         DelayedTask.handleLong(150L, () -> {
            if (this.phobosClient.getEntityIdManager().getThreaded().testLong(750L)) {
               this.normal.run();
            }
         });
      }

      this.intValue = atomicInteger.get();
      switch ((AutoTotemModule.Mode)this.modeSetting.getValue()) {
         case Strict:
            if (!this.booleanValueSetting5.getValue() || !check3() || !this.check9()) {
               return;
            }

            if (!this.threaded.testSetting2(this.mutableSetting)) {
               return;
            }

            this.phobosClient
               .getInventoryManager()
               .handleConsumer2(
                  itemSwitcher -> {
                     Slot slot2 = itemSwitcher.getSlot2();
                     KeyValue<Boolean, Integer> keyValue = getKeyValue();
                     boolean flagx;
                     if (!keyValue.getObject()) {
                        if (slot2.getStack().isOf(Items.TOTEM_OF_UNDYING)) {
                           flagx = true;
                        } else {
                           flagx = false;
                        }
                     } else {
                        Slot slot = itemSwitcher.resolveSlot(keyValue.getObject2() - 1);
                        if (slot2.getStack().isOf(Items.TOTEM_OF_UNDYING) && slot.getStack().isOf(Items.TOTEM_OF_UNDYING)) {
                           flagx = true;
                        } else {
                           flagx = false;
                        }
                     }

                     if (flagx) {
                        if (!this.booleanValueSetting7.getValue() && mc.currentScreen instanceof ActionTimer actionTimer && actionTimer.testInt(1)) {
                           actionTimer.run();
                           if (actionTimer.testSetting(this.mutableSetting4)) {
                              clientPlayerEntity.closeScreen();
                              return;
                           }
                        }
                     } else {
                        MutableObject<Double> mutableObject = new MutableObject<>(Double.MAX_VALUE);
                        TreeMap<Double, Slot> treeMap = new TreeMap<>();
                        itemSwitcher.resolveObject(slot2x -> {
                           if (itemSwitcher.testSlot4(slot2x)) {
                              return null;
                           }

                           if (slot2.equals(slot2x)) {
                              return null;
                           }

                           if (slot2x.getStack().isOf(Items.TOTEM_OF_UNDYING)) {
                              double dx = ClickScheduler.getINSTANCE().computeSlot(slot2x);
                              double d2 = mutableObject.getValue();
                              if (d2 > dx) {
                                 mutableObject.setValue(dx);
                                 treeMap.put(dx, slot2x);
                                 if (treeMap.size() > 5) {
                                    treeMap.pollLastEntry();
                                 }
                              }
                           }

                           return null;
                        });
                        if (!treeMap.isEmpty()) {
                           Slot slot3 = treeMap.firstEntry().getValue();
                           double d = ClickScheduler.getINSTANCE().computeSlot(slot3);
                           Slot slot4 = d > 150.0 ? slot3 : treeMap.values().stream().toList().get(MathUtils.computeInt5(0, treeMap.size()));
                           if (slot4 != null) {
                              if (!this.booleanValueSetting7.getValue()) {
                                 if (mc.currentScreen == null) {
                                    mc.setScreen(new ActionTimer(clientPlayerEntity, 1));
                                 }

                                 if (!(mc.currentScreen instanceof ActionTimer)) {
                                    return;
                                 }
                              } else if (!(mc.currentScreen instanceof InventoryScreen)) {
                                 return;
                              }

                              ISlot iSlot = (ISlot)(Object)slot4;
                              if (iSlot.isLoaded()) {
                                 if (!ClickScheduler.getINSTANCE().isFlag()) {
                                    ClickScheduler.getINSTANCE()
                                       .handleISlot(
                                          iSlot,
                                          FloatPair.createFloatPair(
                                             this.mutableSetting5.getValue().getNumber2(), this.mutableSetting5.getValue().getNumber3()
                                          )
                                       );
                                 }
                              }
                           }
                        }
                     }
                  }
               );
            break;
         default:
            if (!PlayerActionUtils.check2()) {
               return;
            }

            boolean flag = this.booleanValueSetting.getValue()
               && mc.options.useKey.isPressed()
               && mc.currentScreen == null
               && this.testItem(clientPlayerEntity.getMainHandStack().getItem());
            float f = EntityUtils.computeLivingEntity(clientPlayerEntity);
            this.phobosClient
               .getInventoryManager()
               .handleConsumer2(
                  itemSwitcher -> {
                     if (SuicideModule.getSuicideModule().check7()) {
                        if (this.offhandSetting.getValue().getItem() != Items.TOTEM_OF_UNDYING) {
                           this.handleItemSwitcher(itemSwitcher, this.offhandSetting.getValue().getItem());
                        }
                     } else {
                        boolean flag2 = this.behaviorSetting.testObject2(AutoTotemModule.Behavior.Frames)
                           && f > 3.0F
                           && f > this.phobosClient.getDamageCache().getFloatValue()
                           && !this.phobosClient
                              .getExplosionTracker()
                              .testClientPlayerEntity(clientPlayerEntity, this.phobosClient.getDamageCache().getFloatValue(), 400L);
                        if (!flag2 && f <= (flag ? this.floatValueSetting.getValue() : this.floatValueSetting2.getValue())) {
                           this.handleItemSwitcher(itemSwitcher, Items.TOTEM_OF_UNDYING);
                        } else if (this.phobosClient.getBindManager().testKeyCombination(this.keyCombinationSetting.getValue())) {
                           this.handleItemSwitcher(itemSwitcher, Items.SHIELD);
                        } else if (flag) {
                           this.handleItemSwitcher(itemSwitcher, Items.ENCHANTED_GOLDEN_APPLE);
                        } else {
                           this.handleItemSwitcher(
                              itemSwitcher, atomicInteger.get() == 0 ? this.offhandSetting2.getValue().getItem() : this.offhandSetting.getValue().getItem()
                           );
                        }
                     }
                  }
               );
      }
   }

   public static KeyValue<Boolean, Integer> getKeyValue() {
      return KeyValue.createValue(autoTotemModule.booleanValueSetting3.getValue(), autoTotemModule.integerSetting.getValue());
   }

   @Override
   public String nothing() {
      return String.valueOf(this.offhandSetting.getValue());
   }

   public static boolean check4() {
      return autoTotemModule.check7() && autoTotemModule.check2() && check3();
   }

   public static boolean check6() {
      autoTotemModule.mutableSetting2.getValue().run6();
      return autoTotemModule.threaded3.testSetting2(autoTotemModule.mutableSetting2);
   }

   public boolean check5() {
      return !this.modeSetting.testObject2(AutoTotemModule.Mode.Legit);
   }

   public static boolean testFlag(boolean flag) {
      return flag
         ? autoTotemModule.threaded2.testSetting(autoTotemModule.mutableSetting3)
         : autoTotemModule.threaded2.testSetting2(autoTotemModule.mutableSetting3);
   }

   public static void run2() {
      autoTotemModule.threaded3.run();
   }

   public static void handleMultiSelectSetting2(MultiSelectSetting multiSelectSetting) {
      for (AutoTotemModule.ItemType itemType : AutoTotemModule.ItemType.values()) {
         multiSelectSetting.resolveMultiSelectSetting(itemType, itemType.name());
      }
   }

   public static boolean check8() {
      return autoTotemModule.modeSetting.testObject2(AutoTotemModule.Mode.Legit);
   }

   public boolean check9() {
      if (mc.currentScreen instanceof InventoryScreen inventoryScreen) {
         this.mutableSetting6.getValue().run6();
         IInventoryScreen iInventoryScreen = IInventoryScreen.generic(inventoryScreen);
         return iInventoryScreen.getOpenTime().testSetting2(this.mutableSetting6);
      } else {
         return false;
      }
   }

   public static boolean testFlag2(boolean flag) {
      return flag
         ? autoTotemModule.threaded.testSetting(autoTotemModule.mutableSetting)
         : autoTotemModule.threaded.testSetting2(autoTotemModule.mutableSetting);
   }

   private enum Behavior implements TextProvider {
      Default("Regular health based AutoTotem"),
      Frames("Invincibility frames based AutoTotem");

      public final String text;

      Behavior(final String description) {
         this.text = description;
      }

      @Override
      public String getText() {
         return this.text;
      }
   }

   private enum ItemType {
      Sword(item -> item instanceof SwordItem),
      Pickaxe(item -> item instanceof PickaxeItem),
      Axe(item -> item instanceof AxeItem),
      Mace(item -> item instanceof MaceItem);

      public final Predicate<Item> filter;

      ItemType(final Predicate<Item> filter) {
         this.filter = filter;
      }

      public Predicate<Item> getFilter() {
         return this.filter;
      }
   }

   private enum Mode implements TextProvider {
      Normal("Default mode"),
      Strict("Uses old 1.12 (legacy) pickup"),
      Legit("Automatic aim Macro");

      public final String text;

      Mode(final String description) {
         this.text = description;
      }

      @Override
      public String getText() {
         return this.text;
      }
   }

   private enum Offhand {
      None(Items.AIR),
      Totem(Items.TOTEM_OF_UNDYING),
      GApples(Items.ENCHANTED_GOLDEN_APPLE),
      Crystals(Items.END_CRYSTAL);

      public final Item item;

      public Item getItem() {
         return this.item;
      }

      Offhand(final Item item) {
         this.item = item;
      }
   }
}
