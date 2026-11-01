package org.phobos.core.module;

import java.util.ArrayList;
import java.util.List;

public class Categories {
   public static final List<Category> categoryList = new ArrayList<>(5);
   public static final Category combat = resolveCategory(new Category("Combat"));
   public static final Category misc = resolveCategory(new Category("Misc"));
   public static final Category render = resolveCategory(new Category("Render"));
   public static final Category movement = resolveCategory(new Category("Movement"));
   public static final Category player = resolveCategory(new Category("Player"));
   public static final Category client = resolveCategory(new Category("Client"));

   public static <C extends Category> C resolveCategory(C category) {
      categoryList.add(category);
      return category;
   }

   public static List<Category> getCategoryList() {
      return categoryList;
   }

   public static void handleList(List<Category> list) {
      list.addAll(categoryList);
   }
}
