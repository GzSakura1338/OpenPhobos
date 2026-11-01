package org.phobos.event;

import net.minecraft.item.ItemStack;

public class ClearActiveItemEvent {
   public final ItemStack itemStack;

   public ItemStack getItemStack() {
      return this.itemStack;
   }

   public ClearActiveItemEvent(ItemStack itemStack) {
      this.itemStack = itemStack;
   }
}
