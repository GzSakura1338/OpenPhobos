package org.phobos.core.render;

import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipData;

public class TooltipContext {
   public TooltipData tooltipData;
   public ItemStack itemStack;
   public static final TooltipContext INSTANCE = new TooltipContext();

   public void setTooltipData(TooltipData tooltipData) {
      this.tooltipData = tooltipData;
   }

   public static TooltipContext resolveTooltipContext(ItemStack itemStack) {
      INSTANCE.tooltipData = null;
      INSTANCE.itemStack = itemStack;
      return INSTANCE;
   }

   public ItemStack getItemStack() {
      return this.itemStack;
   }

   public void setItemStack(ItemStack itemStack) {
      this.itemStack = itemStack;
   }

   public TooltipData getTooltipData() {
      return this.tooltipData;
   }
}
