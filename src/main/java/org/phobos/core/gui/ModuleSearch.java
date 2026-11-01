package org.phobos.core.gui;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.phobos.module.Module;
import org.phobos.module.client.UIModule;
import org.phobos.core.setting.Setting;
import org.phobos.core.setting.StringSetting;

public class ModuleSearch {
   public static final Setting<String> search = new StringSetting("Search", "");

   public static boolean isEmpty() {
      return describe().isEmpty();
   }

   public static String describe() {
      return search.getValue();
   }

   public static boolean testModule(Module module, ModuleElement moduleElement) {
      if (moduleElement != null) {
         moduleElement.setFlag5(false);
      }

      if (!UIModule.check()) {
         return true;
      }

      String text = describe();
      if (text.isEmpty()) {
         return true;
      }

      String text2 = text.toLowerCase();
      if (module.getName().toLowerCase().contains(text2)) {
         return true;
      }

      MutableBoolean mutableBoolean = new MutableBoolean();

      for (Setting<?> setting : module.getSettingList()) {
         if ((setting.getVisibility() == null || setting.getVisibility().isFlag8()) && setting.getName().toLowerCase().contains(text2)) {
            mutableBoolean.setTrue();
         }
      }

      if (mutableBoolean.isTrue()) {
         if (moduleElement != null) {
            moduleElement.setFlag5(true);
         }

         return true;
      } else {
         return false;
      }
   }

   public static Setting<String> getSearch() {
      return search;
   }

   public static boolean testText(String text, ModuleElement... moduleElements) {
      for (ModuleElement moduleElement : moduleElements) {
         moduleElement.setFlag5(false);
      }

      if (!UIModule.check()) {
         return true;
      }

      String text2 = describe();
      if (text2.isEmpty()) {
         return true;
      }

      String text3 = text2.toLowerCase();
      if (!text.toLowerCase().contains(text3)) {
         return false;
      }

      for (ModuleElement moduleElement2 : moduleElements) {
         moduleElement2.setFlag5(true);
      }

      return true;
   }
}
