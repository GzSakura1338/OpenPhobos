package org.phobos.core.setting.item;

import net.minecraft.item.Item;
import org.phobos.api.NameCodec;
import org.phobos.core.inventory.InventoryUtils;

public class ItemCodec implements NameCodec<Item> {
   public static final ItemCodec INSTANCE = new ItemCodec();

   public Item resolveObject(String text) {
      return InventoryUtils.resolveItem(text);
   }

   public static ItemCodec getINSTANCE() {
      return INSTANCE;
   }

   public String describeBlock(Item item) {
      return item.getName().getString();
   }
}
