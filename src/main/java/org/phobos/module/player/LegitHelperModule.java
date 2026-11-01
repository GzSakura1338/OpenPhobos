package org.phobos.module.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import org.earthhack.ducks.ISlot;
import org.phobos.api.NumberRange;
import org.phobos.api.NumberRange;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.MutableNumberSetting;
import org.phobos.core.setting.ItemSetSetting;
import org.phobos.core.setting.BindSetting;
import org.phobos.core.math.FloatPair;
import org.phobos.core.inventory.ClickScheduler;
import org.phobos.core.inventory.ScheduledClick;
import org.phobos.core.bind.BindKey;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.core.bind.KeyCombination;
import org.phobos.core.PhobosClient;
import org.phobos.core.inventory.ActionTimer;
import org.phobos.core.module.Categories;
import org.phobos.core.tick.TimeUtils;
import org.phobos.core.inventory.InventoryUtils;
import org.phobos.core.PhobosClient.InitEvent;
import org.phobos.event.Listener;
import org.phobos.event.OnMouseButtonEvent;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent2;
import org.phobos.module.Module;

public class LegitHelperModule extends Module {
   public final LegitHelperModule.SwapMacro swapMacro;
   public final LegitHelperModule.SwapEntry swapEntry;
   public final Setting<KeyCombination> elytraSwap;
   public final Setting<KeyCombination> shieldSwap;
   public final Setting<KeyCombination> maceSwap = this.resolveSetting2(
      new BindSetting("MaceSwap").resolveSetting7("Swaps sword & anchors and glowstone to mace & elytra and fireworks")
   );
   public final Setting<NumberRange.Mutable<Float>> speed;
   public final LegitHelperModule.SwapEntry swapEntry2;
   public static LegitHelperModule legitHelperModule;
   public final LegitHelperModule.SwapEntry swapEntry3;

   @Override
   public void run3() {
      this.swapEntry2.run();
      this.swapEntry.run();
   }

   public LegitHelperModule(PhobosClient phobosClient) {
      super(phobosClient, "LegitHelper", Categories.player, "Helps you in vanilla pvp");
      this.elytraSwap = this.resolveSetting2(new BindSetting("ElytraSwap").resolveSetting7("Swaps anchors & glowstone with elytra & fireworks"));
      this.shieldSwap = this.resolveSetting2(new BindSetting("ShieldSwap").resolveSetting7("Swaps shield with axe"));
      this.speed = this.resolveSetting2(new MutableNumberSetting<>("Speed", NumberRange.createMutable(50.0F, 150.0F), 10.0F, 200.0F).resolveSetting7("Aim speed"));
      this.swapEntry2 = new LegitHelperModule.SwapEntry()
         .resolveSwapEntry2(LegitHelperModule.SwapAction.createSwapAction4(itemStack -> itemStack.getItem() instanceof SwordItem, InventoryUtils::testItemStack2))
         .resolveSwapEntry2(
            LegitHelperModule.SwapAction.createSwapAction4(
               itemStack -> itemStack.isOf(Blocks.RESPAWN_ANCHOR.asItem()), itemStack -> itemStack.getItem() instanceof ElytraItem
            )
         )
         .resolveSwapEntry2(
            LegitHelperModule.SwapAction.createSwapAction4(
               itemStack -> itemStack.isOf(Blocks.GLOWSTONE.asItem()), itemStack -> itemStack.getItem() instanceof FireworkRocketItem
            )
         );
      this.swapEntry = new LegitHelperModule.SwapEntry()
         .resolveSwapEntry2(
            LegitHelperModule.SwapAction.createSwapAction4(
               itemStack -> itemStack.isOf(Blocks.RESPAWN_ANCHOR.asItem()), itemStack -> itemStack.getItem() instanceof ElytraItem
            )
         )
         .resolveSwapEntry2(
            LegitHelperModule.SwapAction.createSwapAction4(
               itemStack -> itemStack.isOf(Blocks.GLOWSTONE.asItem()), itemStack -> itemStack.getItem() instanceof FireworkRocketItem
            )
         );
      this.swapEntry3 = new LegitHelperModule.SwapEntry()
         .resolveSwapEntry2(
            LegitHelperModule.SwapAction.createSwapAction4(itemStack -> itemStack.isOf(Items.SHIELD), itemStack -> itemStack.getItem() instanceof AxeItem)
         );
      this.swapMacro = new LegitHelperModule.SwapMacro()
         .resolveSwapMacro(this.maceSwap, this.swapEntry2)
         .resolveSwapMacro(this.elytraSwap, this.swapEntry)
         .resolveSwapMacro(this.shieldSwap, this.swapEntry3);
      final Map<LegitHelperModule.SwapEntry, Setting<Set<Item>>> map = new HashMap<>();

      for (int i = 1; i < 10; i++) {
         Setting<KeyCombination> setting = this.resolveSetting2(new BindSetting("Slot-%sSwap".formatted(i)));
         Setting<Set<Item>> setting2 = this.resolveSetting2(new ItemSetSetting("Slot-%sItems".formatted(i)).resolveSetting7("Allowed sizes: 2, 4, 6, 8"));
         this.resolveSettingGroup2("Slot-%s".formatted(i), setting, setting2);
         LegitHelperModule.SwapEntry swapEntry = new LegitHelperModule.SwapEntry().resolveSwapEntry(() -> {
            int ix = setting2.getValue().size();
            return ix > 0 && ix <= 8 && ix % 2 == 0;
         });
         this.swapMacro.resolveSwapMacro(setting, swapEntry);
         setting2.handleConsumer2(set -> this.handleSet(set, swapEntry));
         map.put(swapEntry, setting2);
      }

      this.handleListener(new Listener<InitEvent>() {
         public void handleObject2(InitEvent initEvent) {
            for (Entry<LegitHelperModule.SwapEntry, Setting<Set<Item>>> entry : map.entrySet()) {
               LegitHelperModule.this.handleSet(entry.getValue().getValue(), entry.getKey());
            }
         }
      });
      this.handleListener(
         new SafeListener<OnMouseButtonEvent>() {
            public void handleObject(
               OnMouseButtonEvent onMouseButtonEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (mc.currentScreen == null && onMouseButtonEvent.getIntValue() == 1) {
                  BindKey bindKey = phobosClient.getBindManager()
                     .resolveBindKey(onMouseButtonEvent.getKeyboardMouseEnum(), onMouseButtonEvent.getIntValue2());
                  if (bindKey != null) {
                     phobosClient.getInventoryManager()
                        .handleConsumer2(itemSwitcher -> LegitHelperModule.this.swapMacro.handleBindKey(bindKey, itemSwitcher));
                  }
               }
            }
         }
      );
      this.handleListener(
         new SafeListener<TickEvent2>() {
            public void handleObject(
               TickEvent2 tickEvent2,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (LegitHelperModule.this.swapMacro.check()) {
                  if (mc.currentScreen instanceof ActionTimer actionTimer && actionTimer.testInt(2)) {
                     clientPlayerEntity.closeScreen();
                  }
               } else {
                  if (!(mc.currentScreen instanceof ActionTimer)) {
                     mc.setScreen(new ActionTimer(clientPlayerEntity, 2));
                  }

                  if (!ClickScheduler.getINSTANCE().isFlag()) {
                     phobosClient.getInventoryManager().handleConsumer2(LegitHelperModule.this.swapMacro::handleItemSwitcher);
                  }
               }
            }
         }
      );
      legitHelperModule = this;
   }

   public void handleSet(Set<Item> set, LegitHelperModule.SwapEntry swapEntry) {
      List<Item> list = new ArrayList<>(set);
      int i = set.size();
      swapEntry.getSwapEntry();
      if (i > 0 && i <= 8 && i % 2 == 0) {
         switch (i) {
            case 2:
               swapEntry.resolveSwapEntry2(
                  LegitHelperModule.SwapAction.createSwapAction4(itemStack -> itemStack.isOf(list.getFirst()), itemStack -> itemStack.isOf(list.getLast()))
               );
            case 3:
            case 5:
            case 7:
            default:
               break;
            case 4:
               swapEntry.resolveSwapEntry2(
                     LegitHelperModule.SwapAction.createSwapAction4(itemStack -> itemStack.isOf(list.getFirst()), itemStack -> itemStack.isOf(list.get(1)))
                  )
                  .resolveSwapEntry2(
                     LegitHelperModule.SwapAction.createSwapAction4(itemStack -> itemStack.isOf(list.get(2)), itemStack -> itemStack.isOf(list.getLast()))
                  );
               break;
            case 6:
               swapEntry.resolveSwapEntry2(
                     LegitHelperModule.SwapAction.createSwapAction4(itemStack -> itemStack.isOf(list.getFirst()), itemStack -> itemStack.isOf(list.get(1)))
                  )
                  .resolveSwapEntry2(
                     LegitHelperModule.SwapAction.createSwapAction4(itemStack -> itemStack.isOf(list.get(2)), itemStack -> itemStack.isOf(list.get(3)))
                  )
                  .resolveSwapEntry2(
                     LegitHelperModule.SwapAction.createSwapAction4(itemStack -> itemStack.isOf(list.get(4)), itemStack -> itemStack.isOf(list.getLast()))
                  );
               break;
            case 8:
               swapEntry.resolveSwapEntry2(
                     LegitHelperModule.SwapAction.createSwapAction4(itemStack -> itemStack.isOf(list.getFirst()), itemStack -> itemStack.isOf(list.get(1)))
                  )
                  .resolveSwapEntry2(
                     LegitHelperModule.SwapAction.createSwapAction4(itemStack -> itemStack.isOf(list.get(2)), itemStack -> itemStack.isOf(list.get(3)))
                  )
                  .resolveSwapEntry2(
                     LegitHelperModule.SwapAction.createSwapAction4(itemStack -> itemStack.isOf(list.get(4)), itemStack -> itemStack.isOf(list.get(5)))
                  )
                  .resolveSwapEntry2(
                     LegitHelperModule.SwapAction.createSwapAction4(itemStack -> itemStack.isOf(list.get(6)), itemStack -> itemStack.isOf(list.getLast()))
                  );
         }
      }
   }

   private record SwapAction(Predicate<ItemStack> item1, Predicate<ItemStack> item2) {

      public static LegitHelperModule.SwapAction createSwapAction(Item item, Predicate<ItemStack> predicate) {
         return new LegitHelperModule.SwapAction(itemStack -> itemStack.isOf(item), predicate);
      }

      public boolean testItemSwitcher(ItemSwitcher itemSwitcher, boolean flag) {
         return flag ? this.testItemSwitcher2(itemSwitcher, this.item1, this.item2) : this.testItemSwitcher2(itemSwitcher, this.item2, this.item1);
      }

      public boolean testItemSwitcher2(ItemSwitcher itemSwitcher, Predicate<ItemStack> predicate, Predicate<ItemStack> predicate2) {
         Slot slot2 = itemSwitcher.resolveObject(slotx -> predicate.test(slotx.getStack()) ? slotx : null);
         if (slot2 == null) {
            return false;
         }

         if (!itemSwitcher.testSlot4(slot2)) {
            ISlot iSlot = ISlot.cast(slot2);
            if (iSlot.isLoaded()) {
               Slot slot = itemSwitcher.resolveObject(slotx -> predicate2.test(slotx.getStack()) && itemSwitcher.testSlot4(slotx) ? slotx : null);
               if (slot != null) {
                  ClickScheduler.getINSTANCE().handleSlot(slot2, new ScheduledClick(slot.getIndex(), SlotActionType.SWAP));
                  ClickScheduler.getINSTANCE()
                     .handleISlot(
                        iSlot,
                        FloatPair.createFloatPair(
                           LegitHelperModule.legitHelperModule.speed.getValue().getNumber2(), LegitHelperModule.legitHelperModule.speed.getValue().getNumber3()
                        )
                     );
                  return true;
               }
            }
         }

         return false;
      }

      public static LegitHelperModule.SwapAction createSwapAction2(Predicate<ItemStack> predicate, Item item) {
         return new LegitHelperModule.SwapAction(predicate, itemStack -> itemStack.isOf(item));
      }

      public Slot resolveSlot(ItemSwitcher itemSwitcher, boolean flag) {
         return flag
            ? itemSwitcher.resolveObject(slot -> this.item2.test(slot.getStack()) ? slot : null)
            : itemSwitcher.resolveObject(slot -> this.item1.test(slot.getStack()) ? slot : null);
      }

      public static LegitHelperModule.SwapAction createSwapAction3(Item item, Item item2) {
         return new LegitHelperModule.SwapAction(itemStack -> itemStack.isOf(item), itemStack -> itemStack.isOf(item2));
      }

      public static LegitHelperModule.SwapAction createSwapAction4(Predicate<ItemStack> predicate, Predicate<ItemStack> predicate2) {
         return new LegitHelperModule.SwapAction(predicate, predicate2);
      }
   }

   private static class SwapEntry {
      public final List<LegitHelperModule.SwapAction> swapActionList = new ArrayList<>();
      public long longValue;
      public int intValue = 0;
      public BooleanSupplier booleanSupplier;

      public boolean testItemSwitcher(ItemSwitcher itemSwitcher) {
         if (this.intValue != 0 && !TimeUtils.testLong2(this.longValue, 400L)) {
            switch (this.intValue) {
               case -1:
                  for (LegitHelperModule.SwapAction swapAction3 : this.swapActionList) {
                     if (swapAction3.resolveSlot(itemSwitcher, false) == null) {
                        this.intValue = 0;
                        return false;
                     }
                  }

                  for (LegitHelperModule.SwapAction swapAction4 : this.swapActionList) {
                     if (swapAction4.testItemSwitcher(itemSwitcher, true)) {
                        return true;
                     }
                  }

                  this.intValue = 0;
                  return false;
               case 1:
                  Iterator var2 = this.swapActionList.iterator();

                  LegitHelperModule.SwapAction swapAction;
                  do {
                     if (!var2.hasNext()) {
                        for (LegitHelperModule.SwapAction swapAction2 : this.swapActionList) {
                           if (swapAction2.testItemSwitcher(itemSwitcher, false)) {
                              return true;
                           }
                        }

                        this.intValue = 0;
                        return false;
                     }

                     swapAction = (LegitHelperModule.SwapAction)var2.next();
                  } while (swapAction.resolveSlot(itemSwitcher, true) != null);

                  this.intValue = 0;
            }

            return false;
         } else {
            return false;
         }
      }

      public LegitHelperModule.SwapEntry resolveSwapEntry(BooleanSupplier booleanSupplier) {
         this.booleanSupplier = booleanSupplier;
         return this;
      }

      public SwapEntry() {
      }

      public boolean check() {
         return this.intValue == 0;
      }

      public LegitHelperModule.SwapEntry resolveSwapEntry2(LegitHelperModule.SwapAction swapAction) {
         this.swapActionList.add(swapAction);
         return this;
      }

      public void handleItemSwitcher(ItemSwitcher itemSwitcher) {
         itemSwitcher.handleRunnable3(() -> {
            if (!this.swapActionList.isEmpty() && (this.booleanSupplier == null || this.booleanSupplier.getAsBoolean())) {
               LegitHelperModule.SwapAction swapAction = this.swapActionList.getFirst();
               if (swapAction.resolveSlot(itemSwitcher, false) != null) {
                  this.longValue = System.currentTimeMillis();
                  this.intValue = 1;
               } else if (swapAction.resolveSlot(itemSwitcher, true) != null) {
                  this.longValue = System.currentTimeMillis();
                  this.intValue = -1;
               } else {
                  this.intValue = 0;
               }
            } else {
               this.intValue = 0;
            }
         });
      }

      public void run() {
         this.intValue = 0;
      }

      public LegitHelperModule.SwapEntry getSwapEntry() {
         this.swapActionList.clear();
         return this;
      }
   }

   private static class SwapMacro {
      public final Map<Supplier<KeyCombination>, LegitHelperModule.SwapEntry> supplierMap = new HashMap<>();

      public void handleBindKey(BindKey bindKey, ItemSwitcher itemSwitcher) {
         for (Entry<Supplier<KeyCombination>, LegitHelperModule.SwapEntry> entry : this.supplierMap.entrySet()) {
            if (entry.getKey().get().getBindKeySet().contains(bindKey)) {
               entry.getValue().handleItemSwitcher(itemSwitcher);
            }
         }
      }

      public SwapMacro() {
      }

      public LegitHelperModule.SwapMacro resolveSwapMacro(Supplier<KeyCombination> supplier, LegitHelperModule.SwapEntry swapEntry) {
         this.supplierMap.put(supplier, swapEntry);
         return this;
      }

      public void handleItemSwitcher(ItemSwitcher itemSwitcher) {
         for (LegitHelperModule.SwapEntry swapEntry : this.supplierMap.values()) {
            if (swapEntry.testItemSwitcher(itemSwitcher)) {
               return;
            }
         }
      }

      public boolean check() {
         return this.supplierMap.values().stream().allMatch(LegitHelperModule.SwapEntry::check);
      }
   }
}
