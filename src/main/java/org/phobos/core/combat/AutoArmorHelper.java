package org.phobos.core.combat;

import java.util.HashMap;
import java.util.Map;
import java.util.Map;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import org.apache.commons.lang3.function.TriConsumer;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.apache.commons.lang3.mutable.MutableObject;
import org.phobos.module.combat.AutoArmorModule;
import org.phobos.core.inventory.EnchantmentUtils;
import org.phobos.core.inventory.InventoryUtils;
import org.phobos.core.inventory.ItemSwitcher;

public class AutoArmorHelper {
   public final AutoArmorModule autoArmorModule;

   public void handleItemSwitcher(
      ItemSwitcher itemSwitcher, ClientPlayerEntity clientPlayerEntity, TriConsumer<ItemSwitcher, ClientPlayerEntity, Integer> triConsumer
   ) {
      Map<ArmorSlotState, Integer> map = new HashMap<>();

      for (int i = 5; i < 9; i++) {
         ItemStack itemStack = clientPlayerEntity.playerScreenHandler.getSlot(i).getStack();
         if (itemStack != null
            && EnchantmentUtils.testItemStack(itemStack, Enchantments.MENDING)
            && !itemStack.isEmpty()
            && itemStack.getItem() instanceof ArmorItem armorItem) {
            EquipmentSlot equipmentSlot = armorItem.getSlotType();
            ArmorSlotState armorSlotState = this.autoArmorModule.resolveArmorSlotState(equipmentSlot);
            if (armorSlotState != null) {
               float f = InventoryUtils.computeItemStack(itemStack);
               if (this.autoArmorModule.testEquipmentSlot(equipmentSlot, f)) {
                  map.put(armorSlotState, i);
               } else {
                  armorSlotState.setFlag(false);
               }
            }
         }
      }

      if (map.size() != 4) {
         for (Map.Entry<ArmorSlotState, Integer> entry : map.entrySet()) {
            entry.getKey().setFlag(true);
            triConsumer.accept(itemSwitcher, clientPlayerEntity, entry.getValue());
         }
      }
   }

   public void handleItemSwitcher2(
      ItemSwitcher itemSwitcher,
      ClientPlayerEntity clientPlayerEntity,
      TriConsumer<ItemSwitcher, ClientPlayerEntity, Integer> triConsumer,
      TriConsumer<ItemSwitcher, ClientPlayerEntity, Integer> triConsumer2
   ) {
      if (this.autoArmorModule.booleanValueSetting2.getValue() && this.autoArmorModule.armorSlotState != null) {
         EquipmentSlot equipmentSlot = this.autoArmorModule.armorSlotState.getEquipmentSlot();
         int i2 = this.autoArmorModule.computeEquipmentSlot(equipmentSlot);
         ItemStack itemStack2 = this.autoArmorModule.resolveItemStack(i2);
         if (itemStack2 == null) {
            this.autoArmorModule.armorSlotState = null;
         } else {
            float f = InventoryUtils.computeItemStack(itemStack2);
            if (!this.autoArmorModule.testEquipmentSlot(equipmentSlot, f)) {
               for (ArmorSlotState armorSlotState : this.autoArmorModule.armorSlotStateArray) {
                  if (!armorSlotState.equals(this.autoArmorModule.armorSlotState)) {
                     int i = this.autoArmorModule.computeEquipmentSlot(this.autoArmorModule.armorSlotState.getEquipmentSlot());
                     ItemStack itemStack = this.autoArmorModule.resolveItemStack(i);
                     if (itemStack != null && !itemStack2.isEmpty()) {
                        triConsumer2.accept(itemSwitcher, clientPlayerEntity, i);
                        break;
                     }
                  }
               }

               return;
            }

            this.autoArmorModule.armorSlotState = null;
         }
      }

      MutableObject<ArmorSlotState> mutableObject = new MutableObject(null);
      MutableFloat mutableFloat = new MutableFloat(Float.MAX_VALUE);
      MutableObject<ItemStack> mutableObject2 = new MutableObject(null);
      this.autoArmorModule.run2();
      boolean flag = false;

      for (int i3 = 5; i3 < 9; i3++) {
         ItemStack itemStack3 = clientPlayerEntity.playerScreenHandler.getSlot(i3).getStack();
         if (itemStack3 != null && !itemStack3.isEmpty() && itemStack3.getItem() instanceof ArmorItem armorItem) {
            EquipmentSlot equipmentSlot2 = armorItem.getSlotType();
            ArmorSlotState armorSlotState2 = this.autoArmorModule.resolveArmorSlotState(equipmentSlot2);
            if (armorSlotState2 != null) {
               float f2 = InventoryUtils.computeItemStack(itemStack3);
               if (!this.autoArmorModule.testEquipmentSlot(equipmentSlot2, f2) && f2 < mutableFloat.getValue()) {
                  mutableFloat.setValue(f2);
                  mutableObject.setValue(armorSlotState2);
               }
            }
         }
      }

      for (ArmorSlotState armorSlotState3 : this.autoArmorModule.armorSlotStateArray) {
         ItemStack itemStack4 = armorSlotState3.getItemStack();
         if (itemStack4 != null) {
            EquipmentSlot equipmentSlot3 = armorSlotState3.getEquipmentSlot();
            float f3 = InventoryUtils.computeItemStack(itemStack4);
            if (!this.autoArmorModule.testEquipmentSlot(equipmentSlot3, f3) && f3 < mutableFloat.getValue()) {
               mutableFloat.setValue(f3);
               mutableObject.setValue(armorSlotState3);
               mutableObject2.setValue(itemStack4);
               flag = true;
            }
         }
      }

      if (mutableObject.getValue() != null) {
         if (!this.autoArmorModule.testEquipmentSlot(((ArmorSlotState)mutableObject.getValue()).getEquipmentSlot(), mutableFloat.getValue())) {
            if (flag) {
               Slot slot = itemSwitcher.resolveObject(slotx -> {
                  ItemStack itemStack = slotx.getStack();
                  return ItemStack.areEqual(itemStack, (ItemStack)mutableObject2.getValue()) ? slotx : null;
               });
               if (slot != null) {
                  triConsumer.accept(itemSwitcher, clientPlayerEntity, slot.id);
                  ((ArmorSlotState)mutableObject.getValue()).setItemStack(null);
               }
            }

            for (ArmorSlotState armorSlotState4 : this.autoArmorModule.armorSlotStateArray) {
               if (!armorSlotState4.equals(mutableObject.getValue())) {
                  int i4 = this.autoArmorModule.computeEquipmentSlot(armorSlotState4.getEquipmentSlot());
                  ItemStack itemStack5 = this.autoArmorModule.resolveItemStack(i4);
                  if (itemStack5 != null && !itemStack5.isEmpty()) {
                     armorSlotState4.setItemStack(itemStack5.copy());
                     triConsumer2.accept(itemSwitcher, clientPlayerEntity, i4);
                     armorSlotState4.setFlag(true);
                  }
               }
            }

            this.autoArmorModule.armorSlotState = (ArmorSlotState)mutableObject.getValue();
         }
      }
   }

   public AutoArmorHelper(AutoArmorModule autoArmorModule) {
      this.autoArmorModule = autoArmorModule;
   }
}
