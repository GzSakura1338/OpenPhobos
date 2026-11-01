package org.phobos.core.inventory;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.AxeItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.slot.Slot;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.Nullable;
import org.phobos.module.combat.AutoArmorModule;
import org.phobos.module.combat.AutoArmorModule.ArmorPriority;
import org.phobos.core.block.BlockUtils;

public final class ItemUtils {
   public static boolean testItemSwitcher(ItemSwitcher itemSwitcher, ItemStack itemStack, Slot slot) {
      return itemSwitcher.resolveObject(slot2 -> !slot2.equals(slot) && testItemStack2(slot2.getStack(), itemStack) ? slot2 : null) != null;
   }

   public static boolean testItemStack(ItemStack itemStack, ItemStack itemStack2) {
      return EnchantmentUtils.resolveMap(itemStack).equals(EnchantmentUtils.resolveMap(itemStack2));
   }

   public static ToolItem resolveToolItem(ToolItem toolItem, ToolItem toolItem2, ItemStack itemStack, ItemStack itemStack2) {
      float f = computeToolItem(toolItem, itemStack);
      float f2 = computeToolItem(toolItem2, itemStack2);
      if (f > f2) {
         return toolItem;
      } else if (f < f2) {
         return toolItem2;
      } else {
         return toolItem.getMaterial().getDurability() >= toolItem2.getMaterial().getDurability() ? toolItem : toolItem2;
      }
   }

   public static boolean testItemSwitcher2(ItemSwitcher itemSwitcher, ItemStack itemStack, Slot slot) {
      return itemSwitcher.resolveObject(
            slot2 -> !slot2.equals(slot) && testItemStack3(itemStack, slot2.getStack()) && testItemStack2(itemStack, slot2.getStack()) ? slot2 : null
         )
         != null;
   }

   public static boolean testItemSwitcher3(ItemSwitcher itemSwitcher, Slot slot, Supplier<Set<Item>> supplier) {
      Set<Item> set = supplier.get();
      ItemStack itemStack = slot.getStack();
      if (itemStack.isEmpty()) {
         return false;
      }

      Item item = itemStack.getItem();
      if (item == null) {
         return false;
      }

      if (item instanceof BlockItem && !set.contains(item)) {
         return !BlockUtils.testItem(item);
      }

      if (testItem(item)) {
         if (testItemSwitcher(itemSwitcher, itemStack, slot)) {
            return true;
         }

         if (testItemSwitcher5(itemSwitcher, item, itemStack)) {
            return true;
         }
      }

      return set.contains(item) ? false : testItemSwitcher5(itemSwitcher, item, itemStack);
   }

   public static boolean testItemStack2(ItemStack itemStack, ItemStack itemStack2) {
      return itemStack.getItem().equals(itemStack2.getItem()) && testItemStack5(itemStack, itemStack2);
   }

   public static boolean testItem(Item item) {
      return switch (item) {
         case SwordItem swordItem -> true;
         case ToolItem toolItem -> true;
         case ArmorItem armorItem -> true;
         case BowItem bowItem -> true;
         case FishingRodItem fishingRodItem -> true;
         default -> false;
      };
   }

   public static boolean testItemSwitcher4(ItemSwitcher itemSwitcher, Item item, ItemStack itemStack, Slot slot) {
      if (itemSwitcher != null && item != null && itemStack != null) {
         switch (item) {
            case SwordItem swordItem:
               float f = computeSwordItem(swordItem, itemStack);
               Slot slot2 = itemSwitcher.resolveObject(
                  slot2x -> !Objects.equals(slot2x, slot)
                        && slot2x.getStack().getItem() instanceof SwordItem swordItem2
                        && testToolItem(swordItem, swordItem2)
                        && computeSwordItem(swordItem, slot2x.getStack()) > f
                     ? slot2x
                     : null
               );
               return slot2 != null;
            case ToolItem toolItem:
               float f2 = computeToolItem(toolItem, itemStack);
               Slot slot3 = itemSwitcher.resolveObject(
                  slot2Arg -> !Objects.equals(slot2Arg, slot)
                        && slot2Arg.getStack().getItem() instanceof ToolItem toolItem2
                        && testToolItem(toolItem, toolItem2)
                        && computeToolItem(toolItem2, slot2Arg.getStack()) > f2
                     ? slot2Arg
                     : null
               );
               return slot3 != null;
            case ArmorItem armorItem:
               float f3 = ItemUtils.Armor.computeItemStack(itemStack);
               Slot slot4 = itemSwitcher.resolveObject(
                  slot2Arg -> !Objects.equals(slot2Arg, slot)
                        && slot2Arg.getStack().getItem() instanceof ArmorItem armorItem2
                        && armorItem2.getSlotType().equals(armorItem.getSlotType())
                        && computeItemStack3(slot2Arg.getStack()) > f3
                     ? slot2Arg
                     : null
               );
               return slot4 != null;
            case BowItem bowItem:
               float f4 = computeItemStack2(itemStack);
               Slot slot5 = itemSwitcher.resolveObject(
                  slot2Arg -> !Objects.equals(slot2Arg, slot) && slot2Arg.getStack().getItem() instanceof BowItem && computeItemStack2(slot2Arg.getStack()) > f4
                     ? slot2Arg
                     : null
               );
               return slot5 != null;
            case FishingRodItem fishingRodItem:
               float f5 = computeItemStack2(itemStack);
               Slot slot6 = itemSwitcher.resolveObject(
                  slot2Arg -> !Objects.equals(slot2Arg, slot) && slot2Arg.getStack().getItem() instanceof FishingRodItem && computeItemStack2(slot2Arg.getStack()) > f5
                     ? slot2Arg
                     : null
               );
               return slot6 != null;
            default:
               return false;
         }
      } else {
         return false;
      }
   }

   public static float computeItemStack(ItemStack itemStack, Item item) {
      return switch (item) {
         case SwordItem swordItem -> computeSwordItem(swordItem, itemStack);
         case ToolItem toolItem -> computeToolItem(toolItem, itemStack);
         case ArmorItem armorItem -> ItemUtils.Armor.computeItemStack(itemStack);
         case BowItem bowItem -> computeItemStack2(itemStack);
         case FishingRodItem fishingRodItem -> computeItemStack2(itemStack);
         default -> 0.0F;
      };
   }

   public static boolean testToolItem(ToolItem toolItem, ToolItem toolItem2) {
      return toolItem instanceof SwordItem && toolItem2 instanceof SwordItem
         || toolItem instanceof PickaxeItem && toolItem2 instanceof PickaxeItem
         || toolItem instanceof AxeItem && toolItem2 instanceof AxeItem
         || toolItem instanceof ShovelItem && toolItem2 instanceof ShovelItem
         || toolItem instanceof HoeItem && toolItem2 instanceof HoeItem;
   }

   public static boolean testItem2(Item item, Item item2) {
      return item.getClass() == item2.getClass();
   }

   public static float computeToolItem(ToolItem toolItem, ItemStack itemStack) {
      float f = InventoryUtils.computeItemStack(itemStack);
      float f2 = toolItem.getMaterial().getMiningSpeedMultiplier();
      float f3 = f / 100.0F;
      float f4 = f2 * f3;
      if (!EnchantmentUtils.testItemStack(itemStack, Enchantments.EFFICIENCY)) {
         return f4;
      }

      int i = EnchantmentUtils.computeItemStack3(itemStack, Enchantments.EFFICIENCY);
      return f4 * i;
   }

   public static boolean testList(List<Slot> list, Item item, ItemStack itemStack, Slot slot) {
      if (item != null && itemStack != null) {
         switch (item) {
            case SwordItem swordItem:
               float f = computeSwordItem(swordItem, itemStack);
               Slot slot2 = resolveSlot(
                  list,
                  slot2x -> !Objects.equals(slot2x, slot)
                        && slot2x.getStack().getItem() instanceof SwordItem swordItem2
                        && testToolItem(swordItem, swordItem2)
                        && computeSwordItem(swordItem2, slot2x.getStack()) > f
                     ? slot2x
                     : null
               );
               return slot2 != null;
            case ToolItem toolItem:
               float f2 = computeToolItem(toolItem, itemStack);
               Slot slot3 = resolveSlot(
                  list,
                  slot2Arg -> !Objects.equals(slot2Arg, slot)
                        && slot2Arg.getStack().getItem() instanceof ToolItem toolItem2
                        && testToolItem(toolItem, toolItem2)
                        && computeToolItem(toolItem2, slot2Arg.getStack()) > f2
                     ? slot2Arg
                     : null
               );
               return slot3 != null;
            case ArmorItem armorItem:
               float f3 = ItemUtils.Armor.computeItemStack(itemStack);
               Slot slot4 = resolveSlot(
                  list,
                  slot2Arg -> !Objects.equals(slot2Arg, slot)
                        && slot2Arg.getStack().getItem() instanceof ArmorItem armorItem2
                        && armorItem2.getSlotType().equals(armorItem.getSlotType())
                        && ItemUtils.Armor.computeItemStack(slot2Arg.getStack()) > f3
                     ? slot2Arg
                     : null
               );
               return slot4 != null;
            case BowItem bowItem:
               float f4 = computeItemStack2(itemStack);
               Slot slot5 = resolveSlot(
                  list,
                  slot2Arg -> !Objects.equals(slot2Arg, slot) && slot2Arg.getStack().getItem() instanceof BowItem && computeItemStack2(slot2Arg.getStack()) > f4
                     ? slot2Arg
                     : null
               );
               return slot5 != null;
            case FishingRodItem fishingRodItem:
               float f5 = computeItemStack2(itemStack);
               Slot slot6 = resolveSlot(
                  list,
                  slot2Arg -> !Objects.equals(slot2Arg, slot) && slot2Arg.getStack().getItem() instanceof FishingRodItem && computeItemStack2(slot2Arg.getStack()) > f5
                     ? slot2Arg
                     : null
               );
               return slot6 != null;
            default:
               return false;
         }
      } else {
         return false;
      }
   }

   public static boolean testList2(List<Slot> list, ItemStack itemStack, Slot slot) {
      return resolveSlot(
            list, slot2 -> !slot2.equals(slot) && testItemStack3(itemStack, slot2.getStack()) && testItemStack2(itemStack, slot2.getStack()) ? slot2 : null
         )
         != null;
   }

   public static boolean testItemStack3(ItemStack itemStack, ItemStack itemStack2) {
      return !itemStack2.isOf(itemStack.getItem()) ? false : itemStack.getMaxCount() == 1 && itemStack2.getMaxCount() == 1;
   }

   public static ItemStack resolveItemStack(ItemStack itemStack, ItemStack itemStack2) {
      return computeItemStack2(itemStack) > computeItemStack2(itemStack2) ? itemStack : itemStack2;
   }

   public static boolean testItemSwitcher5(ItemSwitcher itemSwitcher, Item item, ItemStack itemStack) {
      if (itemSwitcher != null && item != null && itemStack != null) {
         switch (item) {
            case ToolItem toolItem:
               Slot slot = itemSwitcher.resolveObject(
                  slotx -> slotx.getStack().getItem() instanceof ToolItem toolItem2
                        && !Objects.equals(toolItem, toolItem2)
                        && testToolItem(toolItem, toolItem2)
                        && resolveToolItem(toolItem, toolItem2, itemStack, slotx.getStack()) == toolItem2
                     ? slotx
                     : null
               );
               return slot != null;
            case ArmorItem armorItem:
               Slot slot2 = itemSwitcher.resolveObject(
                  slotArg -> slotArg.getStack().getItem() instanceof ArmorItem armorItem2
                        && !Objects.equals(armorItem, armorItem2)
                        && armorItem2.getSlotType().equals(armorItem.getSlotType())
                        && resolveArmorItem(armorItem, armorItem2, itemStack, slotArg.getStack()) == armorItem2
                     ? slotArg
                     : null
               );
               return slot2 != null;
            case BowItem bowItem:
               Slot slot3 = itemSwitcher.resolveObject(
                  slotArg -> slotArg.getStack().getItem() instanceof BowItem bowItem2
                        && !Objects.equals(bowItem, bowItem2)
                        && resolveItemStack(itemStack, slotArg.getStack()) == slotArg.getStack()
                     ? slotArg
                     : null
               );
               return slot3 != null;
            case FishingRodItem fishingRodItem:
               Slot slot4 = itemSwitcher.resolveObject(
                  slotArg -> slotArg.getStack().getItem() instanceof FishingRodItem fishingRodItem2
                        && !Objects.equals(fishingRodItem, fishingRodItem2)
                        && resolveItemStack(itemStack, slotArg.getStack()) == slotArg.getStack()
                     ? slotArg
                     : null
               );
               return slot4 != null;
            default:
               return false;
         }
      } else {
         return false;
      }
   }

   public static int computeItemStack2(ItemStack itemStack) {
      MutableInt mutableInt = new MutableInt(0);

      for (int i : EnchantmentUtils.resolveMap(itemStack).values()) {
         mutableInt.add(i);
      }

      return mutableInt.getValue();
   }

   public static SwordItem resolveSwordItem(SwordItem swordItem, SwordItem swordItem2, ItemStack itemStack, ItemStack itemStack2) {
      float f = computeSwordItem(swordItem, itemStack);
      float f2 = computeSwordItem(swordItem2, itemStack2);
      if (f > f2) {
         return swordItem;
      } else if (f < f2) {
         return swordItem2;
      } else {
         return swordItem.getMaterial().getDurability() >= swordItem2.getMaterial().getDurability() ? swordItem : swordItem2;
      }
   }

   public static float computeSwordItem(SwordItem swordItem, ItemStack itemStack) {
      float f = InventoryUtils.computeItemStack(itemStack);
      float f2 = swordItem.getMaterial().getAttackDamage();
      float f3 = f / 100.0F;
      float f4 = f2 * f3;
      if (!EnchantmentUtils.testItemStack(itemStack, Enchantments.SHARPNESS)) {
         return f4;
      }

      int i = EnchantmentUtils.computeItemStack3(itemStack, Enchantments.SHARPNESS);
      return f4 * i;
   }

   @Nullable
   public static Slot resolveSlot(List<Slot> list, Function<Slot, Slot> function) {
      for (Slot slot2 : list) {
         Slot slot = function.apply(slot2);
         if (slot != null) {
            return slot;
         }
      }

      return null;
   }

   public static int computeItemStack3(ItemStack itemStack) {
      return ItemUtils.Armor.computeItemStack(itemStack);
   }

   public static ArmorItem resolveArmorItem(ArmorItem armorItem, ArmorItem armorItem2, ItemStack itemStack, ItemStack itemStack2) {
      float f = computeItemStack3(itemStack);
      float f2 = computeItemStack3(itemStack2);
      if (f > f2) {
         return armorItem;
      } else if (f < f2) {
         return armorItem2;
      } else {
         return InventoryUtils.computeItemStack(itemStack) >= InventoryUtils.computeItemStack(itemStack2) ? armorItem : armorItem2;
      }
   }

   public ItemUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static boolean testItemStack4(ItemStack itemStack) {
      return EnchantmentUtils.resolveMap(itemStack).isEmpty();
   }

   public static int computeArmorItem(ArmorItem armorItem) {
      RegistryEntry<ArmorMaterial> registryEntry = armorItem.getMaterial();
      if (registryEntry.equals(ArmorMaterials.LEATHER)) {
         return 0;
      } else if (registryEntry.equals(ArmorMaterials.CHAIN)) {
         return 1;
      } else if (registryEntry.equals(ArmorMaterials.IRON) || registryEntry.equals(ArmorMaterials.GOLD)) {
         return 2;
      } else if (registryEntry.equals(ArmorMaterials.DIAMOND)) {
         return 3;
      } else {
         return registryEntry.equals(ArmorMaterials.NETHERITE) ? 4 : 0;
      }
   }

   public static boolean testItemSwitcher6(ItemSwitcher itemSwitcher, ItemStack itemStack) {
      return itemStack.getItem() instanceof ArmorItem armorItem
         ? itemSwitcher.resolveObject(
               slot -> slot.getStack().getItem() instanceof ArmorItem armorItem2x
                     && ((ArmorMaterial)armorItem2x.getMaterial().value()).equals(armorItem.getMaterial().value())
                     && testItemStack5(slot.getStack(), itemStack)
                  ? slot
                  : null
            )
            != null
         : false;
   }

   public static boolean testItemStack5(ItemStack itemStack, ItemStack itemStack2) {
      return testItemStack(itemStack, itemStack2) || testItemStack4(itemStack) && testItemStack4(itemStack2);
   }

   public interface Armor {
      static int computeItemStack(ItemStack itemStack) {
         ArmorPriority armorPriority = AutoArmorModule.getArmorPriority();
         if (itemStack.getItem() instanceof ArmorItem armorItem) {
            int var4 = 0;
            if (EnchantmentUtils.testItemStack(itemStack, Enchantments.PROTECTION)) {
               var4 += EnchantmentUtils.computeItemStack3(itemStack, Enchantments.PROTECTION) * (armorPriority.equals(ArmorPriority.Protection) ? 2 : 1) * 10;
            }

            if (EnchantmentUtils.testItemStack(itemStack, Enchantments.BLAST_PROTECTION)) {
               var4 += EnchantmentUtils.computeItemStack3(itemStack, Enchantments.BLAST_PROTECTION) * (armorPriority.equals(ArmorPriority.Blast) ? 2 : 1) * 10;
            }

            if (EnchantmentUtils.testItemStack(itemStack, Enchantments.UNBREAKING)) {
               var4 += EnchantmentUtils.computeItemStack3(itemStack, Enchantments.UNBREAKING);
            }

            if (EnchantmentUtils.testItemStack(itemStack, Enchantments.MENDING)) {
               var4 *= 2;
            }

            return armorItem.getProtection() + (int)Math.ceil(armorItem.getToughness()) * 100 + var4;
         } else {
            return 0;
         }
      }
   }

   private static class ComparisonResult {
      public final ItemStack itemStack;
      public final ItemUtils.ComparisonResult.Result result;

      public ComparisonResult(ItemStack itemStack, ItemUtils.ComparisonResult.Result result) {
         this.itemStack = itemStack;
         this.result = result;
      }

      public ItemUtils.ComparisonResult.Result getResult() {
         return this.result;
      }

      public ItemStack getItemStack() {
         return this.itemStack;
      }

      public static ItemUtils.ComparisonResult createComparisonResult(
         ItemStack itemStack, ItemStack itemStack2, BiFunction<ItemStack, ItemStack, @Nullable ItemStack> biFunction, ItemStack... itemStacks
      ) {
         ItemStack itemStack3 = biFunction.apply(itemStack, itemStack2);
         if (itemStack3 == null) {
            return new ItemUtils.ComparisonResult(itemStacks.length == 0 ? null : itemStacks[0], ItemUtils.ComparisonResult.Result.TIE);
         } else {
            return itemStack3.equals(itemStack)
               ? new ItemUtils.ComparisonResult(itemStack3, ItemUtils.ComparisonResult.Result.ORIGIN)
               : new ItemUtils.ComparisonResult(itemStack3, ItemUtils.ComparisonResult.Result.OTHER);
         }
      }

      private enum Result {
         ORIGIN,
         OTHER,
         TIE;
      }
   }

   public interface Conversion {
      static ArmorItem resolveArmorItem(ArmorItem armorItem, ArmorItem armorItem2, ItemStack itemStack, ItemStack itemStack2) {
         return ItemUtils.computeItemStack3(itemStack) >= ItemUtils.computeItemStack3(itemStack2) ? armorItem : armorItem2;
      }
   }
}
