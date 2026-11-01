package org.phobos.core.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.phobos.module.Module;

public class ModuleGroups {
   public final Map<String, List<Module>> textMap = new HashMap<>();
   public static final ModuleGroups INSTANCE = new ModuleGroups();
   public boolean flag = false;

   public Map<String, List<Module>> getTextMap() {
      return this.textMap;
   }

   public void handleText(String text, Module module) {
      List<Module> list = this.textMap.get(text);
      if (list == null) {
         this.textMap.put(text, new ArrayList<>(List.of(module)));
      } else {
         list.add(module);
      }
   }

   public boolean isFlag() {
      return this.flag;
   }

   public void setFlag(boolean flag) {
      this.flag = flag;
   }

   public List<Module> resolveList(String text) {
      return this.textMap.getOrDefault(text, List.of());
   }

   public static ModuleGroups getINSTANCE() {
      return INSTANCE;
   }
}
