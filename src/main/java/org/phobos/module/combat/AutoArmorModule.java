package org.phobos.module.combat;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.Nullable;
import org.phobos.api.NumberRange;
import org.phobos.api.TimeGate.Normal;
import org.phobos.api.NumberRange.Mutable;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.MutableLongSetting;
import org.phobos.core.setting.BindSetting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.combat.ArmorSlotState;
import org.phobos.core.combat.AutoArmorHelper;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.core.setting.SettingGroup;
import org.phobos.core.bind.KeyCombination;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.block.BlockPosUtils;
import org.phobos.core.module.ModuleListenerUtils;
import org.phobos.core.player.PlayerActionUtils;
import org.phobos.core.inventory.EnchantmentUtils;
import org.phobos.core.entity.EntityUtils;
import org.phobos.core.inventory.InventoryUtils;
import org.phobos.core.setting.SettingSubsection.Builder;
import org.phobos.core.inventory.ItemUtils.Armor;
import org.phobos.event.AutoArmorAttackPreListener;
import org.phobos.event.AutoArmorTickListener;
import org.phobos.module.Module;
import org.phobos.module.player.AutoXPModule;

public class AutoArmorModule extends Module {
   public AutoArmorHelper autoArmorHelper;
   public Setting<Integer> integerSetting;
   public ArmorSlotState[] armorSlotStateArray;
   public Setting<Boolean> booleanValueSetting;
   public Setting<Mutable<Long>> mutableSetting;
   public Queue<AutoArmorModule.InventoryClick> inventoryClickQueue;
   public static boolean flag = false;
   public Setting<Integer> integerSetting2;
   public Setting<Integer> integerSetting3;
   public Setting<Boolean> booleanValueSetting2;
   public static AutoArmorModule autoArmorModule;
   public Setting<Integer> integerSetting4;
   public Setting<Double> doubleValueSetting;
   public Setting<Boolean> booleanValueSetting3;
   public Setting<AutoArmorModule.ArmorPriority> armorPrioritySetting;
   public Setting<Boolean> booleanValueSetting4;
   public boolean flag2;
   public Normal normal;
   public Setting<Boolean> booleanValueSetting5;
   public Setting<Boolean> booleanValueSetting6;
   public Setting<Float> floatValueSetting;
   public boolean flag3;
   public Setting<Boolean> booleanValueSetting7;
   public Setting<Boolean> booleanValueSetting8;
   public Setting<Double> doubleValueSetting2;
   public Setting<KeyCombination> keyCombinationSetting;
   public ArmorSlotState armorSlotState;
   public Setting<Boolean> booleanValueSetting9;
   private static Object[] phobos$g1806dc930;

   @Override
   public void run3() {
      this.inventoryClickQueue.clear();
      this.run2();
   }

   public ArmorSlotState resolveArmorSlotState(EquipmentSlot equipmentSlot) {
      return switch (equipmentSlot) {
         case FEET -> this.armorSlotStateArray[3];
         case LEGS -> this.armorSlotStateArray[2];
         case CHEST, BODY -> this.armorSlotStateArray[1];
         case HEAD -> this.armorSlotStateArray[0];
         default -> null;
      };
   }

   public static AutoArmorModule getAutoArmorModule() {
      return autoArmorModule;
   }

   public void handleClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld) {
      this.flag3 = true;
      this.handleClientPlayerEntity3(clientPlayerEntity, clientWorld);
   }

   @Nullable
   public ItemStack resolveItemStack(int i) {
      return mc.player == null ? null : mc.player.playerScreenHandler.getSlot(i).getStack();
   }

   public static AutoArmorModule.ArmorPriority getArmorPriority() {
      return autoArmorModule.armorPrioritySetting.getValue();
   }

   public EquipmentSlot resolveEquipmentSlot(ItemStack itemStack) {
      if (!itemStack.isEmpty()) {
         if (itemStack.getItem() instanceof ArmorItem armorItem) {
            return armorItem.getSlotType();
         }

         if (itemStack.getItem() instanceof ElytraItem) {
            return EquipmentSlot.CHEST;
         }
      }

      return null;
   }

   public void handleItemSwitcher(ItemSwitcher itemSwitcher, ClientPlayerEntity clientPlayerEntity, int i) {
      this.handleItemSwitcher2(itemSwitcher, i, 0, SlotActionType.QUICK_MOVE);
   }

   public AutoArmorModule(PhobosClient phobosClient) {
      super(phobosClient, "AutoArmor", Categories.combat, "Automatically puts on armor");
      this.nativeInit(phobosClient);
   }

   public void nativeInit(PhobosClient var1) {
      AutoArmorModule.ArmorPriority var3 = AutoArmorModule.ArmorPriority.Blast;
      EnumSetting var2 = new EnumSetting<>("Priority", var3);
      Setting var4 = var2.resolveSetting7("Priority");
      Setting var5 = this.resolveSetting2(var4);
      this.armorPrioritySetting = var5;
      Long var7 = 50L;
      Long var8 = 50L;
      Mutable var9 = NumberRange.createMutable(var7, var8);
      MutableLongSetting var6 = new MutableLongSetting("Delay", var9, 0L, 500L, true & true);
      Setting var10 = this.resolveSetting2(var6);
      this.mutableSetting = var10;
      BooleanSetting var11 = new BooleanSetting("Elytra");
      Setting var12 = var11.resolveSetting7("Elytra swap");
      Setting var13 = this.resolveSetting2(var12);
      this.booleanValueSetting4 = var13;
      BindSetting var14 = new BindSetting("ElytraKey");
      Setting var15 = this.resolveSetting2(var14);
      this.keyCombinationSetting = var15;
      BooleanSetting var16 = new BooleanSetting("ElytraMace");
      Setting var17 = this.resolveSetting2(var16);
      this.booleanValueSetting3 = var17;
      BooleanSetting var18 = new BooleanSetting("Notify");
      Setting var19 = this.resolveSetting2(var18);
      this.booleanValueSetting = var19;
      BooleanSetting var20 = new BooleanSetting("Mending");
      Setting var21 = var20.resolveSetting7("Mending take-off for efficiency");
      Setting var22 = this.resolveSetting2(var21);
      this.booleanValueSetting5 = var22;
      Integer var24 = 80;
      Integer var25 = 1;
      Integer var26 = 100;
      NumberSetting var23 = new NumberSetting<>("Helmet", var24, var25, var26, "%");
      Setting var27 = this.resolveSetting2(var23);
      this.integerSetting = var27;
      Integer var29 = 85;
      Integer var30 = 1;
      Integer var31 = 100;
      NumberSetting var28 = new NumberSetting<>("Chestplate", var29, var30, var31, "%");
      Setting var32 = this.resolveSetting2(var28);
      this.integerSetting3 = var32;
      Integer var34 = 84;
      Integer var35 = 1;
      Integer var36 = 100;
      NumberSetting var33 = new NumberSetting<>("Leggings", var34, var35, var36, "%");
      Setting var37 = this.resolveSetting2(var33);
      this.integerSetting2 = var37;
      Integer var39 = 80;
      Integer var40 = 1;
      Integer var41 = 100;
      NumberSetting var38 = new NumberSetting<>("Boots", var39, var40, var41, "%");
      Setting var42 = this.resolveSetting2(var38);
      this.integerSetting4 = var42;
      BooleanSetting var43 = new BooleanSetting("MendingSingle");
      Setting var44 = var43.resolveSetting7("Keep on a single piece of armor");
      Setting var45 = this.resolveSetting2(var44);
      this.booleanValueSetting9 = var45;
      BooleanSetting var46 = new BooleanSetting("MendingSingleAwait");
      Setting var47 = var46.resolveSetting7("Wait until the piece has reached the threshold");
      Setting var48 = this.resolveSetting2(var47);
      this.booleanValueSetting2 = var48;
      Double var50 = 12.0;
      Double var51 = 4.0;
      Double var52 = 32.0;
      NumberSetting var49 = new NumberSetting<>("MendingRange", var50, var51, var52);
      Setting var53 = var49.resolveSetting7("Range to lookup enemies");
      Setting var54 = this.resolveSetting2(var53);
      this.doubleValueSetting2 = var54;
      Double var56 = 1.0;
      Double var57 = 0.0;
      Double var58 = 16.0;
      NumberSetting var55 = new NumberSetting<>("MendingDamage", var56, var57, var58);
      Double var59 = 0.0;
      NumberSetting var60 = var55.resolveNumberSetting("Disabled", var59);
      Setting var61 = var60.resolveSetting7("Calculate nearby crystals damage");
      Setting var62 = this.resolveSetting2(var61);
      this.doubleValueSetting = var62;
      BooleanSetting var63 = new BooleanSetting("Lookup");
      Setting var64 = var63.resolveSetting7("Find dropped experience");
      Setting var65 = this.resolveSetting2(var64);
      this.booleanValueSetting7 = var65;
      BooleanSetting var66 = new BooleanSetting("Preserve");
      Setting var67 = var66.resolveSetting7("Preserve low durability armor");
      Setting var68 = this.resolveSetting2(var67);
      this.booleanValueSetting8 = var68;
      Float var70 = (float)1.0F;
      Float var71 = (float)1.0F;
      Float var72 = (float)20.0F;
      NumberSetting var69 = new NumberSetting<>("Durability", var70, var71, var72);
      Setting var73 = this.resolveSetting2(var69);
      this.floatValueSetting = var73;
      BooleanSetting var74 = new BooleanSetting("Inventory");
      Setting var75 = var74.resolveSetting7("Only works when the inventory is open");
      Setting var76 = this.resolveSetting2(var75);
      this.booleanValueSetting6 = var76;
      ConcurrentLinkedQueue var77 = new ConcurrentLinkedQueue();
      this.inventoryClickQueue = var77;
      AutoArmorHelper var78 = new AutoArmorHelper(this);
      this.autoArmorHelper = var78;
      Normal var79 = new Normal();
      this.normal = var79;
      this.flag3 = false & true;
      ArmorSlotState[] var80 = new ArmorSlotState[4];
      EquipmentSlot var82 = EquipmentSlot.HEAD;
      ArmorSlotState var81 = new ArmorSlotState(var82);
      var80[0] = var81;
      EquipmentSlot var84 = EquipmentSlot.CHEST;
      ArmorSlotState var83 = new ArmorSlotState(var84);
      var80[1] = var83;
      EquipmentSlot var86 = EquipmentSlot.LEGS;
      ArmorSlotState var85 = new ArmorSlotState(var86);
      var80[2] = var85;
      EquipmentSlot var88 = EquipmentSlot.FEET;
      ArmorSlotState var87 = new ArmorSlotState(var88);
      var80[3] = var87;
      this.armorSlotStateArray = var80;
      Builder var89 = this.createBuilder();
      Setting var90 = this.booleanValueSetting5;
      Builder var91 = var89.resolveBuilder6(var90);
      SettingGroup[] var92 = new SettingGroup[2];
      Setting[] var93 = new Setting[4];
      Setting var94 = this.integerSetting;
      var93[0] = var94;
      Setting var95 = this.integerSetting3;
      var93[1] = var95;
      Setting var96 = this.integerSetting2;
      var93[2] = var96;
      Setting var97 = this.integerSetting4;
      var93[3] = var97;
      SettingGroup var98 = this.resolveSettingGroup2("Armor", var93);
      var92[0] = var98;
      Setting var99 = this.booleanValueSetting9;
      Setting[] var100 = new Setting[1];
      Setting var101 = this.booleanValueSetting2;
      var100[0] = var101;
      SettingGroup var102 = this.resolveSettingGroup(var99, var100);
      var92[1] = var102;
      Builder var103 = var91.resolveBuilder5(var92);
      Setting[] var104 = new Setting[3];
      Setting var105 = this.doubleValueSetting2;
      var104[0] = var105;
      Setting var106 = this.doubleValueSetting;
      var104[1] = var106;
      Setting var107 = this.booleanValueSetting7;
      var104[2] = var107;
      Builder var108 = var103.resolveBuilder(var104);
      var108.getSettingSubsection();
      Setting var109 = this.booleanValueSetting4;
      Setting[] var110 = new Setting[3];
      Setting var111 = this.keyCombinationSetting;
      var110[0] = var111;
      Setting var112 = this.booleanValueSetting3;
      var110[1] = var112;
      Setting var113 = this.booleanValueSetting;
      var110[2] = var113;
      this.resolveSettingGroup(var109, var110);
      Setting var114 = this.booleanValueSetting8;
      Setting[] var115 = new Setting[1];
      Setting var116 = this.floatValueSetting;
      var115[0] = var116;
      this.resolveSettingGroup(var114, var115);
      AutoArmorTickListener var117 = new AutoArmorTickListener(this);
      this.handleListener(var117);
      AutoArmorAttackPreListener var118 = new AutoArmorAttackPreListener(this);
      this.handleListener(var118);
      Queue var119 = this.inventoryClickQueue;
      Runnable var136 = var119::clear;
      ModuleListenerUtils.handleListenerHolder4(this, var136);
      autoArmorModule = this;
   }

   public void handleItemSwitcher2(ItemSwitcher itemSwitcher, int i, int i2, SlotActionType slotActionType) {
      if (this.normal.testNumber2(this.mutableSetting.getValue().getNumber4())) {
         itemSwitcher.handleRunnable2(() -> itemSwitcher.handleInt5(i, i2, slotActionType));
      } else {
         this.inventoryClickQueue.add(new AutoArmorModule.InventoryClick(i, i2, slotActionType));
      }
   }

   public void run() {
      if (this.booleanValueSetting4.getValue() && mc.currentScreen == null) {
         if (this.phobosClient.getBindManager().testKeyCombination(this.keyCombinationSetting.getValue())) {
            if (!this.flag2) {
               this.run5();
            }

            this.flag2 = true;
         } else {
            this.flag2 = false;
         }
      }
   }

   public boolean check() {
      return this.booleanValueSetting4.getValue() && this.booleanValueSetting3.getValue();
   }

   public void handleClientPlayerEntity2(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld) {
      this.flag3 = false;
      this.handleClientPlayerEntity3(clientPlayerEntity, clientWorld);
   }

   public void handleClientPlayerEntity3(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld) {
      this.run();
      if (!flag) {
         if (mc.currentScreen == null || mc.currentScreen instanceof InventoryScreen || mc.currentScreen instanceof CreativeInventoryScreen) {
            if (!this.booleanValueSetting6.getValue() || mc.currentScreen instanceof InventoryScreen) {
               this.phobosClient
                  .getInventoryManager()
                  .handleConsumer2(
                     itemSwitcher -> {
                        if (this.testClientPlayerEntity(clientPlayerEntity, clientWorld) && itemSwitcher.testFlag(true, false)) {
                           if (this.booleanValueSetting9.getValue()) {
                              this.autoArmorHelper
                                 .handleItemSwitcher2(itemSwitcher, clientPlayerEntity, this::handleItemSwitcher, (itemSwitcherx, clientPlayerEntityxx, i) -> {
                                    if (itemSwitcherx.testFlag(true, false)) {
                                       this.handleItemSwitcher2(itemSwitcherx, i, 0, SlotActionType.QUICK_MOVE);
                                    }
                                 });
                           } else {
                              this.autoArmorHelper.handleItemSwitcher(itemSwitcher, clientPlayerEntity, (itemSwitcherx, clientPlayerEntityxx, i) -> {
                                 if (itemSwitcherx.testFlag(true, false)) {
                                    this.handleItemSwitcher2(itemSwitcherx, i, 0, SlotActionType.QUICK_MOVE);
                                 }
                              });
                           }
                        } else {
                           this.run2();
                           this.clear();
                           this.handleItemSwitcher3(itemSwitcher, clientPlayerEntity);
                        }
                     }
                  );
            }
         }
      }
   }

   @Override
   public void run4() {
      this.run2();
   }

   public int computeEquipmentSlot(EquipmentSlot equipmentSlot) {
      return switch (equipmentSlot) {
         case FEET -> 8;
         case LEGS -> 7;
         case CHEST, BODY -> 6;
         case HEAD -> 5;
         default -> -1;
      };
   }

   public int computeItemStack(ItemStack itemStack) {
      int i = 0;
      if (EnchantmentUtils.testItemStack(itemStack, Enchantments.PROTECTION)) {
         i += EnchantmentUtils.computeItemStack3(itemStack, Enchantments.PROTECTION) * 10;
      }

      if (EnchantmentUtils.testItemStack(itemStack, Enchantments.UNBREAKING)) {
         i += EnchantmentUtils.computeItemStack3(itemStack, Enchantments.UNBREAKING);
      }

      return i;
   }

   public void run2() {
      for (ArmorSlotState armorSlotState : this.armorSlotStateArray) {
         armorSlotState.setFlag(false);
      }
   }

   public void run5() {
      this.flag3 = !this.flag3;
      if (this.booleanValueSetting.getValue()) {
         this.handleText6("Set elytra priority to %s".formatted(this.flag3 ? "true" : "false"));
      }
   }

   public Map<EquipmentSlot, Pair<Slot, Integer>> resolveMap(ItemSwitcher itemSwitcher) {
      Map<EquipmentSlot, Pair<Slot, Integer>> map = new HashMap<>();
      itemSwitcher.resolveObject(slot -> {
         ItemStack itemStack = slot.getStack();
         if (this.testItemStack(itemStack)) {
            return null;
         }

         if (itemStack.getItem() instanceof ElytraItem && this.booleanValueSetting4.getValue() && this.flag3) {
            if (this.booleanValueSetting8.getValue()) {
               float f = InventoryUtils.computeItemStack(itemStack);
               if (f <= this.floatValueSetting.getValue()) {
                  return null;
               }
            }

            EquipmentSlot equipmentSlot = EquipmentSlot.CHEST;
            int i = 1000000000 + this.computeItemStack(itemStack) + (int)InventoryUtils.computeItemStack(itemStack);
            if (map.containsKey(equipmentSlot)) {
               Integer integer = map.get(equipmentSlot).getRight();
               if (integer < i) {
                  map.put(EquipmentSlot.CHEST, new Pair<>(slot, i));
               }
            } else {
               map.put(EquipmentSlot.CHEST, new Pair<>(slot, i));
            }
         }

         if (itemStack.getItem() instanceof ArmorItem armorItem) {
            if (this.booleanValueSetting8.getValue()) {
               float f2 = InventoryUtils.computeItemStack(itemStack);
               if (f2 <= this.floatValueSetting.getValue()) {
                  return null;
               }
            }

            EquipmentSlot equipmentSlot2 = armorItem.getSlotType();
            int i2 = this.computeItemStack2(itemStack);
            if (i2 == -1) {
               return null;
            }

            if (map.containsKey(equipmentSlot2)) {
               Integer integer2 = map.get(equipmentSlot2).getRight();
               if (integer2 < i2) {
                  map.put(equipmentSlot2, new Pair<>(slot, i2));
               }
            } else {
               map.put(equipmentSlot2, new Pair<>(slot, i2));
            }
         }

         return null;
      });
      return map;
   }

   public void handleItemSwitcher3(ItemSwitcher itemSwitcher, ClientPlayerEntity clientPlayerEntity) {
      if (!this.inventoryClickQueue.isEmpty()) {
         if (this.normal.testNumber2(this.mutableSetting.getValue().getNumber4())) {
            AutoArmorModule.InventoryClick inventoryClick = this.inventoryClickQueue.poll();
            if (inventoryClick != null) {
               inventoryClick.handleItemSwitcher(itemSwitcher);
            }
         }
      } else {
         boolean flag = itemSwitcher.testFlag(true, false);
         Map<EquipmentSlot, Pair<Slot, Integer>> map = this.resolveMap(itemSwitcher);

         for (Entry<EquipmentSlot, Pair<Slot, Integer>> entry : map.entrySet()) {
            EquipmentSlot equipmentSlot = entry.getKey();
            ArmorSlotState armorSlotState = this.resolveArmorSlotState(equipmentSlot);
            if (armorSlotState != null && !armorSlotState.isFlag()) {
               int i = this.computeEquipmentSlot(equipmentSlot);
               if (i != -1) {
                  Slot slot = clientPlayerEntity.playerScreenHandler.getSlot(i);
                  Slot slot2 = entry.getValue().getLeft();
                  ItemStack itemStack = slot.getStack();
                  if (slot.id != slot2.id) {
                     int i2 = itemStack.getItem() instanceof ElytraItem && this.booleanValueSetting4.getValue() && this.flag3
                        ? 1000000000 + this.computeItemStack(itemStack) + (int)InventoryUtils.computeItemStack(itemStack)
                        : this.computeItemStack2(itemStack);
                     if (i2 != entry.getValue().getRight()) {
                        if (itemStack.isEmpty()) {
                           this.handleItemSwitcher(itemSwitcher, clientPlayerEntity, slot2.id);
                        } else if (flag) {
                           this.handleItemSwitcher2(itemSwitcher, i, 0, SlotActionType.QUICK_MOVE);
                           this.handleItemSwitcher(itemSwitcher, clientPlayerEntity, slot2.id);
                        } else {
                           this.handleItemSwitcher2(itemSwitcher, slot2.id, 0, SlotActionType.PICKUP);
                           this.handleItemSwitcher2(itemSwitcher, i, 0, SlotActionType.PICKUP);
                           this.handleItemSwitcher2(itemSwitcher, slot2.id, 0, SlotActionType.PICKUP);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public void run7() {
      for (ArmorSlotState armorSlotState : this.armorSlotStateArray) {
         armorSlotState.setFlag(true);
      }
   }

   public boolean testEquipmentSlot(EquipmentSlot equipmentSlot, float f) {
      Setting<Integer> setting = switch (equipmentSlot) {
         case FEET -> this.integerSetting4;
         case LEGS -> this.integerSetting2;
         case CHEST -> this.integerSetting3;
         default -> null;
         case HEAD -> this.integerSetting;
      };
      return setting == null ? false : f >= setting.getValue().intValue();
   }

   public boolean testClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld) {
      if (!this.booleanValueSetting5.getValue()) {
         return false;
      }

      if (!this.check2()) {
         return false;
      }

      if (AutoXPModule.getAutoXPModule().check7()
         || mc.options.useKey.isPressed()
            && (clientPlayerEntity.getMainHandStack().isOf(Items.EXPERIENCE_BOTTLE) || clientPlayerEntity.getOffHandStack().isOf(Items.EXPERIENCE_BOTTLE))
         || this.booleanValueSetting7.getValue() && this.testClientPlayerEntity2(clientPlayerEntity, clientWorld)) {
         PlayerEntity playerEntity2 = EntityUtils.resolvePlayerEntity2(clientWorld, clientPlayerEntity, this.phobosClient);
         if (playerEntity2 != null && playerEntity2.distanceTo(clientPlayerEntity) < this.doubleValueSetting2.getValue()) {
            return false;
         }

         if (!this.doubleValueSetting.getValue().equals(0.0)) {
            PlayerEntity playerEntity = PlayerActionUtils.resolvePlayerEntity(clientPlayerEntity);
            playerEntity.getInventory().setStack(EquipmentSlot.HEAD.getEntitySlotId(), ItemStack.EMPTY);
            playerEntity.getInventory().setStack(EquipmentSlot.CHEST.getEntitySlotId(), ItemStack.EMPTY);
            playerEntity.getInventory().setStack(EquipmentSlot.LEGS.getEntitySlotId(), ItemStack.EMPTY);
            playerEntity.getInventory().setStack(EquipmentSlot.FEET.getEntitySlotId(), ItemStack.EMPTY);

            for (Entity entity : clientWorld.getEntities()) {
               if (!(entity.distanceTo(clientPlayerEntity) > 16.0) && entity instanceof EndCrystalEntity endCrystalEntity) {
                  float f = this.phobosClient.getDamageCalculator().computeEntity8(playerEntity, clientWorld, endCrystalEntity);
                  if (f > this.doubleValueSetting.getValue()) {
                     return false;
                  }
               }
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean testItemStack(ItemStack itemStack) {
      return EnchantmentUtils.testItemStack(itemStack, Enchantments.BINDING_CURSE);
   }

   public int computeItemStack2(ItemStack itemStack) {
      return Armor.computeItemStack(itemStack);
   }

   public boolean check2() {
      for (int i = 5; i < 9; i++) {
         ItemStack itemStack = this.resolveItemStack(i);
         if (itemStack != null) {
            float f = InventoryUtils.computeItemStack(itemStack);
            EquipmentSlot equipmentSlot = this.resolveEquipmentSlot(itemStack);
            if (equipmentSlot != null && !this.testEquipmentSlot(equipmentSlot, f)) {
               return true;
            }
         }
      }

      return false;
   }

   public boolean testClientPlayerEntity2(ClientPlayerEntity clientPlayerEntity, ClientWorld clientWorld) {
      for (Entity entity : clientWorld.getOtherEntities(null, BlockPosUtils.createBox(clientPlayerEntity, 4.0))) {
         if (entity instanceof ExperienceOrbEntity) {
            return true;
         }
      }

      return false;
   }

   public void clear() {
      for (ArmorSlotState armorSlotState : this.armorSlotStateArray) {
         armorSlotState.setItemStack(null);
      }

      this.armorSlotState = null;
   }

   public enum ArmorPriority {
      Protection,
      Blast;
   }

   public static class InventoryClick {
      public final int intValue;
      public final int intValue2;
      public final SlotActionType slotActionType;

      public SlotActionType getSlotActionType() {
         return this.slotActionType;
      }

      public InventoryClick(int i, int i2, SlotActionType slotActionType) {
         this.intValue2 = i;
         this.intValue = i2;
         this.slotActionType = slotActionType;
      }

      public int getIntValue2() {
         return this.intValue2;
      }

      public int getIntValue() {
         return this.intValue;
      }

      public void handleItemSwitcher(ItemSwitcher itemSwitcher) {
         itemSwitcher.handleInt5(this.intValue2, this.intValue, this.slotActionType);
      }
   }
}
