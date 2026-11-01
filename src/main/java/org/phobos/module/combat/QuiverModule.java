package org.phobos.module.combat;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.slot.Slot;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.Nullable;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.NumberSetting;
import org.phobos.core.inventory.ItemSwitcher;
import org.phobos.core.inventory.InventoryManager;
import org.phobos.core.rotation.RotationManager;
import org.phobos.core.PhobosClient;
import org.phobos.core.module.Categories;
import org.phobos.core.inventory.SwitchOverride;
import org.phobos.event.PlayerTickEvent;
import org.phobos.event.SafeListener;
import org.phobos.module.Module;
import org.phobos.module.player.SilentHotbarModule;
import org.phobos.module.render.FreeLookModule;

public class QuiverModule extends Module {
   public final Map<RegistryEntry<StatusEffect>, Long> registryEntryMap;
   public final Setting<Integer> ticks = this.resolveSetting2(new NumberSetting<>("Ticks", 3, 3, 6));
   private static Object[] phobos$g1806e6c68;
   private static Object[] phobos$g1806e6a00;
   private static Object[] phobos$g1806e6b18;

   public void handleClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, ClientPlayerInteractionManager clientPlayerInteractionManager) {
      Map var3 = this.registryEntryMap;
      Set var4 = var3.entrySet();
      Predicate<Entry> var21 = QuiverModule::testEntry;
      var4.removeIf(var21);
      boolean var22 = RotationManager.isFlag();
      if (!var22) {
         PhobosClient var23 = this.phobosClient;
         InventoryManager var24 = var23.getInventoryManager();
         QuiverModule var10000 = this;
         ClientPlayerEntity var10001 = clientPlayerEntity;
         ClientPlayerInteractionManager cap50 = clientPlayerInteractionManager;
         ClientPlayerEntity cap49 = var10001;
         QuiverModule cap48 = var10000;
         Consumer<ItemSwitcher> var41 = var3x -> cap48.handleClientPlayerEntity2(cap49, cap50, var3x);
         var24.handleConsumer2(var41);
      }
   }

   public QuiverModule(PhobosClient phobosClient) {
      super(phobosClient, "Quiver", Categories.combat, "Shoots yourself with arrows");
      this.registryEntryMap = new HashMap<>();
      this.handleListener(
         new SafeListener<PlayerTickEvent>(500) {
            public void handleObject(
               PlayerTickEvent playerTickEvent,
               ClientPlayerEntity clientPlayerEntity,
               ClientWorld clientWorld,
               ClientPlayerInteractionManager clientPlayerInteractionManager
            ) {
               QuiverModule.this.handleClientPlayerEntity(clientPlayerEntity, clientPlayerInteractionManager);
            }
         }
      );
   }

   public boolean testItemSwitcher(ItemSwitcher itemSwitcher, ClientPlayerEntity clientPlayerEntity, ItemStack itemStack) {
      MutableObject var4 = new MutableObject();
      Function[] var5 = new Function[1];
      QuiverModule var10000 = this;
      ItemStack var10001 = itemStack;
      ClientPlayerEntity cap54 = clientPlayerEntity;
      MutableObject cap53 = var4;
      ItemStack cap52 = var10001;
      QuiverModule cap51 = var10000;
      Function<Slot, Slot> var22 = var4x -> cap51.resolveSlot(cap52, cap53, cap54, var4x);
      var5[0] = var22;
      Object var23 = itemSwitcher.resolveObject(var5);
      boolean var24 = var23 instanceof Slot;
      if (var23 != null && !var24) {
         throw new ClassCastException("CHECKCAST failed");
      }

      Object var25 = var4.getValue();
      boolean var26 = var25 instanceof Slot;
      if (var25 != null && !var26) {
         throw new ClassCastException("CHECKCAST failed");
      }

      boolean var27;
      if (var23 != null && var25 != null) {
         itemSwitcher.handleSlot4((Slot)var25, (Slot)var23);
         var27 = true;
      } else {
         var27 = false;
      }

      return var27;
   }

   public void handleItemStack(ItemStack itemStack) {
      PotionContentsComponent potionContentsComponent = this.resolvePotionContentsComponent(itemStack);
      if (potionContentsComponent != null) {
         for (StatusEffectInstance statusEffectInstance : potionContentsComponent.getEffects()) {
            this.registryEntryMap.put(statusEffectInstance.getEffectType(), System.currentTimeMillis() + 3000L);
         }
      }
   }

   public Slot resolveSlot(ItemStack itemStack, MutableObject mutableObject, ClientPlayerEntity clientPlayerEntity, Slot slot) {
      ItemStack itemStack2 = slot.getStack();
      if (itemStack2.equals(itemStack)) {
         mutableObject.setValue(slot);
         return null;
      } else if (!itemStack2.isEmpty() && itemStack2.isOf(Items.TIPPED_ARROW)) {
         PotionContentsComponent potionContentsComponent = this.resolvePotionContentsComponent(itemStack2);
         return potionContentsComponent != null && !this.testClientPlayerEntity(clientPlayerEntity, potionContentsComponent) ? slot : null;
      } else {
         return null;
      }
   }

   public boolean testItemSwitcher2(ItemSwitcher itemSwitcher, ClientPlayerEntity clientPlayerEntity) {
      return itemSwitcher.resolveObject(slot -> {
         ItemStack itemStack = slot.getStack();
         if (!itemStack.isEmpty() && itemStack.isOf(Items.TIPPED_ARROW)) {
            PotionContentsComponent potionContentsComponent = this.resolvePotionContentsComponent(itemStack);
            if (potionContentsComponent == null) {
               return null;
            } else {
               return this.testClientPlayerEntity(clientPlayerEntity, potionContentsComponent) ? null : slot;
            }
         } else {
            return null;
         }
      }) == null;
   }

   public void handleClientPlayerEntity2(
      ClientPlayerEntity clientPlayerEntity, ClientPlayerInteractionManager clientPlayerInteractionManager, ItemSwitcher itemSwitcher
   ) {
      Slot slot = itemSwitcher.resolveObject(slotx -> slotx.getStack().isOf(Items.BOW) ? slotx : null);
      if (slot == null) {
         this.handleText6("No bow found");
         this.run11();
      } else if (!itemSwitcher.testSlot4(slot)) {
         this.handleText6("Bow needs to be in hotbar");
         this.run11();
      } else {
         ItemStack itemStack = clientPlayerEntity.getProjectileType(slot.getStack());
         if (itemStack.isEmpty()) {
            this.handleText6("No arrows found");
            this.run11();
         } else {
            PotionContentsComponent potionContentsComponent = this.resolvePotionContentsComponent(itemStack);
            if (this.testItemSwitcher2(itemSwitcher, clientPlayerEntity)) {
               this.run11();
            } else if (potionContentsComponent != null && !this.testClientPlayerEntity(clientPlayerEntity, potionContentsComponent)) {
               this.phobosClient.getRotationManager().testFloat(FreeLookModule.computeClientPlayerEntity2(clientPlayerEntity), -90.0F);
               SilentHotbarModule.getSilentHotbarManager()
                  .handleObject3(this, slot, 2, SilentHotbarModule.getSilentHotbarManager().getBuilder().resolveBuilder3(true).getSelectionConfig());
               SwitchOverride.handleFlag3(true);
               if (clientPlayerEntity.getItemUseTime() >= this.ticks.getValue()) {
                  clientPlayerInteractionManager.stopUsingItem(clientPlayerEntity);
                  this.handleItemStack(itemStack);
                  if (!this.testItemSwitcher(itemSwitcher, clientPlayerEntity, itemStack)) {
                     this.run11();
                  }
               }
            } else {
               this.testItemSwitcher(itemSwitcher, clientPlayerEntity, itemStack);
            }
         }
      }
   }

   @Override
   public void run3() {
      this.registryEntryMap.clear();
   }

   @Nullable
   public PotionContentsComponent resolvePotionContentsComponent(ItemStack itemStack) {
      return !itemStack.isOf(Items.TIPPED_ARROW) ? null : itemStack.get(DataComponentTypes.POTION_CONTENTS);
   }

   @Override
   public String nothing() {
      return mc.player != null && mc.player.isUsingItem() && mc.player.getMainHandStack().isOf(Items.BOW)
         ? String.valueOf(this.ticks.getValue() - mc.player.getItemUseTime())
         : null;
   }

   public boolean testClientPlayerEntity(ClientPlayerEntity clientPlayerEntity, PotionContentsComponent potionContentsComponent) {
      for (StatusEffectInstance statusEffectInstance : potionContentsComponent.getEffects()) {
         RegistryEntry<StatusEffect> registryEntry = statusEffectInstance.getEffectType();
         if (!clientPlayerEntity.hasStatusEffect(registryEntry) && !this.registryEntryMap.containsKey(registryEntry)) {
            return false;
         }
      }

      return true;
   }

   public static boolean testEntry(Entry entry) {
      return (Long)entry.getValue() < System.currentTimeMillis();
   }

   @Override
   public void run4() {
      SilentHotbarModule.getSilentHotbarManager().handleObject2(this);
      SwitchOverride.run2();
      this.registryEntryMap.clear();
   }
}
