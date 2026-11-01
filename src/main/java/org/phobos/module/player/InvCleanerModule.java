package org.phobos.module.player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.mutable.MutableObject;
import org.phobos.api.TextProvider;
import org.phobos.api.TimeGate;
import org.phobos.api.KeyValue;
import org.phobos.api.MinecraftHolder;
import org.phobos.api.NumberRange;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.MutableLongSetting;
import org.phobos.core.setting.ItemCountSetting;
import org.phobos.core.setting.ItemSetSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.core.PhobosClient;
import org.phobos.module.player.LegacyModule;
import org.phobos.core.inventory.ItemUtils;
import org.phobos.core.module.Categories;
import org.phobos.core.block.BlockUtils;
import org.phobos.event.DropItemEvent;
import org.phobos.event.Listener;
import org.phobos.event.TickEvent2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.phobos.module.Module;

public class InvCleanerModule extends Module {
   public final Setting<NumberRange.Mutable<Long>> delay;
   public static final Logger LOGGER = LoggerFactory.getLogger(InvCleanerModule.class);
   public final TimeGate.Normal normal;
   public boolean flag;
   public final Setting<InvCleanerModule.Mode> mode = this.resolveSetting2(new EnumSetting<>("Mode", InvCleanerModule.Mode.Normal));
   public final Setting<InvCleanerModule.Inventory> inventory = this.resolveSetting2(new EnumSetting<>("Inventory", InvCleanerModule.Inventory.Silent));
   public final Setting<Boolean> silent;
   public static InvCleanerModule invCleanerModule;
   public final Setting<Set<Item>> whitelist;

   public static InvCleanerModule getInvCleanerModule() {
      return invCleanerModule;
   }

   @Override
   public String nothing() {
      return String.valueOf(this.inventory.getValue());
   }

   public InvCleanerModule(PhobosClient phobosClient) {
      super(phobosClient, "InvCleaner", Categories.player, "Throws away useless items");
      this.delay = this.resolveSetting2(new MutableLongSetting("Delay", 50L, 0L, 500L, true));
      this.silent = this.resolveSetting2(new BooleanSetting("Silent", false).resolveSetting7("Disables swing when dropping"));
      this.whitelist = this.resolveSetting2(new ItemSetSetting("Whitelist").resolveSetting4(this.mode, InvCleanerModule.Mode.Normal));
      this.normal = new TimeGate.Normal();
      this.flag = false;
      ItemCountSetting itemCountSetting = this.resolveSetting2(
         new ItemCountSetting("Helmet", Items.NETHERITE_HELMET, 1).resolveItemCountSetting3(1).resolveItemCountSetting(0).getItemCountSetting()
      );
      ItemCountSetting itemCountSetting2 = this.resolveSetting2(
         new ItemCountSetting("Chestplate", Items.NETHERITE_CHESTPLATE, 1).resolveItemCountSetting3(1).resolveItemCountSetting(0).getItemCountSetting()
      );
      ItemCountSetting itemCountSetting3 = this.resolveSetting2(
         new ItemCountSetting("Leggings", Items.NETHERITE_LEGGINGS, 1).resolveItemCountSetting3(1).resolveItemCountSetting(0).getItemCountSetting()
      );
      ItemCountSetting itemCountSetting4 = this.resolveSetting2(
         new ItemCountSetting("Boots", Items.NETHERITE_BOOTS, 1).resolveItemCountSetting3(1).resolveItemCountSetting(0).getItemCountSetting()
      );
      ItemCountSetting itemCountSetting5 = this.resolveSetting2(
         new ItemCountSetting("Sword", Items.NETHERITE_SWORD, 1).resolveItemCountSetting3(1).resolveItemCountSetting(0).getItemCountSetting()
      );
      ItemCountSetting itemCountSetting6 = this.resolveSetting2(
         new ItemCountSetting("Pickaxe", Items.NETHERITE_PICKAXE, 1).resolveItemCountSetting3(1).resolveItemCountSetting(0).getItemCountSetting()
      );
      ItemCountSetting itemCountSetting7 = this.resolveSetting2(
         new ItemCountSetting("Shield", Items.SHIELD, 1).resolveItemCountSetting3(1).resolveItemCountSetting(0).getItemCountSetting()
      );
      ItemCountSetting itemCountSetting8 = this.resolveSetting2(new ItemCountSetting("Crystals", Items.END_CRYSTAL, 1).resolveItemCountSetting3(1).resolveItemCountSetting(0));
      ItemCountSetting itemCountSetting9 = this.resolveSetting2(new ItemCountSetting("XP", Items.EXPERIENCE_BOTTLE, 1).resolveItemCountSetting3(1).resolveItemCountSetting(0));
      ItemCountSetting itemCountSetting10 = this.resolveSetting2(
         new ItemCountSetting("Totems", Items.TOTEM_OF_UNDYING, 1).resolveItemCountSetting3(1).resolveItemCountSetting(0)
      );
      ItemCountSetting itemCountSetting11 = this.resolveSetting2(
         new ItemCountSetting("Gaps", Items.ENCHANTED_GOLDEN_APPLE, 1).resolveItemCountSetting3(1).resolveItemCountSetting(0)
      );
      ItemCountSetting itemCountSetting12 = this.resolveSetting2(new ItemCountSetting("Pearls", Items.ENDER_PEARL, 1).resolveItemCountSetting3(1).resolveItemCountSetting(0));
      ItemCountSetting itemCountSetting13 = this.resolveSetting2(new ItemCountSetting("EChests", Items.ENDER_CHEST, 1).resolveItemCountSetting3(1).resolveItemCountSetting(0));
      ItemCountSetting itemCountSetting14 = this.resolveSetting2(new ItemCountSetting("Obsidian", Items.OBSIDIAN, 1).resolveItemCountSetting3(1).resolveItemCountSetting(0));
      ItemCountSetting itemCountSetting15 = this.resolveSetting2(
         new ItemCountSetting("Choruses", Items.CHORUS_FRUIT, 1).resolveItemCountSetting3(1).resolveItemCountSetting(0)
      );
      final List<Setting<KeyValue<Item, Integer>>> list = List.of(
         itemCountSetting,
         itemCountSetting2,
         itemCountSetting3,
         itemCountSetting4,
         itemCountSetting5,
         itemCountSetting6,
         itemCountSetting7,
         itemCountSetting8,
         itemCountSetting9,
         itemCountSetting10,
         itemCountSetting11,
         itemCountSetting12,
         itemCountSetting13,
         itemCountSetting14,
         itemCountSetting15
      );

      for (Setting<KeyValue<Item, Integer>> setting : list) {
         setting.resolveSetting4(this.mode, InvCleanerModule.Mode.CrystalPvP);
      }

      final List<Item> list2 = new ArrayList<>();

      for (Setting<KeyValue<Item, Integer>> setting2 : list) {
         list2.add(setting2.getValue().getObject());
      }

      this.handleListener(new Listener<DropItemEvent>() {
         public void handleObject2(DropItemEvent dropItemEvent) {
            if (InvCleanerModule.this.silent.getValue() && InvCleanerModule.this.flag) {
               dropItemEvent.setCancelled(true);
               InvCleanerModule.this.flag = false;
            }
         }
      });
      this.handleListener(new Listener<TickEvent2>() {
         public void handleObject2(TickEvent2 tickEvent2) {
            if (!InvCleanerModule.this.inventory.testObject2(InvCleanerModule.Inventory.OpenInv) || MinecraftHolder.mc.currentScreen instanceof InventoryScreen) {
               if (InvCleanerModule.this.normal.testSetting(InvCleanerModule.this.delay)) {
                  InvCleanerModule.this.delay.getValue().run6();
                  phobosClient.getInventoryManager().handleConsumer2(itemSwitcher -> {
                     switch ((InvCleanerModule.Mode)InvCleanerModule.this.mode.getValue()) {
                        case CrystalPvP:
                           Slot slot = itemSwitcher.resolveObject(slotx -> {
                              if (itemSwitcher.getSlot2().equals(slotx) && slotx.getStack().isOf(Items.SHIELD) && LegacyModule.getLegacyModule().check7()) {
                                 return null;
                              } else {
                                 return InvCleanerModule.this.testItemSwitcher(itemSwitcher, slotx) ? slotx : null;
                              }
                           });
                           if (slot != null) {
                              InvCleanerModule.this.flag = true;
                              itemSwitcher.handleSlot2(slot, true);
                           }
                           break;
                        case Normal:
                           for (Setting<KeyValue<Item, Integer>> setting : list) {
                              Item item = setting.getValue().getObject();
                              int i = itemSwitcher.computePredicate(itemStack -> itemStack.isOf(item));
                              if (i > setting.getValue().getObject2() * item.getMaxCount()) {
                                 MutableInt mutableInt = new MutableInt(Integer.MAX_VALUE);
                                 MutableObject<Slot> mutableObject = new MutableObject();
                                 itemSwitcher.resolveObject(slotx -> {
                                    if (itemSwitcher.testSlot4(slotx)) {
                                       return null;
                                    }

                                    ItemStack itemStack = slotx.getStack();
                                    if (itemStack.isEmpty()) {
                                       return null;
                                    }

                                    if (!itemStack.isOf(item)) {
                                       return null;
                                    }

                                    int ix = itemStack.getCount();
                                    if (mutableObject.getValue() == null) {
                                       mutableInt.setValue(ix);
                                       mutableObject.setValue(slotx);
                                       return null;
                                    }

                                    if (mutableInt.getValue() > ix) {
                                       mutableInt.setValue(ix);
                                       mutableObject.setValue(slotx);
                                    }

                                    return null;
                                 });
                                 if (mutableObject.getValue() != null) {
                                    InvCleanerModule.this.flag = true;
                                    itemSwitcher.handleSlot2((Slot)mutableObject.getValue(), true);
                                    if (InvCleanerModule.this.delay.getValue().testNumber(0L)) {
                                       return;
                                    }
                                 }
                              }
                           }

                           Slot slot2 = itemSwitcher.resolveObject(slotx -> {
                              ItemStack itemStack = slotx.getStack();
                              if (itemStack.isEmpty()) {
                                 return null;
                              }

                              Item itemx = itemStack.getItem();
                              return !list2.contains(itemx) ? slotx : null;
                           });
                           if (slot2 != null) {
                              InvCleanerModule.this.flag = true;
                              itemSwitcher.handleSlot2(slot2, true);
                           }
                     }
                  });
               }
            }
         }
      });
      invCleanerModule = this;
   }

   public boolean testItemSwitcher(ItemSwitcher itemSwitcher, Slot slot) {
      ItemStack itemStack = slot.getStack();
      if (slot.getStack().isEmpty()) {
         return false;
      } else {
         Item item = itemStack.getItem();
         if (item == null) {
            return false;
         } else if (ItemUtils.testItem(item)) {
            return ItemUtils.testItemSwitcher4(itemSwitcher, item, itemStack, slot);
         } else {
            return item instanceof BlockItem && BlockUtils.testItem(item) ? false : !this.whitelist.getValue().contains(item);
         }
      }
   }

   private enum Inventory implements TextProvider {
      Silent("Does actions all the time"),
      OpenInv("Only sorts while in inventory");

      public final String text;

      @Override
      public String getText() {
         return this.text;
      }

      Inventory(final String description) {
         this.text = description;
      }
   }

   private enum Mode implements TextProvider {
      Normal("Normal inventory cleaner"),
      CrystalPvP("CrystalPvP cleaner");

      public final String text;

      Mode(final String description) {
         this.text = description;
      }

      @Override
      public String getText() {
         return this.text;
      }
   }
}
