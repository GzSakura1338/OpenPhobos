package org.phobos.core.setting.item;

import org.phobos.api.TextFormatter;
import org.phobos.core.inventory.InventoryUtils;

public class ItemNameFormatter implements TextFormatter {
   public static final ItemNameFormatter INSTANCE = new ItemNameFormatter();

   @Override
   public String describeText(String text) {
      return InventoryUtils.describeText(text);
   }

   public static ItemNameFormatter getINSTANCE() {
      return INSTANCE;
   }
}
