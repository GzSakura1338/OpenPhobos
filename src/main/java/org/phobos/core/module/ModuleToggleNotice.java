package org.phobos.core.module;

import org.phobos.module.Module;

public class ModuleToggleNotice {
   public Module module;
   public boolean flag;

   public ModuleToggleNotice(Module module, boolean flag) {
      this.module = module;
      this.flag = flag;
   }

   public boolean isFlag() {
      return this.flag;
   }

   public Module getModule() {
      return this.module;
   }
}
