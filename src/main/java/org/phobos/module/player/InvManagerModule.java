package org.phobos.module.player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import net.minecraft.block.CobwebBlock;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.AxeItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.EggItem;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.PotionItem;
import net.minecraft.item.SnowballItem;
import net.minecraft.item.SplashPotionItem;
import net.minecraft.item.SwordItem;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import org.apache.commons.lang3.mutable.MutableObject;
import org.phobos.api.TextProvider;
import org.phobos.api.TimeGate;
import org.phobos.api.NumberRange;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.EnumSetting;
import org.phobos.core.setting.MutableLongSetting;
import org.phobos.core.setting.BooleanSetting;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.core.PhobosClient;
import org.phobos.core.inventory.ItemUtils;
import org.phobos.core.module.Categories;
import org.phobos.core.block.BlockUtils;
import org.phobos.core.inventory.InventoryUtils;
import org.phobos.core.inventory.InventoryUtils.PotionUtil;
import org.phobos.event.SafeListener;
import org.phobos.event.TickEvent2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.phobos.module.Module;

public class InvManagerModule extends Module {
   public final Setting<InvManagerModule.Inventory> inventory = this.resolveSetting2(new EnumSetting<>("Inventory", InvManagerModule.Inventory.Silent));
   public final Setting<Boolean> stacker;
   public final TimeGate.Normal normal;
   public static final Logger LOGGER = LoggerFactory.getLogger(InvManagerModule.class);
   public final Setting<NumberRange.Mutable<Long>> delay = this.resolveSetting2(new MutableLongSetting("Delay", 50L, 0L, 5000L, true));
   public final Setting<Boolean> hotbar;

   public void handleClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, ItemSwitcher itemSwitcher, List<Setting<InvManagerModule.ItemType>>... lists) {
      Set<Integer> set = new HashSet<>();

      for (List<Setting<InvManagerModule.ItemType>> list : lists) {
         if (this.testClientPlayerEntity(clientPlayerEntity, itemSwitcher, integer -> list.get(integer).getValue(), set)) {
            break;
         }
      }
   }

   public boolean testClientPlayerEntity(
      ClientPlayerEntity clientPlayerEntity, ItemSwitcher itemSwitcher, Function<Integer, InvManagerModule.ItemType> function, Set<Integer> set
   ) {
      for (int i2 = 0; i2 < 9; i2++) {
         if (!set.contains(i2)) {
            InvManagerModule.ItemType itemType = function.apply(i2);
            Slot slot = clientPlayerEntity.playerScreenHandler.getSlot(ItemSwitcher.computeInt(i2));
            MutableObject<Slot> mutableObject = new MutableObject();
            itemSwitcher.resolveObject(slot2x -> {
               if (slot2x.equals(itemSwitcher.getSlot2()) || itemSwitcher.testSlot5(slot2x)) {
                  return null;
               }

               if (!itemType.apply(slot2x.getStack())) {
                  return null;
               }

               if (mutableObject.getValue() == null) {
                  mutableObject.setValue(slot2x);
                  return null;
               }

               ItemStack itemStack = ((Slot)mutableObject.getValue()).getStack();
               ItemStack itemStack2 = slot2x.getStack();
               Item item = itemStack.getItem();
               Item item2 = itemStack2.getItem();
               if (ItemUtils.testItem(item2) && ItemUtils.testItem2(item, item2)) {
                  float f = ItemUtils.computeItemStack(itemStack, item);
                  float f2 = ItemUtils.computeItemStack(itemStack2, item2);
                  if (f2 > f) {
                     mutableObject.setValue(slot2x);
                  }

                  return null;
               } else if (item instanceof BlockItem && item2 instanceof BlockItem) {
                  int ix = itemStack.getCount();
                  int i2x = itemStack2.getCount();
                  if (i2x > ix || i2x == ix && slot2x.equals(slot)) {
                     mutableObject.setValue(slot2x);
                  }

                  return null;
               } else {
                  FoodComponent foodComponent = (FoodComponent)item.getComponents().get(DataComponentTypes.FOOD);
                  FoodComponent foodComponent2 = (FoodComponent)item2.getComponents().get(DataComponentTypes.FOOD);
                  if (foodComponent != null && foodComponent2 != null) {
                     int i3 = InventoryUtils.computeFoodComponent(foodComponent);
                     int i4 = InventoryUtils.computeFoodComponent(foodComponent2);
                     if (i3 < i4) {
                        mutableObject.setValue(slot2x);
                     }

                     return null;
                  } else {
                     if (itemStack.getMaxCount() == itemStack2.getMaxCount() && itemStack.getCount() < itemStack2.getCount()) {
                        mutableObject.setValue(slot2x);
                     }

                     return null;
                  }
               }
            });
            if (mutableObject.getValue() != null) {
               Slot slot2 = (Slot)mutableObject.getValue();
               if (slot2.equals(slot)) {
                  set.add(i2);
               } else if (slot.getStack().isOf(slot2.getStack().getItem())
                  && slot2.getStack().getMaxCount() == slot.getStack().getMaxCount()
                  && slot2.getStack().getCount() == slot.getStack().getCount()) {
                  set.add(i2);
               } else {
                  int i = i2;
                  itemSwitcher.handleRunnable2(() -> itemSwitcher.handleInt5(((Slot)mutableObject.getValue()).id, i, SlotActionType.SWAP));
                  set.add(i2);
                  if (!this.delay.getValue().testNumber(0L)) {
                     return true;
                  }
               }
            }
         }
      }

      return false;
   }

   @Override
   public String nothing() {
      return String.valueOf(this.inventory.getValue());
   }

   public InvManagerModule(PhobosClient phobosClient) {
      super(phobosClient, "InvManager", Categories.player, "Manages inventory layout");
      this.stacker = this.resolveSetting2(new BooleanSetting("Stacker", false));
      this.hotbar = this.resolveSetting2(new BooleanSetting("Hotbar", false));
      this.normal = new TimeGate.Normal();
      final List<Setting<InvManagerModule.ItemType>> list = new ArrayList<>();
      final List<Setting<InvManagerModule.ItemType>> list2 = new ArrayList<>();

      for (int i = 1; i <= 9; i++) {
         list.add(this.resolveSetting2(new EnumSetting<>("Layout" + i, InvManagerModule.ItemType.Free)));
         list2.add(this.resolveSetting2(new EnumSetting<>("Fallback" + i, InvManagerModule.ItemType.Free)));
      }

      this.createBuilder()
         .resolveBuilder6(this.hotbar)
         .resolveBuilder5(
            this.resolveSettingGroup2("Layout", list.toArray(new Setting[0])), this.resolveSettingGroup2("Fallback", list2.toArray(new Setting[0]))
         )
         .getSettingSubsection();
      this.handleListener(
         new SafeListener<TickEvent2>() {
            public void handleObject(
               TickEvent2 tickEvent2,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               if (!InvManagerModule.this.inventory.testObject2(InvManagerModule.Inventory.OpenInv) || mc.currentScreen instanceof InventoryScreen) {
                  if (InvManagerModule.this.normal.testSetting(InvManagerModule.this.delay)) {
                     InvManagerModule.this.delay.getValue().run6();
                     phobosClient.getInventoryManager().handleConsumer2(itemSwitcher -> {
                        if (InvManagerModule.this.stacker.getValue()) {
                           MutableObject<Slot> mutableObject = new MutableObject(null);
                           Slot slot = itemSwitcher.resolveObject(slotx -> {
                              if (itemSwitcher.testSlot4(slotx)) {
                                 return null;
                              } else if (slotx.getStack().isEmpty()) {
                                 return null;
                              } else {
                                 return slotx.getStack().getCount() < slotx.getStack().getMaxCount() ? itemSwitcher.resolveObject(slot2 -> {
                                    if (!slot2.getStack().isOf(slotx.getStack().getItem())) {
                                       return null;
                                    } else if (slot2.equals(slotx)) {
                                       return null;
                                    } else if (!ItemStack.areItemsAndComponentsEqual(slot2.getStack(), slotx.getStack())) {
                                       return null;
                                    } else if (slot2.getStack().getCount() < slot2.getStack().getMaxCount()) {
                                       mutableObject.setValue(slotx);
                                       return slot2;
                                    } else {
                                       return null;
                                    }
                                 }) : null;
                              }
                           });
                           if (slot != null) {
                              itemSwitcher.handleRunnable2(() -> {
                                 itemSwitcher.handleInt5(slot.id, 0, SlotActionType.PICKUP);
                                 itemSwitcher.handleInt5(slot.id, 0, SlotActionType.PICKUP_ALL);
                                 itemSwitcher.handleInt5(slot.id, 0, SlotActionType.PICKUP);
                              });
                           }
                        }

                        if (InvManagerModule.this.hotbar.getValue()) {
                           InvManagerModule.this.handleClientPlayerEntity(clientPlayerEntity, itemSwitcher, list, list2);
                        }
                     });
                  }
               }
            }
         }
      );
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

   private enum ItemType implements TextProvider {
      Sword("Sword") {
         @Override
         public boolean apply(ItemStack stack) {
            return stack.getItem() instanceof SwordItem;
         }
      },
      Axe("Axe") {
         @Override
         public boolean apply(ItemStack stack) {
            return stack.getItem() instanceof AxeItem;
         }
      },
      Pickaxe("Pickaxe") {
         @Override
         public boolean apply(ItemStack stack) {
            return stack.getItem() instanceof PickaxeItem;
         }
      },
      Rod("Fishing rod") {
         @Override
         public boolean apply(ItemStack stack) {
            return stack.getItem() instanceof FishingRodItem;
         }
      },
      Block("Blocks") {
         @Override
         public boolean apply(ItemStack stack) {
            return BlockUtils.testItem(stack.getItem());
         }
      },
      Food("Food") {
         @Override
         public boolean apply(ItemStack stack) {
            return InventoryUtils.testItem3(stack.getItem());
         }
      },
      Pearl("Ender pearl") {
         @Override
         public boolean apply(ItemStack stack) {
            return stack.isOf(Items.ENDER_PEARL);
         }
      },
      Throwable("Eggs & Snowballs") {
         @Override
         public boolean apply(ItemStack stack) {
            Item item = stack.getItem();
            return item instanceof EggItem || item instanceof SnowballItem;
         }
      },
      Bow("Bow") {
         @Override
         public boolean apply(ItemStack stack) {
            return stack.isOf(Items.BOW);
         }
      },
      PotionH("Harmful drinkable potion") {
         @Override
         public boolean apply(ItemStack stack) {
            return stack.getItem() instanceof PotionItem potion && !(potion instanceof SplashPotionItem);
         }
      },
      PotionB("Beneficial drinkable potion") {
         @Override
         public boolean apply(ItemStack stack) {
            return stack.getItem() instanceof PotionItem potion && !(potion instanceof SplashPotionItem);
         }
      },
      SplashH("Harmful splash potion") {
         @Override
         public boolean apply(ItemStack stack) {
            return stack.getItem() instanceof SplashPotionItem && PotionUtil.testItemStack2(stack);
         }
      },
      SplashB("Beneficial splash potion") {
         @Override
         public boolean apply(ItemStack stack) {
            return stack.getItem() instanceof SplashPotionItem && PotionUtil.testItemStack(stack);
         }
      },
      Water("Water bucket") {
         @Override
         public boolean apply(ItemStack stack) {
            return stack.isOf(Items.WATER_BUCKET);
         }
      },
      Lava("Lava bucket") {
         @Override
         public boolean apply(ItemStack stack) {
            return stack.isOf(Items.LAVA_BUCKET);
         }
      },
      FlintNSteel("Flint and Steel") {
         @Override
         public boolean apply(ItemStack stack) {
            return stack.isOf(Items.FLINT_AND_STEEL);
         }
      },
      Fireball("Fireball") {
         @Override
         public boolean apply(ItemStack stack) {
            return stack.isOf(Items.FIRE_CHARGE);
         }
      },
      Cobweb("Cobweb") {
         @Override
         public boolean apply(ItemStack stack) {
            return stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof CobwebBlock;
         }
      },
      XP("XP") {
         @Override
         public boolean apply(ItemStack stack) {
            return stack.isOf(Items.EXPERIENCE_BOTTLE);
         }
      },
      Crystal("Crystal") {
         @Override
         public boolean apply(ItemStack stack) {
            return stack.isOf(Items.END_CRYSTAL);
         }
      },
      Chorus("Chorus") {
         @Override
         public boolean apply(ItemStack stack) {
            return stack.isOf(Items.CHORUS_FRUIT);
         }
      },
      EC("EC") {
         @Override
         public boolean apply(ItemStack stack) {
            return stack.isOf(Items.ENDER_CHEST);
         }
      },
      Free("Any item") {
         @Override
         public boolean apply(ItemStack stack) {
            return true;
         }
      },
      Empty("Empty slot") {
         @Override
         public boolean apply(ItemStack stack) {
            return stack.isEmpty();
         }
      };

      public final String text;

      @Override
      public String getText() {
         return this.text;
      }

      ItemType(final String description) {
         this.text = description;
      }

      public abstract boolean apply(ItemStack var1);
   }
}
