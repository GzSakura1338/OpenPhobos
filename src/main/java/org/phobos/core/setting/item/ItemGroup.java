package org.phobos.core.setting.item;

import java.util.ArrayList;
import java.util.List;

public class ItemGroup<T> {
   public final String name;
   public final List<T> list = new ArrayList<>();

   public String getName() {
      return this.name;
   }

   public void add(T object) {
      this.list.add(object);
   }

   public List<T> getList() {
      return this.list;
   }

   public ItemGroup(String text) {
      this.name = text;
   }
}
