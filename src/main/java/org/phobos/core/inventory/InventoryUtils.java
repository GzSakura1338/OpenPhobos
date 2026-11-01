package org.phobos.core.inventory;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.block.Block;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.component.ComponentChanges;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentMapImpl;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.ComponentMap.Builder;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.component.type.FoodComponent.StatusEffectEntry;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.AxeItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MaceItem;
import net.minecraft.item.SwordItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ShulkerBoxScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.DyeColor;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.NotNull;
import org.phobos.api.MixinClassNames;
import org.phobos.api.MinecraftHolder;
import org.phobos.mixin.game.ui.IContainerComponent;
import org.phobos.core.chat.ChatTextUtils;

public final class InventoryUtils implements MinecraftHolder {
   public static final List<Item> itemList = List.of(
      Items.APPLE,
      Items.BAKED_POTATO,
      Items.BEEF,
      Items.BEETROOT,
      Items.BEETROOT_SOUP,
      Items.BREAD,
      Items.CARROT,
      Items.CHICKEN,
      Items.CHORUS_FRUIT,
      Items.COD,
      Items.COOKED_BEEF,
      Items.COOKED_CHICKEN,
      Items.COOKED_COD,
      Items.COOKED_MUTTON,
      Items.COOKED_PORKCHOP,
      Items.COOKED_RABBIT,
      Items.COOKED_SALMON,
      Items.COOKIE,
      Items.DRIED_KELP,
      Items.ENCHANTED_GOLDEN_APPLE,
      Items.GOLDEN_APPLE,
      Items.GOLDEN_CARROT,
      Items.HONEY_BOTTLE,
      Items.MELON_SLICE,
      Items.MUSHROOM_STEW,
      Items.MUTTON,
      Items.POISONOUS_POTATO,
      Items.PORKCHOP,
      Items.POTATO,
      Items.PUFFERFISH,
      Items.PUMPKIN_PIE,
      Items.RABBIT,
      Items.RABBIT_STEW,
      Items.ROTTEN_FLESH,
      Items.SALMON,
      Items.SPIDER_EYE,
      Items.SUSPICIOUS_STEW,
      Items.SWEET_BERRIES,
      Items.GLOW_BERRIES,
      Items.TROPICAL_FISH,
      Items.OMINOUS_BOTTLE
   );
   public static final List<Item> itemList2 = new ArrayList<>();
   public static final List<Item> itemList3 = List.of(
      Items.APPLE,
      Items.BAKED_POTATO,
      Items.BEEF,
      Items.BEETROOT,
      Items.BEETROOT_SOUP,
      Items.BREAD,
      Items.CARROT,
      Items.CHICKEN,
      Items.CHORUS_FRUIT,
      Items.COD,
      Items.COOKED_BEEF,
      Items.COOKED_CHICKEN,
      Items.COOKED_COD,
      Items.COOKED_MUTTON,
      Items.COOKED_PORKCHOP,
      Items.COOKED_RABBIT,
      Items.COOKED_SALMON,
      Items.COOKIE,
      Items.DRIED_KELP,
      Items.ENCHANTED_GOLDEN_APPLE,
      Items.GOLDEN_APPLE,
      Items.GOLDEN_CARROT,
      Items.HONEY_BOTTLE,
      Items.MELON_SLICE,
      Items.MUSHROOM_STEW,
      Items.MUTTON,
      Items.POISONOUS_POTATO,
      Items.PORKCHOP,
      Items.POTATO,
      Items.PUFFERFISH,
      Items.PUMPKIN_PIE,
      Items.RABBIT,
      Items.RABBIT_STEW,
      Items.ROTTEN_FLESH,
      Items.SALMON,
      Items.SPIDER_EYE,
      Items.SUSPICIOUS_STEW,
      Items.SWEET_BERRIES,
      Items.GLOW_BERRIES,
      Items.TROPICAL_FISH,
      Items.OMINOUS_BOTTLE,
      Items.SHIELD,
      Items.POTION,
      Items.MILK_BUCKET
   );

   public static void handleItemStack(ItemStack itemStack, ItemStack[] itemStacks) {
      Arrays.fill(itemStacks, ItemStack.EMPTY);
      ComponentMap componentMap = itemStack.getComponents();
      if (componentMap.contains(DataComponentTypes.CONTAINER)) {
         IContainerComponent iContainerComponent = (IContainerComponent)(Object)componentMap.get(DataComponentTypes.CONTAINER);
         DefaultedList<ItemStack> defaultedList = iContainerComponent.getStacks();

         for (int i = 0; i < defaultedList.size(); i++) {
            if (i < itemStacks.length) {
               itemStacks[i] = (ItemStack)defaultedList.get(i);
            }
         }
      } else if (componentMap.contains(DataComponentTypes.BLOCK_ENTITY_DATA)) {
         NbtComponent nbtComponent = (NbtComponent)componentMap.get(DataComponentTypes.BLOCK_ENTITY_DATA);
         if (nbtComponent.contains("Items")) {
            NbtList nbtList = (NbtList)nbtComponent.getNbt().get("Items");

            for (int i3 = 0; i3 < nbtList.size(); i3++) {
               int i2 = nbtList.getCompound(i3).getByte("Slot");
               if (i2 >= 0 && i2 < itemStacks.length) {
                  itemStacks[i2] = ItemStack.fromNbtOrEmpty(mc.player.getRegistryManager(), nbtList.getCompound(i3));
               }
            }
         }
      }
   }

   public static boolean testItemStack(ItemStack itemStack) {
      return EnchantmentUtils.computeItemStack3(itemStack, Enchantments.SHARPNESS) >= 50;
   }

   public static Item resolveItem(String text) {
      for (Item item : itemList2) {
         if (item.getName().getString().equalsIgnoreCase(text)) {
            return item;
         }
      }

      return null;
   }

   @NotNull
   public static String describeText2(String text, int i) {
      text = text.replaceAll("minecraft:", "");

      return switch (text) {
         case "mending", "aqua_affinity", "channeling", "silk_touch", "flame", "infinity", "multishot", "vanishing_curse", "binding_curse" -> {
            char c = text.charAt(0);
            yield (Character.toUpperCase(c) + text.substring(1)).substring(0, 3);
         }
         default -> {
            char c2 = text.charAt(0);
            yield (Character.toUpperCase(c2) + text.substring(1)).substring(0, 2) + i;
         }
      };
   }

   public static boolean testItemStack2(ItemStack itemStack) {
      return itemStack.getItem() instanceof MaceItem
         || itemStack.getItem() instanceof AxeItem axeItem
            && axeItem.getName() != null
            && (axeItem.getName().getString().contains("1.21 Mace") || itemStack.get(DataComponentTypes.CUSTOM_NAME) != null);
   }

   public static List<Slot> resolveList(ScreenHandler screenHandler) {
      List<Slot> list = new ArrayList<>();

      for (int i = 0; i < screenHandler.slots.size(); i++) {
         list.add(screenHandler.getSlot(i));
      }

      return list;
   }

   public InventoryUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static boolean testItem(Item item) {
      return itemList3.contains(item);
   }

   public static List<Slot> resolveList2(GenericContainerScreenHandler genericContainerScreenHandler) {
      List<Slot> list = new ArrayList<>();

      for (int i = 0; i < genericContainerScreenHandler.getInventory().size(); i++) {
         list.add(genericContainerScreenHandler.getSlot(i));
      }

      return list;
   }

   public static ItemStack createItemStack(Item item, int i, Consumer<Builder> consumer) {
      Builder builder = MixinClassNames.LE.resolveObject(consumer, ComponentMap.builder());
      ComponentChanges componentChanges = ComponentChanges.EMPTY;
      ComponentMapImpl componentMapImpl = ComponentMapImpl.create(builder.build(), componentChanges);
      return new ItemStack(item, i, componentMapImpl);
   }

   public static boolean testLivingEntity(LivingEntity livingEntity, Item item) {
      return livingEntity.getMainHandStack().isOf(item) || livingEntity.getOffHandStack().isOf(item);
   }

   public static float computeItemStack(ItemStack itemStack) {
      return (float)(itemStack.getMaxDamage() - itemStack.getDamage()) / itemStack.getMaxDamage() * 100.0F;
   }

   public static boolean testItemStack3(ItemStack itemStack) {
      IContainerComponent iContainerComponent = (IContainerComponent)(Object)itemStack.get(DataComponentTypes.CONTAINER);
      if (iContainerComponent != null && !iContainerComponent.getStacks().isEmpty()) {
         return true;
      }

      NbtCompound nbtCompound = ((NbtComponent)itemStack.getOrDefault(DataComponentTypes.BLOCK_ENTITY_DATA, NbtComponent.DEFAULT)).getNbt();
      return nbtCompound != null && nbtCompound.contains("Items", 9);
   }

   public static List<Slot> resolveList3(ShulkerBoxScreenHandler shulkerBoxScreenHandler) {
      List<Slot> list = new ArrayList<>();

      for (int i = 0; i < 27; i++) {
         list.add(shulkerBoxScreenHandler.getSlot(i));
      }

      return list;
   }

   public static void handleText(String text, Consumer<Item> consumer) {
      for (Item item : itemList2) {
         if (item.getName().getString().equalsIgnoreCase(text)) {
            consumer.accept(item);
            return;
         }
      }
   }

   public static String describeText(String text) {
      List<String> list = new ArrayList<>();

      for (Item item : itemList2) {
         String text2 = item.getName().getString();
         if (text2.toLowerCase().startsWith(text.toLowerCase())) {
            list.add(text2);
         }
      }

      if (list.isEmpty()) {
         return null;
      } else {
         return list.size() == 1 ? list.getFirst() : ChatTextUtils.describeList(list);
      }
   }

   public static boolean testItem2(Item item) {
      return itemList3.contains(item);
   }

   public static boolean testItem3(Item item) {
      return itemList.contains(item);
   }

   public static void run5() {
      for (Item item : Registries.ITEM) {
         itemList2.add(item);
      }

      for (Block block : Registries.BLOCK) {
         itemList2.add(block.asItem());
      }
   }

   public static boolean testItem4(Item item) {
      return item instanceof SwordItem || item instanceof AxeItem || item instanceof MaceItem;
   }

   public static boolean testItemStack4(ItemStack itemStack) {
      ItemStack[] itemStacks = new ItemStack[27];
      handleItemStack(itemStack, itemStacks);

      for (ItemStack itemStack2 : itemStacks) {
         if (!itemStack2.isEmpty()
            && itemStack2.getItem() instanceof SwordItem
            && EnchantmentUtils.testItemStack(itemStack2, Enchantments.SHARPNESS)
            && testItemStack(itemStack2)) {
            return true;
         }
      }

      return false;
   }

   public static double computeItemStack2(ItemStack itemStack) {
      return (double)itemStack.getCount() / itemStack.getMaxCount() * 100.0;
   }

   public static int computeFoodComponent(FoodComponent foodComponent) {
      MutableInt mutableInt = new MutableInt(0);
      mutableInt.add(foodComponent.nutrition() * foodComponent.saturation());

      for (StatusEffectEntry statusEffectEntry : foodComponent.effects()) {
         if (!(statusEffectEntry.probability() < 1.0F)) {
            StatusEffectInstance statusEffectInstance = statusEffectEntry.effect();
            if (InventoryUtils.PotionUtil.testStatusEffectInstance(statusEffectInstance)) {
               mutableInt.add(-(100 * statusEffectInstance.getAmplifier()));
            } else if (InventoryUtils.PotionUtil.testStatusEffectInstance2(statusEffectInstance)) {
               mutableInt.add(100 * statusEffectInstance.getAmplifier());
            }
         }
      }

      return mutableInt.getValue();
   }

   public static Color resolveColor(ItemStack itemStack) {
      if (itemStack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof ShulkerBoxBlock shulkerBoxBlock) {
         DyeColor dyeColor = shulkerBoxBlock.getColor();
         if (dyeColor == null) {
            return Color.WHITE;
         }

         int i = dyeColor.getEntityColor();
         float f = i >> 16 & 0xFF;
         float f2 = i >> 8 & 0xFF;
         float f3 = i & 0xFF;
         return new Color(MathHelper.clamp(f, 0.0F, 1.0F), MathHelper.clamp(f2, 0.0F, 1.0F), MathHelper.clamp(f3, 0.0F, 1.0F), 1.0F);
      } else {
         return Color.WHITE;
      }
   }

   public static boolean testItemStack5(ItemStack itemStack) {
      return itemStack.get(DataComponentTypes.FOOD) != null;
   }

   public interface PotionUtil {
      static boolean testItemStack(ItemStack itemStack) {
         PotionContentsComponent potionContentsComponent = (PotionContentsComponent)itemStack.getComponents().get(DataComponentTypes.POTION_CONTENTS);
         if (potionContentsComponent == null) {
            return false;
         }

         for (StatusEffectInstance statusEffectInstance : potionContentsComponent.getEffects()) {
            if (((StatusEffect)statusEffectInstance.getEffectType().value()).isBeneficial()) {
               return true;
            }
         }

         return false;
      }

      static boolean testStatusEffectInstance(StatusEffectInstance statusEffectInstance) {
         return StatusEffectCategory.HARMFUL.equals(((StatusEffect)statusEffectInstance.getEffectType().value()).getCategory());
      }

      static boolean testItemStack2(ItemStack itemStack) {
         PotionContentsComponent potionContentsComponent = (PotionContentsComponent)itemStack.getComponents().get(DataComponentTypes.POTION_CONTENTS);
         if (potionContentsComponent == null) {
            return false;
         }

         for (StatusEffectInstance statusEffectInstance : potionContentsComponent.getEffects()) {
            if (StatusEffectCategory.HARMFUL.equals(((StatusEffect)statusEffectInstance.getEffectType().value()).getCategory())) {
               return true;
            }
         }

         return false;
      }

      static boolean testStatusEffectInstance2(StatusEffectInstance statusEffectInstance) {
         return ((StatusEffect)statusEffectInstance.getEffectType().value()).isBeneficial();
      }
   }
}
