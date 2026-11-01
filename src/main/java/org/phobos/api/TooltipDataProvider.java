package org.phobos.api;

import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.item.tooltip.TooltipData;

public interface TooltipDataProvider extends TooltipData {
   TooltipComponent getTooltipComponent();
}
