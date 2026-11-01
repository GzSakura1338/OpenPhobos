package org.phobos.core.setting;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.item.Item;
import org.jetbrains.annotations.Nullable;
import org.phobos.core.setting.item.ContainerItemGroup;
import org.phobos.core.setting.item.ItemCodec;
import org.phobos.core.setting.item.ItemNameFormatter;
import org.phobos.core.setting.item.ShulkerItemGroup;

public class ItemSetSetting extends SetSetting<Item> {
   public ItemSetSetting(String text, @Nullable String text2, Set<Item> set) {
      super(text, text2, set, ItemCodec.getINSTANCE(), Item::getDefaultStack, item -> item.getName().getString(), ItemNameFormatter.getINSTANCE());
      this.itemGroupSet.handleItemGroup(ShulkerItemGroup.getINSTANCE());
      this.itemGroupSet.handleItemGroup(ContainerItemGroup.getINSTANCE());
   }

   public ItemSetSetting(String text, Item... items) {
      this(text, "", new HashSet<>(Set.of(items)));
   }
}
