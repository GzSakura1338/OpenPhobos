package org.phobos.event;

import net.minecraft.item.ItemStack;
import org.phobos.core.render.TooltipContext;
import org.phobos.core.render.ContainerTooltip;
import org.phobos.core.inventory.InventoryUtils;
import org.phobos.module.misc.TooltipsModule;

public final class TooltipsTooltipContextListener extends ModuleListener<TooltipsModule, TooltipContext> {
   public TooltipsTooltipContextListener(TooltipsModule tooltipsModule) {
      super(tooltipsModule, TooltipContext.class);
   }

   public void handleObject2(TooltipContext tooltipContext) {
      if (InventoryUtils.testItemStack3(tooltipContext.getItemStack())) {
         ItemStack[] itemStacks = new ItemStack[27];
         InventoryUtils.handleItemStack(tooltipContext.getItemStack(), itemStacks);
         tooltipContext.setTooltipData(new ContainerTooltip(itemStacks, InventoryUtils.resolveColor(tooltipContext.getItemStack())));
      }
   }
}
