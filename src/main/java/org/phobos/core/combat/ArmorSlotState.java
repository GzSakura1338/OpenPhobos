package org.phobos.core.combat;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

public class ArmorSlotState {
   public ItemStack itemStack = null;
   public boolean flag;
   public final EquipmentSlot equipmentSlot;

   public ArmorSlotState(EquipmentSlot equipmentSlot) {
      this.equipmentSlot = equipmentSlot;
   }

   public void setItemStack(ItemStack itemStack) {
      this.itemStack = itemStack;
   }

   public void setFlag(boolean flag) {
      this.flag = flag;
   }

   public ItemStack getItemStack() {
      return this.itemStack;
   }

   public EquipmentSlot getEquipmentSlot() {
      return this.equipmentSlot;
   }

   public boolean isFlag() {
      return this.flag;
   }
}
