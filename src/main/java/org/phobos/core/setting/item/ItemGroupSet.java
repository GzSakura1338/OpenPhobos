package org.phobos.core.setting.item;

import java.util.ArrayList;
import java.util.List;
import org.phobos.core.chat.ChatTextUtils;

public class ItemGroupSet<T> {
   public final List<ItemGroup<T>> itemGroupList = new ArrayList<>();

   public String describeText(String text) {
      List<String> list = new ArrayList<>();

      for (ItemGroup<T> itemGroup : this.itemGroupList) {
         String text2 = itemGroup.getName();
         if (text2.toLowerCase().startsWith(text.toLowerCase())) {
            list.add(text2);
         }
      }

      if (list.isEmpty()) {
         return null;
      } else {
         return list.size() == 1 ? list.getFirst() : ChatTextUtils.describeList(list);
      }
   }

   public void handleItemGroup(ItemGroup<T> itemGroup) {
      this.itemGroupList.add(itemGroup);
   }

   public List<T> resolveList(String text) {
      for (ItemGroup<T> itemGroup : this.itemGroupList) {
         if (itemGroup.getName().equalsIgnoreCase(text)) {
            return itemGroup.getList();
         }
      }

      return List.of();
   }
}
