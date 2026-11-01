package org.phobos.core.inventory;

import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.component.type.ItemEnchantmentsComponent.Builder;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.effect.DamageImmunityEnchantmentEffect;
import net.minecraft.enchantment.effect.EnchantmentEffectEntry;
import net.minecraft.enchantment.effect.EnchantmentValueEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.phobos.api.MinecraftHolder;

public final class EnchantmentUtils implements MinecraftHolder {
   public static float computeItemStack(ItemStack itemStack, Entity entity, float f) {
      MutableFloat mutableFloat = new MutableFloat(f);
      handleItemStack(itemStack, (registryEntry, i) -> handleEnchantment4((Enchantment)registryEntry.value(), i, entity, mutableFloat));
      return mutableFloat.floatValue();
   }

   public static void handleItemStack(ItemStack itemStack, EnchantmentUtils.Consumer consumer) {
      if (itemStack != null) {
         ItemEnchantmentsComponent itemEnchantmentsComponent = (ItemEnchantmentsComponent)itemStack.getOrDefault(
            DataComponentTypes.ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT
         );

         for (Entry<RegistryEntry<Enchantment>> entry : itemEnchantmentsComponent.getEnchantmentEntries()) {
            consumer.handleRegistryEntry((RegistryEntry<Enchantment>)entry.getKey(), entry.getIntValue());
         }
      }
   }

   public static void handleItemStack2(
      ItemStack itemStack, EquipmentSlot equipmentSlot, LivingEntity livingEntity, EnchantmentUtils.ContextAwareConsumer contextAwareConsumer
   ) {
      if (!itemStack.isEmpty()) {
         ItemEnchantmentsComponent itemEnchantmentsComponent = (ItemEnchantmentsComponent)itemStack.get(DataComponentTypes.ENCHANTMENTS);
         if (itemEnchantmentsComponent != null && !itemEnchantmentsComponent.isEmpty()) {
            EnchantmentEffectContext enchantmentEffectContext = new EnchantmentEffectContext(itemStack, equipmentSlot, livingEntity);

            for (Entry<RegistryEntry<Enchantment>> entry : itemEnchantmentsComponent.getEnchantmentEntries()) {
               RegistryEntry<Enchantment> registryEntry = (RegistryEntry<Enchantment>)entry.getKey();
               if (((Enchantment)registryEntry.value()).slotMatches(equipmentSlot)) {
                  contextAwareConsumer.handleRegistryEntry(registryEntry, entry.getIntValue(), enchantmentEffectContext);
               }
            }
         }
      }
   }

   public static float computeItemStack2(ItemStack itemStack, Entity entity, float f) {
      MutableFloat mutableFloat = new MutableFloat(f);
      handleItemStack(itemStack, (registryEntry, i) -> handleEnchantment3((Enchantment)registryEntry.value(), i, entity, mutableFloat));
      return mutableFloat.floatValue();
   }

   public static void handleItemStack3(ItemStack itemStack, RegistryKey<Enchantment> registryKey, int i) {
      if (mc.getNetworkHandler() != null) {
         Builder builder = new Builder(EnchantmentHelper.getEnchantments(itemStack));
         mc.getNetworkHandler().getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(registryKey).ifPresent(reference -> builder.add(reference, i));
         EnchantmentHelper.set(itemStack, builder.build());
      }
   }

   public static void handleEnchantment(
      Enchantment enchantment, int i, ItemStack itemStack, Entity entity, DamageSource damageSource, MutableFloat mutableFloat
   ) {
      for (EnchantmentEffectEntry<EnchantmentValueEffect> enchantmentEffectEntry : enchantment.getEffect(EnchantmentEffectComponentTypes.DAMAGE_PROTECTION)) {
         mutableFloat.setValue(((EnchantmentValueEffect)enchantmentEffectEntry.effect()).apply(i, entity.getRandom(), mutableFloat.floatValue()));
      }
   }

   public static void handleEnchantment2(Enchantment enchantment, int i, Entity entity, MutableFloat mutableFloat) {
      for (EnchantmentEffectEntry<EnchantmentValueEffect> enchantmentEffectEntry : enchantment.getEffect(EnchantmentEffectComponentTypes.DAMAGE)) {
         mutableFloat.setValue(((EnchantmentValueEffect)enchantmentEffectEntry.effect()).apply(i, entity.getRandom(), mutableFloat.floatValue()));
      }
   }

   public static boolean testLivingEntity(LivingEntity livingEntity) {
      MutableBoolean mutableBoolean = new MutableBoolean();
      handleLivingEntity(
         livingEntity,
         (registryEntry, i, enchantmentEffectContext) -> mutableBoolean.setValue(mutableBoolean.isTrue() || testEnchantment((Enchantment)registryEntry.value()))
      );
      return mutableBoolean.isTrue();
   }

   public static Map<RegistryKey<Enchantment>, Integer> resolveMap(ItemStack itemStack) {
      Map<RegistryKey<Enchantment>, Integer> map = new HashMap<>();
      ItemEnchantmentsComponent itemEnchantmentsComponent = EnchantmentHelper.getEnchantments(itemStack);

      for (RegistryEntry<Enchantment> registryEntry : itemEnchantmentsComponent.getEnchantments()) {
         Optional<RegistryKey<Enchantment>> optional;
         if ((optional = registryEntry.getKey()).isPresent()) {
            map.put(optional.get(), EnchantmentHelper.getLevel(registryEntry, itemStack));
         }
      }

      return map;
   }

   public static void handleEnchantment3(Enchantment enchantment, int i, Entity entity, MutableFloat mutableFloat) {
      for (EnchantmentEffectEntry<EnchantmentValueEffect> enchantmentEffectEntry : enchantment.getEffect(EnchantmentEffectComponentTypes.KNOCKBACK)) {
         mutableFloat.setValue(((EnchantmentValueEffect)enchantmentEffectEntry.effect()).apply(i, entity.getRandom(), mutableFloat.floatValue()));
      }
   }

   public static int computeItemStack3(ItemStack itemStack, RegistryKey<Enchantment> registryKey) {
      ItemEnchantmentsComponent itemEnchantmentsComponent = EnchantmentHelper.getEnchantments(itemStack);

      for (RegistryEntry<Enchantment> registryEntry : itemEnchantmentsComponent.getEnchantments()) {
         Optional<RegistryKey<Enchantment>> optional;
         if ((optional = registryEntry.getKey()).isPresent() && optional.get().equals(registryKey)) {
            return EnchantmentHelper.getLevel(registryEntry, itemStack);
         }
      }

      return 0;
   }

   public static void handleLivingEntity(LivingEntity livingEntity, EnchantmentUtils.ContextAwareConsumer contextAwareConsumer) {
      EquipmentSlot[] equipmentSlots = EquipmentSlot.values();

      for (EquipmentSlot equipmentSlot : equipmentSlots) {
         handleItemStack2(livingEntity.getEquippedStack(equipmentSlot), equipmentSlot, livingEntity, contextAwareConsumer);
      }
   }

   public static float computeLivingEntity(LivingEntity livingEntity, DamageSource damageSource) {
      MutableFloat mutableFloat = new MutableFloat(0.0F);
      handleLivingEntity(
         livingEntity,
         (registryEntry, i, enchantmentEffectContext) -> handleEnchantment(
            (Enchantment)registryEntry.value(), i, enchantmentEffectContext.stack(), livingEntity, damageSource, mutableFloat
         )
      );
      return mutableFloat.floatValue();
   }

   public EnchantmentUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static float computeItemStack4(ItemStack itemStack, Entity entity, float f) {
      if (itemStack == null) {
         return 0.0F;
      }

      MutableFloat mutableFloat = new MutableFloat(f);
      handleItemStack(itemStack, (registryEntry, i) -> handleEnchantment2((Enchantment)registryEntry.value(), i, entity, mutableFloat));
      return mutableFloat.floatValue();
   }

   public static void handleEnchantment4(Enchantment enchantment, int i, Entity entity, MutableFloat mutableFloat) {
      for (EnchantmentEffectEntry<EnchantmentValueEffect> enchantmentEffectEntry : enchantment.getEffect(EnchantmentEffectComponentTypes.ARMOR_EFFECTIVENESS)) {
         mutableFloat.setValue(((EnchantmentValueEffect)enchantmentEffectEntry.effect()).apply(i, entity.getRandom(), mutableFloat.floatValue()));
      }
   }

   public static boolean testEnchantment(Enchantment enchantment) {
      Iterator<EnchantmentEffectEntry<DamageImmunityEnchantmentEffect>> iterator = enchantment.getEffect(EnchantmentEffectComponentTypes.DAMAGE_IMMUNITY)
         .iterator();
      return iterator.hasNext();
   }

   public static boolean testItemStack(ItemStack itemStack, RegistryKey<Enchantment> registryKey) {
      ItemEnchantmentsComponent itemEnchantmentsComponent = EnchantmentHelper.getEnchantments(itemStack);

      for (RegistryEntry<Enchantment> registryEntry : itemEnchantmentsComponent.getEnchantments()) {
         Optional<RegistryKey<Enchantment>> optional;
         if ((optional = registryEntry.getKey()).isPresent() && optional.get().equals(registryKey)) {
            return true;
         }
      }

      return false;
   }

   @FunctionalInterface
   private interface Consumer {
      void handleRegistryEntry(RegistryEntry<Enchantment> var1, int var2);
   }

   @FunctionalInterface
   private interface ContextAwareConsumer {
      void handleRegistryEntry(RegistryEntry<Enchantment> var1, int var2, EnchantmentEffectContext var3);
   }

   public static class EnchantmentBuilder {
   }
}
