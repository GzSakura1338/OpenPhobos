package org.phobos.core.module;

import org.phobos.module.Module;

public class ModulePairHandle<A extends Module, B extends Module> {
   public final B module;
   public final A module2;

   public void handleComputable(ModulePairHandle.Computable<A, B> computable) {
      if (this.module2 != null && this.module != null) {
         computable.handleObject(this.module2, this.module);
      }
   }

   public ModulePairHandle(A module, B module2) {
      this.module2 = module;
      this.module = module2;
   }

   public void handleInvokable(ModuleHandle.Invokable invokable) {
      if (this.module2 == null || this.module == null) {
         invokable.run();
      }
   }

   public void handleInvokable2(ModuleHandle.Invokable invokable) {
      if (this.module2 == null && this.module == null) {
         invokable.run();
      }
   }

   public interface Computable<A, B> {
      void handleObject(A var1, B var2);
   }
}
